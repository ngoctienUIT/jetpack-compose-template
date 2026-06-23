"""Shared helpers for brand rebrand scripts."""

from __future__ import annotations

import os
import re
import shutil
from dataclasses import dataclass
from pathlib import Path


def repo_root() -> Path:
    return Path(__file__).resolve().parents[2]


def load_properties(path: Path) -> dict[str, str]:
    props: dict[str, str] = {}
    if not path.exists():
        return props
    for raw_line in path.read_text(encoding="utf-8").splitlines():
        line = raw_line.strip()
        if not line or line.startswith("#"):
            continue
        if "=" not in line:
            continue
        key, value = line.split("=", 1)
        props[key.strip()] = value.strip()
    return props


def resolve_brand_config(root: Path) -> dict[str, str]:
    brand_file = root / "config" / "brand.properties"
    example_file = root / "config" / "brand.properties.example"
    source = brand_file if brand_file.exists() else example_file
    if not source.exists():
        raise FileNotFoundError(
            "Missing config/brand.properties. Run ./scripts/brand init first."
        )
    return load_properties(source)


@dataclass(frozen=True)
class BrandSettings:
    legacy_application_id: str
    application_id: str
    legacy_application_class: str
    application_class: str
    gradle_project_name: str
    app_name_production: str
    app_name_staging: str
    staging_application_id_suffix: str
    staging_version_name_suffix: str
    primary_color: str
    primary_dark_color: str
    background_color_light: str
    splash_background_color: str
    splash_delay_ms: str
    image_disk_cache_dir: str
    legacy_theme_name: str
    theme_name: str

    @classmethod
    def from_props(cls, props: dict[str, str]) -> BrandSettings:
        def req(key: str, default: str = "") -> str:
            value = props.get(key, default).strip()
            if not value:
                raise ValueError(f"Missing required brand config key: {key}")
            return value

        return cls(
            legacy_application_id=req("legacy.application_id"),
            application_id=req("application_id"),
            legacy_application_class=req("legacy.application_class"),
            application_class=req("application_class"),
            gradle_project_name=req("gradle_project_name"),
            app_name_production=req("app_name_production"),
            app_name_staging=req("app_name_staging"),
            staging_application_id_suffix=props.get("staging_application_id_suffix", ".staging"),
            staging_version_name_suffix=props.get("staging_version_name_suffix", "-staging"),
            primary_color=req("primary_color", "#0061A4"),
            primary_dark_color=req("primary_dark_color", "#00497D"),
            background_color_light=req("background_color_light", "#FDFCFF"),
            splash_background_color=props.get("splash_background_color", "#FDFCFF"),
            splash_delay_ms=props.get("splash_delay_ms", "1500"),
            image_disk_cache_dir=props.get("image_disk_cache_dir", "image_cache"),
            legacy_theme_name=req("legacy.theme_name"),
            theme_name=req("theme_name"),
        )

    @property
    def legacy_package_path(self) -> Path:
        return Path(*self.legacy_application_id.split("."))

    @property
    def package_path(self) -> Path:
        return Path(*self.application_id.split("."))

    @property
    def staging_application_id(self) -> str:
        return self.application_id + self.staging_application_id_suffix

    @property
    def identity_changed(self) -> bool:
        return (
            self.legacy_application_id != self.application_id
            or self.legacy_application_class != self.application_class
            or self.legacy_theme_name != self.theme_name
        )


class DryRunWriter:
    def __init__(self, dry_run: bool) -> None:
        self.dry_run = dry_run

    def log(self, message: str) -> None:
        prefix = "[dry-run] " if self.dry_run else ""
        print(f"{prefix}{message}")

    def write_text(self, path: Path, content: str) -> None:
        self.log(f"write {path}")
        if not self.dry_run:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_text(content, encoding="utf-8")

    def copy(self, source: Path, destination: Path) -> None:
        self.log(f"copy {source} -> {destination}")
        if not self.dry_run:
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, destination)

    def move_tree(self, source: Path, destination: Path) -> None:
        self.log(f"move {source} -> {destination}")
        if not self.dry_run:
            destination.parent.mkdir(parents=True, exist_ok=True)
            if destination.exists():
                shutil.rmtree(destination)
            shutil.move(str(source), str(destination))

    def rename(self, source: Path, destination: Path) -> None:
        self.log(f"rename {source} -> {destination}")
        if not self.dry_run:
            source.rename(destination)


def replace_in_file(path: Path, replacements: dict[str, str], writer: DryRunWriter) -> bool:
    if not path.is_file():
        return False
    try:
        original = path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        return False
    updated = original
    for old, new in replacements.items():
        updated = updated.replace(old, new)
    if updated != original:
        writer.log(f"replace in {path}")
        if not writer.dry_run:
            path.write_text(updated, encoding="utf-8")
        return True
    return False


def walk_replace(root: Path, replacements: dict[str, str], writer: DryRunWriter) -> list[Path]:
    changed: list[Path] = []
    skip_dirs = {".git", "build", ".gradle", ".idea", "__pycache__"}
    for dirpath, dirnames, filenames in os.walk(root):
        dirnames[:] = [d for d in dirnames if d not in skip_dirs]
        for filename in filenames:
            path = Path(dirpath) / filename
            if path.suffix in {".png", ".jpg", ".jpeg", ".webp", ".keystore", ".jks"}:
                continue
            if replace_in_file(path, replacements, writer):
                changed.append(path)
    return changed


def update_settings_gradle(root: Path, project_name: str, writer: DryRunWriter) -> None:
    path = root / "settings.gradle.kts"
    content = path.read_text(encoding="utf-8")
    updated = re.sub(
        r'rootProject\.name\s*=\s*"[^"]*"',
        f'rootProject.name = "{project_name}"',
        content,
    )
    writer.write_text(path, updated)

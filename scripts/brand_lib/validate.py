"""Validate brand configuration and project consistency."""

from __future__ import annotations

import json
import sys
from pathlib import Path

from utils import BrandSettings, repo_root, resolve_brand_config


def _read_firebase_package(json_path: Path) -> str | None:
    if not json_path.exists():
        return None
    data = json.loads(json_path.read_text(encoding="utf-8"))
    clients = data.get("client") or []
    if not clients:
        return None
    return clients[0].get("client_info", {}).get("android_client_info", {}).get("package_name")


def validate(force: bool = False) -> int:
    root = repo_root()
    props = resolve_brand_config(root)
    brand = BrandSettings.from_props(props)

    errors: list[str] = []
    warnings: list[str] = []

    brand_file = root / "config" / "brand.properties"
    if not brand_file.exists():
        warnings.append(
            "config/brand.properties not found — using config/brand.properties.example. "
            "Run ./scripts/brand init for a project-specific config."
        )

    if brand.legacy_application_id == brand.application_id and not force:
        warnings.append(
            "application_id equals legacy.application_id — identity rebrand not applied yet."
        )

    for flavor, expected_id in [
        ("production", brand.application_id),
        ("staging", brand.staging_application_id),
    ]:
        firebase_path = root / "app" / "src" / flavor / "google-services.json"
        package_name = _read_firebase_package(firebase_path)
        if package_name is None:
            warnings.append(f"Missing or invalid {firebase_path}")
            continue
        if package_name != expected_id:
            errors.append(
                f"{firebase_path}: package_name '{package_name}' "
                f"!= expected '{expected_id}'"
            )

    for env_name in ("staging", "production"):
        env_path = root / "secrets" / f"env.{env_name}.properties"
        example_path = root / "secrets" / f"env.{env_name}.properties.example"
        if not env_path.exists() and not example_path.exists():
            errors.append(f"Missing secrets/env.{env_name}.properties(.example)")
        elif not env_path.exists():
            warnings.append(
                f"secrets/env.{env_name}.properties not found — build uses .example defaults"
            )

    icons_dir = root / "brand" / "assets" / "icons"
    splash_dir = root / "brand" / "assets" / "splash"
    if not any(icons_dir.glob("*")) if icons_dir.exists() else True:
        warnings.append("No custom icons in brand/assets/icons/ — using template defaults")
    if not any(splash_dir.glob("*")) if splash_dir.exists() else True:
        warnings.append("No custom splash assets in brand/assets/splash/ — using template defaults")

    for message in warnings:
        print(f"WARN: {message}")
    for message in errors:
        print(f"ERROR: {message}")

    if errors:
        return 1
    print("Brand validation passed.")
    return 0


if __name__ == "__main__":
    sys.exit(validate())

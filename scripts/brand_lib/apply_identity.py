"""Apply brand identity: package rename, class/theme renames, settings.gradle."""

from __future__ import annotations

import sys
from pathlib import Path

from utils import (
    BrandSettings,
    DryRunWriter,
    replace_in_file,
    repo_root,
    resolve_brand_config,
    update_settings_gradle,
    walk_replace,
)


def _move_package_tree(root: Path, brand: BrandSettings, writer: DryRunWriter) -> None:
    if brand.legacy_application_id == brand.application_id:
        return

    java_roots = [
        root / "app" / "src" / "main" / "java",
        root / "app" / "src" / "test" / "java",
        root / "app" / "src" / "androidTest" / "java",
    ]

    for java_root in java_roots:
        source = java_root / brand.legacy_package_path
        destination = java_root / brand.package_path
        if not source.exists():
            continue
        if source.resolve() == destination.resolve():
            continue
        writer.move_tree(source, destination)


def _rename_application_class(root: Path, brand: BrandSettings, writer: DryRunWriter) -> None:
    if brand.legacy_application_class == brand.application_class:
        return

    java_root = root / "app" / "src" / "main" / "java"
    legacy_file = java_root / brand.package_path / f"{brand.legacy_application_class}.kt"
    new_file = java_root / brand.package_path / f"{brand.application_class}.kt"

    if legacy_file.exists() and legacy_file != new_file:
        writer.rename(legacy_file, new_file)


def apply_identity(dry_run: bool = False, force: bool = False) -> int:
    root = repo_root()
    props = resolve_brand_config(root)
    brand = BrandSettings.from_props(props)
    writer = DryRunWriter(dry_run=dry_run)

    if not brand.identity_changed and not force:
        print(
            "Identity unchanged (application_id/class/theme match legacy values). "
            "Edit config/brand.properties or pass --force."
        )
        return 0

    replacements = {
        brand.legacy_application_id: brand.application_id,
        brand.legacy_application_class: brand.application_class,
        brand.legacy_theme_name: brand.theme_name,
        f"{brand.legacy_theme_name}.Main": f"{brand.theme_name}.Main",
    }

    _move_package_tree(root, brand, writer)
    _rename_application_class(root, brand, writer)

    changed: list[Path] = []
    replace_targets = [
        root / "app",
        root / "settings.gradle.kts",
    ]
    for target in replace_targets:
        if target.is_file():
            if replace_in_file(target, replacements, writer):
                changed.append(target)
        elif target.is_dir():
            changed.extend(walk_replace(target, replacements, writer))
    update_settings_gradle(root, brand.gradle_project_name, writer)

    print(f"Identity apply complete. {len(changed)} file(s) updated.")
    return 0


if __name__ == "__main__":
    dry_run_flag = "--dry-run" in sys.argv
    force_flag = "--force" in sys.argv
    sys.exit(apply_identity(dry_run=dry_run_flag, force=force_flag))

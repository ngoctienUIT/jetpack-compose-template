#!/usr/bin/env python3
"""Brand CLI entry point invoked by scripts/brand and Gradle tasks."""

from __future__ import annotations

import shutil
import sys
from pathlib import Path

BRAND_LIB_DIR = Path(__file__).resolve().parent
sys.path.insert(0, str(BRAND_LIB_DIR))

from apply_colors import apply_colors
from apply_firebase import apply_firebase
from apply_icons import apply_icons
from apply_identity import apply_identity
from apply_splash import apply_splash
from utils import repo_root
from validate import validate


def cmd_init() -> int:
    root = repo_root()
    brand_dest = root / "config" / "brand.properties"
    brand_example = root / "config" / "brand.properties.example"

    if not brand_example.exists():
        print("ERROR: config/brand.properties.example not found.")
        return 1

    if brand_dest.exists():
        print("config/brand.properties already exists — skipping copy.")
    else:
        shutil.copy2(brand_example, brand_dest)
        print("Created config/brand.properties")

    for name in ("signing.properties", "env.staging.properties", "env.production.properties"):
        example = root / "secrets" / f"{name}.example"
        dest = root / "secrets" / name
        if example.exists() and not dest.exists():
            shutil.copy2(example, dest)
            print(f"Created secrets/{name}")

    asset_dirs = (
        root / "brand" / "assets" / "icons",
        root / "brand" / "assets" / "splash",
        root / "brand" / "assets" / "firebase" / "staging",
        root / "brand" / "assets" / "firebase" / "production",
    )
    for directory in asset_dirs:
        directory.mkdir(parents=True, exist_ok=True)
        print(f"Ensured {directory.relative_to(root)}/")

    print("Init complete. Edit config/brand.properties and add assets under brand/assets/.")
    return 0


def main(argv: list[str]) -> int:
    if len(argv) < 2:
        print("Usage: brand <init|validate|apply ...>")
        return 1

    command = argv[1]
    args = argv[2:]
    dry_run = "--dry-run" in args
    force = "--force" in args

    if command == "init":
        return cmd_init()
    if command == "validate":
        return validate(force=force)
    if command == "apply":
        if not args or args[0] == "all":
            steps = (
                lambda: apply_identity(dry_run=dry_run, force=force),
                lambda: apply_colors(dry_run=dry_run),
                lambda: apply_icons(dry_run=dry_run),
                lambda: apply_splash(dry_run=dry_run),
                lambda: apply_firebase(dry_run=dry_run),
            )
            for step in steps:
                code = step()
                if code != 0:
                    return code
            if not dry_run:
                return validate(force=True)
            return 0

        subcommand = args[0]
        if subcommand == "identity":
            return apply_identity(dry_run=dry_run, force=force)
        if subcommand == "colors":
            return apply_colors(dry_run=dry_run)
        if subcommand == "icons":
            return apply_icons(dry_run=dry_run)
        if subcommand == "splash":
            return apply_splash(dry_run=dry_run)
        if subcommand == "firebase":
            return apply_firebase(dry_run=dry_run)
        print(f"Unknown apply subcommand: {subcommand}")
        return 1

    print(f"Unknown command: {command}")
    return 1


if __name__ == "__main__":
    sys.exit(main(sys.argv))

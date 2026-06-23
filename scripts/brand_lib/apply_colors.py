"""Sync res/values/colors.xml from brand config."""

from __future__ import annotations

import sys

from utils import BrandSettings, DryRunWriter, repo_root, resolve_brand_config


def _normalize_color(color: str) -> str:
    value = color.strip()
    if not value.startswith("#"):
        value = f"#{value}"
    if len(value) == 7:
        return f"#FF{value[1:]}"
    return value


def apply_colors(dry_run: bool = False) -> int:
    root = repo_root()
    brand = BrandSettings.from_props(resolve_brand_config(root))
    writer = DryRunWriter(dry_run=dry_run)

    primary = _normalize_color(brand.primary_color)
    primary_dark = _normalize_color(brand.primary_dark_color)
    background = _normalize_color(brand.background_color_light)
    splash_background = _normalize_color(brand.splash_background_color)

    content = f"""<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">{primary}</color>
    <color name="primary_dark">{primary_dark}</color>
    <color name="background">{background}</color>
    <color name="surface">{background}</color>
    <color name="on_surface">#FF1A1C1E</color>
    <color name="splash_background">{splash_background}</color>
</resources>
"""
    colors_path = root / "app" / "src" / "main" / "res" / "values" / "colors.xml"
    writer.write_text(colors_path, content)
    print("Colors applied to app/src/main/res/values/colors.xml")
    return 0


if __name__ == "__main__":
    sys.exit(apply_colors(dry_run="--dry-run" in sys.argv))

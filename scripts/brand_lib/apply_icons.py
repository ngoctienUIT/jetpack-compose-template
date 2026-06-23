"""Copy or generate launcher icons from brand/assets/icons/."""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path

from utils import DryRunWriter, repo_root

ICON_DENSITIES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}


def _copy_xml_icons(source_dir: Path, res_dir: Path, writer: DryRunWriter) -> bool:
    copied = False
    drawable_names = {
        "ic_launcher_foreground.xml": res_dir / "drawable" / "ic_launcher_foreground.xml",
        "ic_launcher_background.xml": res_dir / "drawable" / "ic_launcher_background.xml",
        "ic_stat_notification.xml": res_dir / "drawable" / "ic_stat_notification.xml",
    }
    for name, destination in drawable_names.items():
        source = source_dir / name
        if source.exists():
            writer.copy(source, destination)
            copied = True
    return copied


def _generate_png_icons(source_dir: Path, res_dir: Path, writer: DryRunWriter) -> bool:
    foreground = source_dir / "ic_launcher_foreground.png"
    if not foreground.exists():
        return False

    if not shutil_which("sips"):
        print("WARN: 'sips' not found — skipping PNG mipmap generation. Provide XML icons instead.")
        return False

    generated = False
    for folder, size in ICON_DENSITIES.items():
        destination = res_dir / folder / "ic_launcher.png"
        writer.log(f"generate {destination} ({size}x{size})")
        if not writer.dry_run:
            destination.parent.mkdir(parents=True, exist_ok=True)
            subprocess.run(
                ["sips", "-z", str(size), str(size), str(foreground), "--out", str(destination)],
                check=True,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
            )
        generated = True

    background = source_dir / "ic_launcher_background.png"
    if background.exists():
        for folder, size in ICON_DENSITIES.items():
            destination = res_dir / folder / "ic_launcher_background.png"
            writer.log(f"generate {destination} ({size}x{size})")
            if not writer.dry_run:
                destination.parent.mkdir(parents=True, exist_ok=True)
                subprocess.run(
                    ["sips", "-z", str(size), str(size), str(background), "--out", str(destination)],
                    check=True,
                    stdout=subprocess.DEVNULL,
                    stderr=subprocess.DEVNULL,
                )
    return generated


def shutil_which(command: str) -> bool:
    from shutil import which

    return which(command) is not None


def apply_icons(dry_run: bool = False) -> int:
    root = repo_root()
    writer = DryRunWriter(dry_run=dry_run)
    source_dir = root / "brand" / "assets" / "icons"
    res_dir = root / "app" / "src" / "main" / "res"

    if not source_dir.exists() or not any(source_dir.iterdir()):
        print("No icons found in brand/assets/icons/ — skipping.")
        return 0

    xml_applied = _copy_xml_icons(source_dir, res_dir, writer)
    png_applied = _generate_png_icons(source_dir, res_dir, writer)

    if xml_applied or png_applied:
        print("Icons applied.")
    else:
        print("No supported icon assets found (expected XML or PNG in brand/assets/icons/).")
    return 0


if __name__ == "__main__":
    sys.exit(apply_icons(dry_run="--dry-run" in sys.argv))

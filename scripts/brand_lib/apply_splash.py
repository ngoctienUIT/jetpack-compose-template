"""Copy splash logo assets from brand/assets/splash/."""

from __future__ import annotations

import sys
from pathlib import Path

from utils import DryRunWriter, repo_root

SPLASH_CANDIDATES = (
    "splash_logo.png",
    "splash_icon.png",
    "splash_logo.xml",
    "splash_icon.xml",
)


def apply_splash(dry_run: bool = False) -> int:
    root = repo_root()
    writer = DryRunWriter(dry_run=dry_run)
    source_dir = root / "brand" / "assets" / "splash"
    drawable_dir = root / "app" / "src" / "main" / "res" / "drawable"

    if not source_dir.exists():
        print("No brand/assets/splash/ directory — skipping.")
        return 0

    copied = False
    for candidate in SPLASH_CANDIDATES:
        source = source_dir / candidate
        if not source.exists():
            continue
        extension = source.suffix
        destination = drawable_dir / f"splash_icon{extension}"
        writer.copy(source, destination)
        copied = True
        break

    if copied:
        print("Splash icon copied to res/drawable/splash_icon.*")
    else:
        print("No splash assets found in brand/assets/splash/ — skipping.")
    return 0


if __name__ == "__main__":
    sys.exit(apply_splash(dry_run="--dry-run" in sys.argv))

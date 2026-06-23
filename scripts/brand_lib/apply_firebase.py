"""Copy Firebase google-services.json from brand/assets/firebase/."""

from __future__ import annotations

import sys

from utils import DryRunWriter, repo_root


def apply_firebase(dry_run: bool = False) -> int:
    root = repo_root()
    writer = DryRunWriter(dry_run=dry_run)
    source_root = root / "brand" / "assets" / "firebase"
    copied = False

    for flavor in ("staging", "production"):
        source = source_root / flavor / "google-services.json"
        if not source.exists():
            continue
        destination = root / "app" / "src" / flavor / "google-services.json"
        writer.copy(source, destination)
        copied = True

    if copied:
        print("Firebase google-services.json files copied.")
    else:
        print("No Firebase configs in brand/assets/firebase/{staging,production}/ — skipping.")
    return 0


if __name__ == "__main__":
    sys.exit(apply_firebase(dry_run="--dry-run" in sys.argv))

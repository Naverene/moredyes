#!/usr/bin/env python3
"""
Cut chest atlas textures into per-face images.

Default behaviour: treat each atlas as a grid (cols x rows) and map cell indices to names
provided in the layout JSON. This is a safe, non-destructive tool — it will not overwrite
existing output files unless you pass --overwrite.

Usage:
  ./scripts/cut_chest_textures.py [--src SRC_DIR] [--out OUT_BASE] [--layout LAYOUT_JSON] [--overwrite]

Defaults (project-relative):
  SRC_DIR = src/main/resources/assets/moredyes/textures/blocks/chest/normal
  OUT_BASE = src/main/resources/assets/moredyes/textures/blocks
  LAYOUT_JSON = scripts/chest_layouts/default64.json

The default layout uses a 4x2 grid and names: front, back, left, right, top, bottom, lid, extra
"""

import os
import sys
import json
import glob
from pathlib import Path

DEFAULT_SRC = "src/main/resources/assets/moredyes/textures/blocks/chest/normal"
DEFAULT_OUT = "src/main/resources/assets/moredyes/textures/blocks"
DEFAULT_LAYOUT = "scripts/chest_layouts/default64.json"


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


def load_layout(path):
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)


def ensure_pillow():
    try:
        from PIL import Image
        return Image
    except Exception:
        return None


def main(argv):
    import argparse
    p = argparse.ArgumentParser()
    p.add_argument("--src", default=DEFAULT_SRC)
    p.add_argument("--out", default=DEFAULT_OUT)
    p.add_argument("--layout", default=DEFAULT_LAYOUT)
    p.add_argument("--overwrite", action="store_true")
    args = p.parse_args(argv)

    Image = ensure_pillow()
    if Image is None:
        eprint("Pillow is required but not installed. Install it with: pip3 install --user pillow")
        return 2

    layout_path = Path(args.layout)
    if not layout_path.exists():
        eprint(f"Layout file not found: {layout_path}")
        return 2

    layout = load_layout(layout_path)
    cols = int(layout.get("cols", 4))
    rows = int(layout.get("rows", 2))
    names = layout.get("names", [])

    src_dir = Path(args.src)
    if not src_dir.exists():
        eprint(f"Source dir not found: {src_dir}")
        return 2

    out_base = Path(args.out)
    out_base.mkdir(parents=True, exist_ok=True)

    pattern = str(src_dir / "*.png")
    files = sorted(glob.glob(pattern))
    if not files:
        eprint("No PNG files found in source dir:", src_dir)
        return 1

    created = []
    skipped = []
    for fpath in files:
        img = Image.open(fpath).convert("RGBA")
        w, h = img.size
        cell_w = w // cols
        cell_h = h // rows
        name_base = Path(fpath).stem
        out_dir = out_base / name_base
        out_dir.mkdir(parents=True, exist_ok=True)

        total_cells = cols * rows
        for idx in range(total_cells):
            if idx >= len(names):
                # skip unnamed cells
                continue
            cell_name = names[idx]
            col = idx % cols
            row = idx // cols
            left = col * cell_w
            upper = row * cell_h
            right = left + cell_w
            lower = upper + cell_h
            box = (left, upper, right, lower)
            out_file = out_dir / (cell_name + ".png")
            if out_file.exists() and not args.overwrite:
                skipped.append(str(out_file))
                continue
            cell = img.crop(box)
            cell.save(out_file)
            created.append(str(out_file))

    print("Processed", len(files), "source images")
    print("Created files:", len(created))
    for x in created[:200]:
        print(" +", x)
    if skipped:
        print("Skipped existing files:", len(skipped))
    return 0


if __name__ == '__main__':
    raise SystemExit(main(sys.argv[1:]))


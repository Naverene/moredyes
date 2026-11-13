#!/usr/bin/env python3
"""Rewrites the NEI collapsible item groups in README.md for the shared dyed block item.

Every dyed block is one item, moredyes:dyedBlock, whose damage is kind * 118 + color (see item/SharedBlocks.java),
so each group of a block kind matches that item's damage range. Rerun after adding a kind to SharedBlocks.KINDS:

    python3 tools/make_nei_groups.py

It rewrites the filter line after each `; {"displayName":...}` line whose block is in KINDS, written either as
the old registry-name filters (r/^moredyes:[a-z]+MixWool$/, r/^moredyes:lilac$/) or as a range written by this script
(with a `# kind` comment naming the kind).
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SHARED = ROOT / "src/main/java/info/kg6jay/moredyes/item/SharedBlocks.java"
README = ROOT / "README.md"
COLORS = 118

src = SHARED.read_text()
body = src[src.index("KINDS = {"):]
body = body[:body.index("};")]
kinds = re.findall(r'"(\w+)"', body)
per_item = 32766 // COLORS

def line_for(kind):
    i = kinds.index(kind)
    item = "dyedBlock" if i // per_item == 0 else "dyedBlock%d" % (i // per_item)
    start = i % per_item * COLORS
    return "r/^moredyes:%s$/ %d-%d" % (item, start, start + COLORS - 1)

old_set = re.compile(r"^r/\^moredyes:\[a-z\]\+Mix(\w+)\$/$")
old_one = re.compile(r"^r/\^moredyes:(\w+)\$/$")
new = re.compile(r"^r/\^moredyes:dyedBlock\d*\$/ (\d+)-\d+$")

out, changed = [], 0
lines = README.read_text().split("\n")
for n, line in enumerate(lines):
    kind = None
    m = old_set.match(line) or old_one.match(line)
    if m and m.group(1) in kinds:
        kind = m.group(1)
    m = new.match(line)
    if m:
        item = re.match(r"^r/\^moredyes:dyedBlock(\d*)", line).group(1)
        index = (int(item) if item else 0) * per_item + int(m.group(1)) // COLORS
        kind = kinds[index]
    if kind:
        line = line_for(kind)
        changed += 1
    out.append(line)
README.write_text("\n".join(out))
print("rewrote", changed, "group filters")

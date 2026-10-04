#!/usr/bin/env python3
"""
Writes data/forge/tags/items/dyes/<color>.json, which put every More Dyes dye in the Forge tag of the vanilla dye
color it looks closest to (CIEDE2000 distance). Mods that read a dye's color from those tags then accept ours,
for example Ender Storage, whose frequency buttons only know the 16 vanilla colors. api/NearestColor.java finds the
same color in code.

Also writes data/moredyes/tags/items/dyes.json, which holds every dye (MoreDyesAPI.DYES).

Run from anywhere: python3 tools/make_dye_tags.py
"""
import json
import math
import os
import re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
COLORS = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes/reference/ColorStrings.java')
RESOURCES = os.path.join(ROOT, 'src/main/resources')
OUT = os.path.join(RESOURCES, 'data/forge/tags/items/dyes')

# Vanilla DyeColor texture colors.
VANILLA = {
    'white': 0xF9FFFE, 'orange': 0xF9801D, 'magenta': 0xC74EBD, 'light_blue': 0x3AB3DA,
    'yellow': 0xFED83D, 'lime': 0x80C71F, 'pink': 0xF38BAA, 'gray': 0x474F52,
    'light_gray': 0x9D9D97, 'cyan': 0x169C9C, 'purple': 0x8932B8, 'blue': 0x3C44AA,
    'brown': 0x835432, 'green': 0x5E7C16, 'red': 0xB02E26, 'black': 0x1D1D21,
}


def lab(rgb):
    def linear(u):
        return ((u + 0.055) / 1.055) ** 2.4 if u > 0.04045 else u / 12.92
    r, g, b = (linear(((rgb >> shift) & 255) / 255) for shift in (16, 8, 0))
    x = (0.4124 * r + 0.3576 * g + 0.1805 * b) / 0.95047
    y = 0.2126 * r + 0.7152 * g + 0.0722 * b
    z = (0.0193 * r + 0.1192 * g + 0.9505 * b) / 1.08883

    def f(t):
        return t ** (1 / 3) if t > 0.008856 else 7.787 * t + 16 / 116
    return 116 * f(y) - 16, 500 * (f(x) - f(y)), 200 * (f(y) - f(z))


def ciede2000(c1, c2):
    l1, a1, b1 = c1
    l2, a2, b2 = c2
    cbar = (math.hypot(a1, b1) + math.hypot(a2, b2)) / 2
    g = 0.5 * (1 - math.sqrt(cbar ** 7 / (cbar ** 7 + 25 ** 7)))
    a1p, a2p = a1 * (1 + g), a2 * (1 + g)
    c1p, c2p = math.hypot(a1p, b1), math.hypot(a2p, b2)
    h1 = math.degrees(math.atan2(b1, a1p)) % 360
    h2 = math.degrees(math.atan2(b2, a2p)) % 360
    dl, dc = l2 - l1, c2p - c1p
    if c1p * c2p == 0:
        dh = 0
    elif abs(h2 - h1) <= 180:
        dh = h2 - h1
    else:
        dh = h2 - h1 - 360 if h2 > h1 else h2 - h1 + 360
    dhh = 2 * math.sqrt(c1p * c2p) * math.sin(math.radians(dh / 2))
    lbar, cbarp = (l1 + l2) / 2, (c1p + c2p) / 2
    if c1p * c2p == 0:
        hbar = h1 + h2
    elif abs(h1 - h2) <= 180:
        hbar = (h1 + h2) / 2
    else:
        hbar = (h1 + h2 + 360) / 2 if h1 + h2 < 360 else (h1 + h2 - 360) / 2
    t = (1 - 0.17 * math.cos(math.radians(hbar - 30)) + 0.24 * math.cos(math.radians(2 * hbar))
         + 0.32 * math.cos(math.radians(3 * hbar + 6)) - 0.2 * math.cos(math.radians(4 * hbar - 63)))
    sl = 1 + 0.015 * (lbar - 50) ** 2 / math.sqrt(20 + (lbar - 50) ** 2)
    sc = 1 + 0.045 * cbarp
    sh = 1 + 0.015 * cbarp * t
    rt = (-2 * math.sqrt(cbarp ** 7 / (cbarp ** 7 + 25 ** 7))
          * math.sin(math.radians(60 * math.exp(-((hbar - 275) / 25) ** 2))))
    return math.sqrt((dl / sl) ** 2 + (dc / sc) ** 2 + (dhh / sh) ** 2 + rt * (dc / sc) * (dhh / sh))


def write(path, values):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w') as f:
        json.dump({'replace': False, 'values': values}, f, indent=2)
        f.write('\n')


def main():
    source = open(COLORS).read()
    colors = re.findall(r'"([0-9a-f]{6})"', re.search(r'\bALL\s*=.*?;', source, re.S).group(0))
    vanilla = {name: lab(rgb) for name, rgb in VANILLA.items()}
    tags = defaultdict(list)
    for color in sorted(colors):
        nearest = min(vanilla, key=lambda name: ciede2000(lab(int(color, 16)), vanilla[name]))
        tags[nearest].append('moredyes:%s_dye' % color)
    for name, values in sorted(tags.items()):
        write(os.path.join(OUT, name + '.json'), values)
    every = ['moredyes:%s_dye' % color for color in colors]
    write(os.path.join(RESOURCES, 'data/moredyes/tags/items/dyes.json'), every)
    print('%d dyes in %d tags' % (len(colors), len(tags)))


if __name__ == '__main__':
    main()

#!/usr/bin/env python3
"""Writes the Rechiseled chiseling groups of the dyed blocks.

Rechiseled (the chisel mod by SuperMartijn642) reads every installed mod's
assets/<namespace>/chiseling_recipes/*.json, on the client and the server, and names each file
after the mod that ships it, so these files become the groups moredyes:<file name>. Rechiseled
then merges groups that share a block. So every file starts with the vanilla block(s) the dyed
shades wash back into: where Rechiseled has a group for that block (stone, cobblestone, ...) the
dyed shades join it, otherwise the file makes a group of its own holding the vanilla block. A
vanilla block is listed in one file only, so no two of these groups merge into each other.

These are plain resource files, so nothing happens when Rechiseled isn't installed. The Chisel
mod is handled in code instead (compat/ChiselCompat.java), with the same groups.

The shades of each color group are read from ColorStrings.java. Run again after changing it:
    python3 tools/make_chisel_recipes.py
"""
import glob
import json
import os
import re

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
COLOR_STRINGS = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes/reference/ColorStrings.java')
RECIPES = os.path.join(ROOT, 'src/main/resources/assets/moredyes/chiseling_recipes')

# file name -> (vanilla blocks as (id, meta), dyed block types as in Reference.BLOCK_INFO_*)
# The first part of the list are groups Rechiseled 1.2.6 for 1.12.2 already has: the vanilla blocks
# listed are in its group, so the dyed shades join it.
GROUPS = {
    # Rechiseled's stone group also has stone bricks, mossy and cracked; carved stone bricks join it.
    'stone': ([('minecraft:stone', 0), ('minecraft:stonebrick', 0), ('minecraft:stonebrick', 2),
               ('minecraft:stonebrick', 3)],
              ['stone', 'stonebrick', 'stonebrick_cracked', 'stonebrick_carved']),
    'cobblestone': ([('minecraft:cobblestone', 0)], ['cobble']),
    'diorite': ([('minecraft:stone', 3)], ['diorite']),
    'obsidian': ([('minecraft:obsidian', 0)], ['obsidian']),
    'quartz_block': ([('minecraft:quartz_block', 0)], ['quartz']),
    'coal_block': ([('minecraft:coal_block', 0)], ['coal']),
    'glowstone': ([('minecraft:glowstone', 0)], ['glowstone']),
    'lapis_block': ([('minecraft:lapis_block', 0)], ['lapis']),
    'redstone_block': ([('minecraft:redstone_block', 0)], ['redstone']),
    'sandstone': ([('minecraft:sandstone', 0)], ['sandstone']),
    'oak_planks': ([('minecraft:planks', 0)], ['plank']),
    # Groups of our own.
    'wool': ([('minecraft:wool', 0)], ['wool']),
    'soul_sand': ([('minecraft:soul_sand', 0)], ['soulsand']),
    'hardened_clay': ([('minecraft:hardened_clay', 0)], ['hardened_clay']),
    'stained_hardened_clay': ([('minecraft:stained_hardened_clay', 0)], ['clay']),
    'brick_block': ([('minecraft:brick_block', 0)], ['brick']),
    'sand': ([('minecraft:sand', 0)], ['sand']),
    'glass': ([('minecraft:glass', 0)], ['glass']),
    'glass_pane': ([('minecraft:glass_pane', 0)], ['glass_pane']),
    'bookshelf': ([('minecraft:bookshelf', 0)], ['bookshelf']),
    'chest': ([('minecraft:chest', 0)], ['chest']),
}

# Dyed block types with one block per color (named by its hex code) instead of one per color group.
SINGLE_COLOR = {'chest'}


def read_colors():
    """The color groups, as (name, shades), from ColorStrings.java."""
    with open(COLOR_STRINGS) as f:
        source = f.read()
    arrays = dict(re.findall(r'String\[\]\s+(\w+)=\s*new String\[\]\{([^}]*)\}', source))
    shades = {name: re.findall(r'"(\w+)"', body) for name, body in arrays.items()}
    order = re.search(r'GROUPS=\s*new String\[\]\[\]\{([^}]*)\}', source).group(1).replace(' ', '').split(',')
    names = shades['GROUP_NAMES']
    return [(names[i], shades[group]) for i, group in enumerate(order)]


def entry(block, meta):
    # Rechiseled refuses a meta for an item without subtypes (such as a group with one shade).
    if meta == 0:
        return {'block': block}
    return {'block': block, 'block_meta': meta}


def main():
    colors = read_colors()
    for old in glob.glob(os.path.join(RECIPES, '*.json')):
        os.remove(old)
    os.makedirs(RECIPES, exist_ok=True)
    for name, (vanilla, dyed) in GROUPS.items():
        entries = [entry(block, meta) for block, meta in vanilla]
        for kind in dyed:
            for group, shades in colors:
                if kind in SINGLE_COLOR:
                    entries += [entry('moredyes:%s_%s' % (kind, shade), 0) for shade in shades]
                else:
                    entries += [entry('moredyes:%s_%s' % (kind, group), meta) for meta in range(len(shades))]
        data = {'type': 'rechiseled:chiseling', 'overwrite': False, 'entries': entries}
        with open(os.path.join(RECIPES, name + '.json'), 'w') as f:
            json.dump(data, f, indent=2)
            f.write('\n')
    print('wrote %d chiseling groups' % len(GROUPS))


if __name__ == '__main__':
    main()

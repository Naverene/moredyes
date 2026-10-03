#!/usr/bin/env python3
"""Writes the resources of the optional compat blocks.

- Dyed Iron Chests (compat/ironchest): a blockstate, a block model that only gives the breaking particles (the chest
  is drawn by a renderer from Iron Chests' own texture, split at run time) and an item model per tier.
- Dyed Storage Drawers (compat/storagedrawers): grey drawer textures made from Storage Drawers' oak drawers (MIT
  licensed, (c) jaquadro / Texelsaur), Storage Drawers' two drawer shapes with every face marked for tinting, and the
  models and blockstate of the dyed drawer block. Making them needs the Storage Drawers jar, which ./gradlew build
  downloads.

Their recipes are made in code (IronChestCompat, StorageDrawersCompat) and their names are in lang/en_us.lang.

Run from anywhere (needs Pillow): python3 tools/make_compat_resources.py
"""
import glob
import io
import json
import os
import re
import sys
import zipfile

from PIL import Image

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from make_assets import grey  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes')
ASSETS = os.path.join(ROOT, 'src/main/resources/assets/moredyes')


def write(path, data):
    path = os.path.join(ASSETS, path)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w') as f:
        json.dump(data, f, indent=2)
        f.write('\n')


def reference_list(name):
    source = open(os.path.join(JAVA, 'reference/Reference.java')).read()
    return re.findall(r'"(\w+)"', re.search(name + r'\s*=.*?;', source, re.S).group(0))


def gradle_property(name):
    source = open(os.path.join(ROOT, 'gradle.properties')).read()
    return re.search(r'^%s=(.*)$' % name, source, re.M).group(1).strip()


def iron_chests():
    tiers = reference_list('IRON_CHEST_TIERS')
    for tier in tiers:
        name = 'dyed_%s_chest' % tier
        # Iron Chests has no breaking texture of its own for obsidian, and uses the obsidian block's.
        particle = 'minecraft:blocks/obsidian' if tier == 'obsidian' else 'ironchest:blocks/%s_break' % tier
        write('blockstates/%s.json' % name, {'variants': {'normal': {'model': 'moredyes:' + name}}})
        write('models/block/%s.json' % name, {'textures': {'particle': particle}})
        # Like the vanilla chest item, the item hands the drawing to the chest renderer.
        write('models/item/%s.json' % name, {'parent': 'minecraft:item/chest'})
    print('wrote dyed Iron Chests resources for %d tiers' % len(tiers))


# Grey drawer texture -> Storage Drawers' oak texture it is made from.
DRAWER_TEXTURES = {'drawers_front_1': 'drawers_oak_front_1', 'drawers_front_2': 'drawers_oak_front_2',
                   'drawers_front_4': 'drawers_oak_front_4', 'drawers_side': 'drawers_oak_side',
                   'drawers_side_h': 'drawers_oak_side_h', 'drawers_side_v': 'drawers_oak_side_v',
                   'drawers_trim': 'drawers_oak_trim'}


def storage_drawers_jar():
    # The Gradle cache holds the jar of every Storage Drawers version any build fetched, so pick this folder's by
    # its CurseForge file id rather than by name.
    file_id = gradle_property('storage_drawers_file')
    pattern = '~/.gradle/caches/modules-2/files-2.1/curse.maven/storage-drawers-223852/%s/*/storage-drawers-223852-%s.jar'
    jars = glob.glob(os.path.expanduser(pattern % (file_id, file_id)))
    if not jars:
        sys.exit('Storage Drawers jar (file %s) not found; run ./gradlew build first.' % file_id)
    return zipfile.ZipFile(jars[0])


def tinted(model):
    """A copy of a Storage Drawers block model with every face marked for tinting."""
    for element in model['elements']:
        element.pop('__comment', None)
        for face in element['faces'].values():
            face['tintindex'] = 0
    return model


def storage_drawers():
    sizes = reference_list('DRAWER_SIZES')
    with storage_drawers_jar() as jar:
        for name, source in DRAWER_TEXTURES.items():
            image = Image.open(io.BytesIO(jar.read('assets/storagedrawers/textures/blocks/%s.png' % source)))
            image = image.convert('RGBA')
            pixels = image.load()
            for y in range(image.height):
                for x in range(image.width):
                    pixels[x, y] = grey(pixels[x, y])
            path = os.path.join(ASSETS, 'textures/blocks/tinted', name + '.png')
            os.makedirs(os.path.dirname(path), exist_ok=True)
            image.save(path)
        for shape in ('drawer_base', 'drawer_half'):
            model = json.loads(jar.read('assets/storagedrawers/models/block/%s.json' % shape))
            write('models/block/drawers/%s.json' % shape, tinted(model))

    t = 'moredyes:blocks/tinted/'
    variants = {}
    for size in sizes:
        count = size[-1]
        name = 'dyed_drawers_' + size
        # As Storage Drawers' basicdrawers_<size>_oak.json.
        if size.startswith('full'):
            model = {'parent': 'moredyes:block/drawers/drawer_base',
                     'textures': {'particle': t + 'drawers_front_' + count, 'north': t + 'drawers_front_' + count,
                                  'down': t + 'drawers_side', 'up': t + 'drawers_side', 'east': t + 'drawers_side',
                                  'south': t + 'drawers_side', 'west': t + 'drawers_side', 'trim': t + 'drawers_trim'}}
        else:
            model = {'parent': 'moredyes:block/drawers/drawer_half',
                     'textures': {'top': t + 'drawers_side_h', 'front': t + 'drawers_front_' + count,
                                  'back': t + 'drawers_side', 'side': t + 'drawers_side_v', 'trim': t + 'drawers_trim'}}
        write('models/block/%s.json' % name, model)
        write('models/item/%s.json' % name, {'parent': 'moredyes:block/' + name})
        for facing, y in (('north', 0), ('east', 90), ('south', 180), ('west', 270)):
            variant = {'model': 'moredyes:' + name}
            if y:
                variant['y'] = y
            variants['block=%s,facing=%s' % (size, facing)] = variant
    write('blockstates/dyed_drawers.json', {'variants': variants})
    print('wrote dyed Storage Drawers resources for %d sizes' % len(sizes))


def main():
    iron_chests()
    storage_drawers()


if __name__ == '__main__':
    main()

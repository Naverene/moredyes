#!/usr/bin/env python3
"""
Writes the resources of the optional compat blocks, which data generation can't make because the other mods' blocks
don't exist while it runs:

- Dyed Iron Chests (compat/ironchest): blockstates and item models that reuse Iron Chests' own, and a recipe per tier
  and color: an Iron Chests chest (or a dyed one of the same tier) and a More Dyes dye make a dyed chest. The recipes
  only load when Iron Chests is installed.
- Dyed Storage Drawers (compat/storagedrawers): grey drawer textures made from Storage Drawers' oak drawers (MIT
  licensed, (c) Texelsaur), models that mark every face for tinting, blockstates, item models, the drawer item tags,
  and a recipe per size and color: any Storage Drawers wooden drawer (or a dyed one) of that size and a dye. The drawer
  keeps what it holds, its name and its upgrades (item/crafting/KeepNbtShapelessRecipe). Making the
  textures needs Pillow and the Storage Drawers jar, which ./gradlew build downloads.

Their names and mineable tags come from data generation (ModLangProvider, ModBlockTagsProvider) as usual.

Run from anywhere: python3 tools/make_compat_resources.py
"""
import glob
import io
import json
import os
import re
import shutil
import sys
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes')
ASSETS = os.path.join(ROOT, 'src/main/resources/assets/moredyes')
DATA = os.path.join(ROOT, 'src/main/resources/data/moredyes')


def read_colors():
    source = open(os.path.join(JAVA, 'reference/ColorStrings.java')).read()
    return re.findall(r'"([0-9a-f]{6})"', re.search(r'\bALL\s*=.*?;', source, re.S).group(0))


def read_iron_chest_tiers():
    source = open(os.path.join(JAVA, 'reference/Reference.java')).read()
    return re.findall(r'"(\w+)"', re.search(r'IRON_CHEST_TIERS\s*=.*?;', source, re.S).group(0))


def write(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w') as f:
        json.dump(data, f, indent=2)
        f.write('\n')


def iron_chests(colors):
    recipes = os.path.join(DATA, 'recipes/iron_chests')
    shutil.rmtree(recipes, ignore_errors=True)
    tiers = read_iron_chest_tiers()
    for tier in tiers:
        name = 'dyed_%s_chest' % tier
        # Iron Chests' block model only names the particle texture: the chest itself is drawn by a renderer.
        write(os.path.join(ASSETS, 'blockstates', name + '.json'),
              {'variants': {'': {'model': 'ironchest:block/%s_chest' % tier}}})
        write(os.path.join(ASSETS, 'models/item', name + '.json'), {'parent': 'ironchest:item/%s_chest' % tier})
        for index, color in enumerate(colors):
            write(os.path.join(recipes, '%s_%s.json' % (name, color)), {
                'conditions': [{'type': 'forge:mod_loaded', 'modid': 'ironchest'}],
                'type': 'minecraft:crafting_shapeless',
                'category': 'misc',
                'group': 'moredyes:' + name,
                'ingredients': [
                    [{'item': 'ironchest:%s_chest' % tier}, {'item': 'moredyes:' + name}],
                    {'item': 'moredyes:%s_dye' % color},
                ],
                'result': {'item': 'moredyes:' + name, 'nbt': {'BlockStateTag': {'color': str(index)}}},
            })
    print('wrote dyed Iron Chests resources for %d tiers and %d colors' % (len(tiers), len(colors)))


DRAWER_WOODS = ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'mangrove', 'cherry', 'bamboo', 'crimson',
                'warped']
# Grey drawer texture -> Storage Drawers' oak texture it is made from.
DRAWER_TEXTURES = {'drawers_front_1': 'drawers_oak_front_1', 'drawers_front_2': 'drawers_oak_front_2',
                   'drawers_front_4': 'drawers_oak_front_4', 'drawers_side': 'drawers_oak_side',
                   'drawers_trim': 'drawers_oak_trim'}


def read_drawer_sizes():
    source = open(os.path.join(JAVA, 'reference/Reference.java')).read()
    return re.findall(r'"(\w+)"', re.search(r'DRAWER_SIZES\s*=.*?;', source, re.S).group(0))


def storage_drawers_jar():
    # The Gradle cache is shared with the other Minecraft versions, so the jar is picked by the file id this one uses.
    properties = open(os.path.join(ROOT, 'gradle.properties')).read()
    file_id = re.search(r'^storage_drawers_file=(\d+)', properties, re.M).group(1)
    jars = glob.glob(os.path.expanduser('~/.gradle/caches/modules-2/files-2.1/curse.maven/storage-drawers-223852/%s/*/*.jar' % file_id))
    if not jars:
        sys.exit('Storage Drawers jar not found; run ./gradlew build first.')
    return zipfile.ZipFile(jars[0])


def drawer_textures():
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    from PIL import Image
    from make_tinted_textures import brighten, grey
    out = os.path.join(ASSETS, 'textures/block/tinted')
    with storage_drawers_jar() as jar:
        for name, source in DRAWER_TEXTURES.items():
            image = Image.open(io.BytesIO(jar.read('assets/storagedrawers/textures/block/%s.png' % source))).convert('RGBA')
            image.putdata(brighten([grey(image.getpixel((x, y))) for y in range(image.height) for x in range(image.width)]))
            image.save(os.path.join(out, name + '.png'))


def tinted(model):
    """A copy of a Storage Drawers block model with every face marked for tinting."""
    for element in model['elements']:
        for face in element['faces'].values():
            face['tintindex'] = 0
    return model


def storage_drawers(colors):
    drawer_textures()
    sizes = read_drawer_sizes()
    # The two drawer shapes are Storage Drawers' full_drawers.json and half_drawers.json, with every face tinted.
    for depth in ('full', 'half'):
        model = json.loads(storage_drawers_jar().read('assets/storagedrawers/models/block/%s_drawers.json' % depth))
        write(os.path.join(ASSETS, 'models/block/drawers/%s_drawers.json' % depth), tinted(model))
    recipes = os.path.join(DATA, 'recipes/storage_drawers')
    shutil.rmtree(recipes, ignore_errors=True)
    for size in sizes:
        depth, count = size.split('_drawers_')
        name = 'dyed_' + size
        front, side = 'moredyes:block/tinted/drawers_front_' + count, 'moredyes:block/tinted/drawers_side'
        write(os.path.join(ASSETS, 'models/block', name + '.json'), {
            'parent': 'moredyes:block/drawers/%s_drawers' % depth,
            # As in Storage Drawers' full_drawers_orientable.json: the top and bottom use the side texture.
            'textures': {'particle': front, 'north': front, 'down': side, 'up': side, 'east': side, 'south': side,
                         'west': side, 'trim': 'moredyes:block/tinted/drawers_trim'}})
        write(os.path.join(ASSETS, 'models/item', name + '.json'), {'parent': 'moredyes:block/' + name})
        write(os.path.join(ASSETS, 'blockstates', name + '.json'), {'variants': {
            'facing=%s' % facing: dict({'model': 'moredyes:block/' + name}, **({'y': y} if y else {}))
            for facing, y in (('north', 0), ('east', 90), ('south', 180), ('west', 270))}})
        for index, color in enumerate(colors):
            write(os.path.join(recipes, '%s_%s.json' % (name, color)), {
                'conditions': [{'type': 'forge:mod_loaded', 'modid': 'storagedrawers'}],
                # A shapeless recipe whose result keeps the drawer's NBT (its contents, name and upgrades).
                'type': 'moredyes:crafting_shapeless_keep_nbt',
                'category': 'misc',
                'group': 'moredyes:' + name,
                'ingredients': [
                    [{'item': 'storagedrawers:%s_%s' % (wood, size)} for wood in DRAWER_WOODS] + [{'item': 'moredyes:' + name}],
                    {'item': 'moredyes:%s_dye' % color},
                ],
                'result': {'item': 'moredyes:' + name, 'nbt': {'BlockStateTag': {'color': str(index)}}},
            })
    # Storage Drawers' own item tags, so its keys, upgrades and recipes treat ours like its drawers.
    tags = os.path.join(ROOT, 'src/main/resources/data/storagedrawers/tags/items')
    for tag, depths in (('drawers', ('full', 'half')), ('full_drawers', ('full',)), ('half_drawers', ('half',))):
        write(os.path.join(tags, tag + '.json'), {'replace': False, 'values': [
            {'id': 'moredyes:dyed_' + size, 'required': False} for size in sizes if size.split('_')[0] in depths]})
    print('wrote dyed Storage Drawers resources for %d sizes and %d colors' % (len(sizes), len(colors)))


def main():
    colors = read_colors()
    iron_chests(colors)
    storage_drawers(colors)


if __name__ == '__main__':
    main()

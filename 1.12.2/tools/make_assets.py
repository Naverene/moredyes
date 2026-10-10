#!/usr/bin/env python3
"""Builds the textures, models and blockstates of the dyed blocks.

Each dyed block is drawn from one grey texture multiplied by its dye color (see
client/ClientHandler.java), the same way 1.7.10 MoreDyes and vanilla grass work. So every
color of a block shares one blockstate file and one model, all written by this script.

The grey textures are made from the vanilla 1.12.2 textures: each pixel keeps its
brightness and loses its color.

Run after `./gradlew build` has downloaded Minecraft (needs Pillow):
    python3 tools/make_assets.py
"""
import colorsys
import io
import json
import os
import sys
import zipfile

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, 'src/main/resources/assets/moredyes')
BASE = os.path.join(ROOT, 'tools/base')
CLIENT_JAR = os.path.expanduser('~/.gradle/caches/forge_gradle/minecraft_repo/versions/1.12.2/client.jar')


def grey(p):
    r, g, b, a = p
    lum = min(255, round(0.299 * r + 0.587 * g + 0.114 * b))
    return (lum, lum, lum, a)


def is_green(p):
    r, g, b, a = p
    if a == 0:
        return False
    h, s, _ = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
    return s > 0.15 and 65 <= h * 360 <= 170


CLEAR = (0, 0, 0, 0)
GREY = grey
GREY_IF_GREEN = lambda p: grey(p) if is_green(p) else CLEAR
GREY_IF_NOT_GREEN = lambda p: CLEAR if is_green(p) else grey(p)
KEEP_IF_GREEN = lambda p: p if is_green(p) else CLEAR
KEEP_IF_NOT_GREEN = lambda p: CLEAR if is_green(p) else p

# output texture -> (vanilla texture under textures/, or base: file in tools/base, transform)
TEXTURES = {
    'blocks/tinted/wool': ('blocks/wool_colored_white', GREY),
    'blocks/tinted/stone': ('blocks/stone', GREY),
    'blocks/tinted/cobble': ('blocks/cobblestone', GREY),
    'blocks/tinted/stonebrick': ('blocks/stonebrick', GREY),
    'blocks/tinted/stonebrick_carved': ('blocks/stonebrick_carved', GREY),
    'blocks/tinted/stonebrick_cracked': ('blocks/stonebrick_cracked', GREY),
    'blocks/tinted/obsidian': ('blocks/obsidian', GREY),
    'blocks/tinted/soulsand': ('blocks/soul_sand', GREY),
    'blocks/tinted/quartz_top': ('blocks/quartz_block_top', GREY),
    'blocks/tinted/quartz_side': ('blocks/quartz_block_side', GREY),
    'blocks/tinted/quartz_bottom': ('blocks/quartz_block_bottom', GREY),
    'blocks/tinted/clay': ('blocks/hardened_clay_stained_white', GREY),
    'blocks/tinted/hardened_clay': ('blocks/hardened_clay', GREY),
    'blocks/tinted/coal': ('blocks/coal_block', GREY),
    'blocks/tinted/glowstone': ('blocks/glowstone', GREY),
    'blocks/tinted/lapis': ('blocks/lapis_block', GREY),
    'blocks/tinted/redstone': ('blocks/redstone_block', GREY),
    'blocks/tinted/plank': ('blocks/planks_oak', GREY),
    'blocks/tinted/brick': ('blocks/brick', GREY),
    'blocks/tinted/sand': ('blocks/sand', GREY),
    'blocks/tinted/sandstone_top': ('blocks/sandstone_top', GREY),
    'blocks/tinted/sandstone_side': ('blocks/sandstone_normal', GREY),
    'blocks/tinted/sandstone_bottom': ('blocks/sandstone_bottom', GREY),
    'blocks/tinted/diorite': ('blocks/stone_diorite', GREY),
    'blocks/tinted/bookshelf': ('blocks/bookshelf', GREY),
    'blocks/tinted/workbench_top': ('blocks/crafting_table_top', GREY),
    'blocks/tinted/workbench_side': ('blocks/crafting_table_side', GREY),
    'blocks/tinted/workbench_front': ('blocks/crafting_table_front', GREY),
    'blocks/tinted/log_top': ('blocks/log_oak_top', GREY),
    'blocks/tinted/glass': ('blocks/glass', GREY),
    'blocks/tinted/glass_foggy': ('base:glass_foggy', GREY),
    'blocks/tinted/glass_pane_top': ('blocks/glass_pane_top', GREY),
    'blocks/tinted/tulip_petals': ('blocks/flower_tulip_white', GREY_IF_NOT_GREEN),
    'blocks/tinted/tulip_stem': ('blocks/flower_tulip_white', KEEP_IF_GREEN),
    'blocks/tinted/sapling_leaves': ('blocks/sapling_oak', GREY_IF_GREEN),
    'blocks/tinted/sapling_trunk': ('blocks/sapling_oak', KEEP_IF_NOT_GREEN),
    'items/dye': ('items/dye_powder_white', GREY),
    # Blocks from newer Minecraft versions. Those 1.12 lacks are drawn from the 1.7.10 textures in tools/base.
    'blocks/tinted/granite': ('blocks/stone_granite', GREY),
    'blocks/tinted/andesite': ('blocks/stone_andesite', GREY),
    'blocks/tinted/polished_granite': ('blocks/stone_granite_smooth', GREY),
    'blocks/tinted/polished_diorite': ('blocks/stone_diorite_smooth', GREY),
    'blocks/tinted/polished_andesite': ('blocks/stone_andesite_smooth', GREY),
    'blocks/tinted/concrete': ('blocks/concrete_white', GREY),
    'blocks/tinted/concrete_powder': ('blocks/concrete_powder_white', GREY),
    'blocks/tinted/soul_soil': ('base:soul_soil', GREY),
    'blocks/tinted/basalt_side': ('base:basalt_side', GREY),
    'blocks/tinted/basalt_top': ('base:basalt_top', GREY),
    'blocks/tinted/polished_basalt_side': ('base:polished_basalt_side', GREY),
    'blocks/tinted/polished_basalt_top': ('base:polished_basalt_top', GREY),
    'blocks/tinted/smooth_stone': ('blocks/stone_slab_top', GREY),
    'blocks/tinted/cut_sandstone_side': ('blocks/sandstone_smooth', GREY),
    'blocks/tinted/mossy_cobble': ('blocks/cobblestone_mossy', GREY),
    'blocks/tinted/mossy_stonebrick': ('blocks/stonebrick_mossy', GREY),
    'blocks/tinted/nether_brick': ('blocks/nether_brick', GREY),
    'blocks/tinted/cracked_nether_brick': ('base:cracked_nether_brick', GREY),
    'blocks/tinted/chiseled_nether_brick': ('base:chiseled_nether_brick', GREY),
    'blocks/tinted/end_stone_brick': ('blocks/end_bricks', GREY),
    'blocks/tinted/bone_block_side': ('blocks/bone_block_side', GREY),
    'blocks/tinted/bone_block_top': ('blocks/bone_block_top', GREY),
    'blocks/tinted/glazed_terracotta': ('blocks/glazed_terracotta_white', GREY),
    'blocks/tinted/netherite_block': ('base:netherite_block', GREY),
    'blocks/tinted/crying_obsidian': ('base:crying_obsidian', GREY),
    'blocks/tinted/chain': ('base:chain', GREY),
    'blocks/tinted/cornflower_petals': ('base:cornflower', GREY_IF_NOT_GREEN),
    'blocks/tinted/cornflower_stem': ('base:cornflower', KEEP_IF_GREEN),
    'blocks/tinted/iron_trapdoor': ('blocks/iron_trapdoor', GREY),
    'blocks/tinted/piston_side': ('blocks/piston_side', GREY),
    'blocks/tinted/piston_bottom': ('blocks/piston_bottom', GREY),
    'blocks/tinted/piston_inner': ('blocks/piston_inner', GREY),
}

# Only the cobblestone on a dyed piston takes the dye. Its wood, iron and darkest greys (the cracks and edges) are
# drawn over the tinted texture in their own colors, from these overlays; the face and the head keep the vanilla
# textures. The same as the newer versions.
PISTON_OVERLAYS = ['piston_side', 'piston_inner', 'piston_bottom']
PISTON_DARK = 0x60


def is_piston_cobblestone(name, x, y):
    """Whether pixel (x, y) of a 16x16 vanilla piston texture is cobblestone. The side has a wooden strip along the
    top; the inside has the iron ring and hole the arm comes out of."""
    if name == 'piston_side':
        return y >= 4
    if name == 'piston_inner':
        ring = 5 <= x <= 10 and 5 <= y <= 10 and not (x in (5, 10) and y in (5, 10))
        return not ring
    return True

# Chest textures are used whole by the chest renderer. The latch keeps its natural color: its
# pixels sit in the top-left corner of the texture.
CHESTS = ['normal', 'normal_double']
LATCH_WIDTH, LATCH_HEIGHT = 6, 5

T = 'moredyes:blocks/tinted/'

# Blocks drawn as a cube with one tinted texture on every side: block type -> texture.
CUBE_ALL = {
    'wool': T + 'wool',
    'rockwool': T + 'wool',
    'stone': T + 'stone',
    'cobble': T + 'cobble',
    'stonebrick': T + 'stonebrick',
    'stonebrick_carved': T + 'stonebrick_carved',
    'stonebrick_cracked': T + 'stonebrick_cracked',
    'obsidian': T + 'obsidian',
    'soulsand': T + 'soulsand',
    'clay': T + 'clay',
    'hardened_clay': T + 'hardened_clay',
    'coal': T + 'coal',
    'glowstone': T + 'glowstone',
    'lapis': T + 'lapis',
    'redstone': T + 'redstone',
    'plank': T + 'plank',
    'brick': T + 'brick',
    'sand': T + 'sand',
    'diorite': T + 'diorite',
    'glass': T + 'glass',
    'glass_foggy': T + 'glass_foggy',
    'granite': T + 'granite',
    'andesite': T + 'andesite',
    'polished_granite': T + 'polished_granite',
    'polished_diorite': T + 'polished_diorite',
    'polished_andesite': T + 'polished_andesite',
    'concrete': T + 'concrete',
    'concrete_powder': T + 'concrete_powder',
    'soul_soil': T + 'soul_soil',
    'smooth_stone': T + 'smooth_stone',
    'smooth_sandstone': T + 'sandstone_top',
    'smooth_quartz': T + 'quartz_bottom',
    'mossy_cobble': T + 'mossy_cobble',
    'mossy_stonebrick': T + 'mossy_stonebrick',
    'nether_brick': T + 'nether_brick',
    'cracked_nether_brick': T + 'cracked_nether_brick',
    'chiseled_nether_brick': T + 'chiseled_nether_brick',
    'end_stone_brick': T + 'end_stone_brick',
    'glazed_terracotta': T + 'glazed_terracotta',
    'netherite_block': T + 'netherite_block',
    'crying_obsidian': T + 'crying_obsidian',
    # The vanilla oak leaves texture is already grey; the game tints it with the biome color.
    'leaf': 'minecraft:blocks/leaves_oak',
}

def top_side_bottom(top, side, bottom):
    return dict(particle=side, down=bottom, up=top, north=side, south=side, west=side, east=side)


# Blocks drawn as a cube with different tinted textures per side: block type -> textures.
CUBE = {
    'basalt': top_side_bottom(T + 'basalt_top', T + 'basalt_side', T + 'basalt_top'),
    'polished_basalt': top_side_bottom(T + 'polished_basalt_top', T + 'polished_basalt_side', T + 'polished_basalt_top'),
    'cut_sandstone': top_side_bottom(T + 'sandstone_top', T + 'cut_sandstone_side', T + 'sandstone_top'),
    'bone_block': top_side_bottom(T + 'bone_block_top', T + 'bone_block_side', T + 'bone_block_top'),
    'quartz': dict(particle=T + 'quartz_side', down=T + 'quartz_bottom', up=T + 'quartz_top',
                   north=T + 'quartz_side', south=T + 'quartz_side', west=T + 'quartz_side', east=T + 'quartz_side'),
    'sandstone': dict(particle=T + 'sandstone_side', down=T + 'sandstone_bottom', up=T + 'sandstone_top',
                      north=T + 'sandstone_side', south=T + 'sandstone_side', west=T + 'sandstone_side',
                      east=T + 'sandstone_side'),
    'bookshelf': dict(particle=T + 'bookshelf', down=T + 'plank', up=T + 'plank',
                      north=T + 'bookshelf', south=T + 'bookshelf', west=T + 'bookshelf', east=T + 'bookshelf'),
    'workbench': dict(particle=T + 'workbench_front', down=T + 'plank', up=T + 'workbench_top',
                      north=T + 'workbench_front', south=T + 'workbench_side', west=T + 'workbench_front',
                      east=T + 'workbench_side'),
}

# Plants drawn as two crossed squares in two layers: block type -> (tinted texture, natural texture).
CROSS = {
    'sapling': (T + 'sapling_leaves', T + 'sapling_trunk'),
    'tulip': (T + 'tulip_petals', T + 'tulip_stem'),
    'cornflower': (T + 'cornflower_petals', T + 'cornflower_stem'),
}

# Stairs, slabs and walls: block name -> the dyed block they are made of. One block holds every color, which it keeps in
# a tile entity, so each has one blockstate file, item model and set of block models.
STAIRS = ['stone', 'granite', 'polished_granite', 'diorite', 'polished_diorite', 'andesite', 'polished_andesite',
          'mossy_cobble', 'mossy_stonebrick', 'end_stone_brick', 'smooth_sandstone', 'smooth_quartz', 'nether_brick']
SLABS = ['stone', 'cut_sandstone', 'diorite']
WALLS = ['brick', 'stonebrick', 'mossy_stonebrick', 'granite', 'diorite', 'andesite', 'nether_brick', 'sandstone',
         'end_stone_brick']

# Glass panes: block type -> texture of the pane's face.
PANES = {
    'glass_pane': T + 'glass',
    'glass_foggy_pane': T + 'glass_foggy',
}

SIDES = ['down', 'up', 'north', 'south', 'west', 'east']


def write_json(path, data):
    path = os.path.join(ASSETS, path)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w') as f:
        json.dump(data, f, indent=2)
        f.write('\n')


def block_and_item(name, model, variants=None):
    """One model shared by the placed block (in every color) and its item."""
    write_json('models/block/%s.json' % name, model)
    write_json('blockstates/%s.json' % name, {'variants': variants or {'normal': {'model': 'moredyes:' + name}}})
    write_json('models/item/%s.json' % name, {'parent': 'moredyes:block/' + name})


def flat_item(name, *layers):
    textures = {'layer%d' % i: texture for i, texture in enumerate(layers)}
    write_json('models/item/%s.json' % name, {'parent': 'item/generated', 'textures': textures})


def cross_planes(texture, tinted):
    face = {'uv': [0, 0, 16, 16], 'texture': texture}
    if tinted:
        face['tintindex'] = 0
    rotation = {'origin': [8, 8, 8], 'axis': 'y', 'angle': 45, 'rescale': True}
    return [
        {'from': [0.8, 0, 8], 'to': [15.2, 16, 8], 'rotation': rotation, 'shade': False,
         'faces': {'north': face, 'south': face}},
        {'from': [8, 0, 0.8], 'to': [8, 16, 15.2], 'rotation': rotation, 'shade': False,
         'faces': {'west': face, 'east': face}},
    ]


def make_textures(jar):
    for name, (src, transform) in sorted(TEXTURES.items()):
        if src.startswith('base:'):
            image = Image.open(os.path.join(BASE, src[5:] + '.png'))
        else:
            image = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/%s.png' % src)))
        image = image.convert('RGBA')
        pixels = image.load()
        for y in range(image.height):
            for x in range(image.width):
                pixels[x, y] = transform(pixels[x, y])
        path = os.path.join(ASSETS, 'textures', name + '.png')
        os.makedirs(os.path.dirname(path), exist_ok=True)
        image.save(path)
    for name in PISTON_OVERLAYS:
        image = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/blocks/%s.png' % name))).convert('RGBA')
        pixels = image.load()
        for y in range(image.height):
            for x in range(image.width):
                dark = grey(pixels[x, y])[0] < PISTON_DARK
                if not dark and is_piston_cobblestone(name, x * 16 // image.width, y * 16 // image.height):
                    pixels[x, y] = CLEAR
        image.save(os.path.join(ASSETS, 'textures/blocks/tinted', name + '_overlay.png'))
    for name in CHESTS:
        image = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/entity/chest/%s.png' % name)))
        image = image.convert('RGBA')
        pixels = image.load()
        for y in range(image.height):
            for x in range(image.width):
                if not (x < LATCH_WIDTH and y < LATCH_HEIGHT):
                    pixels[x, y] = grey(pixels[x, y])
        path = os.path.join(ASSETS, 'textures/entity/chest', name + '.png')
        os.makedirs(os.path.dirname(path), exist_ok=True)
        image.save(path)
    print('wrote %d textures' % (len(TEXTURES) + len(PISTON_OVERLAYS) + len(CHESTS)))


def make_models(jar):
    # Parents: the vanilla cube with every face tinted.
    write_json('models/block/tinted_cube.json', {
        'parent': 'block/block',
        'elements': [{
            'from': [0, 0, 0], 'to': [16, 16, 16],
            'faces': {side: {'texture': '#' + side, 'cullface': side, 'tintindex': 0} for side in SIDES},
        }],
    })
    write_json('models/block/tinted_cube_all.json', {
        'parent': 'moredyes:block/tinted_cube',
        'textures': dict({side: '#all' for side in SIDES}, particle='#all'),
    })

    for name, texture in sorted(CUBE_ALL.items()):
        block_and_item(name, {'parent': 'moredyes:block/tinted_cube_all', 'textures': {'all': texture}})
    for name, textures in sorted(CUBE.items()):
        block_and_item(name, {'parent': 'moredyes:block/tinted_cube', 'textures': textures})

    # Logs: the cut ends take the dye color, the bark stays oak.
    bark = 'minecraft:blocks/log_oak'
    ends = {side: {'texture': '#end', 'cullface': side, 'tintindex': 0} for side in ['down', 'up']}
    sides = {side: {'texture': '#side', 'cullface': side} for side in ['north', 'south', 'west', 'east']}
    write_json('models/block/log_bark.json', {'parent': 'block/cube_all', 'textures': {'all': bark}})
    block_and_item('log', {
        'parent': 'block/block',
        'textures': {'particle': bark, 'end': T + 'log_top', 'side': bark},
        'elements': [{'from': [0, 0, 0], 'to': [16, 16, 16], 'faces': dict(ends, **sides)}],
    }, {
        'axis=y': {'model': 'moredyes:log'},
        'axis=z': {'model': 'moredyes:log', 'x': 90},
        'axis=x': {'model': 'moredyes:log', 'x': 90, 'y': 90},
        'axis=none': {'model': 'moredyes:log_bark'},
    })

    for name, (tinted, natural) in sorted(CROSS.items()):
        write_json('models/block/%s.json' % name, {
            'ambientocclusion': False,
            'textures': {'particle': tinted, 'tinted': tinted, 'natural': natural},
            'elements': cross_planes('#natural', False) + cross_planes('#tinted', True),
        })
        write_json('blockstates/%s.json' % name, {'variants': {'normal': {'model': 'moredyes:' + name}}})
        flat_item(name, tinted, natural)

    # Glass panes: the vanilla pane models with every face tinted.
    parts = ['post', 'side', 'side_alt', 'noside', 'noside_alt']
    for part in parts:
        model = json.loads(jar.read('assets/minecraft/models/block/pane_%s.json' % part).decode('utf-8'))
        for element in model['elements']:
            for face in element['faces'].values():
                face['tintindex'] = 0
        write_json('models/block/tinted_pane_%s.json' % part, model)
    vanilla_state = json.loads(jar.read('assets/minecraft/blockstates/glass_pane.json').decode('utf-8'))
    for name, pane in sorted(PANES.items()):
        for part in parts:
            write_json('models/block/%s_%s.json' % (name, part), {
                'parent': 'moredyes:block/tinted_pane_' + part,
                'textures': {'pane': pane, 'edge': T + 'glass_pane_top'},
            })
        state = json.loads(json.dumps(vanilla_state))
        for case in state['multipart']:
            case['apply']['model'] = 'moredyes:' + case['apply']['model'].replace('glass_pane', name)
        write_json('blockstates/%s.json' % name, state)
        flat_item(name, pane)

    # Chests are drawn by the chest renderer; the model only gives the breaking particles.
    write_json('models/block/chest.json', {'textures': {'particle': T + 'plank'}})
    write_json('blockstates/chest.json', {'variants': {'normal': {'model': 'moredyes:chest'}}})
    write_json('models/item/chest.json', {'parent': 'minecraft:item/chest'})

    # Chain: two crossed planes, all tinted.
    write_json('models/block/chain.json', {
        'ambientocclusion': False,
        'textures': {'particle': T + 'chain', 'tinted': T + 'chain'},
        'elements': cross_planes('#tinted', True),
    })
    write_json('blockstates/chain.json', {'variants': {'normal': {'model': 'moredyes:chain'}}})
    flat_item('chain', T + 'chain')

    flat_item('dye', 'moredyes:items/dye')
    make_shape_models(jar)
    print('wrote the models and blockstates')


def sides_of(name):
    """The top, side and bottom texture of a dyed block, for the stairs, slabs and walls made of it."""
    if name in CUBE_ALL:
        return {'top': CUBE_ALL[name], 'side': CUBE_ALL[name], 'bottom': CUBE_ALL[name]}
    textures = CUBE[name]
    return {'top': textures['up'], 'side': textures['north'], 'bottom': textures['down']}


def tinted_copy(jar, vanilla, name):
    """A vanilla block model with every face tinted, as moredyes:block/<name>."""
    model = json.loads(jar.read('assets/minecraft/models/block/%s.json' % vanilla).decode('utf-8'))
    for element in model['elements']:
        for face in element['faces'].values():
            face['tintindex'] = 0
    write_json('models/block/%s.json' % name, model)


def vanilla_state(jar, name):
    return json.loads(jar.read('assets/minecraft/blockstates/%s.json' % name).decode('utf-8'))


def make_shape_models(jar):
    for part in ['stairs', 'inner_stairs', 'outer_stairs', 'half_slab', 'upper_slab', 'wall_post', 'wall_side',
                 'wall_inventory', 'trapdoor_bottom', 'trapdoor_top', 'trapdoor_open']:
        tinted_copy(jar, part, 'tinted_' + part)

    stairs_state = vanilla_state(jar, 'stone_brick_stairs')
    for base in STAIRS:
        name = base + '_stairs'
        for suffix, part in [('', 'stairs'), ('_inner', 'inner_stairs'), ('_outer', 'outer_stairs')]:
            write_json('models/block/%s%s.json' % (name, suffix),
                       {'parent': 'moredyes:block/tinted_' + part, 'textures': sides_of(base)})
        state = json.loads(json.dumps(stairs_state))
        for variant in state['variants'].values():
            variant['model'] = 'moredyes:' + {'stone_brick_stairs': name,
                                              'stone_brick_inner_stairs': name + '_inner',
                                              'stone_brick_outer_stairs': name + '_outer'}[variant['model']]
        write_json('blockstates/%s.json' % name, state)
        write_json('models/item/%s.json' % name, {'parent': 'moredyes:block/' + name})

    for base in SLABS:
        name = base + '_slab'
        sides = sides_of(base)
        write_json('models/block/%s_bottom.json' % name, {'parent': 'moredyes:block/tinted_half_slab', 'textures': sides})
        write_json('models/block/%s_top.json' % name, {'parent': 'moredyes:block/tinted_upper_slab', 'textures': sides})
        write_json('models/block/%s_double.json' % name, {
            'parent': 'moredyes:block/tinted_cube',
            'textures': top_side_bottom(sides['top'], sides['side'], sides['bottom'])})
        write_json('blockstates/%s.json' % name, {'variants': {
            'type=' + t: {'model': 'moredyes:%s_%s' % (name, t)} for t in ['bottom', 'top', 'double']}})
        write_json('models/item/%s.json' % name, {'parent': 'moredyes:block/%s_bottom' % name})

    for base in WALLS:
        name = base + '_wall'
        wall = {'wall': sides_of(base)['side']}
        for part in ['post', 'side', 'inventory']:
            write_json('models/block/%s_%s.json' % (name, part),
                       {'parent': 'moredyes:block/tinted_wall_' + part, 'textures': wall})
        state = vanilla_state(jar, 'cobblestone_wall')
        for case in state['multipart']:
            case['apply']['model'] = 'moredyes:' + case['apply']['model'].replace('cobblestone_wall', name)
        write_json('blockstates/%s.json' % name, state)
        write_json('models/item/%s.json' % name, {'parent': 'moredyes:block/%s_inventory' % name})

    trapdoor = {'texture': T + 'iron_trapdoor'}
    for part in ['bottom', 'top', 'open']:
        write_json('models/block/iron_trapdoor_%s.json' % part,
                   {'parent': 'moredyes:block/tinted_trapdoor_' + part, 'textures': trapdoor})
    state = vanilla_state(jar, 'iron_trapdoor')
    for variant in state['variants'].values():
        variant['model'] = 'moredyes:' + variant['model']
    write_json('blockstates/iron_trapdoor.json', state)
    write_json('models/item/iron_trapdoor.json', {'parent': 'moredyes:block/iron_trapdoor_bottom'})

    make_piston_models(jar)


def piston_model(textures, extended, inventory=False):
    """A dyed piston: the vanilla piston model with its cobblestone tinted, and a second element that draws the wood,
    iron and cracks over it untinted. #front is the face (the platform, or the inside when extended). The block models
    face north like vanilla's; the inventory model faces up, like vanilla's piston item."""
    depth = 4 if extended else 0
    full = [0, 0, 16, 16]
    if inventory:
        front, back = 'up', 'down'
        sides = {'north': 0, 'south': 0, 'west': 0, 'east': 0}
    else:
        front, back = 'north', 'south'
        sides = {'down': 180, 'up': 0, 'west': 270, 'east': 90}

    def faces(overlay):
        suffix = '_overlay' if overlay else ''
        result = {}
        for side, rotation in sides.items():
            result[side] = {'uv': [0, depth, 16, 16], 'texture': '#side' + suffix, 'cullface': side}
            if rotation:
                result[side]['rotation'] = rotation
        result[back] = {'uv': full, 'texture': '#bottom' + suffix, 'cullface': back}
        # The platform is untinted and needs no overlay; the inside of an extended piston is tinted and has one.
        if extended or not overlay:
            result[front] = {'uv': full, 'texture': '#front' + suffix}
            if not extended:
                result[front]['cullface'] = front
        for face in result.values():
            if not overlay and (extended or face['texture'] != '#front'):
                face['tintindex'] = 0
        return result

    elements = [{'from': [0, 0, depth], 'to': [16, 16, 16], 'faces': faces(overlay)} for overlay in (False, True)]
    model = {'textures': dict(textures, particle='#side'), 'elements': elements}
    if inventory:
        model['parent'] = 'block/block'
    return model


def make_piston_models(jar):
    textures = {'side': T + 'piston_side', 'bottom': T + 'piston_bottom',
                'side_overlay': T + 'piston_side_overlay', 'bottom_overlay': T + 'piston_bottom_overlay'}
    write_json('models/block/piston_extended.json', piston_model(
        dict(textures, front=T + 'piston_inner', front_overlay=T + 'piston_inner_overlay'), True))
    for name, platform in [('piston', 'piston_top_normal'), ('sticky_piston', 'piston_top_sticky')]:
        front = dict(textures, front='minecraft:blocks/' + platform)
        write_json('models/block/%s.json' % name, piston_model(front, False))
        write_json('models/block/%s_inventory.json' % name, piston_model(front, False, inventory=True))
        state = vanilla_state(jar, 'piston')
        for variant in state['variants'].values():
            variant['model'] = 'moredyes:' + variant['model'].replace('piston_normal', name) \
                .replace('piston_extended_normal', 'piston_extended')
        write_json('blockstates/%s.json' % name, state)
        write_json('models/item/%s.json' % name, {'parent': 'moredyes:block/%s_inventory' % name})


def main():
    if not os.path.exists(CLIENT_JAR):
        sys.exit('Minecraft 1.12.2 client.jar not found; run ./gradlew build first.')
    jar = zipfile.ZipFile(CLIENT_JAR)
    make_textures(jar)
    make_models(jar)


if __name__ == '__main__':
    main()

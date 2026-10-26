#!/usr/bin/env python3
"""Builds the grey textures that dyed blocks are tinted from.

Each dyed block is drawn from one grey texture multiplied by its dye color (see
client/Tints.java), the same way 1.7.10 More Dyes and vanilla grass work. The
grey textures are made from the vanilla textures: each pixel keeps its
brightness and loses its color, and the texture is then brightened so that dark
materials like stone still show their dye clearly.

Some vanilla textures are split into two layers: a grey part that takes the dye
color, and a part that keeps its own color and is drawn over it untinted (a
tulip's stem, a sapling's trunk, the wood and iron on a piston).

TEXTURES below is also read by generate_resources.py, which uses it to point
the models at these textures.

Run after `./gradlew build` has downloaded Minecraft (needs Pillow):
    python3 tools/textures.py
"""
import colorsys
import glob
import io
import os
import sys
import zipfile

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, 'src/main/resources/assets/moredyes/textures')
BASE = os.path.join(ROOT, 'tools/base')
WOODS = ['oak', 'birch', 'spruce', 'jungle', 'acacia', 'dark_oak']

# A dye color only shows as bright as the grey it multiplies, so a mid-grey texture like stone would draw every color
# at half brightness. Each grey texture is shifted up until its average is this bright, keeping its pattern.
BRIGHTNESS = 200
# Materials that are meant to stay dark whatever their dye.
STAY_DARK = {'block/obsidian', 'block/coal_block', 'block/crying_obsidian'}

GREY, GREY_IF_GREEN, GREY_IF_NOT_GREEN, KEEP_IF_GREEN, KEEP_IF_NOT_GREEN, KEEP_IF_NOT_COBBLESTONE = (
    'grey', 'grey_if_green', 'grey_if_not_green', 'keep_if_green', 'keep_if_not_green', 'keep_if_not_cobblestone')
GREY_IF_NOT_COLORFUL, KEEP_IF_COLORFUL = 'grey_if_not_colorful', 'keep_if_colorful'
# A bed's bedding, and its pillow, which stays white on every vanilla bed (see bed_keep). The wooden frame is drawn
# separately, in the planks the bed was made from (see bed_textures).
GREY_BEDDING, KEEP_BED_PILLOW = 'grey_bedding', 'keep_bed_pillow'
# The books on a bookshelf (see is_book).
KEEP_BOOKS = 'keep_books'

# Vanilla texture -> the layers it becomes, as (texture in this mod, how it is made, whether it takes the dye color).
# The first layer replaces the vanilla texture in the models; a second layer is drawn over it, untinted.
# A vanilla texture that is not listed is used as it is (for example log bark and leaves, which are already grey).
TEXTURES = {
    'block/glass': [('block/glass', GREY, True)],
    'block/glass_pane_top': [('block/glass_pane_top', GREY, True)],
    'block/sand': [('block/sand', GREY, True)],
    'block/bricks': [('block/bricks', GREY, True)],
    'block/clay': [('block/clay', GREY, True)],
    'block/terracotta': [('block/terracotta', GREY, True)],
    'block/white_wool': [('block/wool', GREY, True)],
    'block/cobblestone': [('block/cobblestone', GREY, True)],
    'block/stone_bricks': [('block/stone_bricks', GREY, True)],
    'block/cracked_stone_bricks': [('block/cracked_stone_bricks', GREY, True)],
    'block/chiseled_stone_bricks': [('block/chiseled_stone_bricks', GREY, True)],
    'block/stone': [('block/stone', GREY, True)],
    'block/andesite': [('block/andesite', GREY, True)],
    'block/diorite': [('block/diorite', GREY, True)],
    'block/obsidian': [('block/obsidian', GREY, True)],
    'block/lapis_block': [('block/lapis_block', GREY, True)],
    'block/glowstone': [('block/glowstone', GREY, True)],
    'block/coal_block': [('block/coal_block', GREY, True)],
    'block/soul_sand': [('block/soul_sand', GREY, True)],
    'block/redstone_block': [('block/redstone_block', GREY, True)],
    'block/quartz_block_side': [('block/quartz_block_side', GREY, True)],
    'block/quartz_block_top': [('block/quartz_block_top', GREY, True)],
    'block/quartz_block_bottom': [('block/quartz_block_bottom', GREY, True)],
    'block/white_concrete': [('block/concrete', GREY, True)],
    'block/white_concrete_powder': [('block/concrete_powder', GREY, True)],
    'block/sandstone': [('block/sandstone', GREY, True)],
    'block/sandstone_top': [('block/sandstone_top', GREY, True)],
    'block/sandstone_bottom': [('block/sandstone_bottom', GREY, True)],
    'block/chiseled_sandstone': [('block/chiseled_sandstone', GREY, True)],
    'block/cut_sandstone': [('block/cut_sandstone', GREY, True)],
    'block/crafting_table_top': [('block/crafting_table_top', GREY, True)],
    'block/crafting_table_side': [('block/crafting_table_side', GREY, True)],
    'block/crafting_table_front': [('block/crafting_table_front', GREY, True)],
    # Only the wood of a bookshelf takes the dye: its books are drawn over it in their own colors.
    'block/bookshelf': [('block/bookshelf', GREY, True), ('block/bookshelf_books', KEEP_BOOKS, False)],
    # Only the cobblestone on a piston takes the dye: its wood, iron and darkest greys (see PISTON_DARK) are drawn over
    # it in their own colors, and the face (piston_top and piston_top_sticky) is not listed, so it stays vanilla.
    'block/piston_side': [('block/piston_side', GREY, True),
                          ('block/piston_side_overlay', KEEP_IF_NOT_COBBLESTONE, False)],
    'block/piston_bottom': [('block/piston_bottom', GREY, True),
                            ('block/piston_bottom_overlay', KEEP_IF_NOT_COBBLESTONE, False)],
    'block/piston_inner': [('block/piston_inner', GREY, True),
                           ('block/piston_inner_overlay', KEEP_IF_NOT_COBBLESTONE, False)],
    'block/white_tulip': [('block/tulip', GREY_IF_NOT_GREEN, True), ('block/tulip_stem', KEEP_IF_GREEN, False)],
    'item/white_dye': [('item/dye', GREY, True)],
    'block/white_glazed_terracotta': [('block/glazed_terracotta', GREY, True)],
    'block/iron_chain': [('block/chain', GREY, True)],
    'item/iron_chain': [('item/chain', GREY, True)],
    'block/oak_sign': [('block/sign', GREY, True)],
    'item/oak_sign': [('item/sign', GREY, True)],
    'block/white_shulker_box': [('block/shulker_box', GREY, True)],
}
for _name in ['granite', 'polished_andesite', 'polished_diorite', 'polished_granite', 'end_stone', 'mossy_cobblestone',
              'mossy_stone_bricks', 'quartz_bricks', 'chiseled_quartz_block', 'chiseled_quartz_block_top',
              'quartz_pillar_side', 'quartz_pillar_top', 'bone_block_side', 'bone_block_top', 'gravel', 'ice',
              'packed_ice', 'snow', 'soul_soil', 'basalt_side', 'basalt_top', 'polished_basalt_side',
              'polished_basalt_top', 'smooth_stone', 'nether_bricks', 'cracked_nether_bricks',
              'chiseled_nether_bricks', 'end_stone_bricks', 'netherite_block', 'crying_obsidian']:
    TEXTURES['block/' + _name] = [('block/' + _name, GREY, True)]
# Flowers: the petals take the dye, the green stem and leaves are drawn over them in their own colors.
for _name in ['allium', 'azure_bluet', 'blue_orchid', 'cornflower', 'dandelion', 'lily_of_the_valley', 'oxeye_daisy',
              'poppy', 'lilac_top', 'lilac_bottom', 'peony_top', 'peony_bottom', 'rose_bush_top', 'rose_bush_bottom']:
    TEXTURES['block/' + _name] = [('block/' + _name, GREY_IF_NOT_GREEN, True), ('block/%s_stem' % _name, KEEP_IF_GREEN, False)]
for _wood in WOODS:
    TEXTURES['block/%s_planks' % _wood] = [('block/%s_planks' % _wood, GREY, True)]
    TEXTURES['block/%s_log_top' % _wood] = [('block/%s_log_top' % _wood, GREY, True)]
    TEXTURES['block/%s_sapling' % _wood] = [('block/%s_sapling' % _wood, GREY_IF_GREEN, True),
                                           ('block/%s_sapling_trunk' % _wood, KEEP_IF_NOT_GREEN, False)]

# Foggy glass replaces the vanilla glass texture with its own, made from tools/base/foggy_glass.png.
FOGGY_GLASS = 'block/foggy_glass'
# Speckled granite (GregTech's red granite textures, LGPL-3.0, see THIRD_PARTY_NOTICES.md) replaces the vanilla granite textures of its kinds with its own, made
# from tools/base/<kind>.png: kind -> (vanilla texture it replaces, texture in this mod).
# Beds are block models: the bedding takes the dye color, and the white pillow is drawn over it as it is. The wooden
# frame and legs are drawn by a model of their own (see bed_textures).
TEXTURES['block/white_bed_foot_up'] = [('block/bed_foot_up', GREY, True)]
BED_SIDES = ('head_up', 'head_east', 'head_west', 'foot_up', 'foot_east', 'foot_west', 'foot_south')
for _side in BED_SIDES[:3] + BED_SIDES[4:]:
    TEXTURES['block/white_bed_' + _side] = [('block/bed_' + _side, GREY_BEDDING, True)]
    if _side.startswith('head'):
        TEXTURES['block/white_bed_' + _side].append(('block/bed_%s_pillow' % _side, KEEP_BED_PILLOW, False))
# The vanilla planks a bed can be made from. Each gets its own frame textures (block/bed/frame/<wood>_<side>), and
# "dyed" a grey frame tinted with the color of More Dyes planks.
BED_WOODS = ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'mangrove', 'cherry', 'pale_oak', 'bamboo',
             'crimson', 'warped']
BED_COLORS = ['white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan',
              'purple', 'blue', 'brown', 'green', 'red', 'black']

OWN_TEXTURES = {'speckled_granite': ('block/granite', 'block/speckled_granite'),
                'smooth_speckled_granite': ('block/polished_granite', 'block/smooth_speckled_granite')}

# Chest textures go on the chest atlas (textures/entity/chest). The latch keeps its natural color: its pixels sit in the
# top-left corner of the texture, (width, height) given here.
CHESTS = {'normal': (6, 5), 'normal_left': (4, 5), 'normal_right': (4, 5)}


def is_colorful(p):
    """Whether a pixel has a color of its own, like a bed's wooden legs or the stripes on a llama's carpet."""
    r, g, b, a = p
    if a == 0:
        return False
    _, s, _ = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
    return s > 0.25


def bed_keep(vanilla, source):
    """Which pixels of a white bed texture are drawn as they are: the wooden frame and the pillow, which stays white on
    every vanilla bed. Those are the pixels with a color of their own on the white bed, or with none on the red and blue
    beds. Returns that, and the pixels to draw for them (the red bed's, whose pillow is vanilla's)."""
    white, red, blue = [list(vanilla(source.replace('white', c)).get_flattened_data()) for c in ('white', 'red', 'blue')]
    keep = [is_colorful(w) or (r[3] > 0 and not is_colorful(r) and not is_colorful(b)) for w, r, b in zip(white, red, blue)]
    return keep, red


def bed_textures(vanilla):
    """The textures of a bed's frame and legs, the pixels with a color of their own on the white bed, in each kind of
    planks: block/bed/frame/<wood>_<side>. Each frame pixel takes the planks' pixel at the same rank of brightness, so
    the frame keeps its shading, and oak comes out like vanilla's. Also the vanilla colors' bedding without the frame
    (block/bed/<color>_<side>), for the More Dyes beds in those colors, and the part of the head's back that is not
    wood (block/bed/head_north). Returns how many textures it wrote."""
    lum = lambda p: 0.299 * p[0] + 0.587 * p[1] + 0.114 * p[2]
    clear = (0, 0, 0, 0)
    count = 0

    def write(pixels, size, name):
        image = Image.new('RGBA', size)
        image.putdata(pixels)
        save(image, name)

    for side in BED_SIDES + ('head_north', 'down'):
        source = 'block/bed_' + side if side in ('head_north', 'down') else 'block/white_bed_' + side
        white = vanilla(source)
        pixels = list(white.get_flattened_data())
        wood = [is_colorful(p) for p in pixels]
        frame = sorted((i for i, w in enumerate(wood) if w), key=lambda i: lum(pixels[i]))
        for name in BED_WOODS:
            planks = sorted(vanilla('block/%s_planks' % name).get_flattened_data(), key=lum)
            layer = [clear] * len(pixels)
            for rank, i in enumerate(frame):
                layer[i] = planks[round(rank * (len(planks) - 1) / max(1, len(frame) - 1))]
            write(layer, white.size, 'block/bed/frame/%s_%s' % (name, side))
        layer = [clear] * len(pixels)
        for i, p in zip(frame, brighten([grey(pixels[i]) for i in frame])):
            layer[i] = p
        write(layer, white.size, 'block/bed/frame/dyed_' + side)
        count += len(BED_WOODS) + 1
        if side == 'head_north':
            write([clear if w else p for p, w in zip(pixels, wood)], white.size, 'block/bed/head_north')
            count += 1
        elif side != 'down':
            for color in BED_COLORS:
                colored = vanilla(source.replace('white', color))
                write([clear if w else p for p, w in zip(colored.get_flattened_data(), wood)], colored.size,
                      'block/bed/%s_%s' % (color, side))
                count += 1
    return count


def split_colorful(image):
    """(the grey part to tint, brightened; the colorful part, kept as it is) of an RGBA image."""
    pixels = list(image.get_flattened_data())
    grey_part = brighten([(0, 0, 0, 0) if is_colorful(p) else grey(p) for p in pixels])
    kept = [p if is_colorful(p) else (0, 0, 0, 0) for p in pixels]
    out = []
    for data in (grey_part, kept):
        layer = Image.new('RGBA', image.size)
        layer.putdata(data)
        out.append(layer)
    return out


def grey(p):
    r, g, b, a = p
    lum = min(255, round(0.299 * r + 0.587 * g + 0.114 * b))
    return (lum, lum, lum, a)


def is_book(p):
    """Whether a pixel of the vanilla bookshelf is a book, not its wood: the frame, shelves and the dark wall behind the
    books are all dull browns."""
    r, g, b, a = p
    if a == 0:
        return False
    h, s, _ = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
    return not (25 <= h * 360 <= 50 and s <= 0.7)


def is_green(p):
    r, g, b, a = p
    if a == 0:
        return False
    h, s, _ = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
    return s > 0.15 and 65 <= h * 360 <= 170


# The darkest greys of a piston's cobblestone (the cracks and edges) stay grey: they are drawn over the dye too.
PISTON_DARK = 0x60


def is_piston_cobblestone(source, x, y):
    """Whether pixel (x, y) of a 16x16 vanilla piston texture is cobblestone. The side has a wooden strip along the
    top; the inside has the iron ring and hole the arm comes out of."""
    if source == 'block/piston_side':
        return y >= 4
    if source == 'block/piston_inner':
        ring = 5 <= x <= 10 and 5 <= y <= 10 and not (x in (5, 10) and y in (5, 10))
        return not ring
    return True


def stats_of(pixels):
    lums = [p[0] for p in pixels if p[3] > 0]
    return (sum(lums) / len(lums), max(lums)) if lums else (BRIGHTNESS, BRIGHTNESS)


def brighten(pixels, stats=None):
    """Shifts grey pixels so their average is BRIGHTNESS. Highlights are compressed rather than clipped to white.
    stats is the (mean, max) to shift by, so a texture can match another one; by default it is the pixels' own."""
    mean, top = stats or stats_of(pixels)
    if mean >= BRIGHTNESS:
        return pixels
    squeeze = min(1.0, (250 - BRIGHTNESS) / max(1.0, top - mean))
    out = []
    for r, g, b, a in pixels:
        d = r - mean
        lum = max(0, min(255, round(BRIGHTNESS + (d * squeeze if d > 0 else d))))
        out.append((lum, lum, lum, a))
    return out


def client_jar():
    jars = glob.glob(os.path.expanduser('~/.gradle/caches/neoformruntime/artifacts/minecraft_*_client.jar'))
    if not jars:
        sys.exit('The Minecraft client jar was not found; run ./gradlew build first.')
    return zipfile.ZipFile(sorted(jars)[-1])


def first_frame(image):
    """Animated textures are a vertical strip of frames; keeps the first frame."""
    return image.crop((0, 0, image.width, min(image.width, image.height)))


def save(image, name):
    path = os.path.join(ASSETS, name + '.png')
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.save(path)


def main():

    jar = client_jar()
    vanilla = lambda path: first_frame(Image.open(io.BytesIO(jar.read('assets/minecraft/textures/%s.png' % path)))
                                       .convert('RGBA'))
    count = 0
    for source, layers in sorted(TEXTURES.items()):
        image = vanilla(source)
        layer_pixels = []
        for name, how, tinted in layers:
            pixels = list(image.get_flattened_data())
            if how == GREY:
                pixels = [grey(p) for p in pixels]
            elif how == GREY_IF_GREEN:
                pixels = [grey(p) if is_green(p) else (0, 0, 0, 0) for p in pixels]
            elif how == GREY_IF_NOT_GREEN:
                pixels = [(0, 0, 0, 0) if is_green(p) else grey(p) for p in pixels]
            elif how == KEEP_IF_GREEN:
                pixels = [p if is_green(p) else (0, 0, 0, 0) for p in pixels]
            elif how == KEEP_IF_NOT_GREEN:
                pixels = [(0, 0, 0, 0) if is_green(p) else p for p in pixels]
            elif how == GREY_IF_NOT_COLORFUL:
                pixels = [(0, 0, 0, 0) if is_colorful(p) else grey(p) for p in pixels]
            elif how == KEEP_IF_COLORFUL:
                pixels = [p if is_colorful(p) else (0, 0, 0, 0) for p in pixels]
            elif how in (GREY_BEDDING, KEEP_BED_PILLOW):
                keep, shown = bed_keep(vanilla, source)
                if how == GREY_BEDDING:
                    pixels = [(0, 0, 0, 0) if k else grey(p) for p, k in zip(pixels, keep)]
                else:
                    pixels = [p if k and not is_colorful(w) else (0, 0, 0, 0) for p, k, w in zip(shown, keep, pixels)]
            elif how == KEEP_BOOKS:
                pixels = [p if is_book(p) else (0, 0, 0, 0) for p in pixels]
            elif how == KEEP_IF_NOT_COBBLESTONE:
                w, h = image.size
                pixels = [(0, 0, 0, 0) if grey(p)[0] >= PISTON_DARK
                          and is_piston_cobblestone(source, i % w * 16 // w, i // w * 16 // h) else p
                          for i, p in enumerate(pixels)]
            if tinted and source not in STAY_DARK:
                pixels = brighten(pixels)
            layer_pixels.append(pixels)
        for (name, _, _), pixels in zip(layers, layer_pixels):
            out = Image.new('RGBA', image.size)
            out.putdata(pixels)
            save(out, name)
            count += 1

    count += bed_textures(vanilla)
    foggy = first_frame(Image.open(os.path.join(BASE, 'foggy_glass.png')).convert('RGBA'))
    foggy.putdata(brighten([grey(p) for p in foggy.get_flattened_data()]))
    save(foggy, FOGGY_GLASS)
    count += 1
    for kind, (_, name) in sorted(OWN_TEXTURES.items()):
        own = first_frame(Image.open(os.path.join(BASE, kind + '.png')).convert('RGBA'))
        own.putdata(brighten([grey(p) for p in own.get_flattened_data()]))
        save(own, name)
        count += 1

    # A llama's carpet (textures/entity/equipment/llama_body) is drawn from the layers its equipment asset names: the
    # grey carpet tinted, and its red and green stripes over it as they are.
    image = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/entity/equipment/llama_body/white.png')))
    tinted, kept = split_colorful(image.convert('RGBA'))
    save(tinted, 'entity/equipment/llama_body/carpet')
    save(kept, 'entity/equipment/llama_body/carpet_stripes')
    count += 2
    # Shulker boxes are drawn from the shulker atlas (textures/entity/shulker) by client/DyedShulkerBoxRenderer.
    shulker = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/entity/shulker/shulker_white.png'))).convert('RGBA')
    shulker.putdata(brighten([grey(p) for p in shulker.get_flattened_data()]))
    save(shulker, 'entity/shulker/dyed')
    count += 1

    for name, (latch_w, latch_h) in sorted(CHESTS.items()):
        image = Image.open(io.BytesIO(jar.read('assets/minecraft/textures/entity/chest/%s.png' % name))).convert('RGBA')
        pixels = image.load()
        body = [(x, y) for y in range(image.height) for x in range(image.width) if not (x < latch_w and y < latch_h)]
        lifted = brighten([grey(pixels[x, y]) for x, y in body])
        for (x, y), p in zip(body, lifted):
            pixels[x, y] = p
        save(image, 'entity/chest/' + name)
        count += 1
    print('wrote %d textures to %s' % (count, os.path.relpath(ASSETS, ROOT)))


if __name__ == '__main__':
    main()

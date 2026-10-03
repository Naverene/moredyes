#!/usr/bin/env python3
"""Writes every generated resource of the mod into src/generated/resources.

More Dyes has about 70 kinds of block in 118 colors, so its blockstates, item models, drops, recipes, tags, names and
trees are generated rather than written by hand. Almost all of it is copied from the vanilla block each kind copies,
read from the Minecraft and NeoForge jars:

  * models: the vanilla model with its textures swapped for the grey ones from textures.py, and the faces that show
    them marked with tintindex 0 so they take the dye color. Models are shared by all colors of a kind.
  * blockstates and drops: the vanilla ones, pointing at the dyed blocks and models.
  * tags: every vanilla and NeoForge tag that lists the vanilla block (mineable/pickaxe, wool, leaves, planks...).
  * recipes: the recipes of the 1.7.10 mod (see the recipes() function).
  * trees: the vanilla tree of each wood, grown from dyed logs and leaves.
  * slabs, stairs and walls: see shapes(). Most have no vanilla version (there is no vanilla glass slab), so their
    models are made from vanilla's stone ones with the textures of the block they are made from.
  * the dyed Storage Drawers (compat/storagedrawers): see storage_drawers(). Their textures and models come from
    tools/storage_drawers.py.

The kinds and colors are read from block/Kind.java and color/ColorGroup.java. Run after `./gradlew build` has
downloaded Minecraft, and after tools/textures.py:
    python3 tools/generate_resources.py
"""
import glob
import json
import os
import re
import shutil
import sys
import zipfile

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from textures import TEXTURES, FOGGY_GLASS, WOODS, client_jar  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUT = os.path.join(ROOT, 'src/generated/resources')
JAVA = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes')
MOD = 'moredyes'
DYE_COLOR = {'type': MOD + ':dye_color'}
VANILLA_DYES = ['white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan',
                'purple', 'blue', 'brown', 'green', 'red', 'black']

# Result color, count, then the vanilla dyes mixed to make it. From 1.7.10's CraftManager.
DYE_MIXES = [
    ('ecbf99', 2, ('white', 'orange')),
    ('d9a6ec', 2, ('white', 'magenta')),
    ('b3ccec', 2, ('white', 'light_blue')),
    ('f2f299', 2, ('white', 'yellow')),
    ('bfe68c', 2, ('white', 'lime')),
    ('f9bfd2', 2, ('white', 'pink')),
    ('cccccc', 2, ('white', 'light_gray')),
    ('a6bfcc', 2, ('white', 'cyan')),
    ('bf9fd9', 2, ('white', 'purple')),
    ('99a6d9', 2, ('white', 'blue')),
    ('b3a699', 2, ('white', 'brown')),
    ('b3bf99', 2, ('white', 'green')),
    ('cc9999', 4, ('white', 'white', 'red', 'red')),
    ('c56685', 2, ('orange', 'magenta')),
    ('9f8c85', 2, ('orange', 'light_blue')),
    ('deb233', 2, ('orange', 'yellow')),
    ('aca526', 2, ('orange', 'lime')),
    ('e57f6c', 2, ('orange', 'pink')),
    ('92663f', 2, ('orange', 'gray')),
    ('b98c66', 2, ('orange', 'light_gray')),
    ('927f66', 2, ('orange', 'cyan')),
    ('ac5f72', 2, ('orange', 'purple')),
    ('866672', 2, ('orange', 'blue')),
    ('9f6633', 2, ('orange', 'brown')),
    ('9f7f33', 2, ('orange', 'green')),
    ('b95933', 2, ('orange', 'red')),
    ('794c26', 2, ('orange', 'black')),
    ('8c72d8', 2, ('magenta', 'light_blue')),
    ('cb9886', 2, ('magenta', 'yellow')),
    ('998c79', 2, ('magenta', 'lime')),
    ('d265bf', 2, ('magenta', 'pink')),
    ('7f4c92', 2, ('magenta', 'gray')),
    ('a672b9', 2, ('magenta', 'light_gray')),
    ('7f65b9', 2, ('magenta', 'cyan')),
    ('9946c5', 2, ('magenta', 'purple')),
    ('734cc5', 2, ('magenta', 'blue')),
    ('8c4c86', 2, ('magenta', 'brown')),
    ('8c6586', 2, ('magenta', 'green')),
    ('a64086', 2, ('magenta', 'red')),
    ('663379', 2, ('magenta', 'black')),
    ('a5bf86', 2, ('light_blue', 'yellow')),
    ('72b279', 2, ('light_blue', 'lime')),
    ('ac8cbf', 2, ('light_blue', 'pink')),
    ('597392', 2, ('light_blue', 'gray')),
    ('7f99b9', 2, ('light_blue', 'light_gray')),
    ('598cb9', 2, ('light_blue', 'cyan')),
    ('726cc5', 2, ('light_blue', 'purple')),
    ('4d73c5', 2, ('light_blue', 'blue')),
    ('667386', 2, ('light_blue', 'brown')),
    ('668c86', 2, ('light_blue', 'green')),
    ('7f6686', 2, ('light_blue', 'red')),
    ('405979', 2, ('light_blue', 'black')),
    ('b2d926', 2, ('yellow', 'lime')),
    ('ebb26c', 2, ('yellow', 'pink')),
    ('99993f', 2, ('yellow', 'gray')),
    ('bfbf66', 2, ('yellow', 'light_gray')),
    ('99b266', 2, ('yellow', 'cyan')),
    ('b29272', 2, ('yellow', 'purple')),
    ('8c9972', 2, ('yellow', 'blue')),
    ('a69933', 2, ('yellow', 'brown')),
    ('a6b233', 2, ('yellow', 'green')),
    ('bf8c33', 2, ('yellow', 'red')),
    ('7f7f26', 2, ('yellow', 'black')),
    ('b8a65f', 2, ('lime', 'pink')),
    ('668c32', 2, ('lime', 'gray')),
    ('8cb359', 2, ('lime', 'light_gray')),
    ('66a659', 2, ('lime', 'cyan')),
    ('7f5665', 2, ('lime', 'purple')),
    ('598c65', 2, ('lime', 'blue')),
    ('738c26', 2, ('lime', 'brown')),
    ('73a626', 2, ('lime', 'green')),
    ('8c8026', 2, ('lime', 'red')),
    ('4c7319', 2, ('lime', 'black')),
    ('864c5f', 2, ('pink', 'gray')),
    ('c6596c', 2, ('pink', 'light_gray')),
    ('ac7f6c', 2, ('pink', 'cyan')),
    ('ac666c', 2, ('pink', 'purple')),
    ('9366ab', 2, ('pink', 'blue')),
    ('b95fab', 2, ('pink', 'brown')),
    ('9f7f9f', 2, ('pink', 'green')),
    ('c68c9f', 2, ('pink', 'red')),
    ('9f6679', 2, ('pink', 'black')),
    ('727272', 2, ('gray', 'light_gray')),
    ('4c6572', 2, ('gray', 'cyan')),
    ('65467f', 2, ('gray', 'purple')),
    ('404c7f', 2, ('gray', 'blue')),
    ('594c40', 2, ('gray', 'brown')),
    ('596540', 2, ('gray', 'green')),
    ('724040', 2, ('gray', 'red')),
    ('333333', 2, ('gray', 'black')),
    ('738c99', 2, ('light_gray', 'cyan')),
    ('8c6ca5', 2, ('light_gray', 'purple')),
    ('6673a5', 2, ('light_gray', 'blue')),
    ('807366', 2, ('light_gray', 'brown')),
    ('808c66', 2, ('light_gray', 'green')),
    ('996666', 2, ('light_gray', 'red')),
    ('595959', 2, ('light_gray', 'black')),
    ('655fa5', 2, ('cyan', 'purple')),
    ('4066a5', 2, ('cyan', 'blue')),
    ('596666', 2, ('cyan', 'brown')),
    ('597f66', 2, ('cyan', 'green')),
    ('725966', 2, ('cyan', 'red')),
    ('334c59', 2, ('cyan', 'black')),
    ('5945b2', 2, ('purple', 'blue')),
    ('734573', 2, ('purple', 'brown')),
    ('735f73', 2, ('purple', 'green')),
    ('8c3973', 2, ('purple', 'red')),
    ('4c2c66', 2, ('purple', 'black')),
    ('4c4c73', 2, ('blue', 'brown')),
    ('4c6573', 2, ('blue', 'green')),
    ('664073', 2, ('blue', 'red')),
    ('263366', 2, ('blue', 'black')),
    ('403326', 2, ('brown', 'green')),
    ('7f4033', 2, ('brown', 'red')),
    ('666533', 2, ('brown', 'black')),
    ('7f5933', 2, ('green', 'red')),
    ('404c26', 2, ('green', 'black')),
    ('592626', 2, ('red', 'black')),
]

# Kinds that are dyed by putting eight vanilla blocks around a dye. The others are made another way (see recipes()).
NOT_DYED_IN_A_RING = {'foggy_glass', 'foggy_glass_pane', 'crafting_table', 'chest', 'piston_head', 'tulip'}
# Tags the dyed blocks are not added to even though their vanilla block is in them: tags that name the vanilla color,
# and tags that would let a dyed block make a vanilla block in a recipe that has a dyed version.
TAG_EXCLUDED = re.compile(r'white|colorless|uncolored|undyed|smelts_to_glass|(^|/)(oak|birch|spruce|jungle|acacia|dark_oak)_logs$')
# Where trees and tulips grow: the overworld, except water, beaches, deserts, badlands, ice and mushroom islands.
WORLDGEN_BIOMES = {
    'type': 'neoforge:and',
    'values': ['#c:is_overworld', {
        'type': 'neoforge:not',
        'value': {'type': 'neoforge:or', 'values': ['#c:is_aquatic', '#c:is_beach', '#c:is_desert', '#c:is_badlands',
                                                    '#c:is_icy', '#c:is_mushroom', '#c:is_cave']},
    }],
}
# The vanilla tree each wood grows. Dark oak grows from one sapling, like 1.7.10, so it has the oak shape.
TREES = {'oak': 'oak', 'birch': 'birch', 'spruce': 'spruce', 'jungle': 'jungle_tree_no_vine', 'acacia': 'acacia',
         'dark_oak': 'oak'}
POTTABLE = ['tulip', 'allium', 'azure_bluet', 'orchid', 'cornflower', 'dandelion', 'lily_of_the_valley', 'oxeye_daisy',
            'poppy'] + ['%s_sapling' % w for w in WOODS]
# Flowers that make a dye of their color: one, or two from a tall flower, like vanilla.
FLOWER_DYES = {'tulip': 1, 'allium': 1, 'azure_bluet': 1, 'orchid': 1, 'cornflower': 1, 'dandelion': 1,
               'lily_of_the_valley': 1, 'oxeye_daisy': 1, 'poppy': 1, 'lilac': 2, 'peony': 2, 'rose_bush': 2}
# The vanilla slab, stairs and wall whose blockstates, models, drops and tags the dyed ones copy, with the textures
# swapped. Wooden ones copy oak's, so they get the wooden_slabs and wooden_stairs tags.
SHAPE_TEMPLATES = {'SLAB': 'stone_slab', 'STAIRS': 'stone_stairs', 'WALL': 'cobblestone_wall'}
WOODEN_SHAPE_TEMPLATES = {'SLAB': 'oak_slab', 'STAIRS': 'oak_stairs'}
SHAPE_NAMES = {'SLAB': 'Slab', 'STAIRS': 'Stairs', 'WALL': 'Wall'}
# Where the made-up vanilla models of the slabs, stairs and walls are kept while they are converted.
SHAPE_MODELS = 'minecraft:block/moredyes_shape/'


# ---------------------------------------------------------------------------------------------------------------------
# Input
# ---------------------------------------------------------------------------------------------------------------------

def read_drawer_sizes():
    source = open(os.path.join(JAVA, 'compat/storagedrawers/StorageDrawersCompat.java')).read()
    return re.findall(r'"(\w+)"', re.search(r'SIZES\s*=.*?;', source, re.S).group(0))


def read_kinds():
    """(id, vanilla id, tab) of each Kind, in order."""
    source = open(os.path.join(JAVA, 'block/Kind.java')).read()
    kinds = re.findall(r'^\s+[A-Z_]+\("([a-z_]+)", "([a-z_]+)", Tab\.([A-Z]+)', source, re.M)
    if not kinds:
        sys.exit('No kinds found in Kind.java')
    return kinds


def read_shapes():
    """(id, shape, base kind id) of each slab, stairs and wall Kind, in order."""
    source = open(os.path.join(JAVA, 'block/Kind.java')).read()
    ids = dict(re.findall(r'^\s+([A-Z_]+)\("([a-z_]+)", "[a-z_]+", Tab\.', source, re.M))
    found = re.findall(r'^\s+[A-Z_]+\(Shape\.([A-Z]+), "([a-z_]+)", ([A-Z_]+)[,)]', source, re.M)
    return [(kind, shape, ids[base]) for shape, kind, base in found]


def read_colors():
    source = open(os.path.join(JAVA, 'color/ColorGroup.java')).read()
    body = source[source.index('enum ColorGroup {'):]
    body = body[:body.index(';')]
    return re.findall(r'"([0-9a-f]{6})"', body)


class Jars:
    def __init__(self):
        self.client = client_jar()
        neo = glob.glob(os.path.expanduser('~/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/*/*/'
                                           'neoforge-*-universal.jar'))
        if not neo:
            sys.exit('The NeoForge jar was not found; run ./gradlew build first.')
        self.neo = zipfile.ZipFile(sorted(neo)[-1])
        self.virtual = {}  # path -> json: models made up here, read as if they were vanilla's

    def json(self, path, default=None):
        if path in self.virtual:
            return self.virtual[path]
        for jar in (self.client, self.neo):
            try:
                return json.loads(jar.read(path))
            except KeyError:
                pass
        if default is not None:
            return default
        raise KeyError(path)

    def has(self, path):
        return any(path in jar.NameToInfo for jar in (self.client, self.neo))

    def names(self, prefix):
        for jar in (self.client, self.neo):
            for name in jar.namelist():
                if name.startswith(prefix) and name.endswith('.json'):
                    yield jar, name


# Last words of names that read the same in the plural, such as "Glass" or "Concrete Powder".
UNCOUNTABLE = {'Andesite', 'Clay', 'Cobblestone', 'Concrete', 'Diorite', 'Glass', 'Glowstone', 'Granite', 'Gravel',
               'Ice', 'Obsidian', 'Powder', 'Sand', 'Sandstone', 'Stone', 'Terracotta', 'Wool'}


def plural(name):
    """"Oak Planks" -> "Oak Planks", "Chest" -> "Chests", "Block of Coal" -> "Blocks of Coal"."""
    if ' of ' in name:
        head, tail = name.split(' of ', 1)
        return plural(head) + ' of ' + tail
    if name.split(' ')[-1] in UNCOUNTABLE or name.endswith('s'):
        return name
    if name.endswith('shelf'):
        return name[:-1] + 'ves'
    if name.endswith(('sh', 'ch', 'x')):
        return name + 'es'
    if name.endswith('y') and name[-2] not in 'aeiou':
        return name[:-1] + 'ies'
    return name + 's'


def full(identifier):
    return identifier if ':' in identifier else 'minecraft:' + identifier


def short(identifier):
    """minecraft:block/stone -> block/stone"""
    return identifier.split(':', 1)[1] if identifier.startswith('minecraft:') else identifier


class Writer:
    def __init__(self):
        self.count = 0

    def write(self, path, data):
        path = os.path.join(OUT, path)
        os.makedirs(os.path.dirname(path), exist_ok=True)
        # The few shared files are written readably; the tens of thousands of per-color files are kept small.
        readable = '/models/' in path or '/lang/' in path
        with open(path, 'w') as f:
            if readable:
                json.dump(data, f, indent=2)
            else:
                json.dump(data, f, separators=(',', ':'))
            f.write('\n')
        self.count += 1


# ---------------------------------------------------------------------------------------------------------------------
# Models
# ---------------------------------------------------------------------------------------------------------------------

class Models:
    """Turns vanilla models into dyed ones, once per kind. Writes them as it goes."""

    def __init__(self, jars, writer):
        self.jars = jars
        self.writer = writer
        self.done = {}      # (kind, vanilla model) -> (model id, tints)
        self.written = {}   # model id -> json, to catch two kinds writing different models under one name

    def chain(self, model_id):
        out = []
        while model_id:
            model_id = full(model_id)
            if model_id.startswith('minecraft:builtin/'):
                out.append((model_id, {}))
                break
            data = self.jars.json('assets/minecraft/models/%s.json' % short(model_id))
            out.append((model_id, data))
            model_id = data.get('parent')
        return out

    @staticmethod
    def layers(texture, kind):
        """The dyed layers that replace a vanilla texture: [(texture, tinted)], or None to keep it."""
        texture = short(texture)
        if kind.startswith('foggy_glass') and texture == 'block/glass':
            return [(MOD + ':' + FOGGY_GLASS, True)]
        if texture in TEXTURES:
            return [(MOD + ':' + name, tinted) for name, _, tinted in TEXTURES[texture]]
        return None

    def name(self, model_id, kind, vanilla):
        """Our name for a vanilla model: the vanilla block's name in it becomes the kind's, or the kind is put in front."""
        if model_id.startswith(SHAPE_MODELS):
            return '%s:block/%s' % (MOD, model_id[len(SHAPE_MODELS):])
        folder, name = short(model_id).split('/', 1)
        if vanilla in name:
            name = name.replace(vanilla, kind, 1)
        elif kind not in name:
            name = kind + '_' + name
        return '%s:%s/%s' % (MOD, folder, name)

    def emit(self, model_id, data):
        if self.written.get(model_id, data) != data:
            sys.exit('Two kinds want different models named ' + model_id)
        if model_id not in self.written:
            self.written[model_id] = data
            self.writer.write('assets/%s/models/%s.json' % (MOD, model_id.split(':', 1)[1]), data)

    def convert(self, model_id, kind, vanilla):
        """Returns (model to use, tints): tints is None for a block model (it uses tintindex 0), or a list with
        whether each layer of a generated item model takes the dye color."""
        key = (kind, full(model_id))
        if key not in self.done:
            self.done[key] = self._convert(full(model_id), kind, vanilla)
        return self.done[key]

    def _convert(self, model_id, kind, vanilla):
        chain = self.chain(model_id)
        textures = {}
        for _, data in reversed(chain):
            textures.update(data.get('textures', {}))

        def resolve(value, seen=0):
            """Follows #references to a texture: its name, or an object with its name as "sprite"."""
            while isinstance(value, str) and value.startswith('#') and seen < 20:
                value, seen = textures.get(value[1:]), seen + 1
            return value

        def sprite(value):
            return value['sprite'] if isinstance(value, dict) else value

        def swap(value, name):
            """The texture value with another sprite, keeping options such as force_translucent."""
            return dict(value, sprite=name) if isinstance(value, dict) else name

        if any(mid == 'minecraft:builtin/generated' for mid, _ in chain):
            layers, tints, changed = {}, [], False
            index = 0
            while 'layer%d' % index in textures:
                texture = sprite(resolve(textures['layer%d' % index]))
                new = self.layers(texture, kind)
                changed |= new is not None
                for name, tinted in new or [(full(texture), False)]:
                    layers['layer%d' % len(tints)] = name
                    tints.append(tinted)
                index += 1
            if not changed:
                return model_id, tints
            ours = self.name(model_id, kind, vanilla)
            self.emit(ours, {'parent': 'minecraft:item/generated', 'textures': layers})
            return ours, tints

        elements = next((data['elements'] for _, data in chain if 'elements' in data), None)
        slots = {}

        def slot(texture):
            for var, value in slots.items():
                if value == texture:
                    return '#' + var
            var = 't%d' % len(slots)
            slots[var] = texture
            return '#' + var

        changed = False
        new_elements = []
        for element in elements or []:
            faces, overlay = {}, {}
            for side, face in element.get('faces', {}).items():
                face = dict(face)
                value = resolve(face['texture'])
                texture = sprite(value)
                new = self.layers(texture, kind) if texture else None
                if new:
                    changed = True
                    face['texture'] = slot(swap(value, new[0][0]))
                    if new[0][1]:
                        face['tintindex'] = 0
                    else:
                        face.pop('tintindex', None)
                    for name, _ in new[1:]:
                        top = dict(face, texture=slot(name))
                        top.pop('tintindex', None)
                        overlay[side] = top
                elif texture:
                    face['texture'] = slot(swap(value, full(texture)))
                faces[side] = face
            new_elements.append(dict(element, faces=faces))
            if overlay:
                new_elements.append(dict(element, faces=overlay))

        particle = resolve(textures.get('particle'))
        if particle:
            new = self.layers(sprite(particle), kind)
            changed |= new is not None
            particle = swap(particle, new[0][0] if new else full(sprite(particle)))
        if not changed:
            return model_id, None
        # A made-up model (see shapes()) is not in the game, so ours names its parent instead.
        parent = chain[1][0] if model_id.startswith(SHAPE_MODELS) else model_id
        data = {'parent': parent, 'textures': dict(slots)}
        if particle:
            data['textures']['particle'] = particle
        if elements is not None:
            data['elements'] = new_elements
        ours = self.name(model_id, kind, vanilla)
        self.emit(ours, data)
        return ours, None


def remap_models(node, convert):
    """Points every "model" in a blockstate at the dyed model."""
    if isinstance(node, list):
        return [remap_models(n, convert) for n in node]
    if isinstance(node, dict):
        return {k: (convert(v)[0] if k == 'model' and isinstance(v, str) else remap_models(v, convert))
                for k, v in node.items()}
    return node


def remap_ids(node, ids):
    """Replaces every string that is a key of ids (such as "minecraft:oak_log") by its value."""
    if isinstance(node, list):
        return [remap_ids(n, ids) for n in node]
    if isinstance(node, dict):
        return {k: remap_ids(v, ids) for k, v in node.items()}
    if isinstance(node, str):
        return ids.get(node, node)
    return node


# ---------------------------------------------------------------------------------------------------------------------
# Generation
# ---------------------------------------------------------------------------------------------------------------------

def main():
    kinds = read_kinds()
    colors = read_colors()
    if sorted(c for c, _, _ in DYE_MIXES) != sorted(colors):
        sys.exit('DYE_MIXES and ColorGroup.java list different colors')
    jars = Jars()
    if os.path.isdir(OUT):
        shutil.rmtree(OUT)
    w = Writer()
    models = Models(jars, w)
    lang = json.loads(jars.client.read('assets/minecraft/lang/en_us.json'))
    vanilla_of = {k: v for k, v, _ in kinds}

    def our(kind, color):
        return '%s:%s_%s' % (MOD, kind, color)

    def ids_for(kind, color):
        """Vanilla id -> dyed id of this color: the kind's own vanilla block first, then the other kinds'."""
        ids = {}
        for k, v, tab in kinds:
            ids.setdefault('minecraft:' + v, our(k, color))
        ids['minecraft:' + vanilla_of[kind]] = our(kind, color)
        for k in POTTABLE:
            ids['minecraft:potted_' + vanilla_of[k]] = '%s:potted_%s_%s' % (MOD, k, color)
        return ids

    names = {}
    groups = {'dye': 'Mixed Dyes'}  # recipe viewer groups (compat/rei), by kind
    for kind, vanilla, tab in kinds:
        convert = lambda m, kind=kind, vanilla=vanilla: models.convert(m, kind, vanilla)
        state = remap_models(jars.json('assets/minecraft/blockstates/%s.json' % vanilla), convert)
        base_name = lang['block.minecraft.' + vanilla].replace('White ', '')
        if kind.startswith('foggy_'):
            base_name = 'Foggy ' + base_name
        if kind == 'orchid':
            base_name = 'Orchid'
        item = None
        if tab != 'NONE':
            groups[kind] = 'Dyed ' + plural(base_name)
            item = jars.json('assets/minecraft/items/%s.json' % vanilla)['model']
            if kind == 'chest':
                item = None  # one per color, below
            else:
                model, tints = convert(item['model'])
                if tints is None:
                    tints = [True]
                while tints and not tints[-1]:
                    tints.pop()
                item = {'type': 'minecraft:model', 'model': model}
                if tints:
                    item['tints'] = [DYE_COLOR if t else {'type': 'minecraft:constant', 'value': -1} for t in tints]
        loot = jars.json('data/minecraft/loot_table/blocks/%s.json' % vanilla, {})
        for color in colors:
            name = '%s_%s' % (kind, color)
            ids = ids_for(kind, color)
            w.write('assets/%s/blockstates/%s.json' % (MOD, name), state)
            names['block.%s.%s' % (MOD, name)] = '%s %s' % (color.upper(), base_name)
            if tab != 'NONE':
                if kind == 'chest':
                    model = {'type': 'minecraft:special', 'base': 'minecraft:item/chest',
                             'model': {'type': MOD + ':dyed_chest', 'color': color}}
                else:
                    model = item
                w.write('assets/%s/items/%s.json' % (MOD, name), {'model': model})
            if loot:
                table = remap_ids(loot, ids)
                table['random_sequence'] = '%s:blocks/%s' % (MOD, name)
                w.write('data/%s/loot_table/blocks/%s.json' % (MOD, name), table)

    shapes(jars, w, models, kinds, colors, lang, names, groups)

    # Flower pots holding the dyed plants
    for kind in POTTABLE:
        vanilla = 'potted_' + vanilla_of[kind]
        convert = lambda m, kind=kind, vanilla=vanilla_of[kind]: models.convert(m, kind, vanilla)
        state = remap_models(jars.json('assets/minecraft/blockstates/%s.json' % vanilla), convert)
        loot = jars.json('data/minecraft/loot_table/blocks/%s.json' % vanilla)
        base_name = lang['block.minecraft.' + vanilla_of[kind]].replace('White ', '')
        for color in colors:
            name = 'potted_%s_%s' % (kind, color)
            w.write('assets/%s/blockstates/%s.json' % (MOD, name), state)
            table = remap_ids(loot, ids_for(kind, color))
            table['random_sequence'] = '%s:blocks/%s' % (MOD, name)
            w.write('data/%s/loot_table/blocks/%s.json' % (MOD, name), table)
            names['block.%s.%s' % (MOD, name)] = 'Potted %s %s' % (color.upper(), base_name)

    # Dyes
    dye_model, dye_tints = models.convert('minecraft:item/white_dye', 'dye', 'white_dye')
    for color in colors:
        w.write('assets/%s/items/dye_%s.json' % (MOD, color), {'model': {
            'type': 'minecraft:model', 'model': dye_model, 'tints': [DYE_COLOR for _ in dye_tints]}})
        names['item.%s.dye_%s' % (MOD, color)] = '%s Dye' % color.upper()

    for tab, title in [('dyes', 'Dyes'), ('blocks', 'Blocks'), ('trees', 'Trees'), ('plants', 'Plants'),
                       ('shapes', 'Slabs and Stairs')]:
        names['itemGroup.%s.%s' % (MOD, tab)] = 'More Dyes ' + title
    storage_drawers(w, colors, names)
    for size in read_drawer_sizes():
        groups[size] = names['block.%s.%s' % (MOD, size)]  # "Dyed Drawers 1x1", already a plural
    for kind, name in groups.items():
        names['group.%s.%s' % (MOD, kind)] = name
    w.write('assets/%s/lang/en_us.json' % MOD, names)

    tags(jars, w, kinds, colors)
    recipes(jars, w, kinds, colors)
    worldgen(jars, w, colors)
    chiseling(w, kinds, colors)
    w.write('data/%s/loot_modifiers/dyed_sheep_wool.json' % MOD, {'type': MOD + ':dyed_sheep_wool'})
    print('wrote %d files to %s' % (w.count, os.path.relpath(OUT, ROOT)))


def shape_template(shape, base):
    if base.endswith('_planks') or base == 'crafting_table':
        return WOODEN_SHAPE_TEMPLATES.get(shape, SHAPE_TEMPLATES[shape])
    return SHAPE_TEMPLATES[shape]


def shape_name(lang, kind, shape, base_name):
    """The vanilla name if vanilla has this slab, stairs or wall, else the base block's: "Glass Slab"."""
    if 'block.minecraft.' + kind in lang:
        return lang['block.minecraft.' + kind]
    name = base_name
    if name.startswith('Block of '):
        name = name[len('Block of '):]
    if name.endswith(' Block'):
        name = name[:-len(' Block')]
    if name.endswith('Bricks'):
        name = name[:-1]
    return '%s %s' % (name, SHAPE_NAMES[shape])


def shapes(jars, w, models, kinds, colors, lang, names, groups):
    """The dyed slabs, stairs and walls. Each copies a vanilla stone one (or oak, for wooden ones): its blockstate,
    item and drops, and its models with the textures of the base block's model: its top, bottom and side. A double
    slab is the base block itself."""
    vanilla_of = {k: v for k, v, _ in kinds}
    for kind, shape, base in read_shapes():
        template = shape_template(shape, base)
        convert = lambda m, kind=kind: models.convert(m, kind, kind)
        base_convert = lambda m, base=base: models.convert(m, base, vanilla_of[base])

        base_state = jars.json('assets/minecraft/blockstates/%s.json' % vanilla_of[base])
        variant = next(iter(base_state['variants'].values()))
        base_model = (variant[0] if isinstance(variant, list) else variant)['model']
        textures = {}
        for _, data in reversed(models.chain(base_model)):
            textures.update(data.get('textures', {}))

        def texture(*keys):
            for key in keys:
                value = textures.get(key)
                while isinstance(value, str) and value.startswith('#'):
                    value = textures.get(value[1:])
                if value is not None:
                    return value
            sys.exit('No texture %s in the model of %s' % ('/'.join(keys), base))

        roles = {'top': texture('top', 'end', 'all', 'up'), 'bottom': texture('bottom', 'end', 'all', 'down'),
                 'side': texture('side', 'all', 'east')}
        roles['wall'] = roles['particle'] = roles['side']

        def model(model_id):
            """A model of the template block made up with the base block's textures, or the base block's own model
            for the template's full block (the double slab)."""
            name = short(model_id).split('/', 1)[1]
            if not name.startswith(template):
                return base_convert(base_model)[0]
            data = jars.json('assets/minecraft/models/%s.json' % short(model_id))
            data = dict(data, textures={key: roles[key] for key in data['textures']})
            virtual = SHAPE_MODELS + kind + name[len(template):]
            jars.virtual['assets/minecraft/models/%s.json' % short(virtual)] = data
            return convert(virtual)[0]

        def remap(node):
            if isinstance(node, list):
                return [remap(n) for n in node]
            if isinstance(node, dict):
                return {k: (model(v) if k == 'model' and isinstance(v, str) else remap(v)) for k, v in node.items()}
            return node

        state = remap(jars.json('assets/minecraft/blockstates/%s.json' % template))
        item = {'type': 'minecraft:model', 'model': model(jars.json('assets/minecraft/items/%s.json' % template)
                                                            ['model']['model']), 'tints': [DYE_COLOR]}
        loot = jars.json('data/minecraft/loot_table/blocks/%s.json' % template)
        base_name = lang['block.minecraft.' + vanilla_of[base]].replace('White ', '')
        if base.startswith('foggy_'):
            base_name = 'Foggy ' + base_name
        name = shape_name(lang, kind, shape, base_name)
        groups[kind] = 'Dyed ' + plural(name)
        for color in colors:
            block = '%s_%s' % (kind, color)
            w.write('assets/%s/blockstates/%s.json' % (MOD, block), state)
            w.write('assets/%s/items/%s.json' % (MOD, block), {'model': item})
            names['block.%s.%s' % (MOD, block)] = '%s %s' % (color.upper(), name)
            table = remap_ids(loot, {'minecraft:' + template: '%s:%s' % (MOD, block)})
            table['random_sequence'] = '%s:blocks/%s' % (MOD, block)
            w.write('data/%s/loot_table/blocks/%s.json' % (MOD, block), table)


# Rechiseled groups that already hold the vanilla block: the dyed kinds are appended to them.
RECHISELED_GROUPS = {
    'stone': ['stone', 'stone_bricks', 'cracked_stone_bricks', 'chiseled_stone_bricks'],
    'cobblestone': ['cobblestone'],
    'obsidian': ['obsidian'],
    'quartz_block': ['quartz_block'],
    'coal_block': ['coal_block'],
    'glowstone': ['glowstone'],
    'lapis_block': ['lapis_block'],
    'redstone_block': ['redstone_block'],
    'sandstone': ['sandstone', 'chiseled_sandstone', 'cut_sandstone'],
    'andesite': ['andesite'],
    'diorite': ['diorite'],
    'oak_planks': ['oak_planks'],
    'birch_planks': ['birch_planks'],
    'spruce_planks': ['spruce_planks'],
    'jungle_planks': ['jungle_planks'],
    'acacia_planks': ['acacia_planks'],
    'dark_oak_planks': ['dark_oak_planks'],
}
# Groups of our own: the vanilla block, then the dyed kinds.
OWN_GROUPS = {
    'wool': ('white_wool', ['wool']),
    'soul_sand': ('soul_sand', ['soul_sand']),
    'terracotta': ('terracotta', ['terracotta']),
    'clay': ('clay', ['clay']),
    'bricks': ('bricks', ['bricks']),
    'sand': ('sand', ['sand']),
    'glass': ('glass', ['glass', 'foggy_glass']),
    'glass_pane': ('glass_pane', ['glass_pane', 'foggy_glass_pane']),
    'concrete': ('white_concrete', ['concrete']),
    'concrete_powder': ('white_concrete_powder', ['concrete_powder']),
    'chest': ('chest', ['chest']),
    'bookshelf': ('bookshelf', ['bookshelf']),
    'crafting_table': ('crafting_table', ['crafting_table']),
    'piston': ('piston', ['piston']),
    'sticky_piston': ('sticky_piston', ['sticky_piston']),
}


def chiseling(w, kinds, colors):
    """Makes the dyed blocks chiselable with Rechiseled, like the 1.7.10 Chisel support: every dyed shade joins the
    group of its vanilla block, so a chisel turns the vanilla block into any shade and back. Where Rechiseled already
    has a group for the vanilla block, the shades are appended to it (a file with the same id and "overwrite": false);
    otherwise a moredyes group is made that also holds the vanilla block. These are plain data files, so nothing
    happens when Rechiseled isn't installed. A vanilla block must not be in two groups.
    """
    kind_ids = {k for k, _, _ in kinds}

    def dyed(group_kinds):
        for kind in group_kinds:
            if kind not in kind_ids:
                sys.exit('Chiseling group lists unknown kind ' + kind)
        return ['%s:%s_%s' % (MOD, kind, color) for kind in group_kinds for color in colors]

    def group(ns, name, entries):
        w.write('data/%s/chiseling_recipes/%s.json' % (ns, name),
                {'type': 'rechiseled:chiseling', 'overwrite': False, 'entries': entries})

    for name, group_kinds in RECHISELED_GROUPS.items():
        group('rechiseled', name, dyed(group_kinds))
    for name, (vanilla, group_kinds) in OWN_GROUPS.items():
        group(MOD, name, ['minecraft:' + vanilla] + dyed(group_kinds))


def tags(jars, w, kinds, colors):
    """Adds each kind to every vanilla and NeoForge tag its vanilla block or item is listed in."""
    out = {}  # (registry, tag) -> set of ids

    def add(registry, tag, values):
        out.setdefault((registry, tag), []).extend(v for v in values if v not in out.get((registry, tag), []))

    by_vanilla = {}
    for kind, vanilla, tab in kinds:
        by_vanilla.setdefault('minecraft:' + vanilla, []).append((kind, tab))
    # A slab, stairs or wall gets the tags of the vanilla one it copies (slabs, wooden_stairs, walls...), except those
    # for the tool that mines it, which come from its base block, so a wool slab is not mined with a pickaxe.
    vanilla_of = {k: v for k, v, _ in kinds}
    shape_by_template, shape_by_base = {}, {}
    for kind, shape, base in read_shapes():
        shape_by_template.setdefault('minecraft:' + shape_template(shape, base), []).append(kind)
        shape_by_base.setdefault('minecraft:' + vanilla_of[base], []).append(kind)
    for registry in ('block', 'item'):
        for jar, path in jars.names(''):
            m = re.match(r'data/([a-z_]+)/tags/%s/(.+)\.json$' % registry, path)
            if not m or TAG_EXCLUDED.search(m.group(2)):
                continue
            tag = '%s:%s' % m.groups()
            for entry in json.loads(jar.read(path)).get('values', []):
                entry = entry['id'] if isinstance(entry, dict) else entry
                for kind, tab in by_vanilla.get(entry, []):
                    if registry == 'item' and tab == 'NONE':
                        continue
                    add(registry, tag, ['%s:%s_%s' % (MOD, kind, c) for c in colors])
                tool = re.match(r'minecraft:(mineable/|needs_)', tag)
                for kind in shape_by_template.get(entry, []) if not tool else []:
                    add(registry, tag, ['%s:%s_%s' % (MOD, kind, c) for c in colors])
                for kind in shape_by_base.get(entry, []) if tool and registry == 'block' else []:
                    add(registry, tag, ['%s:%s_%s' % (MOD, kind, c) for c in colors])
    logs = ['%s:%s_log_%s' % (MOD, wood, c) for wood in WOODS for c in colors]
    add('block', 'minecraft:logs_that_burn', logs)
    add('item', 'minecraft:logs_that_burn', logs)
    add('block', 'minecraft:flower_pots', ['%s:potted_%s_%s' % (MOD, k, c) for k in POTTABLE for c in colors])
    add('item', 'c:dyes', ['%s:dye_%s' % (MOD, c) for c in colors])
    # The dyed Storage Drawers only exist when Storage Drawers is installed, so they are optional entries. Storage
    # Drawers' own item tags let its keys, upgrades and recipes treat them like its drawers.
    sizes = read_drawer_sizes()
    add('block', 'minecraft:mineable/axe', [optional(size) for size in sizes])
    for tag, depths in (('drawers', ('full', 'half')), ('full_drawers', ('full',)), ('half_drawers', ('half',))):
        add('item', 'storagedrawers:' + tag, [optional(size) for size in sizes if size.split('_')[0] in depths])
    for (registry, tag), values in sorted(out.items()):
        ns, path = tag.split(':', 1)
        w.write('data/%s/tags/%s/%s.json' % (ns, registry, path), {'values': values})


def optional(path):
    return {'id': '%s:%s' % (MOD, path), 'required': False}


# Storage Drawers' woods: any of its wooden drawers can be dyed.
DRAWER_WOODS = ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak', 'mangrove', 'cherry', 'pale_oak', 'bamboo',
                'crimson', 'warped']


def storage_drawers(w, colors, names):
    """The dyed Storage Drawers (compat/storagedrawers): one block per drawer size, whose color is its "color" block
    state property (the color's position in ColorGroup.java). Blockstates and item models point at the models from
    tools/storage_drawers.py, and the item is tinted by the color in its block_state component. A Storage Drawers
    wooden drawer (or a dyed one) of a size and a dye make a dyed drawer of that size and color; the recipes only load
    when Storage Drawers is installed. They are transmute recipes, like vanilla's shulker box dyeing: the result keeps
    every component of the drawer it is made from (its stored items and upgrades, its name), only taking the new color."""
    grids = {'1': '1x1', '2': '1x2', '4': '2x2'}
    for size in read_drawer_sizes():
        depth, count = size.split('_drawers_')
        model = '%s:block/drawers/%s' % (MOD, size)
        w.write('assets/%s/blockstates/%s.json' % (MOD, size), {'variants': {
            'facing=%s' % facing: dict({'model': model}, **({'y': y} if y else {}))
            for facing, y in (('north', 0), ('east', 90), ('south', 180), ('west', 270))}})
        w.write('assets/%s/items/%s.json' % (MOD, size), {'model': {
            'type': 'minecraft:model', 'model': model, 'tints': [{'type': MOD + ':state_color'}]}})
        # The item's name comes from Storage Drawers ("C56685 Drawers 1x1", see DyedDrawersItem); this one is the
        # block's, for anything that names a block without its color.
        names['block.%s.%s' % (MOD, size)] = 'Dyed %sDrawers %s' % ('Half ' if depth == 'half' else '', grids[count])
        for index, color in enumerate(colors):
            w.write('data/%s/recipe/dyeing/%s_%s.json' % (MOD, size, color), {
                'neoforge:conditions': [{'type': 'neoforge:mod_loaded', 'modid': 'storagedrawers'}],
                'type': 'minecraft:crafting_transmute', 'category': 'building', 'group': '%s:%s' % (MOD, size),
                'input': ['storagedrawers:%s_%s' % (wood, size) for wood in DRAWER_WOODS] + ['%s:%s' % (MOD, size)],
                'material': '%s:dye_%s' % (MOD, color),
                'result': {'id': '%s:%s' % (MOD, size),
                           'components': {'minecraft:block_state': {'color': str(index)}}}})


def recipes(jars, w, kinds, colors):
    """The recipes of 1.7.10 More Dyes:
      * dyes are mixed from two vanilla dyes (or four for one color), and a dyed flower makes dye of its color;
      * eight vanilla blocks around a dye make eight dyed blocks ("dyeing/"), and a few blocks are dyed one at a time;
      * a dyed block with a water bucket gives the vanilla block back ("washing/"), as does a water cauldron;
      * dyed blocks turn into each other the way their vanilla blocks do (cobblestone to stone, logs to planks...).
    """
    kind_ids = {k for k, _, _ in kinds}
    vanilla_of = {k: v for k, v, _ in kinds}
    priorities = {}
    shapes_list = read_shapes()
    pickaxe = set(jars.json('data/minecraft/tags/block/mineable/pickaxe.json')['values'])

    def item(i, count=1):
        return {'count': count, 'id': i} if count > 1 else {'id': i}

    def shaped(path, result, count, pattern, key, priority=False):
        w.write('data/%s/recipe/%s.json' % (MOD, path), {
            'type': 'minecraft:crafting_shaped', 'category': 'building', 'key': key, 'pattern': pattern,
            'result': item(result, count)})
        if priority:
            priorities['%s:%s' % (MOD, path)] = 1

    def shapeless(path, result, count, ingredients, category='building'):
        w.write('data/%s/recipe/%s.json' % (MOD, path), {
            'type': 'minecraft:crafting_shapeless', 'category': category, 'ingredients': ingredients,
            'result': item(result, count)})

    def smelt(path, ingredient, result, xp):
        w.write('data/%s/recipe/%s.json' % (MOD, path), {
            'type': 'minecraft:smelting', 'category': 'blocks', 'cookingtime': 200, 'experience': xp,
            'ingredient': ingredient, 'result': item(result)})

    # Vanilla two-dye recipes, so a mix never takes the same dyes as one
    vanilla_mixes = set()
    for jar, path in jars.names('data/minecraft/recipe/'):
        data = json.loads(jar.read(path))
        if data.get('type') == 'minecraft:crafting_shapeless' and data['result']['id'].endswith('_dye'):
            if all(isinstance(i, str) for i in data['ingredients']):
                vanilla_mixes.add(tuple(sorted(data['ingredients'])))
    for color, count, dyes in DYE_MIXES:
        ingredients = ['minecraft:%s_dye' % d for d in dyes]
        if tuple(sorted(ingredients)) in vanilla_mixes:
            # Like 1.7.10's four-dye mix for cc9999: twice the dyes, so it is not the vanilla recipe
            ingredients, count = ingredients * 2, count * 2
            print('  dye %s takes two each of %s, as one each makes a vanilla dye' % (color, ' and '.join(dyes)))
        shapeless('dye/%s' % color, '%s:dye_%s' % (MOD, color), count, ingredients, 'misc')

    for c in colors:
        dye = '%s:dye_%s' % (MOD, c)

        def b(kind):
            return '%s:%s_%s' % (MOD, kind, c)

        def wash(kind, vanilla=None):
            shapeless('washing/%s_%s' % (kind, c), 'minecraft:' + (vanilla or vanilla_of[kind]), 1,
                      [b(kind), 'minecraft:water_bucket'])

        for kind in sorted(kind_ids - NOT_DYED_IN_A_RING):
            if kind.endswith('_sapling'):
                shapeless('dyeing/%s_%s' % (kind, c), b(kind), 1, ['minecraft:' + vanilla_of[kind], dye])
            else:
                shaped('dyeing/%s_%s' % (kind, c), b(kind), 8, ['SSS', 'SDS', 'SSS'],
                       {'S': 'minecraft:' + vanilla_of[kind], 'D': dye})
            wash(kind)
        for kind in ('crafting_table', 'chest'):
            shapeless('dyeing/%s_%s' % (kind, c), b(kind), 1, ['minecraft:' + kind, dye])
            wash(kind)
        wash('foggy_glass')
        wash('foggy_glass_pane')
        for flower, count in FLOWER_DYES.items():
            shapeless('dye/%s_from_%s' % (c, flower), dye, count, [b(flower)], 'misc')

        smelt('%s_from_smelting' % b('stone').split(':')[1], b('cobblestone'), b('stone'), 0.1)
        smelt('%s_from_smelting' % b('cracked_stone_bricks').split(':')[1], b('stone_bricks'),
              b('cracked_stone_bricks'), 0.1)
        smelt('%s_from_smelting' % b('glass').split(':')[1], b('sand'), b('glass'), 0.1)
        smelt('%s_from_smelting' % b('foggy_glass').split(':')[1], b('glass'), b('foggy_glass'), 0.1)
        smelt('%s_from_smelting' % b('terracotta').split(':')[1], b('clay'), b('terracotta'), 0.35)
        for src, dst in [('stone', 'stone_bricks'), ('sand', 'sandstone'), ('sandstone', 'cut_sandstone')]:
            shaped('%s_%s' % (dst, c), b(dst), 4, ['SS', 'SS'], {'S': b(src)}, True)
        for block, unpacked in [('redstone_block', 'redstone'), ('lapis_block', 'lapis_lazuli')]:
            shapeless('unpacking/%s_%s' % (block, c), 'minecraft:' + unpacked, 9, [b(block)], 'redstone')
        shaped('glass_pane_%s' % c, b('glass_pane'), 16, ['GGG', 'GGG'], {'G': b('glass')}, True)
        shaped('foggy_glass_pane_%s' % c, b('foggy_glass_pane'), 16, ['GGG', 'GGG'], {'G': b('foggy_glass')}, True)

        # The dyed planks of every wood work together, but only in one color. These win over the vanilla recipes
        # that take any planks, so dyed planks make dyed chests and tables (see recipe_priorities.json).
        planks = [b('%s_planks' % wood) for wood in WOODS]
        for wood in WOODS:
            shapeless('%s_planks_%s' % (wood, c), b('%s_planks' % wood), 4, [b('%s_log' % wood)])
            shaped('%s_fence_%s' % (wood, c), b('%s_fence' % wood), 3, ['W#W', 'W#W'],
                   {'W': b('%s_planks' % wood), '#': 'minecraft:stick'}, True)
        shaped('crafting_table_%s' % c, b('crafting_table'), 1, ['PP', 'PP'], {'P': planks}, True)
        shaped('chest_%s' % c, b('chest'), 1, ['PPP', 'P P', 'PPP'], {'P': planks}, True)
        shaped('bookshelf_%s' % c, b('bookshelf'), 1, ['PPP', 'BBB', 'PPP'], {'P': planks, 'B': 'minecraft:book'},
               True)
        shaped('piston_%s' % c, b('piston'), 1, ['PPP', 'CIC', 'CRC'],
               {'P': planks, 'C': b('cobblestone'), 'I': 'minecraft:iron_ingot', 'R': 'minecraft:redstone'}, True)
        shaped('sticky_piston_%s' % c, b('sticky_piston'), 1, ['S', 'P'],
               {'S': 'minecraft:slime_ball', 'P': b('piston')}, True)

        # Slabs, stairs and walls, crafted from the dyed block like vanilla's, and cut from it on a stonecutter if it
        # is mined with a pickaxe. Those that vanilla also has wash back to the vanilla one.
        for kind, shape, base in shapes_list:
            if shape == 'SLAB':
                shaped(kind + '_' + c, b(kind), 6, ['###'], {'#': b(base)}, True)
            elif shape == 'STAIRS':
                shaped(kind + '_' + c, b(kind), 4, ['#  ', '## ', '###'], {'#': b(base)}, True)
            else:
                shaped(kind + '_' + c, b(kind), 6, ['###', '###'], {'#': b(base)}, True)
            if 'minecraft:' + vanilla_of[base] in pickaxe:
                w.write('data/%s/recipe/stonecutting/%s_%s.json' % (MOD, kind, c), {
                    'type': 'minecraft:stonecutting', 'ingredient': b(base),
                    'result': {'count': 2 if shape == 'SLAB' else 1, 'id': b(kind)}})
            if jars.has('data/minecraft/loot_table/blocks/%s.json' % kind):
                wash(kind, kind)

    w.write('data/neoforge/recipe_priorities.json', {'entries': priorities})


def worldgen(jars, w, colors):
    """A tree for each wood and color, grown by its sapling; and in the world, a dye tree about every four chunks and
    a patch of dyed tulips about every other chunk."""
    trees = []
    for wood in WOODS:
        base = jars.json('data/minecraft/worldgen/feature/%s.json' % TREES[wood])
        source_wood = 'oak' if TREES[wood] == 'oak' else wood
        for c in colors:
            name = '%s_%s' % (wood, c)
            ids = {'minecraft:%s_log' % source_wood: '%s:%s_log_%s' % (MOD, wood, c),
                   'minecraft:%s_leaves' % source_wood: '%s:%s_leaves_%s' % (MOD, wood, c)}
            w.write('data/%s/worldgen/feature/%s.json' % (MOD, name), remap_ids(base, ids))
            trees.append({'feature': '%s:%s' % (MOD, name), 'placement': []})
    w.write('data/%s/worldgen/feature/dye_tree.json' % MOD, {'type': 'minecraft:simple_random_selector',
                                                             'features': trees})
    w.write('data/%s/worldgen/placed_feature/dye_trees.json' % MOD, {
        'feature': MOD + ':dye_tree',
        'placement': [
            {'type': 'minecraft:rarity_filter', 'chance': 4},
            {'type': 'minecraft:in_square'},
            {'type': 'minecraft:surface_water_depth_filter', 'max_water_depth': 0},
            {'type': 'minecraft:heightmap', 'heightmap': 'OCEAN_FLOOR'},
            {'type': 'minecraft:block_predicate_filter',
             'predicate': {'type': 'minecraft:would_survive', 'state': 'minecraft:oak_sapling'}},
            {'type': 'minecraft:biome'},
        ]})

    w.write('data/%s/worldgen/feature/tulips.json' % MOD, {
        'type': 'minecraft:simple_block',
        'to_place': {'type': 'minecraft:weighted',
                     'entries': [{'data': '%s:tulip_%s' % (MOD, c), 'weight': 1} for c in colors]}})
    flowers = jars.json('data/minecraft/worldgen/placed_feature/flower_default.json')['placement']
    placement = []
    for step in flowers:
        step = dict(step)
        if step['type'] == 'minecraft:rarity_filter':
            step['chance'] = 2
        elif step['type'] == 'minecraft:count':
            step['count'] = 32
        placement.append(step)
    w.write('data/%s/worldgen/placed_feature/tulips.json' % MOD, {'feature': MOD + ':tulips', 'placement': placement})

    # One biome modifier for both, not one each: NeoForge loads biome modifiers on several threads at once, and two
    # modifiers that both wrap the same biome tags in an "or" can register listeners on a tag at the same time, which
    # sometimes crashes registry loading (HolderSet$Named.addInvalidationListener, ArrayIndexOutOfBoundsException).
    w.write('data/%s/neoforge/biome_modifier/dye_trees_and_tulips.json' % MOD, {
        'type': 'neoforge:add_features', 'biomes': WORLDGEN_BIOMES,
        'features': ['%s:dye_trees' % MOD, '%s:tulips' % MOD], 'step': 'vegetal_decoration'})


if __name__ == '__main__':
    main()

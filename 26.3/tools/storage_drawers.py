#!/usr/bin/env python3
"""Builds the textures and models of the dyed Storage Drawers (compat/storagedrawers) from Storage Drawers' oak drawers
(MIT licensed, (c) Texelsaur):

  * grey drawer textures, made like the other grey textures (see textures.py);
  * Storage Drawers' two drawer shapes (full_drawers.json and half_drawers.json) with every face marked with
    tintindex 0, so the whole drawer takes the dye color;
  * a model per drawer size that puts the grey textures on its shape, the way Storage Drawers' oak drawer of that size
    puts its oak ones.

They go in src/main/resources. The rest (blockstates, item models, recipes, tags and names) comes from
generate_resources.py, which needs no Storage Drawers jar. Run after `./gradlew build` has downloaded the Storage
Drawers version named in gradle.properties (needs Pillow):
    python3 tools/storage_drawers.py
"""
import glob
import io
import json
import os
import re
import sys
import zipfile

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from textures import brighten, grey, save  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MODELS = os.path.join(ROOT, 'src/main/resources/assets/moredyes/models/block/drawers')
JAVA = os.path.join(ROOT, 'src/main/java/net/neverandy/moredyes/compat/storagedrawers/StorageDrawersCompat.java')
SD_TEXTURE = re.compile(r'^storagedrawers:block/drawers_oak_(\w+)$')


def read_sizes():
    source = open(JAVA).read()
    return re.findall(r'"(\w+)"', re.search(r'SIZES\s*=.*?;', source, re.S).group(0))


def storage_drawers_jar():
    """The Storage Drawers jar of exactly the version the build uses."""
    version = re.search(r'^storage_drawers_version=(.+)$', open(os.path.join(ROOT, 'gradle.properties')).read(),
                        re.M).group(1).strip()
    jars = glob.glob(os.path.expanduser('~/.gradle/caches/modules-2/files-2.1/maven.modrinth/storagedrawers/%s/*/'
                                        'storagedrawers-%s.jar' % (glob.escape(version), glob.escape(version))))
    if not jars:
        sys.exit('The Storage Drawers %s jar was not found; run ./gradlew build first.' % version)
    return zipfile.ZipFile(jars[0])


def write(name, data):
    path = os.path.join(MODELS, name + '.json')
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w') as f:
        json.dump(data, f, indent=2)
        f.write('\n')


def main():
    from PIL import Image

    jar = storage_drawers_jar()

    def model(name):
        return json.loads(jar.read('assets/storagedrawers/models/block/%s.json' % name))

    # The two shapes, every face tinted.
    for depth in ('full', 'half'):
        shape = model('%s_drawers' % depth)
        if ':' not in shape['parent']:
            shape['parent'] = 'minecraft:' + shape['parent']
        for element in shape['elements']:
            element.pop('__comment', None)
            for face in element['faces'].values():
                face['tintindex'] = 0
        write('%s_drawers' % depth, shape)

    # A model per size, with the textures of the oak drawer of that size swapped for grey ones.
    textures = set()
    for size in read_sizes():
        depth = size.split('_')[0]
        oak = model('oak_' + size)
        orientable = model(oak['parent'].split('/')[-1])
        if orientable['parent'] != 'storagedrawers:block/%s_drawers' % depth:
            sys.exit('Unexpected parent of %s: %s' % (oak['parent'], orientable['parent']))
        own = {}
        for key, texture in oak['textures'].items():
            m = SD_TEXTURE.match(texture)
            if not m:
                sys.exit('Unexpected texture in oak_%s: %s' % (size, texture))
            own[key] = 'moredyes:block/drawers_' + m.group(1)
            textures.add(m.group(1))
        resolved = {key: own[value[1:]] if value.startswith('#') else value
                    for key, value in orientable['textures'].items()}
        resolved['trim'] = own['trim']
        write(size, {'parent': 'moredyes:block/drawers/%s_drawers' % depth, 'textures': resolved})

    for name in sorted(textures):
        image = Image.open(io.BytesIO(jar.read('assets/storagedrawers/textures/block/drawers_oak_%s.png' % name)))
        image = image.convert('RGBA')
        image.putdata(brighten([grey(p) for p in image.get_flattened_data()]))
        save(image, 'block/drawers_' + name)
    print('wrote %d drawer textures and %d drawer models' % (len(textures), len(read_sizes()) + 2))


if __name__ == '__main__':
    main()

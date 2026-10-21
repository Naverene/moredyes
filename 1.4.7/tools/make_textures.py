#!/usr/bin/env python3
"""Builds the 1.4.7 texture sheets from the Minecraft 1.7.10 textures.

Minecraft 1.4.7 reads block and item textures from 256x256 sheets of 16x16 tiles, so every grey texture the mod
tints goes into one sheet, moredyes/terrain.png. The sources and transforms are the same as in the 1.7.10 version
(1.7.10/src/main/java/info/kg6jay/moredyes/client/TintSources.java), which makes them from the vanilla textures at
runtime; 1.4.7 has no per-texture files to do that with, so they are made here and committed.

Writes:
  src/main/resources/moredyes/terrain.png     the sheet
  src/main/resources/moredyes/chest.png       grey single chest model texture
  src/main/resources/moredyes/largechest.png  grey double chest model texture
  src/main/resources/moredyes/sign.png        grey sign model texture
  src/main/java/info/kg6jay/moredyes/Textures.java  the index of each texture in the sheet

Usage: python3 tools/make_textures.py [path to a Minecraft 1.7.10 client jar]
Without a path, the jar is downloaded from Mojang into tools/.cache.
"""
import colorsys
import io
import json
import os
import sys
import urllib.request
import zipfile

from PIL import Image

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
BASE = os.path.join(ROOT, "..", "1.7.10", "src", "main", "resources", "assets", "moredyes", "textures", "blocks",
                    "base")
# The grey textures of the 1.16.5 version, for the blocks added after 1.7.10's set (end stone, ice, the other woods'
# planks, the flowers and so on); their petals and stems are already split into a tinted and a natural layer.
TINTED = os.path.join(ROOT, "..", "1.16.5", "src", "main", "resources", "assets", "moredyes", "textures", "block",
                      "tinted")
MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest.json"


def grey_px(p):
    r, g, b, a = p
    lum = min(255, round(0.299 * r + 0.587 * g + 0.114 * b))
    return (lum, lum, lum, a)


def is_green(p):
    r, g, b, a = p
    if a == 0:
        return False
    h, s, v = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
    return s > 0.15 and 65 <= h * 360 <= 170


def grey(p):
    return grey_px(p)


def grey_if_green(p):
    return grey_px(p) if is_green(p) else (0, 0, 0, 0)


def grey_if_not_green(p):
    return (0, 0, 0, 0) if is_green(p) else grey_px(p)


def keep_if_green(p):
    return p if is_green(p) else (0, 0, 0, 0)


def keep_if_not_green(p):
    return (0, 0, 0, 0) if is_green(p) else p


def keep(p):
    return p


def grey_brighter(factor):
    def f(p):
        lum = min(255, round(grey_px(p)[0] * factor))
        return (lum, lum, lum, p[3])
    return f


# (key, source, transform). A source starting with "base/" is one of the mod's own grey textures, one starting with
# "tinted/" is one of the 1.16.5 version's; anything else is a vanilla 1.7.10 path under assets/minecraft/textures. Same as TintSources in the 1.7.10 version, plus the untinted
# log bark, which 1.7.10 takes straight from vanilla.
SOURCES = [
    ("wool", "blocks/wool_colored_white", grey),
    ("stone", "blocks/stone", grey),
    ("cobble", "blocks/cobblestone", grey),
    # Left empty: Minecraft 1.4.7 never tints the sides of a block whose main texture is number 3 (grass) when smooth
    # lighting is on.
    (None, None, None),
    ("stonebrick", "blocks/stonebrick", grey),
    ("stonebrickCarved", "blocks/stonebrick_carved", grey),
    ("stonebrickCracked", "blocks/stonebrick_cracked", grey),
    ("obsidian", "blocks/obsidian", grey),
    ("soulsand", "blocks/soul_sand", grey),
    ("quartz/top", "blocks/quartz_block_top", grey),
    ("quartz/side", "blocks/quartz_block_side", grey),
    ("quartz/bottom", "blocks/quartz_block_bottom", grey),
    ("clay", "blocks/hardened_clay_stained_white", grey),
    ("hardenedClay", "blocks/hardened_clay", grey),
    ("coal", "blocks/coal_block", grey),
    ("glowstone", "blocks/glowstone", grey),
    ("lapis", "blocks/lapis_block", grey),
    ("redstone", "blocks/redstone_block", grey),
    ("plank", "blocks/planks_oak", grey),
    ("brick", "blocks/brick", grey),
    ("sand", "blocks/sand", grey),
    ("sandstone/top", "blocks/sandstone_top", grey),
    ("sandstone/side", "blocks/sandstone_normal", grey),
    ("sandstone/bottom", "blocks/sandstone_bottom", grey),
    ("bookshelf", "blocks/bookshelf", grey),
    ("workbench/top", "blocks/crafting_table_top", grey),
    ("workbench/side", "blocks/crafting_table_side", grey),
    ("workbench/front", "blocks/crafting_table_front", grey),
    ("log/top", "blocks/log_oak_top", grey),
    ("log/bark", "blocks/log_oak", keep),
    ("leaf", "blocks/leaves_oak", grey),
    ("glass/clear", "blocks/glass", grey),
    ("glass/pane", "blocks/glass_pane_top", grey),
    ("glass/foggy", "base/glass_foggy", grey),
    ("diorite", "base/diorite", grey),
    ("smoothStone", "blocks/stone_slab_top", grey),
    ("smoothSandstone", "blocks/sandstone_top", grey),
    ("smoothQuartz", "blocks/quartz_block_bottom", grey),
    ("cutSandstone/top", "blocks/sandstone_top", grey),
    ("cutSandstone/side", "blocks/sandstone_smooth", grey),
    ("cutSandstone/bottom", "blocks/sandstone_top", grey),
    ("mossyCobble", "blocks/cobblestone_mossy", grey),
    ("mossyStonebrick", "blocks/stonebrick_mossy", grey),
    ("netherBrick", "blocks/nether_brick", grey_brighter(4.0)),
    ("granite", "base/granite", grey),
    ("andesite", "base/andesite", grey),
    ("polishedGranite", "base/polishedGranite", grey),
    ("polishedDiorite", "base/polishedDiorite", grey),
    ("polishedAndesite", "base/polishedAndesite", grey),
    ("concrete", "base/concrete", grey),
    ("concretePowder", "base/concretePowder", grey),
    ("soulSoil", "base/soulSoil", grey),
    ("basalt/top", "base/basalt/top", grey),
    ("basalt/side", "base/basalt/side", grey),
    ("basalt/bottom", "base/basalt/top", grey),
    ("polishedBasalt/top", "base/polishedBasalt/top", grey),
    ("polishedBasalt/side", "base/polishedBasalt/side", grey),
    ("polishedBasalt/bottom", "base/polishedBasalt/top", grey),
    ("crackedNetherBrick", "base/crackedNetherBrick", grey),
    ("chiseledNetherBrick", "base/chiseledNetherBrick", grey),
    ("endStoneBrick", "base/endStoneBrick", grey),
    ("boneBlock/top", "base/boneBlock/top", grey),
    ("boneBlock/side", "base/boneBlock/side", grey),
    ("boneBlock/bottom", "base/boneBlock/top", grey),
    ("glazedTerracotta", "base/glazedTerracotta", grey),
    ("netheriteBlock", "base/netheriteBlock", grey),
    ("cryingObsidian", "base/cryingObsidian", grey),
    ("ironTrapdoor", "base/ironTrapdoor", grey),
    ("chain", "base/chain", grey),
    ("cornflower/petals", "base/cornflower", grey_if_not_green),
    ("cornflower/stem", "base/cornflower", keep_if_green),
    ("tulip/petals", "blocks/flower_tulip_white", grey_if_not_green),
    ("tulip/stem", "blocks/flower_tulip_white", keep_if_green),
    ("sapling/leaves", "blocks/sapling_oak", grey_if_green),
    ("sapling/trunk", "blocks/sapling_oak", keep_if_not_green),
    ("dye", "items/dye_powder_white", grey),
    # The blocks the modern versions have that this one lacked, from the 1.16.5 textures.
    ("endStone", "tinted/endstone", grey),
    ("quartzBricks", "tinted/quartz_bricks", grey),
    ("chiseledQuartz/top", "tinted/quartz_chiseled_top", grey),
    ("chiseledQuartz/side", "tinted/quartz_chiseled", grey),
    ("quartzPillar/top", "tinted/quartz_pillar_top", grey),
    ("quartzPillar/side", "tinted/quartz_pillar", grey),
    ("gravel", "tinted/gravel", grey),
    ("ice", "tinted/ice", grey),
    ("packedIce", "tinted/packed_ice", grey),
    ("snow", "tinted/snow", grey),
    ("chiseledSandstone", "tinted/sandstone_carved", grey),
    ("birchPlank", "tinted/birch_planks", grey),
    ("sprucePlank", "tinted/spruce_planks", grey),
    ("junglePlank", "tinted/jungle_planks", grey),
    ("acaciaPlank", "tinted/acacia_planks", grey),
    ("darkOakPlank", "tinted/dark_oak_planks", grey),
    ("allium/petals", "tinted/allium_petals", grey),
    ("allium/stem", "tinted/allium_stem", keep),
    ("azureBluet/petals", "tinted/azurebluet_petals", grey),
    ("azureBluet/stem", "tinted/azurebluet_stem", keep),
    ("blueOrchid/petals", "tinted/orchid_petals", grey),
    ("blueOrchid/stem", "tinted/orchid_stem", keep),
    ("dandelion/petals", "tinted/dandelion_petals", grey),
    ("dandelion/stem", "tinted/dandelion_stem", keep),
    ("lilyOfTheValley/petals", "tinted/lilyofthevalley_petals", grey),
    ("lilyOfTheValley/stem", "tinted/lilyofthevalley_stem", keep),
    ("oxeyeDaisy/petals", "tinted/oxeyedaisy_petals", grey),
    ("oxeyeDaisy/stem", "tinted/oxeyedaisy_stem", keep),
    ("poppy/petals", "tinted/poppy_petals", grey),
    ("poppy/stem", "tinted/poppy_stem", keep),
    ("lilac/top/petals", "tinted/lilac_top_petals", grey),
    ("lilac/top/stem", "tinted/lilac_top_stem", keep),
    ("lilac/bottom/petals", "tinted/lilac_bottom_petals", grey),
    ("lilac/bottom/stem", "tinted/lilac_bottom_stem", keep),
    ("peony/top/petals", "tinted/peony_top_petals", grey),
    ("peony/top/stem", "tinted/peony_top_stem", keep),
    ("peony/bottom/petals", "tinted/peony_bottom_petals", grey),
    ("peony/bottom/stem", "tinted/peony_bottom_stem", keep),
    ("roseBush/top/petals", "tinted/rosebush_top_petals", grey),
    ("roseBush/top/stem", "tinted/rosebush_top_stem", keep),
    ("roseBush/bottom/petals", "tinted/rosebush_bottom_petals", grey),
    ("roseBush/bottom/stem", "tinted/rosebush_bottom_stem", keep),
    ("signItem", "tinted/sign", grey),
    # Untinted: the pot and its soil, drawn around a dyed plant in a flower pot.
    ("flowerPot", "blocks/flower_pot", keep),
    ("flowerPotDirt", "blocks/dirt", keep),
    # GregTech's red granite textures (in the 1.7.10 version's base textures; LGPL-3.0, see THIRD_PARTY_NOTICES.md).
    ("speckledGranite", "base/speckledGranite", grey),
    ("smoothSpeckledGranite", "base/smoothSpeckledGranite", grey),
]

MODELS = [
    ("chest.png", "entity/chest/normal"),
    ("largechest.png", "entity/chest/normal_double"),
    ("sign.png", "entity/sign"),
]


def client_jar():
    if len(sys.argv) > 1:
        return sys.argv[1]
    cache = os.path.join(HERE, ".cache")
    path = os.path.join(cache, "client-1.7.10.jar")
    if not os.path.exists(path):
        os.makedirs(cache, exist_ok=True)
        manifest = json.load(urllib.request.urlopen(MANIFEST))
        version_url = next(v["url"] for v in manifest["versions"] if v["id"] == "1.7.10")
        jar_url = json.load(urllib.request.urlopen(version_url))["downloads"]["client"]["url"]
        urllib.request.urlretrieve(jar_url, path)
    return path


def load(jar, source):
    if source.startswith("base/"):
        return Image.open(os.path.join(BASE, source[5:] + ".png")).convert("RGBA")
    if source.startswith("tinted/"):
        return Image.open(os.path.join(TINTED, source[7:] + ".png")).convert("RGBA")
    return Image.open(io.BytesIO(jar.read("assets/minecraft/textures/" + source + ".png"))).convert("RGBA")


def transform(image, f):
    out = Image.new("RGBA", image.size)
    out.putdata([f(p) for p in image.getdata()])
    return out


def constant(key):
    name = ""
    for i, c in enumerate(key.replace("/", "_")):
        if c.isupper() and i > 0 and name[-1] != "_":
            name += "_"
        name += c.upper()
    return name


def main():
    jar = zipfile.ZipFile(client_jar())
    sheet = Image.new("RGBA", (256, 256))
    for index, (key, source, f) in enumerate(SOURCES):
        if key is None:
            continue
        image = load(jar, source)
        # Animated or larger textures: take the first 16x16 frame.
        image = image.crop((0, 0, image.width, image.width)).resize((16, 16), Image.NEAREST)
        sheet.paste(transform(image, f), (index % 16 * 16, index // 16 * 16))
    out = os.path.join(ROOT, "src", "main", "resources", "moredyes")
    os.makedirs(out, exist_ok=True)
    sheet.save(os.path.join(out, "terrain.png"), optimize=True)
    for name, source in MODELS:
        transform(load(jar, source), grey).save(os.path.join(out, name), optimize=True)

    lines = [
        "package info.kg6jay.moredyes;",
        "",
        "/** Generated by tools/make_textures.py: where each texture is in the sheet " + "moredyes/terrain.png. */",
        "public final class Textures {",
        "",
        "    public static final String SHEET = \"/moredyes/terrain.png\";",
        "    public static final String CHEST = \"/moredyes/chest.png\";",
        "    public static final String LARGE_CHEST = \"/moredyes/largechest.png\";",
        "    public static final String SIGN = \"/moredyes/sign.png\";",
        "",
    ]
    for index, (key, _, _) in enumerate(SOURCES):
        if key is not None:
            lines.append("    public static final int %s = %d;" % (constant(key), index))
    lines += ["", "    private Textures() {}", "}", ""]
    java = os.path.join(ROOT, "src", "main", "java", "info", "kg6jay", "moredyes", "Textures.java")
    with open(java, "w") as f:
        f.write("\n".join(lines))
    print("Wrote %d textures" % len(SOURCES))


if __name__ == "__main__":
    main()

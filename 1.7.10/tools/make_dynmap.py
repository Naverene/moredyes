#!/usr/bin/env python3
"""Writes the files GTNH Web Map (the GT: New Horizons fork of Dynmap) reads to draw More Dyes blocks:
src/main/resources/assets/moredyes/dynmap-texture.txt (each block's textures and tint) and dynmap-models.txt (the
shapes of the blocks that are not full cubes). Run it again after adding or changing a block.

The game tints grey textures it makes when it loads (client/TintSources), and the map has none of those, so the
textures here name the same sources and have compat/dynmap/TintedTextureProcessor make the grey versions. The tint is
DyeColorMultiplier, which reads the shade from the metadata or, for stairs, slabs, walls, trapdoors, pistons and the
dyed Iron Chests and Storage Drawers, from the tile entity.
"""

from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "src/main/resources/assets/moredyes"
PACKAGE = "info.kg6jay.moredyes.compat.dynmap"
TINT = f"custColorMult={PACKAGE}.DyeColorMultiplier"
PROCESSOR = f"processor={PACKAGE}.TintedTextureProcessor"

# MDBlock.colors: the color sets, each registered as "<set>Mix<Suffix>" with the shade in the metadata.
COLOR_SETS = ["white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "darkGray", "lightGray", "cyan",
              "purple", "blue", "brown", "green", "red"]

# Texture modifiers (Dynmap's 1000s codes): tinted with the color multiplier, the same but see-through between
# blocks of one kind (glass), and rotated by 90, 180 or 270 degrees.
TINTED, TINTED_GLASS, ROT90, ROT180, ROT270 = 17000, 21000, 4000, 5000, 6000


def vanilla(name):
    return f"assets/minecraft/textures/blocks/{name}.png"


def base(name):
    return f"assets/moredyes/textures/blocks/base/{name}.png"


# The sources of client/TintSources: key -> (file, transform). Transforms are TintedTextureProcessor's.
SOURCES = {
    "wool": (vanilla("wool_colored_white"), "grey"),
    "stone": (vanilla("stone"), "grey"),
    "cobble": (vanilla("cobblestone"), "grey"),
    "stonebrick": (vanilla("stonebrick"), "grey"),
    "stonebrickCarved": (vanilla("stonebrick_carved"), "grey"),
    "stonebrickCracked": (vanilla("stonebrick_cracked"), "grey"),
    "obsidian": (vanilla("obsidian"), "grey"),
    "soulsand": (vanilla("soul_sand"), "grey"),
    "quartz/top": (vanilla("quartz_block_top"), "grey"),
    "quartz/side": (vanilla("quartz_block_side"), "grey"),
    "quartz/bottom": (vanilla("quartz_block_bottom"), "grey"),
    "clay": (vanilla("hardened_clay_stained_white"), "grey"),
    "hardenedClay": (vanilla("hardened_clay"), "grey"),
    "coal": (vanilla("coal_block"), "grey"),
    "glowstone": (vanilla("glowstone"), "grey"),
    "lapis": (vanilla("lapis_block"), "grey"),
    "redstone": (vanilla("redstone_block"), "grey"),
    "plank": (vanilla("planks_oak"), "grey"),
    "brick": (vanilla("brick"), "grey"),
    "sand": (vanilla("sand"), "grey"),
    "sandstone/top": (vanilla("sandstone_top"), "grey"),
    "sandstone/side": (vanilla("sandstone_normal"), "grey"),
    "sandstone/bottom": (vanilla("sandstone_bottom"), "grey"),
    "bookshelf": (vanilla("bookshelf"), "grey"),
    "workbench/top": (vanilla("crafting_table_top"), "grey"),
    "workbench/side": (vanilla("crafting_table_side"), "grey"),
    "workbench/front": (vanilla("crafting_table_front"), "grey"),
    "log/top": (vanilla("log_oak_top"), "grey"),
    "leaf": (vanilla("leaves_oak"), "grey"),
    "glass/clear": (vanilla("glass"), "grey"),
    "glass/pane": (vanilla("glass_pane_top"), "grey"),
    "glass/foggy": (base("glass_foggy"), "grey"),
    "diorite": (base("diorite"), "grey"),
    "smoothStone": (vanilla("stone_slab_top"), "grey"),
    "smoothSandstone": (vanilla("sandstone_top"), "grey"),
    "smoothQuartz": (vanilla("quartz_block_bottom"), "grey"),
    "cutSandstone/top": (vanilla("sandstone_top"), "grey"),
    "cutSandstone/side": (vanilla("sandstone_smooth"), "grey"),
    "cutSandstone/bottom": (vanilla("sandstone_top"), "grey"),
    "mossyCobble": (vanilla("cobblestone_mossy"), "grey"),
    "mossyStonebrick": (vanilla("stonebrick_mossy"), "grey"),
    "netherBrick": (vanilla("nether_brick"), "greyBrighter4"),
    "granite": (base("granite"), "grey"),
    "andesite": (base("andesite"), "grey"),
    "polishedGranite": (base("polishedGranite"), "grey"),
    "speckledGranite": (base("speckledGranite"), "grey"),
    "smoothSpeckledGranite": (base("smoothSpeckledGranite"), "grey"),
    "polishedDiorite": (base("polishedDiorite"), "grey"),
    "polishedAndesite": (base("polishedAndesite"), "grey"),
    "concrete": (base("concrete"), "grey"),
    "concretePowder": (base("concretePowder"), "grey"),
    "soulSoil": (base("soulSoil"), "grey"),
    "basalt/top": (base("basalt/top"), "grey"),
    "basalt/side": (base("basalt/side"), "grey"),
    "basalt/bottom": (base("basalt/top"), "grey"),
    "polishedBasalt/top": (base("polishedBasalt/top"), "grey"),
    "polishedBasalt/side": (base("polishedBasalt/side"), "grey"),
    "polishedBasalt/bottom": (base("polishedBasalt/top"), "grey"),
    "crackedNetherBrick": (base("crackedNetherBrick"), "grey"),
    "chiseledNetherBrick": (base("chiseledNetherBrick"), "grey"),
    "endStoneBrick": (base("endStoneBrick"), "grey"),
    "boneBlock/top": (base("boneBlock/top"), "grey"),
    "boneBlock/side": (base("boneBlock/side"), "grey"),
    "boneBlock/bottom": (base("boneBlock/top"), "grey"),
    "glazedTerracotta": (base("glazedTerracotta"), "grey"),
    "netheriteBlock": (base("netheriteBlock"), "grey"),
    "cryingObsidian": (base("cryingObsidian"), "grey"),
    "ironTrapdoor": (base("ironTrapdoor"), "grey"),
    "chain": (base("chain"), "grey"),
    "piston/side": (vanilla("piston_side"), "grey"),
    "piston/inner": (vanilla("piston_inner"), "grey"),
    "piston/bottom": (vanilla("piston_bottom"), "grey"),
    "piston/sideOverlay": (vanilla("piston_side"), "pistonSideTrim"),
    "piston/innerOverlay": (vanilla("piston_inner"), "pistonInnerTrim"),
    "piston/bottomOverlay": (vanilla("piston_bottom"), "pistonBottomTrim"),
    "cornflower/petals": (base("cornflower"), "greyIfNotGreen"),
    "cornflower/stem": (base("cornflower"), "keepIfGreen"),
    "tulip/petals": (vanilla("flower_tulip_white"), "greyIfNotGreen"),
    "tulip/stem": (vanilla("flower_tulip_white"), "keepIfGreen"),
    "sapling/leaves": (vanilla("sapling_oak"), "greyIfGreen"),
    "sapling/trunk": (vanilla("sapling_oak"), "keepIfNotGreen"),
}
# The chest model texture, cut down to the lid's top and the front of the base (pixels of the 64 wide texture).
CHEST = "assets/minecraft/textures/entity/chest/normal.png"
CROPPED = {
    "chest/top": (CHEST, "14:0:14:14"),
    "chest/side": (CHEST, "14:33:14:10"),
}
# The dyed Iron Chests (compat/ironchest), one block per tier, drawn like the chest above from the tier's model
# texture, with its grey panels tinted and its frame and latch drawn over them untinted.
IRON_CHEST_TIERS = ["Iron", "Gold", "Diamond", "Copper", "Silver"]
# The dyed Storage Drawers (compat/storagedrawers): registry name -> (front texture, half depth). Their textures are
# made from Storage Drawers' oak drawers, as in the game.
DRAWERS = {
    "dyedFullDrawers1": ("front_1", False), "dyedFullDrawers2": ("front_2", False),
    "dyedFullDrawers4": ("front_4", False), "dyedHalfDrawers2": ("front_2", True),
    "dyedHalfDrawers4": ("front_4", True),
}
DRAWER_PARTS = ["front_1", "front_2", "front_4", "side", "side_h", "side_v"]
# Untinted textures used as they are.
NATURAL = {
    "log_oak": vanilla("log_oak"),
    "piston_top_normal": vanilla("piston_top_normal"),
    "piston_top_sticky": vanilla("piston_top_sticky"),
}

# Full cubes made per color set: registry suffix -> texture key, or (top, side, bottom) keys.
CUBES = {
    "Wool": "wool", "Stonebrick": "stonebrick", "StonebrickCarved": "stonebrickCarved",
    "StonebrickCracked": "stonebrickCracked", "Stone": "stone", "Cobble": "cobble", "Obsidian": "obsidian",
    "Soulsand": "soulsand", "Quartz": ("quartz/top", "quartz/side", "quartz/bottom"), "Clay": "clay", "Coal": "coal",
    "Glowstone": "glowstone", "Lapis": "lapis", "Redstone": "redstone", "Plank": "plank", "Brick": "brick",
    "Sand": "sand", "HardenedClay": "hardenedClay",
    "Sandstone": ("sandstone/top", "sandstone/side", "sandstone/bottom"),
    "Bookshelf": ("plank", "bookshelf", "plank"), "Diorite": "diorite", "Granite": "granite", "Andesite": "andesite",
    "PolishedGranite": "polishedGranite", "SpeckledGranite": "speckledGranite",
    "SmoothSpeckledGranite": "smoothSpeckledGranite", "PolishedDiorite": "polishedDiorite", "PolishedAndesite": "polishedAndesite",
    "Concrete": "concrete", "ConcretePowder": "concretePowder", "SoulSoil": "soulSoil",
    "Basalt": ("basalt/top", "basalt/side", "basalt/bottom"),
    "PolishedBasalt": ("polishedBasalt/top", "polishedBasalt/side", "polishedBasalt/bottom"),
    "SmoothStone": "smoothStone", "SmoothSandstone": "smoothSandstone", "SmoothQuartz": "smoothQuartz",
    "CutSandstone": ("cutSandstone/top", "cutSandstone/side", "cutSandstone/bottom"), "MossyCobble": "mossyCobble",
    "MossyStonebrick": "mossyStonebrick", "NetherBrick": "netherBrick", "CrackedNetherBrick": "crackedNetherBrick",
    "ChiseledNetherBrick": "chiseledNetherBrick", "EndStoneBrick": "endStoneBrick",
    "BoneBlock": ("boneBlock/top", "boneBlock/side", "boneBlock/bottom"), "GlazedTerracotta": "glazedTerracotta",
    "NetheriteBlock": "netheriteBlock", "CryingObsidian": "cryingObsidian",
}
# Plain granite, diorite and andesite, which have one fixed tint (MDBlock.initialize).
PLAIN_STONES = {"granite": "C89680", "diorite": "FFFFFF", "andesite": "FFFFFF"}
# Plants made per color set: suffix -> (tinted texture, untinted texture drawn over it).
PLANTS = {
    "Tulip": ("tulip/petals", "tulip/stem"),
    "Cornflower": ("cornflower/petals", "cornflower/stem"),
    "Sapling": ("sapling/leaves", "sapling/trunk"),
}
# MDBlock.SHAPES: registry name -> texture key, or (top, side, bottom) keys.
STAIRS = {
    "stoneStairs": "stone", "graniteStairs": "granite", "polishedGraniteStairs": "polishedGranite",
    "dioriteStairs": "diorite", "polishedDioriteStairs": "polishedDiorite", "andesiteStairs": "andesite",
    "polishedAndesiteStairs": "polishedAndesite", "mossyCobbleStairs": "mossyCobble",
    "mossyStonebrickStairs": "mossyStonebrick", "endStoneBrickStairs": "endStoneBrick",
    "smoothSandstoneStairs": "smoothSandstone", "smoothQuartzStairs": "smoothQuartz",
    "netherBrickStairs": "netherBrick", "speckledGraniteStairs": "speckledGranite",
    "smoothSpeckledGraniteStairs": "smoothSpeckledGranite",
}
SLABS = {
    "stoneSlab": "stone", "cutSandstoneSlab": ("cutSandstone/top", "cutSandstone/side", "cutSandstone/bottom"),
    "dioriteSlab": "diorite", "speckledGraniteSlab": "speckledGranite", "smoothSpeckledGraniteSlab": "smoothSpeckledGranite",
}
WALLS = {
    "brickWall": "brick", "stonebrickWall": "stonebrick", "mossyStonebrickWall": "mossyStonebrick",
    "graniteWall": "granite", "dioriteWall": "diorite", "andesiteWall": "andesite", "netherBrickWall": "netherBrick",
    "sandstoneWall": ("sandstone/top", "sandstone/side", "sandstone/bottom"), "endStoneBrickWall": "endStoneBrick",
}

# Directions by Minecraft side number (0 down, 1 up, 2 north, 3 south, 4 west, 5 east), which are also the patch
# numbers of TileColorBoxRenderer's faces.
DIRECTIONS = [(0, -1, 0), (0, 1, 0), (0, 0, -1), (0, 0, 1), (-1, 0, 0), (1, 0, 0)]
# For each face of a box, by side number, the directions of its texture's U (left to right) and V (bottom to top), as
# CustomRenderer.addBox lays them out.
FACE_AXES = [
    ((1, 0, 0), (0, 0, 1)),
    ((1, 0, 0), (0, 0, -1)),
    ((-1, 0, 0), (0, 1, 0)),
    ((1, 0, 0), (0, 1, 0)),
    ((0, 0, 1), (0, 1, 0)),
    ((0, 0, -1), (0, 1, 0)),
]


def tid(key):
    """The texture id in the texture file."""
    return "moredyes/" + key


def names(suffix):
    return [f"%{s}Mix{suffix}" for s in COLOR_SETS]


def ids(block_names):
    return ",".join(f"id={n}" for n in block_names)


def top_side_bottom(textures):
    return textures if isinstance(textures, tuple) else (textures,) * 3


def dot(a, b):
    return sum(x * y for x, y in zip(a, b))


def rotation_towards(face, direction):
    """The modifier that turns a texture on this face so its top points the given way."""
    u, v = FACE_AXES[face]
    if dot(v, direction) > 0:
        return 0
    if dot(v, direction) < 0:
        return ROT180
    return ROT270 if dot(u, direction) > 0 else ROT90


def box(xmin=0.0, xmax=1.0, ymin=0.0, ymax=1.0, zmin=0.0, zmax=1.0):
    return f"xmin={xmin},xmax={xmax},ymin={ymin},ymax={ymax},zmin={zmin},zmax={zmax}"


def facing_box(facing, depth):
    """A box filling the block except for depth on the side it faces (or only that much of it, when negative)."""
    bounds = {"xmin": 0.0, "xmax": 1.0, "ymin": 0.0, "ymax": 1.0, "zmin": 0.0, "zmax": 1.0}
    axis = "xyz"[[i for i, d in enumerate(DIRECTIONS[facing]) if d][0]]
    positive = sum(DIRECTIONS[facing]) > 0
    if depth > 0:
        bounds[axis + ("max" if positive else "min")] = 1.0 - depth if positive else depth
    else:
        bounds[axis + ("min" if positive else "max")] = 1.0 + depth if positive else -depth
    return box(**bounds)


def texture_file():
    lines = [
        "# More Dyes blocks for GTNH Web Map. Made by tools/make_dynmap.py; edit that instead.",
        "modname:moredyes",
        "",
    ]
    for key, (path, transform) in SOURCES.items():
        lines.append(f"texture:id={tid(key)},filename={path},{PROCESSOR},transform={transform}")
    for key, (path, crop) in CROPPED.items():
        lines.append(f"texture:id={tid(key)},filename={path},{PROCESSOR},crop={crop}")
    for key, path in NATURAL.items():
        lines.append(f"texture:id={tid(key)},filename={path}")
    # Only loaded when Iron Chests or Storage Drawers is installed: the map skips textures no known block uses.
    for tier in IRON_CHEST_TIERS:
        path = f"assets/ironchest/textures/model/{tier.lower()}chest.png"
        for part, crop in (("top", CROPPED["chest/top"][1]), ("side", CROPPED["chest/side"][1])):
            for transform, suffix in (("chestPanels", ""), ("chestTrim", "Trim")):
                lines.append(f"texture:id={tid(f'ironchest/{tier.lower()}/{part}{suffix}')},filename={path},"
                             f"{PROCESSOR},transform={transform},crop={crop}")
    for part in DRAWER_PARTS:
        lines.append(f"texture:id={tid('drawers/' + part)},"
                     f"filename=assets/storagedrawers/textures/blocks/drawers_oak_{part}.png,{PROCESSOR}")
    lines.append("")

    def t(key, modifier=TINTED):
        return f"{modifier}:{tid(key)}"

    def cube(block_ids, textures, extra=TINT):
        top, side, bottom = top_side_bottom(textures)
        return f"block:{block_ids},data=*,face0={t(bottom)},face1={t(top)},allsides={t(side)},stdrot=true,{extra}"

    lines.append("# Full blocks, one per color set with the shade in the metadata")
    for suffix, textures in CUBES.items():
        lines.append(cube(ids(names(suffix)), textures))
    for name, color in PLAIN_STONES.items():
        lines.append(cube(f"id=%{name}", name, f"colorMult={color}"))
    lines.append(f"block:{ids(names('Log'))},data=*,topbottom={t('log/top')},allsides=0:{tid('log_oak')},"
                 f"stdrot=true,{TINT}")
    lines.append(f"block:{ids(names('Leaf'))},data=*,allfaces={t('leaf')},stdrot=true,transparency=LEAVES,{TINT}")
    lines.append(f"block:{ids(names('Workbench'))},data=*,face0={t('plank')},face1={t('workbench/top')},"
                 f"face2={t('workbench/front')},face3={t('workbench/side')},face4={t('workbench/front')},"
                 f"face5={t('workbench/side')},stdrot=true,{TINT}")
    for variant in ("Clear", "Foggy"):
        key = "glass/" + variant.lower()
        lines.append(f"block:{ids(names('Glass' + variant))},data=*,allfaces={t(key, TINTED_GLASS)},stdrot=true,"
                     f"transparency=TRANSPARENT,{TINT}")
        lines.append(f"block:{ids(names('Glass' + variant + 'Pane'))},data=*,patch0={t(key, TINTED_GLASS)},"
                     f"patch1={t('glass/pane', TINTED_GLASS)},transparency=TRANSPARENT,{TINT}")
    lines.append(f"block:{ids(names('Chest'))},data=*,patch0-1={t('chest/top')},patch2-5={t('chest/side')},"
                 f"transparency=TRANSPARENT,{TINT}")
    lines.append(f"block:{ids(names('Carpet'))},data=*,patch0-5={t('wool')},transparency=TRANSPARENT,{TINT}")

    lines.append("")
    lines.append("# Plants: the untinted stem or trunk, with the tinted petals or leaves where it is see-through")
    for suffix, (tinted, natural) in PLANTS.items():
        lines.append(f"block:{ids(names(suffix))},data=*,patch0-1=0:{tid(natural)},patch2-3={t(tinted)},"
                     f"layer0=2,layer1=3,transparency=TRANSPARENT,{TINT}")
    lines.append(f"block:{ids(names('Chain'))},data=*,patch0-1={t('chain')},transparency=TRANSPARENT,{TINT}")

    lines.append("")
    lines.append("# Stairs, slabs, walls, trapdoors and pistons: one block for every color, the color in a tile entity")
    for name, textures in list(STAIRS.items()) + list(WALLS.items()):
        top, side, bottom = top_side_bottom(textures)
        lines.append(f"block:id=%{name},data=*,patch0={t(side)},patch1={t(top)},patch2={t(bottom)},"
                     f"transparency=SEMITRANSPARENT,{TINT}")
    for name, textures in SLABS.items():
        top, side, bottom = top_side_bottom(textures)
        lines.append(f"block:id=%{name},data=*,patch0={t(bottom)},patch1={t(top)},patch2-5={t(side)},"
                     f"transparency=SEMITRANSPARENT,{TINT}")
    lines.append(f"block:id=%ironTrapdoor,data=*,patch0-5={t('ironTrapdoor')},transparency=TRANSPARENT,{TINT}")
    # Beds are drawn as a box of their tinted wool.
    lines.append(f"block:id=%bed,data=*,patch0-5={t('wool')},transparency=TRANSPARENT,{TINT}")

    lines.append("")
    lines.append("# Dyed Iron Chests and Storage Drawers, the color in a tile entity")
    for tier in IRON_CHEST_TIERS:
        key = f"ironchest/{tier.lower()}/"
        lines.append(f"block:id=%dyed{tier}Chest,data=*,patch0-1=0:{tid(key + 'topTrim')},"
                     f"patch2-5=0:{tid(key + 'sideTrim')},patch6-7={t(key + 'top')},patch8-11={t(key + 'side')},"
                     + ",".join(f"layer{face}={face + 6}" for face in range(6)) + f",transparency=TRANSPARENT,{TINT}")
    # DyedDrawersRenderer's textures: top and bottom, left and right, back, front.
    for name, (front, half) in DRAWERS.items():
        top, sides = ("side_h", "side_v") if half else ("side", "side")
        lines.append(f"block:id=%{name},data=*,patch0={t('drawers/' + top)},patch1={t('drawers/' + sides)},"
                     f"patch2={t('drawers/side')},patch3={t('drawers/' + front)},"
                     f"transparency={'SEMITRANSPARENT' if half else 'OPAQUE'},{TINT}")

    # Pistons: the cobblestone is tinted and the wood, iron and cracks are drawn over it untinted (patches 6 to 11
    # hold the tinted layer under faces 0 to 5), with the wooden strip on the sides towards the front.
    for name, face_texture in (("piston", "piston_top_normal"), ("stickyPiston", "piston_top_sticky")):
        for meta in range(16):
            facing, extended = meta & 7, meta & 8
            if facing > 5:
                continue
            front = DIRECTIONS[facing]
            faces = []
            for face in range(6):
                if face == facing:
                    if extended:
                        faces.append((f"0:{tid('piston/innerOverlay')}", t("piston/inner")))
                    else:
                        faces.append((f"0:{tid(face_texture)}", None))
                elif DIRECTIONS[face] == tuple(-d for d in front):
                    faces.append((f"0:{tid('piston/bottomOverlay')}", t("piston/bottom")))
                else:
                    faces.append((f"{rotation_towards(face, front)}:{tid('piston/sideOverlay')}", t("piston/side")))
            args = []
            for face, (overlay, under) in enumerate(faces):
                args.append(f"patch{face}={overlay}")
                if under:
                    args.append(f"patch{face + 6}={under},layer{face}={face + 6}")
            lines.append(f"block:id=%{name},data={meta},{','.join(args)},transparency=TRANSPARENT,{TINT}")
    # The arm's head is not dyed.
    for meta in range(16):
        facing = meta & 7
        if facing > 5:
            continue
        face_texture = "piston_top_sticky" if meta & 8 else "piston_top_normal"
        lines.append(f"block:id=%pistonHead,data={meta},patch0-5=0:{tid(face_texture)},transparency=TRANSPARENT")
    return "\n".join(lines) + "\n"


def models_file():
    renderer = f"class={PACKAGE}.TileColorBoxRenderer"
    lines = [
        "# More Dyes block shapes for GTNH Web Map. Made by tools/make_dynmap.py; edit that instead.",
        "modname:moredyes",
        "",
        "patch:id=VertX1Z0ToX0Z1,Ox=1.0,Oy=0.0,Oz=0.0,Ux=0.0,Uy=0.0,Uz=1.0,Vx=1.0,Vy=1.0,Vz=0.0,visibility=flip",
        "",
        "# Glass panes, chests, carpets and beds",
        f"customblock:{ids(names('GlassClearPane') + names('GlassFoggyPane'))},data=*,"
        "class=org.dynmap.hdmap.renderer.PaneRenderer",
        f"customblock:{ids(names('Chest'))},data=*,{renderer},{box(0.0625, 0.9375, 0.0, 0.875, 0.0625, 0.9375)}",
        f"customblock:{ids(names('Carpet'))},data=*,{renderer},{box(ymax=0.0625)}",
        f"customblock:id=%bed,data=*,{renderer},{box(ymax=0.5625)}",
        "",
        "# Plants and chains are two crossed squares",
        f"patchblock:{ids(sum((names(s) for s in list(PLANTS) + ['Chain']), []))},data=*,"
        "patch0=VertX1Z0ToX0Z1,patch1=VertX1Z0ToX0Z1@90",
        "",
        "# Stairs, slabs, walls, trapdoors and pistons, which also need the color from their tile entity",
    ]
    wall_links = ",".join(f"link{i}=%{name}" for i, name in enumerate(WALLS))
    for name in STAIRS:
        lines.append(f"customblock:id=%{name},data=*,class={PACKAGE}.TileColorStairsRenderer")
    for name in WALLS:
        lines.append(f"customblock:id=%{name},data=*,class={PACKAGE}.TileColorWallRenderer,type=wall,{wall_links}")
    for name in SLABS:
        lines.append(f"customblock:id=%{name},data=0,{renderer},{box(ymax=0.5)}")
        lines.append(f"customblock:id=%{name},data=1,{renderer},{box(ymin=0.5)}")
        lines.append(f"customblock:id=%{name},data=2,{renderer},{box()}")
    # Like vanilla trapdoors: closed on the bottom (0-3) or top (8-11) half, or open against one side.
    trapdoors = [
        ("data=0,data=1,data=2,data=3", box(ymax=0.1875)),
        ("data=8,data=9,data=10,data=11", box(ymin=0.8125)),
        ("data=4,data=12", box(zmin=0.8125)),
        ("data=5,data=13", box(zmax=0.1875)),
        ("data=6,data=14", box(xmin=0.8125)),
        ("data=7,data=15", box(xmax=0.1875)),
    ]
    for data, bounds in trapdoors:
        lines.append(f"customblock:id=%ironTrapdoor,{data},{renderer},{bounds}")
    for meta in range(16):
        facing, extended = meta & 7, meta & 8
        if facing > 5:
            continue
        bounds = facing_box(facing, 0.25) if extended else box()
        lines.append(f"customblock:id=%piston,id=%stickyPiston,data={meta},{renderer},{bounds}")
        lines.append(f"customblock:id=%pistonHead,data={meta},{renderer},{facing_box(facing, -0.25)}")
    lines.append("")
    lines.append("# Dyed Iron Chests and Storage Drawers (the drawers turn the way their tile entity says)")
    lines.append(f"customblock:{ids(f'%dyed{tier}Chest' for tier in IRON_CHEST_TIERS)},data=*,{renderer},"
                 f"{box(0.0625, 0.9375, 0.0, 0.875, 0.0625, 0.9375)}")
    for name, (front, half) in DRAWERS.items():
        lines.append(f"customblock:id=%{name},data=*,class={PACKAGE}.DyedDrawersRenderer,depth={0.5 if half else 1.0}")
    return "\n".join(lines) + "\n"


def main():
    (OUT / "dynmap-texture.txt").write_text(texture_file())
    (OUT / "dynmap-models.txt").write_text(models_file())


if __name__ == "__main__":
    main()

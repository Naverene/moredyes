This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water.

If Thermal Expansion is detected, rockwool will be available in the mod colors.

If Chisel is detected, all colors of all blocks will be chiselable.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in.

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips.


Dyed crafting tables and chests are available in every color. Craft them from four (crafting table) or eight (chest) dyed planks of one color, or combine a vanilla crafting table or chest with a dye. Two chests of the same color placed side by side join into a double chest. Craft either with a bucket of water to get the vanilla block back.

If Iron Chests is installed, its iron, gold, diamond, copper and silver chests come in every color: craft one with a dye (a dyed one can be dyed again). In 1.7.10 these chests are metal all over, so the dye colors the flat panel of each face and the frame and latch keep the metal's color. Crystal chests (glass), obsidian and dirt chests have no panels to dye and stay as they are. Iron Chests' upgrades keep the color, and also turn a dyed wooden chest into a dyed iron or copper chest; an upgrade to crystal or obsidian gives Iron Chests' plain chest.

If Storage Drawers is installed, its drawers come in every color in all five sizes: craft any wooden drawer of a size with a dye (a taped drawer keeps its contents). They work with hoppers, keys, upgrades and drawer controllers like Storage Drawers' own. Their textures are made from Storage Drawers' oak drawers when the game loads, in the same way as the other tinted textures.

If GregTech (GT New Horizons' GT5-Unofficial) is installed, the chemical reactor and mixer turn any of the mod's dyes into GregTech's chemical dye and water-mixed dye of the vanilla color it looks closest to, as they do with a vanilla dye. Chemical dye fills spray cans, so the mod's dyes can color GregTech's cables, pipes, hatches, buses and machines, and AE2 cables. The infinite spray can doesn't use dye, so it needs nothing from More Dyes.

If Applied Energistics 2 is installed, every dye counts as the vanilla color it looks closest to: eight fluix cables (glass, covered, smart or dense) or matter balls around it make eight cables or paint balls of that color, and the Color Applicator takes it. The dyes are in the ore dictionary as moredyes plus that color (moredyesLightBlue, for example) rather than dyeLightBlue, which vanilla's recipes take in 1.7.10. To try it in the dev game, pass `-PwithAE2` to Gradle.

The dyed chests and drawers are one block per tier or size, with the color in the tile entity and the color's number as the item damage, like the dyed stairs. To run the game with both mods while developing, pass `-Pcompat_mods=true` to Gradle (for example `./gradlew runClient -Pcompat_mods=true`).

## Blocks from newer Minecraft versions

Many blocks from Minecraft 1.8 to 1.16 are available in every color: granite, andesite and their polished versions (and polished diorite), concrete and concrete powder, soul soil, basalt and polished basalt, smooth stone, smooth sandstone, smooth quartz, cut sandstone, end stone bricks, cracked and chiseled nether bricks, bone blocks, glazed terracotta, blocks of netherite, crying obsidian, chains and cornflowers. Mossy cobblestone, mossy stone bricks and nether bricks come in every color too, because their stairs and walls are made from them.

Plain granite and andesite generate underground and are crafted as in 1.8, unless another mod already provides them (the same as diorite). Concrete powder turns into concrete when it touches water.

Stairs, slabs and walls come in every color for stone, granite, diorite, andesite, the polished stones, mossy cobblestone, mossy stone bricks, end stone bricks, smooth sandstone, smooth quartz, nether bricks, bricks, stone bricks, sandstone and cut sandstone (not every material has all three shapes, the same as in vanilla). They are crafted from dyed blocks of one shade with the vanilla recipes and are in their own creative tab. Iron trapdoors come in every color as well and, like iron doors, only open with redstone. Pistons and sticky pistons come in every color too and work like the vanilla ones; craft a piston or sticky piston with a dye, or put a slimeball on top of a dyed piston to make it sticky. The rod of an extended piston keeps its wood color, and other pistons cannot push a dyed piston (in 1.7.10 pistons cannot move blocks that store extra data, the same as chests).

Most of these use their vanilla recipe with dyed blocks of one shade, or eight blocks around a dye. Blocks that have no recipe in 1.7:

- Soul soil: dyed soul sand and dirt make two.
- Basalt: smelt dyed soul soil.
- Crying obsidian: eight dyed obsidian around a ghast tear.
- Block of netherite: obsidian in the corners, gold ingots on the sides and a dye in the middle make four.
- Chain: an iron ingot above and below a dye make four.
- Bone block: eight bone meal around a dye.
- Iron trapdoor: four iron ingots and a dye.

Only the newer blocks that have a plain or vanilla version (granite, andesite, cut sandstone, mossy cobblestone, mossy stone bricks and nether bricks) can be washed back. Stairs, slabs and walls cannot be washed.

Workstations from newer versions (loom, smoker, barrel and so on) are left to other mods.
Sheep can be dyed any of the mod's colors: right-click a sheep with a dye, and shearing or killing it gives wool in that color. A vanilla dye puts it back on a vanilla color. Some new sheep also spawn in a random mod color; the `sheepSpawnChance` config option sets how often (5% by default, 0 to turn it off).

## Item groups in NEI (GT New Horizons)

GTNH's NotEnoughItems can fold many items into one collapsible entry, but mods cannot add groups themselves: they come only from `config/NEI/collapsibleitems.cfg`. To have NEI show one entry per kind of block (all the colors of wool as "Dyed Wool", and so on) instead of thousands of separate items, paste these lines into that file. The comment at the top says where in the file to put them; NEI puts each item in the first group that matches it.

```
# ---- More Dyes (1.7.10): one collapsible group per kind of block, all 118 colors in each ----
# For GTNH's NotEnoughItems: paste these lines into config/NEI/collapsibleitems.cfg. Every filter is anchored to
# "moredyes:", so none of them takes items from any other mod. Each `; {"displayName":...}` line names the group on
# the line after it (GTNH NEI reads these option lines; it has since at least 2.6.41, and 2.8.x still does).
#
# Where to paste: NEI puts an item in the FIRST group that matches it.
# - Before the first group of the GTNH file (above "# spawn egg", line 23 in GTNH 2.9's file): every kind below
#   gets its own "Dyed ..." group. Recommended.
# - At the end of the file: these kinds stay in GTNH's generic groups instead, and their lines below do nothing:
#     planks             -> line 854  $plankWood !$plateWood   (our ore name plankWood)
#     logs               -> line 806  $logwood ...             (our ore name logWood)
#     saplings           -> line 356  ... $treeSapling ...     (our ore name treeSapling)
#     the 13 stairs      -> line 794  stair !chisel:           (registry names like moredyes:stoneStairs)
#     the 3 slabs        -> line 795  slab !double !full       (moredyes:stoneSlab, dioriteSlab, cutSandstoneSlab)
#     iron trapdoors     -> line 793  trapdoor                 (moredyes:ironTrapdoor)
#     dyed drawers       -> lines 47-51 halfdrawers2 ... fulldrawers4 (these match anywhere in the name, ignoring
#                           case, so moredyes:dyedFullDrawers1 joins Storage Drawers' own 1x1 drawers group)
#   Every other kind matches no GTNH group (leaves are "...MixLeaf", walls are not "minecraft:..._wall", the
#   dyes are not minecraft:dye, wool, glass and chests are not the vanilla ids, and the dyed Iron Chests are
#   moredyes:dyed<Tier>Chest, which line 528 (IronChest's upgrades) does not take), so it gets its own group either way.
# The dyed Iron Chests and Storage Drawers groups only fill when those mods are installed; without those mods the
# lines match nothing and the groups never show.
; {"displayName":"Mixed Dyes"}
r/^moredyes:item\.[a-z]+Dye$/
; {"displayName":"Dyed Wool"}
r/^moredyes:[a-z]+MixWool$/
; {"displayName":"Dyed Stone Bricks"}
r/^moredyes:[a-z]+MixStonebrick$/
; {"displayName":"Dyed Carved Stone Bricks"}
r/^moredyes:[a-z]+MixStonebrickCarved$/
; {"displayName":"Dyed Cracked Stone Bricks"}
r/^moredyes:[a-z]+MixStonebrickCracked$/
; {"displayName":"Dyed Stone"}
r/^moredyes:[a-z]+MixStone$/
; {"displayName":"Dyed Cobblestone"}
r/^moredyes:[a-z]+MixCobble$/
; {"displayName":"Dyed Obsidian"}
r/^moredyes:[a-z]+MixObsidian$/
; {"displayName":"Dyed Soulsand"}
r/^moredyes:[a-z]+MixSoulsand$/
; {"displayName":"Dyed Quartz Blocks"}
r/^moredyes:[a-z]+MixQuartz$/
; {"displayName":"Dyed Hardened Stained Clay"}
r/^moredyes:[a-z]+MixClay$/
; {"displayName":"Dyed Coal Blocks"}
r/^moredyes:[a-z]+MixCoal$/
; {"displayName":"Dyed Glowstone"}
r/^moredyes:[a-z]+MixGlowstone$/
; {"displayName":"Dyed Lapis Blocks"}
r/^moredyes:[a-z]+MixLapis$/
; {"displayName":"Dyed Redstone Blocks"}
r/^moredyes:[a-z]+MixRedstone$/
; {"displayName":"Dyed Planks"}
r/^moredyes:[a-z]+MixPlank$/
; {"displayName":"Dyed Tulips"}
r/^moredyes:[a-z]+MixTulip$/
; {"displayName":"Dyed Logs"}
r/^moredyes:[a-z]+MixLog$/
; {"displayName":"Dyed Leaves"}
r/^moredyes:[a-z]+MixLeaf$/
; {"displayName":"Dyed Saplings"}
r/^moredyes:[a-z]+MixSapling$/
; {"displayName":"Dyed Clear Glass"}
r/^moredyes:[a-z]+MixGlassClear$/
; {"displayName":"Dyed Foggy Glass"}
r/^moredyes:[a-z]+MixGlassFoggy$/
; {"displayName":"Dyed Clear Glass Panes"}
r/^moredyes:[a-z]+MixGlassClearPane$/
; {"displayName":"Dyed Foggy Glass Panes"}
r/^moredyes:[a-z]+MixGlassFoggyPane$/
; {"displayName":"Dyed Bricks"}
r/^moredyes:[a-z]+MixBrick$/
; {"displayName":"Dyed Sand"}
r/^moredyes:[a-z]+MixSand$/
; {"displayName":"Dyed Crafting Tables"}
r/^moredyes:[a-z]+MixWorkbench$/
; {"displayName":"Dyed Hardened Clay"}
r/^moredyes:[a-z]+MixHardenedClay$/
; {"displayName":"Dyed Sandstone"}
r/^moredyes:[a-z]+MixSandstone$/
; {"displayName":"Dyed Bookshelves"}
r/^moredyes:[a-z]+MixBookshelf$/
; {"displayName":"Dyed Diorite"}
r/^moredyes:[a-z]+MixDiorite$/
; {"displayName":"Dyed Chests"}
r/^moredyes:[a-z]+MixChest$/
; {"displayName":"Dyed Granite"}
r/^moredyes:[a-z]+MixGranite$/
; {"displayName":"Dyed Andesite"}
r/^moredyes:[a-z]+MixAndesite$/
; {"displayName":"Dyed Polished Granite"}
r/^moredyes:[a-z]+MixPolishedGranite$/
; {"displayName":"Dyed Polished Diorite"}
r/^moredyes:[a-z]+MixPolishedDiorite$/
; {"displayName":"Dyed Polished Andesite"}
r/^moredyes:[a-z]+MixPolishedAndesite$/
; {"displayName":"Dyed Concrete"}
r/^moredyes:[a-z]+MixConcrete$/
; {"displayName":"Dyed Concrete Powder"}
r/^moredyes:[a-z]+MixConcretePowder$/
; {"displayName":"Dyed Soul Soil"}
r/^moredyes:[a-z]+MixSoulSoil$/
; {"displayName":"Dyed Basalt"}
r/^moredyes:[a-z]+MixBasalt$/
; {"displayName":"Dyed Polished Basalt"}
r/^moredyes:[a-z]+MixPolishedBasalt$/
; {"displayName":"Dyed Smooth Stone"}
r/^moredyes:[a-z]+MixSmoothStone$/
; {"displayName":"Dyed Smooth Sandstone"}
r/^moredyes:[a-z]+MixSmoothSandstone$/
; {"displayName":"Dyed Smooth Quartz"}
r/^moredyes:[a-z]+MixSmoothQuartz$/
; {"displayName":"Dyed Cut Sandstone"}
r/^moredyes:[a-z]+MixCutSandstone$/
; {"displayName":"Dyed Mossy Cobblestone"}
r/^moredyes:[a-z]+MixMossyCobble$/
; {"displayName":"Dyed Mossy Stone Bricks"}
r/^moredyes:[a-z]+MixMossyStonebrick$/
; {"displayName":"Dyed Nether Bricks"}
r/^moredyes:[a-z]+MixNetherBrick$/
; {"displayName":"Dyed Cracked Nether Bricks"}
r/^moredyes:[a-z]+MixCrackedNetherBrick$/
; {"displayName":"Dyed Chiseled Nether Bricks"}
r/^moredyes:[a-z]+MixChiseledNetherBrick$/
; {"displayName":"Dyed End Stone Bricks"}
r/^moredyes:[a-z]+MixEndStoneBrick$/
; {"displayName":"Dyed Bone Blocks"}
r/^moredyes:[a-z]+MixBoneBlock$/
; {"displayName":"Dyed Glazed Terracotta"}
r/^moredyes:[a-z]+MixGlazedTerracotta$/
; {"displayName":"Dyed Blocks of Netherite"}
r/^moredyes:[a-z]+MixNetheriteBlock$/
; {"displayName":"Dyed Crying Obsidian"}
r/^moredyes:[a-z]+MixCryingObsidian$/
; {"displayName":"Dyed Cornflowers"}
r/^moredyes:[a-z]+MixCornflower$/
; {"displayName":"Dyed Chains"}
r/^moredyes:[a-z]+MixChain$/
; {"displayName":"Dyed Rockwool"}
r/^moredyes:[a-z]+MixRockwool$/
; {"displayName":"Dyed Stone Stairs"}
r/^moredyes:stoneStairs$/
; {"displayName":"Dyed Granite Stairs"}
r/^moredyes:graniteStairs$/
; {"displayName":"Dyed Polished Granite Stairs"}
r/^moredyes:polishedGraniteStairs$/
; {"displayName":"Dyed Diorite Stairs"}
r/^moredyes:dioriteStairs$/
; {"displayName":"Dyed Polished Diorite Stairs"}
r/^moredyes:polishedDioriteStairs$/
; {"displayName":"Dyed Andesite Stairs"}
r/^moredyes:andesiteStairs$/
; {"displayName":"Dyed Polished Andesite Stairs"}
r/^moredyes:polishedAndesiteStairs$/
; {"displayName":"Dyed Mossy Cobblestone Stairs"}
r/^moredyes:mossyCobbleStairs$/
; {"displayName":"Dyed Mossy Stone Brick Stairs"}
r/^moredyes:mossyStonebrickStairs$/
; {"displayName":"Dyed End Stone Brick Stairs"}
r/^moredyes:endStoneBrickStairs$/
; {"displayName":"Dyed Smooth Sandstone Stairs"}
r/^moredyes:smoothSandstoneStairs$/
; {"displayName":"Dyed Smooth Quartz Stairs"}
r/^moredyes:smoothQuartzStairs$/
; {"displayName":"Dyed Nether Brick Stairs"}
r/^moredyes:netherBrickStairs$/
; {"displayName":"Dyed Stone Slabs"}
r/^moredyes:stoneSlab$/
; {"displayName":"Dyed Cut Sandstone Slabs"}
r/^moredyes:cutSandstoneSlab$/
; {"displayName":"Dyed Diorite Slabs"}
r/^moredyes:dioriteSlab$/
; {"displayName":"Dyed Brick Walls"}
r/^moredyes:brickWall$/
; {"displayName":"Dyed Stone Brick Walls"}
r/^moredyes:stonebrickWall$/
; {"displayName":"Dyed Mossy Stone Brick Walls"}
r/^moredyes:mossyStonebrickWall$/
; {"displayName":"Dyed Granite Walls"}
r/^moredyes:graniteWall$/
; {"displayName":"Dyed Diorite Walls"}
r/^moredyes:dioriteWall$/
; {"displayName":"Dyed Andesite Walls"}
r/^moredyes:andesiteWall$/
; {"displayName":"Dyed Nether Brick Walls"}
r/^moredyes:netherBrickWall$/
; {"displayName":"Dyed Sandstone Walls"}
r/^moredyes:sandstoneWall$/
; {"displayName":"Dyed End Stone Brick Walls"}
r/^moredyes:endStoneBrickWall$/
; {"displayName":"Dyed Iron Trapdoors"}
r/^moredyes:ironTrapdoor$/
; {"displayName":"Dyed Pistons"}
r/^moredyes:piston$/
; {"displayName":"Dyed Sticky Pistons"}
r/^moredyes:stickyPiston$/
; {"displayName":"Dyed Iron Chests"}
r/^moredyes:dyedIronChest$/
; {"displayName":"Dyed Gold Chests"}
r/^moredyes:dyedGoldChest$/
; {"displayName":"Dyed Diamond Chests"}
r/^moredyes:dyedDiamondChest$/
; {"displayName":"Dyed Copper Chests"}
r/^moredyes:dyedCopperChest$/
; {"displayName":"Dyed Silver Chests"}
r/^moredyes:dyedSilverChest$/
; {"displayName":"Dyed Drawers"}
r/^moredyes:dyedFullDrawers1$/
; {"displayName":"Dyed Drawers 1x2"}
r/^moredyes:dyedFullDrawers2$/
; {"displayName":"Dyed Drawers 2x2"}
r/^moredyes:dyedFullDrawers4$/
; {"displayName":"Dyed Half Drawers 1x2"}
r/^moredyes:dyedHalfDrawers2$/
; {"displayName":"Dyed Half Drawers 2x2"}
r/^moredyes:dyedHalfDrawers4$/
# ---- end of More Dyes ----
```

## How the colors are drawn

Blocks are not stored as one texture file per color. Each block type has one grey texture, and the game multiplies it by the dye color when it draws the block, the same way it colors grass and leaves.

The grey textures are made from the vanilla textures when the game loads, so they follow whatever resource pack the player uses. The list of which vanilla texture each block uses is in `client/TintSources.java`. Blocks that do not exist in vanilla 1.7.10 use a grey file in `textures/blocks/base/` instead (diorite, granite, concrete and the other newer blocks, and foggy glass).

To give a new block a texture, add a line to `TintSources` and call `TintedTextures.register(iconRegister, "<key>")` from the block's `registerBlockIcons`. Parts that should keep their natural color, such as a flower's stem or a log's bark, are drawn as a separate untinted layer (see `ILayeredBlock`).

Stairs, slabs, walls, trapdoors and pistons need their metadata for their direction or shape, so they keep their color in a small tile entity instead. Each is a single block for all colors, with the color's number (see `ColorIndex`) as the item damage.

## Downloads and releases

GitHub Actions builds the mod on every push, then starts a server with it and a fresh world; the build fails if the mod crashes or throws an error while loading. Pushing or merging to `main` updates the **Development build** pre-release on the Releases page with the newest jar. These builds are for testing.

To publish a proper release, tag the commit with a version number starting with `v` and push the tag:

```
git tag v1.2.0
git push origin v1.2.0
```

The workflow builds that commit and creates a release named after the tag, with the jar attached and release notes generated from the merged pull requests and commits.

This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water.

If Thermal Expansion is detected, rockwool will be available in the mod colors.

If Chisel is detected, all colors of all blocks will be chiselable.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in.

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips.


Dyed crafting tables and chests are available in every color. Craft them from four (crafting table) or eight (chest) dyed planks of one color, or combine a vanilla crafting table or chest with a dye. Two chests of the same color placed side by side join into a double chest. Craft either with a bucket of water to get the vanilla block back.

If Iron Chests is installed, its iron, gold, diamond, copper and silver chests come in every color: craft one with a dye (a dyed one can be dyed again). In 1.7.10 these chests are metal all over, so the dye colors the flat panel of each face and the frame and latch keep the metal's color. Crystal chests (glass), obsidian and dirt chests have no panels to dye and stay as they are. Iron Chests' upgrades keep the color, and also turn a dyed wooden chest into a dyed iron or copper chest; an upgrade to crystal or obsidian gives Iron Chests' plain chest. The `Compat_IronChests` and `Compat_StorageDrawers` config options leave the dyed chests and drawers out, and `Compat_GregTechRecipes` leaves out the GregTech recipes.

If Storage Drawers is installed, its drawers come in every color in all five sizes: craft any wooden drawer of a size with a dye (a taped drawer keeps its contents). They work with hoppers, keys, upgrades and drawer controllers like Storage Drawers' own. Their textures are made from Storage Drawers' oak drawers when the game loads, in the same way as the other tinted textures.

If Et Futurum Requiem is installed (it comes with GT: New Horizons), its shulker boxes come in every color: craft any Et Futurum shulker box, or a dyed one, with a dye. The box keeps its contents, name and tier. With Iron Chests installed, Et Futurum's iron, gold, diamond, copper, silver, crystal and obsidian boxes can be dyed too, and its upgrades work on dyed boxes; the dye colors the shell and the metal trim keeps its color. The grey textures are made from Et Futurum's white boxes when the game loads (the trim is the part that is the same on its white and red boxes). Washing a dyed box in a cauldron gives back Et Futurum's plain box with the contents still inside. The dyed boxes are one block for every color and tier (`moredyes:dyedShulkerBox`): the color is in the tile entity and is the item damage, and the tier is Et Futurum's `Type` tag. The `Compat_EtFuturum` config option leaves them out.

If [Right Proper Lighting Engine (RPLE)](https://www.curseforge.com/minecraft/mc-mods/rple) is installed, light is colored: dyed redstone lamps, glowstone and the other dyed blocks that give off light shine in their own color, and dyed glass and panes tint the light that passes through them. The colors are made more saturated than the dyes (RPLE mixes light, so pale colors would look almost white), and darker dyes give dimmer light. The `Compat_RPLE` config option turns this off. To try it in a dev game, run `./gradlew runClient -Prple=true` (or `runServer`), which adds RPLE and the mods it needs.

The dyed chests and drawers are one block per tier or size, with the color in the tile entity and the color's number as the item damage, like the dyed stairs. To run the game with these mods while developing, pass `-Pcompat_mods=true` to Gradle (for example `./gradlew runClient -Pcompat_mods=true`).

GTNH Web Map (the GT: New Horizons version of Dynmap) draws the dyed blocks in their colors and shapes, using the definitions in `assets/moredyes/dynmap-texture.txt` and `dynmap-models.txt`. These are made by `tools/make_dynmap.py`, so run it again after adding or changing a block. The map makes the grey textures and reads the colors with the classes in `compat/dynmap`, which only load when the map is installed. The dyed Iron Chests and Storage Drawers are on the map too when those mods are installed. To run the game with the map while developing, pass `-Pweb_map=true` to Gradle.

## Blocks from newer Minecraft versions

Many blocks from Minecraft 1.8 to 1.16 are available in every color: granite, andesite and their polished versions (and polished diorite), concrete and concrete powder, soul soil, basalt and polished basalt, smooth stone, smooth sandstone, smooth quartz, cut sandstone, end stone bricks, cracked and chiseled nether bricks, bone blocks, glazed terracotta, blocks of netherite, crying obsidian, quartz bricks, chains, cornflowers and lilies of the valley. Mossy cobblestone, mossy stone bricks and nether bricks come in every color too, because their stairs and walls are made from them.

So do more of the 1.7 blocks: end stone, gravel, ice, packed ice, snow blocks, chiseled quartz, quartz pillars, chiseled sandstone, the planks of all six woods (oak planks were already dyed), and the flowers: poppies, dandelions, blue orchids, alliums, azure bluets and oxeye daisies, and the tall lilacs, peonies and rose bushes. The petals take the dye and the stems stay green, like the dyed tulips. Dyed ice and packed ice behave like the vanilla ones (slippery, and they need Silk Touch to drop). Quartz pillars and the other blocks that store their color in the metadata only stand upright, like the dyed basalt and bone blocks.

Carpets and beds come in every color too. Dyed carpets work like vanilla ones; dyed beds work like the vanilla bed (sleeping, setting the spawn point, exploding in the Nether), with the sheet in the dye color and the pillow and frame as in vanilla.

Fences of all six woods and signs come in every color as well, and so does a crafting table slab, which opens the crafting grid like a whole crafting table. Dyed fences of every wood connect to each other, as in 1.8. A dyed flower, tulip, cornflower or sapling goes into an empty flower pot like a vanilla one and keeps its color there; breaking the pot gives back the pot and the plant.

Plain granite and andesite generate underground and are crafted as in 1.8, unless another mod already provides them (the same as diorite). Concrete powder turns into concrete when it touches water.

Stairs and slabs come in every color for every full block that has them in Minecraft 1.16: stone, cobblestone and mossy cobblestone, stone bricks (plain, cracked, carved and mossy), bricks, stained clay, coal, lapis, redstone, quartz (plain, bricks, chiseled, pillar and smooth), obsidian, glowstone, soul sand, sand, sandstone (plain, chiseled and cut), granite, diorite, andesite and the polished stones, end stone, ice, packed ice, snow, bone blocks, both glasses, wool, concrete, concrete powder and the planks of all six woods. Smooth sandstone, end stone bricks and nether bricks have stairs too. Walls come for the vanilla wall materials: cobblestone, mossy cobblestone, bricks, stone bricks, mossy stone bricks, granite, diorite, andesite, sandstone, end stone, end stone bricks and nether bricks. A glowstone slab glows and ice stairs are slippery, like the full blocks. They are crafted from dyed blocks of one shade with the vanilla recipes and are in their own creative tab. Iron trapdoors come in every color as well and, like iron doors, only open with redstone. Pistons and sticky pistons come in every color too and work like the vanilla ones; craft a piston or sticky piston with a dye, or put a slimeball on top of a dyed piston to make it sticky. The rod of an extended piston keeps its wood color, and other pistons cannot push a dyed piston (in 1.7.10 pistons cannot move blocks that store extra data, the same as chests).

Most of these use their vanilla recipe with dyed blocks of one shade, or eight blocks around a dye. Blocks that have no recipe in 1.7:

- Soul soil: dyed soul sand and dirt make two.
- Basalt: smelt dyed soul soil.
- Crying obsidian: eight dyed obsidian around a ghast tear.
- Block of netherite: obsidian in the corners, gold ingots on the sides and a dye in the middle make four.
- Chain: an iron ingot above and below a dye make four.
- Bone block: eight bone meal around a dye.
- Iron trapdoor: four iron ingots and a dye.
- Quartz bricks: four dyed quartz blocks in a square.
- Packed ice: nine dyed ice.
- Lily of the valley: grows wild, like the cornflower, and both make a dye of their shade.
- Fences: dyed planks of one shade with two sticks between them make three, as in 1.8 (a dyed oak fence can also be made from eight vanilla fences around a dye).
- Signs: six dyed planks of one shade (any wood) and a stick make three, or eight vanilla signs around a dye.
- Crafting table slab: three dyed crafting tables in a row.
- Carpets: two dyed wool of one shade make three, or eight carpets of any vanilla color around a dye.
- Beds: three dyed wool of one shade over three planks of any wood, or a vanilla bed and a dye.

Planks of any of the six woods in one shade also make that shade's crafting table and chest. Dyed flowers do not give dye back, or eight flowers and a dye would make eight dyes.

Only the newer blocks that have a plain or vanilla version (granite, andesite, cut sandstone, mossy cobblestone, mossy stone bricks, nether bricks, end stone, gravel, ice, packed ice, snow, chiseled quartz, quartz pillars, chiseled sandstone, the planks and the small flowers apart from lily of the valley) can be washed back, and so can carpets (to white carpet) and beds (to the vanilla bed). Stairs, slabs, walls, fences, tall flowers and signs cannot be washed.

Workstations from newer versions (loom, smoker, barrel and so on) are left to other mods.
Sheep can be dyed any of the mod's colors: right-click a sheep with a dye, and shearing or killing it gives wool in that color. A vanilla dye puts it back on a vanilla color. Some new sheep also spawn in a mod color: by default 20% in a natural brown, beige or grey and 2% in burgundy. The `sheepSpawnChance` and `sheepSpawnColors` config options set the chance and the colors (0 turns it off, an empty list allows any color), and `rareSheepSpawnChance` and `rareSheepSpawnColors` do the same for the rare ones.

## One item id for every dyed block

Every dyed block, stair, slab, wall, fence, tall flower, trapdoor and piston is one item, `moredyes:dyedBlock`, with the block and color in its damage value (block kind × 118 + color). Storage that keeps one item id per slot, such as the Extra Utilities Filing Cabinet, holds all of them. The blocks themselves keep their own block ids, so placed blocks don't change.

Worlds from before keep working: the blocks' old items are hidden, and turn into the shared item when they are in a player's inventory, dropped on the ground, or dropped by a broken block. An old item alone in a crafting grid also crafts into the shared one, for items still sitting in storage. Chisel works: while a Chisel GUI (the chisel or the Auto Chisel) is open, your dyed blocks are their old items, so Chisel takes them, and they turn back into the shared item when it closes. Other mods that only accept "real" block items (AE2 formation planes, block placers, GregTech's machines that chisel) don't take the shared item.

The dyes, signs, beds and the dyed Iron Chests, drawers and shulker boxes still have their own items.

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
# - At the end of the file: some kinds stay in GTNH's generic groups instead, and their lines below do nothing:
#     planks (6 woods)   -> line 854  $plankWood !$plateWood   (our ore name plankWood)
#     logs               -> line 806  $logwood ...             (our ore name logWood)
#     saplings           -> line 356  ... $treeSapling ...     (our ore name treeSapling)
#     dyed drawers       -> lines 47-51 halfdrawers2 ... fulldrawers4 (these match anywhere in the name, ignoring
#                           case, so moredyes:dyedFullDrawers1 joins Storage Drawers' own 1x1 drawers group)
#   Every other kind matches no GTNH group, so it gets its own group either way.
# Every dyed block is one item, moredyes:dyedBlock, and each group below is a range of its damage values (block kind
# * 118 + color). tools/make_nei_groups.py in the source rewrites these lines when blocks are added.
# The dyed Iron Chests, shulker box and Storage Drawers groups only fill when those mods are installed; without those mods the
# lines match nothing and the groups never show.
; {"displayName":"Mixed Dyes"}
r/^moredyes:item\.[a-z]+Dye$/
; {"displayName":"Dyed Wool"}
r/^moredyes:dyedBlock$/ 0-117
; {"displayName":"Dyed Carpets"}
r/^moredyes:dyedBlock$/ 118-235
; {"displayName":"Dyed Stone Bricks"}
r/^moredyes:dyedBlock$/ 236-353
; {"displayName":"Dyed Carved Stone Bricks"}
r/^moredyes:dyedBlock$/ 354-471
; {"displayName":"Dyed Cracked Stone Bricks"}
r/^moredyes:dyedBlock$/ 472-589
; {"displayName":"Dyed Stone"}
r/^moredyes:dyedBlock$/ 590-707
; {"displayName":"Dyed Cobblestone"}
r/^moredyes:dyedBlock$/ 708-825
; {"displayName":"Dyed Obsidian"}
r/^moredyes:dyedBlock$/ 826-943
; {"displayName":"Dyed Soulsand"}
r/^moredyes:dyedBlock$/ 944-1061
; {"displayName":"Dyed Quartz Blocks"}
r/^moredyes:dyedBlock$/ 1062-1179
; {"displayName":"Dyed Hardened Stained Clay"}
r/^moredyes:dyedBlock$/ 1180-1297
; {"displayName":"Dyed Coal Blocks"}
r/^moredyes:dyedBlock$/ 1298-1415
; {"displayName":"Dyed Glowstone"}
r/^moredyes:dyedBlock$/ 1416-1533
; {"displayName":"Dyed Lapis Blocks"}
r/^moredyes:dyedBlock$/ 1534-1651
; {"displayName":"Dyed Redstone Blocks"}
r/^moredyes:dyedBlock$/ 1652-1769
; {"displayName":"Dyed Planks"}
r/^moredyes:dyedBlock$/ 1770-1887
; {"displayName":"Dyed Tulips"}
r/^moredyes:dyedBlock$/ 1888-2005
; {"displayName":"Dyed Logs"}
r/^moredyes:dyedBlock$/ 2006-2123
; {"displayName":"Dyed Leaves"}
r/^moredyes:dyedBlock$/ 2124-2241
; {"displayName":"Dyed Saplings"}
r/^moredyes:dyedBlock$/ 2242-2359
; {"displayName":"Dyed Clear Glass"}
r/^moredyes:dyedBlock$/ 2360-2477
; {"displayName":"Dyed Foggy Glass"}
r/^moredyes:dyedBlock$/ 2478-2595
; {"displayName":"Dyed Clear Glass Panes"}
r/^moredyes:dyedBlock$/ 2596-2713
; {"displayName":"Dyed Foggy Glass Panes"}
r/^moredyes:dyedBlock$/ 2714-2831
; {"displayName":"Dyed Bricks"}
r/^moredyes:dyedBlock$/ 2832-2949
; {"displayName":"Dyed Sand"}
r/^moredyes:dyedBlock$/ 2950-3067
; {"displayName":"Dyed Crafting Tables"}
r/^moredyes:dyedBlock$/ 3068-3185
; {"displayName":"Dyed Hardened Clay"}
r/^moredyes:dyedBlock$/ 3186-3303
; {"displayName":"Dyed Sandstone"}
r/^moredyes:dyedBlock$/ 3304-3421
; {"displayName":"Dyed Bookshelves"}
r/^moredyes:dyedBlock$/ 3422-3539
; {"displayName":"Dyed Diorite"}
r/^moredyes:dyedBlock$/ 3540-3657
; {"displayName":"Dyed Chests"}
r/^moredyes:dyedBlock$/ 3658-3775
; {"displayName":"Dyed Granite"}
r/^moredyes:dyedBlock$/ 3776-3893
; {"displayName":"Dyed Andesite"}
r/^moredyes:dyedBlock$/ 3894-4011
; {"displayName":"Dyed Polished Granite"}
r/^moredyes:dyedBlock$/ 4012-4129
; {"displayName":"Dyed Speckled Granite"}
r/^moredyes:dyedBlock$/ 4130-4247
; {"displayName":"Dyed Smooth Speckled Granite"}
r/^moredyes:dyedBlock$/ 4248-4365
; {"displayName":"Dyed Polished Diorite"}
r/^moredyes:dyedBlock$/ 4366-4483
; {"displayName":"Dyed Polished Andesite"}
r/^moredyes:dyedBlock$/ 4484-4601
; {"displayName":"Dyed Concrete"}
r/^moredyes:dyedBlock$/ 4602-4719
; {"displayName":"Dyed Concrete Powder"}
r/^moredyes:dyedBlock$/ 4720-4837
; {"displayName":"Dyed Soul Soil"}
r/^moredyes:dyedBlock$/ 4838-4955
; {"displayName":"Dyed Basalt"}
r/^moredyes:dyedBlock$/ 4956-5073
; {"displayName":"Dyed Polished Basalt"}
r/^moredyes:dyedBlock$/ 5074-5191
; {"displayName":"Dyed Smooth Stone"}
r/^moredyes:dyedBlock$/ 5192-5309
; {"displayName":"Dyed Smooth Sandstone"}
r/^moredyes:dyedBlock$/ 5310-5427
; {"displayName":"Dyed Smooth Quartz"}
r/^moredyes:dyedBlock$/ 5428-5545
; {"displayName":"Dyed Cut Sandstone"}
r/^moredyes:dyedBlock$/ 5546-5663
; {"displayName":"Dyed Mossy Cobblestone"}
r/^moredyes:dyedBlock$/ 5664-5781
; {"displayName":"Dyed Mossy Stone Bricks"}
r/^moredyes:dyedBlock$/ 5782-5899
; {"displayName":"Dyed Nether Bricks"}
r/^moredyes:dyedBlock$/ 5900-6017
; {"displayName":"Dyed Cracked Nether Bricks"}
r/^moredyes:dyedBlock$/ 6018-6135
; {"displayName":"Dyed Chiseled Nether Bricks"}
r/^moredyes:dyedBlock$/ 6136-6253
; {"displayName":"Dyed End Stone Bricks"}
r/^moredyes:dyedBlock$/ 6254-6371
; {"displayName":"Dyed Bone Blocks"}
r/^moredyes:dyedBlock$/ 6372-6489
; {"displayName":"Dyed Glazed Terracotta"}
r/^moredyes:dyedBlock$/ 6490-6607
; {"displayName":"Dyed Blocks of Netherite"}
r/^moredyes:dyedBlock$/ 6608-6725
; {"displayName":"Dyed Crying Obsidian"}
r/^moredyes:dyedBlock$/ 6726-6843
; {"displayName":"Dyed Cornflowers"}
r/^moredyes:dyedBlock$/ 6844-6961
; {"displayName":"Dyed Chains"}
r/^moredyes:dyedBlock$/ 6962-7079
; {"displayName":"Dyed End Stone"}
r/^moredyes:dyedBlock$/ 7552-7669
; {"displayName":"Dyed Gravel"}
r/^moredyes:dyedBlock$/ 7670-7787
; {"displayName":"Dyed Ice"}
r/^moredyes:dyedBlock$/ 7788-7905
; {"displayName":"Dyed Packed Ice"}
r/^moredyes:dyedBlock$/ 7906-8023
; {"displayName":"Dyed Snow Blocks"}
r/^moredyes:dyedBlock$/ 8024-8141
; {"displayName":"Dyed Quartz Bricks"}
r/^moredyes:dyedBlock$/ 8142-8259
; {"displayName":"Dyed Chiseled Quartz Blocks"}
r/^moredyes:dyedBlock$/ 8260-8377
; {"displayName":"Dyed Quartz Pillars"}
r/^moredyes:dyedBlock$/ 8378-8495
; {"displayName":"Dyed Chiseled Sandstone"}
r/^moredyes:dyedBlock$/ 8496-8613
; {"displayName":"Dyed Spruce Planks"}
r/^moredyes:dyedBlock$/ 8614-8731
; {"displayName":"Dyed Birch Planks"}
r/^moredyes:dyedBlock$/ 8732-8849
; {"displayName":"Dyed Jungle Planks"}
r/^moredyes:dyedBlock$/ 8850-8967
; {"displayName":"Dyed Acacia Planks"}
r/^moredyes:dyedBlock$/ 8968-9085
; {"displayName":"Dyed Dark Oak Planks"}
r/^moredyes:dyedBlock$/ 9086-9203
; {"displayName":"Dyed Alliums"}
r/^moredyes:dyedBlock$/ 9204-9321
; {"displayName":"Dyed Azure Bluets"}
r/^moredyes:dyedBlock$/ 9322-9439
; {"displayName":"Dyed Blue Orchids"}
r/^moredyes:dyedBlock$/ 9440-9557
; {"displayName":"Dyed Dandelions"}
r/^moredyes:dyedBlock$/ 9558-9675
; {"displayName":"Dyed Lilies of the Valley"}
r/^moredyes:dyedBlock$/ 9676-9793
; {"displayName":"Dyed Oxeye Daisies"}
r/^moredyes:dyedBlock$/ 9794-9911
; {"displayName":"Dyed Poppies"}
r/^moredyes:dyedBlock$/ 9912-10029
; {"displayName":"Dyed Rockwool"}
r/^moredyes:dyedBlock$/ 10030-10147
; {"displayName":"Dyed Stone Stairs"}
r/^moredyes:dyedBlock$/ 10856-10973
; {"displayName":"Dyed Granite Stairs"}
r/^moredyes:dyedBlock$/ 10974-11091
; {"displayName":"Dyed Polished Granite Stairs"}
r/^moredyes:dyedBlock$/ 11092-11209
; {"displayName":"Dyed Speckled Granite Stairs"}
r/^moredyes:dyedBlock$/ 11210-11327
; {"displayName":"Dyed Smooth Speckled Granite Stairs"}
r/^moredyes:dyedBlock$/ 11328-11445
; {"displayName":"Dyed Diorite Stairs"}
r/^moredyes:dyedBlock$/ 11446-11563
; {"displayName":"Dyed Polished Diorite Stairs"}
r/^moredyes:dyedBlock$/ 11564-11681
; {"displayName":"Dyed Andesite Stairs"}
r/^moredyes:dyedBlock$/ 11682-11799
; {"displayName":"Dyed Polished Andesite Stairs"}
r/^moredyes:dyedBlock$/ 11800-11917
; {"displayName":"Dyed Mossy Cobblestone Stairs"}
r/^moredyes:dyedBlock$/ 11918-12035
; {"displayName":"Dyed Mossy Stone Brick Stairs"}
r/^moredyes:dyedBlock$/ 12036-12153
; {"displayName":"Dyed End Stone Brick Stairs"}
r/^moredyes:dyedBlock$/ 12154-12271
; {"displayName":"Dyed Smooth Sandstone Stairs"}
r/^moredyes:dyedBlock$/ 12272-12389
; {"displayName":"Dyed Smooth Quartz Stairs"}
r/^moredyes:dyedBlock$/ 12390-12507
; {"displayName":"Dyed Nether Brick Stairs"}
r/^moredyes:dyedBlock$/ 12508-12625
; {"displayName":"Dyed Stone Slabs"}
r/^moredyes:dyedBlock$/ 12626-12743
; {"displayName":"Dyed Cut Sandstone Slabs"}
r/^moredyes:dyedBlock$/ 12744-12861
; {"displayName":"Dyed Diorite Slabs"}
r/^moredyes:dyedBlock$/ 12862-12979
; {"displayName":"Dyed Brick Walls"}
r/^moredyes:dyedBlock$/ 12980-13097
; {"displayName":"Dyed Stone Brick Walls"}
r/^moredyes:dyedBlock$/ 13098-13215
; {"displayName":"Dyed Mossy Stone Brick Walls"}
r/^moredyes:dyedBlock$/ 13216-13333
; {"displayName":"Dyed Granite Walls"}
r/^moredyes:dyedBlock$/ 13334-13451
; {"displayName":"Dyed Diorite Walls"}
r/^moredyes:dyedBlock$/ 13452-13569
; {"displayName":"Dyed Andesite Walls"}
r/^moredyes:dyedBlock$/ 13570-13687
; {"displayName":"Dyed Nether Brick Walls"}
r/^moredyes:dyedBlock$/ 13688-13805
; {"displayName":"Dyed Sandstone Walls"}
r/^moredyes:dyedBlock$/ 13806-13923
; {"displayName":"Dyed End Stone Brick Walls"}
r/^moredyes:dyedBlock$/ 13924-14041
; {"displayName":"Dyed Cobblestone Stairs"}
r/^moredyes:dyedBlock$/ 14042-14159
; {"displayName":"Dyed Stone Brick Stairs"}
r/^moredyes:dyedBlock$/ 14160-14277
; {"displayName":"Dyed Cracked Stone Brick Stairs"}
r/^moredyes:dyedBlock$/ 14278-14395
; {"displayName":"Dyed Carved Stone Brick Stairs"}
r/^moredyes:dyedBlock$/ 14396-14513
; {"displayName":"Dyed Brick Stairs"}
r/^moredyes:dyedBlock$/ 14514-14631
; {"displayName":"Dyed Hardened Stained Clay Stairs"}
r/^moredyes:dyedBlock$/ 14632-14749
; {"displayName":"Dyed Coal Stairs"}
r/^moredyes:dyedBlock$/ 14750-14867
; {"displayName":"Dyed Lapis Stairs"}
r/^moredyes:dyedBlock$/ 14868-14985
; {"displayName":"Dyed Redstone Stairs"}
r/^moredyes:dyedBlock$/ 14986-15103
; {"displayName":"Dyed Quartz Stairs"}
r/^moredyes:dyedBlock$/ 15104-15221
; {"displayName":"Dyed Quartz Brick Stairs"}
r/^moredyes:dyedBlock$/ 15222-15339
; {"displayName":"Dyed Chiseled Quartz Stairs"}
r/^moredyes:dyedBlock$/ 15340-15457
; {"displayName":"Dyed Quartz Pillar Stairs"}
r/^moredyes:dyedBlock$/ 15458-15575
; {"displayName":"Dyed Obsidian Stairs"}
r/^moredyes:dyedBlock$/ 15576-15693
; {"displayName":"Dyed Glowstone Stairs"}
r/^moredyes:dyedBlock$/ 15694-15811
; {"displayName":"Dyed Soulsand Stairs"}
r/^moredyes:dyedBlock$/ 15812-15929
; {"displayName":"Dyed Sand Stairs"}
r/^moredyes:dyedBlock$/ 15930-16047
; {"displayName":"Dyed Sandstone Stairs"}
r/^moredyes:dyedBlock$/ 16048-16165
; {"displayName":"Dyed Chiseled Sandstone Stairs"}
r/^moredyes:dyedBlock$/ 16166-16283
; {"displayName":"Dyed Cut Sandstone Stairs"}
r/^moredyes:dyedBlock$/ 16284-16401
; {"displayName":"Dyed End Stone Stairs"}
r/^moredyes:dyedBlock$/ 16402-16519
; {"displayName":"Dyed Ice Stairs"}
r/^moredyes:dyedBlock$/ 16520-16637
; {"displayName":"Dyed Packed Ice Stairs"}
r/^moredyes:dyedBlock$/ 16638-16755
; {"displayName":"Dyed Snow Stairs"}
r/^moredyes:dyedBlock$/ 16756-16873
; {"displayName":"Dyed Bone Block Stairs"}
r/^moredyes:dyedBlock$/ 16874-16991
; {"displayName":"Dyed Clear Glass Stairs"}
r/^moredyes:dyedBlock$/ 16992-17109
; {"displayName":"Dyed Foggy Glass Stairs"}
r/^moredyes:dyedBlock$/ 17110-17227
; {"displayName":"Dyed Wool Stairs"}
r/^moredyes:dyedBlock$/ 17228-17345
; {"displayName":"Dyed Concrete Stairs"}
r/^moredyes:dyedBlock$/ 17346-17463
; {"displayName":"Dyed Concrete Powder Stairs"}
r/^moredyes:dyedBlock$/ 17464-17581
; {"displayName":"Dyed Oak Stairs"}
r/^moredyes:dyedBlock$/ 17582-17699
; {"displayName":"Dyed Spruce Stairs"}
r/^moredyes:dyedBlock$/ 17700-17817
; {"displayName":"Dyed Birch Stairs"}
r/^moredyes:dyedBlock$/ 17818-17935
; {"displayName":"Dyed Jungle Stairs"}
r/^moredyes:dyedBlock$/ 17936-18053
; {"displayName":"Dyed Acacia Stairs"}
r/^moredyes:dyedBlock$/ 18054-18171
; {"displayName":"Dyed Dark Oak Stairs"}
r/^moredyes:dyedBlock$/ 18172-18289
; {"displayName":"Dyed Cobblestone Slabs"}
r/^moredyes:dyedBlock$/ 18290-18407
; {"displayName":"Dyed Mossy Cobblestone Slabs"}
r/^moredyes:dyedBlock$/ 18408-18525
; {"displayName":"Dyed Stone Brick Slabs"}
r/^moredyes:dyedBlock$/ 18526-18643
; {"displayName":"Dyed Cracked Stone Brick Slabs"}
r/^moredyes:dyedBlock$/ 18644-18761
; {"displayName":"Dyed Carved Stone Brick Slabs"}
r/^moredyes:dyedBlock$/ 18762-18879
; {"displayName":"Dyed Mossy Stone Brick Slabs"}
r/^moredyes:dyedBlock$/ 18880-18997
; {"displayName":"Dyed Brick Slabs"}
r/^moredyes:dyedBlock$/ 18998-19115
; {"displayName":"Dyed Hardened Stained Clay Slabs"}
r/^moredyes:dyedBlock$/ 19116-19233
; {"displayName":"Dyed Coal Slabs"}
r/^moredyes:dyedBlock$/ 19234-19351
; {"displayName":"Dyed Lapis Slabs"}
r/^moredyes:dyedBlock$/ 19352-19469
; {"displayName":"Dyed Redstone Slabs"}
r/^moredyes:dyedBlock$/ 19470-19587
; {"displayName":"Dyed Quartz Slabs"}
r/^moredyes:dyedBlock$/ 19588-19705
; {"displayName":"Dyed Quartz Brick Slabs"}
r/^moredyes:dyedBlock$/ 19706-19823
; {"displayName":"Dyed Chiseled Quartz Slabs"}
r/^moredyes:dyedBlock$/ 19824-19941
; {"displayName":"Dyed Quartz Pillar Slabs"}
r/^moredyes:dyedBlock$/ 19942-20059
; {"displayName":"Dyed Smooth Quartz Slabs"}
r/^moredyes:dyedBlock$/ 20060-20177
; {"displayName":"Dyed Obsidian Slabs"}
r/^moredyes:dyedBlock$/ 20178-20295
; {"displayName":"Dyed Glowstone Slabs"}
r/^moredyes:dyedBlock$/ 20296-20413
; {"displayName":"Dyed Soulsand Slabs"}
r/^moredyes:dyedBlock$/ 20414-20531
; {"displayName":"Dyed Sand Slabs"}
r/^moredyes:dyedBlock$/ 20532-20649
; {"displayName":"Dyed Sandstone Slabs"}
r/^moredyes:dyedBlock$/ 20650-20767
; {"displayName":"Dyed Chiseled Sandstone Slabs"}
r/^moredyes:dyedBlock$/ 20768-20885
; {"displayName":"Dyed Granite Slabs"}
r/^moredyes:dyedBlock$/ 20886-21003
; {"displayName":"Dyed Andesite Slabs"}
r/^moredyes:dyedBlock$/ 21004-21121
; {"displayName":"Dyed Polished Granite Slabs"}
r/^moredyes:dyedBlock$/ 21122-21239
; {"displayName":"Dyed Speckled Granite Slabs"}
r/^moredyes:dyedBlock$/ 21240-21357
; {"displayName":"Dyed Smooth Speckled Granite Slabs"}
r/^moredyes:dyedBlock$/ 21358-21475
; {"displayName":"Dyed Polished Diorite Slabs"}
r/^moredyes:dyedBlock$/ 21476-21593
; {"displayName":"Dyed Polished Andesite Slabs"}
r/^moredyes:dyedBlock$/ 21594-21711
; {"displayName":"Dyed End Stone Slabs"}
r/^moredyes:dyedBlock$/ 21712-21829
; {"displayName":"Dyed Ice Slabs"}
r/^moredyes:dyedBlock$/ 21830-21947
; {"displayName":"Dyed Packed Ice Slabs"}
r/^moredyes:dyedBlock$/ 21948-22065
; {"displayName":"Dyed Snow Slabs"}
r/^moredyes:dyedBlock$/ 22066-22183
; {"displayName":"Dyed Bone Block Slabs"}
r/^moredyes:dyedBlock$/ 22184-22301
; {"displayName":"Dyed Clear Glass Slabs"}
r/^moredyes:dyedBlock$/ 22302-22419
; {"displayName":"Dyed Foggy Glass Slabs"}
r/^moredyes:dyedBlock$/ 22420-22537
; {"displayName":"Dyed Wool Slabs"}
r/^moredyes:dyedBlock$/ 22538-22655
; {"displayName":"Dyed Concrete Slabs"}
r/^moredyes:dyedBlock$/ 22656-22773
; {"displayName":"Dyed Concrete Powder Slabs"}
r/^moredyes:dyedBlock$/ 22774-22891
; {"displayName":"Dyed Oak Slabs"}
r/^moredyes:dyedBlock$/ 22892-23009
; {"displayName":"Dyed Spruce Slabs"}
r/^moredyes:dyedBlock$/ 23010-23127
; {"displayName":"Dyed Birch Slabs"}
r/^moredyes:dyedBlock$/ 23128-23245
; {"displayName":"Dyed Jungle Slabs"}
r/^moredyes:dyedBlock$/ 23246-23363
; {"displayName":"Dyed Acacia Slabs"}
r/^moredyes:dyedBlock$/ 23364-23481
; {"displayName":"Dyed Dark Oak Slabs"}
r/^moredyes:dyedBlock$/ 23482-23599
; {"displayName":"Dyed Crafting Table Slabs"}
r/^moredyes:dyedBlock$/ 23600-23717
; {"displayName":"Dyed Cobblestone Walls"}
r/^moredyes:dyedBlock$/ 23718-23835
; {"displayName":"Dyed Mossy Cobblestone Walls"}
r/^moredyes:dyedBlock$/ 23836-23953
; {"displayName":"Dyed End Stone Walls"}
r/^moredyes:dyedBlock$/ 23954-24071
; {"displayName":"Dyed Oak Fences"}
r/^moredyes:dyedBlock$/ 24072-24189
; {"displayName":"Dyed Spruce Fences"}
r/^moredyes:dyedBlock$/ 24190-24307
; {"displayName":"Dyed Birch Fences"}
r/^moredyes:dyedBlock$/ 24308-24425
; {"displayName":"Dyed Jungle Fences"}
r/^moredyes:dyedBlock$/ 24426-24543
; {"displayName":"Dyed Acacia Fences"}
r/^moredyes:dyedBlock$/ 24544-24661
; {"displayName":"Dyed Dark Oak Fences"}
r/^moredyes:dyedBlock$/ 24662-24779
; {"displayName":"Dyed Lilacs"}
r/^moredyes:dyedBlock$/ 10502-10619
; {"displayName":"Dyed Peonys"}
r/^moredyes:dyedBlock$/ 10620-10737
; {"displayName":"Dyed Rose Bushes"}
r/^moredyes:dyedBlock$/ 10738-10855
; {"displayName":"Dyed Signs"}
r/^moredyes:sign$/
; {"displayName":"Dyed Beds"}
r/^moredyes:dyedBed$/
; {"displayName":"Dyed Iron Trapdoors"}
r/^moredyes:dyedBlock$/ 10148-10265
; {"displayName":"Dyed Pistons"}
r/^moredyes:dyedBlock$/ 10266-10383
; {"displayName":"Dyed Sticky Pistons"}
r/^moredyes:dyedBlock$/ 10384-10501
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
; {"displayName":"Dyed Shulker Boxes"}
r/^moredyes:dyedShulkerBox$/
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

Stairs, slabs, walls, fences, tall flowers, trapdoors, pistons and beds need their metadata for their direction, shape or half, so they keep their color in a small tile entity instead; signs keep it with their text. Beds are drawn by `client/DyedBedRenderer`, a copy of the vanilla bed renderer that tints the sheet and draws the pillow and frame over it untinted. Each is a single block for all colors, with the color's number (see `ColorIndex`) as the item damage.

## For other mods

Other mods can use the colors through `info.kg6jay.moredyes.api.MoreDyesAPI` (the exact color of any dye or dyed block, dyes by color, sheep colors, the nearest vanilla color) and the `moredyesDye` ore name, which holds every dye. See [API.md](../API.md).

## Downloads and releases

GitHub Actions builds the mod on every push, then starts a server with it and a fresh world; the build fails if the mod crashes or throws an error while loading. Pushing or merging to `main` updates the **Development build** pre-release on the Releases page with the newest jar. These builds are for testing.

To publish a proper release, tag the commit with a version number starting with `v` and push the tag:

```
git tag v1.2.0
git push origin v1.2.0
```

The workflow builds that commit and creates a release named after the tag, with the jar attached and release notes generated from the merged pull requests and commits.

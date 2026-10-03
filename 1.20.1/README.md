# More Dyes for Minecraft Forge 1.20.1

This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water, or by dipping them in a cauldron of water.

If Rechiseled is installed, the dyed blocks can be chiseled into each other and into their vanilla block.

If Iron Chests is installed, its iron, gold, diamond and copper chests come in every color: craft one with a dye (a dyed one can be dyed again). The wood takes the color and the metal keeps its own. Iron Chests' upgrades keep the color, and also turn a dyed wooden chest into a dyed iron or copper chest.

If Storage Drawers is installed, its drawers come in every color in all six sizes: craft any wooden drawer of a size (or a dyed one) with a dye; it keeps its contents, name and upgrades. They work with hoppers, keys, upgrades and drawer controllers like Storage Drawers' own. Their textures are made from Storage Drawers' oak drawers (MIT licensed, by Texelsaur).

To run the game with both mods while developing, pass `-Pcompat_mods=true` to Gradle (for example `./gradlew runClient -Pcompat_mods=true`). tools/make_compat_resources.py writes their models, recipes and textures.

Every dye also counts as the vanilla dye color it looks closest to (the Forge `forge:dyes/<color>` tags), so mods that only know the 16 vanilla colors accept it. With Ender Storage, for example, any More Dyes dye sets a frequency button to its nearest vanilla color, and with Applied Energistics 2 it colors cables and paint balls (eight fluix cables or matter balls around the dye) and loads into the Color Applicator as that color. tools/make_dye_tags.py writes these tags. It also writes the same lists as Fabric's `c:<color>_dyes` tags for the Fabric build.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in.

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips.

Sheep can be dyed any MoreDyes color, and a few spawn in the world already dyed.

With Roughly Enough Items (REI) installed, its item list shows each kind of item as one collapsible entry holding all of its colors, such as "Dyed Oak Planks", and the dyes as one "Mixed Dyes" entry. JEI and EMI have no such groups and list every color on its own. REI is optional; to try it in the dev game, run `./gradlew runClient -Precipe_viewer=true`.

## Building

Needs Java 17.

- `./gradlew build` builds the mod jar into `build/libs`.
- `./gradlew runClient` and `./gradlew runServer` start the game with the mod.
- `./gradlew runData` writes the generated blockstates, models, recipes, loot tables, tags and language file into
  `src/generated/resources`. It needs about 8 GB of memory: `_JAVA_OPTIONS=-Xmx8g ./gradlew runData`.
- `python3 tools/make_tinted_textures.py` rebuilds the grey block textures from the vanilla ones (needs Pillow and a
  finished `./gradlew build`). Run it after adding a dyed block type.

The wall blocks take about 1 GB of memory. They can be turned off with `wall_blocks` in `config/moredyes-client.toml`;
a server and its players must use the same setting.

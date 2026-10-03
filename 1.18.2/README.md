This is the Minecraft 1.18.2 (Forge) version of More Dyes. It needs Java 17 to build and play.

This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water.

If Thermal Expansion is detected, rockwool will be available in the mod colors.

If Chisel is detected, all colors of all blocks will be chiselable.

If Iron Chests is installed, its iron, gold, diamond and copper chests come in every color: craft one with a dye (a dyed one can be dyed again). The wood takes the color and the metal keeps its own. Iron Chests' upgrades keep the color, and also turn a dyed wooden chest into a dyed iron or copper chest.

If Storage Drawers is installed, its drawers come in every color in all six sizes: craft any wooden drawer of a size (or a dyed one) with a dye; it keeps its contents, name and upgrades. They work with hoppers, keys, upgrades and drawer controllers like Storage Drawers' own. Their textures are made from Storage Drawers' oak drawers (MIT licensed, by Texelsaur).

To run the game with both mods while developing, pass `-Pcompat_mods=true` to Gradle (for example `./gradlew runClient -Pcompat_mods=true`). tools/make_compat_resources.py writes their models, recipes and textures.

Every dye also counts as the vanilla dye color it looks closest to (the Forge `forge:dyes/<color>` tags), so mods that only know the 16 vanilla colors accept it. With Ender Storage, for example, any More Dyes dye sets a frequency button to its nearest vanilla color. tools/make_dye_tags.py writes these tags.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in. 

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips. 

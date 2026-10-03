This is the Minecraft 1.18.2 (Forge) version of More Dyes. It needs Java 17 to build and play.

This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water.

If Thermal Expansion is detected, rockwool will be available in the mod colors.

If Chisel is detected, all colors of all blocks will be chiselable.

With Roughly Enough Items (REI) installed, its item list shows each kind of item as one collapsible entry holding all of its colors, such as "Dyed Oak Planks", and the dyes as one "Mixed Dyes" entry. JEI has no such groups and lists every color on its own. REI is optional; to try it in the dev game, run `./gradlew runClient -Precipe_viewer=true`.

Every dye also counts as the vanilla dye color it looks closest to (the Forge `forge:dyes/<color>` tags), so mods that only know the 16 vanilla colors accept it. With Ender Storage, for example, any More Dyes dye sets a frequency button to its nearest vanilla color. tools/make_dye_tags.py writes these tags.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in. 

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips. 

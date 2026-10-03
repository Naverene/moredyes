# More Dyes (Minecraft 1.12.2)

More Dyes adds 118 dyes to Minecraft, each a mix of two of the 16 vanilla dyes, and a set of blocks in every one of those colors. This is the Forge 1.12.2 version.

## What it adds

- **Dyes.** Craft two different vanilla dyes together to get two of the mixed dye. Six pairs already make a vanilla dye (for example red and yellow make orange); for those, use two of each to get four of the mixed dye.
- **Dyed blocks.** Put eight blocks around a dye to get eight dyed blocks. This works for wool, stone, cobblestone, the three kinds of stone bricks, diorite, bricks, terracotta and stained terracotta, sand, sandstone, glass, glass panes, quartz, obsidian, soul sand, glowstone, bookshelves, planks, and the blocks of coal, lapis and redstone.
- **Dyed chests and crafting tables.** Craft a chest or crafting table with a dye, or build one from dyed planks of a single color. Two dyed chests of the same color join into a double chest.
- **Dye trees and tulips.** Both generate in the overworld in every color. Craft a sapling with a dye to get a dyed sapling. The leaves drop their sapling and their dye, and a tulip crafts into one dye.
- **Dyed sheep.** Right-click a sheep with one of the dyes to color it. Shearing or killing it gives wool of that color, the wool grows back in it, and lambs take after a parent. A few sheep (5% by default) get a random color when they first appear in the world.
- **Washing.** Craft a dyed block with a water bucket to get the vanilla block back, or right-click a cauldron holding water with a stack of dyed blocks to wash the whole stack for one level of water.

Dyed blocks behave like the vanilla blocks they are made from: dyed stone drops dyed cobblestone, dyed cobblestone smelts into dyed stone, dyed sand falls and smelts into dyed glass, dyed bookshelves power an enchanting table, and so on. They are also in the ore dictionary under the same names as the vanilla blocks, so recipes from other mods accept them.

If Thermal Foundation is installed, rockwool can be dyed too: eight rockwool of any color around a dye. Washing it gives back light gray rockwool, the color it is made in.

If GregTech CEu is installed, a mixer dyes a stack of 64 vanilla blocks with one dye (30 EU/t, 64 seconds) and mixes the mod's dyes from vanilla dyes with twice the crafting table's output, so two dyes make four (30 EU/t, 5 seconds), and a chemical bath bleaches a dyed block back to its vanilla block with 50 L of chlorine, the way GregTech bleaches wool. A chemical reactor also turns any of the mod's dyes into GregTech's chemical dye of the vanilla color it looks closest to (one dye, two salt and 250 L of sulfuric acid make 288 L, as with a vanilla dye), so they fill spray cans for coloring GregTech's pipes, cables and machines. The `gregtechRecipes` config option turns these off.

If Chisel or Rechiseled is installed, every dyed shade can be chiseled to and from its vanilla block (stone, wool, planks, chests and the rest). Where the chisel mod already has a group for the vanilla block, the shades join it. Neither mod is needed.

If Iron Chests is installed, its iron, gold, diamond, copper, silver, crystal and obsidian chests come in every color: craft one with a dye (a dyed one can be dyed again). The panels of each side take the color and the edges, latch and inside keep the metal's; on the crystal chest, whose sides are clear glass, the edges take the color. Iron Chests' upgrades keep the color, and also turn a dyed wooden chest into a dyed iron or copper chest.

If Storage Drawers is installed, its drawers come in every color in all five sizes: craft any wooden drawer of a size with a dye. They work with hoppers, keys, upgrades and drawer controllers like Storage Drawers' own. Their textures are made from Storage Drawers' oak drawers (MIT licensed, by jaquadro and Texelsaur).

If Applied Energistics 2 is installed, every dye counts as the vanilla color it looks closest to: eight fluix cables (any kind) or matter balls around it make eight cables or paint balls of that color, and the Color Applicator takes it. The dyes are in the ore dictionary as moredyes plus that color (moredyesLightBlue, for example) rather than AE2's dyeLightBlue, which the mod's own dye mixes use. To try it in the dev game, pass `-PwithAE2` to Gradle.

With Had Enough Items (HEI, CleanroomMC's fork of JEI, 4.30 or newer) installed, its item list shows each kind of block (and each tier of dyed Iron Chests and size of dyed drawers) as one collapsible entry holding all of its colors, such as "Dyed Wool", and the dyes as one "Mixed Dyes" entry. The original JEI has no such groups and lists every color on its own.

The config file has switches for the tree and tulip generation and for whether mobs can spawn on the dyed blocks, and sets how often sheep get a random color.

## Building

The mod builds with Java 8:

    ./gradlew build

The jar is written to `build/libs`.

To run the game from the project, import it into your IDE first (or run `./gradlew eclipse` once). That makes ForgeGradle decompile Minecraft, which `runClient` and `runServer` need on 1.12.2; without it they crash while Forge loads.

Every color of a block shares one grey texture, which the game tints with the dye color. The textures, models and blockstate files are written by `tools/make_assets.py`; run it again after changing it.

None of the mods above is needed to build or run the mod. To try the integrations in the dev game, run `./gradlew prepareTestMods -PwithThermal -PwithGregTech -PwithHEI` once, then add the same `-P` options to `runClient` or `runServer`. That fetches Thermal Foundation, GregTech CEu or Had Enough Items (and the libraries they need) for the dev game only. Likewise `-Pcompat_mods=true` puts Iron Chests and Storage Drawers in the dev game (no `prepareTestMods` needed); `tools/make_compat_resources.py` writes their models, blockstates and drawer textures.

The Rechiseled chiseling groups (`assets/moredyes/chiseling_recipes`) are written by `tools/make_chisel_recipes.py`; the Chisel groups are sent as IMC messages from `compat/ChiselCompat.java`. Keep the two in step.

## Builds and releases

GitHub Actions builds every push and pull request, and starts a server with the mod to check that it loads. Every push to `main` updates the "Development build" pre-release with the newest jar. To publish a release, push a version tag such as `v1.0.0`; the build attaches the jar to a release with that name.

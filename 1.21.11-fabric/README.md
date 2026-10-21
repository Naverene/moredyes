# More Dyes for Minecraft Fabric 1.21.11

The Fabric version of More Dyes for Minecraft 1.21.11. It has everything the NeoForge 1.21.11 version has: 118 new
dyes, dyed blocks with their slabs, stairs and walls, dyed glass and panes, chests, signs, pistons, bookshelves,
crafting tables (and the crafting table slab), logs, planks, leaves, saplings, fences and flowers, dyed sheep, washing
blocks in a cauldron, dye trees and tulips in the world, and the Rechiseled data. It needs
[Fabric API](https://modrinth.com/mod/fabric-api). See [../1.21.11/README.md](../1.21.11/README.md) for how to play.

With Roughly Enough Items (REI) installed, its item list shows each kind of item as one collapsible entry holding all
of its colors, such as "Dyed Oak Planks", and the dyes as one "Mixed Dyes" entry. JEI and EMI have no such groups and
list every color on its own. REI is optional; to try it in the dev game, run
`./gradlew runClient -Precipe_viewer=true`.

## How it relates to the NeoForge version

The Java code started as a copy of `../1.21.11` and keeps its class names and Mojang names. The NeoForge registries,
events and config are replaced with Fabric API and a few mixins:

- `entity/DyedSheep` keeps a sheep's More Dyes color in a Fabric data attachment (saved, and synced to players), and
  swaps the vanilla wool a dyed sheep drops for its dyed wool with Fabric's loot drop event, where NeoForge uses an
  attachment and a global loot modifier. `mixin/SheepMixin` colors spawned and bred sheep, and
  `mixin/SheepRendererMixin` hands the color to the render state.
- `mixin/PortalShapeMixin` lets dyed obsidian build nether portals (NeoForge asks the block).
- `mixin/RecipeManagerMixin` applies NeoForge's recipe priorities, so eight dyed planks make a dyed chest rather than
  a vanilla one.
- `registry/DataMaps` hands NeoForge's furnace fuel and compost data maps to Fabric's fuel and composting registries.
- `world/ModWorldGen` adds the dye trees and tulips to the same biomes as NeoForge's biome modifier.
- `moredyes.accesswidener` opens up the vanilla lists of item tint and special item model types, which Fabric API has
  no registry for.

The models, textures, language file, recipes, loot tables, tags, worldgen features and Rechiseled files are not copied:
the build reads them from `../1.21.11/src/main/resources` and `../1.21.11/src/generated/resources`, so this folder needs
the NeoForge folder next to it. NeoForge-only files are left out (the biome modifier and the loot modifier), the data
maps and recipe priorities go into the jar under `moredyes/fabric/` for the mod to read, and the dyed drawer and Iron
Chests recipes and loot tables are rewritten with Fabric's load conditions while building. To add a block, add it on the
NeoForge side, run its `tools/generate_resources.py`, and check that it registers here too.

The settings are in `config/moredyes.properties`: `sheep_spawn_chance` (0 to 1, 0.05 by default) is the chance that a
sheep spawning in the world has a random More Dyes color. The same file can turn off shapes, block types, world gen
and mod compat; see "Turning parts of the mod off" in the [main README](../README.md).

## Storage Drawers

If [Storage Drawers](https://modrinth.com/mod/storagedrawers) (its own Fabric version) is installed, its drawers come in
every color in all six sizes, as on NeoForge: craft any wooden drawer of a size with a dye. The code is in
`compat/storagedrawers`; the models, textures, tags and recipes are the NeoForge folder's. Storage Drawers makes its
block entity types from its list of drawer blocks while it initializes, and Fabric runs the mods' initializers in no set
order, so `StorageDrawersBlockEntitiesMixin` registers the dyed drawers from Storage Drawers' initializer, just before it
makes them. Those mixins are only applied when Storage Drawers is installed.

## Iron Chests

If [Iron Chests](https://modrinth.com/mod/ironchest) (its Fabric port, by anner) is installed, its iron, gold, diamond
and copper chests come in every color too, as on NeoForge: craft one (or a dyed one) with a dye. They hold as much as
the plain chest of the tier and open its screen, and Iron Chests' upgrade items keep the color (an iron-to-gold upgrade
on a dyed iron chest gives a dyed gold chest; a wood-to-iron upgrade on a dyed chest gives a dyed iron chest). The
Fabric port's chests are all metal, unlike the NeoForge mod's, so the metal panels take the dye color and the frame,
latch and inside keep the tier's own colors: `compat/ironchest/client/DyedIronChestTextures` splits Iron Chests' loaded
textures into a grey panel texture, tinted in code, and the rest. The code is in `compat/ironchest`; the blockstates,
item models, names, loot tables and recipes are the NeoForge folder's, with vanilla's chest item model in place of
NeoForge Iron Chests' as the item models' base. The recipes only load when Iron Chests is installed. Iron Chests' screen
is built on LibGui, which Iron Chests carries inside its jar, so More Dyes opens it by reflection rather than compiling
against LibGui.

To run the game with Storage Drawers and Iron Chests while developing, pass `-Pcompat_mods=true` to Gradle (the build
unpacks LibGui from the Iron Chests jar for the dev game).

## For other mods

Other mods can use the colors through `net.neverandy.moredyes.api.MoreDyesAPI` (the exact color of any dye or dyed block, dyes by color, sheep colors, the nearest vanilla color) and the tags `moredyes:dyes` and `c:dyes`, which hold every dye. See [API.md](../API.md).

## Building

The mod needs Java 21.

- `./gradlew build` builds the mod jar into `build/libs` as `moredyes-<mod version>-1.21.11-fabric.jar`.
- `./gradlew runClient` and `./gradlew runServer` start the game with the mod.
- `./scripts/check-server-starts.sh` starts a server on a fresh world and fails if anything goes wrong while it loads.
- `./gradlew runClientGameTest` (needs a display) builds a scene of dyed blocks, chests, a sign and dyed sheep in a
  creative world, checks the recipe priorities, fuels and compost chances, and saves screenshots to
  `build/run/clientGameTest/screenshots`. The test is in `src/gametest` and is not part of the mod jar.

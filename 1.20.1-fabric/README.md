# More Dyes for Minecraft Fabric 1.20.1

The Fabric version of More Dyes for Minecraft 1.20.1. It has everything the Forge 1.20.1 version has: more than 100
new dyes, dyed blocks, slabs, stairs and walls, dyed chests, pistons, bookshelves, crafting tables, saplings and
flowers, dyed sheep, washing blocks in a cauldron, and dye trees and tulips in the world. It needs
[Fabric API](https://modrinth.com/mod/fabric-api).

With Roughly Enough Items (REI) installed, its item list shows each kind of item as one collapsible entry holding all
of its colors, such as "Dyed Oak Planks", and the dyes as one "Mixed Dyes" entry. JEI and EMI have no such groups and
list every color on its own. REI is optional; to try it in the dev game, run
`./gradlew runClient -Precipe_viewer=true`.

## How it relates to the Forge version

The Java code started as a copy of `../1.20.1` and keeps its class names and Mojang names. The Forge registries,
events and config are replaced with Fabric API and a few mixins:

- `mixin/SheepMixin` keeps a sheep's MoreDyes color, saves it, and colors spawned and bred sheep.
- `mixin/EntityMixin` turns the vanilla wool a dyed sheep drops (sheared or killed) into its dyed wool.
- `mixin/ServerEntityMixin` tells players a sheep's color once they can see it.
- `mixin/PistonBaseBlockMixin` keeps extended dyed pistons from being pushed.
- `mixin/compat/StorageDrawersMixin` registers the dyed drawers in time for Storage Drawers (see below).

The models, textures, language file, recipes, loot tables, tags and worldgen features are not copied: the build reads
them from `../1.20.1/src/main/resources` and `../1.20.1/src/generated/resources`, so this folder needs the Forge
folder next to it. Forge-only files are left out, and the wall recipes (Forge conditional recipes) are rewritten with
Fabric's load conditions while building. To add a block, add it on the Forge side, run its `runData`, and register it
here too.

## Storage Drawers

If [Storage Drawers](https://modrinth.com/mod/storagedrawers) (its own Fabric version) is installed, its drawers come in
every color in all six sizes, as on Forge: craft any wooden drawer of a size with a dye. The code is in
`compat/storagedrawers`; the models, textures, tags and recipes are the Forge folder's (its
`tools/make_compat_resources.py` gives the recipes both loaders' mod-loaded conditions). Storage Drawers makes its block
entity types from its list of drawer blocks while it initializes, and Fabric runs the mods' initializers in no set
order, so `mixin/compat/StorageDrawersMixin` registers the dyed drawers from Storage Drawers' initializer, just before it
makes them. That mixin is only applied when Storage Drawers is installed.

## Iron Chests

If [Iron Chests](https://modrinth.com/mod/ironchest) (its Fabric port, by anner) is installed, its iron, gold, diamond
and copper chests come in every color too, as on Forge: craft one (or a dyed one) with a dye. They hold as much as the
plain chest of the tier and open its screen, and Iron Chests' upgrade items keep the color (an iron-to-gold upgrade on a
dyed iron chest gives a dyed gold chest; a wood-to-iron upgrade on a dyed chest gives a dyed iron chest). The Fabric
port's chests are all metal, unlike the Forge mod's, so the metal panels take the dye color and the frame, latch and
inside keep the tier's own colors: `compat/ironchest/client/DyedIronChestTextures` splits Iron Chests' loaded textures
into a grey panel texture, tinted in code, and the rest. The code is in `compat/ironchest`; the blockstates, names and
mineable tag are the Forge folder's, its recipes are rewritten with Fabric's load condition while building, and the item
models are this folder's own (vanilla's chest item model). The recipes only load when Iron Chests is installed. Iron
Chests' screen is built on LibGui, which Iron Chests carries inside its jar, so More Dyes opens it by reflection rather
than compiling against LibGui.

To run the game with Storage Drawers and Iron Chests while developing, pass `-Pcompat_mods=true` to Gradle (the build
unpacks LibGui from the Iron Chests jar for the dev game).

## For other mods

Other mods can use the colors through `net.neverandy.moredyes.api.MoreDyesAPI` (the exact color of any dye or dyed block, dyes by color, sheep colors, the nearest vanilla color) and the tags `moredyes:dyes` and `c:dyes`, which hold every dye. Each dye is also in the conventional tag of its nearest vanilla color, such as `c:red_dyes`; tools/make_dye_tags.py in the Forge folder writes these. See [API.md](../API.md).

## Building

Builds on Java 21 (the mod itself runs on Java 17).

- `./gradlew build` builds the mod jar into `build/libs` as `moredyes-<mod version>-1.20.1-fabric.jar`.
- `./gradlew runClient` and `./gradlew runServer` start the game with the mod.

The settings are in `config/moredyes.properties`. The wall blocks take about 1 GB of memory; turn them off with
`wall_blocks=false`. A server and its players must use the same setting. The same file can turn off the other shapes
and any block type; see "Turning parts of the mod off" in the [main README](../README.md).

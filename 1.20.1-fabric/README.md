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

The models, textures, language file, recipes, loot tables, tags and worldgen features are not copied: the build reads
them from `../1.20.1/src/main/resources` and `../1.20.1/src/generated/resources`, so this folder needs the Forge
folder next to it. Forge-only files are left out, and the wall recipes (Forge conditional recipes) are rewritten with
Fabric's load conditions while building. To add a block, add it on the Forge side, run its `runData`, and register it
here too.

## Building

Builds on Java 21 (the mod itself runs on Java 17).

- `./gradlew build` builds the mod jar into `build/libs` as `moredyes-<mod version>-1.20.1-fabric.jar`.
- `./gradlew runClient` and `./gradlew runServer` start the game with the mod.

The settings are in `config/moredyes.properties`. The wall blocks take about 1 GB of memory; turn them off with
`wall_blocks=false`. A server and its players must use the same setting.

# More Dyes for Minecraft 26.3

This mod adds 118 new dyes to Minecraft, one for every mix of two vanilla dye colors. Each color also comes with
blocks dyed in it: wool, stone and stone bricks (in all varieties), cobblestone and mossy cobblestone, andesite,
diorite and granite (plain and polished), end stone, bricks, clay, terracotta, concrete and concrete powder, sand,
gravel and sandstone, soul sand, obsidian, glowstone, bone blocks, ice, packed ice and snow, blocks of coal, lapis,
redstone and quartz (and quartz bricks, pillars, chiseled and smooth quartz), clear and foggy glass and panes,
bookshelves, crafting tables, chests, pistons and sticky pistons, logs, planks, leaves, saplings and fences in all six
woods, and twelve flowers. Most of the blocks also come as slabs and stairs, and those vanilla has walls for as walls.
A crafting table slab works like a crafting table. The colors are named by their hex code.

This is the NeoForge port for Minecraft 26.3, made from the 1.16.5 version.

## Playing

* Dyes are crafted from two vanilla dyes. Six colors take two of each, because the plain pair already makes a vanilla
  dye. A dyed flower also makes dye of its color: one, or two from a tall flower.
* Eight vanilla blocks around a dye make eight dyed blocks. A sapling, a crafting table or a chest takes one dye.
* Dyed slabs, stairs and walls are crafted from the dyed block like vanilla's, or cut from it on a stonecutter.
* A dyed block crafted with a water bucket, or used on a water cauldron, turns back into the vanilla block.
* Dyed blocks turn into each other like the vanilla ones: cobblestone smelts to stone, logs make planks, dyed planks
  make crafting tables, chests, bookshelves and pistons of their color, and so on.
* A dye used on a sheep dyes its wool, and some sheep spawn already dyed (the chance is `sheep_spawn_chance` in the
  server config, `serverconfig/moredyes-server.toml`). Lambs take a color from a parent.
* Dye trees grow in the overworld about every four chunks, and patches of dyed tulips about every other chunk.
* Dyed blocks behave like their vanilla block: dyed obsidian makes nether portals and holds end crystals, dyed redstone
  blocks power repeaters and comparators, dyed bookshelves power enchanting tables, and dyed soul sand makes bubble
  columns.
* With [Rechiseled](https://modrinth.com/mod/rechiseled) installed, a chisel turns a vanilla block into any of its
  dyed shades and back. Rechiseled is optional: More Dyes only ships data files for it.
* With [Roughly Enough Items](https://modrinth.com/mod/rei) installed, its item list shows each kind of block as one
  collapsible entry holding all 118 colors, such as "Dyed Oak Planks", and the dyes as one "Mixed Dyes" entry (JEI has
  no such groups). To try it in the dev game, run `./gradlew runClient -Precipe_viewer=true`.
* With [Storage Drawers](https://modrinth.com/mod/storagedrawers) installed, its drawers come in every color in all six
  sizes: craft any wooden drawer of a size (or a dyed one) with a dye; a filled drawer keeps its contents. They work
  with hoppers, keys, upgrades, labels and drawer controllers like Storage Drawers' own. Their textures are made from
  Storage Drawers' oak drawers (MIT licensed, by Texelsaur). Storage Drawers is optional.

## Building

The mod needs Java 25. `./gradlew build` builds the jar into `build/libs`, `./gradlew runClient` and
`./gradlew runServer` start the game with the mod.

Every block is drawn from one grey texture that the game tints with the block's color, so the textures don't need a
file per color. The resources that come once per color (blockstates, item models, drops, recipes, tags, names and
trees) are generated from the vanilla ones and kept in `src/generated/resources`. After changing the kinds of block
(`block/Kind.java`) or the colors (`color/ColorGroup.java`), run `./gradlew build` once so Minecraft is downloaded,
then:

```
python3 tools/textures.py            # the grey textures, made from the vanilla ones (needs Pillow)
python3 tools/generate_resources.py  # everything in src/generated/resources
```

`python3 tools/storage_drawers.py` makes the dyed drawers' textures and models from the Storage Drawers jar that
`./gradlew build` downloads. To run the game with Storage Drawers while developing, pass `-Pcompat_mods=true` (for
example `./gradlew runClient -Pcompat_mods=true`).

## Releases

Every push to `main` updates the "Development build" pre-release on GitHub. Pushing a version tag such as `v3.0.0`
publishes a release with the jar and uploads it to [CurseForge](https://www.curseforge.com/minecraft/mc-mods/moredyes)
when the repository has a `CURSEFORGE_TOKEN` secret.

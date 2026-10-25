# More Dyes

More Dyes adds more than 100 new dye colors to Minecraft, with stone, wood, wool, glass, chests, sheep, pet collars and more in every color. Download it from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/moredyes) or from this repository's releases.

This repository holds every version of the mod, one folder per Minecraft version:

| Folder | Minecraft | Loader | Java |
| --- | --- | --- | --- |
| [1.7.10](1.7.10) | 1.7.10 | Forge (GTNH buildscript) | 8 to play, 21 to build |
| [1.12.2](1.12.2) | 1.12.2 | Forge | 8 |
| [1.16.5](1.16.5) | 1.16.5 | Forge | 8 |
| [1.18.2](1.18.2) | 1.18.2 | Forge | 17 |
| [1.19.2](1.19.2) | 1.19.2 | Forge | 17 |
| [1.20.1](1.20.1) | 1.20.1 | Forge | 17 |
| [1.20.1-fabric](1.20.1-fabric) | 1.20.1 | Fabric (needs Fabric API) | 17 to play, 21 to build |
| [1.21.1](1.21.1) | 1.21.1 | NeoForge | 21 |
| [1.21.1-fabric](1.21.1-fabric) | 1.21.1 | Fabric (needs Fabric API) | 21 |
| [1.21.11](1.21.11) | 1.21.11 | NeoForge | 21 |
| [1.21.11-fabric](1.21.11-fabric) | 1.21.11 | Fabric (needs Fabric API) | 21 |
| [26.1.2](26.1.2) | 26.1.2 | NeoForge | 25 |
| [26.3](26.3) | 26.3 | NeoForge | 25 |

Each Minecraft folder is its own Gradle project with its own wrapper. Open the folder for the version you want in your IDE (not the repository root), and run `./gradlew build` inside it. The jar lands in that folder's `build/libs` as `moredyes-<mod version>-<Minecraft version>-<loader>.jar`, such as `moredyes-1.3.0-1.20.1-forge.jar` or `moredyes-1.3.0-1.21.1-neoforge.jar`. Each Fabric build uses the models, textures, recipes and other data files from the Forge or NeoForge folder of the same Minecraft version, so it needs that folder next to it.

## Turning parts of the mod off

Modpacks that only want some of the blocks can leave the rest out. Blocks that are turned off are never added, so they
also save memory and loading time, and their recipes, loot tables and world gen go with them. The settings are read at
startup: restart the game after changing them. A server and its players must use the same settings, and blocks that
are turned off disappear from worlds that already have them.

| Setting | What it does |
| --- | --- |
| `stair_blocks`, `slab_blocks`, `wall_blocks`, `fence_blocks`, `pane_blocks` | Add dyed stairs, slabs, walls, wooden fences and glass panes (`true` by default). Walls take the most memory. |
| `disabled_blocks` | Block types to leave out, named as in their ids without the color. On 1.16.5 to 1.20.1 that is for example `["concretepowder", "chest", "piston", "sign"]`; on 1.21.1 and newer it is `["concrete_powder", "chest", "piston", "sign"]`. Leaving out a type also leaves out its stairs, slabs and walls. The log lists every type name if one is wrong. |
| `generate_trees`, `generate_flowers` | Grow dye trees and dyed tulips in new chunks. |
| `iron_chests`, `storage_drawers` | Add dyed Iron Chests and Storage Drawers when those mods are installed. |
| `iron_shulker_boxes` | Add dyed Iron Shulker Boxes when that mod is installed (1.16.5 and 1.20.1; on 1.12.2 it is `ironShulkerBoxes`, for the boxes in Iron Chests). |
| `Compat_EtFuturum` | 1.7.10 only: add dyed Et Futurum Requiem shulker boxes when that mod is installed. |

Where the settings live:

- Forge 1.16.5 to 1.20.1: `config/moredyes-client.toml` (on servers too, since it decides which blocks exist).
- NeoForge 1.21.1 and newer: `config/moredyes-startup.toml`.
- Fabric: `config/moredyes.properties`, with lists written as `disabled_blocks=chest,piston,sign`.
- 1.7.10 and 1.12.2 have only the world gen, Iron Chests, Iron Shulker Boxes, Et Futurum, Storage Drawers and GregTech recipe switches, in the
  mod's `.cfg` file (see that version's README).

## Wild sheep colors

Some sheep spawn in the wild with a More Dyes color, next to the white, greys, black, brown and pink vanilla sheep
spawn with. By default 20% get one of the natural browns, beiges and greys, and 2% get a rare burgundy (`592626`).
The same chances apply to sheep from spawn eggs, spawners and `/summon` (lambs still take after their parents).
These are server settings, so a server's own config decides them for everyone who plays on it.

| Setting | What it does |
| --- | --- |
| `sheep_spawn_chance` | Chance (0 to 1) that a wild sheep gets one of `sheep_spawn_colors` (`0.2` by default; `0` turns it off). |
| `sheep_spawn_colors` | The colors to pick from, as hex like `7f5933`. Empty means any More Dyes color. |
| `rare_sheep_spawn_chance` | Chance (0 to 1) that a wild sheep gets one of `rare_sheep_spawn_colors` instead (`0.02` by default). |
| `rare_sheep_spawn_colors` | The rare colors (`592626`, burgundy, by default). Empty means any More Dyes color. |

Where the settings live:

- Forge 1.16.5 to 1.20.1 and NeoForge 1.21.1 and newer: the per-world server config, `saves/<world>/serverconfig/moredyes-server.toml`
  (copy it to `defaultconfigs/` to use it for new worlds).
- Fabric: `config/moredyes.properties`, with lists written as `sheep_spawn_colors=7f5933,403326`.
- 1.4.7, 1.7.10 and 1.12.2: the mod's `.cfg` file, as `sheepSpawnChance`, `sheepSpawnColors`, `rareSheepSpawnChance`
  and `rareSheepSpawnColors`.

Worlds that already have a config file keep the `sheep_spawn_chance` saved in it (it was 0.05), but use the natural
colors from now on.

## Dyed collars

Any More Dyes dye can recolor the collar of a tamed wolf or cat you own: right-click the pet with the dye, the same as
with a vanilla dye. The collar keeps the exact More Dyes color, and a vanilla dye turns it back to a vanilla color.
Cats have collars from Minecraft 1.14 on, so 1.4.7 and 1.12.2 only dye wolf collars. On 1.7.10, cats tamed with
[Not Enough Pets](https://www.curseforge.com/minecraft/mc-mods/not-enough-pets) installed have collars too, and More
Dyes dyes color them (and Not Enough Pets' own wolves).

## For other mods

Other mods can read and use More Dyes colors through tags and the `MoreDyesAPI` class in every version; see [API.md](API.md).

## Builds and releases

[`.github/workflows/build.yml`](.github/workflows/build.yml) builds each version on its own Java and checks that it loads on a server.

- Pull requests build only the versions they change.
- Every push to the default branch (`main`) builds all versions and replaces the **Development build** pre-release with the newest jar for each one.
- Pushing a tag such as `v1.2.0` builds every version as mod version 1.2.0, publishes a release with all the jars, and uploads each jar to CurseForge (project 252137) when the `CURSEFORGE_TOKEN` secret is set. Push the tag with git (`git tag v1.2.0 && git push origin v1.2.0`) or make the release on GitHub; both work.

[`.gitlab-ci.yml`](.gitlab-ci.yml) can build the same versions on a self-hosted GitLab runner, if one is set up. It is not in use now.

## History

Each folder used to be its own repository. Their full histories were brought in here, so `git log -- 1.16.5` shows the 1.16.5 version's history. The old repositories are Naverene/MoreDyes_1_7_10, moredyes_1122, moredyes_1165, moredyes_1182, moredyes_1201 and moredyes_263. The 26.1.2 version was made here from the 26.3 one.

## License

More Dyes is released under the MIT License; see [LICENSE](LICENSE). The speckled granite textures come from GregTech 5 Unofficial and are under the LGPL-3.0; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

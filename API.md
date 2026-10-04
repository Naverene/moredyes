# More Dyes API

Other mods can work with More Dyes' 118 colors through tags (or the ore dictionary on 1.7.10 and 1.12.2) and through one Java class, `MoreDyesAPI`, in every version. Everything else in More Dyes is internal and may change between releases; `MoreDyesAPI` keeps its methods. Its `API_VERSION` field goes up when methods are added.

A More Dyes color is identified by its RGB value, `0xRRGGBB`. That is also the hex code in its name: the dye `334C59 Dye` is color `0x334C59`, and from 1.16.5 on it is in the registry names too (`moredyes:334c59_dye`, `moredyes:wool_334c59`).

## Tags and ore names

| Version | Every More Dyes dye | Dyes by nearest vanilla color |
| --- | --- | --- |
| 1.7.10, 1.12.2 | ore name `moredyesDye` (also in `dye`) | none |
| 1.16.5, 1.18.2, 1.20.1 Forge | `moredyes:dyes` | `forge:dyes/<color>`, such as `forge:dyes/red` |
| 1.20.1 Fabric | `moredyes:dyes`, `c:dyes` | `c:<color>_dyes`, such as `c:red_dyes` |
| 26.1.2, 26.3 | `moredyes:dyes`, `c:dyes` | none |

The nearest-color tags let mods that only know the 16 vanilla colors accept our dyes (Ender Storage, for example). On 26.x the vanilla recipes read NeoForge's `c:dyes/<color>` tags, so our dyes stay out of them; otherwise they would make vanilla-colored wool and glass. Use `MoreDyesAPI.nearestVanillaColor` there instead.

## MoreDyesAPI

The class is `net.neverandy.moredyes.api.MoreDyesAPI` (`info.kg6jay.moredyes.api.MoreDyesAPI` on 1.7.10). All methods are static.

| Method | What it does |
| --- | --- |
| `colors()` | Every More Dyes color as `0xRRGGBB`, in the mod's own order. |
| `isColor(int rgb)` | Whether a color is one of More Dyes' colors. |
| `isDye(ItemStack)` | Whether a stack is a More Dyes dye. |
| `getDye(int rgb, int count)` | The dye of a color, or an empty stack (`null` on 1.7.10) if it is not a More Dyes color. |
| `getColor(ItemStack)` | The color of a dye or any dyed More Dyes item (blocks, chests, pistons, and the dyed iron chests and drawers when Iron Chests or Storage Drawers is installed), as an `OptionalInt`. |
| `getColor(BlockState)` | The color of a placed dyed block. On 1.12.2 it takes an `IBlockState` and misses the dyed iron chests and drawers, which keep their color in a tile entity. |
| `getColor(world, pos)` | 1.7.10 (`world, x, y, z`) and 1.12.2 only: the color of the block at a position, tile entities included. |
| `getSheepColor(sheep)` | The More Dyes color of a sheep's wool, or empty for a vanilla color. |
| `setSheepColor(sheep, int rgb)` | Dyes a sheep a More Dyes color, like using the dye. Call it on the server; players who can see the sheep are told. Returns false if the color is not a More Dyes color. |
| `clearSheepColor(sheep)` | Gives a sheep its vanilla wool color back. |
| `displayColor(int rgb)` | The color blocks, items and sheep are drawn in. From 1.16.5 on this is a little brighter and more saturated than the color itself; on 1.7.10 and 1.12.2 it is the same. |
| `nearestVanillaColor(int rgb)` | The vanilla dye color that looks closest to any color, by CIEDE2000 distance: a `DyeColor` (`EnumDyeColor` on 1.12.2, the wool metadata on 1.7.10). For a More Dyes dye it matches the nearest-color tag above. |
| `DYES` / `ORE_DYES` | The `moredyes:dyes` item tag, or the `moredyesDye` ore name on 1.7.10 and 1.12.2. |

Example (1.20.1 Forge):

```java
OptionalInt color = MoreDyesAPI.getColor(stack);
if (color.isPresent()) {
    int rgb = color.getAsInt();                                  // the exact More Dyes color
    DyeColor nearest = MoreDyesAPI.nearestVanillaColor(rgb);     // for things that only take 16 colors
}
```

## Depending on More Dyes

Use the mod jar from CurseForge through [CurseMaven](https://www.cursemaven.com/), for example `compileOnly "curse.maven:moredyes-252137:<file id>"`, and check that More Dyes is loaded before calling the API so it stays optional for your mod.

This mod adds more than 100 new dyes to Minecraft. For now they are all combinations of the 16 vanilla dye colors. Each color also has a variety of blocks that can be colored with the dye. These include stone, stonebrick(in all varieties), wool, glowstone, even redstone and lapis blocks.

Blocks can be cleaned and returned to their vanilla color by crafting them with a bucket of water.

If Thermal Expansion is detected, rockwool will be available in the mod colors.

If Chisel is detected, all colors of all blocks will be chiselable.

I'm open to suggestions for naming the colors. Right now they are all labeled with their hex code.

Dyed saplings can now be crafted from an oak sapling and the appropriate colored dye. Thats right, dye trees are now in.

Tulips in all the colors are available from the creative menu. Worldgen now exists for both the dye trees and the tulips.


Dyed crafting tables and chests are available in every color. Craft them from four (crafting table) or eight (chest) dyed planks of one color, or combine a vanilla crafting table or chest with a dye. Two chests of the same color placed side by side join into a double chest. Craft either with a bucket of water to get the vanilla block back.

## How the colors are drawn

Blocks are not stored as one texture file per color. Each block type has one grey texture, and the game multiplies it by the dye color when it draws the block, the same way it colors grass and leaves.

The grey textures are made from the vanilla textures when the game loads, so they follow whatever resource pack the player uses. The list of which vanilla texture each block uses is in `client/TintSources.java`. Blocks that do not exist in vanilla 1.7.10 use a grey file in `textures/blocks/base/` instead (diorite, foggy glass).

To give a new block a texture, add a line to `TintSources` and call `TintedTextures.register(iconRegister, "<key>")` from the block's `registerBlockIcons`. Parts that should keep their natural color, such as a flower's stem or a log's bark, are drawn as a separate untinted layer (see `ILayeredBlock`).

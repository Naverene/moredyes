package net.neverandy.moredyes.tileentity;

/**
 * A tile entity that keeps the color of its block, by its position in ColorStrings.ALL (see block/TileColor). The
 * vanilla bed, banner and shulker box tile entities already have a getColor of their own, so these names differ.
 */
public interface IDyedTile
{
	int getColorIndex();
	void setColorIndex(int color);
}

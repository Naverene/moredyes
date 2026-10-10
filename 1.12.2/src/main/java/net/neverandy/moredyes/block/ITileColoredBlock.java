package net.neverandy.moredyes.block;

/**
 * A block that holds every color in one block and keeps the color in a TileEntityColor (see TileColor). Its item
 * damage is the color's position in ColorStrings.ALL.
 */
public interface ITileColoredBlock
{
	/** Name of the block ("granite_stairs"); also the name of its blockstate file and item model. */
	String getTypeName();
}

package info.kg6jay.moredyes.block;

/** A tile entity that holds the color of a dyed block (see TileEntityDyed). */
public interface IDyedTile {

    int getColor();

    void setColor(int color);
}

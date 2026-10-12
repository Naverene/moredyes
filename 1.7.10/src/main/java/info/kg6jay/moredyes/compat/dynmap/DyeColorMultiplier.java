package info.kg6jay.moredyes.compat.dynmap;

import net.minecraft.block.Block;

import org.dynmap.renderer.CustomColorMultiplier;
import org.dynmap.renderer.MapDataContext;

import info.kg6jay.moredyes.block.IBlockColored;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * The dye color a block is tinted with on the web map, as in the game: the shade in the metadata for the blocks made
 * per color set, or the color in the tile entity for stairs, slabs, walls, trapdoors and pistons. The map only reads
 * that tile entity for blocks whose shape asks for it (TileColorBoxRenderer and the other renderers here).
 */
public class DyeColorMultiplier extends CustomColorMultiplier {

    /** The colors of the blocks made per color set, by block id and metadata, plus one so that 0 means not known. */
    private static final int[] SHADES = new int[4096 * 16];

    @Override
    public int getColorMultiplier(MapDataContext context) {
        int id = context.getBlockTypeID();
        int index = (id << 4 | context.getBlockData() & 15) % SHADES.length;
        int shade = SHADES[index];
        if (shade == 0) {
            Block block = Block.getBlockById(id);
            if (!(block instanceof IBlockColored colored)) {
                Object color = context.getBlockTileEntityField("color");
                return ColorIndex.rgb(color instanceof Number number ? number.intValue() : 0);
            }
            shade = 1 + ColorUtil.shade(colorStrings(colored.getColorSet()), context.getBlockData());
            SHADES[index] = shade;
        }
        return shade - 1;
    }

    private static String[] colorStrings(String colorSet) {
        for (int i = 0; i < MDBlock.colors.length; i++) {
            if (MDBlock.colors[i].equals(colorSet)) {
                return MDBlock.colorStrings[i];
            }
        }
        return MDBlock.colorStrings[0];
    }
}

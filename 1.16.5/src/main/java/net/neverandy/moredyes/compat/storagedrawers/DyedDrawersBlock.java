package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Storage Drawers' standard drawers in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. Its tile
 * entity is Storage Drawers' own (BlockStandardDrawers makes it); StorageDrawersCompat lets that tile entity type
 * accept this block.
 */
public class DyedDrawersBlock extends BlockStandardDrawers
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    /** Storage Drawers' oak drawer of the same size, whose label and count positions this block shares. */
    public final String plainName;

    public DyedDrawersBlock(String size, int drawerCount, boolean halfDepth, Properties properties)
    {
        super(drawerCount, halfDepth, properties);
        this.plainName = "oak_" + size;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(COLOR);
    }

    /** Storage Drawers' drop (which may carry the contents) in this drawer's color. */
    @Override
    protected ItemStack getMainDrop(BlockState state, TileEntityDrawers tile)
    {
        return withColor(super.getMainDrop(state, tile), state);
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        return withColor(new ItemStack(this), state);
    }

    private static ItemStack withColor(ItemStack stack, BlockState state)
    {
        stack.getOrCreateChildTag("BlockStateTag").putString(COLOR.getName(), Integer.toString(state.get(COLOR)));
        return stack;
    }
}

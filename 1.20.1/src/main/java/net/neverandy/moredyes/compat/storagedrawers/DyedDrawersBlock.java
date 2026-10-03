package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neverandy.moredyes.reference.ColorStrings;

/** Storage Drawers' standard drawers in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. */
public class DyedDrawersBlock extends BlockStandardDrawers
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    public DyedDrawersBlock(int drawerCount, boolean halfDepth, Properties properties)
    {
        super(drawerCount, halfDepth, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    /** Storage Drawers' drop (which may carry the contents) in this drawer's color. */
    @Override
    protected ItemStack getMainDrop(BlockState state, BlockEntityDrawers tile)
    {
        return withColor(super.getMainDrop(state, tile), state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
    {
        return withColor(new ItemStack(this), state);
    }

    private static ItemStack withColor(ItemStack stack, BlockState state)
    {
        CompoundTag tag = new CompoundTag();
        tag.putString(COLOR.getName(), Integer.toString(state.getValue(COLOR)));
        stack.addTagElement("BlockStateTag", tag);
        return stack;
    }
}

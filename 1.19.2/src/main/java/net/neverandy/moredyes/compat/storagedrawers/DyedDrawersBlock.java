package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Storage Drawers' standard drawers in a More Dyes color. COLOR is the color's position in ColorStrings.ALL.
 *
 * <p>Storage Drawers 10.x for 1.18.2 finds drawer blocks by looking through the whole block registry for its block
 * classes (ModBlocks.getDrawers), so being a BlockStandardDrawers is all it takes to join its block entity types,
 * label geometry, models and render layers. No hook or access transformer is needed on this version.
 */
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
        return StorageDrawersCompat.withColor(super.getMainDrop(state, tile), state.getValue(COLOR));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return StorageDrawersCompat.withColor(new ItemStack(this), state.getValue(COLOR));
    }
}

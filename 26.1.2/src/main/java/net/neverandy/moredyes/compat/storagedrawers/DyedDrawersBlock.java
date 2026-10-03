package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.config.IDrawerConfig;
import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import net.neverandy.moredyes.color.MixColors;

/**
 * Storage Drawers' standard drawers in a More Dyes color. The item keeps the color in its {@code minecraft:block_state}
 * component, which the placed block takes over like any block state property.
 */
public class DyedDrawersBlock extends BlockStandardDrawers {

    /** The color's position in {@link MixColors#ALL}. */
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, MixColors.ALL.size() - 1);

    public DyedDrawersBlock(int drawerCount, boolean halfDepth, IDrawerConfig config, Properties properties) {
        super(drawerCount, halfDepth, config, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    /** Storage Drawers' drop (which may carry the contents) in this drawer's color. */
    @Override
    protected ItemStack getMainDrop(BlockState state, BlockEntityDrawers tile) {
        return withColor(super.getMainDrop(state, tile), state);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return withColor(super.getCloneItemStack(level, pos, state, includeData), state);
    }

    private static ItemStack withColor(ItemStack stack, BlockState state) {
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(COLOR, state));
        return stack;
    }
}

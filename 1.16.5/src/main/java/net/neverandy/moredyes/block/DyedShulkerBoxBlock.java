package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A dyed shulker box. It is a vanilla shulker box with no dye color (like the purple one), so it shares the vanilla
 * tile entity and keeps its contents the same way; client/DyedShulkerBoxRenderer draws it in its own color.
 */
public class DyedShulkerBoxBlock extends ShulkerBoxBlock
{
    public DyedShulkerBoxBlock(AbstractBlock.Properties properties)
    {
        super(null, properties);
    }

    /** Vanilla's shulker box properties that AbstractBlock.Properties.from leaves out. */
    public static AbstractBlock.Properties properties(AbstractBlock.Properties properties)
    {
        AbstractBlock.IPositionPredicate closed = (state, world, pos) ->
        {
            TileEntity tile = world.getTileEntity(pos);
            return !(tile instanceof ShulkerBoxTileEntity) || ((ShulkerBoxTileEntity) tile).isVisuallyClosed();
        };
        return properties.setSuffocates(closed).setBlocksVision(closed);
    }

    /**
     * A full box broken in creative drops itself with its contents. Vanilla's code would drop an undyed box, as it
     * picks the item by dye color, so the box is dropped here and emptied before vanilla's code sees it.
     */
    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (!world.isRemote && player.isCreative() && tile instanceof ShulkerBoxTileEntity && !((ShulkerBoxTileEntity) tile).isEmpty())
        {
            ShulkerBoxTileEntity box = (ShulkerBoxTileEntity) tile;
            ItemStack stack = new ItemStack(this);
            CompoundNBT contents = box.saveToNbt(new CompoundNBT());
            if (!contents.isEmpty())
            {
                stack.setTagInfo("BlockEntityTag", contents);
            }
            if (box.hasCustomName())
            {
                stack.setDisplayName(box.getCustomName());
            }
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            item.setDefaultPickupDelay();
            world.addEntity(item);
            box.clear();
        }
        super.onBlockHarvested(world, pos, state, player);
    }
}

package net.neverandy.moredyes.block;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.neverandy.moredyes.tileentity.DyedBedTileEntity;

import javax.annotation.Nullable;

/** A More Dyes bed, which remembers the planks it was made from (see DyedBedTileEntity). */
public class DyedBedBlock extends BedBlock
{
    public DyedBedBlock(DyeColor color, Properties properties)
    {
        super(color, properties);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new DyedBedTileEntity();
    }

    /** Vanilla places the head; it gets the planks too. The foot has them from the item's block entity tag. */
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        for (BlockPos part : new BlockPos[] {pos, pos.offset(state.get(HORIZONTAL_FACING))})
        {
            TileEntity entity = world.getTileEntity(part);
            if (entity instanceof DyedBedTileEntity)
            {
                ((DyedBedTileEntity) entity).setWood(DyedBedTileEntity.woodOf(stack));
            }
        }
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        ItemStack stack = super.getItem(world, pos, state);
        TileEntity entity = world.getTileEntity(pos);
        if (entity instanceof DyedBedTileEntity)
        {
            DyedBedTileEntity.setWood(stack, ((DyedBedTileEntity) entity).getWood());
        }
        return stack;
    }
}

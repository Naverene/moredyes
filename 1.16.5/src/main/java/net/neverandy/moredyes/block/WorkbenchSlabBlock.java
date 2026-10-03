package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/** Half a dyed crafting table. Using it opens the crafting grid, like a whole one. */
public class WorkbenchSlabBlock extends SlabBlock
{
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.crafting");

    public WorkbenchSlabBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }
        player.openContainer(new SimpleNamedContainerProvider((id, inventory, p) ->
                new ContainerWorkbench(id, inventory, IWorldPosCallable.of(worldIn, pos)), CONTAINER_NAME));
        player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return ActionResultType.CONSUME;
    }
}

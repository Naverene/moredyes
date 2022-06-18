package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.Random;

public class BlockSapling extends SaplingBlock
{
    //private final TreeSpawner tree;
    public BlockSapling(BlockInfo info)
    {
        super(null, Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .doesNotBlockMovement()
                .tickRandomly()
                .setLightLevel(value -> info.lightlevel));
        //this.tree = spawner;
    }
    public void placeTree(ServerWorld world, BlockPos pos, BlockState state, Random rand) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycleValue(STAGE), 4);
        }
        else {
            //tree.spawn(world, world.getChunkProvider().generator, pos, state, rand);
        }

    }
}

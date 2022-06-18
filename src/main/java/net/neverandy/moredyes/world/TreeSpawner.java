package net.neverandy.moredyes.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TreeSpawner
{
    @Nullable
    protected abstract Feature<IFeatureConfig> getFeature(Random rand);

    @Nullable
    protected abstract IFeatureConfig getConfig(Random rand);

    public boolean spawn(ISeedReader reader, ChunkGenerator gen, BlockPos pos, BlockState blockUnder, Random rand)
    {
        Feature<IFeatureConfig> tree = getFeature(rand);
        if(tree == null)
        {
            return false;
        }
        IFeatureConfig config = getConfig(rand);
        if(config == null)
        {
            return false;
        }
        reader.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        if(tree.generate(reader,gen, rand, pos, config))
        {
            return true;
        }
        reader.setBlockState(pos, blockUnder, 4);
        return false;
    }


}

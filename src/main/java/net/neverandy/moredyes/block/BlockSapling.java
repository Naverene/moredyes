package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.Random;

public class BlockSapling extends BushBlock implements IGrowable
{
    private final String color;
    public BlockSapling(String blockColor, BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));
        this.color = blockColor;
        //initModel();
    }
    public void grow(World worldIn, BlockPos pos, BlockState state, Random rand)
    {
        this.generateTree(worldIn, pos, state, rand);
    }
    public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand)
    {

    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        return false;
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {

    }
}

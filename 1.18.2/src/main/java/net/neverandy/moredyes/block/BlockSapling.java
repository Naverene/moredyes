package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.Random;

import static net.minecraftforge.event.ForgeEventFactory.saplingGrowTree;

public class BlockSapling extends BushBlock implements IGrowable
{
    private final String color;
    private final int index;
    public BlockSapling(String blockColor, BlockInfo info, int i)
    {
        super(Properties.of(info.blockMaterial)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound)
                .strength(info.hardness, info.resistance));
        this.color = blockColor;
        this.index=i;
        setRegistryName(info.blockName+"_"+this.color);
        //initModel();
    }
    @OnlyIn(Dist.CLIENT)
    public void initModel()
    {
        ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
    }
    public void grow(World worldIn, BlockPos pos, BlockState state, Random rand)
    {
        this.generateTree(worldIn, pos, state, rand);
    }
    public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand)
    {
        if (!saplingGrowTree(worldIn, rand, pos)) return;

        int i = 0;
        int j = 0;
        boolean flag = false;
        int r=rand.nextInt(100);
        if(r>10)
        {
            //worldgenerator = new WorldGenDyeTree(true, false,this.index);
        }
        else if(r>1)
        {
            //worldgenerator = new WorldGenDyeTreeBig(true,this.index);
        }
        else
        {
            // worldgenerator = new WorldGenDyeTreeHuge(true,this.index);
        }

        BlockState iblockstate2 = Blocks.AIR.defaultBlockState();

        if (flag)
        {
            worldIn.setBlock(pos.offset(i, 0, j), iblockstate2, 4);
            worldIn.setBlock(pos.offset(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlock(pos.offset(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlock(pos.offset(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        {
            worldIn.setBlock(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.offset(i, 0, j)))
        {
            if (flag)
            {
                worldIn.setBlock(pos.offset(i, 0, j), state, 4);
                worldIn.setBlock(pos.offset(i + 1, 0, j), state, 4);
                worldIn.setBlock(pos.offset(i, 0, j + 1), state, 4);
                worldIn.setBlock(pos.offset(i + 1, 0, j + 1), state, 4);
            }
            else
            {
                worldIn.setBlock(pos, state, 4);
            }
        }
    }
    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
    {
        if(worldIn.isClientSide)
        {
            //super.up(worldIn, pos, state, rand);

            if (worldIn.getBestNeighborSignal(pos.above()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
    @Override
    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_)
    {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_)
    {
        return false;
    }

    @Override
    public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_)
    {

    }
}

package net.neverandy.moredyes.block;

import java.util.Random;

import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;
import net.neverandy.moredyes.world.feature.WorldGenDyeTree;
import net.neverandy.moredyes.world.feature.WorldGenDyeTreeBig;
import net.neverandy.moredyes.world.feature.WorldGenDyeTreeHuge;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SaplingBlock extends BlockBush implements IGrowable
{
	private String color;
	private int index;
	public SaplingBlock(String blockColor, BlockInfo info, int i)
	{
		this.color = blockColor;
		this.index=i;
		setHardness(info.hardness);
		setHarvestLevel(info.harvestTool,info.harvestLevel);
		setSoundType(info.sound);
		setCreativeTab(info.tab);
		setResistance(info.resistance);
		setRegistryName(info.blockName+"_"+this.color);
		setUnlocalizedName(info.blockName+"."+this.color);
		ItemBlock itemBlock = new ItemBlock(this);
		//initModel();
	}
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0,new ModelResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
	}
    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	this.generateTree(worldIn, pos, state, rand);
    }
    public int damageDropped(IBlockState state)
    {
        return 0;
    }
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator;
        int i = 0;
        int j = 0;
        boolean flag = false;

        int r=rand.nextInt(100);
        if(r>10)
        {
        	worldgenerator = new WorldGenDyeTree(true, false,this.index);
        }
        else if(r>1)
        {
        	worldgenerator = new WorldGenDyeTreeBig(true,this.index);
        }
        else
        {
        	worldgenerator = new WorldGenDyeTreeHuge(true,this.index);
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
            if (flag)
            {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else
            {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
	@Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }
	@Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }
	@Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }
}

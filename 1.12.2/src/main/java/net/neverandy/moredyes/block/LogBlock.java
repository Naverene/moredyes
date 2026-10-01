package net.neverandy.moredyes.block;

import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LogBlock extends BlockLog
{
	public String color;
	public LogBlock(String blockColor, BlockInfo info)
	{
		this.color = blockColor;
		setHardness(info.hardness);
		setHarvestLevel(info.harvestTool,info.harvestLevel);
		setSoundType(info.sound);
		setCreativeTab(info.tab);
		setResistance(info.resistance);
		setRegistryName(info.blockName+"_"+this.color);
		setUnlocalizedName(info.blockName+"."+this.color);
		ItemBlock itemBlock = new ItemBlock(this);
		this.setDefaultState(getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
		//initModel();
	}
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0,new ModelResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
	}
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);

        switch (meta & 12)
        {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @SuppressWarnings("incomplete-switch")
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
        {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
    }

}

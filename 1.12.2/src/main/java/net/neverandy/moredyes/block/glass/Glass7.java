package net.neverandy.moredyes.block.glass;

import net.neverandy.moredyes.block.GlassBlock;
import net.neverandy.moredyes.block.enums.EnumSet7;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class Glass7 extends GlassBlock
{
	public static final PropertyEnum TYPE = PropertyEnum.create("color", EnumSet7.class);
	public Glass7(String[] blockColors, BlockInfo info, boolean translucent,int set)
	{
		super(blockColors, info, translucent, set);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumSet7.META_0));
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
	    return new BlockStateContainer(this, new IProperty[] { TYPE });
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		switch(meta)
		{
		case 0:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_0);
		case 1:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_1);
		case 2:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_2);
		case 3:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_3);
		case 4:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_4);
		case 5:
			return getDefaultState().withProperty(TYPE, EnumSet7.META_5);
		}
		return getDefaultState().withProperty(TYPE,EnumSet7.META_0);
	}
	@Override
	public int damageDropped(IBlockState state)
	{
	    return getMetaFromState(state);
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumSet7) state.getValue(TYPE)).getID();
	}
}

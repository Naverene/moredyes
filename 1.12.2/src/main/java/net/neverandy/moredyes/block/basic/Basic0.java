package net.neverandy.moredyes.block.basic;

import net.neverandy.moredyes.block.BasicBlock;
import net.neverandy.moredyes.block.enums.EnumSet0;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class Basic0 extends BasicBlock
{
	public static final PropertyEnum TYPE = PropertyEnum.create("color", EnumSet0.class);
	public Basic0(String[] blockColors, BlockInfo info, int set)
	{
		super(blockColors, info, set);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumSet0.META_0));
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
			return getDefaultState().withProperty(TYPE, EnumSet0.META_0);
		case 1:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_1);
		case 2:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_2);
		case 3:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_3);
		case 4:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_4);
		case 5:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_5);
		case 6:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_6);
		case 7:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_7);
		case 8:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_8);
		case 9:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_9);
		case 10:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_10);
		case 11:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_11);
		case 12:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_12);
		case 13:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_13);
		case 14:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_14);
		case 15:
			return getDefaultState().withProperty(TYPE, EnumSet0.META_15);
		}
		return getDefaultState().withProperty(TYPE,EnumSet0.META_0);
	}
	@Override
	public int damageDropped(IBlockState state)
	{
	    return getMetaFromState(state);
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumSet0) state.getValue(TYPE)).getID();
	}
}

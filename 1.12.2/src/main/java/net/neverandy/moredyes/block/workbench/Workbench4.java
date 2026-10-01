package net.neverandy.moredyes.block.workbench;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.neverandy.moredyes.block.GlassBlock;
import net.neverandy.moredyes.block.WorkbenchBlock;
import net.neverandy.moredyes.block.enums.EnumSet0;
import net.neverandy.moredyes.block.enums.EnumSet4;
import net.neverandy.moredyes.utility.BlockInfo;

public class Workbench4 extends WorkbenchBlock
{
	public static final PropertyEnum TYPE = PropertyEnum.create("color", EnumSet4.class);
	public Workbench4(String[] blockColors, BlockInfo info, int set)
	{
		super(blockColors, info, set);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumSet4.META_0));
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
			return getDefaultState().withProperty(TYPE, EnumSet4.META_0);
		case 1:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_1);
		case 2:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_2);
		case 3:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_3);
		case 4:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_4);
		case 5:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_5);
		case 6:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_6);
		case 7:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_7);
		case 8:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_8);
		case 9:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_9);
		case 10:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_10);
		case 11:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_11);
		case 12:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_12);
		case 13:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_13);
		case 14:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_14);
		case 15:
			return getDefaultState().withProperty(TYPE, EnumSet4.META_15);
		}
		return getDefaultState().withProperty(TYPE,EnumSet4.META_0);
	}
	@Override
	public int damageDropped(IBlockState state)
	{
	    return getMetaFromState(state);
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumSet4) state.getValue(TYPE)).getID();
	}
}

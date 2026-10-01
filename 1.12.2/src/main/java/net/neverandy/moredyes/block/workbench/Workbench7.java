package net.neverandy.moredyes.block.workbench;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.neverandy.moredyes.block.WorkbenchBlock;
import net.neverandy.moredyes.block.enums.EnumSet0;
import net.neverandy.moredyes.block.enums.EnumSet7;
import net.neverandy.moredyes.utility.BlockInfo;

public class Workbench7 extends WorkbenchBlock
{
	public static final PropertyEnum TYPE = PropertyEnum.create("color", EnumSet7.class);
	public Workbench7(String[] blockColors, BlockInfo info, int set)
	{
		super(blockColors, info, set);
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

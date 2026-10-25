package net.neverandy.moredyes.block;

import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;

/**
 * Dyed beds in every color: the vanilla bed, whose bedding takes the dye color. One block for all colors, placed by
 * item/ItemDyedBed; both halves keep the color in a TileEntityDyedBed, and client/DyedBedRenderer draws them.
 */
public class BlockDyedBed extends BlockBed
{
	public BlockDyedBed()
	{
		this.setSoundType(SoundType.WOOD);
		this.setHardness(0.2f);
		this.disableStats();
		this.setRegistryName(Reference.MOD_ID,"bed");
		this.setUnlocalizedName(Reference.MOD_ID+".bed");
	}
	@Override
	public TileEntity createNewTileEntity(World world,int meta)
	{
		return new TileEntityDyedBed();
	}
	/** Players sleep in it and respawn at it; Forge only counts the vanilla bed by default. */
	@Override
	public boolean isBed(IBlockState state,IBlockAccess world,BlockPos pos,Entity player)
	{
		return true;
	}
	/** The bed item in the color of the bed at this position; the head half is asked for the foot. */
	private static ItemStack stack(IBlockAccess world,BlockPos pos,IBlockState state)
	{
		BlockPos head=state.getValue(PART)==EnumPartType.FOOT?pos.offset(state.getValue(FACING)):pos;
		TileEntity tile=world.getTileEntity(head);
		return tile instanceof TileEntityDyedBed?((TileEntityDyedBed)tile).getItemStack():new ItemStack(MDItem.bed);
	}
	@Override
	public void dropBlockAsItemWithChance(World world,BlockPos pos,IBlockState state,float chance,int fortune)
	{
		if(state.getValue(PART)==EnumPartType.HEAD)
		{
			spawnAsEntity(world,pos,stack(world,pos,state));
		}
	}
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		return stack(world,pos,state);
	}
}

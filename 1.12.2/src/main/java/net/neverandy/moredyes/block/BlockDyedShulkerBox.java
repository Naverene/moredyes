package net.neverandy.moredyes.block;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedShulkerBox;

/**
 * Dyed shulker boxes in every color: the vanilla box, purple underneath, whose shell takes the dye color. One block
 * for all colors; the color is kept in a TileEntityDyedShulkerBox and is the item damage, like the other blocks of
 * TileColor. client/DyedShulkerBoxRenderer draws it.
 */
public class BlockDyedShulkerBox extends BlockShulkerBox implements ITileColoredBlock
{
	public BlockDyedShulkerBox()
	{
		super(EnumDyeColor.PURPLE);
		this.setHardness(2.0f);
		this.setSoundType(SoundType.STONE);
		this.setRegistryName(Reference.MOD_ID,"shulker_box");
		this.setUnlocalizedName(Reference.MOD_ID+".shulker_box");
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	@Override
	public String getTypeName()
	{
		return "shulker_box";
	}
	@Override
	public TileEntity createNewTileEntity(World world,int meta)
	{
		return new TileEntityDyedShulkerBox();
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		TileColor.getSubBlocks(this,items);
	}
	/** The box item in its color, with its contents and name. */
	private static ItemStack stack(TileEntityDyedShulkerBox box)
	{
		ItemStack stack=new ItemStack(MDBlock.shulkerBox,1,box.getColorIndex());
		NBTTagCompound contents=box.saveToNbt(new NBTTagCompound());
		if(!contents.hasNoTags())
		{
			stack.setTagInfo("BlockEntityTag",contents);
		}
		return stack;
	}
	/**
	 * The vanilla box drops its item, with the contents, when it is broken, but without the color. This drops it with
	 * the color, and clears the box so the vanilla code drops nothing more.
	 */
	@Override
	public void breakBlock(World world,BlockPos pos,IBlockState state)
	{
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityDyedShulkerBox)
		{
			TileEntityDyedShulkerBox box=(TileEntityDyedShulkerBox)tile;
			if(!box.isCleared()&&box.shouldDrop())
			{
				ItemStack stack=stack(box);
				if(box.hasCustomName())
				{
					stack.setStackDisplayName(box.getName());
					box.setCustomName("");
				}
				spawnAsEntity(world,pos,stack);
			}
			box.clear();
		}
		super.breakBlock(world,pos,state);
	}
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		TileEntity tile=world.getTileEntity(pos);
		return tile instanceof TileEntityDyedShulkerBox?stack((TileEntityDyedShulkerBox)tile):new ItemStack(this);
	}
}

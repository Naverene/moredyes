package net.neverandy.moredyes.item;

import java.util.List;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MDItemDye extends Item
{
	String[] colors;
	public MDItemDye(String[] names, int count,int set)
	{
		this.colors=names;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
		this.setCreativeTab(MoreDyes.tabDyes);
		this.setUnlocalizedName("dye."+set);
		this.setRegistryName(set+"_dye");
		//initModel();
	}
	@SideOnly(Side.CLIENT)
    public void initModel()
	{
		for(int i=0;i<this.colors.length;i++)
		{
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(Reference.MOD_ID+":"+this.colors[i]+"_dye", "inventory"));
		}
    }
   public String getUnlocalizedName(ItemStack stack)
   {
	   return super.getUnlocalizedName() + "." + (this.colors[stack.getItemDamage()]);
   }
   public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
   {
	   return EnumActionResult.PASS;
   }
   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
   {
	   /*
       if (target instanceof EntitySheep)
       {
           EntitySheep entitysheep = (EntitySheep)target;
           EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());

           if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor)
           {
               entitysheep.setFleeceColor(enumdyecolor);
               --stack.stackSize;
           }

           return true;
       }
       else
       {
           return false;
       }*/
	   return false;
   }
   @SideOnly(Side.CLIENT)
   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
   {
       for (int i = 0; i < this.colors.length; ++i)
       {
           subItems.add(new ItemStack(itemIn, 1, i));
       }
   }
}

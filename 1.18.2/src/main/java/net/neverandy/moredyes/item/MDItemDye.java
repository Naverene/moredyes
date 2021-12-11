package net.neverandy.moredyes.item;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.setup.ModSetup;

import java.util.List;

public class MDItemDye extends Item
{
    String color;
    public MDItemDye(String name)
    {
        super(new Properties().tab(ModSetup.tabDyes));
        this.color=name;
        //this.setRegistryName(set+"_dye");
        //initModel();
    }
    public ActionResult onItemUse(ItemStack stack, PlayerEntity playerIn, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ)
    {
        return null;
    }
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
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
}
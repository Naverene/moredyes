package info.kg6jay.moredyes.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.entity.SheepColors;

/** The More Dyes dye: one item for every color, its damage being the color. Named "ECBF99 Dye" and so on. */
public class ItemMoreDye extends Item {

    public ItemMoreDye(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setItemName("moredyes.dye");
        this.setCreativeTab(MoreDyes.tabDyes);
        this.setTextureFile(Textures.SHEET);
        this.setIconIndex(Textures.DYE);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return Colors.hex(Colors.clamp(stack.getItemDamage())) + " Dye";
    }

    /** The grey dye texture is multiplied by the dye's color. */
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return Colors.rgb(Colors.clamp(stack.getItemDamage()));
    }

    /** Dyes a sheep this color, like vanilla dyes do with theirs. */
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityLiving entity) {
        if (!(entity instanceof EntitySheep)) {
            return false;
        }
        EntitySheep sheep = (EntitySheep) entity;
        int color = Colors.clamp(stack.getItemDamage());
        if (sheep.getSheared() || SheepColors.get(sheep) == color) {
            return false;
        }
        if (!sheep.worldObj.isRemote) {
            SheepColors.set(sheep, color);
        }
        --stack.stackSize;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List list) {
        for (int i = 0; i < Colors.COUNT; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }
}

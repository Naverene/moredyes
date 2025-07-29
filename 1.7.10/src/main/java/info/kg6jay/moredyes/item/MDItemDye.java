package info.kg6jay.moredyes.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.entity.passive.MDEntitySheep;
import info.kg6jay.moredyes.reference.Reference;

public class MDItemDye extends Item {

    public String[] dyeNames;
    @SideOnly(Side.CLIENT)
    private IIcon[] icon;

    public MDItemDye(String[] names, String dyeSet) {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        dyeNames = names;
        this.setCreativeTab(MoreDyes.tabDyes);
        this.setUnlocalizedName(dyeSet + "Dye");
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        int j = MathHelper.clamp_int(meta, 0, dyeNames.length - 1);
        return this.icon[j];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, dyeNames.length - 1);
        return super.getUnlocalizedName() + "_" + i;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void func_150918_a(World world, int x, int y, int z, int meta) {
        if (meta == 0) {
            meta = 15;
        }

        Block block = world.getBlock(x, y, z);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(world, x, y, z);

            for (int i1 = 0; i1 < meta; ++i1) {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                world.spawnParticle(
                    "happyVillager",
                    (double) ((float) x + itemRand.nextFloat()),
                    (double) y + (double) itemRand.nextFloat() * block.getBlockBoundsMaxY(),
                    (double) ((float) z + itemRand.nextFloat()),
                    d0,
                    d1,
                    d2);
            }
        } else {
            for (int i1 = 0; i1 < meta; ++i1) {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                world.spawnParticle(
                    "happyVillager",
                    (double) ((float) x + itemRand.nextFloat()),
                    (double) y + (double) itemRand.nextFloat() * 1.0f,
                    (double) ((float) z + itemRand.nextFloat()),
                    d0,
                    d1,
                    d2);
            }
        }
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    // TODO: Implement custom sheep then update this code to interact with them instead, as I can't adjust the fleece
    // options in vanilla sheep.
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntitySheep) {
            /*
             * EntitySheep entitysheep = (EntitySheep)entityLiving;
             * int i = BlockColored.func_150032_b(stack.getItemDamage());
             * if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != i)
             * {
             * entitysheep.setFleeceColor(i);
             * --stack.stackSize;
             * }
             */
            return false;// true;
        } else if (entityLiving instanceof MDEntitySheep) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < dyeNames.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = new IIcon[dyeNames.length];

        for (int i = 0; i < dyeNames.length; ++i) {
            this.icon[i] = iconRegister.registerIcon(Reference.MOD_ID + ":dye/dye_" + dyeNames[i]);
        }
    }
}

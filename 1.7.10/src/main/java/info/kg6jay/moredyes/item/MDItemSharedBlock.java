package info.kg6jay.moredyes.item;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The one item for every dyed block (see SharedBlocks). It hands everything to the block's own item ("old" item),
 * with a stack of that item standing in for this one, so each block places, names and draws itself the way it always
 * did.
 */
public class MDItemSharedBlock extends Item {

    /** Which shared item this is (0 for "dyedBlock"). */
    public final int index;

    public MDItemSharedBlock(int index) {
        this.index = index;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("moredyes.dyedBlock");
    }

    private static ItemStack old(ItemStack stack) {
        return SharedBlocks.toOld(stack);
    }

    /** Copies what the old item's code did to its stand-in back to the shared stack. */
    private static void sync(ItemStack shared, ItemStack old) {
        shared.stackSize = old.stackSize;
        shared.setTagCompound(old.getTagCompound());
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        ItemStack old = old(stack);
        if (old == null) {
            return false;
        }
        boolean used = old.getItem()
            .onItemUse(old, player, world, x, y, z, side, hitX, hitY, hitZ);
        sync(stack, old);
        return used;
    }

    /** Checked on the client before a placing click is sent (ItemBlock's placement check). */
    @SideOnly(Side.CLIENT)
    public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack) {
        ItemStack old = old(stack);
        return old != null && old.getItem() instanceof ItemBlock block
            && block.func_150936_a(world, x, y, z, side, player, old);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        ItemStack old = old(stack);
        return old == null ? super.getUnlocalizedName(stack)
            : old.getItem()
                .getUnlocalizedName(old);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ItemStack old = old(stack);
        return old == null ? super.getItemStackDisplayName(stack)
            : old.getItem()
                .getItemStackDisplayName(old);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        ItemStack old = old(stack);
        if (old != null) {
            old.getItem()
                .addInformation(old, player, list, advanced);
        }
    }

    // Creative tabs and NEI: every variant, in the tab of its block.

    @Override
    public CreativeTabs[] getCreativeTabs() {
        Set<CreativeTabs> tabs = new LinkedHashSet<>();
        for (ItemBlock old : SharedBlocks.oldItemsInOrder(this.index)) {
            CreativeTabs tab = Block.getBlockFromItem(old)
                .getCreativeTabToDisplayOn();
            if (tab != null) {
                tabs.add(tab);
            }
        }
        return tabs.toArray(new CreativeTabs[0]);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        CreativeTabs[] tabs = this.getCreativeTabs();
        return tabs.length > 0 ? tabs[0] : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (ItemBlock old : SharedBlocks.oldItemsInOrder(this.index)) {
            Block block = Block.getBlockFromItem(old);
            if (tab != null && block.getCreativeTabToDisplayOn() != tab) {
                continue;
            }
            List<ItemStack> variants = new ArrayList<>();
            block.getSubBlocks(old, block.getCreativeTabToDisplayOn(), variants);
            for (ItemStack variant : variants) {
                ItemStack shared = SharedBlocks.toShared(variant);
                if (shared != null && shared.getItem() == this) {
                    list.add(shared);
                }
            }
        }
    }

    // Icons and colors come from the old item. The icons are all on the blocks texture sheet.

    @Override
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        ItemStack old = old(stack);
        return old == null ? null
            : old.getItem()
                .getIconIndex(old);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        ItemStack old = old(stack);
        return old == null ? null
            : old.getItem()
                .getIcon(old, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        ItemStack old = old(new ItemStack(this, 1, damage));
        return old == null ? null
            : old.getItem()
                .getIconFromDamage(old.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        ItemStack old = old(new ItemStack(this, 1, damage));
        return old == null ? null
            : old.getItem()
                .getIconFromDamageForRenderPass(old.getItemDamage(), pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int damage) {
        ItemStack old = old(new ItemStack(this, 1, damage));
        if (old == null) {
            return 1;
        }
        Item item = old.getItem();
        return item.requiresMultipleRenderPasses() ? item.getRenderPasses(old.getItemDamage()) : 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        ItemStack old = old(stack);
        return old == null ? 0xFFFFFF
            : old.getItem()
                .getColorFromItemStack(old, pass);
    }
}

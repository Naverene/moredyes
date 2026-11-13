package info.kg6jay.moredyes.item;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * The one item for every dyed block (see SharedBlocks). Everything is handed to the block's own item, with a stack of
 * that item standing in for this one, so each block places, names and draws itself the way it always did.
 */
public class ItemSharedBlock extends Item {

    public ItemSharedBlock(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setItemName("moredyes.dyedBlock");
    }

    private static Item oldItem(ItemStack stack) {
        Block block = SharedBlocks.sharedBlock(stack);
        return block == null ? null : Item.itemsList[block.blockID];
    }

    private ItemStack old(int damage) {
        return SharedBlocks.toOld(new ItemStack(this, 1, damage));
    }

    /**
     * Places the block: the stack is turned into the old item for the call (so the block's code sees its own item,
     * also through the player's held item) and back afterwards.
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        Item old = oldItem(stack);
        if (old == null) {
            return false;
        }
        int damage = stack.getItemDamage();
        int color = SharedBlocks.sharedColor(stack);
        stack.itemID = old.itemID;
        stack.setItemDamage(color);
        try {
            return old.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        } finally {
            if (stack.itemID == old.itemID) {
                stack.itemID = this.itemID;
                stack.setItemDamage(damage);
            }
        }
    }

    @Override
    public String getItemNameIS(ItemStack stack) {
        ItemStack old = SharedBlocks.toOld(stack);
        return old == null ? super.getItemNameIS(stack) : old.getItem().getItemNameIS(old);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        ItemStack old = SharedBlocks.toOld(stack);
        return old == null ? super.getItemDisplayName(stack) : old.getItem().getItemDisplayName(old);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("rawtypes")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        ItemStack old = SharedBlocks.toOld(stack);
        if (old != null) {
            old.getItem().addInformation(old, player, list, advanced);
        }
    }

    // Creative menu: every color of every kind, in the tab of its block.

    @Override
    public CreativeTabs[] getCreativeTabs() {
        Set<CreativeTabs> tabs = new LinkedHashSet<CreativeTabs>();
        for (Block block : SharedBlocks.blocks()) {
            if (block != null && block.getCreativeTabToDisplayOn() != null) {
                tabs.add(block.getCreativeTabToDisplayOn());
            }
        }
        return tabs.toArray(new CreativeTabs[tabs.size()]);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        CreativeTabs[] tabs = this.getCreativeTabs();
        return tabs.length > 0 ? tabs[0] : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubItems(int id, CreativeTabs tab, List list) {
        Block[] blocks = SharedBlocks.blocks();
        for (Block block : blocks) {
            if (block == null || tab != null && block.getCreativeTabToDisplayOn() != tab) {
                continue;
            }
            for (int color = 0; color < Colors.COUNT; color++) {
                list.add(SharedBlocks.stack(block, 1, color));
            }
        }
    }

    // Icons and colors come from the old item. Every dyed block is on the mod's texture sheet.

    @Override
    public String getTextureFile() {
        return Textures.SHEET;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int damage) {
        ItemStack old = this.old(damage);
        return old == null ? 0 : old.getItem().getIconFromDamage(old.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconIndex(ItemStack stack, int pass) {
        ItemStack old = SharedBlocks.toOld(stack);
        return old == null ? 0 : old.getItem().getIconIndex(old, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconFromDamageForRenderPass(int damage, int pass) {
        ItemStack old = this.old(damage);
        return old == null ? 0 : old.getItem().getIconFromDamageForRenderPass(old.getItemDamage(), pass);
    }

    /** Always true; getRenderPasses gives one pass for the blocks drawn in a single pass. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int damage) {
        ItemStack old = this.old(damage);
        if (old == null) {
            return 1;
        }
        Item item = old.getItem();
        return item.requiresMultipleRenderPasses() ? item.getRenderPasses(old.getItemDamage()) : 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        ItemStack old = SharedBlocks.toOld(stack);
        return old == null ? 0xFFFFFF : old.getItem().getColorFromItemStack(old, pass);
    }
}

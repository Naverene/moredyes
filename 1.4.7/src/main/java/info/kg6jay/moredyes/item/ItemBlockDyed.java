package info.kg6jay.moredyes.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.block.BlockDyedChest;
import info.kg6jay.moredyes.block.Dyed;
import info.kg6jay.moredyes.block.IDyedBlock;
import info.kg6jay.moredyes.block.ILayeredBlock;

/**
 * The item of a dyed block. Its damage is the color; placing it puts the color in the block's tile entity. Named
 * after the color's hex code, like "ECBF99 Wool".
 */
public class ItemBlockDyed extends ItemBlock {

    public ItemBlockDyed(int id) {
        super(id);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    protected Block block() {
        return Block.blocksList[this.getBlockID()];
    }

    /** The color is not metadata: blocks start at 0 and onBlockPlaced sets their facing or shape. */
    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ, int metadata) {
        Block block = this.block();
        int color = Colors.clamp(stack.getItemDamage());
        if (block instanceof BlockDyedChest && !((BlockDyedChest) block).canPlaceChestAt(world, x, y, z, color)) {
            return false;
        }
        if (!world.setBlockAndMetadataWithNotify(x, y, z, block.blockID, metadata)) {
            return false;
        }
        if (world.getBlockId(x, y, z) == block.blockID) {
            Dyed.set(world, x, y, z, color);
            block.onBlockPlacedBy(world, x, y, z, player);
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }
        return true;
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        Block block = this.block();
        String name = block instanceof IDyedBlock ? ((IDyedBlock) block).getDyedName() : "";
        return Colors.hex(Colors.clamp(stack.getItemDamage())) + " " + name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        Block block = this.block();
        int color = Colors.clamp(stack.getItemDamage());
        if (block instanceof ILayeredBlock) {
            return ((ILayeredBlock) block).getLayerColor(color, pass);
        }
        return block.getRenderColor(color);
    }

    /** Flowers and saplings are drawn as two flat layers: the tinted petals or leaves, and the natural stem. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        Block block = this.block();
        return block instanceof ILayeredBlock && ((ILayeredBlock) block).isPlant();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconFromDamageForRenderPass(int damage, int pass) {
        Block block = this.block();
        if (block instanceof ILayeredBlock) {
            return ((ILayeredBlock) block).getLayerTexture(2, pass);
        }
        return super.getIconFromDamageForRenderPass(damage, pass);
    }
}

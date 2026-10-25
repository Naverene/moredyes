package info.kg6jay.moredyes.compat.etfuturum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import ganymedes01.etfuturum.blocks.itemblocks.ItemBlockShulkerBox;
import ganymedes01.etfuturum.tileentities.TileEntityShulkerBox;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed shulker box. The item damage is the color's number (see ColorIndex); the tag is Et Futurum's (contents, tier
 * as "Type"), so the box keeps its contents and tooltip like an Et Futurum box and cannot be put into another box.
 */
public class DyedShulkerBoxItem extends ItemBlockShulkerBox {

    public DyedShulkerBoxItem(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        // Et Futurum's dispenser behaviour places its own box, without the color.
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new DyedShulkerBoxDispense());
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    /** The tier's number, as Et Futurum keeps it in the item tag (0 is the plain box). */
    public static int tier(ItemStack stack) {
        int type = stack.hasTagCompound() ? stack.getTagCompound()
            .getByte("Type") : 0;
        return type > 0 && type <= TileEntityShulkerBox.tiers.length ? type : 0;
    }

    /** "tile.moredyes.dyedShulkerBox", or "tile.moredyes.dyedShulkerBox.iron" for a tier. */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int tier = tier(stack);
        return this.field_150939_a.getUnlocalizedName() + (tier > 0 ? "." + TileEntityShulkerBox.tiers[tier - 1] : "");
    }

    /** "ECBF99 Shulker Box", or "ECBF99 Iron Shulker Box" for a tier. */
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector
            .translateToLocalFormatted(this.getUnlocalizedName(stack) + ".name", ColorIndex.hex(stack.getItemDamage()))
            .trim();
    }
}

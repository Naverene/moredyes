package info.kg6jay.moredyes.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.MDBlockDyedTallFlower;

/**
 * Item for the tall dyed flowers. Like the vanilla ones it shows the top of the flower, drawn in two layers: the
 * petals in the color and the stem untinted.
 */
public class MDItemBlockDyedTallFlower extends MDItemBlockTileColored {

    public MDItemBlockDyedTallFlower(Block block) {
        super(block);
    }

    private MDBlockDyedTallFlower flower() {
        return (MDBlockDyedTallFlower) this.field_150939_a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.flower()
            .getLayerIcon(2, MDBlockDyedTallFlower.TOP, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return this.flower()
            .getLayerIcon(2, MDBlockDyedTallFlower.TOP, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return this.flower()
            .getLayerColor(stack.getItemDamage(), pass);
    }
}

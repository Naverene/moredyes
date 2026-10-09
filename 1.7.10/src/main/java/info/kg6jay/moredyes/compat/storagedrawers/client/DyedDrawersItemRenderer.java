package info.kg6jay.moredyes.compat.storagedrawers.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import com.jaquadro.minecraft.storagedrawers.client.renderer.DrawersItemRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Draws dyed drawer items: Storage Drawers' own drawer item renderer, with a box renderer that multiplies the drawer by
 * the dye color, which is the item damage (see TintedBoxRenderer).
 */
@SideOnly(Side.CLIENT)
public class DyedDrawersItemRenderer implements IItemRenderer {

    private final DrawersItemRenderer drawers = new DrawersItemRenderer();
    private final TintedBoxRenderer box = TintedBoxRenderer.install(this.drawers, DrawersItemRenderer.class);

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return this.drawers.handleRenderType(item, type);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return this.drawers.shouldUseRenderHelper(type, item, helper);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (this.box != null) {
            this.box.setTint(DyedDrawersRenderer.color(item.getItemDamage()));
        }
        this.drawers.renderItem(type, item, data);
    }
}

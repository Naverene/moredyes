package info.kg6jay.moredyes.compat.etfuturum.client;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.tileentities.TileEntityShulkerBox;
import info.kg6jay.moredyes.compat.etfuturum.DyedShulkerBoxItem;
import info.kg6jay.moredyes.compat.etfuturum.DyedShulkerBoxTile;

/** Draws a dyed shulker box item as the box itself, in its color and tier, as Et Futurum does for its boxes. */
@SideOnly(Side.CLIENT)
public class DyedShulkerBoxItemRenderer implements IItemRenderer {

    private final DyedShulkerBoxTile box = new DyedShulkerBoxTile();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        this.box.setDye(item.getItemDamage());
        this.box.type = TileEntityShulkerBox.ShulkerBoxType.VALUES[DyedShulkerBoxItem.tier(item)];
        GL11.glPushMatrix();
        if (type == ItemRenderType.ENTITY || type == ItemRenderType.INVENTORY) {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        }
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.box, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }
}

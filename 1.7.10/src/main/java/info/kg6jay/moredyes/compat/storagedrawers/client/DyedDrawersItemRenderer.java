package info.kg6jay.moredyes.compat.storagedrawers.client;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.client.renderer.ModularBoxRenderer;
import com.jaquadro.minecraft.storagedrawers.util.RenderHelper;
import com.jaquadro.minecraft.storagedrawers.util.RenderHelperState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Draws dyed drawer items: the same as Storage Drawers' drawer item renderer (DrawersItemRenderer), with the drawer
 * multiplied by the dye color, which is the item damage.
 */
@SideOnly(Side.CLIENT)
public class DyedDrawersItemRenderer implements IItemRenderer {

    /** The face Storage Drawers draws the front of an item on. */
    private static final int FRONT = 4;

    private final ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (!(Block.getBlockFromItem(item.getItem()) instanceof BlockDrawers block)) {
            return;
        }
        if (type == ItemRenderType.INVENTORY) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        }
        if (type == ItemRenderType.ENTITY) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
        boolean centred = type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY;
        if (centred) {
            GL11.glTranslatef(block.halfDepth ? -0.75F : -0.5F, -0.5F, -0.5F);
        }

        float[] color = DyedDrawersRenderer.color(item.getItemDamage());
        RenderHelper.instance.state.setUVRotation(1, RenderHelperState.ROTATION_BY_FACE_FACE[3][FRONT]);
        this.boxRenderer.setUnit(block.getTrimWidth());
        this.boxRenderer.setColor(color);
        for (int i = 0; i < 6; ++i) {
            this.boxRenderer.setIcon(block.getIcon(i, 0), i);
        }
        double depth = block.halfDepth ? 0.5 : 1.0;
        this.boxRenderer.renderExterior(
            null,
            block,
            0,
            0,
            0,
            1.0 - depth,
            0.0,
            0.0,
            1.0,
            1.0,
            1.0,
            0,
            ModularBoxRenderer.sideCut[FRONT]);
        RenderHelper.instance.state.clearUVRotation(1);

        double unit = block.getTrimDepth();
        this.boxRenderer.setUnit(0.0);
        this.boxRenderer.setInteriorIcon(block.getIcon(FRONT, 0), ForgeDirection.OPPOSITES[FRONT]);
        this.boxRenderer.renderInterior(
            null,
            block,
            0,
            0,
            0,
            1.0 - depth,
            unit,
            unit,
            1.0 - depth + unit,
            1.0 - unit,
            1.0 - unit,
            0,
            ModularBoxRenderer.sideCut[FRONT]);

        // A taped drawer, as Storage Drawers shows it.
        if (item.hasTagCompound() && item.getTagCompound()
            .hasKey("tile")) {
            RenderHelper.instance.setRenderBounds(1.0 - depth - 0.005, 0.0, 0.0, 1.0, 1.0, 1.0);
            RenderHelper.instance.renderFace(FRONT, null, block, block.getTapeIcon(), 1.0F, 1.0F, 1.0F);
        }
        if (centred) {
            GL11.glTranslatef(block.halfDepth ? 0.75F : 0.5F, 0.5F, 0.5F);
        }
    }
}

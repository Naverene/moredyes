package info.kg6jay.moredyes.render;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.reference.Reference;

public class TileEntityMDBlockColoredChestRenderer extends TileEntitySpecialRenderer {

    private final ModelChest modelChest = new ModelChest();
    private final ModelChest modelDoubleChest = new ModelChest(); // For simplicity, using the same model. Replace if
                                                                  // you have a double chest model.

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityMDBlockColoredChest)) return;
        TileEntityMDBlockColoredChest chest = (TileEntityMDBlockColoredChest) tile;

        // Determine color from block metadata (or tile entity if you store it there)
        int meta = chest.getBlockMetadata();
        String color = getColorFromMeta(meta); // Implement this method to map meta to color name

        // Determine if this is a double chest (adjacent chest check)
        boolean isDouble = isDoubleChest(chest); // Implement this method if needed

        // Build texture path
        String type = isDouble ? "double" : "normal";
        String texturePath = String.format("textures/blocks/chest/%s/%s.png", type, color);
        ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, texturePath);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        this.bindTexture(texture);
        if (isDouble) {
            modelDoubleChest.renderAll();
        } else {
            modelChest.renderAll();
        }

        GL11.glPopMatrix();
    }

    private String getColorFromMeta(int meta) {
        // TODO: Map metadata to color string (e.g., 0 -> "white", 1 -> "orange", ...)
        // This should match your color array in MDBlockColoredChest
        // Example:
        String[] colors = { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "lightGray",
            "cyan", "purple", "blue", "brown", "green", "red", "black" };
        if (meta >= 0 && meta < colors.length) {
            return colors[meta];
        }
        return "white";
    }

    private boolean isDoubleChest(TileEntityMDBlockColoredChest chest) {
        // TODO: Implement adjacency check for double chests if needed
        // For now, always return false (single chest)
        return false;
    }
}

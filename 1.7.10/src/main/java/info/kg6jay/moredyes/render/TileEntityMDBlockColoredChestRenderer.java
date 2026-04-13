package info.kg6jay.moredyes.render;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.reference.ColorStrings;

public class TileEntityMDBlockColoredChestRenderer extends TileEntitySpecialRenderer {

    private final ModelChest modelChest = new ModelChest();
    private final ModelChest modelDoubleChest = new ModelChest(); // For simplicity, using the same model.

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityMDBlockColoredChest)) return;
        TileEntityMDBlockColoredChest chest = (TileEntityMDBlockColoredChest) tile;

        // Determine color from block metadata (or tile entity if you store it there)
        int meta = chest.getBlockMetadata();
        String colorHex = getHexFromMeta(meta); // Map meta to the hex texture filename that exists

        // Determine if this is a double chest (adjacent chest check)
        boolean isDouble = isDoubleChest(chest); // Implement this method if needed

        // Build texture path
        String type = isDouble ? "double" : "normal";
        ResourceLocation texture = null;
        // 1) Try to get the block's registered icon name and bind the matching file under textures/blocks
        try {
            if (chest.getWorldObj() != null) {
                net.minecraft.block.Block block = chest.getBlockType();
                if (block != null) {
                    net.minecraft.util.IIcon icon = block.getIcon(0, chest.getBlockMetadata());
                    if (icon != null) {
                        String iconName = icon.getIconName(); // e.g. "moredyes:chest/normal/a5bf86"
                        String ns = Reference.MOD_ID;
                        String path = iconName;
                        if (iconName.contains(":")) {
                            String[] parts = iconName.split(":", 2);
                            ns = parts[0];
                            path = parts[1];
                        }
                        ResourceLocation rl = new ResourceLocation(ns, "textures/blocks/" + path + ".png");
                        try {
                            this.bindTexture(rl);
                            texture = rl;
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 2) Fallback: explicit chest file path under textures/blocks/chest/<type>/<hex>.png
        if (texture == null) {
            ResourceLocation cand = new ResourceLocation(Reference.MOD_ID,
                "textures/blocks/chest/" + type + "/" + colorHex + ".png");
            try {
                this.bindTexture(cand);
                texture = cand;
            } catch (Exception ignored) {}
        }

        // 3) Final fallback: default white texture
        if (texture == null) {
            ResourceLocation fallback = new ResourceLocation(Reference.MOD_ID, "textures/blocks/chest/normal/ecbf99.png");
            try { this.bindTexture(fallback); } catch (Exception ignored) {}
        }

        GL11.glPushMatrix();
        // Vanilla chest TESR sequence: enable rescale normal, translate to center of block, scale, rotate, translate down, render
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0F, (float) z + 0.5F);
        // Determine facing like vanilla (consider adjacent chests for correct rotation)
        int short1 = 0;
        try {
            if (chest.getWorldObj() != null) {
                short1 = chest.getBlockMetadata();
                if (chest.adjacentChestZNeg != null) short1 = 2;
                if (chest.adjacentChestXNeg != null) short1 = 5;
            }
        } catch (Throwable ignored) { short1 = meta; }
        int angle = 0;
        if (short1 == 2) angle = 180;
        if (short1 == 3) angle = 0;
        if (short1 == 4) angle = 90;
        if (short1 == 5) angle = -90;
        GL11.glRotatef((float) angle, 0.0F, 1.0F, 0.0F);
        // Translate down to model origin, then apply negative Y/Z scale (vanilla order)
        GL11.glTranslatef(0.0F, -1.0F, 0.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        if (isDouble) {
            modelDoubleChest.renderAll();
        } else {
            modelChest.renderAll();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private String getHexFromMeta(int meta) {
        // Map the 0..14 metadata to the first hex in each ColorStrings array.
        // This matches the texture files present under textures/blocks/chest/normal/<hex>.png
        switch (meta) {
            case 1:
                return ColorStrings.ORANGE[0];
            case 2:
                return ColorStrings.MAGENTA[0];
            case 3:
                return ColorStrings.LBLUE[0];
            case 4:
                return ColorStrings.YELLOW[0];
            case 5:
                return ColorStrings.LIME[0];
            case 6:
                return ColorStrings.PINK[0];
            case 7:
                return ColorStrings.DGRAY[0];
            case 8:
                return ColorStrings.LGRAY[0];
            case 9:
                return ColorStrings.CYAN[0];
            case 10:
                return ColorStrings.PURPLE[0];
            case 11:
                return ColorStrings.BLUE[0];
            case 12:
                return ColorStrings.BROWN[0];
            case 13:
                return ColorStrings.GREEN[0];
            case 14:
                return ColorStrings.RED[0];
            default:
                return ColorStrings.WHITE[0];
        }
    }

    private boolean isDoubleChest(TileEntityMDBlockColoredChest chest) {
        // TODO: Implement adjacency check for double chests if needed
        // For now, always return false (single chest)
        return false;
    }
}

package info.kg6jay.moredyes.compat.ironchest.client;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.compat.ironchest.DyedIronChestBlock;
import info.kg6jay.moredyes.compat.ironchest.DyedIronChestTile;
import info.kg6jay.moredyes.compat.ironchest.IronChestCompat;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Draws a dyed Iron Chests chest with the chest model Iron Chests uses, twice: once with the grey panels of its tier's
 * texture multiplied by the dye color, and once with the frame and latch in their own colors. Both textures are made
 * from Iron Chests' texture when the game loads (TintSources.metalChest), so none of its art is copied into More Dyes.
 * Also draws the chest in the inventory.
 */
@SideOnly(Side.CLIENT)
public class DyedIronChestRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private final ModelChest model = new ModelChest();
    private final Map<IronChestCompat.Tier, ResourceLocation> panels = new EnumMap<>(IronChestCompat.Tier.class);
    private final Map<IronChestCompat.Tier, ResourceLocation> trims = new EnumMap<>(IronChestCompat.Tier.class);

    public DyedIronChestRenderer() {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            this.panels.put(tier, TintedTextures.modelTexture("ironchest/" + tier.id() + "_panels"));
            this.trims.put(tier, TintedTextures.modelTexture("ironchest/" + tier.id() + "_trim"));
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof DyedIronChestTile chest)) {
            return;
        }
        float lid = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
        this.render(chest.getTier(), chest.getColor(), chest.getFacing(), lid, x, y, z);
    }

    /** Same transforms as Iron Chests' renderer. */
    private void render(IronChestCompat.Tier tier, int color, int facing, float lid, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        // Each pass leaves the other's pixels clear, which must not be drawn.
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int angle = switch (facing) {
            case 2 -> 180;
            case 4 -> 90;
            case 5 -> -90;
            default -> 0;
        };
        GL11.glRotatef((float) angle, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;
        this.model.chestLid.rotateAngleX = -(lid * (float) Math.PI / 2.0F);
        this.model.chestKnob.rotateAngleX = this.model.chestLid.rotateAngleX;

        int rgb = ColorIndex.rgb(color);
        this.bind(this.panels.get(tier));
        GL11.glColor4f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F, 1.0F);
        this.model.chestLid.render(0.0625F);
        this.model.chestBelow.render(0.0625F);
        this.bind(this.trims.get(tier));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.model.renderAll();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void bind(ResourceLocation texture) {
        // The tile entity dispatcher is not set up while drawing an item, so bind through the texture manager.
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(texture);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof DyedIronChestBlock chest)) {
            return;
        }
        // Matches what RenderBlocks does before drawing a vanilla chest item.
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        this.render(chest.getTier(), metadata, 3, 0.0F, 0.0D, 0.0D, 0.0D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        // Nothing to draw in the chunk mesh; the tile entity renderer draws the chest.
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderIds.dyedIronChest;
    }
}

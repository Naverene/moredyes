package info.kg6jay.moredyes.compat.etfuturum.client;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.SignedBytes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.client.model.ModelShulker;
import info.kg6jay.moredyes.client.TintSources;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.compat.etfuturum.DyedShulkerBoxTile;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Draws a dyed shulker box like Et Futurum Requiem's own renderer does, but tinted: the shell from a grey texture in
 * the box's color, then for a tier the metal trim over it as it is. A crystal box also shows its top items.
 */
@SideOnly(Side.CLIENT)
public class DyedShulkerBoxRenderer extends TileEntitySpecialRenderer {

    /** Where a crystal box shows the items it holds, as in Et Futurum. */
    private static final float[][] SHIFTS = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F },
        { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F }, { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F },
        { 0.5F, 0.32F, 0.5F } };

    private final ModelShulker model = new ModelShulker();
    private final ResourceLocation[] shells = new ResourceLocation[TintSources.SHULKER_TIERS.length];
    private final ResourceLocation[] trims = new ResourceLocation[TintSources.SHULKER_TIERS.length];
    private final RenderItem itemRenderer;

    public DyedShulkerBoxRenderer() {
        for (int i = 0; i < TintSources.SHULKER_TIERS.length; i++) {
            String tier = TintSources.SHULKER_TIERS[i];
            this.shells[i] = TintedTextures.modelTexture("shulker/" + tier);
            this.trims[i] = i == 0 ? null : TintedTextures.modelTexture("shulker/" + tier + "_trim");
        }
        this.itemRenderer = new RenderItem() {

            @Override
            public byte getMiniBlockCount(ItemStack stack, byte original) {
                return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
            }

            @Override
            public byte getMiniItemCount(ItemStack stack, byte original) {
                return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 7) + 1);
            }

            @Override
            public boolean shouldBob() {
                return false;
            }

            @Override
            public boolean shouldSpreadItems() {
                return false;
            }
        };
        this.itemRenderer.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if (te instanceof DyedShulkerBoxTile box) {
            this.render(box, x, y, z, partialTicks);
        }
    }

    private void render(DyedShulkerBoxTile box, double x, double y, double z, float partialTicks) {
        int tier = Math.min(box.type.ordinal(), this.shells.length - 1);
        ForgeDirection facing = box.hasWorldObj() ? ForgeDirection.VALID_DIRECTIONS[box.facing % 6] : ForgeDirection.UP;
        float progress = box.getProgress(partialTicks);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        int rgb = ColorIndex.rgb(box.getDye());
        this.bindTexture(this.shells[tier]);
        GL11.glColor4f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F, 1.0F);
        this.renderModel(facing, progress, x, y, z);
        if (this.trims[tier] != null) {
            this.bindTexture(this.trims[tier]);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderModel(facing, progress, x, y, z);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (box.hasWorldObj() && box.type.getIsClear()
            && box.getDistanceFrom(
                this.field_147501_a.field_147560_j,
                this.field_147501_a.field_147561_k,
                this.field_147501_a.field_147558_l) < 128.0D) {
            this.renderTopItems(box, x, y, z);
        }
        GL11.glPopAttrib();
    }

    /** Et Futurum's model, turned to the side the box faces, with the lid lifted and turned as it opens. */
    private void renderModel(ForgeDirection facing, float progress, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.9995F, 0.9995F, 0.9995F);
        GL11.glTranslatef(0.0F, -1.0F, 0.0F);
        switch (facing) {
            case DOWN -> {
                GL11.glTranslatef(0.0F, 2.0F, 0.0F);
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            }
            case NORTH -> {
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
            case SOUTH -> {
                GL11.glTranslatef(0.0F, 1.0F, -1.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }
            case WEST -> {
                GL11.glTranslatef(-1.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            }
            case EAST -> {
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            }
            default -> {}
        }
        this.model.base.render(0.0625F);
        GL11.glTranslatef(0.0F, -progress * 0.5F, 0.0F);
        GL11.glRotatef(270.0F * progress, 0.0F, 1.0F, 0.0F);
        this.model.lid.render(0.0625F);
        GL11.glPopMatrix();
    }

    private void renderTopItems(DyedShulkerBoxTile box, double x, double y, double z) {
        ItemStack[] stacks = box.getTopItemStacks();
        int shift = 0;
        float scale = 0.7F;
        float angle = (float) (360.0D * (System.currentTimeMillis() & 16383L) / 16383.0D);
        if (stacks[1] == null) {
            shift = 8;
            scale = 0.85F;
        }
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        EntityItem item = new EntityItem(this.field_147501_a.field_147550_f);
        item.hoverStart = 0.0F;
        for (ItemStack stack : stacks) {
            if (shift >= SHIFTS.length) {
                break;
            }
            if (stack == null) {
                shift++;
                continue;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(SHIFTS[shift][0], SHIFTS[shift][1], SHIFTS[shift][2]);
            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(scale, scale, scale);
            item.setEntityItemStack(stack);
            this.itemRenderer.doRender(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glPopMatrix();
            shift++;
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

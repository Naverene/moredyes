package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BedTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws every bed, vanilla and dyed. It replaces vanilla's renderer for the bed tile entity, which dyed beds share:
 * a dyed bed is drawn from the grey bedding texture {@code textures/entity/bed/dyed.png} tinted with its color, and
 * the wooden frame ({@code dyed_frame.png}) over it as it is. Vanilla's renderer keeps its models to itself, so the
 * models and drawing are copied from it.
 */
public class DyedBedRenderer extends BedTileEntityRenderer
{
    static final RenderMaterial BEDDING = new RenderMaterial(Atlases.BED_ATLAS, new ResourceLocation(Reference.MOD_ID, "entity/bed/dyed"));
    static final RenderMaterial FRAME = new RenderMaterial(Atlases.BED_ATLAS, new ResourceLocation(Reference.MOD_ID, "entity/bed/dyed_frame"));

    private final ModelRenderer head;
    private final ModelRenderer foot;
    private final ModelRenderer[] legs = new ModelRenderer[4];

    public DyedBedRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
        head = new ModelRenderer(64, 64, 0, 0);
        head.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
        foot = new ModelRenderer(64, 64, 0, 22);
        foot.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
        for (int i = 0; i < 4; i++)
        {
            legs[i] = new ModelRenderer(64, 64, 50, i * 6);
            legs[i].addBox(i < 2 ? 0.0F : -16.0F, 6.0F, i % 2 == 0 ? -16.0F : 0.0F, 3.0F, 3.0F, 3.0F);
            legs[i].rotateAngleX = (float) (Math.PI / 2);
        }
        legs[1].rotateAngleZ = (float) (Math.PI / 2);
        legs[2].rotateAngleZ = (float) (Math.PI * 3.0 / 2.0);
        legs[3].rotateAngleZ = (float) Math.PI;
    }

    @Override
    public void render(BedTileEntity bed, float partialTicks, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        World world = bed.getWorld();
        int tint = world == null ? -1 : ColorHandlers.tintOf(bed.getBlockState().getBlock());
        if (tint == -1)
        {
            super.render(bed, partialTicks, pose, buffer, light, overlay);
            return;
        }
        BlockState state = bed.getBlockState();
        TileEntityMerger.ICallbackWrapper<? extends BedTileEntity> combined = TileEntityMerger.func_226924_a_(TileEntityType.BED,
                BedBlock::getMergeType, BedBlock::getFootDirection, ChestBlock.FACING, state, world, bed.getPos(), (w, pos) -> false);
        int brightness = ((Int2IntFunction) combined.apply(new DualBrightnessCallback<>())).get(light);
        renderPiece(pose, buffer, state.get(BedBlock.PART) == BedPart.HEAD, state.get(BedBlock.HORIZONTAL_FACING), tint, brightness, overlay, false);
    }

    /** Draws a dyed bed item; the item's tile entity has no world to tell which bed it is. */
    public void renderItem(int tint, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        renderPiece(pose, buffer, true, Direction.SOUTH, tint, light, overlay, false);
        renderPiece(pose, buffer, false, Direction.SOUTH, tint, light, overlay, true);
    }

    /** Vanilla's piece drawing, with the tinted bedding and then the frame. */
    private void renderPiece(MatrixStack pose, IRenderTypeBuffer buffer, boolean isHead, Direction facing, int tint, int light,
            int overlay, boolean isFoot)
    {
        head.showModel = isHead;
        foot.showModel = !isHead;
        legs[0].showModel = !isHead;
        legs[1].showModel = isHead;
        legs[2].showModel = !isHead;
        legs[3].showModel = isHead;
        pose.push();
        pose.translate(0.0, 0.5625, isFoot ? -1.0 : 0.0);
        pose.rotate(Vector3f.XP.rotationDegrees(90.0F));
        pose.translate(0.5, 0.5, 0.5);
        pose.rotate(Vector3f.ZP.rotationDegrees(180.0F + facing.getHorizontalAngle()));
        pose.translate(-0.5, -0.5, -0.5);
        float red = (tint >> 16 & 255) / 255.0F;
        float green = (tint >> 8 & 255) / 255.0F;
        float blue = (tint & 255) / 255.0F;
        // Both layers are cut out, so the frame's see-through pixels don't hide the bedding (and the other way round).
        IVertexBuilder bedding = BEDDING.getBuffer(buffer, RenderType::getEntityCutout);
        renderParts(pose, bedding, light, overlay, red, green, blue);
        IVertexBuilder frame = FRAME.getBuffer(buffer, RenderType::getEntityCutout);
        renderParts(pose, frame, light, overlay, 1.0F, 1.0F, 1.0F);
        pose.pop();
    }

    private void renderParts(MatrixStack pose, IVertexBuilder builder, int light, int overlay, float red, float green, float blue)
    {
        head.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        foot.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        for (ModelRenderer leg : legs)
        {
            leg.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        }
    }
}

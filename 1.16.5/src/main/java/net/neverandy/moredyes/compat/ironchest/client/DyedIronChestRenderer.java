package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlock;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestTileEntity;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Draws a dyed Iron Chests chest: Iron Chests' chest model (the same boxes as its IronChestTileEntityRenderer), once
 * with the grey wood texture multiplied by the dye color and once with the metal, latch and inside in their own colors
 * (see DyedIronChestTextures).
 */
public class DyedIronChestRenderer extends TileEntityRenderer<DyedIronChestTileEntity>
{
    private static final ModelRenderer BOTTOM = new ModelRenderer(64, 64, 0, 19);
    private static final ModelRenderer LID = new ModelRenderer(64, 64, 0, 0);
    private static final ModelRenderer LOCK = new ModelRenderer(64, 64, 0, 0);

    static
    {
        BOTTOM.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        LID.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        LID.rotationPointY = 9.0F;
        LID.rotationPointZ = 1.0F;
        LOCK.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        LOCK.rotationPointY = 8.0F;
    }

    public DyedIronChestRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(DyedIronChestTileEntity chest, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        World world = chest.getWorld();
        BlockState state = chest.getBlockState();
        if (world == null || !(state.getBlock() instanceof DyedIronChestBlock))
        {
            return;
        }
        DyedIronChestBlock block = (DyedIronChestBlock) state.getBlock();
        TileEntityMerger.ICallbackWrapper<? extends GenericIronChestTileEntity> merged = block.getWrapper(state, world, chest.getPos(), true);
        float lid = merged.<Float2FloatFunction>apply(GenericIronChestBlock.getLid(chest)).get(partialTicks);
        int mergedLight = merged.<Int2IntFunction>apply(new DualBrightnessCallback<>()).applyAsInt(light);
        render(block.tier(), state.get(DyedIronChestBlock.COLOR), state.get(GenericIronChestBlock.FACING), lid, matrix, buffer, mergedLight, overlay);
    }

    public static void render(IronChestCompat.Tier tier, int color, Direction facing, float lid, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;

        matrix.push();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.rotate(Vector3f.YP.rotationDegrees(-facing.getHorizontalAngle()));
        matrix.translate(-0.5D, -0.5D, -0.5D);

        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;
        LID.rotateAngleX = -(lid * ((float) Math.PI / 2F));
        LOCK.rotateAngleX = LID.rotateAngleX;

        IVertexBuilder wood = buffer.getBuffer(RenderType.getEntityCutout(DyedIronChestTextures.wood(tier)));
        LID.render(matrix, wood, light, overlay, r, g, b, 1.0F);
        BOTTOM.render(matrix, wood, light, overlay, r, g, b, 1.0F);
        IVertexBuilder trim = buffer.getBuffer(RenderType.getEntityCutout(DyedIronChestTextures.trim(tier)));
        LID.render(matrix, trim, light, overlay);
        LOCK.render(matrix, trim, light, overlay);
        BOTTOM.render(matrix, trim, light, overlay);
        matrix.pop();
    }
}

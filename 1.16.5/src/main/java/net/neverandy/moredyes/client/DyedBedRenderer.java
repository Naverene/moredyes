package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedBedTileEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Draws every bed, vanilla and More Dyes. It replaces vanilla's renderer for the bed tile entity, and draws the More
 * Dyes bed tile entity (DyedBedTileEntity) too. A dyed bed is drawn from the grey bedding texture
 * {@code textures/entity/bed/dyed.png} tinted with its color, with the white pillow ({@code pillow.png}) over it; a
 * More Dyes bed in a vanilla color uses vanilla's texture instead. Over that goes the wooden frame, in the planks the
 * bed was made from: {@code frame/<wood>.png} for vanilla planks, or {@code frame/dyed.png} tinted with the color of
 * More Dyes planks. Vanilla's renderer keeps its models to itself, so the models and drawing are copied from it.
 */
public class DyedBedRenderer extends BedTileEntityRenderer
{
    static final RenderMaterial BEDDING = material("dyed");
    static final RenderMaterial PILLOW = material("pillow");
    static final RenderMaterial DYED_FRAME = material("frame/dyed");
    /** The vanilla planks with a frame texture of their own, by wood ("dark_oak" for minecraft:dark_oak_planks). */
    static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped"};
    static final Map<String, RenderMaterial> FRAMES = new HashMap<>();

    static
    {
        for (String wood : WOODS)
        {
            FRAMES.put(wood, material("frame/" + wood));
        }
    }

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

    private static RenderMaterial material(String name)
    {
        return new RenderMaterial(Atlases.BED_ATLAS, new ResourceLocation(Reference.MOD_ID, "entity/bed/" + name));
    }

    /** Every texture the renderer adds to the bed atlas. */
    static List<RenderMaterial> materials()
    {
        List<RenderMaterial> materials = new ArrayList<>(Arrays.asList(BEDDING, PILLOW, DYED_FRAME));
        materials.addAll(FRAMES.values());
        return materials;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(BedTileEntity bed, float partialTicks, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        World world = bed.getWorld();
        BlockState state = bed.getBlockState();
        Block block = state.getBlock();
        if (world == null || !(block instanceof BedBlock) || ColorHandlers.tintOf(block) == -1)
        {
            super.render(bed, partialTicks, pose, buffer, light, overlay);
            return;
        }
        Look look = new Look((BedBlock) block, bed instanceof DyedBedTileEntity ? ((DyedBedTileEntity) bed).getWood() : null);
        TileEntityMerger.ICallbackWrapper<? extends BedTileEntity> combined = TileEntityMerger.func_226924_a_(
                (TileEntityType<BedTileEntity>) bed.getType(), BedBlock::getMergeType, BedBlock::getFootDirection, ChestBlock.FACING,
                state, world, bed.getPos(), (w, pos) -> false);
        int brightness = ((Int2IntFunction) combined.apply(new DualBrightnessCallback<>())).get(light);
        renderPiece(pose, buffer, state.get(BedBlock.PART) == BedPart.HEAD, state.get(BedBlock.HORIZONTAL_FACING), look, brightness, overlay, false);
    }

    /** Draws a More Dyes bed item; the item's tile entity has no world to tell which bed it is. */
    public void renderItem(ItemStack stack, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        Look look = new Look((BedBlock) ((BlockItem) stack.getItem()).getBlock(), DyedBedTileEntity.woodOf(stack));
        renderPiece(pose, buffer, true, Direction.SOUTH, look, light, overlay, false);
        renderPiece(pose, buffer, false, Direction.SOUTH, look, light, overlay, true);
    }

    /** How a More Dyes bed is drawn: its bedding and its frame. */
    private static final class Look
    {
        final RenderMaterial bedding;
        final int beddingTint;
        final boolean pillow;
        final RenderMaterial frame;
        final int frameTint;

        Look(BedBlock block, @Nullable ResourceLocation wood)
        {
            boolean dyed = MDBlock.vanillaColorBed(block.getColor()) != block;
            bedding = dyed ? BEDDING : Atlases.BED_TEXTURES[block.getColor().getId()];
            beddingTint = dyed ? ColorHandlers.tintOf(block) : -1;
            pillow = dyed;
            String vanilla = wood != null && "minecraft".equals(wood.getNamespace()) && wood.getPath().endsWith("_planks")
                    ? wood.getPath().substring(0, wood.getPath().length() - "_planks".length()) : null;
            int tint = wood == null || vanilla != null ? -1 : ColorHandlers.tintOf(ForgeRegistries.BLOCKS.getValue(wood));
            if (tint != -1)
            {
                frame = DYED_FRAME;
                frameTint = tint;
            }
            else
            {
                frame = FRAMES.getOrDefault(vanilla, FRAMES.get("oak"));
                frameTint = -1;
            }
        }
    }

    /** Vanilla's piece drawing, with the bedding, the pillow and then the frame. */
    private void renderPiece(MatrixStack pose, IRenderTypeBuffer buffer, boolean isHead, Direction facing, Look look, int light,
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
        // All layers are cut out, so one layer's see-through pixels don't hide another's.
        renderParts(pose, buffer, look.bedding, look.beddingTint, light, overlay);
        if (look.pillow)
        {
            renderParts(pose, buffer, PILLOW, -1, light, overlay);
        }
        renderParts(pose, buffer, look.frame, look.frameTint, light, overlay);
        pose.pop();
    }

    private void renderParts(MatrixStack pose, IRenderTypeBuffer buffer, RenderMaterial material, int tint, int light, int overlay)
    {
        IVertexBuilder builder = material.getBuffer(buffer, RenderType::getEntityCutout);
        float red = tint == -1 ? 1.0F : (tint >> 16 & 255) / 255.0F;
        float green = tint == -1 ? 1.0F : (tint >> 8 & 255) / 255.0F;
        float blue = tint == -1 ? 1.0F : (tint & 255) / 255.0F;
        head.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        foot.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        for (ModelRenderer leg : legs)
        {
            leg.render(pose, builder, light, overlay, red, green, blue, 1.0F);
        }
    }
}

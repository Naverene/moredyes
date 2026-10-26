package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedBedBlockEntity;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Draws every bed, vanilla and More Dyes. It replaces vanilla's renderer for the bed block entity, and draws the More
 * Dyes bed block entity (DyedBedBlockEntity) too. A dyed bed is drawn from the grey bedding texture
 * {@code textures/entity/bed/dyed.png} tinted with its color, with the white pillow ({@code pillow.png}) over it; a
 * More Dyes bed in a vanilla color uses vanilla's texture instead. Over that goes the wooden frame, in the planks the
 * bed was made from: {@code frame/<wood>.png} for vanilla planks, or {@code frame/dyed.png} tinted with the color of
 * More Dyes planks. Vanilla's renderer keeps its models to itself, so the drawing is copied from it.
 */
public class DyedBedRenderer extends BedRenderer
{
    static final Material BEDDING = material("dyed");
    static final Material PILLOW = material("pillow");
    static final Material DYED_FRAME = material("frame/dyed");
    /** The vanilla planks with a frame texture of their own, by wood ("dark_oak" for minecraft:dark_oak_planks). */
    static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"};
    static final Map<String, Material> FRAMES = new HashMap<>();

    static
    {
        for (String wood : WOODS)
        {
            FRAMES.put(wood, material("frame/" + wood));
        }
    }

    private final ModelPart head;
    private final ModelPart foot;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
        this.head = context.bakeLayer(ModelLayers.BED_HEAD);
        this.foot = context.bakeLayer(ModelLayers.BED_FOOT);
    }

    private static Material material(String name)
    {
        return new Material(Sheets.BED_SHEET, new ResourceLocation(Reference.MOD_ID, "entity/bed/" + name));
    }

    /** Every texture the renderer adds to the bed atlas. */
    static List<Material> materials()
    {
        List<Material> materials = new ArrayList<>(List.of(BEDDING, PILLOW, DYED_FRAME));
        materials.addAll(FRAMES.values());
        return materials;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(BedBlockEntity bed, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        BlockState state = bed.getBlockState();
        if (!(state.getBlock() instanceof BedBlock block) || ColorHandlers.tintOf(block) == -1)
        {
            super.render(bed, partialTicks, pose, buffer, light, overlay);
            return;
        }
        Look look = new Look(block, bed instanceof DyedBedBlockEntity dyed ? dyed.getWood() : null);
        Level level = bed.getLevel();
        if (level != null)
        {
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> combined = DoubleBlockCombiner.combineWithNeigbour(
                    (BlockEntityType<BedBlockEntity>) bed.getType(), BedBlock::getBlockType, BedBlock::getConnectedDirection,
                    ChestBlock.FACING, state, level, bed.getBlockPos(), (world, pos) -> false);
            int brightness = combined.apply(new BrightnessCombiner<>()).get(light);
            renderPiece(pose, buffer, state.getValue(BedBlock.PART) == BedPart.HEAD ? head : foot,
                    state.getValue(BedBlock.FACING), look, brightness, overlay, false);
        }
        else
        {
            renderPiece(pose, buffer, head, Direction.SOUTH, look, light, overlay, false);
            renderPiece(pose, buffer, foot, Direction.SOUTH, look, light, overlay, true);
        }
    }

    /** How a More Dyes bed is drawn: its bedding and its frame. */
    private static final class Look
    {
        final Material bedding;
        final int beddingTint;
        final boolean pillow;
        final Material frame;
        final int frameTint;

        Look(BedBlock block, @Nullable ResourceLocation wood)
        {
            boolean dyed = MDBlock.vanillaColorBed(block.getColor()) != block;
            bedding = dyed ? BEDDING : Sheets.BED_TEXTURES[block.getColor().getId()];
            beddingTint = dyed ? ColorHandlers.tintOf(block) : -1;
            pillow = dyed;
            String vanilla = wood != null && "minecraft".equals(wood.getNamespace()) && wood.getPath().endsWith("_planks")
                    ? wood.getPath().substring(0, wood.getPath().length() - "_planks".length()) : null;
            int tint = wood == null || vanilla != null ? -1 : ColorHandlers.tintOf(BuiltInRegistries.BLOCK.get(wood));
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

    /** Vanilla's BedRenderer.renderPiece, drawing the bedding, the pillow and then the frame. */
    private static void renderPiece(PoseStack pose, MultiBufferSource buffer, ModelPart part, Direction facing, Look look,
            int light, int overlay, boolean foot)
    {
        pose.pushPose();
        pose.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F + facing.toYRot()));
        pose.translate(-0.5F, -0.5F, -0.5F);
        // All layers are cut out, so one layer's see-through pixels don't hide another's.
        draw(part, pose, buffer, look.bedding, look.beddingTint, light, overlay);
        if (look.pillow)
        {
            draw(part, pose, buffer, PILLOW, -1, light, overlay);
        }
        draw(part, pose, buffer, look.frame, look.frameTint, light, overlay);
        pose.popPose();
    }

    private static void draw(ModelPart part, PoseStack pose, MultiBufferSource buffer, Material material, int tint, int light, int overlay)
    {
        VertexConsumer consumer = material.buffer(buffer, RenderType::entityCutout);
        if (tint == -1)
        {
            part.render(pose, consumer, light, overlay);
        }
        else
        {
            part.render(pose, consumer, light, overlay, (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F, 1.0F);
        }
    }
}

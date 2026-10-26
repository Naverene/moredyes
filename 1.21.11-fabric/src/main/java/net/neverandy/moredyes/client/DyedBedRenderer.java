package net.neverandy.moredyes.client;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.block.DyedBedBlockEntity;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * Draws every bed, vanilla and More Dyes. It replaces vanilla's renderer for the bed block entity, and draws the More
 * Dyes bed block entity (DyedBedBlockEntity) too. A dyed bed is drawn from the grey bedding texture
 * {@code textures/entity/bed/dyed.png} tinted with its color, with the white pillow ({@code pillow.png}) over it; a
 * More Dyes bed in a vanilla color uses vanilla's texture instead. Over that goes the wooden frame, in the planks the
 * bed was made from: {@code frame/<wood>.png} for vanilla planks, or {@code frame/dyed.png} tinted with the color of
 * More Dyes planks. All layers are drawn cut out, so none covers another.
 */
public class DyedBedRenderer extends BedRenderer {

    public static final Material BEDDING = material("dyed");
    public static final Material PILLOW = material("pillow");
    public static final Material DYED_FRAME = material("frame/dyed");
    /** The vanilla planks with a frame texture of their own, by wood ("dark_oak" for minecraft:dark_oak_planks). */
    private static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove",
        "cherry", "pale_oak", "bamboo", "crimson", "warped"};
    private static final Map<String, Material> FRAMES = new HashMap<>();

    static {
        for (String wood : WOODS) {
            FRAMES.put(wood, material("frame/" + wood));
        }
    }

    private final MaterialSet materials;
    private final Model.Simple headModel;
    private final Model.Simple footModel;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.materials(), context.entityModelSet());
    }

    public DyedBedRenderer(MaterialSet materials, EntityModelSet models) {
        super(materials, models);
        this.materials = materials;
        this.headModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_HEAD), RenderTypes::entitySolid);
        this.footModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_FOOT), RenderTypes::entitySolid);
    }

    private static Material material(String name) {
        return Sheets.BED_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, name));
    }

    @Override
    public BedRenderState createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BedBlockEntity bed, BedRenderState state, float partialTicks, Vec3 cameraPosition,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(bed, state, partialTicks, cameraPosition, breakProgress);
        ((State) state).look = bed.getBlockState().getBlock() instanceof DyedBedBlock block
            ? Look.of(block, bed instanceof DyedBedBlockEntity dyed ? dyed.getWood() : null) : null;
    }

    @Override
    public void submit(BedRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
        CameraRenderState camera) {
        Look look = state instanceof State ours ? ours.look : null;
        if (look == null) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        submitPiece(poseStack, collector, state.isHead ? headModel : footModel, state.facing, look, state.lightCoords,
            OverlayTexture.NO_OVERLAY, false, state.breakProgress, 0);
    }

    /** Draws a More Dyes bed as an item, like vanilla's submitSpecial. */
    public void submitItem(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        Look look, int outlineColor) {
        submitPiece(poseStack, collector, headModel, Direction.SOUTH, look, lightCoords, overlayCoords, false, null,
            outlineColor);
        submitPiece(poseStack, collector, footModel, Direction.SOUTH, look, lightCoords, overlayCoords, true, null,
            outlineColor);
    }

    private void submitPiece(PoseStack poseStack, SubmitNodeCollector collector, Model.Simple model,
        Direction facing, Look look, int lightCoords, int overlayCoords, boolean foot,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, int outlineColor) {
        poseStack.pushPose();
        // Vanilla's BedRenderer.preparePose
        poseStack.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        collector.submitModel(model, Unit.INSTANCE, poseStack, look.bedding().renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, look.beddingTint(), materials.get(look.bedding()), outlineColor, breakProgress);
        if (look.pillow()) {
            collector.submitModel(model, Unit.INSTANCE, poseStack, PILLOW.renderType(RenderTypes::entityCutout),
                lightCoords, overlayCoords, -1, materials.get(PILLOW), 0, null);
        }
        collector.submitModel(model, Unit.INSTANCE, poseStack, look.frame().renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, look.frameTint(), materials.get(look.frame()), 0, null);
        poseStack.popPose();
    }

    /** How a More Dyes bed is drawn: its bedding (with the pillow on dyed beds) and its frame. */
    public record Look(Material bedding, int beddingTint, boolean pillow, Material frame, int frameTint) {

        static Look of(DyedBedBlock block, @Nullable Identifier wood) {
            return ModBlocks.kindOf(block) == Kind.BED ? dyed(Tints.blockTint(block), wood)
                : vanilla(block.getColor(), wood);
        }

        /** A dyed bed in a color (ARGB). */
        public static Look dyed(int tint, @Nullable Identifier wood) {
            return withFrame(BEDDING, tint, true, wood);
        }

        /** A More Dyes bed in a vanilla color. */
        public static Look vanilla(DyeColor color, @Nullable Identifier wood) {
            return withFrame(Sheets.getBedMaterial(color), -1, false, wood);
        }

        private static Look withFrame(Material bedding, int beddingTint, boolean pillow, @Nullable Identifier wood) {
            String vanilla = wood != null && "minecraft".equals(wood.getNamespace()) && wood.getPath().endsWith("_planks")
                ? wood.getPath().substring(0, wood.getPath().length() - "_planks".length()) : null;
            int tint = wood == null || vanilla != null ? -1 : Tints.blockTint(BuiltInRegistries.BLOCK.getValue(wood));
            if (tint != -1) {
                return new Look(bedding, beddingTint, pillow, DYED_FRAME, tint);
            }
            return new Look(bedding, beddingTint, pillow, FRAMES.getOrDefault(vanilla, FRAMES.get("oak")), -1);
        }
    }

    public static class State extends BedRenderState {
        public @Nullable Look look;
    }
}

package net.neverandy.moredyes.client;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
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

    public static final SpriteId BEDDING = sprite("dyed");
    public static final SpriteId PILLOW = sprite("pillow");
    public static final SpriteId DYED_FRAME = sprite("frame/dyed");
    /** The vanilla planks with a frame texture of their own, by wood ("dark_oak" for minecraft:dark_oak_planks). */
    private static final String[] WOODS = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove",
        "cherry", "pale_oak", "bamboo", "crimson", "warped"};
    private static final Map<String, SpriteId> FRAMES = new HashMap<>();

    static {
        for (String wood : WOODS) {
            FRAMES.put(wood, sprite("frame/" + wood));
        }
    }

    private final SpriteGetter sprites;
    private final Model.Simple headModel;
    private final Model.Simple footModel;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.sprites(), context.entityModelSet());
    }

    public DyedBedRenderer(SpriteGetter sprites, EntityModelSet models) {
        super(sprites, models);
        this.sprites = sprites;
        this.headModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_HEAD), RenderTypes::entitySolid);
        this.footModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_FOOT), RenderTypes::entitySolid);
    }

    private static SpriteId sprite(String name) {
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
        poseStack.pushPose();
        poseStack.mulPose(modelTransform(state.facing));
        submitLook(state.part, look, poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
            state.breakProgress, 0);
        poseStack.popPose();
    }

    /** Vanilla's submitPiece, drawing the bedding, the pillow on dyed beds, and the frame. */
    public void submitLook(BedPart part, Look look, PoseStack poseStack, SubmitNodeCollector collector,
        int lightCoords, int overlayCoords, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
        int outlineColor) {
        Model.Simple model = part == BedPart.HEAD ? headModel : footModel;
        collector.submitModel(model, Unit.INSTANCE, poseStack, look.bedding().renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, look.beddingTint(), sprites.get(look.bedding()), outlineColor, breakProgress);
        if (look.pillow()) {
            collector.submitModel(model, Unit.INSTANCE, poseStack, PILLOW.renderType(RenderTypes::entityCutout),
                lightCoords, overlayCoords, -1, sprites.get(PILLOW), 0, null);
        }
        collector.submitModel(model, Unit.INSTANCE, poseStack, look.frame().renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, look.frameTint(), sprites.get(look.frame()), 0, null);
    }

    /** How a More Dyes bed is drawn: its bedding (with the pillow on dyed beds) and its frame. */
    public record Look(SpriteId bedding, int beddingTint, boolean pillow, SpriteId frame, int frameTint) {

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
            return withFrame(Sheets.getBedSprite(color), -1, false, wood);
        }

        private static Look withFrame(SpriteId bedding, int beddingTint, boolean pillow, @Nullable Identifier wood) {
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

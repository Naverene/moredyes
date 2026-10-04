package net.neverandy.moredyes.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedSignBlockEntity;

import java.util.List;

/**
 * Draws dyed signs. This is the vanilla sign renderer, except the board and post are the grey
 * textures/entity/signs/dyed multiplied by the dye color.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DyedSignRenderer implements BlockEntityRenderer<DyedSignBlockEntity>
{
    private static final Material MATERIAL = new Material(Sheets.SIGN_SHEET, new ResourceLocation(Reference.MOD_ID, "entity/signs/dyed"));
    private static final float SCALE = 0.6666667F;
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);

    private final SignRenderer.SignModel model;
    private final Font font;

    public DyedSignRenderer(BlockEntityRendererProvider.Context context)
    {
        model = SignRenderer.createSignModel(context.getModelSet(), WoodType.OAK);
        font = context.getFont();
    }

    @Override
    public void render(DyedSignBlockEntity sign, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        BlockState state = sign.getBlockState();
        Block block = state.getBlock();
        pose.pushPose();
        pose.translate(0.5D, 0.5D, 0.5D);
        if (block instanceof StandingSignBlock)
        {
            pose.mulPose(Vector3f.YP.rotationDegrees(-(state.getValue(StandingSignBlock.ROTATION) * 360 / 16.0F)));
            model.stick.visible = true;
        }
        else
        {
            pose.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(WallSignBlock.FACING).toYRot()));
            pose.translate(0.0D, -0.3125D, -0.4375D);
            model.stick.visible = false;
        }

        int color = ColorHandlers.colorOf(block.getRegistryName());
        pose.pushPose();
        pose.scale(SCALE, -SCALE, -SCALE);
        VertexConsumer consumer = MATERIAL.buffer(buffer, model::renderType);
        model.root.render(pose, consumer, light, overlay, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        pose.popPose();

        float textScale = 0.010416667F;
        pose.translate(0.0D, 0.33333334F, 0.046666667F);
        pose.scale(textScale, -textScale, textScale);
        int darkColor = darkColor(sign);
        FormattedCharSequence[] lines = sign.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), line ->
        {
            List<FormattedCharSequence> split = font.split(line, 90);
            return split.isEmpty() ? FormattedCharSequence.EMPTY : split.get(0);
        });
        int textColor;
        boolean outline;
        int textLight;
        if (sign.hasGlowingText())
        {
            textColor = sign.getColor().getTextColor();
            outline = isOutlineVisible(sign, textColor);
            textLight = 15728880;
        }
        else
        {
            textColor = darkColor;
            outline = false;
            textLight = light;
        }
        for (int i = 0; i < 4; i++)
        {
            FormattedCharSequence line = lines[i];
            float x = -font.width(line) / 2;
            if (outline)
            {
                font.drawInBatch8xOutline(line, x, i * 10 - 20, textColor, darkColor, pose.last().pose(), buffer, textLight);
            }
            else
            {
                font.drawInBatch(line, x, i * 10 - 20, textColor, false, pose.last().pose(), buffer, false, 0, textLight);
            }
        }
        pose.popPose();
    }

    private static boolean isOutlineVisible(DyedSignBlockEntity sign, int textColor)
    {
        if (textColor == DyeColor.BLACK.getTextColor())
        {
            return true;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && minecraft.options.getCameraType().isFirstPerson() && player.isScoping())
        {
            return true;
        }
        Entity camera = minecraft.getCameraEntity();
        return camera != null && camera.distanceToSqr(Vec3.atCenterOf(sign.getBlockPos())) < OUTLINE_RENDER_DISTANCE;
    }

    private static int darkColor(DyedSignBlockEntity sign)
    {
        int color = sign.getColor().getTextColor();
        if (color == DyeColor.BLACK.getTextColor() && sign.hasGlowingText())
        {
            return -988212;
        }
        return NativeImage.combine(0, (int) (NativeImage.getB(color) * 0.4), (int) (NativeImage.getG(color) * 0.4), (int) (NativeImage.getR(color) * 0.4));
    }

    /** The grey sign texture is not on the sign atlas unless we add it. */
    @SubscribeEvent
    public static void stitch(TextureStitchEvent.Pre event)
    {
        if (event.getAtlas().location().equals(Sheets.SIGN_SHEET))
        {
            event.addSprite(MATERIAL.texture());
        }
    }
}

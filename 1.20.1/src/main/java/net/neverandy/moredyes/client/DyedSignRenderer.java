package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedSignBlockEntity;

import java.util.List;

/**
 * Draws dyed signs. This is the vanilla sign renderer, whose parts are not public, except the board and post are
 * the grey textures/entity/signs/dyed multiplied by the dye color. The vanilla sign atlas picks up every texture in
 * textures/entity/signs, so the grey one needs no registering.
 */
public class DyedSignRenderer implements BlockEntityRenderer<DyedSignBlockEntity>
{
    private static final Material MATERIAL = new Material(Sheets.SIGN_SHEET, new ResourceLocation(Reference.MOD_ID, "entity/signs/dyed"));
    private static final float SCALE = 0.6666667F;
    private static final Vec3 TEXT_OFFSET = new Vec3(0.0, 0.33333334F, 0.046666667F);
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;

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
        if (!(state.getBlock() instanceof SignBlock block))
        {
            return;
        }
        boolean standing = block instanceof StandingSignBlock;
        pose.pushPose();
        pose.translate(0.5F, 0.75F * SCALE, 0.5F);
        pose.mulPose(Axis.YP.rotationDegrees(-block.getYRotationDegrees(state)));
        if (!standing)
        {
            pose.translate(0.0F, -0.3125F, -0.4375F);
        }

        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        int color = name == null ? 0xFFFFFF : ColorHandlers.colorOf(name);
        pose.pushPose();
        pose.scale(SCALE, -SCALE, -SCALE);
        model.stick.visible = standing;
        VertexConsumer consumer = MATERIAL.buffer(buffer, model::renderType);
        model.root.render(pose, consumer, light, overlay, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        pose.popPose();

        renderText(sign.getBlockPos(), sign.getFrontText(), pose, buffer, light, sign.getTextLineHeight(), sign.getMaxTextLineWidth(), true);
        renderText(sign.getBlockPos(), sign.getBackText(), pose, buffer, light, sign.getTextLineHeight(), sign.getMaxTextLineWidth(), false);
        pose.popPose();
    }

    private void renderText(BlockPos pos, SignText text, PoseStack pose, MultiBufferSource buffer, int light, int lineHeight, int maxWidth, boolean front)
    {
        pose.pushPose();
        if (!front)
        {
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        float scale = 0.015625F * SCALE;
        pose.translate(TEXT_OFFSET.x, TEXT_OFFSET.y, TEXT_OFFSET.z);
        pose.scale(scale, -scale, scale);

        int darkColor = darkColor(text);
        int top = 4 * lineHeight / 2;
        FormattedCharSequence[] lines = text.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), line ->
        {
            List<FormattedCharSequence> split = font.split(line, maxWidth);
            return split.isEmpty() ? FormattedCharSequence.EMPTY : split.get(0);
        });
        int textColor;
        boolean outline;
        int textLight;
        if (text.hasGlowingText())
        {
            textColor = text.getColor().getTextColor();
            outline = isOutlineVisible(pos, textColor);
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
                font.drawInBatch8xOutline(line, x, i * lineHeight - top, textColor, darkColor, pose.last().pose(), buffer, textLight);
            }
            else
            {
                font.drawInBatch(line, x, i * lineHeight - top, textColor, false, pose.last().pose(), buffer, Font.DisplayMode.POLYGON_OFFSET, 0, textLight);
            }
        }
        pose.popPose();
    }

    private static boolean isOutlineVisible(BlockPos pos, int textColor)
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
        return camera != null && camera.distanceToSqr(Vec3.atCenterOf(pos)) < OUTLINE_RENDER_DISTANCE;
    }

    private static int darkColor(SignText text)
    {
        int color = text.getColor().getTextColor();
        if (color == DyeColor.BLACK.getTextColor() && text.hasGlowingText())
        {
            return BLACK_TEXT_OUTLINE_COLOR;
        }
        return FastColor.ARGB32.color(0, (int) (FastColor.ARGB32.red(color) * 0.4), (int) (FastColor.ARGB32.green(color) * 0.4),
                (int) (FastColor.ARGB32.blue(color) * 0.4));
    }
}

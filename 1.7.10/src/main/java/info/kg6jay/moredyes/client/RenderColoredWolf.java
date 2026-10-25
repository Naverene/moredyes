package info.kg6jay.moredyes.client;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.entity.PetCollar;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.utility.ColorUtil;

/** The vanilla wolf renderer, drawing a collar with a More Dyes shade (see {@link PetCollar}) in that shade. */
public class RenderColoredWolf extends RenderWolf {

    public RenderColoredWolf() {
        super(new ModelWolf(), new ModelWolf(), 0.5F);
    }

    @Override
    protected int shouldRenderPass(EntityWolf wolf, int pass, float partialTicks) {
        int result = super.shouldRenderPass(wolf, pass, partialTicks);
        int color = PetCollar.get(wolf);
        // Pass 1 is the collar of a tamed wolf.
        if (pass == 1 && result == 1 && color != SheepColor.NONE) {
            int set = SheepColor.setOf(color);
            int rgb = ColorUtil.shade(MDBlock.colorStrings[set], SheepColor.shadeOf(color));
            GL11.glColor3f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        return result;
    }
}

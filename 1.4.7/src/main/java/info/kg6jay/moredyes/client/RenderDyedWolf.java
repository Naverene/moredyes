package info.kg6jay.moredyes.client;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWolf;
import org.lwjgl.opengl.GL11;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.entity.SheepColors;
import info.kg6jay.moredyes.entity.WolfCollars;

/** The vanilla wolf renderer, drawing a collar with a More Dyes color (see {@link WolfCollars}) in that color. */
public class RenderDyedWolf extends RenderWolf {

    public RenderDyedWolf() {
        super(new ModelWolf(), new ModelWolf(), 0.5F);
    }

    @Override
    protected int shouldRenderPass(EntityLiving entity, int pass, float partialTicks) {
        int result = super.shouldRenderPass(entity, pass, partialTicks);
        int color = WolfCollars.get((EntityWolf) entity);
        // Pass 1 is the collar of a tamed wolf.
        if (pass == 1 && result == 1 && color != SheepColors.NONE) {
            int rgb = Colors.rgb(color);
            GL11.glColor3f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        return result;
    }
}

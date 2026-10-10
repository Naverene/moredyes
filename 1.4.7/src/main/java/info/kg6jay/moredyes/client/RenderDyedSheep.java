package info.kg6jay.moredyes.client;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.entity.SheepColors;

/** The vanilla sheep renderer, but it draws the fleece of a sheep colored with a More Dyes dye in that color. */
public class RenderDyedSheep extends RenderSheep {

    public RenderDyedSheep() {
        super(new ModelSheep2(), new ModelSheep1(), 0.7F);
    }

    @Override
    protected int setWoolColorAndRender(EntitySheep sheep, int pass, float partialTicks) {
        int result = super.setWoolColorAndRender(sheep, pass, partialTicks);
        int color = SheepColors.get(sheep);
        if (result == 1 && color != SheepColors.NONE) {
            int rgb = Colors.rgb(color);
            GL11.glColor3f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        return result;
    }
}

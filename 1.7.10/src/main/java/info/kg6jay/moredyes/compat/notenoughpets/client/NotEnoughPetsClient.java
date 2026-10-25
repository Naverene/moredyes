package info.kg6jay.moredyes.compat.notenoughpets.client;

import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

import astrotibs.notenoughpets.client.model.ModelOcelotNEP;
import astrotibs.notenoughpets.client.model.ModelWolfNEP;
import astrotibs.notenoughpets.client.render.RenderOcelotNEP;
import astrotibs.notenoughpets.client.render.RenderWolfNEP;
import astrotibs.notenoughpets.entity.EntityOcelotNEP;
import astrotibs.notenoughpets.entity.EntityWolfNEP;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.entity.PetCollar;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * Swaps the Not Enough Pets cat and wolf renderers for ones that draw a collar with a More Dyes shade in that shade.
 * More Dyes registers its renderers after Not Enough Pets (see the @Mod dependencies), so these replace the originals.
 */
public final class NotEnoughPetsClient {

    private NotEnoughPetsClient() {}

    public static void register() {
        RenderingRegistry.registerEntityRenderingHandler(
            EntityOcelotNEP.class,
            new Cats(new ModelOcelotNEP(), new ModelOcelotNEP()));
        RenderingRegistry
            .registerEntityRenderingHandler(EntityWolfNEP.class, new Wolves(new ModelWolfNEP(), new ModelWolfNEP()));
    }

    /** Tints the collar pass (pass 1, which the renderer has just colored) if the pet has a More Dyes shade. */
    static int tint(EntityLivingBase pet, int pass, int result) {
        int color = PetCollar.get(pet);
        if (pass == 1 && result == 1 && color != SheepColor.NONE) {
            int set = SheepColor.setOf(color);
            int rgb = ColorUtil.shade(MDBlock.colorStrings[set], SheepColor.shadeOf(color));
            GL11.glColor3f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        return result;
    }

    private static class Cats extends RenderOcelotNEP {

        Cats(ModelOcelotNEP model, ModelOcelotNEP passModel) {
            super(model, passModel, 0.4F);
        }

        @Override
        protected int shouldRenderPass(EntityLivingBase cat, int pass, float partialTicks) {
            return tint(cat, pass, super.shouldRenderPass(cat, pass, partialTicks));
        }
    }

    private static class Wolves extends RenderWolfNEP {

        Wolves(ModelWolfNEP model, ModelWolfNEP passModel) {
            super(model, passModel, 0.5F);
        }

        @Override
        protected int shouldRenderPass(EntityLivingBase wolf, int pass, float partialTicks) {
            return tint(wolf, pass, super.shouldRenderPass(wolf, pass, partialTicks));
        }
    }
}

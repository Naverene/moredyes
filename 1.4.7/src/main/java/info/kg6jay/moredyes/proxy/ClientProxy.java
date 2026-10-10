package info.kg6jay.moredyes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.BlockDyedChest;
import info.kg6jay.moredyes.block.BlockDyedLog;
import info.kg6jay.moredyes.block.BlockDyedPlant;
import info.kg6jay.moredyes.block.TileEntityDyedChest;
import info.kg6jay.moredyes.client.DyedChestRenderer;
import info.kg6jay.moredyes.client.LayeredBlockRenderer;
import info.kg6jay.moredyes.client.RenderDyedSheep;
import info.kg6jay.moredyes.client.RenderFallingDyed;
import info.kg6jay.moredyes.entity.EntityFallingDyed;
import info.kg6jay.moredyes.entity.SheepColors;

public class ClientProxy extends CommonProxy {

    /** Called before the blocks are made, since they read their render IDs when asked. */
    @Override
    public void registerRenderers() {
        MinecraftForgeClient.preloadTexture(Textures.SHEET);
        MinecraftForgeClient.preloadTexture(Textures.CHEST);
        MinecraftForgeClient.preloadTexture(Textures.LARGE_CHEST);

        BlockDyedPlant.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new LayeredBlockRenderer(BlockDyedPlant.renderId, true));
        BlockDyedLog.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new LayeredBlockRenderer(BlockDyedLog.renderId, false));

        DyedChestRenderer chest = new DyedChestRenderer();
        BlockDyedChest.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(chest);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedChest.class, chest);

        RenderingRegistry.registerEntityRenderingHandler(EntityFallingDyed.class, new RenderFallingDyed());
        RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, new RenderDyedSheep());
    }

    @Override
    public void onSheepColor(int entityId, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.theWorld == null ? null : mc.theWorld.getEntityByID(entityId);
        if (entity instanceof EntitySheep) {
            SheepColors.store((EntitySheep) entity, color);
        }
    }
}

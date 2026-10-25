package info.kg6jay.moredyes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDPiston;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDSign;
import info.kg6jay.moredyes.client.DyedBedRenderer;
import info.kg6jay.moredyes.client.FlowerPotRenderer;
import info.kg6jay.moredyes.client.LayeredBlockRenderer;
import info.kg6jay.moredyes.client.RenderColoredSheep;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.compat.ironchest.client.IronChestClient;
import info.kg6jay.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.render.TileEntityMDBlockColoredChestRenderer;
import info.kg6jay.moredyes.render.TileEntityMDPistonRenderer;
import info.kg6jay.moredyes.render.TileEntityMDSignRenderer;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        // Must be in place before the game loads its textures at the end of mod loading.
        TintedTextures.install();
    }

    @Override
    public void registerRenderThings() {
        RenderIds.chest = RenderingRegistry.getNextAvailableRenderId();
        TileEntityMDBlockColoredChestRenderer chestRenderer = new TileEntityMDBlockColoredChestRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDBlockColoredChest.class, chestRenderer);
        RenderingRegistry.registerBlockHandler(chestRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDPiston.class, new TileEntityMDPistonRenderer());

        RenderIds.layeredCube = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry
            .registerBlockHandler(new LayeredBlockRenderer(RenderIds.layeredCube, LayeredBlockRenderer.Shape.CUBE));
        RenderIds.layeredPlant = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry
            .registerBlockHandler(new LayeredBlockRenderer(RenderIds.layeredPlant, LayeredBlockRenderer.Shape.PLANT));
        RenderIds.layeredPiston = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry
            .registerBlockHandler(new LayeredBlockRenderer(RenderIds.layeredPiston, LayeredBlockRenderer.Shape.PISTON));
        RenderIds.flowerPot = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new FlowerPotRenderer(RenderIds.flowerPot));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDSign.class, new TileEntityMDSignRenderer());
        RenderIds.bed = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new DyedBedRenderer(RenderIds.bed));

        if (ConfigHandler.ironChests()) {
            IronChestClient.register();
        }
        if (ConfigHandler.storageDrawers()) {
            StorageDrawersClient.register();
        }

        // Draws sheep dyed with More Dyes in their shade; sheep without one look the same as in vanilla.
        RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, new RenderColoredSheep());
    }

    @Override
    public void setSheepColor(int entityId, int color) {
        World world = Minecraft.getMinecraft().theWorld;
        SheepColor sheepColor = world == null ? null : SheepColor.of(world.getEntityByID(entityId));
        if (sheepColor != null) {
            sheepColor.put(color);
        }
    }
}

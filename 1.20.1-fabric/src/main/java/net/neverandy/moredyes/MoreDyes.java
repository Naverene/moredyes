package net.neverandy.moredyes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.handler.CauldronWashing;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDTabs;
import net.neverandy.moredyes.item.crafting.ModRecipes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.ModTileEntities;
import net.neverandy.moredyes.world.ModWorldGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDyes implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.MOD_ID);

    @Override
    public void onInitialize()
    {
        ConfigHandler.load();
        // Wall recipes carry this load condition, so they are skipped when the walls are not registered.
        ResourceConditions.register(new ResourceLocation(Reference.MOD_ID, "walls_enabled"), json -> ConfigHandler.wallBlocks());

        MDBlock.register();
        MDItem.register();
        MDTabs.register();
        ModTileEntities.register();
        ModWorldGen.register();
        ModRecipes.register();
        DyedSheep.register();
        CauldronWashing.register();

        // Optional compat. The compat classes refer to the other mod's classes, so they are only touched when it's installed.
        if (FabricLoader.getInstance().isModLoaded(StorageDrawersCompat.MOD_ID))
        {
            // The drawers themselves are registered from Storage Drawers' initializer (mixin/compat/StorageDrawersMixin).
            StorageDrawersCompat.init();
        }
    }
}

package net.neverandy.moredyes;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neverandy.moredyes.block.Furnishings;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.data.condition.WallsEnabledCondition;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDTabs;
import net.neverandy.moredyes.item.crafting.ModRecipes;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.ModTileEntities;
import net.neverandy.moredyes.toggle.Toggles;
import net.neverandy.moredyes.world.ModWorldGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MOD_ID)
public class MoreDyes
{
    public static final Logger LOGGER = LogManager.getLogger();

    public MoreDyes()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.SERVER_CONFIG);
        ConfigHandler.loadEarly();
        CraftingHelper.register(WallsEnabledCondition.SERIALIZER);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);

        MDBlock.register(modBus);
        warnUnknownTypes();
        MDItem.register(modBus);
        MDTabs.register(modBus);
        ModTileEntities.register(modBus);
        ModWorldGen.register(modBus);
        ModRecipes.register(modBus);

        // Optional compat. The compat classes refer to the other mod's classes, so they are only touched when it's installed.
        if (ModList.get().isLoaded("ironchest") && Toggles.ironChests)
        {
            IronChestCompat.register(modBus);
        }
        if (ModList.get().isLoaded("storagedrawers") && Toggles.storageDrawers)
        {
            StorageDrawersCompat.register(modBus);
        }
        if (ModList.get().isLoaded("ironshulkerbox") && Toggles.ironShulkerBoxes)
        {
            IronShulkerBoxCompat.register(modBus);
        }
        // Turned off in the config: their recipes and tags are then dropped like those of turned-off blocks.
        if (!Toggles.ironChests)
        {
            for (String tier : Reference.IRON_CHEST_TIERS)
            {
                Toggles.leftOut("dyed_" + tier + "_chest");
            }
        }
        if (!Toggles.ironShulkerBoxes)
        {
            for (String tier : Reference.IRON_SHULKER_BOX_TIERS)
            {
                Toggles.leftOut("dyed_" + tier + "_shulker_box");
            }
        }
        if (!Toggles.storageDrawers)
        {
            for (String size : Reference.DRAWER_SIZES)
            {
                Toggles.leftOut("dyed_" + size);
            }
        }
    }

    /** Names in the config's disabled_blocks that match no block type, likely typos. */
    private static void warnUnknownTypes()
    {
        if (!Toggles.unknownTypes().isEmpty())
        {
            LOGGER.warn("disabled_blocks in moredyes-client.toml names {}, which aren't More Dyes block types. The types are: {}",
                    Toggles.unknownTypes(), Toggles.knownTypes());
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModNetwork.register();
        event.enqueueWork(MDBlock::registerPottedPlants);
        event.enqueueWork(Furnishings::setup);
    }
}

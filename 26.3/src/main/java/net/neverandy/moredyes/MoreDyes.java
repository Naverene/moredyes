package net.neverandy.moredyes;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.handler.CauldronWashing;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;
import net.neverandy.moredyes.registry.ModCreativeTabs;
import net.neverandy.moredyes.registry.ModItems;
import net.neverandy.moredyes.toggle.Toggles;

@Mod(MoreDyes.MOD_ID)
public class MoreDyes {

    public static final String MOD_ID = "moredyes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreDyes(IEventBus modBus, ModContainer container) {
        // A startup config is read as soon as it is registered, so it can choose which blocks to register.
        container.registerConfig(ModConfig.Type.STARTUP, Config.STARTUP_SPEC);
        Config.applyStartup();
        ModBlocks.register(modBus);
        if (!Toggles.unknownTypes().isEmpty()) {
            LOGGER.warn("disabled_blocks in moredyes-startup.toml names {}, which aren't More Dyes block types. The types are: {}",
                Toggles.unknownTypes(), Toggles.knownTypes());
        }
        ModItems.register(modBus);
        ModBlockEntities.register(modBus);
        ModCreativeTabs.register(modBus);
        DyedSheep.register(modBus);
        DyedCollars.register(modBus);
        modBus.addListener(CauldronWashing::register);
        modBus.addListener(MoreDyes::commonSetup);
        container.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        // Optional compat. Its classes refer to the other mod's classes, so they are only touched when it's installed.
        if (ModList.get().isLoaded(StorageDrawersCompat.MOD_ID) && Toggles.storageDrawers) {
            StorageDrawersCompat.register(modBus);
        }
        leaveOutCompat();
    }

    /** Compat blocks turned off in the config: their recipes and tags are dropped like those of turned-off blocks. */
    private static void leaveOutCompat() {
        if (!Toggles.storageDrawers) {
            for (String size : new String[]{"full_drawers_1", "full_drawers_2", "full_drawers_4", "half_drawers_1", "half_drawers_2", "half_drawers_4"}) {
                Toggles.leftOut(size);
            }
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Each kind burns like its vanilla block (see FireBlock.bootStrap).
            FireBlock fire = (FireBlock) Blocks.FIRE;
            for (Kind kind : Kind.values()) {
                int ignite = fire.getIgniteOdds(kind.vanilla().defaultBlockState());
                int burn = fire.getBurnOdds(kind.vanilla().defaultBlockState());
                if (ignite > 0 || burn > 0) {
                    ModBlocks.all(kind).forEach(block -> fire.setFlammable(block.get(), ignite, burn));
                }
            }
        });
        LOGGER.info("More Dyes registered {} colors of {} kinds of block", MixColors.ALL.size(), Kind.values().length);
    }
}

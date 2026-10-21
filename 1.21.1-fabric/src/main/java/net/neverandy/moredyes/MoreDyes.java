package net.neverandy.moredyes;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.handler.CauldronWashing;
import net.neverandy.moredyes.registry.DataMaps;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;
import net.neverandy.moredyes.registry.ModCreativeTabs;
import net.neverandy.moredyes.registry.ModItems;
import net.neverandy.moredyes.toggle.Toggles;
import net.neverandy.moredyes.registry.ModRecipes;
import net.neverandy.moredyes.world.ModWorldGen;

public class MoreDyes implements ModInitializer {

    public static final String MOD_ID = "moredyes";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        Config.load();
        ModBlocks.register();
        if (!Toggles.unknownTypes().isEmpty()) {
            LOGGER.warn("disabled_blocks in moredyes.properties names {}, which aren't More Dyes block types. The types are: {}",
                Toggles.unknownTypes(), Toggles.knownTypes());
        }
        ModItems.register();
        ModBlockEntities.register();
        ModCreativeTabs.register();
        ModRecipes.register();
        DyedSheep.register();
        ModWorldGen.register();
        DataMaps.register();
        CauldronWashing.register();
        // Each kind burns like its vanilla block (see FireBlock.bootStrap).
        FireBlock fire = (FireBlock) Blocks.FIRE;
        for (Kind kind : Kind.values()) {
            int ignite = fire.getIgniteOdds(kind.vanilla().defaultBlockState());
            int burn = fire.getBurnOdds(kind.vanilla().defaultBlockState());
            if (ignite > 0 || burn > 0) {
                for (Block block : ModBlocks.all(kind)) {
                    fire.setFlammable(block, ignite, burn);
                }
            }
        }
        // Optional compat. Its classes refer to the other mod's classes, so they are only touched when it's installed.
        if (FabricLoader.getInstance().isModLoaded(StorageDrawersCompat.MOD_ID) && Toggles.storageDrawers) {
            // The drawers themselves are registered from Storage Drawers' initializer (StorageDrawersBlockEntitiesMixin).
            StorageDrawersCompat.init();
        }
        if (FabricLoader.getInstance().isModLoaded(IronChestCompat.MOD_ID) && Toggles.ironChests) {
            IronChestCompat.register();
        }
        // Turned off in the config: their recipes and tags are then dropped like those of turned-off blocks.
        if (!Toggles.ironChests) {
            for (String tier : new String[]{"iron", "gold", "diamond", "copper"}) {
                Toggles.leftOut(tier + "_chest");
            }
        }
        if (!Toggles.storageDrawers) {
            for (String size : new String[]{"full_drawers_1", "full_drawers_2", "full_drawers_4", "half_drawers_1", "half_drawers_2", "half_drawers_4"}) {
                Toggles.leftOut(size);
            }
        }
        LOGGER.info("More Dyes registered {} colors of {} kinds of block", MixColors.ALL.size(), Kind.values().length);
    }
}

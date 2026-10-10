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
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.handler.CauldronWashing;
import net.neverandy.moredyes.registry.DataMaps;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;
import net.neverandy.moredyes.registry.ModCreativeTabs;
import net.neverandy.moredyes.registry.ModItems;
import net.neverandy.moredyes.registry.ModRecipes;
import net.neverandy.moredyes.world.ModWorldGen;

public class MoreDyes implements ModInitializer {

    public static final String MOD_ID = "moredyes";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        Config.load();
        ModBlocks.register();
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
        if (FabricLoader.getInstance().isModLoaded(StorageDrawersCompat.MOD_ID)) {
            // The drawers themselves are registered from Storage Drawers' initializer (StorageDrawersBlockEntitiesMixin).
            StorageDrawersCompat.init();
        }
        LOGGER.info("More Dyes registered {} colors of {} kinds of block", MixColors.ALL.size(), Kind.values().length);
    }
}

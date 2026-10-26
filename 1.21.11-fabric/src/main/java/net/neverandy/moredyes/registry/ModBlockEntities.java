package net.neverandy.moredyes.registry;

import java.util.stream.Stream;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedBedBlockEntity;
import net.neverandy.moredyes.block.DyedChestBlockEntity;
import net.neverandy.moredyes.block.Kind;

public final class ModBlockEntities {

    /** One type for the dyed chests of every color. */
    public static BlockEntityType<DyedChestBlockEntity> CHEST;

    /**
     * The More Dyes beds, which remember the planks they were made from. Beds placed before that keep vanilla's bed
     * block entity (see register) and an oak frame.
     */
    public static BlockEntityType<DyedBedBlockEntity> BED;

    private ModBlockEntities() {}

    /** Call after {@link ModBlocks#register()}. */
    public static void register() {
        CHEST = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "chest"),
            FabricBlockEntityTypeBuilder.create(DyedChestBlockEntity::new, ModBlocks.all(Kind.CHEST).toArray(Block[]::new))
                .build());
        BED = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "bed"),
            FabricBlockEntityTypeBuilder.create(DyedBedBlockEntity::new, ModBlocks.beds().toArray(Block[]::new)).build());
        // Dyed signs keep their text in a vanilla sign block entity, drawn by DyedSignRenderer. Dyed beds, banners
        // and shulker boxes likewise use vanilla's block entities, drawn by the Dyed*Renderer classes.
        Stream.concat(ModBlocks.all(Kind.SIGN).stream(), ModBlocks.all(Kind.WALL_SIGN).stream())
            .forEach(BlockEntityType.SIGN::addSupportedBlock);
        // The beds in the vanilla colors too: a bed block entity checks its block against vanilla's type when made.
        ModBlocks.beds().forEach(BlockEntityType.BED::addSupportedBlock);
        Stream.concat(ModBlocks.all(Kind.BANNER).stream(), ModBlocks.all(Kind.WALL_BANNER).stream())
            .forEach(BlockEntityType.BANNER::addSupportedBlock);
        ModBlocks.all(Kind.SHULKER_BOX).forEach(BlockEntityType.SHULKER_BOX::addSupportedBlock);
    }
}

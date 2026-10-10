package net.neverandy.moredyes.registry;

import java.util.stream.Stream;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedChestBlockEntity;
import net.neverandy.moredyes.block.Kind;

public final class ModBlockEntities {

    /** One type for the dyed chests of every color. */
    public static BlockEntityType<DyedChestBlockEntity> CHEST;

    private ModBlockEntities() {}

    /** Call after {@link ModBlocks#register()}. */
    public static void register() {
        CHEST = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "chest"),
            FabricBlockEntityTypeBuilder.create(DyedChestBlockEntity::new, ModBlocks.all(Kind.CHEST).toArray(Block[]::new))
                .build());
        // Dyed signs keep their text in a vanilla sign block entity, drawn by DyedSignRenderer.
        Stream.concat(ModBlocks.all(Kind.SIGN).stream(), ModBlocks.all(Kind.WALL_SIGN).stream())
            .forEach(BlockEntityType.SIGN::addSupportedBlock);
    }
}

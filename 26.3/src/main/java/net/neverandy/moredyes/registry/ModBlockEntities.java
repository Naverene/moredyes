package net.neverandy.moredyes.registry;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedChestBlockEntity;
import net.neverandy.moredyes.block.Kind;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
        .create(Registries.BLOCK_ENTITY_TYPE, MoreDyes.MOD_ID);

    /** One type for the dyed chests of every color. */
    public static final Supplier<BlockEntityType<DyedChestBlockEntity>> CHEST = BLOCK_ENTITIES.register("chest",
        () -> new BlockEntityType<>(DyedChestBlockEntity::new, blocks(Kind.CHEST)));

    private ModBlockEntities() {}

    public static void register(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
        modBus.addListener(ModBlockEntities::addSigns);
    }

    /** Dyed signs keep their text in a vanilla sign block entity, so the vanilla renderer draws it. */
    private static void addSigns(BlockEntityTypeAddBlocksEvent event) {
        Block[] signs = Stream.concat(ModBlocks.all(Kind.SIGN).stream(), ModBlocks.all(Kind.WALL_SIGN).stream())
            .map(DeferredBlock::get).toArray(Block[]::new);
        event.modify(BlockEntityTypes.SIGN, signs);
    }

    private static Set<Block> blocks(Kind kind) {
        return ModBlocks.all(kind).stream().map(DeferredBlock::get).collect(Collectors.toUnmodifiableSet());
    }
}

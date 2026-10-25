package net.neverandy.moredyes.registry;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
        () -> BlockEntityType.Builder.of(DyedChestBlockEntity::new, blocks(Kind.CHEST)).build(null));

    private ModBlockEntities() {}

    public static void register(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
        modBus.addListener(ModBlockEntities::addSigns);
    }

    /**
     * Dyed signs keep their text in a vanilla sign block entity, drawn by DyedSignRenderer. Dyed beds, banners and
     * shulker boxes likewise use vanilla's block entities, drawn by the Dyed*Renderer classes.
     */
    private static void addSigns(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, blocks(Kind.SIGN, Kind.WALL_SIGN));
        event.modify(BlockEntityType.BED, blocks(Kind.BED));
        event.modify(BlockEntityType.BANNER, blocks(Kind.BANNER, Kind.WALL_BANNER));
        event.modify(BlockEntityType.SHULKER_BOX, blocks(Kind.SHULKER_BOX));
    }

    private static Block[] blocks(Kind... kinds) {
        return Stream.of(kinds).flatMap(kind -> ModBlocks.all(kind).stream()).map(DeferredBlock::get)
            .toArray(Block[]::new);
    }
}

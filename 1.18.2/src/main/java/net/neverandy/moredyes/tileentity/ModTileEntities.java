package net.neverandy.moredyes.tileentity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;

import java.util.Arrays;
import java.util.Objects;

public final class ModTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MOD_ID);

    /** One tile entity type shared by every dyed chest. */
    public static final RegistryObject<BlockEntityType<MDChestTileEntity>> CHEST = TILE_ENTITIES.register("chest",
            () -> BlockEntityType.Builder.of(MDChestTileEntity::new, present(MDBlock.chestArray)).build(null));

    /** One tile entity type shared by every dyed sign, standing or on a wall. */
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<BlockEntityType<DyedSignBlockEntity>> SIGN = TILE_ENTITIES.register("sign",
            () -> BlockEntityType.Builder.of(DyedSignBlockEntity::new, present(MDBlock.signs())).build(null));

    /** One block entity type shared by every dyed bed, which remembers the planks the bed was made from. */
    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<DyedBedBlockEntity>> BED = TILE_ENTITIES.register("bed",
            () -> BlockEntityType.Builder.of(DyedBedBlockEntity::new, present(MDBlock.beds())).build(null));

    private ModTileEntities() {}

    /** The blocks the config didn't turn off. */
    private static Block[] present(Block[] blocks)
    {
        return Arrays.stream(blocks).filter(Objects::nonNull).toArray(Block[]::new);
    }

    public static void register(IEventBus modBus)
    {
        TILE_ENTITIES.register(modBus);
    }
}

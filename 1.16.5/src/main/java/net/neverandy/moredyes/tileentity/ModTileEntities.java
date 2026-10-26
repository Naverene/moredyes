package net.neverandy.moredyes.tileentity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;

import java.util.Arrays;
import java.util.Objects;

public final class ModTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    /** One tile entity type shared by every dyed chest. */
    public static final RegistryObject<TileEntityType<MDChestTileEntity>> CHEST = TILE_ENTITIES.register("chest",
            () -> TileEntityType.Builder.create(MDChestTileEntity::new, present(MDBlock.chestArray)).build(null));

    /** One tile entity type shared by every dyed sign, standing or on a wall. */
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<DyedSignTileEntity>> SIGN = TILE_ENTITIES.register("sign",
            () -> TileEntityType.Builder.create(DyedSignTileEntity::new, present(MDBlock.signs())).build(null));

    /** One tile entity type shared by every dyed bed, which remembers the planks the bed was made from. */
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<DyedBedTileEntity>> BED = TILE_ENTITIES.register("bed",
            () -> TileEntityType.Builder.create(DyedBedTileEntity::new, present(MDBlock.beds())).build(null));

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

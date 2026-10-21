package net.neverandy.moredyes.tileentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Objects;

public final class ModTileEntities
{
    /** One block entity type shared by every dyed chest. Registered after the chests. */
    public static BlockEntityType<MDChestTileEntity> CHEST;
    /** One block entity type shared by every dyed sign, standing or on a wall. */
    public static BlockEntityType<DyedSignBlockEntity> SIGN;

    private ModTileEntities() {}

    /** The blocks the config didn't turn off. */
    private static Block[] present(Block[] blocks)
    {
        return Arrays.stream(blocks).filter(Objects::nonNull).toArray(Block[]::new);
    }

    public static void register()
    {
        CHEST = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, "chest"),
                FabricBlockEntityTypeBuilder.create(MDChestTileEntity::new, present(MDBlock.chestArray)).build());
        SIGN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, "sign"),
                FabricBlockEntityTypeBuilder.create(DyedSignBlockEntity::new, present(MDBlock.signs())).build());
    }
}

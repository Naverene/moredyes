package net.neverandy.moredyes.tileentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;

public final class ModTileEntities
{
    /** One block entity type shared by every dyed chest. Registered after the chests. */
    public static BlockEntityType<MDChestTileEntity> CHEST;

    private ModTileEntities() {}

    public static void register()
    {
        CHEST = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, "chest"),
                FabricBlockEntityTypeBuilder.create(MDChestTileEntity::new, MDBlock.chestArray).build());
    }
}

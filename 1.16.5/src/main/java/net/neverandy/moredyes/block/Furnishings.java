package net.neverandy.moredyes.block;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.GameData;
import net.neverandy.moredyes.reference.Reference;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Makes the dyed beds, banners and shulker boxes work like vanilla's: they share vanilla's tile entities, villagers
 * sleep in the beds and dispensers place the boxes. Vanilla's cauldron already washes any shulker box and banner.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class Furnishings
{
    private Furnishings() {}

    /** Must run after registration, in common setup's main-thread work. */
    public static void setup()
    {
        addValidBlocks(TileEntityType.BED, MDBlock.bedArray);
        addValidBlocks(TileEntityType.BANNER, MDBlock.bannerArray);
        addValidBlocks(TileEntityType.BANNER, MDBlock.wallBannerArray);
        addValidBlocks(TileEntityType.SHULKER_BOX, MDBlock.shulkerBoxArray);
        for (Block box : MDBlock.shulkerBoxArray)
        {
            if (box != null)
            {
                DispenserBlock.registerDispenseBehavior(box.asItem(), new ShulkerBoxDispenseBehavior());
            }
        }
        addHomes();
    }

    /**
     * Lets a vanilla tile entity type sit on dyed blocks. The game checks a tile entity's block against its type's
     * blocks before drawing it, and Forge has no way to add to them, so the set is replaced.
     */
    private static void addValidBlocks(TileEntityType<?> type, Block[] blocks)
    {
        Field field = ObfuscationReflectionHelper.findField(TileEntityType.class, "field_223046_I");
        try
        {
            @SuppressWarnings("unchecked")
            Set<Block> valid = (Set<Block>) field.get(type);
            ImmutableSet.Builder<Block> builder = ImmutableSet.<Block>builder().addAll(valid);
            for (Block block : blocks)
            {
                if (block != null)
                {
                    builder.add(block);
                }
            }
            field.set(type, builder.build());
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalStateException("Could not add dyed blocks to " + type.getRegistryName(), e);
        }
    }

    /**
     * Villagers take the head of a dyed bed as their home, like a vanilla bed. Forge rebuilds the map from block states
     * to points of interest when it loads registries, so the beds are added again before each server starts.
     */
    private static void addHomes()
    {
        Map<BlockState, PointOfInterestType> map = GameData.getBlockStatePointOfInterestTypeMap();
        for (Block bed : MDBlock.bedArray)
        {
            if (bed == null)
            {
                continue;
            }
            for (BlockState state : bed.getStateContainer().getValidStates())
            {
                if (state.get(BedBlock.PART) == BedPart.HEAD)
                {
                    map.putIfAbsent(state, PointOfInterestType.HOME);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        addHomes();
    }
}

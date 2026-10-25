package net.neverandy.moredyes.block;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.neverandy.moredyes.reference.Reference;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Makes the dyed beds, banners and shulker boxes work like vanilla's: they share vanilla's block entities, villagers
 * sleep in the beds, dispensers place the boxes, and a water cauldron washes the boxes and banners.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class Furnishings
{
    private Furnishings() {}

    /** Must run after registration, in common setup's main-thread work. */
    public static void setup()
    {
        addValidBlocks(BlockEntityType.BED, MDBlock.bedArray);
        addValidBlocks(BlockEntityType.BANNER, MDBlock.bannerArray);
        addValidBlocks(BlockEntityType.BANNER, MDBlock.wallBannerArray);
        addValidBlocks(BlockEntityType.SHULKER_BOX, MDBlock.shulkerBoxArray);
        for (Block box : MDBlock.shulkerBoxArray)
        {
            if (box != null)
            {
                DispenserBlock.registerBehavior(box, new ShulkerBoxDispenseBehavior());
                CauldronInteraction.WATER.put(box.asItem(), CauldronInteraction.SHULKER_BOX);
            }
        }
        for (Block banner : MDBlock.bannerArray)
        {
            if (banner != null)
            {
                // Takes off the last pattern; a plain banner is washed to a white one by CauldronWashing instead.
                CauldronInteraction.WATER.put(banner.asItem(), CauldronInteraction.BANNER);
            }
        }
        addHomes();
    }

    /**
     * Lets a vanilla block entity type sit on dyed blocks. The game checks a block entity's block against its type's
     * blocks before drawing or ticking it, and Forge has no way to add to them, so the set is replaced.
     */
    private static void addValidBlocks(BlockEntityType<?> type, Block[] blocks)
    {
        Field field = ObfuscationReflectionHelper.findField(BlockEntityType.class, "f_58915_");
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
            throw new IllegalStateException("Could not add dyed blocks to " + ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(type), e);
        }
    }

    /**
     * Villagers take the head of a dyed bed as their home, like a vanilla bed. Forge rebuilds the map from block states
     * to points of interest when it loads registries, so the beds are added again before each server starts.
     */
    private static void addHomes()
    {
        PoiType home = ForgeRegistries.POI_TYPES.getValue(PoiTypes.HOME.location());
        if (home == null)
        {
            return;
        }
        Map<BlockState, PoiType> map = GameData.getBlockStatePointOfInterestTypeMap();
        for (Block bed : MDBlock.bedArray)
        {
            if (bed == null)
            {
                continue;
            }
            for (BlockState state : bed.getStateDefinition().getPossibleStates())
            {
                if (state.getValue(BedBlock.PART) == BedPart.HEAD)
                {
                    map.putIfAbsent(state, home);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event)
    {
        addHomes();
    }
}

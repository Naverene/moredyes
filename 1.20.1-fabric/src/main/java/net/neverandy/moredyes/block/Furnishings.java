package net.neverandy.moredyes.block;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.HashSet;
import java.util.Set;

/**
 * Makes the dyed beds, banners and shulker boxes work like vanilla's: they share vanilla's block entities, villagers
 * sleep in the beds, dispensers place the boxes, and a water cauldron washes the boxes and banners.
 */
public final class Furnishings
{
    private Furnishings() {}

    /** Must run after the blocks are registered. */
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
     * blocks before drawing or ticking it, so the set (made accessible by moredyes.accesswidener) is replaced.
     */
    private static void addValidBlocks(BlockEntityType<?> type, Block[] blocks)
    {
        ImmutableSet.Builder<Block> builder = ImmutableSet.<Block>builder().addAll(type.validBlocks);
        for (Block block : blocks)
        {
            if (block != null)
            {
                builder.add(block);
            }
        }
        type.validBlocks = builder.build();
    }

    /** Villagers take the head of a dyed bed as their home, like a vanilla bed. */
    private static void addHomes()
    {
        Set<BlockState> heads = new HashSet<>();
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
                    heads.add(state);
                }
            }
        }
        PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiTypes.HOME), heads);
    }
}

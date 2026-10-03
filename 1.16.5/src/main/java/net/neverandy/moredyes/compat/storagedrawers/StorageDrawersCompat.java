package net.neverandy.moredyes.compat.storagedrawers;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dyed Storage Drawers, in every More Dyes color. Each drawer size is one block whose color is a block state property,
 * so there are six blocks rather than six times 118. They are Storage Drawers' own standard drawers with a grey model
 * tinted in the dye color, and they use its tile entities, so labels, hoppers, keys, upgrades and controllers all work.
 *
 * <p>Storage Drawers 8.5 for 1.16.5 has no way for another mod to add drawers: its tile entity types list its own blocks,
 * and its client fills in label positions and models for its own blocks only. Minecraft only ticks and draws a tile
 * entity whose type lists the block it is in, so our blocks are added to those lists here (by reflection, as the list is
 * private), and client/StorageDrawersClient does the rest.
 *
 * <p>Only loaded when Storage Drawers is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class StorageDrawersCompat
{
    public static final String MOD_ID = "storagedrawers";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    static final List<RegistryObject<DyedDrawersBlock>> DRAWERS = new ArrayList<>();
    static final List<RegistryObject<DyedDrawersItem>> DRAWER_ITEMS = new ArrayList<>();

    private StorageDrawersCompat() {}

    public static void register(IEventBus modBus)
    {
        for (String name : Reference.DRAWER_SIZES)
        {
            int count = name.charAt(name.length() - 1) - '0';
            boolean half = name.startsWith("half");
            RegistryObject<DyedDrawersBlock> block = BLOCKS.register("dyed_" + name, () -> new DyedDrawersBlock(name, count, half,
                    // The same as Storage Drawers' own wooden drawers.
                    AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(5.0F)
                            .setSuffocates((state, world, pos) -> false).setOpaque((state, world, pos) -> false)));
            DRAWERS.add(block);
            DRAWER_ITEMS.add(ITEMS.register("dyed_" + name, () -> new DyedDrawersItem(block.get(), new Item.Properties().group(MoreDyes.tabBlocks))));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(StorageDrawersCompat::setup);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            StorageDrawersClient.register(modBus);
        }
    }

    private static void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(StorageDrawersCompat::joinTileEntityTypes);
    }

    /** Adds each drawer block to the Storage Drawers tile entity type for its drawer count. */
    private static void joinTileEntityTypes()
    {
        Field validBlocks = ObfuscationReflectionHelper.findField(TileEntityType.class, "field_223046_I_");
        for (int count : new int[]{1, 2, 4})
        {
            ResourceLocation id = new ResourceLocation(MOD_ID, "standard_drawers_" + count);
            TileEntityType<?> type = ForgeRegistries.TILE_ENTITIES.getValue(id);
            if (type == null)
            {
                MoreDyes.LOGGER.error("Storage Drawers has no tile entity type {}, so dyed drawers with {} drawers won't work", id, count);
                continue;
            }
            List<Block> ours = blocks().stream().filter(block -> block.getDrawerCount() == count).collect(Collectors.toList());
            try
            {
                @SuppressWarnings("unchecked")
                Set<Block> blocks = (Set<Block>) validBlocks.get(type);
                validBlocks.set(type, ImmutableSet.<Block>builder().addAll(blocks).addAll(ours).build());
            }
            catch (IllegalAccessException e)
            {
                MoreDyes.LOGGER.error("Couldn't add dyed drawers to {}", id, e);
            }
        }
    }

    /** A dyed drawer item in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Item item, int color)
    {
        ItemStack stack = new ItemStack(item);
        stack.getOrCreateChildTag("BlockStateTag").putString(DyedDrawersBlock.COLOR.getName(), Integer.toString(color));
        return stack;
    }

    /** The color of a dyed drawer item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundNBT tag = stack.getChildTag("BlockStateTag");
        if (tag == null)
        {
            return 0;
        }
        try
        {
            int color = Integer.parseInt(tag.getString(DyedDrawersBlock.COLOR.getName()));
            return color >= 0 && color < ColorStrings.ALL.length ? color : 0;
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static List<DyedDrawersBlock> blocks()
    {
        return DRAWERS.stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    public static List<DyedDrawersItem> items()
    {
        return DRAWER_ITEMS.stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}

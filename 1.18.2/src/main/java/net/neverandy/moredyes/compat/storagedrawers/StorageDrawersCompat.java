package net.neverandy.moredyes.compat.storagedrawers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Dyed Storage Drawers, in every More Dyes color. Each drawer size is one block whose color is a block state property,
 * so there are six blocks rather than six times 118. They are Storage Drawers' own standard drawers with a grey model
 * tinted in the dye color, and they share its block entities, so labels, hoppers, pipes and controllers all work
 * (see DyedDrawersBlock for how Storage Drawers finds them).
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
            RegistryObject<DyedDrawersBlock> block = BLOCKS.register("dyed_" + name, () -> new DyedDrawersBlock(count, half,
                    // The same as Storage Drawers' own wooden drawers (ModBlocks.getWoodenDrawerBlockProperties).
                    BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(3.0F, 4.0F)
                            .isSuffocating((state, level, pos) -> false).isRedstoneConductor((state, level, pos) -> false)));
            DRAWERS.add(block);
            // Listed in every color at the end of the More Dyes blocks tab (DyedDrawersItem.fillItemCategory).
            DRAWER_ITEMS.add(ITEMS.register("dyed_" + name, () -> new DyedDrawersItem(block.get(), new Item.Properties().tab(MoreDyes.tabBlocks))));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            StorageDrawersClient.register(modBus);
        }
    }

    /** The stack with its BlockStateTag set to a color, by its position in ColorStrings.ALL. */
    public static ItemStack withColor(ItemStack stack, int color)
    {
        CompoundTag state = new CompoundTag();
        state.putString(DyedDrawersBlock.COLOR.getName(), Integer.toString(color));
        stack.addTagElement("BlockStateTag", state);
        return stack;
    }

    /** The color of a dyed drawer item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundTag tag = stack.getTagElement("BlockStateTag");
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
        return DRAWERS.stream().map(RegistryObject::get).toList();
    }

    public static List<DyedDrawersItem> items()
    {
        return DRAWER_ITEMS.stream().map(RegistryObject::get).toList();
    }
}

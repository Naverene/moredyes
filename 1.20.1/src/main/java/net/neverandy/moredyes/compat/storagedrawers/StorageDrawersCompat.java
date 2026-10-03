package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.texelsaurus.minecraft.chameleon.api.ChameleonInit;
import com.texelsaurus.minecraft.chameleon.registry.ChameleonRegistry;
import com.texelsaurus.minecraft.chameleon.registry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Dyed Storage Drawers, in every More Dyes color. Each drawer size is one block whose color is a block state property,
 * so there are six blocks rather than six times 118. They are Storage Drawers' own standard drawers with a grey model
 * tinted in the dye color, and they share its block entities, so labels, hoppers, pipes and controllers all work.
 *
 * <p>Only loaded when Storage Drawers is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class StorageDrawersCompat
{
    public static final String MOD_ID = "storagedrawers";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    /** Every drawer block, for Storage Drawers to find (see BlockList). */
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
                    // The same as Storage Drawers' own wooden drawers.
                    BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(3.0F, 4.0F)
                            .isSuffocating((state, level, pos) -> false).isRedstoneConductor((state, level, pos) -> false)));
            DRAWERS.add(block);
            DRAWER_ITEMS.add(ITEMS.register("dyed_" + name, () -> new DyedDrawersItem(block.get(), new Item.Properties())));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        // Storage Drawers builds its block entity types, label positions and models from every drawer block in its own
        // registry and in the ones added here, so ours get its renderer and hopper and controller support.
        ModBlocks.tryAddExternalRegistry(new BlockList());
        modBus.addListener(StorageDrawersCompat::creativeTab);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            StorageDrawersClient.register(modBus);
        }
    }

    /** A dyed drawer item in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Item item, int color)
    {
        ItemStack stack = new ItemStack(item);
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

    /** Lists every color of every size at the end of the More Dyes blocks tab. */
    private static void creativeTab(BuildCreativeModeTabContentsEvent event)
    {
        ResourceKey<CreativeModeTab> blocks = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(Reference.MOD_ID, "blocks"));
        if (event.getTabKey() == blocks)
        {
            for (RegistryObject<DyedDrawersItem> item : DRAWER_ITEMS)
            {
                for (int color = 0; color < ColorStrings.ALL.length; color++)
                {
                    event.accept(stack(item.get(), color));
                }
            }
        }
    }

    /**
     * Our drawer blocks, in the shape of Storage Drawers' own registry so it can list them. They are registered through
     * Forge as usual; Storage Drawers only reads them back.
     */
    private static final class BlockList implements ChameleonRegistry<Block>
    {
        @Override
        public <C extends Block> RegistryEntry<C> register(String id, Supplier<C> supplier)
        {
            throw new UnsupportedOperationException("More Dyes registers its drawers itself");
        }

        @Override
        public Collection<RegistryEntry<Block>> getEntries()
        {
            List<RegistryEntry<Block>> entries = new ArrayList<>();
            for (RegistryObject<DyedDrawersBlock> drawer : DRAWERS)
            {
                entries.add(new RegistryEntry<>()
                {
                    @Override
                    public ResourceLocation getId()
                    {
                        return drawer.getId();
                    }

                    @Override
                    public Block get()
                    {
                        return drawer.get();
                    }
                });
            }
            return entries;
        }

        @Override
        public void init(ChameleonInit.InitContext context)
        {
        }
    }
}

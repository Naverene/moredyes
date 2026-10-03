package net.neverandy.moredyes.compat.storagedrawers;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.storagedrawers.api.config.IDrawerConfig;
import com.jaquadro.minecraft.storagedrawers.config.ModCommonConfig;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColors;

/**
 * Dyed Storage Drawers, in every More Dyes color. Each drawer size is one block whose color is a block state property
 * (see {@link DyedDrawersBlock#COLOR}), so there are six blocks rather than six times 118. They are Storage Drawers'
 * own standard drawers with a grey model tinted in the dye color. {@code StorageDrawersModBlocksMixin} adds them to
 * Storage Drawers' list of drawer blocks, so they share its block entity types (hoppers, controllers and labels work)
 * and get its label positions and model overlays.
 *
 * <p>
 * Only loaded when Storage Drawers is installed (see MoreDyes), so nothing outside this package may refer to it.
 */
public final class StorageDrawersCompat {

    public static final String MOD_ID = "storagedrawers";

    /**
     * The drawer sizes that have dyed versions, named as Storage Drawers names them. Also the registry names of the
     * dyed drawers ({@code moredyes:full_drawers_1}...); {@code tools/generate_resources.py} reads them from here.
     */
    public static final List<String> SIZES = List.of("full_drawers_1", "full_drawers_2", "full_drawers_4",
        "half_drawers_1", "half_drawers_2", "half_drawers_4");

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreDyes.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreDyes.MOD_ID);
    private static final List<DeferredBlock<DyedDrawersBlock>> DRAWERS = new ArrayList<>();
    private static final List<DeferredItem<DyedDrawersItem>> DRAWER_ITEMS = new ArrayList<>();

    private StorageDrawersCompat() {}

    public static void register(IEventBus modBus) {
        for (String size : SIZES) {
            int count = size.charAt(size.length() - 1) - '0';
            boolean half = size.startsWith("half");
            // The same properties and capacity settings as Storage Drawers' own wooden drawers.
            DeferredBlock<DyedDrawersBlock> block = BLOCKS.registerBlock(size,
                p -> new DyedDrawersBlock(count, half, config(count, half), p),
                () -> BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(3.0F, 4.0F)
                    .isSuffocating((state, level, pos) -> false).isRedstoneConductor((state, level, pos) -> false));
            DRAWERS.add(block);
            DRAWER_ITEMS.add(ITEMS.registerItem(size, p -> new DyedDrawersItem(block.get(), p.useBlockDescriptionPrefix())));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(StorageDrawersCompat::creativeTab);
    }

    private static IDrawerConfig config(int count, boolean half) {
        ModCommonConfig.Drawers drawers = ModCommonConfig.INSTANCE.DRAWERS;
        return switch (count) {
            case 1 -> half ? drawers.halfDrawers1x1 : drawers.fullDrawers1x1;
            case 2 -> half ? drawers.halfDrawers1x2 : drawers.fullDrawers1x2;
            default -> half ? drawers.halfDrawers2x2 : drawers.fullDrawers2x2;
        };
    }

    /** Every dyed drawer block, in the order of {@link #SIZES}. */
    public static List<DyedDrawersBlock> blocks() {
        return DRAWERS.stream().map(DeferredBlock::get).toList();
    }

    public static List<DyedDrawersItem> items() {
        return DRAWER_ITEMS.stream().map(DeferredItem::get).toList();
    }

    /** A dyed drawer item in a color, by its position in {@link MixColors#ALL}. */
    public static ItemStack stack(Item item, int color) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(DyedDrawersBlock.COLOR, color));
        return stack;
    }

    /** The color of a dyed drawer item, by its position in {@link MixColors#ALL}, or 0 if it has none. */
    public static int colorOf(ItemStack stack) {
        Integer color = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY)
            .get(DyedDrawersBlock.COLOR);
        return color != null ? color : 0;
    }

    /** Lists every color of every size at the end of the More Dyes blocks tab. */
    private static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> blocks = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "blocks"));
        if (blocks.equals(event.getTabKey())) {
            for (DeferredItem<DyedDrawersItem> item : DRAWER_ITEMS) {
                for (int color = 0; color < MixColors.ALL.size(); color++) {
                    event.accept(stack(item.get(), color));
                }
            }
        }
    }
}

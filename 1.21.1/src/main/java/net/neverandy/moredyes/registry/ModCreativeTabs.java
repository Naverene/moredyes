package net.neverandy.moredyes.registry;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;

/** The creative tabs: Dyes, Blocks, Trees and Plants from the original mod, and Slabs and Stairs. */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
        .create(Registries.CREATIVE_MODE_TAB, MoreDyes.MOD_ID);

    private static final MixColor ICON_COLOR = MixColors.byHex("c56685").orElseThrow();

    static {
        TABS.register("dyes", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.moredyes.dyes"))
            .icon(() -> new ItemStack(ModItems.dye(ICON_COLOR).get()))
            .displayItems((parameters, output) -> MixColors.ALL.forEach(color -> output.accept(ModItems.dye(color).get())))
            .build());
        register("blocks", Kind.Tab.BLOCKS, Kind.BRICKS);
        register("trees", Kind.Tab.TREES, Kind.OAK_SAPLING);
        register("plants", Kind.Tab.PLANTS, Kind.TULIP);
        register("shapes", Kind.Tab.SHAPES, Kind.STONE_BRICK_STAIRS);
    }

    private ModCreativeTabs() {}

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }

    /**
     * A tab listing every block of the kinds in {@code tab}, grouped by kind and then by color. Its icon is the
     * {@code icon} kind, or the first kind the config kept. A tab whose kinds are all turned off is left out.
     */
    private static void register(String name, Kind.Tab tab, Kind icon) {
        List<Kind> kinds = Arrays.stream(Kind.values()).filter(kind -> kind.tab() == tab && kind.enabled()).toList();
        if (kinds.isEmpty()) {
            return;
        }
        Kind iconKind = kinds.contains(icon) ? icon : kinds.get(0);
        TABS.register(name, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.moredyes." + name))
            .icon(() -> new ItemStack(ModItems.get(iconKind, ICON_COLOR).get()))
            .displayItems((parameters, output) -> {
                for (Kind kind : kinds) {
                    MixColors.ALL.forEach(color -> output.accept(ModItems.get(kind, color).get()));
                }
            })
            .build());
    }
}

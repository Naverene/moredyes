package net.neverandy.moredyes.registry;

import java.util.Arrays;
import java.util.List;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;

/** The creative tabs: Dyes, Blocks, Trees and Plants from the original mod, and Slabs and Stairs. */
public final class ModCreativeTabs {

    private static final MixColor ICON_COLOR = MixColors.byHex("c56685").orElseThrow();

    private ModCreativeTabs() {}

    /** Call after {@link ModItems#register()}. */
    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyes"),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.moredyes.dyes"))
                .icon(() -> new ItemStack(ModItems.dye(ICON_COLOR)))
                .displayItems((parameters, output) -> MixColors.ALL.forEach(color -> output.accept(ModItems.dye(color))))
                .build());
        register("blocks", Kind.Tab.BLOCKS, Kind.BRICKS);
        register("trees", Kind.Tab.TREES, Kind.OAK_SAPLING);
        register("plants", Kind.Tab.PLANTS, Kind.TULIP);
        register("shapes", Kind.Tab.SHAPES, Kind.STONE_BRICK_STAIRS);
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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, name),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.moredyes." + name))
                .icon(() -> new ItemStack(ModItems.get(iconKind, ICON_COLOR)))
                .displayItems((parameters, output) -> {
                    for (Kind kind : kinds) {
                        MixColors.ALL.forEach(color -> output.accept(ModItems.get(kind, color)));
                    }
                })
                .build());
    }
}

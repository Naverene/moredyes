package net.neverandy.moredyes.registry;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyes"),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.moredyes.dyes"))
                .icon(() -> new ItemStack(ModItems.dye(ICON_COLOR)))
                .displayItems((parameters, output) -> MixColors.ALL.forEach(color -> output.accept(ModItems.dye(color))))
                .build());
        register("blocks", Kind.Tab.BLOCKS, () -> ModItems.get(Kind.BRICKS, ICON_COLOR));
        register("trees", Kind.Tab.TREES, () -> ModItems.get(Kind.OAK_SAPLING, ICON_COLOR));
        register("plants", Kind.Tab.PLANTS, () -> ModItems.get(Kind.TULIP, ICON_COLOR));
        register("shapes", Kind.Tab.SHAPES, () -> ModItems.get(Kind.STONE_BRICK_STAIRS, ICON_COLOR));
    }

    /** A tab listing every block of the kinds in {@code tab}, grouped by kind and then by color. */
    private static void register(String name, Kind.Tab tab, Supplier<Item> icon) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, name),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.moredyes." + name))
                .icon(() -> new ItemStack(icon.get()))
                .displayItems((parameters, output) -> {
                    for (Kind kind : Kind.values()) {
                        if (kind.tab() == tab) {
                            MixColors.ALL.forEach(color -> output.accept(ModItems.get(kind, color)));
                        }
                    }
                })
                .build());
    }
}

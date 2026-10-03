package net.neverandy.moredyes.compat.ae2;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import net.neverandy.moredyes.MoreDyes;

/**
 * Applied Energistics 2's cables, paint balls and Color Applicator only know the 16 vanilla colors, so each More Dyes
 * dye counts as the vanilla color it looks closest to. The dyes are in the item tags moredyes:dyes/&lt;vanilla
 * color&gt;, and recipes like AE2's own make colored cables and paint balls from them (both from
 * tools/generate_resources.py). The Color Applicator finds a dye's color through a private map from dye tag to AE2
 * color; this adds our tags to it, so it takes our dyes too.
 *
 * <p>
 * AE2 is optional and More Dyes doesn't compile against it: this only uses reflection, and only runs when AE2 is
 * installed (see MoreDyes). If AE2 changes, the Color Applicator just won't take our dyes; nothing else breaks.
 */
public final class AE2Compat {

    public static final String MOD_ID = "ae2";

    private AE2Compat() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(AE2Compat::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(AE2Compat::addColorApplicatorDyes);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addColorApplicatorDyes() {
        try {
            Class<? extends Enum> aeColor = (Class<? extends Enum>) Class.forName("appeng.api.util.AEColor");
            Field field = Class.forName("appeng.items.tools.powered.ColorApplicatorItem")
                    .getDeclaredField("TAG_TO_COLOR");
            field.setAccessible(true);
            Map<TagKey<Item>, Object> tagToColor = (Map<TagKey<Item>, Object>) field.get(null);
            for (DyeColor color : DyeColor.values()) {
                String name = color.getSerializedName();
                tagToColor.put(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID,
                        "dyes/" + name)), Enum.valueOf(aeColor, name.toUpperCase(Locale.ROOT)));
            }
            MoreDyes.LOGGER.info("Applied Energistics 2's Color Applicator takes More Dyes dyes");
        } catch (ReflectiveOperationException | RuntimeException e) {
            MoreDyes.LOGGER.warn("Couldn't add More Dyes dyes to Applied Energistics 2's Color Applicator", e);
        }
    }
}

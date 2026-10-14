package net.neverandy.moredyes.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.paint.PaintGunItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/** The dyes, one per color, named "<color>_dye" such as "334c59_dye", and the paint gun. */
public class MDItem
{
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final MDItemDye[] dye = new MDItemDye[ColorStrings.ALL.length];
    public static final RegistryObject<PaintGunItem> PAINT_GUN = ITEMS.register("paint_gun", () -> new PaintGunItem(new Item.Properties()));

    public static void register(IEventBus modBus)
    {
        ITEMS.register(modBus);
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            final int color = i;
            RegistryObject<MDItemDye> item = ITEMS.register(ColorStrings.ALL[i] + "_dye", () -> dye[color] = new MDItemDye(color, new Item.Properties()));
            MDTabs.DYES.add(item);
        }
        MDTabs.DYES.add(PAINT_GUN);
    }
}

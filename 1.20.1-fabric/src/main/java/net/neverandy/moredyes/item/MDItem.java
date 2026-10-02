package net.neverandy.moredyes.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neverandy.moredyes.block.BlockChest;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/** The dyes, one per color, named "<color>_dye" such as "334c59_dye". */
public class MDItem
{
    public static final MDItemDye[] dye = new MDItemDye[ColorStrings.ALL.length];

    public static void register()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            dye[i] = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Reference.MOD_ID, ColorStrings.ALL[i] + "_dye"),
                    new MDItemDye(i, new Item.Properties()));
            MDTabs.DYES.add(dye[i]);
        }
        // Dyed chests burn as long as a vanilla chest.
        for (BlockChest chest : MDBlock.chestArray)
        {
            FuelRegistry.INSTANCE.add(chest, 300);
        }
    }
}

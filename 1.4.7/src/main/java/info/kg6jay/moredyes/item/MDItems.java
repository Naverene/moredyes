package info.kg6jay.moredyes.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public final class MDItems {

    public static Item dye;

    private MDItems() {}

    public static void init(Configuration config) {
        dye = new ItemMoreDye(config.getItem("dye", 25600).getInt());
        // Any recipe that takes "dye" accepts a More Dyes dye.
        OreDictionary.registerOre("dye", new ItemStack(dye, 1, -1));
    }
}

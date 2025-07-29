package info.kg6jay.moredyes.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.block.MDBlock;

public class MDItem {

    public static Item[] dye;

    public static void initialize() {
        dye = new Item[MDBlock.colors.length];
        for (int i = 0; i < MDBlock.colors.length; i++) {
            dye[i] = new MDItemDye(MDBlock.colorStrings[i], MDBlock.colors[i]);
        }
    }

    public static void register() {
        for (Item item : dye) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }
    }

    public static void registerOreDict() {

    }
}

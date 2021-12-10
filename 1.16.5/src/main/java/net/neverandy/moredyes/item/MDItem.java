package net.neverandy.moredyes.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;

public class MDItem
{
    public static Item[] dye;
    public static void initialize()
    {
        dye=new Item[8];
        dye[0]=new MDItemDye(ColorStrings.SET_0,ColorStrings.SET_0.length,0);
        dye[1]=new MDItemDye(ColorStrings.SET_1,ColorStrings.SET_1.length,1);
        dye[2]=new MDItemDye(ColorStrings.SET_2,ColorStrings.SET_2.length,2);
        dye[3]=new MDItemDye(ColorStrings.SET_3,ColorStrings.SET_3.length,3);
        dye[4]=new MDItemDye(ColorStrings.SET_4,ColorStrings.SET_4.length,4);
        dye[5]=new MDItemDye(ColorStrings.SET_5,ColorStrings.SET_5.length,5);
        dye[6]=new MDItemDye(ColorStrings.SET_6,ColorStrings.SET_6.length,6);
        dye[7]=new MDItemDye(ColorStrings.SET_7,ColorStrings.SET_7.length,7);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(dye);
        registry.registerAll(MDBlock.log[0].asItem());
    }
}
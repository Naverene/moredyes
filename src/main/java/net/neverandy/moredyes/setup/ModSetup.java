package net.neverandy.moredyes.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup tabTrees = new ItemGroup("trees")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(MDBlock.logArray[78]);
        }
    };
    public static final ItemGroup tabPlants = new ItemGroup("plants")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MDBlock.saplingArray[56]);
        }
    };
    public static final ItemGroup tabDyes = new ItemGroup("dyes")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MDItem.dye[99]);
        }
    };
    public static final ItemGroup tabBlocks = new ItemGroup("blocks")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MDBlock.brickArray[100]);
        }
    };

    @SubscribeEvent
    public static void serverLoad(RegisterCommandsEvent event)
    {
        //ModCommands.register(event.getDispatcher());
    }
}
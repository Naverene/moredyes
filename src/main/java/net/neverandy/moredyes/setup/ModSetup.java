package net.neverandy.moredyes.setup;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neverandy.moredyes.reference.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup tabTrees = new ItemGroup(Reference.MOD_NAME + " Trees")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Blocks.OAK_LOG);
        }
    };
    public static final ItemGroup tabPlants = new ItemGroup(Reference.MOD_NAME + " Plants")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.ACACIA_SAPLING);
        }
    };
    public static final ItemGroup tabDyes = new ItemGroup(Reference.MOD_NAME + " Dyes")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.LAPIS_LAZULI);
        }
    };
    public static final ItemGroup tabBlocks = new ItemGroup(Reference.MOD_NAME + " Blocks")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.DIAMOND);
        }
    };

    public static void init(final FMLCommonSetupEvent event)
    {
        /*
        Networking.registerMessages();
        CapabilityEntityCharge.register();

        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(Registration.WEIRDMOB.get(), WeirdMobEntity.prepareAttributes().build());

            Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MyTutorial.MODID, "chunkgen"),
                    TutorialChunkGenerator.CODEC);
            Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MyTutorial.MODID, "biomes"),
                    TutorialBiomeProvider.CODEC);
        });

         */
    }

    @SubscribeEvent
    public static void serverLoad(RegisterCommandsEvent event)
    {
        //ModCommands.register(event.getDispatcher());
    }
}
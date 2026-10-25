package net.neverandy.moredyes;

import net.minecraft.world.entity.EntityType;
import net.neverandy.moredyes.client.DyedPistonRenderer;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.client.ChestRenderer;
import net.neverandy.moredyes.tileentity.ModTileEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.neverandy.moredyes.toggle.Toggles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neverandy.moredyes.block.BlockGlass;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.neverandy.moredyes.block.DyedShapes;
import net.neverandy.moredyes.data.condition.WallsEnabledCondition;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.crafting.ModRecipes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.world.ModWorldGen;
import net.neverandy.moredyes.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MOD_ID)
public class MoreDyes
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab tabTrees = new CreativeModeTab("trees")
    {
        @Override
        public ItemStack makeIcon()
        {
            return icon(MDBlock.darkOakLogArray, 78);
        }
    };
    public static final CreativeModeTab tabPlants = new CreativeModeTab("plants")
    {
        @Override
        public ItemStack makeIcon() {
            return icon(MDBlock.oakSaplingArray, 56);
        }
    };
    public static final CreativeModeTab tabDyes = new CreativeModeTab("dyes")
    {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(MDItem.dye[99]);
        }
    };
    public static final CreativeModeTab tabBlocks = new CreativeModeTab("blocks")
    {
        @Override
        public ItemStack makeIcon() {
            return icon(MDBlock.brickArray, 100);
        }
    };
    public static final CreativeModeTab tabShapes = new CreativeModeTab("shapes")
    {
        @Override
        public ItemStack makeIcon() {
            for (DyedShapes shapes : DyedShapes.ALL)
            {
                for (Block[] blocks : new Block[][]{shapes.stairs, shapes.slabs, shapes.walls})
                {
                    if (blocks.length > 0 && blocks[100] != null)
                    {
                        return new ItemStack(blocks[100]);
                    }
                }
            }
            return new ItemStack(Items.WHITE_DYE);
        }
    };

    /** A tab's icon: the block at {@code index}, or another one when the config turned that off. */
    private static ItemStack icon(Block[] blocks, int index)
    {
        if (blocks[index] != null)
        {
            return new ItemStack(blocks[index]);
        }
        for (Block block : blocks)
        {
            if (block != null)
            {
                return new ItemStack(block);
            }
        }
        return new ItemStack(Items.WHITE_DYE);
    }

    /**
     * Sets a block's render layer, skipping blocks the config turned off (null). The layer is passed as an Object so
     * that this class doesn't name RenderType, a client class, in a method signature, which servers can't load.
     */
    private static void layer(Block block, Object type)
    {
        if (block != null)
        {
            ItemBlockRenderTypes.setRenderLayer(block, (RenderType) type);
        }
    }

    public MoreDyes()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.SERVER_CONFIG);
        CraftingHelper.register(WallsEnabledCondition.SERIALIZER);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //RegistryHandler.init();
        MDBlock.initialize();
        // The blocks are made when block registration starts, so unknown names in the config are reported then.
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, EventPriority.LOW,
                (RegistryEvent.Register<Block> event) -> warnUnknownTypes());
        MDItem.initialize();
        ModWorldGen.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModTileEntities.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRecipes.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(ModWorldGen::onBiomeLoading);

        // Optional compat. The compat classes refer to the other mod's classes, so they are only touched when it's installed.
        if (ModList.get().isLoaded("ironchest") && Toggles.ironChests)
        {
            IronChestCompat.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
        if (ModList.get().isLoaded("storagedrawers") && Toggles.storageDrawers)
        {
            StorageDrawersCompat.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
        // Turned off in the config: their recipes and tags are then dropped like those of turned-off blocks.
        if (!Toggles.ironChests)
        {
            for (String tier : Reference.IRON_CHEST_TIERS)
            {
                Toggles.leftOut("dyed_" + tier + "_chest");
            }
        }
        if (!Toggles.storageDrawers)
        {
            for (String size : Reference.DRAWER_SIZES)
            {
                Toggles.leftOut("dyed_" + size);
            }
        }
    }

    /** Names in the config's disabled_blocks that match no block type, likely typos. */
    private static void warnUnknownTypes()
    {
        if (!Toggles.unknownTypes().isEmpty())
        {
            LOGGER.warn("disabled_blocks in moredyes-client.toml names {}, which aren't More Dyes block types. The types are: {}",
                    Toggles.unknownTypes(), Toggles.knownTypes());
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModNetwork.register();
        event.enqueueWork(ModWorldGen::setup);
        event.enqueueWork(MDBlock::registerPottedPlants);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        for (BlockGlass block: MDBlock.glassArray)
        {
            event.enqueueWork(() -> layer(block, RenderType.cutout()));
        }
        event.enqueueWork(() ->
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                layer(MDBlock.glassPaneArray[i], RenderType.cutoutMipped());
                // Foggy glass is half see-through, so it needs the translucent layer.
                layer(MDBlock.glassFoggyArray[i], RenderType.translucent());
                layer(MDBlock.glassFoggyPaneArray[i], RenderType.translucent());
            }
        });
        event.enqueueWork(() ->
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                layer(MDBlock.tulipArray[i], RenderType.cutout());
                layer(MDBlock.oakSaplingArray[i], RenderType.cutout());
                layer(MDBlock.birchSaplingArray[i], RenderType.cutout());
                layer(MDBlock.acaciaSaplingArray[i], RenderType.cutout());
                layer(MDBlock.darkOakSaplingArray[i], RenderType.cutout());
                layer(MDBlock.jungleSaplingArray[i], RenderType.cutout());
                layer(MDBlock.spruceSaplingArray[i], RenderType.cutout());
                layer(MDBlock.oakLeafArray[i], RenderType.cutoutMipped());
                layer(MDBlock.birchLeafArray[i], RenderType.cutoutMipped());
                layer(MDBlock.acaciaLeafArray[i], RenderType.cutoutMipped());
                layer(MDBlock.darkOakLeafArray[i], RenderType.cutoutMipped());
                layer(MDBlock.jungleLeafArray[i], RenderType.cutoutMipped());
                layer(MDBlock.spruceLafArray[i], RenderType.cutoutMipped());
                // The wood and iron on a dyed piston are an untinted layer drawn over the tinted cobblestone, with see-through gaps.
                layer(MDBlock.stickyPistonArray[i], RenderType.cutout());
                layer(MDBlock.pistonArray[i], RenderType.cutout());
                layer(MDBlock.iceArray[i], RenderType.translucent());
                for (Block[] flowers : MDBlock.smallFlowerArrays)
                {
                    layer(flowers[i], RenderType.cutout());
                }
                for (Block[] flowers : MDBlock.tallFlowerArrays)
                {
                    layer(flowers[i], RenderType.cutout());
                }
                layer(MDBlock.chainArray[i], RenderType.cutout());
                for (MDBlock.PottedPlants potted : MDBlock.POTTED)
                {
                    layer(potted.pots[i], RenderType.cutout());
                }
            }
            for (DyedShapes shapes : DyedShapes.ALL)
            {
                if (shapes.layer == DyedShapes.Layer.SOLID)
                {
                    continue;
                }
                RenderType layer = shapes.layer == DyedShapes.Layer.CUTOUT ? RenderType.cutout() : RenderType.translucent();
                for (Block[] blocks : new Block[][]{shapes.slabs, shapes.stairs, shapes.walls})
                {
                    for (Block block : blocks)
                    {
                        layer(block, layer);
                    }
                }
            }
        });

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(Reference.MOD_ID, "helloworld", () ->
        {
            LOGGER.info("Hello world from the MDK");
    
            

            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
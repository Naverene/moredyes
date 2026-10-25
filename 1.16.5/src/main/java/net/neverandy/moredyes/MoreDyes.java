package net.neverandy.moredyes;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.EntityType;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.client.DyedPistonRenderer;
import net.neverandy.moredyes.client.DyedSheepRenderer;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.client.ChestRenderer;
import net.neverandy.moredyes.client.DyedSignRenderer;
import net.neverandy.moredyes.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.neverandy.moredyes.toggle.Toggles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neverandy.moredyes.block.BlockGlass;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.neverandy.moredyes.block.DyedShapes;
import net.neverandy.moredyes.data.condition.WallsEnabledCondition;
import net.neverandy.moredyes.block.Furnishings;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
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
    public static final ItemGroup tabTrees = new ItemGroup("trees")
    {
        @Override
        public ItemStack createIcon()
        {
            return icon(MDBlock.darkOakLogArray, 78);
        }
    };
    public static final ItemGroup tabPlants = new ItemGroup("plants")
    {
        @Override
        public ItemStack createIcon() {
            return icon(MDBlock.oakSaplingArray, 56);
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
            return icon(MDBlock.brickArray, 100);
        }
    };
    public static final ItemGroup tabShapes = new ItemGroup("shapes")
    {
        @Override
        public ItemStack createIcon() {
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
            RenderTypeLookup.setRenderLayer(block, (RenderType) type);
        }
    }

    public MoreDyes()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.SERVER_CONFIG);
        CraftingHelper.register(WallsEnabledCondition.SERIALIZER);
        // Optional tags must be made before the tags load, so the API's tag is made here rather than on first use.
        MoreDyesAPI.DYES.getName();

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
        MinecraftForge.EVENT_BUS.addListener(ModWorldGen::onBiomeLoading);
        ModRecipes.register(FMLJavaModLoadingContext.get().getModEventBus());

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
        event.enqueueWork(Furnishings::setup);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CHEST.get(), ChestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.SIGN.get(), DyedSignRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.SHEEP, DyedSheepRenderer::new);
        // Replaces the vanilla renderer for blocks being moved by pistons, so dyed pistons move their own heads.
        ClientRegistry.bindTileEntityRenderer(TileEntityType.PISTON, DyedPistonRenderer::new);
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        for (BlockGlass block: MDBlock.glassArray)
        {
            event.enqueueWork(() -> layer(block, RenderType.getCutout()));
        }
        event.enqueueWork(() ->
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                layer(MDBlock.glassPaneArray[i], RenderType.getCutoutMipped());
                // Foggy glass is half see-through, so it needs the translucent layer.
                layer(MDBlock.glassFoggyArray[i], RenderType.getTranslucent());
                layer(MDBlock.glassFoggyPaneArray[i], RenderType.getTranslucent());
            }
        });
        event.enqueueWork(() ->
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                layer(MDBlock.tulipArray[i], RenderType.getCutout());
                layer(MDBlock.oakSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.birchSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.acaciaSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.darkOakSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.jungleSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.spruceSaplingArray[i], RenderType.getCutout());
                layer(MDBlock.oakLeafArray[i], RenderType.getCutoutMipped());
                layer(MDBlock.birchLeafArray[i], RenderType.getCutoutMipped());
                layer(MDBlock.acaciaLeafArray[i], RenderType.getCutoutMipped());
                layer(MDBlock.darkOakLeafArray[i], RenderType.getCutoutMipped());
                layer(MDBlock.jungleLeafArray[i], RenderType.getCutoutMipped());
                layer(MDBlock.spruceLafArray[i], RenderType.getCutoutMipped());
                // The wood and iron on a dyed piston are an untinted layer drawn over the tinted cobblestone, with see-through gaps.
                layer(MDBlock.stickyPistonArray[i], RenderType.getCutout());
                layer(MDBlock.pistonArray[i], RenderType.getCutout());
                layer(MDBlock.iceArray[i], RenderType.getTranslucent());
                layer(MDBlock.chainArray[i], RenderType.getCutout());
                for (MDBlock.PottedPlants potted : MDBlock.POTTED)
                {
                    layer(potted.pots[i], RenderType.getCutout());
                }
                for (Block[] flowers : MDBlock.smallFlowerArrays)
                {
                    layer(flowers[i], RenderType.getCutout());
                }
                for (Block[] flowers : MDBlock.tallFlowerArrays)
                {
                    layer(flowers[i], RenderType.getCutout());
                }
            }
            for (DyedShapes shapes : DyedShapes.ALL)
            {
                if (shapes.layer == DyedShapes.Layer.SOLID)
                {
                    continue;
                }
                RenderType layer = shapes.layer == DyedShapes.Layer.CUTOUT ? RenderType.getCutout() : RenderType.getTranslucent();
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
    public void onServerStarting(FMLServerStartingEvent event)
    {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
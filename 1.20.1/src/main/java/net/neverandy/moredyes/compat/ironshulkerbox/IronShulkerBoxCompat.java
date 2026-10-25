package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.IronShulkerBoxesTypes;
import com.progwml6.ironshulkerbox.common.inventory.IronShulkerBoxMenu;
import com.progwml6.ironshulkerbox.common.registraton.IronShulkerBoxesBlocks;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.compat.ironshulkerbox.client.IronShulkerBoxClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.Locale;

/**
 * Dyed Iron Shulker Boxes, in every More Dyes color. Each tier is one block whose color is a block state property, so
 * there are six blocks rather than six times 118. They hold as much as the Iron Shulker Boxes box of the same tier and
 * use its screen. The shell is drawn in the dye color and the metal trim keeps its own (client/DyedIronShulkerBoxRenderer).
 *
 * <p>Only loaded when Iron Shulker Boxes is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronShulkerBoxCompat
{
    public static final String MOD_ID = "ironshulkerbox";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

    public enum Tier
    {
        IRON(IronShulkerBoxesTypes.IRON, IronShulkerBoxMenu::createIronContainer),
        GOLD(IronShulkerBoxesTypes.GOLD, IronShulkerBoxMenu::createGoldContainer),
        DIAMOND(IronShulkerBoxesTypes.DIAMOND, IronShulkerBoxMenu::createDiamondContainer),
        COPPER(IronShulkerBoxesTypes.COPPER, IronShulkerBoxMenu::createCopperContainer),
        CRYSTAL(IronShulkerBoxesTypes.CRYSTAL, IronShulkerBoxMenu::createCrystalContainer),
        OBSIDIAN(IronShulkerBoxesTypes.OBSIDIAN, IronShulkerBoxMenu::createObsidianContainer);

        public final IronShulkerBoxesTypes type;
        final MenuFactory menu;
        public RegistryObject<DyedIronShulkerBoxBlock> block;
        public RegistryObject<DyedIronShulkerBoxItem> item;
        public RegistryObject<BlockEntityType<DyedIronShulkerBoxBlockEntity>> entity;

        Tier(IronShulkerBoxesTypes type, MenuFactory menu)
        {
            this.type = type;
            this.menu = menu;
        }

        /** The name Iron Shulker Boxes uses for this tier, as in "iron" for ironshulkerbox:iron_shulker_box. */
        public String id()
        {
            return name().toLowerCase(Locale.ROOT);
        }

        /** The dyed tier of an Iron Shulker Boxes type, or null for the vanilla box. */
        public static Tier of(IronShulkerBoxesTypes type)
        {
            for (Tier tier : values())
            {
                if (tier.type == type)
                {
                    return tier;
                }
            }
            return null;
        }
    }

    interface MenuFactory
    {
        AbstractContainerMenu create(int id, Inventory inventory, Container container);
    }

    private IronShulkerBoxCompat() {}

    public static void register(IEventBus modBus)
    {
        for (Tier tier : Tier.values())
        {
            String name = "dyed_" + tier.id() + "_shulker_box";
            tier.block = BLOCKS.register(name, () -> new DyedIronShulkerBoxBlock(tier, properties(tier)));
            tier.item = ITEMS.register(name, () -> new DyedIronShulkerBoxItem(tier.block.get(), new Item.Properties().stacksTo(1)));
            //noinspection DataFlowIssue
            tier.entity = BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(
                    (pos, state) -> tier == Tier.CRYSTAL ? new DyedCrystalShulkerBoxBlockEntity(pos, state)
                            : new DyedIronShulkerBoxBlockEntity(tier, pos, state), tier.block.get()).build(null));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        modBus.addListener(IronShulkerBoxCompat::creativeTab);
        modBus.addListener(IronShulkerBoxCompat::setup);
        MinecraftForge.EVENT_BUS.register(ShulkerBoxUpgrades.class);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            IronShulkerBoxClient.register(modBus);
        }
    }

    /** Washing a dyed box in a cauldron gives back Iron Shulker Boxes' undyed box, keeping its contents and name. */
    private static void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            for (Tier tier : Tier.values())
            {
                CauldronInteraction.WATER.put(tier.item.get(), (state, level, pos, player, hand, stack) ->
                {
                    if (!level.isClientSide)
                    {
                        ItemStack washed = new ItemStack(plain(tier));
                        if (stack.getTag() != null)
                        {
                            washed.setTag(stack.getTag().copy());
                            washed.removeTagKey("BlockStateTag");
                        }
                        player.setItemInHand(hand, washed);
                        player.awardStat(Stats.CLEAN_SHULKER_BOX);
                        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                });
            }
        });
    }

    /** The same as Iron Shulker Boxes' own boxes (IronShulkerBoxesBlocks); obsidian ones resist explosions. */
    private static BlockBehaviour.Properties properties(Tier tier)
    {
        BlockBehaviour.StatePredicate closed = (state, level, pos) ->
                !(level.getBlockEntity(pos) instanceof DyedIronShulkerBoxBlockEntity box) || box.isClosed();
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                .dynamicShape().noOcclusion().isSuffocating(closed).isViewBlocking(closed).pushReaction(PushReaction.DESTROY);
        return tier == Tier.OBSIDIAN ? properties.strength(3.0F, 10000.0F) : properties.strength(3.0F);
    }

    /** A dyed box of a tier in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Tier tier, int color)
    {
        ItemStack stack = new ItemStack(tier.item.get());
        CompoundTag state = new CompoundTag();
        state.putString(DyedIronShulkerBoxBlock.COLOR.getName(), Integer.toString(color));
        stack.addTagElement("BlockStateTag", state);
        return stack;
    }

    /** The color of a dyed box item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundTag tag = stack.getTagElement("BlockStateTag");
        if (tag == null)
        {
            return 0;
        }
        try
        {
            int color = Integer.parseInt(tag.getString(DyedIronShulkerBoxBlock.COLOR.getName()));
            return color >= 0 && color < ColorStrings.ALL.length ? color : 0;
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    /** Lists every color of every tier at the end of the More Dyes blocks tab. */
    private static void creativeTab(BuildCreativeModeTabContentsEvent event)
    {
        ResourceKey<CreativeModeTab> blocks = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(Reference.MOD_ID, "blocks"));
        if (event.getTabKey() == blocks)
        {
            for (Tier tier : Tier.values())
            {
                for (int color = 0; color < ColorStrings.ALL.length; color++)
                {
                    event.accept(stack(tier, color));
                }
            }
        }
    }

    /** Iron Shulker Boxes' own undyed box of a tier. */
    static Block plain(Tier tier)
    {
        return switch (tier)
        {
            case IRON -> IronShulkerBoxesBlocks.IRON_SHULKER_BOX.get();
            case GOLD -> IronShulkerBoxesBlocks.GOLD_SHULKER_BOX.get();
            case DIAMOND -> IronShulkerBoxesBlocks.DIAMOND_SHULKER_BOX.get();
            case COPPER -> IronShulkerBoxesBlocks.COPPER_SHULKER_BOX.get();
            case CRYSTAL -> IronShulkerBoxesBlocks.CRYSTAL_SHULKER_BOX.get();
            case OBSIDIAN -> IronShulkerBoxesBlocks.OBSIDIAN_SHULKER_BOX.get();
        };
    }
}

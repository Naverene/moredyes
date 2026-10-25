package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.IronShulkerBoxesTypes;
import com.progwml6.ironshulkerbox.common.block.tileentity.GenericIronShulkerBoxTileEntity;
import com.progwml6.ironshulkerbox.common.inventory.IronShulkerBoxContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironshulkerbox.client.DyedIronShulkerBoxItemRenderer;
import net.neverandy.moredyes.compat.ironshulkerbox.client.IronShulkerBoxClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.Locale;

/**
 * Dyed Iron Shulker Boxes, in every More Dyes color. Each tier is one block whose color is a block state property, so
 * there are seven blocks rather than seven times 118. They hold as much as the Iron Shulker Boxes box of the same tier
 * and use its screen. The shell is drawn in the dye color and the metal trim keeps its own
 * (client/DyedIronShulkerBoxRenderer).
 *
 * <p>Only loaded when Iron Shulker Boxes is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronShulkerBoxCompat
{
    public static final String MOD_ID = "ironshulkerbox";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    public enum Tier
    {
        IRON(IronShulkerBoxesTypes.IRON, IronShulkerBoxContainer::createIronContainer),
        GOLD(IronShulkerBoxesTypes.GOLD, IronShulkerBoxContainer::createGoldContainer),
        DIAMOND(IronShulkerBoxesTypes.DIAMOND, IronShulkerBoxContainer::createDiamondContainer),
        COPPER(IronShulkerBoxesTypes.COPPER, IronShulkerBoxContainer::createCopperContainer),
        SILVER(IronShulkerBoxesTypes.SILVER, IronShulkerBoxContainer::createSilverContainer),
        CRYSTAL(IronShulkerBoxesTypes.CRYSTAL, IronShulkerBoxContainer::createCrystalContainer),
        OBSIDIAN(IronShulkerBoxesTypes.OBSIDIAN, IronShulkerBoxContainer::createObsidianContainer);

        public final IronShulkerBoxesTypes type;
        final ContainerFactory container;
        public RegistryObject<DyedIronShulkerBoxBlock> block;
        public RegistryObject<DyedIronShulkerBoxItem> item;
        public RegistryObject<TileEntityType<DyedIronShulkerBoxTileEntity>> tileEntity;

        Tier(IronShulkerBoxesTypes type, ContainerFactory container)
        {
            this.type = type;
            this.container = container;
        }

        /** The name Iron Shulker Boxes uses for this tier, as in "iron" for ironshulkerbox:iron_shulker_box_white. */
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

    interface ContainerFactory
    {
        Container create(int id, PlayerInventory inventory, IInventory box);
    }

    private IronShulkerBoxCompat() {}

    public static void register(IEventBus modBus)
    {
        for (Tier tier : Tier.values())
        {
            String name = "dyed_" + tier.id() + "_shulker_box";
            tier.block = BLOCKS.register(name, () -> new DyedIronShulkerBoxBlock(tier, properties(tier)));
            tier.item = ITEMS.register(name, () -> new DyedIronShulkerBoxItem(tier.block.get(), new Item.Properties()
                    .maxStackSize(1).group(MoreDyes.tabBlocks).setISTER(() -> DyedIronShulkerBoxItemRenderer::new)));
            //noinspection ConstantConditions
            tier.tileEntity = TILE_ENTITIES.register(name, () -> TileEntityType.Builder.create(
                    () -> new DyedIronShulkerBoxTileEntity(tier), tier.block.get()).build(null));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILE_ENTITIES.register(modBus);
        MinecraftForge.EVENT_BUS.register(ShulkerBoxUpgrades.class);
        MinecraftForge.EVENT_BUS.register(ShulkerBoxWashing.class);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            IronShulkerBoxClient.register(modBus);
        }
    }

    /** The same as Iron Shulker Boxes' own boxes (ShulkerBoxesBlocks); obsidian ones resist explosions. */
    private static AbstractBlock.Properties properties(Tier tier)
    {
        AbstractBlock.IPositionPredicate closed = (state, world, pos) ->
        {
            TileEntity entity = world.getTileEntity(pos);
            return !(entity instanceof GenericIronShulkerBoxTileEntity) || ((GenericIronShulkerBoxTileEntity) entity).isClosed();
        };
        return AbstractBlock.Properties.create(Material.IRON)
                .hardnessAndResistance(3.0F, tier == Tier.OBSIDIAN ? 10000.0F : 3.0F)
                .variableOpacity().notSolid().setSuffocates(closed).setBlocksVision(closed);
    }

    /** A dyed box of a tier in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Tier tier, int color)
    {
        ItemStack stack = new ItemStack(tier.item.get());
        stack.getOrCreateChildTag("BlockStateTag").putString(DyedIronShulkerBoxBlock.COLOR.getName(), Integer.toString(color));
        return stack;
    }

    /** The color of a dyed box item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundNBT tag = stack.getChildTag("BlockStateTag");
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
}

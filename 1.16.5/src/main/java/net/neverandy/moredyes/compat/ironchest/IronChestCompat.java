package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.client.DyedIronChestItemRenderer;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.Locale;

/**
 * Dyed Iron Chests, in every More Dyes color. Each tier is one block whose color is a block state property, so there are
 * five blocks rather than five times 118. They hold as much as the Iron Chests chest of the same tier and use its
 * screen. The wood is drawn in the dye color and the metal keeps its own (client/DyedIronChestRenderer).
 *
 * <p>Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronChestCompat
{
    public static final String MOD_ID = "ironchest";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    /** The tiers that have wood to dye. Crystal, obsidian and dirt chests have none, so they are left out. */
    public enum Tier
    {
        IRON(IronChestsTypes.IRON, IronChestContainer::createIronContainer),
        GOLD(IronChestsTypes.GOLD, IronChestContainer::createGoldContainer),
        DIAMOND(IronChestsTypes.DIAMOND, IronChestContainer::createDiamondContainer),
        COPPER(IronChestsTypes.COPPER, IronChestContainer::createCopperContainer),
        SILVER(IronChestsTypes.SILVER, IronChestContainer::createSilverContainer);

        public final IronChestsTypes type;
        final ContainerFactory container;
        public RegistryObject<DyedIronChestBlock> block;
        public RegistryObject<DyedIronChestItem> item;
        public RegistryObject<TileEntityType<DyedIronChestTileEntity>> tileEntity;

        Tier(IronChestsTypes type, ContainerFactory container)
        {
            this.type = type;
            this.container = container;
        }

        /** The name Iron Chests uses for this tier, as in "iron" for ironchest:iron_chest. */
        public String id()
        {
            return name().toLowerCase(Locale.ROOT);
        }

        /** The dyed tier of an Iron Chests chest type, or null if it has none. */
        public static Tier of(IronChestsTypes type)
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
        Container create(int id, PlayerInventory inventory, IInventory chest);
    }

    private IronChestCompat() {}

    public static void register(IEventBus modBus)
    {
        for (Tier tier : Tier.values())
        {
            String name = "dyed_" + tier.id() + "_chest";
            // The same as Iron Chests' own chests (IronChestsBlocks).
            tier.block = BLOCKS.register(name, () -> new DyedIronChestBlock(tier,
                    AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3.0F)));
            tier.item = ITEMS.register(name, () -> new DyedIronChestItem(tier.block.get(), new Item.Properties()
                    .group(MoreDyes.tabBlocks).setISTER(() -> DyedIronChestItemRenderer::new)));
            //noinspection ConstantConditions
            tier.tileEntity = TILE_ENTITIES.register(name, () -> TileEntityType.Builder.create(
                    () -> new DyedIronChestTileEntity(tier), tier.block.get()).build(null));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILE_ENTITIES.register(modBus);
        MinecraftForge.EVENT_BUS.register(ChestUpgrades.class);
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            IronChestClient.register(modBus);
        }
    }

    /** A dyed chest of a tier in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Tier tier, int color)
    {
        ItemStack stack = new ItemStack(tier.item.get());
        stack.getOrCreateChildTag("BlockStateTag").putString(DyedIronChestBlock.COLOR.getName(), Integer.toString(color));
        return stack;
    }

    /** The color of a dyed chest item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundNBT tag = stack.getChildTag("BlockStateTag");
        if (tag == null)
        {
            return 0;
        }
        try
        {
            int color = Integer.parseInt(tag.getString(DyedIronChestBlock.COLOR.getName()));
            return color >= 0 && color < ColorStrings.ALL.length ? color : 0;
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
}

package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.Locale;

/**
 * Dyed Iron Chests, in every More Dyes color. Each tier is one block whose color is a block state property, so there are
 * four blocks rather than four times 118. They hold as much as the Iron Chests chest of the same tier and use its
 * screen. The wood is drawn in the dye color and the metal keeps its own (client/DyedIronChestRenderer).
 *
 * <p>Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronChestCompat
{
    public static final String MOD_ID = "ironchest";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MOD_ID);

    /** The tiers that have wood to dye. Crystal, obsidian and dirt chests have none, so they are left out. */
    public enum Tier
    {
        IRON(IronChestsTypes.IRON, IronChestMenu::createIronContainer),
        GOLD(IronChestsTypes.GOLD, IronChestMenu::createGoldContainer),
        DIAMOND(IronChestsTypes.DIAMOND, IronChestMenu::createDiamondContainer),
        COPPER(IronChestsTypes.COPPER, IronChestMenu::createCopperContainer);

        public final IronChestsTypes type;
        final MenuFactory menu;
        public RegistryObject<DyedIronChestBlock> block;
        public RegistryObject<DyedIronChestItem> item;
        public RegistryObject<BlockEntityType<DyedIronChestBlockEntity>> entity;

        Tier(IronChestsTypes type, MenuFactory menu)
        {
            this.type = type;
            this.menu = menu;
        }

        /** The name Iron Chests uses for this tier, as in "iron" for ironchest:iron_chest. */
        public String id()
        {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    interface MenuFactory
    {
        AbstractContainerMenu create(int id, Inventory inventory, Container container);
    }

    private IronChestCompat() {}

    public static void register(IEventBus modBus)
    {
        for (Tier tier : Tier.values())
        {
            String name = "dyed_" + tier.id() + "_chest";
            // The same as Iron Chests' own chests (IronChestsBlocks).
            tier.block = BLOCKS.register(name, () -> new DyedIronChestBlock(tier,
                    BlockBehaviour.Properties.of(Material.METAL).strength(3.0F)));
            // Listed in every color at the end of the More Dyes blocks tab (DyedIronChestItem.fillItemCategory).
            tier.item = ITEMS.register(name, () -> new DyedIronChestItem(tier.block.get(), new Item.Properties().tab(MoreDyes.tabBlocks)));
            //noinspection DataFlowIssue
            tier.entity = BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(
                    (pos, state) -> new DyedIronChestBlockEntity(tier, pos, state), tier.block.get()).build(null));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
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
        CompoundTag state = new CompoundTag();
        state.putString(DyedIronChestBlock.COLOR.getName(), Integer.toString(color));
        stack.addTagElement("BlockStateTag", state);
        return stack;
    }

    /** The color of a dyed chest item, or 0 if it has none. */
    public static int colorOf(ItemStack stack)
    {
        CompoundTag tag = stack.getTagElement("BlockStateTag");
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

package net.neverandy.moredyes.compat.ironchest;

import java.util.Set;
import java.util.function.Supplier;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColors;

/**
 * Dyed Iron Chests, in every More Dyes color. Each tier is one block whose color is a block state property (see
 * {@link DyedIronChestBlock#COLOR}), so there are four blocks rather than four times 118. They hold as much as the Iron
 * Chests chest of the same tier and use its screen. The wood is drawn in the dye color and the metal keeps its own
 * (client/DyedIronChestRenderer). Iron Chests' upgrades keep the color (ChestUpgrades).
 *
 * <p>
 * Only loaded when Iron Chests is installed (see MoreDyes), so nothing outside this package may refer to it.
 */
public final class IronChestCompat {

    public static final String MOD_ID = "ironchest";

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreDyes.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreDyes.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
        .create(Registries.BLOCK_ENTITY_TYPE, MoreDyes.MOD_ID);

    /**
     * The tiers that have wood to dye. Crystal, obsidian and dirt chests have none, so they are left out. Their
     * registry names ({@code moredyes:iron_chest}...) are Iron Chests' own; {@code tools/generate_resources.py} reads
     * them from here, so keep the first argument of each a plain string literal.
     */
    public enum Tier {
        IRON("iron", IronChestsTypes.IRON, IronChestMenu::createIronContainer, IronChestsBlocks.IRON_CHEST),
        GOLD("gold", IronChestsTypes.GOLD, IronChestMenu::createGoldContainer, IronChestsBlocks.GOLD_CHEST),
        DIAMOND("diamond", IronChestsTypes.DIAMOND, IronChestMenu::createDiamondContainer, IronChestsBlocks.DIAMOND_CHEST),
        COPPER("copper", IronChestsTypes.COPPER, IronChestMenu::createCopperContainer, IronChestsBlocks.COPPER_CHEST);

        private final String id;
        public final IronChestsTypes type;
        final MenuFactory menu;
        private final Supplier<? extends Block> plain;
        DeferredBlock<DyedIronChestBlock> block;
        DeferredItem<DyedIronChestItem> item;
        Supplier<BlockEntityType<DyedIronChestBlockEntity>> entity;

        Tier(String id, IronChestsTypes type, MenuFactory menu, Supplier<? extends Block> plain) {
            this.id = id;
            this.type = type;
            this.menu = menu;
            this.plain = plain;
        }

        /** The name Iron Chests uses for this tier, as in "iron" for ironchest:iron_chest. */
        public String id() {
            return id;
        }

        public DyedIronChestBlock block() {
            return block.get();
        }

        public DyedIronChestItem item() {
            return item.get();
        }

        public BlockEntityType<DyedIronChestBlockEntity> entity() {
            return entity.get();
        }

        /** Iron Chests' own chest of this tier. */
        public Block plain() {
            return plain.get();
        }

        /** The dyed tier for an Iron Chests chest type, or null if it has none. */
        public static Tier of(IronChestsTypes type) {
            for (Tier tier : values()) {
                if (tier.type == type) {
                    return tier;
                }
            }
            return null;
        }
    }

    interface MenuFactory {
        AbstractContainerMenu create(int id, Inventory inventory, Container container);
    }

    private IronChestCompat() {}

    public static void register(IEventBus modBus) {
        for (Tier tier : Tier.values()) {
            String name = tier.id() + "_chest";
            // The same properties as Iron Chests' own chests (IronChestsBlocks).
            tier.block = BLOCKS.registerBlock(name, p -> new DyedIronChestBlock(tier, p),
                () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F).sound(SoundType.METAL));
            tier.item = ITEMS.registerItem(name, p -> new DyedIronChestItem(tier.block.get(), p.useBlockDescriptionPrefix()));
            tier.entity = BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(
                (pos, state) -> new DyedIronChestBlockEntity(tier, pos, state), Set.of(tier.block.get())));
        }
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        modBus.addListener(IronChestCompat::creativeTab);
        NeoForge.EVENT_BUS.addListener(ChestUpgrades::onRightClickBlock);
    }

    /** A dyed chest of a tier in a color, by its position in {@link MixColors#ALL}. */
    public static ItemStack stack(Tier tier, int color) {
        ItemStack stack = new ItemStack(tier.item());
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(DyedIronChestBlock.COLOR, color));
        return stack;
    }

    /** The color of a dyed chest item, by its position in {@link MixColors#ALL}, or 0 if it has none. */
    public static int colorOf(ItemStack stack) {
        Integer color = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY)
            .get(DyedIronChestBlock.COLOR);
        return color != null ? color : 0;
    }

    /** Lists every color of every tier at the end of the More Dyes blocks tab. */
    private static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> blocks = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "blocks"));
        if (blocks.equals(event.getTabKey())) {
            for (Tier tier : Tier.values()) {
                for (int color = 0; color < MixColors.ALL.size(); color++) {
                    event.accept(stack(tier, color));
                }
            }
        }
    }
}

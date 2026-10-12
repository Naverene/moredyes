package net.neverandy.moredyes.compat.ironchest;

import anner.ironchest.blocks.ChestTypes;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.lang.reflect.Constructor;
import java.util.Locale;

/**
 * Dyed Iron Chests, in every More Dyes color, for the Fabric port of Iron Chests (Modrinth "ironchest", by anner). Each
 * tier is one block whose color is a block state property, so there are four blocks rather than four times 118. They
 * hold as much as the Iron Chests chest of the same tier and use its screen. That port's chests are all metal, with no
 * wood, so the metal panels are drawn in the dye color and the frame, latch and inside keep the tier's own colors
 * (client/DyedIronChestRenderer). Iron Chests' upgrades keep the color (ChestUpgrades).
 *
 * <p>Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronChestCompat
{
    public static final String MOD_ID = "ironchest";

    /** The tiers that have dyed versions, as in the Forge build. Iron Chests' other chests stay plain. */
    public enum Tier
    {
        IRON(ChestTypes.IRON),
        GOLD(ChestTypes.GOLD),
        DIAMOND(ChestTypes.DIAMOND),
        COPPER(ChestTypes.COPPER);

        public final ChestTypes type;
        DyedIronChestBlock block;
        DyedIronChestItem item;
        BlockEntityType<DyedIronChestBlockEntity> entity;

        Tier(ChestTypes type)
        {
            this.type = type;
        }

        /** The name Iron Chests uses for this tier, as in "iron" for ironchest:iron_chest. */
        public String id()
        {
            return name().toLowerCase(Locale.ROOT);
        }

        public DyedIronChestBlock block()
        {
            return block;
        }

        public DyedIronChestItem item()
        {
            return item;
        }

        public BlockEntityType<DyedIronChestBlockEntity> entity()
        {
            return entity;
        }

        /** Iron Chests' own chest of this tier. */
        public Block plain()
        {
            return ChestTypes.get(type);
        }

        /** The dyed tier for an Iron Chests chest type, or null if it has none. */
        public static Tier of(ChestTypes type)
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

    /**
     * Iron Chests' chest screen. It is built on LibGui, which Iron Chests carries inside its jar, so it isn't on the
     * compile classpath and is made by reflection.
     */
    private static Constructor<?> screen;

    private IronChestCompat() {}

    public static void register()
    {
        for (Tier tier : Tier.values())
        {
            ResourceLocation id = new ResourceLocation(Reference.MOD_ID, "dyed_" + tier.id() + "_chest");
            // The same hardness, sound and tool as Iron Chests' own chest of the tier.
            tier.block = Registry.register(BuiltInRegistries.BLOCK, id, new DyedIronChestBlock(tier, tier.type.setting()));
            tier.item = Registry.register(BuiltInRegistries.ITEM, id, new DyedIronChestItem(tier.block, new Item.Properties()));
            tier.entity = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(
                    (pos, state) -> new DyedIronChestBlockEntity(tier, pos, state), tier.block).build());
        }
        // Every color of every tier at the end of the More Dyes blocks tab.
        ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(Reference.MOD_ID, "blocks")))
                .register(entries -> {
                    for (Tier tier : Tier.values())
                    {
                        for (int color = 0; color < ColorStrings.ALL.length; color++)
                        {
                            entries.accept(stack(tier, color));
                        }
                    }
                });
        UseBlockCallback.EVENT.register(ChestUpgrades::onRightClickBlock);
    }

    /** A dyed chest of a tier in a color, by its position in ColorStrings.ALL. */
    public static ItemStack stack(Tier tier, int color)
    {
        ItemStack stack = new ItemStack(tier.item);
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

    /** Iron Chests' screen for a chest of this tier, showing the chest at this position. */
    static AbstractContainerMenu menu(Tier tier, int id, Inventory inventory, Level level, BlockPos pos)
    {
        try
        {
            if (screen == null)
            {
                screen = Class.forName("anner.ironchest.screenhandlers.ChestScreenHandler")
                        .getConstructor(MenuType.class, ChestTypes.class, int.class, Inventory.class, ContainerLevelAccess.class);
            }
            return (AbstractContainerMenu) screen.newInstance(tier.type.getScreenHandlerType(), tier.type, id, inventory,
                    ContainerLevelAccess.create(level, pos));
        }
        catch (ReflectiveOperationException e)
        {
            throw new IllegalStateException("Couldn't open Iron Chests' chest screen", e);
        }
    }
}

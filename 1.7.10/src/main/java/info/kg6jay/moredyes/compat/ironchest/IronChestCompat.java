package info.kg6jay.moredyes.compat.ironchest;

import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import info.kg6jay.moredyes.recipe.KeepTagRecipe;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Dyed Iron Chests, in every More Dyes color. There is one block per tier, and the color is kept in the chest's tile
 * entity: block metadata only holds 16 values and there are 118 colors, and a block per color would use hundreds of
 * block ids. The item damage is the color's number (see ColorIndex), as for the dyed stairs, slabs and trapdoors.
 * <p>
 * In Minecraft 1.7.10 Iron Chests' chests are metal all over, with no wood. The dye colors the flat panel of each face
 * and the frame around it and the latch keep the tier's own color (see TintSources.metalChest). The chests hold as
 * much as the Iron Chests chest of the same tier, use its screen, and are Iron Chests tile entities, so other mods see
 * them as such.
 * <p>
 * Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronChestCompat {

    /**
     * The tiers that have dyed versions: the ones with a frame and panels. Crystal chests are glass (their panels are
     * clear, so there is nothing to dye), and obsidian and dirt chests are one rough texture all over with no frame.
     * They stay as they are.
     */
    public enum Tier {

        IRON,
        GOLD,
        DIAMOND,
        COPPER,
        SILVER;

        /** Iron Chests' type, or null if this version of Iron Chests has no such tier. */
        public IronChestType type;
        public DyedIronChestBlock block;

        /** The name Iron Chests uses for this tier's textures, as in "iron" for ironchest.png. */
        public String id() {
            return this.name()
                .toLowerCase(Locale.ROOT);
        }

        /** "Iron", for block names. */
        public String title() {
            return this.name()
                .charAt(0)
                + this.id()
                    .substring(1);
        }

        DyedIronChestTile newTile() {
            return switch (this) {
                case IRON -> new DyedIronChestTile.Iron();
                case GOLD -> new DyedIronChestTile.Gold();
                case DIAMOND -> new DyedIronChestTile.Diamond();
                case COPPER -> new DyedIronChestTile.Copper();
                case SILVER -> new DyedIronChestTile.Silver();
            };
        }

        Class<? extends DyedIronChestTile> tileClass() {
            return switch (this) {
                case IRON -> DyedIronChestTile.Iron.class;
                case GOLD -> DyedIronChestTile.Gold.class;
                case DIAMOND -> DyedIronChestTile.Diamond.class;
                case COPPER -> DyedIronChestTile.Copper.class;
                case SILVER -> DyedIronChestTile.Silver.class;
            };
        }

        /** The dyed tier of an Iron Chests type, or null if it has none. */
        public static Tier of(IronChestType type) {
            for (Tier tier : values()) {
                if (tier.type == type && tier.block != null) {
                    return tier;
                }
            }
            return null;
        }
    }

    private IronChestCompat() {}

    /** Registers the blocks and tile entities. Called in preInit, after Iron Chests' own preInit. */
    public static void preInit() {
        for (Tier tier : Tier.values()) {
            try {
                tier.type = IronChestType.valueOf(tier.name());
            } catch (IllegalArgumentException e) {
                // Forks of Iron Chests may not have every tier.
                LogHelper.info("Iron Chests has no " + tier.id() + " chest, so there is no dyed one");
                continue;
            }
            tier.block = new DyedIronChestBlock(tier);
            String name = "dyed" + tier.title() + "Chest";
            GameRegistry.registerBlock(tier.block, DyedIronChestItem.class, name);
            GameRegistry.registerTileEntity(tier.tileClass(), "moredyes." + name);
        }
        LogHelper.info("Iron Chests is loaded: added dyed Iron Chests");
    }

    /**
     * Recipes: an Iron Chests chest (or a dyed one of the same tier) and a dye make a dyed chest. Also lets Iron
     * Chests' upgrade items keep the color (ChestUpgrades).
     */
    public static void init() {
        for (Tier tier : Tier.values()) {
            if (tier.block == null) {
                continue;
            }
            ItemStack plain = new ItemStack(IronChest.ironChestBlock, 1, tier.type.ordinal());
            ItemStack anyDyed = new ItemStack(tier.block, 1, OreDictionary.WILDCARD_VALUE);
            for (int color = 0; color < ColorIndex.count(); color++) {
                ItemStack result = new ItemStack(tier.block, 1, color);
                GameRegistry.addRecipe(new KeepTagRecipe(result, plain, ColorIndex.dye(color)));
                GameRegistry.addRecipe(new KeepTagRecipe(result, anyDyed, ColorIndex.dye(color)));
            }
        }
        MinecraftForge.EVENT_BUS.register(new ChestUpgrades());
    }
}

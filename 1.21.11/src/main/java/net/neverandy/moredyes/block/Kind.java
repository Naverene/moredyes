package net.neverandy.moredyes.block;

import java.util.Optional;
import java.util.function.BiFunction;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * Every kind of block that comes in all of the mixed colors. Each kind is registered once per color under the name
 * {@code <id>_<hex>}, for example {@code moredyes:stone_bricks_c56685}.
 *
 * <p>
 * Each kind copies the properties (hardness, sound, light, map color, piston behavior...) of the vanilla block
 * named next to it, and gets the same tags, so vanilla behaviors that check a tag (nether portal frames, bubble
 * columns, what plants grow on, enchanting power...) work the same way. Slabs, stairs and walls are written
 * {@code NAME(Shape.X, "id", BASE_KIND)} instead. {@code tools/generate_resources.py} reads the ids and vanilla blocks from this file to write the models,
 * drops, recipes and tags, so keep the first two arguments of each entry as plain string literals.
 */
public enum Kind {

    WOOL("wool", "white_wool", Tab.BLOCKS, simple()),
    STONE("stone", "stone", Tab.BLOCKS, simple()),
    COBBLESTONE("cobblestone", "cobblestone", Tab.BLOCKS, simple()),
    STONE_BRICKS("stone_bricks", "stone_bricks", Tab.BLOCKS, simple()),
    CRACKED_STONE_BRICKS("cracked_stone_bricks", "cracked_stone_bricks", Tab.BLOCKS, simple()),
    CHISELED_STONE_BRICKS("chiseled_stone_bricks", "chiseled_stone_bricks", Tab.BLOCKS, simple()),
    ANDESITE("andesite", "andesite", Tab.BLOCKS, simple()),
    DIORITE("diorite", "diorite", Tab.BLOCKS, simple()),
    BRICKS("bricks", "bricks", Tab.BLOCKS, simple()),
    CLAY("clay", "clay", Tab.BLOCKS, simple()),
    TERRACOTTA("terracotta", "terracotta", Tab.BLOCKS, simple()),
    CONCRETE("concrete", "white_concrete", Tab.BLOCKS, simple()),
    CONCRETE_POWDER("concrete_powder", "white_concrete_powder", Tab.BLOCKS,
        (color, p) -> new ConcretePowderBlock(ModBlocks.get(Kind.CONCRETE, color).get(), p)),
    SAND("sand", "sand", Tab.BLOCKS, (color, p) -> new ColoredFallingBlock(new ColorRGBA(0xFF000000 | color.rgb()), p)),
    SANDSTONE("sandstone", "sandstone", Tab.BLOCKS, simple()),
    CHISELED_SANDSTONE("chiseled_sandstone", "chiseled_sandstone", Tab.BLOCKS, simple()),
    CUT_SANDSTONE("cut_sandstone", "cut_sandstone", Tab.BLOCKS, simple()),
    SOUL_SAND("soul_sand", "soul_sand", Tab.BLOCKS, (color, p) -> new SoulSandBlock(p)),
    OBSIDIAN("obsidian", "obsidian", Tab.BLOCKS, (color, p) -> new DyedObsidianBlock(p)),
    GLOWSTONE("glowstone", "glowstone", Tab.BLOCKS, simple()),
    COAL_BLOCK("coal_block", "coal_block", Tab.BLOCKS, simple()),
    LAPIS_BLOCK("lapis_block", "lapis_block", Tab.BLOCKS, simple()),
    REDSTONE_BLOCK("redstone_block", "redstone_block", Tab.BLOCKS, (color, p) -> new PoweredBlock(p)),
    QUARTZ_BLOCK("quartz_block", "quartz_block", Tab.BLOCKS, simple()),
    GLASS("glass", "glass", Tab.BLOCKS, (color, p) -> new TransparentBlock(p)),
    FOGGY_GLASS("foggy_glass", "glass", Tab.BLOCKS, (color, p) -> new TransparentBlock(p)),
    GLASS_PANE("glass_pane", "glass_pane", Tab.BLOCKS, (color, p) -> new IronBarsBlock(p)),
    FOGGY_GLASS_PANE("foggy_glass_pane", "glass_pane", Tab.BLOCKS, (color, p) -> new IronBarsBlock(p)),
    BOOKSHELF("bookshelf", "bookshelf", Tab.BLOCKS, simple()),
    CRAFTING_TABLE("crafting_table", "crafting_table", Tab.BLOCKS, (color, p) -> new DyedCraftingTableBlock(p)),
    CHEST("chest", "chest", Tab.BLOCKS, (color, p) -> new DyedChestBlock(p)),
    PISTON("piston", "piston", Tab.BLOCKS, (color, p) -> new DyedPistonBaseBlock(false, color, p)),
    STICKY_PISTON("sticky_piston", "sticky_piston", Tab.BLOCKS, (color, p) -> new DyedPistonBaseBlock(true, color, p)),
    /** The moving part of a dyed piston. It has no item, like the vanilla piston head. */
    PISTON_HEAD("piston_head", "piston_head", Tab.NONE, (color, p) -> new DyedPistonHeadBlock(color, p)),
    SIGN("sign", "oak_sign", Tab.BLOCKS, (color, p) -> new StandingSignBlock(WoodType.OAK, p)),
    /** A sign on a wall. It has no item of its own: the sign item places it, and it drops the sign, like vanilla's. */
    WALL_SIGN("wall_sign", "oak_wall_sign", Tab.NONE, (color, p) -> {
        Block sign = ModBlocks.get(Kind.SIGN, color).get();
        return new WallSignBlock(WoodType.OAK, p.overrideLootTable(sign.getLootTable())
            .overrideDescription(sign.getDescriptionId()));
    }),

    OAK_LOG("oak_log", "oak_log", Tab.TREES, log()),
    OAK_PLANKS("oak_planks", "oak_planks", Tab.TREES, simple()),
    OAK_LEAVES("oak_leaves", "oak_leaves", Tab.TREES, leaves()),
    OAK_SAPLING("oak_sapling", "oak_sapling", Tab.TREES, sapling(Wood.OAK)),
    OAK_FENCE("oak_fence", "oak_fence", Tab.TREES, fence()),
    BIRCH_LOG("birch_log", "birch_log", Tab.TREES, log()),
    BIRCH_PLANKS("birch_planks", "birch_planks", Tab.TREES, simple()),
    BIRCH_LEAVES("birch_leaves", "birch_leaves", Tab.TREES, leaves()),
    BIRCH_SAPLING("birch_sapling", "birch_sapling", Tab.TREES, sapling(Wood.BIRCH)),
    BIRCH_FENCE("birch_fence", "birch_fence", Tab.TREES, fence()),
    SPRUCE_LOG("spruce_log", "spruce_log", Tab.TREES, log()),
    SPRUCE_PLANKS("spruce_planks", "spruce_planks", Tab.TREES, simple()),
    SPRUCE_LEAVES("spruce_leaves", "spruce_leaves", Tab.TREES, leaves()),
    SPRUCE_SAPLING("spruce_sapling", "spruce_sapling", Tab.TREES, sapling(Wood.SPRUCE)),
    SPRUCE_FENCE("spruce_fence", "spruce_fence", Tab.TREES, fence()),
    JUNGLE_LOG("jungle_log", "jungle_log", Tab.TREES, log()),
    JUNGLE_PLANKS("jungle_planks", "jungle_planks", Tab.TREES, simple()),
    JUNGLE_LEAVES("jungle_leaves", "jungle_leaves", Tab.TREES, leaves()),
    JUNGLE_SAPLING("jungle_sapling", "jungle_sapling", Tab.TREES, sapling(Wood.JUNGLE)),
    JUNGLE_FENCE("jungle_fence", "jungle_fence", Tab.TREES, fence()),
    ACACIA_LOG("acacia_log", "acacia_log", Tab.TREES, log()),
    ACACIA_PLANKS("acacia_planks", "acacia_planks", Tab.TREES, simple()),
    ACACIA_LEAVES("acacia_leaves", "acacia_leaves", Tab.TREES, leaves()),
    ACACIA_SAPLING("acacia_sapling", "acacia_sapling", Tab.TREES, sapling(Wood.ACACIA)),
    ACACIA_FENCE("acacia_fence", "acacia_fence", Tab.TREES, fence()),
    DARK_OAK_LOG("dark_oak_log", "dark_oak_log", Tab.TREES, log()),
    DARK_OAK_PLANKS("dark_oak_planks", "dark_oak_planks", Tab.TREES, simple()),
    DARK_OAK_LEAVES("dark_oak_leaves", "dark_oak_leaves", Tab.TREES, leaves()),
    DARK_OAK_SAPLING("dark_oak_sapling", "dark_oak_sapling", Tab.TREES, sapling(Wood.DARK_OAK)),
    DARK_OAK_FENCE("dark_oak_fence", "dark_oak_fence", Tab.TREES, fence()),

    GRANITE("granite", "granite", Tab.BLOCKS, simple()),
    POLISHED_ANDESITE("polished_andesite", "polished_andesite", Tab.BLOCKS, simple()),
    POLISHED_DIORITE("polished_diorite", "polished_diorite", Tab.BLOCKS, simple()),
    POLISHED_GRANITE("polished_granite", "polished_granite", Tab.BLOCKS, simple()),
    END_STONE("end_stone", "end_stone", Tab.BLOCKS, simple()),
    MOSSY_COBBLESTONE("mossy_cobblestone", "mossy_cobblestone", Tab.BLOCKS, simple()),
    MOSSY_STONE_BRICKS("mossy_stone_bricks", "mossy_stone_bricks", Tab.BLOCKS, simple()),
    QUARTZ_BRICKS("quartz_bricks", "quartz_bricks", Tab.BLOCKS, simple()),
    CHISELED_QUARTZ_BLOCK("chiseled_quartz_block", "chiseled_quartz_block", Tab.BLOCKS, simple()),
    QUARTZ_PILLAR("quartz_pillar", "quartz_pillar", Tab.BLOCKS, log()),
    SMOOTH_QUARTZ("smooth_quartz", "smooth_quartz", Tab.BLOCKS, simple()),
    BONE_BLOCK("bone_block", "bone_block", Tab.BLOCKS, log()),
    GRAVEL("gravel", "gravel", Tab.BLOCKS, (color, p) -> new ColoredFallingBlock(new ColorRGBA(0xFF000000 | color.rgb()), p)),
    ICE("ice", "ice", Tab.BLOCKS, (color, p) -> new IceBlock(p)),
    PACKED_ICE("packed_ice", "packed_ice", Tab.BLOCKS, simple()),
    SNOW_BLOCK("snow_block", "snow_block", Tab.BLOCKS, simple()),

    TULIP("tulip", "white_tulip", Tab.PLANTS, (color, p) -> new FlowerBlock(MobEffects.WEAKNESS, 7.0F, p)),
    ALLIUM("allium", "allium", Tab.PLANTS, flower("allium")),
    AZURE_BLUET("azure_bluet", "azure_bluet", Tab.PLANTS, flower("azure_bluet")),
    BLUE_ORCHID("orchid", "blue_orchid", Tab.PLANTS, flower("blue_orchid")),
    CORNFLOWER("cornflower", "cornflower", Tab.PLANTS, flower("cornflower")),
    DANDELION("dandelion", "dandelion", Tab.PLANTS, flower("dandelion")),
    LILY_OF_THE_VALLEY("lily_of_the_valley", "lily_of_the_valley", Tab.PLANTS, flower("lily_of_the_valley")),
    OXEYE_DAISY("oxeye_daisy", "oxeye_daisy", Tab.PLANTS, flower("oxeye_daisy")),
    POPPY("poppy", "poppy", Tab.PLANTS, flower("poppy")),
    LILAC("lilac", "lilac", Tab.PLANTS, (color, p) -> new TallFlowerBlock(p)),
    PEONY("peony", "peony", Tab.PLANTS, (color, p) -> new TallFlowerBlock(p)),
    ROSE_BUSH("rose_bush", "rose_bush", Tab.PLANTS, (color, p) -> new TallFlowerBlock(p)),

    // Slabs, stairs and walls, made from the dyed block of the same color. Only vanilla's wall types get walls: a wall
    // has 324 block states, and walls of every type in all the colors would need more memory than a client has.
    STONE_SLAB(Shape.SLAB, "stone_slab", STONE),
    STONE_STAIRS(Shape.STAIRS, "stone_stairs", STONE),
    COBBLESTONE_SLAB(Shape.SLAB, "cobblestone_slab", COBBLESTONE),
    COBBLESTONE_STAIRS(Shape.STAIRS, "cobblestone_stairs", COBBLESTONE),
    COBBLESTONE_WALL(Shape.WALL, "cobblestone_wall", COBBLESTONE),
    MOSSY_COBBLESTONE_SLAB(Shape.SLAB, "mossy_cobblestone_slab", MOSSY_COBBLESTONE),
    MOSSY_COBBLESTONE_STAIRS(Shape.STAIRS, "mossy_cobblestone_stairs", MOSSY_COBBLESTONE),
    MOSSY_COBBLESTONE_WALL(Shape.WALL, "mossy_cobblestone_wall", MOSSY_COBBLESTONE),
    STONE_BRICK_SLAB(Shape.SLAB, "stone_brick_slab", STONE_BRICKS),
    STONE_BRICK_STAIRS(Shape.STAIRS, "stone_brick_stairs", STONE_BRICKS),
    STONE_BRICK_WALL(Shape.WALL, "stone_brick_wall", STONE_BRICKS),
    CRACKED_STONE_BRICK_SLAB(Shape.SLAB, "cracked_stone_brick_slab", CRACKED_STONE_BRICKS),
    CRACKED_STONE_BRICK_STAIRS(Shape.STAIRS, "cracked_stone_brick_stairs", CRACKED_STONE_BRICKS),
    CHISELED_STONE_BRICK_SLAB(Shape.SLAB, "chiseled_stone_brick_slab", CHISELED_STONE_BRICKS),
    CHISELED_STONE_BRICK_STAIRS(Shape.STAIRS, "chiseled_stone_brick_stairs", CHISELED_STONE_BRICKS),
    MOSSY_STONE_BRICK_SLAB(Shape.SLAB, "mossy_stone_brick_slab", MOSSY_STONE_BRICKS),
    MOSSY_STONE_BRICK_STAIRS(Shape.STAIRS, "mossy_stone_brick_stairs", MOSSY_STONE_BRICKS),
    MOSSY_STONE_BRICK_WALL(Shape.WALL, "mossy_stone_brick_wall", MOSSY_STONE_BRICKS),
    BRICK_SLAB(Shape.SLAB, "brick_slab", BRICKS),
    BRICK_STAIRS(Shape.STAIRS, "brick_stairs", BRICKS),
    BRICK_WALL(Shape.WALL, "brick_wall", BRICKS),
    CLAY_SLAB(Shape.SLAB, "clay_slab", CLAY),
    CLAY_STAIRS(Shape.STAIRS, "clay_stairs", CLAY),
    COAL_SLAB(Shape.SLAB, "coal_slab", COAL_BLOCK),
    COAL_STAIRS(Shape.STAIRS, "coal_stairs", COAL_BLOCK),
    LAPIS_SLAB(Shape.SLAB, "lapis_slab", LAPIS_BLOCK),
    LAPIS_STAIRS(Shape.STAIRS, "lapis_stairs", LAPIS_BLOCK),
    REDSTONE_SLAB(Shape.SLAB, "redstone_slab", REDSTONE_BLOCK),
    REDSTONE_STAIRS(Shape.STAIRS, "redstone_stairs", REDSTONE_BLOCK),
    QUARTZ_SLAB(Shape.SLAB, "quartz_slab", QUARTZ_BLOCK),
    QUARTZ_STAIRS(Shape.STAIRS, "quartz_stairs", QUARTZ_BLOCK),
    QUARTZ_BRICK_SLAB(Shape.SLAB, "quartz_brick_slab", QUARTZ_BRICKS),
    QUARTZ_BRICK_STAIRS(Shape.STAIRS, "quartz_brick_stairs", QUARTZ_BRICKS),
    CHISELED_QUARTZ_SLAB(Shape.SLAB, "chiseled_quartz_slab", CHISELED_QUARTZ_BLOCK),
    CHISELED_QUARTZ_STAIRS(Shape.STAIRS, "chiseled_quartz_stairs", CHISELED_QUARTZ_BLOCK),
    QUARTZ_PILLAR_SLAB(Shape.SLAB, "quartz_pillar_slab", QUARTZ_PILLAR),
    QUARTZ_PILLAR_STAIRS(Shape.STAIRS, "quartz_pillar_stairs", QUARTZ_PILLAR),
    SMOOTH_QUARTZ_SLAB(Shape.SLAB, "smooth_quartz_slab", SMOOTH_QUARTZ),
    SMOOTH_QUARTZ_STAIRS(Shape.STAIRS, "smooth_quartz_stairs", SMOOTH_QUARTZ),
    OBSIDIAN_SLAB(Shape.SLAB, "obsidian_slab", OBSIDIAN),
    OBSIDIAN_STAIRS(Shape.STAIRS, "obsidian_stairs", OBSIDIAN),
    GLOWSTONE_SLAB(Shape.SLAB, "glowstone_slab", GLOWSTONE),
    GLOWSTONE_STAIRS(Shape.STAIRS, "glowstone_stairs", GLOWSTONE),
    SOUL_SAND_SLAB(Shape.SLAB, "soul_sand_slab", SOUL_SAND),
    SOUL_SAND_STAIRS(Shape.STAIRS, "soul_sand_stairs", SOUL_SAND),
    SAND_SLAB(Shape.SLAB, "sand_slab", SAND),
    SAND_STAIRS(Shape.STAIRS, "sand_stairs", SAND),
    SANDSTONE_SLAB(Shape.SLAB, "sandstone_slab", SANDSTONE),
    SANDSTONE_STAIRS(Shape.STAIRS, "sandstone_stairs", SANDSTONE),
    SANDSTONE_WALL(Shape.WALL, "sandstone_wall", SANDSTONE),
    CHISELED_SANDSTONE_SLAB(Shape.SLAB, "chiseled_sandstone_slab", CHISELED_SANDSTONE),
    CHISELED_SANDSTONE_STAIRS(Shape.STAIRS, "chiseled_sandstone_stairs", CHISELED_SANDSTONE),
    CUT_SANDSTONE_SLAB(Shape.SLAB, "cut_sandstone_slab", CUT_SANDSTONE),
    CUT_SANDSTONE_STAIRS(Shape.STAIRS, "cut_sandstone_stairs", CUT_SANDSTONE),
    ANDESITE_SLAB(Shape.SLAB, "andesite_slab", ANDESITE),
    ANDESITE_STAIRS(Shape.STAIRS, "andesite_stairs", ANDESITE),
    ANDESITE_WALL(Shape.WALL, "andesite_wall", ANDESITE),
    DIORITE_SLAB(Shape.SLAB, "diorite_slab", DIORITE),
    DIORITE_STAIRS(Shape.STAIRS, "diorite_stairs", DIORITE),
    DIORITE_WALL(Shape.WALL, "diorite_wall", DIORITE),
    GRANITE_SLAB(Shape.SLAB, "granite_slab", GRANITE),
    GRANITE_STAIRS(Shape.STAIRS, "granite_stairs", GRANITE),
    GRANITE_WALL(Shape.WALL, "granite_wall", GRANITE),
    POLISHED_ANDESITE_SLAB(Shape.SLAB, "polished_andesite_slab", POLISHED_ANDESITE),
    POLISHED_ANDESITE_STAIRS(Shape.STAIRS, "polished_andesite_stairs", POLISHED_ANDESITE),
    POLISHED_DIORITE_SLAB(Shape.SLAB, "polished_diorite_slab", POLISHED_DIORITE),
    POLISHED_DIORITE_STAIRS(Shape.STAIRS, "polished_diorite_stairs", POLISHED_DIORITE),
    POLISHED_GRANITE_SLAB(Shape.SLAB, "polished_granite_slab", POLISHED_GRANITE),
    POLISHED_GRANITE_STAIRS(Shape.STAIRS, "polished_granite_stairs", POLISHED_GRANITE),
    END_STONE_SLAB(Shape.SLAB, "end_stone_slab", END_STONE),
    END_STONE_STAIRS(Shape.STAIRS, "end_stone_stairs", END_STONE),
    END_STONE_WALL(Shape.WALL, "end_stone_wall", END_STONE),
    ICE_SLAB(Shape.SLAB, "ice_slab", ICE),
    ICE_STAIRS(Shape.STAIRS, "ice_stairs", ICE),
    PACKED_ICE_SLAB(Shape.SLAB, "packed_ice_slab", PACKED_ICE),
    PACKED_ICE_STAIRS(Shape.STAIRS, "packed_ice_stairs", PACKED_ICE),
    SNOW_SLAB(Shape.SLAB, "snow_slab", SNOW_BLOCK),
    SNOW_STAIRS(Shape.STAIRS, "snow_stairs", SNOW_BLOCK),
    BONE_BLOCK_SLAB(Shape.SLAB, "bone_block_slab", BONE_BLOCK),
    BONE_BLOCK_STAIRS(Shape.STAIRS, "bone_block_stairs", BONE_BLOCK),
    GLASS_SLAB(Shape.SLAB, "glass_slab", GLASS),
    GLASS_STAIRS(Shape.STAIRS, "glass_stairs", GLASS),
    FOGGY_GLASS_SLAB(Shape.SLAB, "foggy_glass_slab", FOGGY_GLASS),
    FOGGY_GLASS_STAIRS(Shape.STAIRS, "foggy_glass_stairs", FOGGY_GLASS),
    WOOL_SLAB(Shape.SLAB, "wool_slab", WOOL),
    WOOL_STAIRS(Shape.STAIRS, "wool_stairs", WOOL),
    CONCRETE_SLAB(Shape.SLAB, "concrete_slab", CONCRETE),
    CONCRETE_STAIRS(Shape.STAIRS, "concrete_stairs", CONCRETE),
    CONCRETE_POWDER_SLAB(Shape.SLAB, "concrete_powder_slab", CONCRETE_POWDER),
    CONCRETE_POWDER_STAIRS(Shape.STAIRS, "concrete_powder_stairs", CONCRETE_POWDER),
    OAK_SLAB(Shape.SLAB, "oak_slab", OAK_PLANKS),
    OAK_STAIRS(Shape.STAIRS, "oak_stairs", OAK_PLANKS),
    BIRCH_SLAB(Shape.SLAB, "birch_slab", BIRCH_PLANKS),
    BIRCH_STAIRS(Shape.STAIRS, "birch_stairs", BIRCH_PLANKS),
    SPRUCE_SLAB(Shape.SLAB, "spruce_slab", SPRUCE_PLANKS),
    SPRUCE_STAIRS(Shape.STAIRS, "spruce_stairs", SPRUCE_PLANKS),
    JUNGLE_SLAB(Shape.SLAB, "jungle_slab", JUNGLE_PLANKS),
    JUNGLE_STAIRS(Shape.STAIRS, "jungle_stairs", JUNGLE_PLANKS),
    ACACIA_SLAB(Shape.SLAB, "acacia_slab", ACACIA_PLANKS),
    ACACIA_STAIRS(Shape.STAIRS, "acacia_stairs", ACACIA_PLANKS),
    DARK_OAK_SLAB(Shape.SLAB, "dark_oak_slab", DARK_OAK_PLANKS),
    DARK_OAK_STAIRS(Shape.STAIRS, "dark_oak_stairs", DARK_OAK_PLANKS),
    /** Half a dyed crafting table, which works as a crafting table too. */
    CRAFTING_TABLE_SLAB(Shape.SLAB, "crafting_table_slab", CRAFTING_TABLE, (color, p) -> new DyedCraftingSlabBlock(p)),
    ;

    /** The creative tab a kind is listed in. */
    public enum Tab {
        BLOCKS,
        TREES,
        PLANTS,
        /** Slabs, stairs and walls. */
        SHAPES,
        /** Not listed, and no item. */
        NONE
    }

    /** The part of a block a slab, stairs or wall kind is. */
    public enum Shape {
        SLAB,
        STAIRS,
        WALL
    }

    private final String id;
    private final String vanilla;
    private final Tab tab;
    private final BiFunction<MixColor, BlockBehaviour.Properties, Block> factory;
    /** For a slab, stairs or wall: its shape and the kind it is made from; otherwise null. */
    private final @Nullable Shape shape;
    private final @Nullable Kind base;

    Kind(String id, String vanilla, Tab tab, BiFunction<MixColor, BlockBehaviour.Properties, Block> factory) {
        this.id = id;
        this.vanilla = vanilla;
        this.tab = tab;
        this.factory = factory;
        this.shape = null;
        this.base = null;
    }

    /**
     * A slab, stairs or wall of the kind {@code base}. It copies the properties of the dyed block of the same color, so
     * a glowstone slab glows and an ice slab is slippery, and its vanilla block is the base kind's.
     */
    Kind(Shape shape, String id, Kind base) {
        this(shape, id, base, switch (shape) {
            case SLAB -> (color, p) -> new SlabBlock(p);
            case STAIRS -> (color, p) -> new DyedStairBlock(ModBlocks.get(base, color).get().defaultBlockState(), p);
            // Not occluding, so the game skips caching the face-occlusion shapes of a wall's 324 states. That cache is
            // most of what walls cost in memory, and walls rarely hide a neighbor's face anyway.
            case WALL -> (color, p) -> new WallBlock(p.noOcclusion());
        });
    }

    /** A slab, stairs or wall made by {@code factory}. */
    Kind(Shape shape, String id, Kind base, BiFunction<MixColor, BlockBehaviour.Properties, Block> factory) {
        this.id = id;
        this.vanilla = base.vanilla;
        this.tab = Tab.SHAPES;
        this.factory = factory;
        this.shape = shape;
        this.base = base;
    }

    public String id() {
        return id;
    }

    public Tab tab() {
        return tab;
    }

    public boolean hasItem() {
        return tab != Tab.NONE;
    }

    /** The vanilla block this kind copies. For a slab, stairs or wall, that is the vanilla block of its base kind. */
    public Block vanilla() {
        return BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(vanilla));
    }

    /** The slab, stairs or wall this kind is, or null for a full block. */
    public @Nullable Shape shape() {
        return shape;
    }

    /** The kind a slab, stairs or wall is made from, or null for a full block. */
    public @Nullable Kind base() {
        return base;
    }

    /**
     * The vanilla item a dyed block washes back to: the vanilla block it copies, or for a slab, stairs or wall the
     * vanilla one of the same name, if vanilla has one (there is no vanilla glass slab, for example).
     */
    public Optional<Item> washed() {
        if (shape == null) {
            return Optional.of(vanilla().asItem());
        }
        return BuiltInRegistries.ITEM.getOptional(Identifier.withDefaultNamespace(id));
    }

    public Block create(MixColor color, BlockBehaviour.Properties properties) {
        return factory.apply(color, properties);
    }

    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> simple() {
        return (color, p) -> new Block(p);
    }

    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> log() {
        return (color, p) -> new RotatedPillarBlock(p);
    }

    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> leaves() {
        return (color, p) -> new TintedParticleLeavesBlock(0.01F, p);
    }

    /** A small flower with the vanilla flower's suspicious stew effects. */
    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> flower(String vanilla) {
        return (color, p) -> new FlowerBlock(((FlowerBlock) BuiltInRegistries.BLOCK
            .getValue(Identifier.withDefaultNamespace(vanilla))).getSuspiciousEffects(), p);
    }

    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> fence() {
        return (color, p) -> new FenceBlock(p);
    }

    /**
     * Each colored sapling grows a tree of its own wood and color. The trees are data: see
     * {@code data/moredyes/worldgen/configured_feature/<wood>_<hex>.json}.
     */
    private static BiFunction<MixColor, BlockBehaviour.Properties, Block> sapling(Wood wood) {
        return (color, p) -> {
            String name = color.id(wood.id());
            ResourceKey<ConfiguredFeature<?, ?>> tree = ResourceKey.create(Registries.CONFIGURED_FEATURE,
                Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, name));
            TreeGrower grower = new TreeGrower(MoreDyes.MOD_ID + ":" + name, Optional.empty(), Optional.of(tree),
                Optional.empty());
            return new SaplingBlock(grower, p);
        };
    }

}

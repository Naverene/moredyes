package net.neverandy.moredyes.screenshots;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.AABB;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.api.MoreDyesAPI;

/**
 * Dev only (./gradlew runScreenshots, not in the mod's jar). Fabric's client game tests don't exist for 1.21.1, so
 * this does by hand what the 1.21.11 build's MoreDyesClientGameTest does: it makes a creative superflat world, checks
 * what the NeoForge build gets from its data maps, loot modifier, block hook and recipe order, builds a small scene
 * (dyed blocks, slabs, stairs and walls, glass and panes, a double chest, a sign, pistons, plants and dyed sheep), takes
 * screenshots of it into run/screenshots and closes the game. A failed check is logged as an error and stops the game.
 */
public class MoreDyesScreenshots implements ClientModInitializer {

    private static final int Y = -60;
    private static final String[] SHOTS = {"moredyes-scene", "moredyes-chests-sign", "moredyes-sheep", "moredyes-beacons"};
    private static final String[] VIEWS = {"0.5 -59 -4.5 0 15", "-0.5 -59 -0.5 0 35", "-6 -59 -1.5 0 25",
        "13.5 -59 -6.5 0 -35"};

    private boolean started;
    private int ticks = -1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(Minecraft client) {
        if (!started) {
            // The title screen, or on a first start the accessibility screen before it, once loading is done.
            if (client.screen != null && client.getOverlay() == null) {
                started = true;
                createWorld(client);
            }
            return;
        }
        MinecraftServer server = client.getSingleplayerServer();
        if (server == null || client.player == null || client.level == null) {
            return;
        }
        ticks++;
        if (ticks == 40) {
            server.execute(() -> setUp(server));
        }
        // Long enough for the chunks to draw and the recipe toast to go away; then each view gets two seconds.
        for (int i = 0; i < SHOTS.length; i++) {
            int at = 240 + i * 60;
            if (ticks == at - 40) {
                String view = VIEWS[i];
                server.execute(() -> run(server, "tp @a " + view));
            }
            if (ticks == at) {
                Screenshot.grab(client.gameDirectory, SHOTS[i] + ".png", client.getMainRenderTarget(),
                    message -> MoreDyes.LOGGER.info("Screenshot: {}", message.getString()));
            }
        }
        if (ticks == 240 + SHOTS.length * 60 - 10) {
            server.execute(() -> checkBeacons(server));
        }
        if (ticks == 240 + SHOTS.length * 60) {
            MoreDyes.LOGGER.info("More Dyes screenshots taken into {}", new File(client.gameDirectory, "screenshots"));
            client.stop();
        }
    }

    private static void createWorld(Minecraft client) {
        GameRules rules = new GameRules();
        rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
        rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
        LevelSettings settings = new LevelSettings("moredyes-screenshots-" + System.currentTimeMillis(), GameType.CREATIVE,
            false, Difficulty.PEACEFUL, true, rules, WorldDataConfiguration.DEFAULT);
        client.createWorldOpenFlows().createFreshLevel(settings.levelName(), settings, new WorldOptions(0L, false, false),
            registries -> registries.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value()
                .createWorldDimensions(),
            client.screen);
    }

    private static void setUp(MinecraftServer server) {
        checkData(server);
        run(server, "gamerule sendCommandFeedback false");
        run(server, "time set noon");
        run(server, "recipe give @a *");

        String[] blocks = {"wool_c56685", "wool_334c59", "stone_bricks_4066a5", "oak_planks_ecbf99", "bricks_8c72d8",
            "concrete_b2d926", "quartz_block_f9bfd2", "glowstone_deb233", "bookshelf_7f4033", "crafting_table_9946c5",
            "terracotta_598cb9", "obsidian_d265bf", "sandstone_e57f6c"};
        for (int i = 0; i < blocks.length; i++) {
            setBlock(server, -6 + i, Y, 6, blocks[i]);
        }
        String[] upper = {"stone_brick_slab_4066a5", "stone_brick_stairs_4066a5[facing=south]", "cobblestone_wall_c56685",
            "oak_leaves_b2d926[persistent=true]", "oak_log_7f4033", "glass_d9a6ec", "foggy_glass_a6bfcc",
            "glass_pane_e57f6c", "piston_c56685[facing=up]", "sticky_piston_334c59[facing=up]", "ice_99a6d9",
            "crafting_table_slab_9946c5", "gravel_bf9fd9"};
        for (int i = 0; i < upper.length; i++) {
            setBlock(server, -6 + i, Y + 1, 6, upper[i]);
        }
        // A double chest and a single one, a sign and some plants in front of the wall.
        setBlock(server, -3, Y, 3, "chest_c56685[facing=north,type=left]");
        setBlock(server, -2, Y, 3, "chest_c56685[facing=north,type=right]");
        setBlock(server, 0, Y, 3, "chest_5945b2[facing=north]");
        run(server, "setblock 2 " + Y + " 3 moredyes:sign_b2d926[rotation=8]"
            + "{front_text:{messages:['\"More\"','\"Dyes\"','\"Fabric\"','\"1.21.1\"']}}");
        setBlock(server, 3, Y, 3, "potted_tulip_b2d926");
        setBlock(server, 4, Y, 3, "tulip_c56685");
        setBlock(server, 5, Y, 3, "poppy_4066a5");
        setBlock(server, 6, Y, 3, "oak_sapling_d265bf");

        // Dyed sheep that stand still.
        ServerLevel level = server.overworld();
        int[] colors = {0xC56685, 0x334C59, 0xB2D926};
        for (int i = 0; i < colors.length; i++) {
            Sheep sheep = EntityType.SHEEP.create(level);
            sheep.setPos(-4.5 - i * 1.6, Y, 2.5);
            sheep.setNoAi(true);
            sheep.setYRot(180);
            sheep.setYHeadRot(180);
            MoreDyesAPI.setSheepColor(sheep, colors[i]);
            level.addFreshEntity(sheep);
        }
        // Beacons shining through dyed glass and a dyed pane.
        for (int i = 0; i < BEACON_GLASS.length; i++) {
            int x = BEACON_X + i * 4;
            run(server, "fill " + (x - 1) + " " + Y + " " + (BEACON_Z - 1) + " " + (x + 1) + " " + Y + " " + (BEACON_Z + 1)
                + " minecraft:iron_block");
            run(server, "setblock " + x + " " + (Y + 1) + " " + BEACON_Z + " minecraft:beacon");
            setBlock(server, x, Y + 3, BEACON_Z, BEACON_GLASS[i]);
        }
        // Dyed items in the hotbar.
        String[] items = {"chest_c56685", "dye_c56685", "wool_334c59", "sign_b2d926", "oak_sapling_d265bf",
            "glass_pane_e57f6c", "piston_c56685", "tulip_c56685", "stone_brick_stairs_4066a5"};
        for (int i = 0; i < items.length; i++) {
            run(server, "item replace entity @a hotbar." + i + " with moredyes:" + items[i]);
        }
    }

    /**
     * Checks what the NeoForge build gets from NeoForge: eight dyed planks make a dyed chest rather than vanilla's
     * (recipe order), dyed planks burn and dyed leaves compost (data maps), a shorn dyed sheep drops dyed wool (loot
     * modifier) and dyed obsidian makes a portal frame (block hook).
     */
    private static void checkData(MinecraftServer server) {
        ServerLevel level = server.overworld();
        ItemStack planks = new ItemStack(item("oak_planks_c56685"));
        List<ItemStack> grid = new ArrayList<>(Collections.nCopies(9, planks));
        grid.set(4, ItemStack.EMPTY);
        CraftingInput input = CraftingInput.of(3, 3, grid);
        ItemStack result = server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level)
            .map(holder -> holder.value().assemble(input, server.registryAccess()))
            .orElse(ItemStack.EMPTY);
        check(result.is(item("chest_c56685")), "eight dyed planks make " + result);
        check(Integer.valueOf(300).equals(AbstractFurnaceBlockEntity.getFuel().get(planks.getItem())),
            "dyed planks burn for 300 ticks");
        check(ComposterBlock.COMPOSTABLES.getFloat(item("oak_leaves_c56685")) == 0.3F, "dyed leaves compost");

        BlockPos at = new BlockPos(20, Y, -30);
        Sheep sheep = EntityType.SHEEP.create(level);
        sheep.setPos(at.getX() + 0.5, Y, at.getZ() + 0.5);
        level.addFreshEntity(sheep);
        MoreDyesAPI.setSheepColor(sheep, 0x334C59);
        sheep.shear(SoundSource.PLAYERS);
        List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(at).inflate(3));
        check(!drops.isEmpty() && drops.stream().allMatch(drop -> drop.getItem().is(item("wool_334c59"))),
            "a shorn dyed sheep drops " + drops.stream().map(ItemEntity::getItem).toList());
        drops.forEach(ItemEntity::discard);
        sheep.discard();

        Block obsidian = BuiltInRegistries.BLOCK.get(id("obsidian_d265bf"));
        BlockPos frame = new BlockPos(30, Y, -30);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                if (x == 0 || x == 3 || y == 0 || y == 4) {
                    level.setBlockAndUpdate(frame.offset(x, y, 0), obsidian.defaultBlockState());
                }
            }
        }
        check(PortalShape.findEmptyPortalShape(level, frame.offset(1, 1, 0), Direction.Axis.X).isPresent(),
            "dyed obsidian makes a portal frame");
        MoreDyes.LOGGER.info("More Dyes checks passed: recipe order, fuel, compost, sheep wool, portal frame");
    }

    private static final String[] BEACON_GLASS = {"glass_c56685", "foggy_glass_334c59", "glass_pane_b2d926"};
    private static final int BEACON_X = 9;
    private static final int BEACON_Z = 4;

    /** Checks that the beam of each beacon takes the exact color of the dyed glass above it. */
    private static void checkBeacons(MinecraftServer server) {
        for (int i = 0; i < BEACON_GLASS.length; i++) {
            BlockPos pos = new BlockPos(BEACON_X + i * 4, Y + 1, BEACON_Z);
            BeaconBlockEntity beacon = (BeaconBlockEntity) server.overworld().getBlockEntity(pos);
            List<BeaconBlockEntity.BeaconBeamSection> sections = beacon.getBeamSections();
            String hex = BEACON_GLASS[i].substring(BEACON_GLASS[i].lastIndexOf('_') + 1);
            int expected = 0xFF000000 | MoreDyesAPI.displayColor(Integer.parseInt(hex, 16));
            int got = sections.isEmpty() ? 0 : sections.get(sections.size() - 1).getColor();
            check(got == expected, "beacon beam through " + BEACON_GLASS[i] + " is " + Integer.toHexString(got)
                + ", not " + Integer.toHexString(expected));
        }
        MoreDyes.LOGGER.info("More Dyes checks passed: beacon beams through dyed glass and panes");
    }

    private static Item item(String name) {
        return BuiltInRegistries.ITEM.get(id(name));
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, name);
    }

    private static void check(boolean condition, String what) {
        if (!condition) {
            MoreDyes.LOGGER.error("More Dyes check failed: {}", what);
            Minecraft.getInstance().execute(Minecraft.getInstance()::stop);
            throw new AssertionError("Failed: " + what);
        }
    }

    private static void setBlock(MinecraftServer server, int x, int y, int z, String block) {
        run(server, "setblock " + x + " " + y + " " + z + " moredyes:" + block);
    }

    private static void run(MinecraftServer server, String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}

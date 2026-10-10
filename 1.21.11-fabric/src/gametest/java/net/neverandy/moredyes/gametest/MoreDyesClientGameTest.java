package net.neverandy.moredyes.gametest;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;

import net.neverandy.moredyes.api.MoreDyesAPI;

/**
 * Builds a small scene in a creative superflat world (dyed blocks, slabs, stairs and walls, glass and panes, a double
 * chest, a sign, pistons, a tree's blocks, flowers and dyed sheep) and takes screenshots of it, so the tinting can be
 * checked by eye. Run with ./gradlew runClientGameTest.
 */
public class MoreDyesClientGameTest implements FabricClientGameTest {

    private static final int Y = -60;

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder()
            .adjustSettings(settings -> settings.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE))
            .create()) {
            world.getClientWorld().waitForChunksRender();
            var server = world.getServer();
            server.runOnServer(MoreDyesClientGameTest::checkData);
            server.runCommand("gamerule advance_time false");
            server.runCommand("time set noon");
            server.runCommand("weather clear");
            server.runCommand("gamerule send_command_feedback false");
            server.runCommand("recipe give @a *");

            String[] blocks = {"wool_c56685", "wool_334c59", "stone_bricks_4066a5", "oak_planks_ecbf99",
                "bricks_8c72d8", "concrete_b2d926", "quartz_block_f9bfd2", "glowstone_deb233", "bookshelf_7f4033",
                "crafting_table_9946c5", "terracotta_598cb9", "obsidian_d265bf", "sandstone_e57f6c"};
            for (int i = 0; i < blocks.length; i++) {
                setBlock(server, -6 + i, Y, 6, blocks[i]);
            }
            String[] upper = {"stone_brick_slab_4066a5", "stone_brick_stairs_4066a5[facing=south]",
                "cobblestone_wall_c56685", "oak_leaves_b2d926[persistent=true]", "oak_log_7f4033",
                "glass_d9a6ec", "foggy_glass_a6bfcc", "glass_pane_e57f6c", "piston_c56685[facing=up]",
                "sticky_piston_334c59[facing=up]", "ice_99a6d9", "crafting_table_slab_9946c5", "gravel_bf9fd9"};
            for (int i = 0; i < upper.length; i++) {
                setBlock(server, -6 + i, Y + 1, 6, upper[i]);
            }
            // A double chest and a single one, a sign and some plants in front of the wall.
            setBlock(server, -3, Y, 3, "chest_c56685[facing=north,type=left]");
            setBlock(server, -2, Y, 3, "chest_c56685[facing=north,type=right]");
            setBlock(server, 0, Y, 3, "chest_5945b2[facing=north]");
            server.runCommand("setblock 2 " + Y + " 3 moredyes:sign_b2d926[rotation=8]"
                + "{front_text:{messages:[\"More\",\"Dyes\",\"Fabric\",\"1.21.11\"]}}");
            setBlock(server, 4, Y, 3, "tulip_c56685");
            setBlock(server, 5, Y, 3, "poppy_4066a5");
            setBlock(server, 6, Y, 3, "oak_sapling_d265bf");
            setBlock(server, 3, Y, 3, "potted_tulip_b2d926");

            // Dyed sheep that stand still.
            server.runOnServer(s -> {
                ServerLevel level = s.overworld();
                int[] colors = {0xC56685, 0x334C59, 0xB2D926};
                for (int i = 0; i < colors.length; i++) {
                    Sheep sheep = EntityType.SHEEP.create(level, EntitySpawnReason.COMMAND);
                    sheep.setPos(-4.5 - i * 1.6, Y, 2.5);
                    sheep.setNoAi(true);
                    sheep.setYRot(180);
                    sheep.setYHeadRot(180);
                    MoreDyesAPI.setSheepColor(sheep, colors[i]);
                    level.addFreshEntity(sheep);
                }
            });
            // Dyed items in the hotbar.
            String[] items = {"chest_c56685", "dye_c56685", "wool_334c59", "sign_b2d926", "oak_sapling_d265bf",
                "glass_pane_e57f6c", "piston_c56685", "tulip_c56685", "stone_brick_stairs_4066a5"};
            for (int i = 0; i < items.length; i++) {
                server.runCommand("item replace entity @a hotbar." + i + " with moredyes:" + items[i]);
            }

            server.runCommand("tp @a 0.5 " + (Y + 1) + " -4.5 0 15");
            // Long enough for the recipe toast to go away.
            context.waitTicks(200);
            world.getClientWorld().waitForChunksRender();
            context.takeScreenshot("moredyes-scene");

            server.runCommand("tp @a -0.5 " + (Y + 1) + " -0.5 0 35");
            context.waitTicks(20);
            world.getClientWorld().waitForChunksRender();
            context.takeScreenshot("moredyes-chests-sign");

            server.runCommand("tp @a -6 " + (Y + 1) + " -1.5 0 25");
            context.waitTicks(20);
            world.getClientWorld().waitForChunksRender();
            context.takeScreenshot("moredyes-sheep");
        }
    }

    /**
     * Checks what the NeoForge build gets from its data maps and recipe priorities: eight dyed planks make a dyed
     * chest rather than vanilla's, and dyed planks burn and dyed leaves compost.
     */
    private static void checkData(MinecraftServer server) {
        ItemStack planks = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse("moredyes:oak_planks_c56685")));
        List<ItemStack> grid = new ArrayList<>(Collections.nCopies(9, planks));
        grid.set(4, ItemStack.EMPTY);
        ItemStack result = server.getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, CraftingInput.of(3, 3, grid), server.overworld())
            .map(holder -> holder.value().assemble(CraftingInput.of(3, 3, grid), server.registryAccess()))
            .orElse(ItemStack.EMPTY);
        check(BuiltInRegistries.ITEM.getKey(result.getItem()).toString().equals("moredyes:chest_c56685"),
            "eight dyed planks make " + result);
        check(server.fuelValues().burnDuration(planks) == 300, "dyed planks burn for 300 ticks");
        Item leaves = BuiltInRegistries.ITEM.getValue(Identifier.parse("moredyes:oak_leaves_c56685"));
        check(ComposterBlock.COMPOSTABLES.getFloat(leaves) == 0.3F, "dyed leaves compost");
    }

    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("Failed: " + what);
        }
    }

    private static void setBlock(net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,
        int x, int y, int z, String block) {
        server.runCommand("setblock " + x + " " + y + " " + z + " moredyes:" + block);
    }
}

package net.neverandy.moredyes.mixin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.google.gson.JsonElement;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import net.neverandy.moredyes.registry.DataMaps;

/**
 * Recipe priorities, which NeoForge reads from {@code data/neoforge/recipe_priorities.json} and Fabric has no
 * equivalent of. Dyed planks are in {@code #minecraft:planks}, so the vanilla chest, crafting table, sign, bookshelf,
 * piston and fence recipes also take them; the dyed versions of those recipes are listed there so they win. The game
 * uses the first recipe that matches, so this puts the listed recipes first (highest priority first).
 */
@Mixin(RecipeManager.class)
abstract class RecipeManagerMixin {

    @Unique
    private static Map<Identifier, Integer> moredyes$priorities;

    @ModifyArg(
        method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeMap;create(Ljava/lang/Iterable;)Lnet/minecraft/world/item/crafting/RecipeMap;"))
    private Iterable<RecipeHolder<?>> moredyes$prioritize(Iterable<RecipeHolder<?>> recipes) {
        Map<Identifier, Integer> priorities = moredyes$priorities();
        List<RecipeHolder<?>> sorted = new ArrayList<>();
        recipes.forEach(sorted::add);
        // A stable sort, so recipes of the same priority keep the game's order.
        sorted.sort(Comparator.comparingInt(holder -> -priorities.getOrDefault(holder.id().identifier(), 0)));
        return sorted;
    }

    @Unique
    private static Map<Identifier, Integer> moredyes$priorities() {
        if (moredyes$priorities == null) {
            Map<Identifier, Integer> priorities = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : DataMaps.read("recipe_priorities.json")
                .getAsJsonObject("entries").entrySet()) {
                priorities.put(Identifier.parse(entry.getKey()), entry.getValue().getAsInt());
            }
            moredyes$priorities = priorities;
        }
        return moredyes$priorities;
    }
}

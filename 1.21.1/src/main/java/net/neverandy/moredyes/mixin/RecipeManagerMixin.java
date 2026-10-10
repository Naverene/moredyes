package net.neverandy.moredyes.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import net.neverandy.moredyes.MoreDyes;

/**
 * Puts More Dyes' recipes ahead of every other recipe of their type. Vanilla uses the first recipe that matches, and
 * dyed planks are also in the {@code minecraft:planks} tag (so they make sticks, and work in other mods' recipes), so
 * eight dyed planks match both vanilla's chest and the dyed chest; this way they make the dyed one. More Dyes' recipes
 * only take dyed blocks or More Dyes dyes, so they never take another recipe's place for vanilla items.
 */
@Mixin(RecipeManager.class)
abstract class RecipeManagerMixin {

    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        at = @At("RETURN"))
    private void moredyes$ownRecipesFirst(Map<ResourceLocation, JsonElement> recipes, ResourceManager resources,
        ProfilerFiller profiler, CallbackInfo ci) {
        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> ordered = ImmutableMultimap.builder();
        for (Map.Entry<RecipeType<?>, RecipeHolder<?>> entry : byType.entries()) {
            if (MoreDyes.MOD_ID.equals(entry.getValue().id().getNamespace())) {
                ordered.put(entry);
            }
        }
        for (Map.Entry<RecipeType<?>, RecipeHolder<?>> entry : byType.entries()) {
            if (!MoreDyes.MOD_ID.equals(entry.getValue().id().getNamespace())) {
                ordered.put(entry);
            }
        }
        byType = ordered.build();
    }
}

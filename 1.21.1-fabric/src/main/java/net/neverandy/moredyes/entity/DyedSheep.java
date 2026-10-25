package net.neverandy.moredyes.entity;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.neverandy.moredyes.Config;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Lets vanilla sheep wear any More Dyes color. Vanilla sheep only know the 16 vanilla colors, so the More Dyes color
 * is kept in a Fabric data attachment on the sheep, which is saved with it and synced to the players who can see it.
 * The client draws the wool in that color (see {@code SheepFurLayerMixin}), and the vanilla wool the sheep drops,
 * when shorn or killed, is swapped for the dyed wool (by {@code EntityMixin}). Spawning and breeding are hooked in {@code SheepMixin}.
 */
public final class DyedSheep {

    /** The sheep's More Dyes color as a hex code; absent if it has a vanilla color. */
    private static final AttachmentType<String> FLEECE = AttachmentRegistry.create(
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "fleece"),
        builder -> builder.persistent(Codec.STRING).syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all()));

    /** Every More Dyes color's hex code, for WildSheepColors. */
    private static final List<String> ALL_HEX = MixColors.ALL.stream().map(MixColor::hex).toList();

    private DyedSheep() {}

    public static void register() {
        UseEntityCallback.EVENT.register(DyedSheep::onInteract);
    }

    public static Optional<MixColor> getColor(Sheep sheep) {
        String hex = sheep.getAttached(FLEECE);
        return hex == null ? Optional.empty() : MixColors.byHex(hex);
    }

    /** Sets the sheep's More Dyes color, or clears it with null. Call on the server; it syncs to clients. */
    public static void setColor(Sheep sheep, MixColor color) {
        if (color == null) {
            sheep.removeAttached(FLEECE);
        } else {
            sheep.setAttached(FLEECE, color.hex());
        }
    }

    /** A vanilla dye on a sheep with a More Dyes color gives it that vanilla color again. */
    private static InteractionResult onInteract(Player player, Level level, InteractionHand hand, Entity target,
        net.minecraft.world.phys.EntityHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(target instanceof Sheep sheep) || !(stack.getItem() instanceof DyeItem dyeItem) || player.isSpectator()
            || !sheep.isAlive() || sheep.isSheared() || getColor(sheep).isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            setColor(sheep, null);
            sheep.setColor(dyeItem.getDyeColor());
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Some sheep spawn in the world with a More Dyes color, as set in the config (see WildSheepColors): wild ones, and like
     * vanilla's random colors also those from spawn eggs, spawners and /summon. Called by SheepMixin.
     */
    public static void onSpawn(Sheep sheep, MobSpawnType reason) {
        if (reason == MobSpawnType.BREEDING || reason == MobSpawnType.CONVERSION) {
            return;
        }
        String hex = WildSheepColors.pick(sheep.getRandom().nextDouble(), sheep.getRandom()::nextInt, ALL_HEX,
            Config.sheepSpawnChance(), Config.sheepSpawnColors(),
            Config.rareSheepSpawnChance(), Config.rareSheepSpawnColors());
        if (hex != null) {
            MixColors.byHex(hex).ifPresent(color -> setColor(sheep, color));
        }
    }

    /** A lamb takes the color of one of its parents, picked at random. Called by SheepMixin. */
    public static void onBreed(Sheep a, Sheep b, Sheep child) {
        if (getColor(a).isEmpty() && getColor(b).isEmpty()) {
            return;
        }
        Sheep parent = child.getRandom().nextBoolean() ? a : b;
        Optional<MixColor> color = getColor(parent);
        if (color.isPresent()) {
            setColor(child, color.get());
        } else {
            child.setColor(parent.getColor());
        }
    }

    /**
     * The item a sheep drops: vanilla wool from a sheep with a More Dyes color becomes that color's wool. Called by
     * EntityMixin for every item a sheep drops, whether shorn (1.21.1 doesn't use a loot table for that) or killed.
     */
    public static ItemStack drop(Sheep sheep, ItemStack stack) {
        // Without dyed wool (turned off in the config) a dyed sheep drops white wool.
        if (!isVanillaWool(stack) || !Kind.WOOL.enabled()) {
            return stack;
        }
        return getColor(sheep).map(color -> new ItemStack(ModItems.get(Kind.WOOL, color), stack.getCount()))
            .orElse(stack);
    }

    private static boolean isVanillaWool(ItemStack stack) {
        return stack.is(ItemTags.WOOL)
            && "minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace());
    }
}

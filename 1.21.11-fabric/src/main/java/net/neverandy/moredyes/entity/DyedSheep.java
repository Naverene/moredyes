package net.neverandy.moredyes.entity;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import net.neverandy.moredyes.Config;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Lets vanilla sheep wear any More Dyes color. Vanilla sheep only know the 16 vanilla colors, so the More Dyes color
 * is kept in a Fabric data attachment on the sheep, which is saved with it and synced to the players who can see it.
 * The client draws the wool in that color (see {@code SheepRenderStateMixin}), and the vanilla wool the sheep drops,
 * when shorn or killed, is swapped for the dyed wool. Spawning and breeding are hooked in {@code SheepMixin}.
 */
public final class DyedSheep {

    /** The sheep's More Dyes color as a hex code; absent if it has a vanilla color. */
    private static final AttachmentType<String> FLEECE = AttachmentRegistry.create(
        Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "fleece"),
        builder -> builder.persistent(Codec.STRING).syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all()));

    private DyedSheep() {}

    public static void register() {
        UseEntityCallback.EVENT.register(DyedSheep::onInteract);
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> dyeWool(context, drops));
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

    /** Some sheep spawn in the world with a random More Dyes color, as set in the config. Called by SheepMixin. */
    public static void onSpawn(Sheep sheep, EntitySpawnReason reason) {
        if (reason != EntitySpawnReason.NATURAL && reason != EntitySpawnReason.CHUNK_GENERATION) {
            return;
        }
        if (sheep.getRandom().nextDouble() < Config.sheepSpawnChance()) {
            setColor(sheep, MixColors.ALL.get(sheep.getRandom().nextInt(MixColors.ALL.size())));
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

    /** Turns the vanilla wool dropped by a sheep with a More Dyes color into that color's wool. */
    private static void dyeWool(LootContext context, List<ItemStack> loot) {
        if (!(context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Sheep sheep)) {
            return;
        }
        // Without dyed wool (turned off in the config) a dyed sheep drops white wool.
        getColor(sheep).filter(color -> Kind.WOOL.enabled()).ifPresent(color -> loot.replaceAll(stack -> isVanillaWool(stack)
            ? new ItemStack(ModItems.get(Kind.WOOL, color), stack.getCount())
            : stack));
    }

    private static boolean isVanillaWool(ItemStack stack) {
        return stack.is(ItemTags.WOOL)
            && "minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace());
    }
}

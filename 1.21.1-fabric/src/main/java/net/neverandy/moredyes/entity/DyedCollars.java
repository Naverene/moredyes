package net.neverandy.moredyes.entity;

import java.util.Optional;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.item.MixDyeItem;
import net.neverandy.moredyes.mixin.CatAccessor;
import net.neverandy.moredyes.mixin.WolfAccessor;

/**
 * Lets tamed wolves and cats wear a collar in any More Dyes color. Vanilla collars only know the 16 vanilla colors, so
 * the More Dyes color is kept in a Fabric data attachment on the pet, which is saved with it and synced to the players
 * who can see it, the same way as for sheep (see {@link DyedSheep}). The client draws the collar in that color (see
 * {@code WolfCollarLayerMixin} and {@code CatCollarLayerMixin}).
 */
public final class DyedCollars {

    /** The pet's More Dyes collar color as a hex code; absent if it has a vanilla color. */
    private static final AttachmentType<String> COLLAR = AttachmentRegistry.create(
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "collar"),
        builder -> builder.persistent(Codec.STRING).syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all()));

    private DyedCollars() {}

    public static void register() {
        UseEntityCallback.EVENT.register(DyedCollars::onInteract);
    }

    /** Wolves and cats are the pets with a collar. */
    public static boolean hasCollar(Entity entity) {
        return entity instanceof Wolf || entity instanceof Cat;
    }

    public static Optional<MixColor> getColor(TamableAnimal pet) {
        String hex = pet.getAttached(COLLAR);
        return hex == null ? Optional.empty() : MixColors.byHex(hex);
    }

    /** Sets the pet's More Dyes collar color, or clears it with null. Call on the server; it syncs to clients. */
    public static void setColor(TamableAnimal pet, MixColor color) {
        if (color == null) {
            pet.removeAttached(COLLAR);
        } else {
            pet.setAttached(COLLAR, color.hex());
        }
    }

    /**
     * Dyes the collar of a tamed wolf or cat that belongs to the player. This runs before the pet's own right click,
     * which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar. A vanilla dye on a
     * More Dyes collar gives it that vanilla color again, even the one hidden underneath.
     */
    private static InteractionResult onInteract(Player player, Level level, InteractionHand hand, Entity target,
        net.minecraft.world.phys.EntityHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(target instanceof TamableAnimal pet) || !hasCollar(pet) || player.isSpectator() || !pet.isAlive()
            || !pet.isTame() || !pet.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }
        boolean server = !level.isClientSide();
        if (stack.getItem() instanceof MixDyeItem dye) {
            if (getColor(pet).filter(dye.color()::equals).isPresent()) {
                return InteractionResult.PASS;
            }
            if (server) {
                setColor(pet, dye.color());
            }
        } else if (stack.getItem() instanceof DyeItem dye && getColor(pet).isPresent()) {
            if (server) {
                setColor(pet, null);
                setVanillaColor(pet, dye.getDyeColor());
            }
        } else {
            return InteractionResult.PASS;
        }
        level.playSound(player, pet, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (server) {
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }

    private static void setVanillaColor(TamableAnimal pet, DyeColor color) {
        if (pet instanceof Wolf wolf) {
            ((WolfAccessor) wolf).moredyes$setCollarColor(color);
        } else if (pet instanceof Cat cat) {
            ((CatAccessor) cat).moredyes$setCollarColor(color);
        }
    }
}

package net.neverandy.moredyes.entity;

import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.item.MixDyeItem;
import net.neverandy.moredyes.mixin.CatAccessor;
import net.neverandy.moredyes.mixin.WolfAccessor;

/**
 * Lets tamed wolves and cats wear a collar in any More Dyes color. Vanilla collars only know the 16 vanilla colors, so
 * the More Dyes color is kept in an attachment on the pet, which is saved with it and synced to the players who can
 * see it, the same way as for sheep (see {@link DyedSheep}). The client draws the collar in that color (see
 * {@code mixin/WolfCollarLayerMixin} and {@code mixin/CatCollarLayerMixin}).
 */
public final class DyedCollars {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister
        .create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MoreDyes.MOD_ID);

    /** The pet's More Dyes collar color as a hex code, or "" if it has a vanilla color. */
    private static final Supplier<AttachmentType<String>> COLLAR = ATTACHMENTS.register("collar",
        () -> AttachmentType.builder(() -> "")
            .serialize(Codec.STRING.fieldOf("hex"), hex -> !hex.isEmpty())
            .sync(ByteBufCodecs.STRING_UTF8)
            .build());

    private DyedCollars() {}

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
        NeoForge.EVENT_BUS.addListener(DyedCollars::onInteract);
    }

    /** Wolves and cats are the pets with a collar. */
    public static boolean hasCollar(Entity entity) {
        return entity instanceof Wolf || entity instanceof Cat;
    }

    public static Optional<MixColor> getColor(TamableAnimal pet) {
        return MixColors.byHex(pet.getData(COLLAR));
    }

    /** Sets the pet's More Dyes collar color, or clears it with null. Call on the server; it syncs to clients. */
    public static void setColor(TamableAnimal pet, MixColor color) {
        pet.setData(COLLAR, color == null ? "" : color.hex());
    }

    /**
     * Dyes the collar of a tamed wolf or cat that belongs to the player. This runs before the pet's own right click,
     * which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar. A vanilla dye on a
     * More Dyes collar gives it that vanilla color again, even the one hidden underneath.
     */
    private static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof TamableAnimal pet) || !hasCollar(pet)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if (!pet.isAlive() || !pet.isTame() || !pet.isOwnedBy(player)) {
            return;
        }
        boolean server = !pet.level().isClientSide();
        DyeColor vanilla = stack.get(DataComponents.DYE);
        if (stack.getItem() instanceof MixDyeItem dye) {
            if (getColor(pet).filter(dye.color()::equals).isPresent()) {
                return;
            }
            if (server) {
                setColor(pet, dye.color());
            }
        } else if (stack.getItem() instanceof DyeItem && vanilla != null && getColor(pet).isPresent()) {
            if (server) {
                setColor(pet, null);
                setVanillaColor(pet, vanilla);
            }
        } else {
            return;
        }
        pet.level().playSound(player, pet, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (server) {
            stack.consume(1, player);
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static void setVanillaColor(TamableAnimal pet, DyeColor color) {
        if (pet instanceof Wolf wolf) {
            ((WolfAccessor) wolf).moredyes$setCollarColor(color);
        } else if (pet instanceof Cat cat) {
            ((CatAccessor) cat).moredyes$setCollarColor(color);
        }
    }
}

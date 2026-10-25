package net.neverandy.moredyes.entity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.neverandy.moredyes.Config;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Lets vanilla sheep wear any More Dyes color. Vanilla sheep only know the 16 vanilla colors, so the More Dyes color
 * is kept in an attachment on the sheep, which is saved with it and synced to the players who can see it. The client
 * draws the wool in that color (see {@code mixin/SheepFurLayerMixin}), and the {@link Wool} loot modifier swaps the
 * vanilla wool the sheep drops, when shorn or killed, for the dyed wool.
 */
public final class DyedSheep {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister
        .create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MoreDyes.MOD_ID);
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister
        .create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MoreDyes.MOD_ID);

    /** The sheep's More Dyes color as a hex code, or "" if it has a vanilla color. */
    private static final Supplier<AttachmentType<String>> FLEECE = ATTACHMENTS.register("fleece",
        () -> AttachmentType.builder(() -> "")
            .serialize(Codec.STRING, hex -> !hex.isEmpty())
            .sync(ByteBufCodecs.STRING_UTF8)
            .build());

    static {
        LOOT_MODIFIERS.register("dyed_sheep_wool", () -> Wool.CODEC);
    }

    /** Every More Dyes color's hex code, for WildSheepColors. */
    private static final List<String> ALL_HEX = MixColors.ALL.stream().map(MixColor::hex).toList();

    private DyedSheep() {}

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
        LOOT_MODIFIERS.register(modBus);
        NeoForge.EVENT_BUS.addListener(DyedSheep::onInteract);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, DyedSheep::onSpawn);
        NeoForge.EVENT_BUS.addListener(DyedSheep::onBreed);
    }

    public static Optional<MixColor> getColor(Sheep sheep) {
        return MixColors.byHex(sheep.getData(FLEECE));
    }

    /** Sets the sheep's More Dyes color, or clears it with null. Call on the server; it syncs to clients. */
    public static void setColor(Sheep sheep, MixColor color) {
        sheep.setData(FLEECE, color == null ? "" : color.hex());
    }

    /** A vanilla dye on a sheep with a More Dyes color gives it that vanilla color again. */
    private static void onInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (!(event.getTarget() instanceof Sheep sheep) || !(stack.getItem() instanceof DyeItem dyeItem)
            || !sheep.isAlive() || sheep.isSheared() || getColor(sheep).isEmpty()) {
            return;
        }
        DyeColor dye = dyeItem.getDyeColor();
        if (!sheep.level().isClientSide()) {
            setColor(sheep, null);
            sheep.setColor(dye);
            stack.consume(1, event.getEntity());
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(sheep.level().isClientSide()));
        event.setCanceled(true);
    }

    /**
     * Some sheep spawn in the world with a More Dyes color, as set in the server config (see WildSheepColors): wild ones,
     * and like vanilla's random colors also those from spawn eggs, spawners and /summon.
     */
    private static void onSpawn(FinalizeSpawnEvent event) {
        if (!(event.getEntity() instanceof Sheep sheep) || event.isSpawnCancelled()
            || event.getSpawnType() == MobSpawnType.BREEDING || event.getSpawnType() == MobSpawnType.CONVERSION) {
            return;
        }
        String hex = WildSheepColors.pick(sheep.getRandom().nextDouble(), sheep.getRandom()::nextInt, ALL_HEX,
            Config.SHEEP_SPAWN_CHANCE.get(), Config.SHEEP_SPAWN_COLORS.get(),
            Config.RARE_SHEEP_SPAWN_CHANCE.get(), Config.RARE_SHEEP_SPAWN_COLORS.get());
        if (hex != null) {
            MixColors.byHex(hex).ifPresent(color -> setColor(sheep, color));
        }
    }

    /** A lamb takes the color of one of its parents, picked at random. */
    private static void onBreed(BabyEntitySpawnEvent event) {
        if (!(event.getParentA() instanceof Sheep a) || !(event.getParentB() instanceof Sheep b)
            || !(event.getChild() instanceof Sheep child)) {
            return;
        }
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
    public static final class Wool implements IGlobalLootModifier {

        public static final Wool INSTANCE = new Wool();
        public static final MapCodec<Wool> CODEC = MapCodec.unit(INSTANCE);

        private Wool() {}

        @Override
        public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> loot, LootContext context) {
            if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Sheep sheep)) {
                return loot;
            }
            // Without dyed wool (turned off in the config) a dyed sheep drops white wool.
            getColor(sheep).filter(color -> Kind.WOOL.enabled()).ifPresent(color -> loot.replaceAll(stack -> isVanillaWool(stack)
                ? new ItemStack(ModItems.get(Kind.WOOL, color).get(), stack.getCount())
                : stack));
            return loot;
        }

        @Override
        public MapCodec<? extends IGlobalLootModifier> codec() {
            return CODEC;
        }

        private static boolean isVanillaWool(ItemStack stack) {
            return stack.is(ItemTags.WOOL)
                && "minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace());
        }
    }
}

package net.neverandy.moredyes.paint;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.neverandy.moredyes.api.MoreDyesAPI;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;

/**
 * The paint gun paints blocks in the world any color.
 *
 * <ul>
 * <li>Load it like a bundle: pick up dyes and right-click them onto the gun, or pick up the gun and right-click it
 * onto dyes. The dyes fill its red, green, blue, white and black tanks (see {@link Ink}).</li>
 * <li>Use it in the air to open its screen and pick a color, the mode (paint or remove paint) and the brush size.</li>
 * <li>Use it on a block to paint it; hold the button to keep painting.</li>
 * <li>Sneak and use it on a block to copy that block's color.</li>
 * </ul>
 */
public class PaintGunItem extends Item
{
    public enum Mode
    {
        PAINT, ERASE
    }

    public static final int DEFAULT_COLOR = 0xFFFFFF;
    /** The brush is a square this many blocks wide, facing the player. */
    public static final int[] BRUSHES = {1, 3, 5};

    public PaintGunItem(Properties properties)
    {
        super(properties.stacksTo(1));
    }

    // ---- What the gun holds, kept in the stack's tag ----

    public static int getColor(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("Color") ? tag.getInt("Color") & 0xFFFFFF : DEFAULT_COLOR;
    }

    public static void setColor(ItemStack stack, int rgb)
    {
        stack.getOrCreateTag().putInt("Color", rgb & 0xFFFFFF);
    }

    public static Mode getMode(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        int mode = tag == null ? 0 : tag.getByte("Mode");
        return mode >= 0 && mode < Mode.values().length ? Mode.values()[mode] : Mode.PAINT;
    }

    public static void setMode(ItemStack stack, Mode mode)
    {
        stack.getOrCreateTag().putByte("Mode", (byte) mode.ordinal());
    }

    /** The brush width in blocks: 1, 3 or 5. */
    public static int getBrush(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        int brush = tag == null ? 1 : tag.getInt("Brush");
        for (int size : BRUSHES)
        {
            if (size == brush)
            {
                return size;
            }
        }
        return 1;
    }

    public static void setBrush(ItemStack stack, int brush)
    {
        stack.getOrCreateTag().putInt("Brush", brush);
    }

    /** The ink in each tank, see {@link Ink}. */
    public static int[] getInk(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        int[] ink = tag == null ? new int[0] : tag.getIntArray("Ink");
        return ink.length == Ink.TANKS ? ink : new int[Ink.TANKS];
    }

    public static void setInk(ItemStack stack, int[] ink)
    {
        stack.getOrCreateTag().putIntArray("Ink", ink);
    }

    // ---- Loading dyes ----

    /** Loads as many dyes from a stack as fit, and says how many. */
    public static int load(ItemStack gun, ItemStack dyes)
    {
        OptionalInt color = Ink.dyeColor(dyes);
        if (color.isEmpty() || dyes.isEmpty())
        {
            return 0;
        }
        int[] ink = getInk(gun);
        int count = Ink.dyesThatFit(ink, color.getAsInt(), dyes.getCount());
        if (count > 0)
        {
            int[] add = Ink.perDye(color.getAsInt());
            for (int i = 0; i < Ink.TANKS; i++)
            {
                ink[i] += add[i] * count;
            }
            setInk(gun, ink);
            dyes.shrink(count);
        }
        return count;
    }

    /** Carrying dyes and right-clicking them onto the gun. */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack gun, ItemStack carried, Slot slot, ClickAction action, Player player, SlotAccess access)
    {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player) || Ink.dyeColor(carried).isEmpty())
        {
            return false;
        }
        if (load(gun, carried) > 0)
        {
            player.playSound(SoundEvents.BOTTLE_FILL, 0.8F, 1.0F);
        }
        return true;
    }

    /** Carrying the gun and right-clicking it onto dyes. */
    @Override
    public boolean overrideStackedOnOther(ItemStack gun, Slot slot, ClickAction action, Player player)
    {
        ItemStack dyes = slot.getItem();
        if (action != ClickAction.SECONDARY || !slot.allowModification(player) || Ink.dyeColor(dyes).isEmpty())
        {
            return false;
        }
        if (load(gun, dyes) > 0)
        {
            slot.setChanged();
            player.playSound(SoundEvents.BOTTLE_FILL, 0.8F, 1.0F);
        }
        return true;
    }

    // ---- Using it ----

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
        {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> net.neverandy.moredyes.paint.client.PaintGunScreen.open(hand));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player == null)
        {
            return InteractionResult.PASS;
        }
        if (player.isSecondaryUseActive())
        {
            if (!level.isClientSide)
            {
                int color = pickColor(level, pos);
                setColor(stack, color);
                player.displayClientMessage(Component.translatable("item.moredyes.paint_gun.picked", hex(color)), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!level.isClientSide)
        {
            int changed = apply((ServerLevel) level, player, stack, pos, context.getClickedFace());
            if (changed > 0)
            {
                level.playSound(null, pos, getMode(stack) == Mode.PAINT ? SoundEvents.DYE_USE : SoundEvents.BUCKET_EMPTY,
                        SoundSource.PLAYERS, 0.6F, 1.0F + level.random.nextFloat() * 0.2F);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Paints or erases the blocks under the brush, and says how many changed. */
    private static int apply(ServerLevel level, Player player, ItemStack stack, BlockPos center, Direction face)
    {
        Mode mode = getMode(stack);
        int color = getColor(stack);
        int[] cost = Ink.cost(color);
        int[] ink = getInk(stack);
        boolean free = player.getAbilities().instabuild;
        int radius = getBrush(stack) / 2;
        int changed = 0;
        boolean ranDry = false;
        for (int a = -radius; a <= radius; a++)
        {
            for (int b = -radius; b <= radius; b++)
            {
                BlockPos pos = offset(center, face, a, b);
                BlockState state = level.getBlockState(pos);
                if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, face, stack))
                {
                    continue;
                }
                if (mode == Mode.ERASE)
                {
                    if (Painting.erase(level, pos))
                    {
                        changed++;
                    }
                    continue;
                }
                if (!Painting.canPaint(state))
                {
                    continue;
                }
                PaintData.Paint old = Painting.get(level, pos);
                if (old != null && old.rgb() == color)
                {
                    continue;
                }
                if (!free)
                {
                    if (!Ink.has(ink, cost))
                    {
                        ranDry = true;
                        continue;
                    }
                    for (int i = 0; i < Ink.TANKS; i++)
                    {
                        ink[i] -= cost[i];
                    }
                }
                Painting.paint(level, pos, color);
                changed++;
            }
        }
        if (!free && mode == Mode.PAINT)
        {
            setInk(stack, ink);
        }
        if (ranDry)
        {
            player.displayClientMessage(Component.translatable("item.moredyes.paint_gun.empty").withStyle(ChatFormatting.RED), true);
        }
        return changed;
    }

    /** The block a, b steps across the face that was clicked. */
    private static BlockPos offset(BlockPos center, Direction face, int a, int b)
    {
        return switch (face.getAxis())
        {
            case X -> center.offset(0, a, b);
            case Y -> center.offset(a, 0, b);
            case Z -> center.offset(a, b, 0);
        };
    }

    /** The color of a block: its paint, its More Dyes color, or the color it has on maps. */
    private static int pickColor(Level level, BlockPos pos)
    {
        PaintData.Paint paint = Painting.get(level, pos);
        if (paint != null)
        {
            return paint.rgb();
        }
        BlockState state = level.getBlockState(pos);
        OptionalInt dyed = MoreDyesAPI.getColor(state);
        if (dyed.isPresent())
        {
            return MoreDyesAPI.displayColor(dyed.getAsInt());
        }
        return state.getMapColor(level, pos).col;
    }

    // ---- Looks ----

    public static String hex(int rgb)
    {
        return String.format(Locale.ROOT, "#%06X", rgb & 0xFFFFFF);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        int color = getColor(stack);
        tooltip.add(Component.translatable("item.moredyes.paint_gun.color",
                Component.literal("■ " + hex(color)).withStyle(Style.EMPTY.withColor(color))).withStyle(ChatFormatting.GRAY));
        if (getMode(stack) == Mode.ERASE)
        {
            tooltip.add(Component.translatable("item.moredyes.paint_gun.mode.erase").withStyle(ChatFormatting.GRAY));
        }
        int brush = getBrush(stack);
        tooltip.add(Component.translatable("item.moredyes.paint_gun.brush", brush, brush).withStyle(ChatFormatting.GRAY));
        int[] ink = getInk(stack);
        for (int i = 0; i < Ink.TANKS; i++)
        {
            MutableComponent amount = Component.literal(String.format(Locale.ROOT, "%.1f", ink[i] / (double) Ink.PER_DYE));
            tooltip.add(Component.translatable("item.moredyes.paint_gun.ink." + Ink.NAMES[i], amount)
                    .withStyle(Style.EMPTY.withColor(i == Ink.BLACK ? 0x808080 : Ink.TANK_COLORS[i])));
        }
        tooltip.add(Component.translatable("item.moredyes.paint_gun.help").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        // Painting changes the ink in the tag every use; don't bob the gun each time.
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}

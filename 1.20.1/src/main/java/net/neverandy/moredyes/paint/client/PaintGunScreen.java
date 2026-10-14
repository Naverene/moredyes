package net.neverandy.moredyes.paint.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.paint.GunSettingsPacket;
import net.neverandy.moredyes.paint.Ink;
import net.neverandy.moredyes.paint.PaintGunItem;

import java.util.Locale;

/** The paint gun's screen: pick any color with sliders, a hex code or the dye colors, plus the mode and brush size. */
public class PaintGunScreen extends Screen
{
    private static final int WIDTH = 220;
    private static final int SWATCH = 13;

    private final InteractionHand hand;
    private int color;
    private PaintGunItem.Mode mode;
    private int brush;

    private final ChannelSlider[] sliders = new ChannelSlider[3];
    private EditBox hexBox;
    private Button modeButton;
    private Button brushButton;
    private boolean updating;
    private int left;
    private int top;

    private PaintGunScreen(InteractionHand hand, ItemStack gun)
    {
        super(Component.translatable("item.moredyes.paint_gun"));
        this.hand = hand;
        this.color = PaintGunItem.getColor(gun);
        this.mode = PaintGunItem.getMode(gun);
        this.brush = PaintGunItem.getBrush(gun);
    }

    public static void open(InteractionHand hand)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getItemInHand(hand).getItem() instanceof PaintGunItem)
        {
            minecraft.setScreen(new PaintGunScreen(hand, minecraft.player.getItemInHand(hand)));
        }
    }

    @Override
    protected void init()
    {
        left = (width - WIDTH) / 2;
        top = Math.max(4, (height - 206) / 2);
        int y = top + 18;
        String[] names = {"R", "G", "B"};
        for (int i = 0; i < 3; i++)
        {
            sliders[i] = addRenderableWidget(new ChannelSlider(left + 50, y + i * 22, WIDTH - 50, 20, names[i], 16 - 8 * i));
        }
        y += 66;
        hexBox = addRenderableWidget(new EditBox(font, left + 50, y + 1, 60, 18, Component.literal("Hex")));
        hexBox.setMaxLength(7);
        hexBox.setResponder(this::hexChanged);
        y += 24;
        // The 16 dye colors, as quick picks.
        for (DyeColor dye : DyeColor.values())
        {
            int rgb = dye.getTextColor();
            int x = left + (dye.getId() % 16) * SWATCH;
            addRenderableWidget(Button.builder(Component.empty(), button -> setColor(rgb))
                    .bounds(x, y, SWATCH - 1, SWATCH - 1)
                    .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("color.minecraft." + dye.getName())))
                    .build());
        }
        y += SWATCH + 6;
        modeButton = addRenderableWidget(Button.builder(Component.empty(), button ->
        {
            mode = mode == PaintGunItem.Mode.PAINT ? PaintGunItem.Mode.ERASE : PaintGunItem.Mode.PAINT;
            updateButtons();
        }).bounds(left, y, WIDTH / 2 - 2, 20).build());
        brushButton = addRenderableWidget(Button.builder(Component.empty(), button ->
        {
            int next = 0;
            for (int i = 0; i < PaintGunItem.BRUSHES.length; i++)
            {
                if (PaintGunItem.BRUSHES[i] == brush)
                {
                    next = (i + 1) % PaintGunItem.BRUSHES.length;
                }
            }
            brush = PaintGunItem.BRUSHES[next];
            updateButtons();
        }).bounds(left + WIDTH / 2 + 2, y, WIDTH / 2 - 2, 20).build());
        y += 24 + 33;
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left + WIDTH / 2 - 50, y, 100, 20).build());
        setColor(color);
        updateButtons();
    }

    private void updateButtons()
    {
        modeButton.setMessage(Component.translatable("item.moredyes.paint_gun.mode." + mode.name().toLowerCase(Locale.ROOT)));
        brushButton.setMessage(Component.translatable("item.moredyes.paint_gun.brush", brush, brush));
    }

    private void setColor(int rgb)
    {
        color = rgb & 0xFFFFFF;
        updating = true;
        for (int i = 0; i < 3; i++)
        {
            sliders[i].setChannel((color >> sliders[i].shift) & 0xFF);
        }
        String hex = PaintGunItem.hex(color);
        if (!hexBox.getValue().equalsIgnoreCase(hex))
        {
            hexBox.setValue(hex);
        }
        updating = false;
    }

    private void hexChanged(String text)
    {
        if (updating)
        {
            return;
        }
        String hex = text.startsWith("#") ? text.substring(1) : text;
        if (hex.length() == 6)
        {
            try
            {
                int rgb = Integer.parseInt(hex, 16);
                updating = true;
                color = rgb;
                for (int i = 0; i < 3; i++)
                {
                    sliders[i].setChannel((color >> sliders[i].shift) & 0xFF);
                }
                updating = false;
            }
            catch (NumberFormatException ignored)
            {
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, top + 4, 0xFFFFFF);
        // The color, big, to the left of the sliders.
        graphics.fill(left - 1, top + 17, left + 45, top + 83, 0xFF000000);
        graphics.fill(left, top + 18, left + 44, top + 82, 0xFF000000 | color);
        graphics.drawString(font, "Hex", left + 22, top + 90, 0xA0A0A0);
        super.render(graphics, mouseX, mouseY, partialTick);

        // The dye swatches are drawn over their buttons.
        int y = top + 18 + 66 + 24;
        for (DyeColor dye : DyeColor.values())
        {
            int x = left + dye.getId() * SWATCH;
            graphics.fill(x + 2, y + 2, x + SWATCH - 3, y + SWATCH - 3, 0xFF000000 | dye.getTextColor());
        }

        // The tanks, and how many blocks they paint in this color.
        ItemStack gun = minecraft.player == null ? ItemStack.EMPTY : minecraft.player.getItemInHand(hand);
        int[] ink = PaintGunItem.getInk(gun);
        int[] cost = Ink.cost(color);
        int ty = y + SWATCH + 6 + 24;
        int barWidth = (WIDTH - 4 * 4) / Ink.TANKS;
        int blocks = Integer.MAX_VALUE;
        for (int i = 0; i < Ink.TANKS; i++)
        {
            int x = left + i * (barWidth + 4);
            graphics.fill(x, ty, x + barWidth, ty + 8, 0xFF606060);
            int filled = (int) Math.round(barWidth * Math.min(1.0, ink[i] / (double) Ink.CAPACITY));
            graphics.fill(x, ty, x + filled, ty + 8, 0xFF000000 | Ink.TANK_COLORS[i]);
            graphics.drawCenteredString(font, String.format(Locale.ROOT, "%.1f", ink[i] / (double) Ink.PER_DYE), x + barWidth / 2, ty + 10, 0xC0C0C0);
            if (cost[i] > 0)
            {
                blocks = Math.min(blocks, ink[i] / cost[i]);
            }
        }
        Component left;
        if (minecraft.player != null && minecraft.player.getAbilities().instabuild)
        {
            left = Component.translatable("item.moredyes.paint_gun.creative");
        }
        else
        {
            left = Component.translatable("item.moredyes.paint_gun.blocks_left", blocks == Integer.MAX_VALUE ? 0 : blocks);
        }
        graphics.drawCenteredString(font, left, width / 2, ty + 21, 0xFFFFFF);
    }

    @Override
    public void onClose()
    {
        ModNetwork.CHANNEL.sendToServer(new GunSettingsPacket(hand, color, mode, brush));
        // Show the new settings right away instead of waiting for the server to send the stack back.
        if (minecraft.player != null && minecraft.player.getItemInHand(hand).getItem() instanceof PaintGunItem)
        {
            ItemStack gun = minecraft.player.getItemInHand(hand);
            PaintGunItem.setColor(gun, color);
            PaintGunItem.setMode(gun, mode);
            PaintGunItem.setBrush(gun, brush);
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    /** A slider for one of R, G and B. */
    private class ChannelSlider extends AbstractSliderButton
    {
        private final String name;
        final int shift;

        ChannelSlider(int x, int y, int width, int height, String name, int shift)
        {
            super(x, y, width, height, Component.empty(), 0);
            this.name = name;
            this.shift = shift;
            updateMessage();
        }

        int channel()
        {
            return (int) Math.round(value * 255);
        }

        void setChannel(int channel)
        {
            value = channel / 255.0;
            updateMessage();
        }

        @Override
        protected void updateMessage()
        {
            setMessage(Component.literal(name + ": " + channel()));
        }

        @Override
        protected void applyValue()
        {
            if (updating)
            {
                return;
            }
            setColor((color & ~(0xFF << shift)) | (channel() << shift));
        }
    }
}

package info.kg6jay.moredyes.compat.rple;

import net.minecraft.block.Block;

import com.falsepattern.rple.api.common.colorizer.RPLEBlockColorRegistry;
import com.falsepattern.rple.api.common.event.BlockColorRegistrationEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.kg6jay.moredyes.block.IBlockColored;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.MDBlockGlass;
import info.kg6jay.moredyes.block.MDBlockGlassPane;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorUtil;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Colored light with Right Proper Lighting Engine (RPLE): dyed blocks that give off light (lamps, glowstone, crying
 * obsidian and so on) shine in their own color, and dyed glass and panes tint the light that passes through. The
 * color of each block is fixed by its block and metadata, so it is registered once with RPLE's color registry.
 */
public final class RPLECompat {

    private static final int MAX = 15;

    private RPLECompat() {}

    public static void preInit() {
        // RPLE posts the event on the FML bus during its init, after every mod's preInit.
        FMLCommonHandler.instance()
            .bus()
            .register(new RPLECompat());
    }

    @SubscribeEvent
    public void registerColors(BlockColorRegistrationEvent event) {
        RPLEBlockColorRegistry registry = event.registry();
        int lights = 0, filters = 0;
        for (Object object : Block.blockRegistry) {
            Block block = (Block) object;
            if (!(block instanceof IBlockColored)) {
                continue;
            }
            String name = Block.blockRegistry.getNameForObject(block);
            if (name == null || !name.startsWith(Reference.MOD_ID + ":")) {
                continue;
            }
            IBlockColored colored = (IBlockColored) block;
            String[] colors = colorStrings(colored.getColorSet());
            boolean glass = block instanceof MDBlockGlass || block instanceof MDBlockGlassPane;
            int light = block.getLightValue();
            if (!glass && light <= 0) {
                continue;
            }
            for (int meta = 0; meta <= colored.getMaxMeta(); meta++) {
                int rgb = ColorUtil.shade(colors, meta);
                if (glass) {
                    registry.colorizeBlock(block, meta)
                        .translucency(rgb16(rgb, MAX))
                        .apply();
                    filters++;
                } else {
                    registry.colorizeBlock(block, meta)
                        .brightness(rgb16(rgb, light))
                        .apply();
                    lights++;
                }
            }
        }
        LogHelper.info("RPLE: colored " + lights + " light sources and " + filters + " glass blocks");
    }

    /**
     * An RPLE color (4 bits per channel) for a dye color. RPLE's own colors are saturated (red light is F00), and
     * the More Dyes colors are softer mixes, so the channels are stretched away from white: each is its share of
     * the brightest channel, squared. Darker dyes give dimmer light, as RPLE's grey (555) and black (111) do.
     */
    static short rgb16(int rgb, int level) {
        int r = rgb >> 16 & 0xFF, g = rgb >> 8 & 0xFF, b = rgb & 0xFF;
        int max = Math.max(r, Math.max(g, b));
        if (max == 0) {
            return 0x111;
        }
        int top = Math.max(1, (int) Math.round(level * Math.sqrt(max / 255.0)));
        return (short) (channel(r, max, top) << 8 | channel(g, max, top) << 4 | channel(b, max, top));
    }

    private static int channel(int value, int max, int top) {
        double share = value / (double) max;
        return Math.min(MAX, (int) Math.round(top * share * share));
    }

    private static String[] colorStrings(String colorSet) {
        for (int i = 0; i < MDBlock.colors.length; i++) {
            if (MDBlock.colors[i].equals(colorSet)) {
                return MDBlock.colorStrings[i];
            }
        }
        return MDBlock.colorStrings[0];
    }
}

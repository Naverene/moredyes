package info.kg6jay.moredyes.compat.storagedrawers.client;

import java.lang.reflect.Field;

import net.minecraft.util.IIcon;

import com.jaquadro.minecraft.storagedrawers.client.renderer.ModularBoxRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Storage Drawers' box renderer, which multiplies the faces showing the dyed drawers' grey textures by the dye color.
 * Every other face (upgrade overlays and the like) stays white. It is swapped into Storage Drawers' own block and item
 * renderers, so they draw dyed drawers exactly as they draw their own: this works with both the original Storage
 * Drawers and the GT: New Horizons fork, whose rendering code differs (vertical drawers, a per-thread RenderHelper).
 */
@SideOnly(Side.CLIENT)
public class TintedBoxRenderer extends ModularBoxRenderer {

    private static final String TINTED_PREFIX = Reference.MOD_ID + ":tinted/drawers/";

    private float[] tint = COLOR_WHITE;

    /** The color for the faces set from now on; the icons are set by Storage Drawers after it sets its colors. */
    public void setTint(float[] tint) {
        this.tint = tint;
    }

    private float[] colorFor(IIcon icon) {
        return icon != null && icon.getIconName()
            .startsWith(TINTED_PREFIX) ? this.tint : COLOR_WHITE;
    }

    @Override
    public void setExteriorIcon(IIcon icon) {
        super.setExteriorIcon(icon);
        this.setExteriorColor(this.colorFor(icon));
    }

    @Override
    public void setExteriorIcon(IIcon icon, int side) {
        super.setExteriorIcon(icon, side);
        this.setExteriorColor(this.colorFor(icon), side);
    }

    @Override
    public void setInteriorIcon(IIcon icon) {
        super.setInteriorIcon(icon);
        this.setInteriorColor(this.colorFor(icon));
    }

    @Override
    public void setInteriorIcon(IIcon icon, int side) {
        super.setInteriorIcon(icon, side);
        this.setInteriorColor(this.colorFor(icon), side);
    }

    @Override
    public void setCutIcon(IIcon icon) {
        super.setCutIcon(icon);
        this.setCutColor(this.colorFor(icon));
    }

    @Override
    public void setCutIcon(IIcon icon, int side) {
        super.setCutIcon(icon, side);
        this.setCutColor(this.colorFor(icon), side);
    }

    /**
     * Puts a new tinted box renderer into the (private) "boxRenderer" field that the Storage Drawers renderer class
     * declares, on this renderer, and returns it; or returns null if there is no such field, in which case the drawers
     * are drawn grey.
     */
    public static TintedBoxRenderer install(Object renderer, Class<?> declaringClass) {
        try {
            Field field = declaringClass.getDeclaredField("boxRenderer");
            if (field.getType() == ModularBoxRenderer.class) {
                field.setAccessible(true);
                TintedBoxRenderer box = new TintedBoxRenderer();
                field.set(renderer, box);
                return box;
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            // Logged below.
        }
        LogHelper.warn(
            "Could not find the box renderer in " + declaringClass.getName() + "; dyed drawers will be drawn grey");
        return null;
    }
}

package net.neverandy.moredyes.block;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

/**
 * The planks a More Dyes bed's frame is drawn in, as a block state property, which picks the frame's model: the vanilla
 * planks, and DYED for More Dyes planks, whose grey frame takes the planks' color from the block entity. Other planks
 * (another mod's) give an oak frame.
 */
public enum BedWood implements StringRepresentable {
    OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, MANGROVE, CHERRY, PALE_OAK, BAMBOO, CRIMSON, WARPED, DYED;

    private final String name = name().toLowerCase(java.util.Locale.ROOT);

    @Override
    public String getSerializedName() {
        return name;
    }

    /** The frame for planks, by their block id; null (no planks recorded) is oak. */
    public static BedWood of(@Nullable Identifier planks) {
        if (planks == null) {
            return OAK;
        }
        if ("minecraft".equals(planks.getNamespace()) && planks.getPath().endsWith("_planks")) {
            String wood = planks.getPath().substring(0, planks.getPath().length() - "_planks".length());
            for (BedWood value : values()) {
                if (value != DYED && value.name.equals(wood)) {
                    return value;
                }
            }
            return OAK;
        }
        return planks.getNamespace().equals(net.neverandy.moredyes.MoreDyes.MOD_ID) ? DYED : OAK;
    }
}

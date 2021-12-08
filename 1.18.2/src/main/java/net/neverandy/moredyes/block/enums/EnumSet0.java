package net.neverandy.moredyes.block.enums;

import net.minecraft.util.IStringSerializable;
import net.neverandy.moredyes.reference.ColorStrings;

public enum EnumSet0 implements IStringSerializable
{
    META_0(0, ColorStrings.SET_0[0]),
    META_1(1, ColorStrings.SET_0[1]),
    META_2(2, ColorStrings.SET_0[2]),
    META_3(3, ColorStrings.SET_0[3]),
    META_4(4, ColorStrings.SET_0[4]),
    META_5(5, ColorStrings.SET_0[5]),
    META_6(6, ColorStrings.SET_0[6]),
    META_7(7, ColorStrings.SET_0[7]),
    META_8(8, ColorStrings.SET_0[8]),
    META_9(9, ColorStrings.SET_0[9]),
    META_10(10, ColorStrings.SET_0[10]),
    META_11(11, ColorStrings.SET_0[11]),
    META_12(12, ColorStrings.SET_0[12]),
    META_13(13, ColorStrings.SET_0[13]),
    META_14(14, ColorStrings.SET_0[14]),
    META_15(15, ColorStrings.SET_0[15]);

    private final int ID;
    private String name="set0";

    EnumSet0(int ID, String name)
    {
        this.ID = ID;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    @Override
    public String toString()
    {
        return getName();
    }
    public int getID()
    {
        return ID;
    }

    @Override
    public String getSerializedName()
    {
        return null;
    }
}
package net.neverandy.moredyes.block.enums;

import net.neverandy.moredyes.reference.ColorStrings;
import net.minecraft.util.IStringSerializable;

public enum EnumSet5 implements IStringSerializable
{
    META_0(0, ColorStrings.SET_5[0]),
    META_1(1, ColorStrings.SET_5[1]),
    META_2(2, ColorStrings.SET_5[2]),
    META_3(3, ColorStrings.SET_5[3]),
    META_4(4, ColorStrings.SET_5[4]),
    META_5(5, ColorStrings.SET_5[5]),
    META_6(6, ColorStrings.SET_5[6]),
    META_7(7, ColorStrings.SET_5[7]),
    META_8(8, ColorStrings.SET_5[8]),
    META_9(9, ColorStrings.SET_5[9]),
    META_10(10, ColorStrings.SET_5[10]),
    META_11(11, ColorStrings.SET_5[11]),
    META_12(12, ColorStrings.SET_5[12]),
    META_13(13, ColorStrings.SET_5[13]),
    META_14(14, ColorStrings.SET_5[14]),
    META_15(15, ColorStrings.SET_5[15]);

    private final int ID;
    private String name="set5";
    
    EnumSet5(int ID, String name)
    {
        this.ID = ID;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return getName();
    }
    public int getID() {
        return ID;
    }

    @Override
    public String getSerializedName()
    {
        return null;
    }
}
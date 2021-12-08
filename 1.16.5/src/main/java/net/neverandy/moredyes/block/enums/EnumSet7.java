package net.neverandy.moredyes.block.enums;

import net.neverandy.moredyes.reference.ColorStrings;
import net.minecraft.util.IStringSerializable;

public enum EnumSet7 implements IStringSerializable
{
    META_0(0, ColorStrings.SET_7[0]),
    META_1(1, ColorStrings.SET_7[1]),
    META_2(2, ColorStrings.SET_7[2]),
    META_3(3, ColorStrings.SET_7[3]),
    META_4(4, ColorStrings.SET_7[4]),
    META_5(5, ColorStrings.SET_7[5]);

    private final int ID;
    private String name="set7";
    
    EnumSet7(int ID, String name)
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
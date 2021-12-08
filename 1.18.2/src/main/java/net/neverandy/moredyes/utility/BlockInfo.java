package net.neverandy.moredyes.utility;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.setup.ModSetup;

public class BlockInfo
{
    public String blockName;
    public float hardness;
    public SoundType sound;
    public ToolType harvestTool;
    public int harvestLevel;
    public float resistance;
    public Material blockMaterial;
    public ItemGroup tab;
    public BlockInfo(String blockName,Material mat,float h,SoundType t, ToolType toolType, int hL,float r,ItemGroup tab)
    {
        this.blockName=blockName;
        this.blockMaterial=mat;
        this.hardness=h;
        this.sound=t;
        this.harvestTool=toolType;
        this.harvestLevel=hL;
        this.resistance = r;
        this.tab=tab;
    }
    public BlockInfo(String blockName,Material mat,float h,SoundType t, ToolType s, int hL,float r)
    {
        this(blockName,mat,h,t,s,hL,r, ModSetup.tabBlocks);
    }
    public BlockInfo(String blockName,Material mat,float h,SoundType t, ToolType s, int hL)
    {
        this(blockName,mat,h,t,s,hL,1.0F);
    }
    public BlockInfo()
    {
        this("",Material.STONE,1.0f,SoundType.STONE, ToolType.HOE,1);
    }
}
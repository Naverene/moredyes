package net.neverandy.moredyes.block;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.item.MDItemBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

public class WorkbenchBlock extends BlockWorkbench
{
    private String[] colors;
    public String blockName;
    public WorkbenchBlock(String[] blockColor, BlockInfo info, int set)
    {
        super();
        this.colors = blockColor;
        this.blockName = info.blockName+"_"+set;
        setHardness(info.hardness);
        setHarvestLevel(info.harvestTool,info.harvestLevel);
        setSoundType(info.sound);
        setCreativeTab(info.tab);
        setResistance(info.resistance);
        setRegistryName(info.blockName+"_"+set);
        setUnlocalizedName(info.blockName+"."+set);
        MDItemBlock itemBlock = new MDItemBlock(this);
        //initModel(info.blockName);
    }
    @SideOnly(Side.CLIENT)
    public void initModel(String name)
    {
        for(int i=0;i<this.colors.length;i++)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),i,new ModelResourceLocation(Reference.MOD_ID+":"+name+"_"+this.colors[i],"inventory"));
        }
    }

}
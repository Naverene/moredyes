package net.neverandy.moredyes.block;

import net.minecraft.block.CraftingTableBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

public class WorkbenchBlock extends CraftingTableBlock
{
    private final String[] colors;
    public String blockName;

    public WorkbenchBlock(String[] blockColor, BlockInfo info, int set)
    {
        super(Properties.of(info.blockMaterial)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound)
                .strength(info.hardness, info.resistance));
        this.colors = blockColor;
        this.blockName = info.blockName + "_" + set;
        setRegistryName(info.blockName + "_" + set);
        //MDItemBlock itemBlock = new MDItemBlock(this);
        //initModel(info.blockName);
    }

    @OnlyIn(Dist.CLIENT)
    public void initModel(String name)
    {
        for (int i = 0; i < this.colors.length; i++)
        {
            ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID + ":" + name + "_" + this.colors[i], "inventory"));
        }
    }
}

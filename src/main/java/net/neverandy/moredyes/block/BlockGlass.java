package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.item.ItemStack;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockGlass extends AbstractGlassBlock
{
    private boolean translucent;
    private final String color;
    public String blockName;
    public BlockGlass(String blockColor, BlockInfo info, boolean translucent)
    {
        super(AbstractBlock.Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel)
                .notSolid().variableOpacity());
        this.color = blockColor;
        this.blockName=info.blockName;
        this.translucent=translucent;
    }
    /*
    @OnlyIn(Dist.CLIENT)
    public void getSubBlocks(Item itemIn, ItemGroup tab, List<ItemStack> list)
    {
        for (int i = 0; i < this.colors.length; ++i)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

     */
    public String getColor()
    {
        return this.color;
    }
    public int getColorCount()
    {
        return 118;
    }
    public String getColorName(ItemStack stack)
    {
        return this.color;
    }
    /*
    @OnlyIn(Dist.CLIENT)
    public BlockModelRenderer getBlockLayer()
    {
        if(this.translucent)
            return BlockRenderLayer.translucent;
        else
            return BlockRenderLayer.CUTOUT;
    }

     */
}
package net.neverandy.moredyes.block;

import net.minecraft.block.GlassBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockGlass extends GlassBlock
{
    private boolean translucent = false;
    private final String color;
    public String blockName;
    public BlockGlass(String blockColor, BlockInfo info, boolean translucent)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).strength(info.hardness).harvestLevel(info.harvestLevel));
        this.color = blockColor;
        this.blockName=info.blockName;
        //setRegistryName(info.blockName+"_"+set);
        //MDItemBlock itemBlock = new MDItemBlock(this);
        //initModel(info.blockName);
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
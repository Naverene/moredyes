package net.neverandy.moredyes.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.GlassBlock;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.List;

public class BlockGlass extends GlassBlock
{
    private static final boolean translucent = false;
    private final String[] colors;
    public String blockName;
    public BlockGlass(String[] blockColor, BlockInfo info, boolean translucent, int set)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).strength(info.hardness).harvestLevel(info.harvestLevel));
        this.colors = blockColor;
        this.blockName=info.blockName;
        setRegistryName(info.blockName+"_"+set);
        //MDItemBlock itemBlock = new MDItemBlock(this);
        //initModel(info.blockName);
        this.translucent=translucent;
    }
    @OnlyIn(Dist.CLIENT)
    public void initModel(String name)
    {
        for (String color : this.colors)
        {
            ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID + ":" + name + "_" + color, "inventory"));
        }
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
    public String[] getColors()
    {
        return this.colors;
    }
    public int getColorCount()
    {
        return this.colors.length;
    }
    public String getColorName(ItemStack stack)
    {
        return this.colors[stack.getDamageValue()];
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
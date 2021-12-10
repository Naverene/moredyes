package net.neverandy.moredyes.block;

import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.List;

public class BlockFalling extends FallingBlock
{
    private final String[] colors;
    private final int set;
    public String blockName;
    public BlockFalling(String[] blockColors, BlockInfo info, int set)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).strength(info.hardness));
        this.set=set;
        this.colors = blockColors;
        this.blockName = info.blockName;
        this.setRegistryName(info.blockName+"_"+set);
        //MDItemBlock itemBlock = new MDItemBlock(this);
        //initModel(info.blockName);
    }
    @OnlyIn(Dist.CLIENT)
    public void initModel(String name)
    {
        for (String color : this.colors)
        {
            ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID + ":" + name + "_" + color));
        }
    }
    /*
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
}

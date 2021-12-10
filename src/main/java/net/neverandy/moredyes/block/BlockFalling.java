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
    private final String color;
    public String blockName;
    public BlockFalling(String blockColor, BlockInfo info)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).strength(info.hardness));
        this.color = blockColor;
        this.blockName = info.blockName;
        //this.setRegistryName(name);
        //initModel(info.blockName);
    }
    @OnlyIn(Dist.CLIENT)
    public void initModel(String name)
    {
        ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID + ":" + name + "_" + this.color));
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
    public String getColor()
    {
        return this.color;
    }
    public int getColorCount()
    {
        //return this.colors.length;
        return 118;
    }
    public String getColorName(ItemStack stack)
    {
        return this.color;
    }
}

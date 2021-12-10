package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import java.util.function.ToIntFunction;

public class BasicBlock extends Block
{
    private String color;
    public String blockName;
    public BasicBlock(String blockColor, BlockInfo info)
    {
        super(Properties.of(info.blockMaterial)
                .strength(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound).lightLevel(value -> info.lightlevel));
        this.color = blockColor;
        this.blockName=info.blockName;
        //initModel(info.blockName);
    }
}
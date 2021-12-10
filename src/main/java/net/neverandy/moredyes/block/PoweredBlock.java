package net.neverandy.moredyes.block;

import net.minecraft.block.RedstoneBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

public class PoweredBlock extends RedstoneBlock
{
    public String color;
    public PoweredBlock(String blockColor, BlockInfo info)
    {
        super(Properties.of(info.blockMaterial)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound)
                .strength(info.hardness, info.resistance));
        this.color = blockColor;


    }
}
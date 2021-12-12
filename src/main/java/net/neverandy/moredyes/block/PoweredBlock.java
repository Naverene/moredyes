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
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound).setLightLevel(value -> info.lightlevel));
        this.color = blockColor;


    }
}
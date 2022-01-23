package net.neverandy.moredyes.block;

import net.minecraft.block.RedstoneBlock;
import net.neverandy.moredyes.utility.BlockInfo;

public class PoweredBlock extends RedstoneBlock
{
    public PoweredBlock(BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));


    }
}
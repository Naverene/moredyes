package net.neverandy.moredyes.block;

import net.minecraft.block.LeavesBlock;
import net.neverandy.moredyes.utility.BlockInfo;


public class LeafBlock extends LeavesBlock
{
    private final String color;
    public LeafBlock(String blockColor, BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));
        this.color = blockColor;

    }
}
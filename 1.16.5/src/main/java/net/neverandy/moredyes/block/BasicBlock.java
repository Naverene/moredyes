package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.neverandy.moredyes.utility.BlockInfo;

public class BasicBlock extends Block
{
    public String blockName;
    private String color;

    public BasicBlock(String blockColor, BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness, info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));
        this.color = blockColor;
        this.blockName = info.blockName;
    }
}
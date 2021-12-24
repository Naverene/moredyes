package net.neverandy.moredyes.block;

import net.minecraft.block.FallingBlock;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockFalling extends FallingBlock
{
    private final String color;
    public String blockName;
    public BlockFalling(String blockColor, BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));
        this.color = blockColor;
        this.blockName = info.blockName;
    }
}

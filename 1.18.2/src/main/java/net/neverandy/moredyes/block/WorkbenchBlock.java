package net.neverandy.moredyes.block;

import net.minecraft.block.CraftingTableBlock;
import net.neverandy.moredyes.utility.BlockInfo;

public class WorkbenchBlock extends CraftingTableBlock
{
    private final String color;

    public WorkbenchBlock(String blockColor, BlockInfo info)
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

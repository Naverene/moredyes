package net.neverandy.moredyes.block;

import net.minecraft.block.FenceBlock;
import net.minecraft.block.WoodType;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockFence extends FenceBlock
{
    public BlockFence(BlockInfo info)
    {
        super(Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness, info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .setRequiresTool()
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel));
        WoodType.register(info.woodType);
    }
}
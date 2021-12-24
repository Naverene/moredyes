package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BushBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.neverandy.moredyes.utility.BlockInfo;

public class FlowerBlock extends BushBlock
{
    private final String color;
    public FlowerBlock(String blockColor, BlockInfo info)
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

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public AbstractBlock.OffsetType getOffsetType()
    {
        return OffsetType.XZ;
    }
}
package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

public class FlowerBlock extends BushBlock
{
    private final String color;
    public FlowerBlock(String blockColor, BlockInfo info)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).strength(info.hardness));
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
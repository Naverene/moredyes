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
    public PoweredBlock(String[] blockColor, BlockInfo info, int set)
    {
        super(Properties.of(info.blockMaterial)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound)
                .strength(info.hardness, info.resistance));
        this.setRegistryName(info.blockName+"_"+set);


    }
    @OnlyIn(Dist.CLIENT)
    public void initModel()
    {
        ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
    }
}
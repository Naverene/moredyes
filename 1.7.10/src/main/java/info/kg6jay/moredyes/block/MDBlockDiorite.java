package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;

/**
 * Plain (undyed) diorite, the base for dyed diorite. It generates in the world and has a crafting recipe only when
 * no other mod provides diorite. Unlike the dyed blocks it follows the vanilla mob spawning rules, because it
 * generates underground like stone.
 */
public class MDBlockDiorite extends Block {

    public MDBlockDiorite(BlockInfo info) {
        super(info.blockMaterial);
        this.setHardness(info.hardness);
        this.setResistance(info.resistance);
        this.setHarvestLevel(info.harvestTool, info.harvestLevel);
        this.setStepSound(info.sound);
        this.setBlockName(Reference.MOD_ID + ".diorite");
        this.setCreativeTab(info.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = TintedTextures.register(iconRegister, "diorite");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }
}

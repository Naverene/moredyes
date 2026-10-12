package info.kg6jay.moredyes.block;

import java.util.List;

import net.minecraft.block.BlockIce;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.utility.BlockInfo;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * Dyed ice. Like vanilla ice it is slippery and see-through, melts next to bright light, and turns to water when
 * broken without Silk Touch (with Silk Touch it drops itself in its shade).
 */
public class MDBlockIce extends BlockIce implements IBlockColored {

    @SideOnly(Side.CLIENT)
    protected IIcon icon;
    protected String[] blockColors;
    protected String blockName, colorSet;

    public MDBlockIce(String[] colors, BlockInfo info, String colorSet) {
        this.blockColors = colors;
        this.blockName = info.blockName;
        this.colorSet = colorSet;
        this.setHardness(info.hardness);
        this.setHarvestLevel(info.harvestTool, info.harvestLevel);
        this.setStepSound(info.sound);
        this.setResistance(info.resistance);
        this.setLightOpacity(3);
        char tmp = (char) (((int) this.blockName.charAt(0)) - 32);
        this.setBlockName(colorSet + "Mix" + tmp + this.blockName.substring(1));
        this.setCreativeTab(info.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < blockColors.length; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return ColorUtil.shade(this.blockColors, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.getRenderColor(world.getBlockMetadata(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icon = TintedTextures.register(iconRegister, this.blockName);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return !ConfigHandler.preventMobSpawning && super.canCreatureSpawn(type, world, x, y, z);
    }

    @Override
    public String getColorSet() {
        return this.colorSet;
    }

    @Override
    public int getMaxMeta() {
        return this.blockColors.length - 1;
    }
}

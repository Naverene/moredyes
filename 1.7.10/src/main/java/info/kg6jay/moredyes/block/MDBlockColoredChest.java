package info.kg6jay.moredyes.block;

import java.util.List;

import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;

public class MDBlockColoredChest extends BlockChest implements IBlockColored {

    private final String[] colors;
    private final String color;

    public MDBlockColoredChest(String name, BlockInfo info, String[] colors) {
        super(info.woodType);
        this.colors = colors;
        this.color = name;
        this.setBlockName(name);
        this.setCreativeTab(info.tab);
        this.setBlockTextureName(Reference.MOD_ID + ":chest/" + name);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < colors.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "." + colors[meta];
    }

    @Override
    public String getColorSet() {
        return color;
    }

    @Override
    public int getMaxMeta() {
        return colors.length - 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMDBlockColoredChest();
    }
}

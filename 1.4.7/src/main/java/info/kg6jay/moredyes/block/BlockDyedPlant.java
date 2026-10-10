package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockFlower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * A dyed plant (flower or sapling) drawn as crossed squares in two layers: the dyed part (petals, leaves) tinted, and
 * the rest (stem, trunk) in its natural color.
 */
public class BlockDyedPlant extends BlockFlower implements IDyedBlock, ILayeredBlock {

    public static int renderId;

    protected final BlockInfo info;
    private final int tinted, natural;

    public BlockDyedPlant(int id, BlockInfo info, int tinted, int natural) {
        super(id, tinted, info.material);
        this.info = info;
        this.tinted = tinted;
        this.natural = natural;
        info.apply(this);
        this.setTextureFile(Textures.SHEET);
    }

    @Override
    public String getDyedName() {
        return this.info.displayName;
    }

    @Override
    public int getLayerCount() {
        return 2;
    }

    @Override
    public int getLayerTexture(int side, int layer) {
        return layer == 0 ? this.tinted : this.natural;
    }

    @Override
    public int getLayerColor(int color, int layer) {
        return layer == 0 ? Colors.rgb(color) : 0xFFFFFF;
    }

    @Override
    public boolean isPlant() {
        return true;
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return this.getLayerTexture(side, RenderLayer.layer());
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityDyed();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        Dyed.remember(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        return Dyed.drops(this, world, x, y, z, 1);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(this, world, x, y, z, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        Dyed.addSubBlocks(id, list);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int color) {
        return this.getLayerColor(color, RenderLayer.layer());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.getLayerColor(Dyed.get(world, x, y, z), RenderLayer.layer());
    }
}

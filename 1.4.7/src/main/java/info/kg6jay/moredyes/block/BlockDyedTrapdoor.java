package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * Dyed iron trapdoor in every color (Minecraft 1.8). Like an iron door it only opens with redstone. The metadata holds
 * the direction and whether it is open, so the color is in the tile entity.
 */
public class BlockDyedTrapdoor extends BlockTrapDoor implements IDyedBlock {

    private final BlockInfo info;

    public BlockDyedTrapdoor(int id, BlockInfo info) {
        super(id, info.material);
        this.info = info;
        info.apply(this);
        this.setTextureFile(Textures.SHEET);
        this.blockIndexInTexture = Textures.IRON_TRAPDOOR;
    }

    @Override
    public String getDyedName() {
        return this.info.displayName;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return Textures.IRON_TRAPDOOR;
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
        return Colors.rgb(color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return Dyed.rgb(world, x, y, z);
    }
}

package info.kg6jay.moredyes.compat.storagedrawers;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.block.TileColor;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;

/**
 * Storage Drawers' standard drawers of one size, in any color. The color is kept in the tile entity (DyedDrawersTile);
 * the metadata is unused. Everything else (drops with taped contents, upgrades, keys, hoppers) is Storage Drawers'.
 */
public class DyedDrawersBlock extends BlockDrawers {

    public DyedDrawersBlock(StorageDrawersCompat.Size size) {
        // Storage Drawers' name is kept as the config name, which its block config (trim widths) is read by.
        super(size.name, size.drawerCount, size.halfDepth);
        this.setBlockName(Reference.MOD_ID + "." + size.registryName());
        this.setCreativeTab(MoreDyes.tabBlocks);
    }

    @Override
    public TileEntityDrawers createNewTileEntity(World world, int meta) {
        return new DyedDrawersTile();
    }

    @Override
    public int getRenderType() {
        return RenderIds.dyedDrawers;
    }

    /** The color is read from the tile entity, which Storage Drawers keeps until the drops are worked out. */
    @Override
    protected ItemStack getMainDrop(World world, int x, int y, int z, int metadata) {
        return new ItemStack(this, 1, colorAt(world, x, y, z));
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(this, 1, colorAt(world, x, y, z));
    }

    public static int colorAt(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof DyedDrawersTile drawers ? drawers.getColor() : 0;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        TileColor.addSubBlocks(item, list);
    }

    /**
     * Storage Drawers' icons (it also registers the upgrade, indicator and lock icons and reads the trim widths), with
     * the wood replaced by grey textures made from its oak drawers. Every metadata uses them.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        this.iconFront1 = new IIcon[] { TintedTextures.register(register, "drawers/front_1") };
        this.iconFront2 = new IIcon[] { TintedTextures.register(register, "drawers/front_2") };
        this.iconFront4 = new IIcon[] { TintedTextures.register(register, "drawers/front_4") };
        this.iconSide = new IIcon[] { TintedTextures.register(register, "drawers/side") };
        this.iconSideH = new IIcon[] { TintedTextures.register(register, "drawers/side_h") };
        this.iconSideV = new IIcon[] { TintedTextures.register(register, "drawers/side_v") };
        this.iconTrim = new IIcon[] { TintedTextures.register(register, "drawers/trim") };
    }
}

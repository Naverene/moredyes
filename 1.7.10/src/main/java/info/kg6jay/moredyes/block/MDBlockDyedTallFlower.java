package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDColor;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * A two-block-tall dyed flower (lilac, peony, rose bush) in every color. The metadata says which half a block is (TOP
 * or 0 for the bottom), so the color is kept in a tile entity in each half (see TileColor), and the item damage is the
 * color's number. Drawn like the other dyed plants: the petals tinted and the stem in its natural color.
 */
public class MDBlockDyedTallFlower extends Block implements ILayeredBlock, IPlantable {

    public static final int TOP = 8;

    private final String texture;
    @SideOnly(Side.CLIENT)
    private IIcon topPetals, topStem, bottomPetals, bottomStem;

    /** @param info the flower's name (its texture key is "name/topPetals" and so on) and sound */
    public MDBlockDyedTallFlower(BlockInfo info) {
        super(Material.plants);
        this.texture = info.blockName;
        this.setHardness(info.hardness);
        this.setStepSound(info.sound);
        this.setBlockName(Reference.MOD_ID + "." + info.blockName);
        this.setCreativeTab(MoreDyes.tabPlants);
        this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
    }

    public static boolean isTop(int meta) {
        return (meta & TOP) != 0;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return y < world.getHeight() - 1 && super.canPlaceBlockAt(world, x, y, z)
            && world.isAirBlock(x, y + 1, z)
            && this.canSustain(world, x, y, z);
    }

    private boolean canSustain(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        return MDBlockFlower.isSand(below)
            || below.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    /** Places the top half too, both in the color of the item. */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        world.setBlock(x, y + 1, z, this, TOP, 2);
        TileColor.set(world, x, y, z, stack.getItemDamage());
        TileColor.set(world, x, y + 1, z, stack.getItemDamage());
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (isTop(world.getBlockMetadata(x, y, z))) {
            return world.getBlock(x, y - 1, z) == this && !isTop(world.getBlockMetadata(x, y - 1, z));
        }
        return world.getBlock(x, y + 1, z) == this && isTop(world.getBlockMetadata(x, y + 1, z))
            && this.canSustain(world, x, y, z);
    }

    /**
     * When one half goes, so does the other. Each half has the color, so whichever half is broken drops the flower and
     * the other half goes without dropping anything; a bottom half that loses its ground drops it.
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (this.canBlockStay(world, x, y, z)) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        boolean otherHalfThere = isTop(meta) ? world.getBlock(x, y - 1, z) == this
            : world.getBlock(x, y + 1, z) == this;
        if (otherHalfThere) {
            this.dropBlockAsItem(world, x, y, z, meta, 0);
        }
        world.setBlockToAir(x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
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
    public int getRenderType() {
        return RenderIds.layeredPlant;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityMDColor();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return TileColor.drops(this, world, x, y, z, 1);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(world, player, x, y, z, false);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return TileColor.stack(this, world, x, y, z, 1);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        TileColor.addSubBlocks(item, list);
    }

    @Override
    public int getLayerCount() {
        return 2;
    }

    /** The meta is the block's metadata; the item uses the top half's icons. */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getLayerIcon(int side, int meta, int layer) {
        if (isTop(meta)) {
            return layer == 0 ? this.topPetals : this.topStem;
        }
        return layer == 0 ? this.bottomPetals : this.bottomStem;
    }

    /** Here the meta is the color's number, as on the item. */
    @Override
    public int getLayerColor(int meta, int layer) {
        return layer == 0 ? ColorIndex.rgb(meta) : ColorUtil.WHITE;
    }

    @Override
    public boolean isPlant() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.getLayerIcon(side, meta, Math.max(RenderLayer.current, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return ColorIndex.rgb(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return Math.max(RenderLayer.current, 0) == 0 ? TileColor.rgb(world, x, y, z) : ColorUtil.WHITE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.topPetals = TintedTextures.register(register, this.texture + "/topPetals");
        this.topStem = TintedTextures.register(register, this.texture + "/topStem");
        this.bottomPetals = TintedTextures.register(register, this.texture + "/bottomPetals");
        this.bottomStem = TintedTextures.register(register, this.texture + "/bottomStem");
    }
}

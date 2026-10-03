package info.kg6jay.moredyes.compat.ironchest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChest;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.block.TileColor;
import info.kg6jay.moredyes.reference.Reference;

/**
 * A dyed Iron Chests chest of one tier, in any color. The color is kept in the tile entity (DyedIronChestTile); the
 * metadata is unused. Otherwise it behaves like Iron Chests' own chest block.
 */
public class DyedIronChestBlock extends BlockContainer {

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlock(IronChestCompat.Tier tier) {
        // The same as Iron Chests' own chest block.
        super(Material.iron);
        this.tier = tier;
        this.setBlockName(Reference.MOD_ID + ".dyed" + tier.title() + "Chest");
        this.setHardness(3.0F);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        this.setCreativeTab(MoreDyes.tabBlocks);
    }

    public IronChestCompat.Tier getTier() {
        return this.tier;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return this.tier.newTile();
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
        return RenderIds.dyedIronChest;
    }

    /** Opens Iron Chests' own screen for this tier, the same as its chests do. */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof DyedIronChestTile) || world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)
            || world.isRemote) {
            return true;
        }
        player.openGui(IronChest.instance, this.tier.type.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int facing = switch (MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
            case 0 -> 2;
            case 1 -> 5;
            case 2 -> 3;
            default -> 4;
        };
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof DyedIronChestTile chest) {
            chest.setFacing(facing);
            chest.setColor(stack.getItemDamage());
            world.markBlockForUpdate(x, y, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof DyedIronChestTile chest) {
            IronChest.ironChestBlock.dropContent(0, chest, world, x, y, z);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    /** The color comes from the tile entity, which is still there here (see removedByPlayer). */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(this, 1, colorAt(world, x, y, z)));
        return drops;
    }

    /** Keeps the block (and its tile entity) until harvestBlock has worked out the drop. */
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
        return new ItemStack(this, 1, colorAt(world, x, y, z));
    }

    private static int colorAt(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof DyedIronChestTile chest ? chest.getColor() : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        TileColor.addSubBlocks(item, list);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof IInventory inventory ? Container.calcRedstoneFromInventory(inventory) : 0;
    }

    /** Nothing to register: the breaking particles use Iron Chests' icon for this tier. */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return IronChest.ironChestBlock.getIcon(side, this.tier.type.ordinal());
    }
}

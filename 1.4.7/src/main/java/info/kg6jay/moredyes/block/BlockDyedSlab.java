package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * A dyed slab in every color, with the color in the tile entity. Unlike vanilla, the single and double slab are the
 * same block: the metadata is BOTTOM, TOP or DOUBLE. ItemBlockDyedSlab joins two slabs of the same color into a
 * double slab.
 */
public class BlockDyedSlab extends Block implements IDyedBlock {

    public static final int BOTTOM = 0, TOP = 1, DOUBLE = 2;

    private final String displayName;
    private final Faces faces;

    public BlockDyedSlab(int id, BlockDyed model, String name, CreativeTabs tab) {
        super(id, model.faces.side(), model.blockMaterial);
        this.displayName = model.getDyedName() + " Slab";
        this.faces = model.faces;
        this.setHardness(model.getBlockHardness(null, 0, 0, 0));
        this.setResistance(model.getExplosionResistance(null) * 5.0F / 3.0F);
        this.setStepSound(model.stepSound);
        this.setLightOpacity(255);
        Block.useNeighborBrightness[id] = true;
        this.setBlockName("moredyes." + name);
        this.setCreativeTab(tab);
        this.setTextureFile(Textures.SHEET);
        this.setBlockBoundsForItemRender();
        if (model.info.tool != null) {
            MinecraftForge.setBlockHarvestLevel(this, model.info.tool, model.info.harvestLevel);
        }
    }

    @Override
    public String getDyedName() {
        return this.displayName;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return this.faces.get(side);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        switch (world.getBlockMetadata(x, y, z)) {
            case TOP:
                this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case DOUBLE:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            default:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                break;
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }

    @Override
    public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB mask, List list,
        Entity collider) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollidingBlockToList(world, x, y, z, mask, list, collider);
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
    public boolean isBlockNormalCube(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == DOUBLE;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        switch (world.getBlockMetadata(x, y, z)) {
            case DOUBLE:
                return true;
            case TOP:
                return side == ForgeDirection.UP;
            default:
                return side == ForgeDirection.DOWN;
        }
    }

    /** Placed on the upper half of a block's side, or on the underside of a block, it becomes a top slab. */
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ,
        int meta) {
        return side != 0 && (side == 1 || hitY <= 0.5F) ? BOTTOM : TOP;
    }

    /** Hides the faces between slabs of this block that touch each other. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlockId(x, y, z) == this.blockID) {
            int other = world.getBlockMetadata(x, y, z);
            int self = world.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side],
                z - Facing.offsetsZForSide[side]);
            boolean hidden;
            if (side == 0) {
                hidden = self != TOP && other != BOTTOM;
            } else if (side == 1) {
                hidden = self != BOTTOM && other != TOP;
            } else {
                hidden = other == DOUBLE || other == self;
            }
            if (hidden) {
                return false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
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
        return Dyed.drops(this, world, x, y, z, meta == DOUBLE ? 2 : 1);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(this, world, x, y, z, 1);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        return Dyed.canCreatureSpawn(type, world, x, y, z) && world.getBlockMetadata(x, y, z) != BOTTOM
            && super.canCreatureSpawn(type, world, x, y, z);
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

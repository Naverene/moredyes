package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDColor;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.reference.Reference;

/**
 * A dyed bed in every color: a vanilla bed (same halves, metadata and sleeping) with its color in a TileEntityMDColor
 * on both halves, placed by MDItem.bed. DyedBedRenderer draws it twice: the sheet tinted (layer 0), then the pillow and
 * wooden frame over it as they are (layer 1).
 */
public class MDBlockDyedBed extends BlockBed {

    private static final String[] PARTS = { "top", "end", "side" };

    /** By layer (sheet, frame), half (feet, head) and part (top, end, side). */
    @SideOnly(Side.CLIENT)
    private IIcon[][][] icons;

    public MDBlockDyedBed() {
        this.setHardness(0.2F);
        this.disableStats();
        this.setBlockName(Reference.MOD_ID + ".bed");
    }

    @Override
    public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
        return true;
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
    public int getRenderType() {
        return RenderIds.bed;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return RenderLayer.current == 1 ? 0xFFFFFF : TileColor.rgb(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return 0xFFFFFF;
    }

    /**
     * The icon of this side for the layer being drawn, or null where the layer has nothing (the sheet has no bottom).
     * Outside the renderer (breaking particles, which are tinted) it is the sheet, also on the bottom.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int layer = RenderLayer.current;
        if (side == 0) {
            if (layer == 1) {
                return Blocks.planks.getBlockTextureFromSide(0);
            }
            if (layer == 0) {
                return null;
            }
            side = 1;
        }
        int direction = Direction.bedDirection[getDirection(meta)][side];
        int head = isBlockHeadOfBed(meta) ? 1 : 0;
        int part;
        if (head == 1 && direction == 2 || head == 0 && direction == 3) {
            part = 1;
        } else if (direction == 4 || direction == 5) {
            part = 2;
        } else {
            part = 0;
        }
        return this.icons[layer == 1 ? 1 : 0][head][part];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icons = new IIcon[2][2][PARTS.length];
        for (int half = 0; half < 2; half++) {
            for (int part = 0; part < PARTS.length; part++) {
                String key = "bed/" + (half == 1 ? "head_" : "feet_") + PARTS[part];
                this.icons[0][half][part] = TintedTextures.register(register, key);
                this.icons[1][half][part] = TintedTextures.register(register, key + "_frame");
            }
        }
    }

    /** Only the foot drops the bed, as in vanilla (see dropBlockAsItemWithChance). */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (!isBlockHeadOfBed(meta)) {
            drops.add(new ItemStack(MDItem.bed, 1, TileColor.get(world, x, y, z)));
        }
        return drops;
    }

    /**
     * Like vanilla, a foot whose head is gone removes itself and drops the bed, but the color is read before the block
     * and its tile entity are removed.
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        int meta = world.getBlockMetadata(x, y, z);
        int direction = getDirection(meta);
        if (isBlockHeadOfBed(meta)) {
            if (world.getBlock(x - field_149981_a[direction][0], y, z - field_149981_a[direction][1]) != this) {
                world.setBlockToAir(x, y, z);
            }
        } else if (world.getBlock(x + field_149981_a[direction][0], y, z + field_149981_a[direction][1]) != this) {
            int color = TileColor.get(world, x, y, z);
            world.setBlockToAir(x, y, z);
            if (!world.isRemote && world.getGameRules()
                .getGameRuleBooleanValue("doTileDrops")) {
                EntityItem item = new EntityItem(
                    world,
                    x + 0.5D,
                    y + 0.5D,
                    z + 0.5D,
                    new ItemStack(MDItem.bed, 1, color));
                item.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(item);
            }
        }
    }

    /** Keeps the block, and with it the color, until harvestBlock has worked out the drops. */
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
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return MDItem.bed;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(MDItem.bed, 1, TileColor.get(world, x, y, z));
    }
}

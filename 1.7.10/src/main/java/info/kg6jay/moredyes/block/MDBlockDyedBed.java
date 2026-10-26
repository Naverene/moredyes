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
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBed;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed bed in every color: a vanilla bed (same halves, metadata and sleeping) with its color, wood and bedding in a
 * TileEntityMDBed on both halves, placed by MDItem.bed. DyedBedRenderer draws it in three layers: the sheet tinted (or
 * vanilla's red one), the white pillow, and the wooden frame in the planks the bed was made from (see BedWood), tinted
 * for dyed planks.
 */
public class MDBlockDyedBed extends BlockBed {

    private static final String[] PARTS = { "top", "end", "side" };

    /** The layers DyedBedRenderer draws. */
    public static final int SHEET = 0, PILLOW = 1, FRAME = 2, LAYERS = 3;

    /** By half (feet, head) and part (top, end, side). */
    @SideOnly(Side.CLIENT)
    private IIcon[][] sheet, vanillaSheet, pillow;
    /** By wood (BedWood.WOODS, then dyed planks), half and part. */
    @SideOnly(Side.CLIENT)
    private IIcon[][][] frames;

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
        return new TileEntityMDBed();
    }

    /** The bed's half at this position, or null for a bed placed before beds kept their wood. */
    private static TileEntityMDBed bed(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityMDBed bed ? bed : null;
    }

    /** The wood the frame is drawn in: the bed's planks, or null for oak. */
    private static String frameWood(TileEntityMDBed bed) {
        return bed == null ? null : bed.getWood();
    }

    /** The bed item the bed at this position drops. */
    private static ItemStack stack(IBlockAccess world, int x, int y, int z) {
        TileEntityMDBed bed = bed(world, x, y, z);
        return bed != null ? bed.getItemStack() : new ItemStack(MDItem.bed, 1, TileColor.get(world, x, y, z));
    }

    @Override
    public int getRenderType() {
        return RenderIds.bed;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        TileEntityMDBed bed = bed(world, x, y, z);
        switch (RenderLayer.current) {
            case PILLOW:
                return 0xFFFFFF;
            case FRAME:
                int dyed = BedWood.dyed(frameWood(bed));
                return dyed < 0 ? 0xFFFFFF : ColorIndex.rgb(dyed);
            default:
                return bed != null && bed.isVanilla() ? 0xFFFFFF : TileColor.rgb(world, x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return 0xFFFFFF;
    }

    /**
     * The icon of this side for the layer being drawn, or null where the layer has nothing (the sheet and pillow have
     * no
     * bottom). Outside the renderer (breaking particles, which are tinted) it is the sheet, also on the bottom.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icon(side, meta, null);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return this.icon(side, world.getBlockMetadata(x, y, z), bed(world, x, y, z));
    }

    @SideOnly(Side.CLIENT)
    private IIcon icon(int side, int meta, TileEntityMDBed bed) {
        int layer = RenderLayer.current;
        String wood = frameWood(bed);
        int vanillaWood = BedWood.vanilla(wood);
        if (side == 0) {
            if (layer == FRAME) {
                int dyed = BedWood.dyed(wood);
                if (dyed >= 0 && MDBlock.plank[ColorIndex.set(dyed)] != null) {
                    return MDBlock.plank[ColorIndex.set(dyed)].getIcon(0, ColorIndex.shade(dyed));
                }
                return Blocks.planks.getIcon(0, Math.max(0, vanillaWood));
            }
            if (layer >= 0) {
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
        switch (layer) {
            case PILLOW:
                return this.pillow[head][part];
            case FRAME:
                return this.frames[vanillaWood >= 0 ? vanillaWood
                    : BedWood.dyed(wood) >= 0 ? BedWood.WOODS.length : 0][head][part];
            default:
                return (bed != null && bed.isVanilla() ? this.vanillaSheet : this.sheet)[head][part];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.sheet = new IIcon[2][PARTS.length];
        this.vanillaSheet = new IIcon[2][PARTS.length];
        this.pillow = new IIcon[2][PARTS.length];
        this.frames = new IIcon[BedWood.WOODS.length + 1][2][PARTS.length];
        for (int half = 0; half < 2; half++) {
            for (int part = 0; part < PARTS.length; part++) {
                String key = "bed/" + (half == 1 ? "head_" : "feet_") + PARTS[part];
                this.sheet[half][part] = TintedTextures.register(register, key);
                this.vanillaSheet[half][part] = TintedTextures.register(register, key + "_vanilla");
                this.pillow[half][part] = TintedTextures.register(register, key + "_pillow");
                for (int wood = 0; wood <= BedWood.WOODS.length; wood++) {
                    this.frames[wood][half][part] = TintedTextures.register(
                        register,
                        key + "_frame_" + (wood < BedWood.WOODS.length ? BedWood.WOODS[wood] : "dyed"));
                }
            }
        }
    }

    /** Only the foot drops the bed, as in vanilla (see dropBlockAsItemWithChance). */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (!isBlockHeadOfBed(meta)) {
            drops.add(stack(world, x, y, z));
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
            ItemStack dropped = stack(world, x, y, z);
            world.setBlockToAir(x, y, z);
            if (!world.isRemote && world.getGameRules()
                .getGameRuleBooleanValue("doTileDrops")) {
                EntityItem item = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, dropped);
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
        return stack(world, x, y, z);
    }
}

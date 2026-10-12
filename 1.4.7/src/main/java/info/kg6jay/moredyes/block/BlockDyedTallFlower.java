package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * A dyed two-block-tall flower (lilac, peony, rose bush), which Minecraft 1.4.7 does not have: a bottom half and a top
 * half of the same block, the top marked by bit 8 of the metadata, both holding the color. Each half needs the other,
 * so breaking one breaks both, and only the bottom drops the flower.
 */
public class BlockDyedTallFlower extends BlockDyedPlant {

    public static final int TOP = 8;

    /** Tinted and natural textures of the top half, then of the bottom half. */
    private final int topPetals, topStem, bottomPetals, bottomStem;

    public BlockDyedTallFlower(int id, BlockInfo info, int topPetals, int topStem, int bottomPetals, int bottomStem) {
        super(id, info, topPetals, topStem);
        this.topPetals = topPetals;
        this.topStem = topStem;
        this.bottomPetals = bottomPetals;
        this.bottomStem = bottomStem;
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
    }

    /** The item shows the top half, as in the newer versions. */
    @Override
    public int getLayerTexture(int side, int layer) {
        return layer == 0 ? this.topPetals : this.topStem;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        boolean top = (meta & TOP) != 0;
        if (RenderLayer.layer() == 0) {
            return top ? this.topPetals : this.bottomPetals;
        }
        return top ? this.topStem : this.bottomStem;
    }

    /** There must be room for the top half. */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return y < 255 && super.canPlaceBlockAt(world, x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    /** The item places the bottom half, which then adds the top half in the same color. */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
        if ((world.getBlockMetadata(x, y, z) & TOP) == 0 && world.isAirBlock(x, y + 1, z)) {
            Dyed.place(world, x, y + 1, z, this, TOP, Dyed.get(world, x, y, z), true);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if ((world.getBlockMetadata(x, y, z) & TOP) != 0) {
            return world.getBlockId(x, y - 1, z) == this.blockID
                && (world.getBlockMetadata(x, y - 1, z) & TOP) == 0;
        }
        Block soil = Block.blocksList[world.getBlockId(x, y - 1, z)];
        return world.getBlockId(x, y + 1, z) == this.blockID
            && (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z))
            && soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        if ((meta & TOP) != 0) {
            return new ArrayList<ItemStack>();
        }
        return super.getBlockDropped(world, x, y, z, meta, fortune);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(this, world, x, y, z, 1);
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}

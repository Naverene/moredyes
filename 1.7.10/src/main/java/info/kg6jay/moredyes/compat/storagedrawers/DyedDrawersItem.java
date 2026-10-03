package info.kg6jay.moredyes.compat.storagedrawers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;

import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed drawer. The item damage is the color's number (see ColorIndex), which goes into the tile entity when it is
 * placed; the block is placed with metadata 0. Storage Drawers' item does the rest (capacity, taped contents).
 */
public class DyedDrawersItem extends ItemDrawers {

    public DyedDrawersItem(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
            return false;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof DyedDrawersTile drawers) {
            drawers.setColor(stack.getItemDamage());
            world.markBlockForUpdate(x, y, z);
        }
        return true;
    }

    /** "ECBF99 Drawers 1x2", like the names of the other dyed blocks. */
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector
            .translateToLocalFormatted(this.getUnlocalizedName() + ".name", ColorIndex.hex(stack.getItemDamage()))
            .trim();
    }
}

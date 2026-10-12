package info.kg6jay.moredyes.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.Dyed;
import info.kg6jay.moredyes.block.MDBlocks;

/**
 * The dyed sign item, which is the item of the standing dyed sign. Places a standing sign on top of a block or a wall
 * sign on its side, like the vanilla sign, and opens the sign editor.
 */
public class ItemBlockDyedSign extends ItemBlockDyed {

    public ItemBlockDyedSign(int id) {
        super(id);
        this.setMaxStackSize(16);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (side == 0 || !world.getBlockMaterial(x, y, z).isSolid()) {
            return false;
        }
        switch (side) {
            case 1:
                ++y;
                break;
            case 2:
                --z;
                break;
            case 3:
                ++z;
                break;
            case 4:
                --x;
                break;
            default:
                ++x;
                break;
        }
        if (stack.stackSize == 0 || !player.canPlayerEdit(x, y, z, side, stack)
            || !MDBlocks.sign.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }
        if (side == 1) {
            int rotation = MathHelper.floor_double((player.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
            world.setBlockAndMetadataWithNotify(x, y, z, MDBlocks.sign.blockID, rotation);
        } else {
            world.setBlockAndMetadataWithNotify(x, y, z, MDBlocks.wallSign.blockID, side);
        }
        Dyed.set(world, x, y, z, Colors.clamp(stack.getItemDamage()));
        --stack.stackSize;
        TileEntity sign = world.getBlockTileEntity(x, y, z);
        if (sign instanceof TileEntitySign) {
            player.displayGUIEditSign(sign);
        }
        return true;
    }

    /** Checked on the client before the click is sent; the sign goes on any solid side but the bottom. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player,
        ItemStack stack) {
        return side != 0 && world.getBlockMaterial(x, y, z).isSolid();
    }

    @Override
    public String getTextureFile() {
        return Textures.SHEET;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int damage) {
        return Textures.SIGN_ITEM;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return Colors.rgb(Colors.clamp(stack.getItemDamage()));
    }
}

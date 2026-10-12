package info.kg6jay.moredyes.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDSign;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed sign in every color; the damage is the color's number (see ColorIndex). Placed like the vanilla sign: on top
 * of a block it stands, on the side of one it hangs on the wall, and the text editor opens.
 */
public class MDItemDyedSign extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public MDItemDyedSign() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
        this.setUnlocalizedName(Reference.MOD_ID + ".sign");
        this.setCreativeTab(MoreDyes.tabTrees);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (side == 0 || !world.getBlock(x, y, z)
            .getMaterial()
            .isSolid()) {
            return false;
        }
        switch (side) {
            case 1 -> ++y;
            case 2 -> --z;
            case 3 -> ++z;
            case 4 -> --x;
            default -> ++x;
        }
        if (!player.canPlayerEdit(x, y, z, side, stack) || !MDBlock.standingSign.canPlaceBlockAt(world, x, y, z)) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        if (side == 1) {
            int rotation = MathHelper.floor_double((player.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
            world.setBlock(x, y, z, MDBlock.standingSign, rotation, 3);
        } else {
            world.setBlock(x, y, z, MDBlock.wallSign, side, 3);
        }
        --stack.stackSize;
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityMDSign sign) {
            sign.setColor(stack.getItemDamage());
            player.func_146100_a(sign);
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < ColorIndex.count(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return ColorIndex.rgb(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icon = TintedTextures.register(register, "sign");
    }
}

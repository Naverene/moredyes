package info.kg6jay.moredyes.compat.etfuturum;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.blocks.BlockShulkerBox;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.TileColor;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed shulker box in any More Dyes color and any of Et Futurum Requiem's tiers. It is Et Futurum's shulker box
 * block with its own tile entity (DyedShulkerBoxTile), which keeps the color: opening, the contents, the tier upgrades,
 * dropping with the contents inside and the screen are all Et Futurum's.
 */
public class DyedShulkerBoxBlock extends BlockShulkerBox {

    public DyedShulkerBoxBlock() {
        this.setBlockName(Reference.MOD_ID + ".dyedShulkerBox");
        this.setCreativeTab(MoreDyes.tabBlocks);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new DyedShulkerBoxTile();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        if (world.getTileEntity(x, y, z) instanceof DyedShulkerBoxTile box) {
            box.setDye(stack.getItemDamage());
            box.markDirty();
            world.markBlockForUpdate(x, y, z);
        }
    }

    /** The breaking particles: one grey icon for all of Et Futurum's colors, tinted by colorMultiplier. */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = TintedTextures.register(register, "shulker/block");
        Arrays.fill(this.colorIcons, this.blockIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) instanceof DyedShulkerBoxTile box ? ColorIndex.rgb(box.getDye()) : 0xFFFFFF;
    }

    /** One plain box per color. The tiers are made with Et Futurum's upgrades, as its own boxes are. */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        TileColor.addSubBlocks(item, list);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        if (!(world.getTileEntity(x, y, z) instanceof DyedShulkerBoxTile box)) {
            return new ItemStack(this);
        }
        ItemStack stack = new ItemStack(this, 1, box.getDye());
        if (box.type.ordinal() > 0) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("Type", (byte) box.type.ordinal());
            stack.setTagCompound(tag);
        }
        return stack;
    }
}

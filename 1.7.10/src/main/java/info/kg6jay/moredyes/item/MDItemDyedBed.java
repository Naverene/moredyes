package info.kg6jay.moredyes.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.TileColor;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed bed in every color; the damage is the color's number (see ColorIndex). Placed like the vanilla bed, with the
 * color given to both halves. The icon is the vanilla one: its sheet tinted, then the pillow and frame over it.
 */
public class MDItemDyedBed extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon sheet, frame;

    public MDItemDyedBed() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(Reference.MOD_ID + ".bed");
        this.setCreativeTab(MoreDyes.tabTrees);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
    }

    /** The vanilla ItemBed's placement, with the dyed bed block. */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if (side != 1) {
            return false;
        }
        ++y;
        int direction = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int dx = direction == 1 ? -1 : direction == 3 ? 1 : 0;
        int dz = direction == 0 ? 1 : direction == 2 ? -1 : 0;
        if (!player.canPlayerEdit(x, y, z, side, stack) || !player.canPlayerEdit(x + dx, y, z + dz, side, stack)) {
            return false;
        }
        if (!world.isAirBlock(x, y, z) || !world.isAirBlock(x + dx, y, z + dz)
            || !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
            || !World.doesBlockHaveSolidTopSurface(world, x + dx, y - 1, z + dz)) {
            return false;
        }
        int color = stack.getItemDamage();
        world.setBlock(x, y, z, MDBlock.bed, direction, 3);
        TileColor.set(world, x, y, z, color);
        if (world.getBlock(x, y, z) == MDBlock.bed) {
            world.setBlock(x + dx, y, z + dz, MDBlock.bed, direction + 8, 3);
            TileColor.set(world, x + dx, y, z + dz, color);
        }
        --stack.stackSize;
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
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return pass == 0 ? ColorIndex.rgb(stack.getItemDamage()) : 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return pass == 0 ? this.sheet : this.frame;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.sheet;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.sheet = TintedTextures.register(register, "bed/item");
        this.frame = TintedTextures.register(register, "bed/item_frame");
    }
}

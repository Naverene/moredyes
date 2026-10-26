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
import info.kg6jay.moredyes.block.BedWood;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBed;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed bed in every color; the damage is the color's number (see ColorIndex), and its tags keep the planks it was
 * made from and whether it has the vanilla bedding (see BedWood). Placed like the vanilla bed, with all that given to
 * both halves. The icon is the vanilla one: its sheet tinted, then the pillow and the frame in its wood over it.
 */
public class MDItemDyedBed extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon sheet, vanillaSheet, pillow;
    /** By wood: BedWood.WOODS, then dyed planks. */
    @SideOnly(Side.CLIENT)
    private IIcon[] frames;

    public MDItemDyedBed() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(Reference.MOD_ID + ".bed");
        this.setCreativeTab(MoreDyes.tabTrees);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        // A bed with the vanilla bedding has vanilla's name.
        return BedWood.isVanilla(stack) ? "item.bed" : super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
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
        world.setBlock(x, y, z, MDBlock.bed, direction, 3);
        setValues(world, x, y, z, stack);
        if (world.getBlock(x, y, z) == MDBlock.bed) {
            world.setBlock(x + dx, y, z + dz, MDBlock.bed, direction + 8, 3);
            setValues(world, x + dx, y, z + dz, stack);
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
        return 3;
    }

    /** The wood the frame is drawn in, or null for oak. */
    private static String frameWood(ItemStack stack) {
        return BedWood.woodOf(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (pass == 0) {
            return BedWood.isVanilla(stack) ? 0xFFFFFF : ColorIndex.rgb(stack.getItemDamage());
        }
        int dyed = pass == 2 ? BedWood.dyed(frameWood(stack)) : -1;
        return dyed < 0 ? 0xFFFFFF : ColorIndex.rgb(dyed);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return BedWood.isVanilla(stack) ? this.vanillaSheet : this.sheet;
        }
        if (pass == 1) {
            return this.pillow;
        }
        String wood = frameWood(stack);
        int vanilla = BedWood.vanilla(wood);
        return this.frames[vanilla >= 0 ? vanilla : BedWood.dyed(wood) >= 0 ? BedWood.WOODS.length : 0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return pass == 0 ? this.sheet : pass == 1 ? this.pillow : this.frames[0];
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
        this.vanillaSheet = TintedTextures.register(register, "bed/item_vanilla");
        this.pillow = TintedTextures.register(register, "bed/item_pillow");
        this.frames = new IIcon[BedWood.WOODS.length + 1];
        for (int wood = 0; wood <= BedWood.WOODS.length; wood++) {
            this.frames[wood] = TintedTextures
                .register(register, "bed/item_frame_" + (wood < BedWood.WOODS.length ? BedWood.WOODS[wood] : "dyed"));
        }
    }

    /** Gives a half of the bed the item's color, wood and bedding, and sends them to the players who can see it. */
    private static void setValues(World world, int x, int y, int z, ItemStack stack) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityMDBed bed) {
            bed.setItemValues(stack);
            world.markBlockForUpdate(x, y, z);
        }
    }
}

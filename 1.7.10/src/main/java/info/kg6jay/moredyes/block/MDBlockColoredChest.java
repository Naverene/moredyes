package info.kg6jay.moredyes.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.handler.GuiHandler;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class MDBlockColoredChest extends BlockChest implements IBlockColored {

    private final String[] colors;
    private final String color;

    @SideOnly(Side.CLIENT)
    private IIcon[] colorIcons;

    public MDBlockColoredChest(String name, BlockInfo info, String[] colors) {
        super(info.woodType);
        this.colors = colors;
        this.color = name;
        this.setBlockName(name);
        this.setCreativeTab(info.tab);
        this.setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        // Map the color name to the actual hex texture filename available in assets
        String hex = getHexForColorName(name);
        // Register texture name without an extra 'blocks/' segment. The resource loader will map
        // 'moredyes:chest/normal/<hex>' -> 'assets/moredyes/textures/blocks/chest/normal/<hex>.png'
        this.setBlockTextureName(Reference.MOD_ID + ":chest/normal/" + hex);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        // Register a per-color icon array based on the configured colors for this block
        if (this.colors != null && this.colors.length > 0) {
            this.colorIcons = new IIcon[this.colors.length];
            for (int i = 0; i < this.colors.length; i++) {
                String hex = getHexForColorName(this.colors[i]);
                // Register icon name without the extra 'blocks/' segment to avoid doubling when
                // the texture system prefixes 'textures/blocks/'.
                String path = Reference.MOD_ID + ":chest/normal/" + hex;
                this.colorIcons[i] = iconRegister.registerIcon(path);
            }
            // Keep a fallback blockIcon (use first color or the named texture)
            this.blockIcon = this.colorIcons[0] != null ? this.colorIcons[0] : iconRegister.registerIcon(this.getTextureName());
        } else {
            this.blockIcon = iconRegister.registerIcon(this.getTextureName());
        }
    }

    @Override
    public int getRenderType() {
        // Return the vanilla chest render type to avoid mismatches with renderers
        return Blocks.chest.getRenderType();
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
    public boolean hasComparatorInputOverride() {
        return true;
    }

    // Add this override to use different textures per color
    @Override
    public IIcon getIcon(int side, int meta) {
        // Defensive bounds check for meta (color index)
        if (meta < 0) meta = 0;
        if (this.colorIcons != null && meta < this.colorIcons.length) {
            IIcon ico = this.colorIcons[meta];
            if (ico != null) return ico;
        }
        // Fallback
        return this.blockIcon;
    }

    // Add this override for correct unlocalized name per color
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta < 0 || meta >= colors.length) meta = 0;
        return super.getUnlocalizedName() + "." + colors[meta];
    }

    @Override
    public int damageDropped(int meta) {
        // Ensure the dropped item keeps its color (metadata)
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < colors.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "." + colors[meta];
    }

    @Override
    public String getColorSet() {
        return color;
    }

    @Override
    public int getMaxMeta() {
        return colors.length - 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMDBlockColoredChest();
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2,
        float f3) {
        TileEntity te = world.getTileEntity(i, j, k);

        if (te == null || !(te instanceof TileEntityMDBlockColoredChest)) {
            return true;
        }

        if (world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN)) {
            return true;
        }

        if (world.isRemote) {
            return true;
        }

        player.openGui(MoreDyes.instance, GuiHandler.COLORED_CHEST_GUI_ID, world, i, j, k);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemStack) {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0) {
            chestFacing = 2;
        }
        if (facing == 1) {
            chestFacing = 5;
        }
        if (facing == 2) {
            chestFacing = 3;
        }
        if (facing == 3) {
            chestFacing = 4;
        }
        TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && te instanceof TileEntityMDBlockColoredChest teic)
        {
            teic.wasPlaced(entityliving, itemStack);
            teic.setFacing(chestFacing);
            world.markBlockForUpdate(i, j, k);
        }
    }

    private String getHexForColorName(String name) {
        if (name == null) return "ecbf99";
        for (int i = 0; i < MDBlock.colors.length; i++) {
            if (MDBlock.colors[i].equalsIgnoreCase(name)) {
                if (i < MDBlock.colorStrings.length && MDBlock.colorStrings[i] != null
                    && MDBlock.colorStrings[i].length > 0) {
                    return MDBlock.colorStrings[i][0];
                }
            }
        }
        return "ecbf99";
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (worldIn.getBlock(x, y, z - 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x, y, z + 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        }
        else if (worldIn.getBlock(x - 1, y, z) == this)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x + 1, y, z) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        }
        else
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

}

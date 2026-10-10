package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockPane;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * Dyed glass pane, clear or foggy. Like vanilla glass panes it drops nothing unless broken with silk touch. It joins
 * panes of its own kind (any color), vanilla glass and full blocks; Minecraft 1.4.7 decides that in a method mods
 * cannot change.
 */
public class BlockDyedPane extends BlockPane implements IDyedBlock {

    private final BlockInfo info;
    private final String variant;
    private final int texture;

    public BlockDyedPane(int id, BlockInfo info, int texture, int edge, String variant) {
        super(id, texture, edge, info.material, false);
        this.info = info;
        this.variant = variant;
        this.texture = texture;
        info.apply(this);
        this.setTextureFile(Textures.SHEET);
    }

    @Override
    public String getDyedName() {
        return this.variant + " " + this.info.displayName;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return this.texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityDyed();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        Dyed.remember(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            BlockDyedStone.Silk.harvest(this, world, player, x, y, z);
        } else {
            super.harvestBlock(world, player, x, y, z, meta);
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(this, world, x, y, z, 1);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        return Dyed.canCreatureSpawn(type, world, x, y, z) && super.canCreatureSpawn(type, world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        Dyed.addSubBlocks(id, list);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int color) {
        return Colors.rgb(color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return Dyed.rgb(world, x, y, z);
    }
}

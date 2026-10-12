package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDColor;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed wooden fence in every color, with the color kept in a tile entity (see TileColor). Like the fences of
 * Minecraft 1.8, dyed fences of every wood connect to each other. They do not connect to the vanilla fence, which would
 * not connect back.
 */
public class MDBlockDyedFence extends BlockFence {

    private final TileColor.Icons icons;

    /**
     * @param model   the dyed planks the fence is made of, which it takes its hardness and sound from
     * @param name    registry name, such as "oakFence"
     * @param texture the tinted texture key of the planks
     */
    public MDBlockDyedFence(Block model, String name, String texture) {
        super(texture, model.getMaterial());
        this.icons = new TileColor.Icons(texture);
        this.setHardness(model.getBlockHardness(null, 0, 0, 0));
        this.setResistance(model.getExplosionResistance(null) * 5.0F / 3.0F);
        this.setStepSound(model.stepSound);
        this.setHarvestLevel(model.getHarvestTool(0), model.getHarvestLevel(0));
        this.setBlockName(Reference.MOD_ID + "." + name);
        this.setCreativeTab(MoreDyes.tabTrees);
    }

    @Override
    public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof MDBlockDyedFence || block == Blocks.fence_gate) {
            return true;
        }
        return block.getMaterial()
            .isOpaque() && block.renderAsNormalBlock()
            && block.getMaterial() != Material.gourd;
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityMDColor();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return TileColor.drops(this, world, x, y, z, 1);
    }

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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return TileColor.stack(this, world, x, y, z, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        TileColor.addSubBlocks(item, list);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return ColorIndex.rgb(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return TileColor.rgb(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.icons.register(register);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons.get(side);
    }
}

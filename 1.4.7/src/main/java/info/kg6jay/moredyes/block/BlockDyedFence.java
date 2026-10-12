package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * A dyed wooden fence in every color, with the color in the tile entity and the texture of the planks it is made of.
 * Dyed fences of every wood join each other and the vanilla wooden fence, but not the nether brick fence.
 */
public class BlockDyedFence extends BlockFence implements IDyedBlock {

    private final String displayName;

    public BlockDyedFence(int id, BlockDyed model, String name, CreativeTabs tab) {
        super(id, model.faces.side(), model.blockMaterial);
        this.displayName = model.getDyedName() + " Fence";
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setStepSound(model.stepSound);
        this.setBlockName("moredyes." + name);
        this.setCreativeTab(tab);
        this.setTextureFile(Textures.SHEET);
        if (model.info.tool != null) {
            MinecraftForge.setBlockHarvestLevel(this, model.info.tool, model.info.harvestLevel);
        }
    }

    @Override
    public String getDyedName() {
        return this.displayName;
    }

    @Override
    public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];
        if (block instanceof BlockFence && block.blockMaterial == Material.wood || id == Block.fenceGate.blockID) {
            return true;
        }
        return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock()
            && block.blockMaterial != Material.pumpkin;
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
        return Dyed.drops(this, world, x, y, z, 1);
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

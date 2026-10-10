package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStairs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
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
 * Dyed stairs in every color. The metadata holds the direction, as for vanilla stairs, and the color is in the tile
 * entity. Hardness, sound and textures come from the dyed block they are made of.
 */
public class BlockDyedStairs extends BlockStairs implements IDyedBlock {

    private final String displayName;

    public BlockDyedStairs(int id, BlockDyed model, String name, CreativeTabs tab) {
        super(id, model, 0);
        this.displayName = model.getDyedName() + " Stairs";
        this.setBlockName("moredyes." + name);
        this.setCreativeTab(tab);
        this.setTextureFile(Textures.SHEET);
        if (model.info.tool != null) {
            net.minecraftforge.common.MinecraftForge.setBlockHarvestLevel(this, model.info.tool,
                model.info.harvestLevel);
        }
    }

    @Override
    public String getDyedName() {
        return this.displayName;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityDyed();
    }

    // breakBlock goes to the dyed block the stairs are made of, which remembers the color for the drop.

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

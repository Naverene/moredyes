package info.kg6jay.moredyes.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.tileEntity.TileEntityColoredChest;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;

public class MDColoredChest extends BlockContainer implements IBlockColored, IInventory
{
    private final Random rand = new Random();

    @SideOnly(Side.CLIENT)
    protected IIcon[] blockIcons;

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    private static String[] sideNames = {"top", "front", "side"};
    protected String[] blockColors;
    protected String blockName, colorSet;
    private String customName;
    protected ItemStack[] chestContents = new ItemStack[36];


    public MDColoredChest(String[] colors, BlockInfo info, String colorSet)
    {
        super(Material.wood);
        this.blockColors = colors;
        this.blockName = info.blockName;
        this.colorSet = colorSet;
        this.setHardness(info.hardness);
        this.setHarvestLevel(info.harvestTool, info.harvestLevel);
        this.setStepSound(info.sound);
        this.setResistance(info.resistance);
        char tmp = (char) (((int) this.blockName.charAt(0)) - 32);
        this.setBlockName(colorSet + "Mix" + tmp + this.blockName.substring(1));
        this.setCreativeTab(info.tab);
        this.setBlockTextureName(Reference.MOD_ID + ":chest" + colorSet);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

    }

    @Override
    @SideOnly(Side.CLIENT) //Just for Particle effects
    public void registerBlockIcons(IIconRegister reg)
    {
        for (String blockColor : this.blockColors)
        {
            this.blockIcon = reg.registerIcon(Reference.MOD_ID + ":chest/normal/" + blockColor);
        }
    }



    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        Block block = worldIn.getBlock(x, y, z - 1);
        Block block1 = worldIn.getBlock(x, y, z + 1);
        Block block2 = worldIn.getBlock(x - 1, y, z);
        Block block3 = worldIn.getBlock(x + 1, y, z);
        byte b0 = 0;
        int l = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            b0 = 2;
        }

        if (l == 1)
        {
            b0 = 5;
        }

        if (l == 2)
        {
            b0 = 3;
        }

        if (l == 3)
        {
            b0 = 4;
        }

        if (block != this && block1 != this && block2 != this && block3 != this)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
        } else
        {
            if ((block == this || block1 == this) && (b0 == 4 || b0 == 5))
            {
                if (block == this)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z - 1, b0, 3);
                } else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z + 1, b0, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
            }

            if ((block2 == this || block3 == this) && (b0 == 2 || b0 == 3))
            {
                if (block2 == this)
                {
                    worldIn.setBlockMetadataWithNotify(x - 1, y, z, b0, 3);
                } else
                {
                    worldIn.setBlockMetadataWithNotify(x + 1, y, z, b0, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, b0, 3);
            }
        }

        if (itemIn.hasDisplayName())
        {
            ((TileEntityColoredChest) worldIn.getTileEntity(x, y, z)).func_145976_a(itemIn.getDisplayName());
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        TileEntityChest tileentitychest = (TileEntityChest) worldIn.getTileEntity(x, y, z);

        if (tileentitychest != null)
        {
            tileentitychest.updateContainingBlockInfo();
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityColoredChest te = (TileEntityColoredChest) worldIn.getTileEntity(x, y, z);

        if (te != null)
        {
            for (int i1 = 0; i1 < te.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = te.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; worldIn.spawnEntityInWorld(entityitem))
                    {
                        int j1 = this.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(worldIn, (float) x + f, (float) y + f1, (float) z + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            worldIn.func_147453_f(x, y, z, blockBroken);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public IInventory func_149951_m(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
    {
        Object object = (TileEntityChest) p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

        if (object == null)
        {
            return null;
        } else if (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_, DOWN))
        {
            return null;
        } else if (func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_))
        {
            return null;
        } else if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.isSideSolid(p_149951_2_ - 1, p_149951_3_ + 1, p_149951_4_, DOWN) || func_149953_o(p_149951_1_, p_149951_2_ - 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        } else if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.isSideSolid(p_149951_2_ + 1, p_149951_3_ + 1, p_149951_4_, DOWN) || func_149953_o(p_149951_1_, p_149951_2_ + 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        } else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this && (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ - 1, DOWN) || func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ - 1)))
        {
            return null;
        } else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this && (p_149951_1_.isSideSolid(p_149951_2_, p_149951_3_ + 1, p_149951_4_ + 1, DOWN) || func_149953_o(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ + 1)))
        {
            return null;
        } else
        {
            if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this)
            {
                object = new InventoryLargeChest("More Dyes Large Chest", (TileEntityChest) p_149951_1_.getTileEntity(p_149951_2_ - 1, p_149951_3_, p_149951_4_), (IInventory) object);
            }

            if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this)
            {
                object = new InventoryLargeChest("More Dyes Large Chest", (IInventory) object, (TileEntityChest) p_149951_1_.getTileEntity(p_149951_2_ + 1, p_149951_3_, p_149951_4_));
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this)
            {
                object = new InventoryLargeChest("More Dyes Large Chest", (TileEntityChest) p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ - 1), (IInventory) object);
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this)
            {
                object = new InventoryLargeChest("More Dyes Large Chest", (IInventory) object, (TileEntityChest) p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ + 1));
            }

            return (IInventory) object;
        }
    }


    private static boolean func_149953_o(World p_149953_0_, int p_149953_1_, int p_149953_2_, int p_149953_3_)
    {
        Iterator iterator = p_149953_0_.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox((double) p_149953_1_, (double) (p_149953_2_ + 1), (double) p_149953_3_, (double) (p_149953_1_ + 1), (double) (p_149953_2_ + 2), (double) (p_149953_3_ + 1))).iterator();
        EntityOcelot entityocelot;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            Entity entity = (Entity) iterator.next();
            entityocelot = (EntityOcelot) entity;
        }
        while (!entityocelot.isSitting());

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        TileEntityColoredChest te = new TileEntityColoredChest();
        return te;
    }

    @Override
    public String getColorSet()
    {
        return this.colorSet;
    }

    @Override
    public int getMaxMeta()
    {
        return 0;
    }

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {

    }

    @Override
    public String getInventoryName()
    {
        return blockName + "_inventory";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 0;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
}
package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;

public class MDBlockBookshelf extends MDBlockColoredMulti {

    public IIcon[] topIcons, sideIcons, bottomIcons;

    public MDBlockBookshelf(String[] colors, BlockInfo info, String colorSet) {
        super(colors, info, colorSet);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta >= this.sideIcons.length) {
            meta = 0;
        }
        return switch (side) {
            case 0 -> this.bottomIcons[meta];
            case 1 -> this.topIcons[meta];
            default -> this.sideIcons[meta];
        };
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        if (ConfigHandler.overrideDefaultMobSpawning) {
            return ConfigHandler.mobSpawnOnBlock;
        } else {
            return super.canCreatureSpawn(type, world, x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.topIcons = new IIcon[this.blockColors.length];
        this.sideIcons = new IIcon[this.blockColors.length];
        this.bottomIcons = new IIcon[this.blockColors.length];

        for (int i = 0; i < this.topIcons.length; ++i) {
            this.topIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":plank/" + blockColors[i]);
            this.sideIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + blockName + "/" + blockColors[i]);
            this.bottomIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":plank/" + blockColors[i]);
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 3;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Items.book;
    }
}

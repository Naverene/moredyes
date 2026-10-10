package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Textures;

/**
 * Plain (undyed) granite, diorite or andesite, the base for the dyed versions. It shares the grey texture of the dyed
 * version, tinted with the stone's natural color. Unlike the dyed blocks these follow the vanilla mob spawning rules,
 * because they generate underground like stone.
 */
public class BlockPlainStone extends Block {

    private final int color;

    public BlockPlainStone(int id, BlockInfo info, int texture, int color) {
        super(id, texture, info.material);
        this.color = color;
        info.apply(this);
        this.setTextureFile(Textures.SHEET);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return this.color;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.color;
    }
}

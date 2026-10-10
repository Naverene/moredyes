package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Dyed bookshelf: drops three books, like a vanilla bookshelf. */
public class BlockDyedBookshelf extends BlockDyed {

    public BlockDyedBookshelf(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Item.book, 3));
        return drops;
    }
}

package info.kg6jay.moredyes.handler;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.IFuelHandler;
import info.kg6jay.moredyes.block.MDBlocks;
import info.kg6jay.moredyes.item.SharedBlocks;

/**
 * Burn times for the dyed blocks that are not wooden (wooden blocks already burn for 300 ticks). A dyed coal block
 * is made of eight coal, so it burns as long as eight coal.
 */
public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        Block block = SharedBlocks.blockOf(fuel);
        if (block == MDBlocks.coal) {
            return 8 * 1600;
        }
        if (block == MDBlocks.sapling) {
            return 100;
        }
        return 0;
    }
}

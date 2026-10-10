package info.kg6jay.moredyes.handler;

import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

import info.kg6jay.moredyes.block.BlockDyedSapling;
import info.kg6jay.moredyes.block.MDBlocks;

/** Bone meal on a dyed sapling grows it into a tree some of the time, like on a vanilla sapling. */
public class BonemealHandler {

    @ForgeSubscribe
    public void onBonemeal(BonemealEvent event) {
        if (event.ID != MDBlocks.sapling.blockID) {
            return;
        }
        if (!event.world.isRemote && event.world.rand.nextFloat() < 0.45F) {
            ((BlockDyedSapling) MDBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
        }
        event.setResult(Event.Result.ALLOW);
    }
}

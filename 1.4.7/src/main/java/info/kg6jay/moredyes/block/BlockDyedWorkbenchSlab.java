package info.kg6jay.moredyes.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.handler.GuiHandler;

/** A dyed crafting table slab: half a dyed crafting table that opens the crafting grid like a whole one. */
public class BlockDyedWorkbenchSlab extends BlockDyedSlab {

    public BlockDyedWorkbenchSlab(int id, BlockDyed model, String name, CreativeTabs tab) {
        super(id, model, name, tab);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(MoreDyes.instance, GuiHandler.WORKBENCH, world, x, y, z);
        }
        return true;
    }
}

package info.kg6jay.moredyes.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.handler.GuiHandler;

/** A dyed crafting table. Opens the vanilla crafting grid. */
public class BlockDyedWorkbench extends BlockDyed {

    public BlockDyedWorkbench(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
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

package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.handler.GuiHandler;

/** Half a dyed crafting table, in every color. Using it opens the crafting grid, like a whole one. */
public class MDBlockDyedWorkbenchSlab extends MDBlockDyedSlab {

    public MDBlockDyedWorkbenchSlab(Block model, String name, String... textures) {
        super(model, name, textures);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(MoreDyes.instance, GuiHandler.COLORED_WORKBENCH_GUI_ID, world, x, y, z);
        }
        return true;
    }
}

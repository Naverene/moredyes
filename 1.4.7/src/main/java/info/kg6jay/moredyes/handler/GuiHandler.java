package info.kg6jay.moredyes.handler;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import info.kg6jay.moredyes.inventory.ContainerDyedWorkbench;

public class GuiHandler implements IGuiHandler {

    public static final int WORKBENCH = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == WORKBENCH) {
            return new ContainerDyedWorkbench(player.inventory, world, x, y, z);
        }
        return null;
    }

    /** The client never checks canInteractWith, so the vanilla crafting screen is used as is. */
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == WORKBENCH) {
            return new GuiCrafting(player.inventory, world, x, y, z);
        }
        return null;
    }
}

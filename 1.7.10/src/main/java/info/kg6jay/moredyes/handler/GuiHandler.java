package info.kg6jay.moredyes.handler;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int COLORED_WORKBENCH_GUI_ID = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("[MoreDyes] getServerGuiElement called with ID: " + ID);
        if (ID == COLORED_WORKBENCH_GUI_ID) {
            return new ContainerWorkbench(player.inventory, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("[MoreDyes] getClientGuiElement called with ID: " + ID);
        if (ID == COLORED_WORKBENCH_GUI_ID) {
            return new GuiCrafting(player.inventory, world, x, y, z);
        }
        return null;
    }
}

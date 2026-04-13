package info.kg6jay.moredyes.handler;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;

public class GuiHandler implements IGuiHandler {

    public static final int COLORED_WORKBENCH_GUI_ID = 1;
    public static final int COLORED_CHEST_GUI_ID = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("[MoreDyes] getServerGuiElement called with ID: " + ID);
        if (ID == COLORED_WORKBENCH_GUI_ID) {
            return new ContainerWorkbench(player.inventory, world, x, y, z);
        }
        if (ID == COLORED_CHEST_GUI_ID) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityMDBlockColoredChest) {
                return new ContainerChest(player.inventory, (TileEntityMDBlockColoredChest) te);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        System.out.println("[MoreDyes] getClientGuiElement called with ID: " + ID);
        if (ID == COLORED_WORKBENCH_GUI_ID) {
            return new GuiCrafting(player.inventory, world, x, y, z);
        }
        if (ID == COLORED_CHEST_GUI_ID) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityMDBlockColoredChest) {
                return new GuiChest(player.inventory, (TileEntityMDBlockColoredChest) te);
            }
        }
        return null;
    }
}

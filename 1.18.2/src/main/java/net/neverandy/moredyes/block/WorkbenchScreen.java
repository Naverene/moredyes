package net.neverandy.moredyes.block;

import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.text.ITextComponent;

public class WorkbenchScreen extends CraftingScreen
{
    public WorkbenchScreen(WorkbenchContainer container, PlayerInventory player, ITextComponent textComponent)
    {
        super(container, player, textComponent);
    }
}

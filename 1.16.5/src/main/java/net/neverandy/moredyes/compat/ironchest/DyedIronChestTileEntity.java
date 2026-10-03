package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

/** The inventory of a dyed Iron Chests chest: the size and screen of its tier. */
public class DyedIronChestTileEntity extends GenericIronChestTileEntity
{
    private final IronChestCompat.Tier tier;

    public DyedIronChestTileEntity(IronChestCompat.Tier tier)
    {
        super(tier.tileEntity.get(), tier.type, tier.block::get);
        this.tier = tier;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return tier.container.create(id, inventory, this);
    }
}

package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.tileentity.GenericIronShulkerBoxTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import java.util.Collections;

/** The inventory of a dyed Iron Shulker Boxes box: the size and screen of its tier. */
public class DyedIronShulkerBoxTileEntity extends GenericIronShulkerBoxTileEntity
{
    private final IronShulkerBoxCompat.Tier tier;

    public DyedIronShulkerBoxTileEntity(IronShulkerBoxCompat.Tier tier)
    {
        // No DyeColor: the color is the block's COLOR property. Iron Shulker Boxes only uses the block list and the
        // color to draw its own boxes, which this renderer doesn't.
        super(tier.tileEntity.get(), null, tier.type, Collections.emptyList());
        this.tier = tier;
    }

    public IronShulkerBoxCompat.Tier tier()
    {
        return tier;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory)
    {
        return tier.container.create(id, inventory, this);
    }

    @Override
    public Block getBlockToUse()
    {
        return tier.block.get();
    }
}

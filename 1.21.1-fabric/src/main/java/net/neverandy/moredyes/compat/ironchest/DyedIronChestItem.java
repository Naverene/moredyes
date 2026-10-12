package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.color.MixColors;

/**
 * A dyed Iron Chests chest. Its color is in the item's {@code minecraft:block_state} component. Its item model is
 * vanilla's chest item model (builtin/entity), drawn by client/IronChestClient.
 */
public class DyedIronChestItem extends BlockItem {

    private final DyedIronChestBlock block;

    public DyedIronChestItem(DyedIronChestBlock block, Properties properties) {
        super(block, properties);
        this.block = block;
    }

    public IronChestCompat.Tier tier() {
        return block.tier();
    }

    /** "C56685 Iron Chest": Iron Chests' name for the tier (in every language it has), after the hex code. */
    @Override
    public Component getName(ItemStack stack) {
        return name(tier(), IronChestCompat.colorOf(stack));
    }

    static Component name(IronChestCompat.Tier tier, int color) {
        return Component.literal(MixColors.ALL.get(color).label() + " ").append(tier.plain().getName());
    }
}

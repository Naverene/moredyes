package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.component.SuspiciousStewEffects;

/** A dyed small flower. Unlike vanilla flowers it can also be planted on sand, dyed sand included. */
public class DyedFlowerBlock extends FlowerBlock {

    public DyedFlowerBlock(Holder<MobEffect> effect, float seconds, Properties properties) {
        super(effect, seconds, properties);
    }

    public DyedFlowerBlock(SuspiciousStewEffects effects, Properties properties) {
        super(effects, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.SAND);
    }
}

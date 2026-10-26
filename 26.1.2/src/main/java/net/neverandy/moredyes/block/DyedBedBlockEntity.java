package net.neverandy.moredyes.block;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModComponents;

/**
 * A More Dyes bed's block entity: vanilla's, plus the planks the bed was made from, which client/DyedBedRenderer draws
 * its frame in. Both halves of the bed hold it. The item keeps it as the {@code moredyes:wood} component, the planks'
 * block id; a bed without it (or made from oak) has vanilla's oak frame.
 */
public class DyedBedBlockEntity extends BedBlockEntity {

    private static final String WOOD = "Wood";
    public static final Identifier OAK = Identifier.withDefaultNamespace("oak_planks");

    private @Nullable Identifier wood;

    public DyedBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.BED.get();
    }

    public @Nullable Identifier getWood() {
        return wood;
    }

    public void setWood(@Nullable Identifier wood) {
        this.wood = OAK.equals(wood) ? null : wood;
        setChanged();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        wood = input.read(WOOD, Identifier.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (wood != null) {
            output.store(WOOD, Identifier.CODEC, wood);
        }
    }

    /** Sent to the client, which draws the frame. */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        setWood(components.get(ModComponents.WOOD.get()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (wood != null) {
            components.set(ModComponents.WOOD.get(), wood);
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(WOOD);
    }

    /** The planks a bed item was made from, or null for oak. */
    public static @Nullable Identifier woodOf(ItemStack stack) {
        return stack.get(ModComponents.WOOD.get());
    }

    /** Records the planks on a bed item; oak (or null) leaves the item plain, so it stacks with other beds. */
    public static void setWood(ItemStack stack, @Nullable Identifier wood) {
        if (wood == null || OAK.equals(wood)) {
            stack.remove(ModComponents.WOOD.get());
        } else {
            stack.set(ModComponents.WOOD.get(), wood);
        }
    }
}

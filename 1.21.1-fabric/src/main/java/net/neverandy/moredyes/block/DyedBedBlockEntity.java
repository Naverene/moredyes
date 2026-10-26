package net.neverandy.moredyes.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModComponents;

/**
 * A More Dyes bed's block entity: vanilla's, plus the planks the bed was made from, which client/DyedBedRenderer draws
 * its frame in. Both halves of the bed hold it. The item keeps it as the {@code moredyes:wood} component, the planks'
 * block id; a bed without it (or made from oak) has vanilla's oak frame.
 */
public class DyedBedBlockEntity extends BedBlockEntity {

    private static final String WOOD = "Wood";
    public static final ResourceLocation OAK = ResourceLocation.withDefaultNamespace("oak_planks");

    @Nullable
    private ResourceLocation wood;

    public DyedBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.BED;
    }

    @Nullable
    public ResourceLocation getWood() {
        return wood;
    }

    public void setWood(@Nullable ResourceLocation wood) {
        this.wood = OAK.equals(wood) ? null : wood;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        wood = tag.contains(WOOD) ? ResourceLocation.tryParse(tag.getString(WOOD)) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (wood != null) {
            tag.putString(WOOD, wood.toString());
        }
    }

    /** Sent to the client, which draws the frame. */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        setWood(input.get(ModComponents.WOOD));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (wood != null) {
            components.set(ModComponents.WOOD, wood);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(WOOD);
    }

    /** The planks a bed item was made from, or null for oak. */
    @Nullable
    public static ResourceLocation woodOf(ItemStack stack) {
        return stack.get(ModComponents.WOOD);
    }

    /** Records the planks on a bed item; oak (or null) leaves the item plain, so it stacks with other beds. */
    public static void setWood(ItemStack stack, @Nullable ResourceLocation wood) {
        if (wood == null || OAK.equals(wood)) {
            stack.remove(ModComponents.WOOD);
        } else {
            stack.set(ModComponents.WOOD, wood);
        }
    }
}

package net.neverandy.moredyes.compat.ironshulkerbox.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxItem;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;

/** Draws a dyed Iron Shulker Boxes item as a small closed box, like Iron Shulker Boxes does for its own. */
public class DyedIronShulkerBoxItemRenderer extends BlockEntityWithoutLevelRenderer
{
    private static DyedIronShulkerBoxItemRenderer instance;
    private DyedIronShulkerBoxRenderer box;

    private DyedIronShulkerBoxItemRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public static DyedIronShulkerBoxItemRenderer get()
    {
        if (instance == null)
        {
            instance = new DyedIronShulkerBoxItemRenderer();
        }
        return instance;
    }

    /** The box model comes from code, not resource packs, so it doesn't need baking again when resources reload. */
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        if (stack.getItem() instanceof DyedIronShulkerBoxItem item)
        {
            if (box == null)
            {
                box = new DyedIronShulkerBoxRenderer(Minecraft.getInstance().getEntityModels());
            }
            box.render(item.tier(), IronShulkerBoxCompat.colorOf(stack), Direction.UP, 0.0F, pose, buffer, light, overlay);
        }
    }
}

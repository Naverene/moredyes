package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestItem;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/** Draws a dyed Iron Chests chest item as a small closed chest, like Iron Chests does for its own. */
public class DyedIronChestItemRenderer extends BlockEntityWithoutLevelRenderer
{
    private static DyedIronChestItemRenderer instance;
    private DyedIronChestRenderer chest;

    private DyedIronChestItemRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public static DyedIronChestItemRenderer get()
    {
        if (instance == null)
        {
            instance = new DyedIronChestItemRenderer();
        }
        return instance;
    }

    /** The chest model comes from code, not resource packs, so it doesn't need baking again when resources reload. */
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        if (stack.getItem() instanceof DyedIronChestItem item)
        {
            if (chest == null)
            {
                chest = new DyedIronChestRenderer(Minecraft.getInstance().getEntityModels());
            }
            chest.render(item.tier(), IronChestCompat.colorOf(stack), Direction.SOUTH, 0.0F, pose, buffer, light, overlay);
        }
    }
}

package net.neverandy.moredyes.compat.storagedrawers.client;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.client.model.BasicDrawerModel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersBlock;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersItem;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Client side of dyed drawers. Their models are grey and mark every face with tintindex 0, which is colored here. The
 * rest is what Storage Drawers 8.5 only does for its own blocks: its cutout render layer, its model wrapper that adds the
 * lock, void, shroud and fill indicator overlays, and the positions of the item labels and counts on the front, which it
 * reads from model files into each block once its textures are stitched. Ours copy them from the oak drawer of their size.
 */
public final class StorageDrawersClient
{
    private StorageDrawersClient() {}

    public static void register(IEventBus modBus)
    {
        modBus.addListener(StorageDrawersClient::setup);
        modBus.addListener(StorageDrawersClient::blockColors);
        modBus.addListener(StorageDrawersClient::itemColors);
        modBus.addListener(StorageDrawersClient::bakeModels);
    }

    private static void setup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            for (DyedDrawersBlock block : StorageDrawersCompat.blocks())
            {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped());
            }
        });
    }

    /** Runs after the textures are stitched, so Storage Drawers' blocks have their label positions by then. */
    private static void bakeModels(ModelBakeEvent event)
    {
        for (DyedDrawersBlock block : StorageDrawersCompat.blocks())
        {
            BasicDrawerModel.Register.replaceBlock(event, block);
            Block plain = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(StorageDrawersCompat.MOD_ID, block.plainName));
            if (plain instanceof BlockDrawers && ((BlockDrawers) plain).getDrawerCount() == block.getDrawerCount())
            {
                BlockDrawers oak = (BlockDrawers) plain;
                copy(oak.labelGeometry, block.labelGeometry);
                copy(oak.countGeometry, block.countGeometry);
                copy(oak.indGeometry, block.indGeometry);
                copy(oak.indBaseGeometry, block.indBaseGeometry);
                copy(oak.slotGeometry, block.slotGeometry);
            }
            else
            {
                MoreDyes.LOGGER.warn("No Storage Drawers block {} to take label positions from for {}", block.plainName, block.getRegistryName());
            }
        }
    }

    private static void copy(AxisAlignedBB[] from, AxisAlignedBB[] to)
    {
        System.arraycopy(from, 0, to, 0, Math.min(from.length, to.length));
    }

    private static int color(int index)
    {
        return ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[index], 16));
    }

    private static void blockColors(ColorHandlerEvent.Block event)
    {
        event.getBlockColors().register((state, world, pos, tintIndex) -> tintIndex == 0 ? color(state.get(DyedDrawersBlock.COLOR)) : 0xFFFFFF,
                StorageDrawersCompat.blocks().toArray(new DyedDrawersBlock[0]));
    }

    private static void itemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register((stack, tintIndex) -> tintIndex == 0 ? color(StorageDrawersCompat.colorOf(stack)) : 0xFFFFFF,
                StorageDrawersCompat.items().toArray(new DyedDrawersItem[0]));
    }
}

package net.neverandy.moredyes.paint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.reference.Reference;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Paint on blocks in the world. The paint is kept per chunk ({@link PaintData}) on the server and sent to the
 * players who can see the chunk; the block itself is not changed, so a painted block works and drops as usual.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class Painting
{
    private Painting() {}

    /** Whether a block can be painted: it has to be drawn from a block model, not by a block entity renderer. */
    public static boolean canPaint(BlockState state)
    {
        return !state.isAir() && state.getRenderShape() == RenderShape.MODEL;
    }

    @Nullable
    private static PaintData data(Level level, BlockPos pos)
    {
        return level.getChunkAt(pos).getCapability(PaintData.CAPABILITY).resolve().orElse(null);
    }

    /** The paint on a block, or null if it has none. */
    @Nullable
    public static PaintData.Paint get(Level level, BlockPos pos)
    {
        PaintData data = data(level, pos);
        return data == null ? null : data.get(pos);
    }

    /** Paints a block a color, 0xRRGGBB. */
    public static void paint(ServerLevel level, BlockPos pos, int rgb)
    {
        PaintData data = data(level, pos);
        if (data != null)
        {
            PaintData.Paint paint = new PaintData.Paint(rgb & 0xFFFFFF, level.getBlockState(pos).getBlock());
            data.set(pos, paint);
            send(level, pos, paint);
        }
    }

    /** Removes a block's paint, and says whether it had any. */
    public static boolean erase(Level level, BlockPos pos)
    {
        PaintData data = data(level, pos);
        if (data == null || !data.remove(pos))
        {
            return false;
        }
        if (level instanceof ServerLevel server)
        {
            send(server, pos, null);
        }
        return true;
    }

    private static void send(ServerLevel level, BlockPos pos, @Nullable PaintData.Paint paint)
    {
        Map<Long, PaintData.Paint> change = new HashMap<>();
        change.put(pos.asLong(), paint);
        LevelChunk chunk = level.getChunkAt(pos);
        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new PaintPacket(chunk.getPos(), false, change));
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<LevelChunk> event)
    {
        event.addCapability(PaintData.ID, new PaintData(event.getObject()));
    }

    /** Sends a chunk's paint to a player who starts seeing it, right after the chunk itself. */
    @SubscribeEvent
    public static void watch(ChunkWatchEvent.Watch event)
    {
        event.getChunk().getCapability(PaintData.CAPABILITY).ifPresent(data ->
        {
            Map<Long, PaintData.Paint> paints = data.all();
            if (!paints.isEmpty())
            {
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(event::getPlayer),
                        new PaintPacket(event.getPos(), true, new HashMap<>(paints)));
            }
        });
    }

    // A painted block that is broken or replaced loses its paint, so a new block placed there starts clean.

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void broken(BlockEvent.BreakEvent event)
    {
        if (event.getLevel() instanceof Level level)
        {
            erase(level, event.getPos());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void placed(BlockEvent.EntityPlaceEvent event)
    {
        if (event.getLevel() instanceof Level level)
        {
            erase(level, event.getPos());
        }
    }

    @SubscribeEvent
    public static void exploded(ExplosionEvent.Detonate event)
    {
        for (BlockPos pos : event.getAffectedBlocks())
        {
            erase(event.getLevel(), pos);
        }
    }
}

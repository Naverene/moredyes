package net.neverandy.moredyes.entity;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.BlockColoredCarpet;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.network.LlamaCarpetMessage;
import net.neverandy.moredyes.network.ModNetwork;

/**
 * Tamed llamas wear dyed carpet. The 1.12 llama only takes the vanilla carpet in its carpet slot, so a player puts a
 * dyed one on by using it on the llama, and takes it off in the llama's inventory as usual. The carpet sits in the
 * vanilla slot, so it is saved and dropped like a vanilla one; the vanilla llama just does not draw it. The server
 * tells the players who see the llama its color, and client/LayerDyedLlamaDecor draws it.
 */
public class LlamaCarpets
{
	/** On the client: the color of the llama's dyed carpet, kept in the llama's own data. */
	private static final String CLIENT_TAG="moredyes:carpet";
	private static final String HORSE_CHEST="field_110296_bG";
	private static final int CHECK_TICKS=10;
	/** The color last sent for each llama the server knows. */
	private final Map<EntityLlama,Integer> sent=new WeakHashMap<EntityLlama,Integer>();

	private static ContainerHorseChest chest(EntityLlama llama)
	{
		return ObfuscationReflectionHelper.getPrivateValue(AbstractHorse.class,llama,HORSE_CHEST);
	}
	/** The color of a dyed carpet as 0xRRGGBB, or -1 if the stack is not one. */
	private static int color(ItemStack stack)
	{
		Block block=Block.getBlockFromItem(stack.getItem());
		return block instanceof BlockColoredCarpet?((IColoredBlock)block).getColor(stack.getMetadata()):-1;
	}
	/** The color of the dyed carpet a llama wears on the server, or -1. */
	private static int serverColor(EntityLlama llama)
	{
		return color(chest(llama).getStackInSlot(1));
	}
	/** The color of the dyed carpet a llama wears, as the server sent it, or -1. */
	public static int clientColor(EntityLlama llama)
	{
		return llama.getEntityData().hasKey(CLIENT_TAG)?llama.getEntityData().getInteger(CLIENT_TAG):-1;
	}
	/** Records the color the server sent for a llama. */
	public static void storeClientColor(EntityLlama llama,int color)
	{
		if(color<0)
		{
			llama.getEntityData().removeTag(CLIENT_TAG);
		}
		else
		{
			llama.getEntityData().setInteger(CLIENT_TAG,color);
		}
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteract event)
	{
		if(!(event.getTarget() instanceof EntityLlama))
		{
			return;
		}
		EntityLlama llama=(EntityLlama)event.getTarget();
		ItemStack held=event.getItemStack();
		if(color(held)<0||!llama.isTame()||llama.isChild())
		{
			return;
		}
		// The client only knows whether the llama wears a carpet it can see, which is enough to tell.
		if(event.getWorld().isRemote)
		{
			if(llama.getColor()==null&&clientColor(llama)<0)
			{
				event.setCanceled(true);
				event.setCancellationResult(EnumActionResult.SUCCESS);
			}
			return;
		}
		ContainerHorseChest chest=chest(llama);
		if(!chest.getStackInSlot(1).isEmpty())
		{
			return;
		}
		ItemStack carpet=held.copy();
		carpet.setCount(1);
		chest.setInventorySlotContents(1,carpet);
		EntityPlayer player=event.getEntityPlayer();
		if(!player.capabilities.isCreativeMode)
		{
			held.shrink(1);
		}
		llama.playSound(SoundEvents.ENTITY_LLAMA_SWAG,0.5f,1.0f);
		this.send(llama);
		event.setCanceled(true);
		event.setCancellationResult(EnumActionResult.SUCCESS);
	}
	/** Catches the carpet being taken off or swapped in the llama's inventory. */
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		Entity entity=event.getEntity();
		if(entity instanceof EntityLlama&&!entity.world.isRemote&&entity.ticksExisted%CHECK_TICKS==0)
		{
			EntityLlama llama=(EntityLlama)entity;
			Integer last=this.sent.get(llama);
			int color=serverColor(llama);
			if(last==null?color>=0:last!=color)
			{
				this.send(llama);
			}
		}
	}
	@SubscribeEvent
	public void onStartTracking(PlayerEvent.StartTracking event)
	{
		if(event.getTarget() instanceof EntityLlama&&event.getEntityPlayer() instanceof EntityPlayerMP)
		{
			int color=serverColor((EntityLlama)event.getTarget());
			if(color>=0)
			{
				ModNetwork.CHANNEL.sendTo(new LlamaCarpetMessage(event.getTarget().getEntityId(),color),(EntityPlayerMP)event.getEntityPlayer());
			}
		}
	}
	private void send(EntityLlama llama)
	{
		int color=serverColor(llama);
		this.sent.put(llama,color);
		ModNetwork.CHANNEL.sendToAllTracking(new LlamaCarpetMessage(llama.getEntityId(),color),llama);
	}
}

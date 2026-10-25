package net.neverandy.moredyes.handler;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.network.SheepColorMessage;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Lets tamed wolves wear a collar in any mod color. Vanilla collars only know the 16 vanilla colors, so the mod color
 * is kept in the wolf's Forge data (saved with it) and sent to the players who can see it, the same way as for sheep
 * (see DyedSheepHandler). The client draws the collar in that color (client/LayerDyedWolfCollar).
 */
public class DyedCollarHandler
{
	/** Saved on the wolf: the position of its collar color in ColorStrings.ALL. Absent for a vanilla color. */
	private static final String COLOR_TAG="MoreDyesCollar";

	/** The mod collar color of a wolf as a position in ColorStrings.ALL, or -1 when it has a vanilla color. */
	public static int getColor(EntityWolf wolf)
	{
		if(!wolf.getEntityData().hasKey(COLOR_TAG))
		{
			return -1;
		}
		int color=wolf.getEntityData().getInteger(COLOR_TAG);
		return color>=0&&color<ColorStrings.ALL.length?color:-1;
	}
	/** Colors a collar (-1 for back to its vanilla color) and tells the players who can see the wolf. */
	public static void setColor(EntityWolf wolf,int color)
	{
		storeColor(wolf,color);
		if(!wolf.world.isRemote)
		{
			ModNetwork.CHANNEL.sendToAllTracking(new SheepColorMessage(wolf.getEntityId(),color),wolf);
		}
	}
	/** Records the color on the wolf without telling anyone; the client uses this for what the server sent it. */
	public static void storeColor(EntityWolf wolf,int color)
	{
		if(color<0)
		{
			wolf.getEntityData().removeTag(COLOR_TAG);
		}
		else
		{
			wolf.getEntityData().setInteger(COLOR_TAG,color);
		}
	}

	/**
	 * Dyes the collar of a tamed wolf that belongs to the player. This runs before the wolf's own right click, which
	 * would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar.
	 */
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteract event)
	{
		if(!(event.getTarget() instanceof EntityWolf))
		{
			return;
		}
		EntityWolf wolf=(EntityWolf)event.getTarget();
		EntityPlayer player=event.getEntityPlayer();
		ItemStack held=event.getItemStack();
		if(!wolf.isEntityAlive()||!wolf.isTamed()||!wolf.isOwner(player))
		{
			return;
		}
		boolean server=!event.getWorld().isRemote;
		int color=getColor(wolf);
		if(held.getItem() instanceof MDItemDye)
		{
			int dye=((MDItemDye)held.getItem()).getColorIndex(held.getMetadata());
			if(dye==color)
			{
				return;
			}
			if(server)
			{
				setColor(wolf,dye);
			}
		}
		else if(held.getItem()==Items.DYE&&color>=0)
		{
			// A vanilla dye on a mod collar gives it that vanilla color again, even the one hidden underneath.
			if(server)
			{
				setColor(wolf,-1);
				wolf.setCollarColor(EnumDyeColor.byDyeDamage(held.getMetadata()));
			}
		}
		else
		{
			return;
		}
		if(server&&!player.capabilities.isCreativeMode)
		{
			held.shrink(1);
		}
		event.setCanceled(true);
		event.setCancellationResult(EnumActionResult.SUCCESS);
	}

	/** Tells a player the collar color of a wolf that comes into view. */
	@SubscribeEvent
	public void onStartTracking(PlayerEvent.StartTracking event)
	{
		if(event.getTarget() instanceof EntityWolf&&event.getEntityPlayer() instanceof EntityPlayerMP)
		{
			EntityWolf wolf=(EntityWolf)event.getTarget();
			int color=getColor(wolf);
			if(color>=0)
			{
				ModNetwork.CHANNEL.sendTo(new SheepColorMessage(wolf.getEntityId(),color),(EntityPlayerMP)event.getEntityPlayer());
			}
		}
	}
}

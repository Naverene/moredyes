package net.neverandy.moredyes.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.neverandy.moredyes.MoreDyes;

/** Tells a client the color (0xRRGGBB, or -1 for none) of the dyed carpet a llama it can see wears. */
public class LlamaCarpetMessage implements IMessage
{
	private int entityId;
	private int color;

	public LlamaCarpetMessage()
	{
	}
	public LlamaCarpetMessage(int entityId,int color)
	{
		this.entityId=entityId;
		this.color=color;
	}
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityId=buf.readInt();
		this.color=buf.readInt();
	}
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityId);
		buf.writeInt(this.color);
	}

	public static class Handler implements IMessageHandler<LlamaCarpetMessage,IMessage>
	{
		@Override
		public IMessage onMessage(LlamaCarpetMessage message,MessageContext ctx)
		{
			MoreDyes.proxy.setLlamaCarpet(message.entityId,message.color);
			return null;
		}
	}
}

package dfs.dkv.api.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketEvent implements Cancellable {
	private boolean issend = false;
	private boolean cancelled = false;
	private Object packet;
	private Player player;
	private ChannelHandlerContext context;
	private ChannelPromise promise;
	public PacketEvent (Player p, ChannelHandlerContext cc, Object pk, ChannelPromise cp) {
		player = p;
		context = cc;
		promise = cp;
		packet = pk;
		if (cp != null) {
			issend = true;
		}
	}

	public Object getPacket () {
		return packet;
	}
	public ChannelPromise getPromise () {
		return promise;
	}
	public ChannelHandlerContext getContext () {
		return context;
	}
	public Player getPlayer () {
		return player;
	}
	public boolean isSend () {
		return (issend == true);
	}
	public boolean isReceive () {
		return (issend == false);
	}
	public boolean is (Class<?> cl) {
		if (cl == null) {
			return false;
		}
		return packet.getClass().equals(cl);
	}

	@Override
	public boolean isCancelled () {
		return cancelled;
	}

	@Override
	public void setCancelled (boolean c) {
		cancelled = c;
	}
}
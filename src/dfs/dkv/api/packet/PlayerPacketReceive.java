package dfs.dkv.api.packet;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelHandlerContext;

public class PlayerPacketReceive extends PacketEvent{
	public PlayerPacketReceive (Player p, ChannelHandlerContext cc, Object pk) {
		super(p, cc, pk, null);
	}
}
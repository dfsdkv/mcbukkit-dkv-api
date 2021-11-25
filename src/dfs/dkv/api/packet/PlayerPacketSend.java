package dfs.dkv.api.packet;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PlayerPacketSend extends PacketEvent {
	public PlayerPacketSend (Player p, ChannelHandlerContext cc, Object pk, ChannelPromise cp) {
		super(p, cc, pk, cp);
	}
}
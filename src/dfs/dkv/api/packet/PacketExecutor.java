package dfs.dkv.api.packet;

import java.lang.reflect.Method;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketExecutor implements Listener {
	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		addPlayer(e.getPlayer());
	}
	@EventHandler
	public void onQuit (PlayerQuitEvent e) {
		removePlayer(e.getPlayer());
	}



	private void removePlayer (Player p) {
		Channel c = ((CraftPlayer) p).getHandle().playerConnection.networkManager.channel;
		c.eventLoop().submit(() -> {
			c.pipeline().remove(p.getName());
			return null;
		});
	}
	private void addPlayer (Player p) {
		((CraftPlayer) p).getHandle().playerConnection.networkManager.channel.pipeline().addBefore(
			"packet_handler", p.getName(),
			new ChannelDuplexHandler() {
				// Pacotes que o servidor está enviando
				@Override
				public void channelRead (ChannelHandlerContext cc, Object pk) throws Exception {
					invoke(p, cc, pk, null, "receive");
				}
				// Pacotes que o jogador está enviando
				@Override
				public void write (ChannelHandlerContext cc, Object pk, ChannelPromise cp) throws Exception {
					invoke(p, cc, pk, cp, "send");
				}
				private void invoke (Player p, ChannelHandlerContext cc, Object pk, ChannelPromise cp, String tp) {
					boolean sended = false;
					for (JavaPlugin pl : Packet.getAllRegisteredListeners().keySet()) {
						for (PacketListener pe : Packet.getRegisteredListeners(pl)) {
							for (Method m : pe.getClass().getMethods()) {
								if (m.isAnnotationPresent(EventHandler.class) && m.getParameterCount() == 1) {
									for (Class<?> c : m.getParameterTypes()) {
										if (c.equals(PlayerPacketReceive.class) || c.equals(PlayerPacketSend.class) || c.equals(PacketEvent.class)) {
											m.setAccessible(true);
											sended = true;
											if (tp.equals("send") && c.equals(PlayerPacketSend.class)) { // PlayerPacketSend
												try {
													PlayerPacketSend pp = new PlayerPacketSend(p, cc, pk, cp);
													m.invoke(pe, pp);
													if (!pp.isCancelled()) {
														super.write(cc, pk, cp);
													}
												} catch (Exception err) {}
											} else if (tp.equals("receive") && c.equals(PlayerPacketReceive.class)) { // PlayerPacketReceive
												try {
													PlayerPacketReceive pp = new PlayerPacketReceive(p, cc, pk);
													m.invoke(pe, pp);
													if (!pp.isCancelled()) {
														super.channelRead(cc, pk);
													}
												} catch (Exception err) {}
											} else if (c.equals(PacketEvent.class)) {
												try {
													PacketEvent pp = new PacketEvent(p, cc, pk, cp);
													m.invoke(pe, pp);
													if (!pp.isCancelled()) {
														if (tp.equals("send")) {
															super.write(cc, pk, cp);
														} else {
															super.channelRead(cc, pk);
														}
													}
												} catch (Exception err) {}
											}
										}
									}
								}
							}
						}
					}
					if (!sended) {
						if (tp.equals("send")) {
							try {
								super.write(cc, pk, cp);
							} catch (Exception e) {}
						} else {
							try {
								super.channelRead(cc, pk);
							} catch (Exception e) {}
						}
					}
				}
			}
		);
	}
}
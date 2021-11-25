package dfs.dkv.api.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("all")
public class Packet {
	private static Map<JavaPlugin, List<PacketListener>> listeners = new HashMap<>();
	public static void registerListener (JavaPlugin pl, PacketListener pk) {
		List<PacketListener> ls = getRegisteredListeners(pl);
		ls.add(pk);
		listeners.put(pl, ls);
	}
	public static void unregisterListeners (JavaPlugin pl) {
		List<PacketListener> ls = getRegisteredListeners(pl);
		if (ls.size() > 0) {
			ls.clear();
			listeners.put(pl, ls);
		}
	}
	public static List<PacketListener> getRegisteredListeners (JavaPlugin pl) {
		List<PacketListener> ls = new ArrayList<>();
		if (listeners.containsKey(pl)) {
			ls = listeners.get(pl);
		}
		return ls;
	}
	public static Map<JavaPlugin, List<PacketListener>> getAllRegisteredListeners () {
		return listeners;
	}

	public Object create (Class<?> cl, Object ...params) {
		Class<?>[] pr = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			pr[i] = params[i].getClass();
		}
		Object i = null;
		try {
			i = cl.getDeclaredConstructor(pr).newInstance(params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
				InvocationTargetException | NoSuchMethodException | SecurityException e) {}
		return i;
	}
	public void sendToPlayer (Player p, Object pk) {
		if (p != null && p.isOnline() && pk instanceof net.minecraft.server.v1_8_R3.Packet) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket((net.minecraft.server.v1_8_R3.Packet) pk);
		}
	}
	public void sendToServer (Object pk) {
		if  (pk instanceof net.minecraft.server.v1_8_R3.Packet) {
			((CraftServer) Bukkit.getServer()).getHandle().sendAll((net.minecraft.server.v1_8_R3.Packet) pk);;
		}
	}
}
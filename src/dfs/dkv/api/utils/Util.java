/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import dfs.dkv.api.DkvAPI;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

@SuppressWarnings("all")

/**
 * Class Util.
 */
public class Util {	
	/**
	 * Pegar o field privado de uma classe.
	 *
	 * @param name o nome do field
	 * @param clazz as classe que o field está
	 * @return O field
	 */
	public static Field getField (String name, Class<?> clazz) {
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
		} catch (Exception ex) {}
		return null;
	}

	/**
	 * Pegar o método privado de uma classe.
	 *
	 * @param name o nome do método
	 * @param clazz a classe que o método está
	 * @return O método
	 */
	public static Method getMethod (String name, Class<?> clazz) {
		Method[] arrayOfMethod;
		int j = (arrayOfMethod = clazz.getDeclaredMethods()).length;
		for (int i = 0; i < j; i++) {
			Method m = arrayOfMethod[i];
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Pegar o name prefixed.
	 *
	 * @param p o p
	 * @return O name prefixed
	 */
	/*
	 * Pegar o nome do jogador junto com a tag
	 */
	public static String getNamePrefixed (OfflinePlayer p) {
		/*String n = Fake.getName(p);
		if (p != null) {
			UserPerm up = new UserPerm(p);
			GroupPerm g = up.getGroup();
			if (g != null) {
				n = g.getPrefixedSuffixed(p);
			}
		}
		return n;*/
		return "";
	}

	/**
	 * Enviar um título/subtítulo para um jogador com tempo de exibição.
	 *
	 * @param p o jogador a receber o título
	 * @param title o título
	 * @param subtitle o subtitulo
	 * @param show o tempo que o título vai ficar na tela (em segundos)
	 * @param fadein o tempo de transição inicial do título (em ticks)
	 * @param fadeout o tempo de transição final do título (em ticks)
	 */
	public static void sendTitle (Player p, String title, String subtitle, int show, int fadein, int fadeout) {
		p.resetTitle();
		show *= 20;
		if (title != null && !title.isEmpty()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(
				new PacketPlayOutTitle(EnumTitleAction.TITLE,
					ChatSerializer.a("{\"text\": \"" + title + "\"}"), fadein, show, fadeout)
			);
		}
		if (subtitle != null && !subtitle.isEmpty()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(
				new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
					ChatSerializer.a("{\"text\": \"" + subtitle + "\"}"), fadein, show, fadeout)
			);
		}
	}

	/**
	 * Enviar mensagens na hotbar.
	 *
	 * @param p o jogador a receber a mensagem
	 * @param m a mensagem a ser enviada
	 */
	public static void sendActionbarMessage (Player p, String m) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(
			new PacketPlayOutChat(ChatSerializer.a("{\"text\": \"" + m + "\"}"), (byte) 2)
		);
	}

	/**
	 * Pegar um valor de Metadata de um bloco ou entidade.
	 *
	 * @param o o bloco/entidade
	 * @param attr o nome do atributo a ser pêgo
	 * @return O MetadataValue
	 */
	public static MetadataValue getMetadataAttr (Object o, String attr) {
		MetadataValue obj = null;
		if (o instanceof Block) {
			for (MetadataValue mv : ((Block) o).getMetadata(attr)) {
				obj = mv;
			}
		} else if (o instanceof Entity) {
			for (MetadataValue mv : ((Entity) o).getMetadata(attr)) {
				obj = mv;
			}
		}
		return obj;
	}

	/**
	 * Setar um valor de Metadata de um bloco ou entidade.
	 *
	 * @param o o bloco/entidade
	 * @param attr o nome do atributo
	 * @param value o valor a ser definido
	 */
	public static void setMetadataAttr (Object o, String attr, Object value) {
		FixedMetadataValue d = new FixedMetadataValue(DkvAPI.getPlug(), value);
		if (o instanceof Block) {
			((Block) o).setMetadata(attr, d);
		} else if (o instanceof Entity) {
			((Entity) o).setMetadata(attr, d);
		}
	}

	/**
	 * Remover um atributo de Metadata de um bloco ou entidade.
	 *
	 * @param o o bloco/entidade
	 * @param attr o nome do atributo a ser removido
	 */
	public static void removeMetadataAttr (Object o, String attr) {
		if (o instanceof Block) {
			((Block) o).removeMetadata(attr, DkvAPI.getPlug());
		} else if (o instanceof Entity) {
			((Entity) o).removeMetadata(attr, DkvAPI.getPlug());
		}
	}
}
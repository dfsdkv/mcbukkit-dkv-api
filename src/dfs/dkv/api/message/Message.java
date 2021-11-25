/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.message;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Achievement;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

/**
 * Class Message.
 */
public class Message {
	
	/** As partes que formam a mensagem com ações. */
	private List<TextComponent> msgs = new ArrayList<>();
	
	/** As partes que formam a mensagem. */
	private List<String> msg = new ArrayList<>();
	
	/** É mensagem com ações ou não. */
	private boolean str = false;
	
	/** O recebedores da mensagem. */
	private List<Player> recipients = new ArrayList<>();

	/**
	 * Construtor de message.
	 *
	 * @param m a mensagem comum
	 */
	public Message (String m) {
		if (m != null) {
			msg.add(m);
		}
		str = true;
	}

	/**
	 * Construtor de message.
	 *
	 * @param strs a mensgaem dividia em parte
	 */
	public Message (List<String> strs) {
		msg.addAll(strs);
		/*for (String s : strs) {
			msg.add(s);
		}*/
		str = true;
	}

	/**
	 * Construtor de message.
	 */
	public Message () {
		str = false;
	}

	/**
	 * Construtor de message.
	 *
	 * @param m a mensahem comum
	 * @param p o jogador recebedor da mensagem
	 */
	public Message (String m, Player p) {
		this(m);
		addRecipient(p);
	}

	/**
	 * Construtor de message.
	 *
	 * @param p o jogador recebedor da mensagem
	 */
	public Message (Player p) {
		this();
		addRecipient(p);
	}

	/**
	 * Substituir uma parte da mensagem por um valor.
	 *
	 * @param var a variável a ser substituída
	 * @param val o valor da variável
	 * @return O message
	 */
	public Message repVar (String var, String val) {
		if (val == null) {
			val = "";
		}
		for (int i = 0; i < msg.size(); i++) {
			msg.set(i, msg.get(i).replace("%" + var + "%", val));
		}
		return this;
	}

	/**
	 * Pegar a mensagem como String.
	 *
	 * @return A mensagem
	 */
	public String getMsg () {
		String m = "";
		int i = 0;
		for (String s : msg) {
			if (i > 0) {
				m += "\n";
			}
			m += s;
			i++;
		}
		return m;
	}

	/**
	 * Pegar a mensagem como lista.
	 *
	 * @return A lista de partes da mensagem
	 */
	public List<String> getMsgList () {
		for (int i = 0; i < msg.size(); i++) {
			msg.set(i, msg.get(i).replaceAll("%([\\w\\d]+)%", ""));
		}
		return msg;
	}

	/**
	 * Adicionar um novo recebedor da mensagem.
	 *
	 * @param p o jogador recebedor
	 * @return O message
	 */
	public Message addRecipient (Player p) {
		recipients.add(p);
		return this;
	}

	/*-------------------*
	 *   TEXTCOMPONENT   *
	 *-------------------*/

	/**
	 * Pegar a mensagem como TextComponent.
	 *
	 * @return A mensagem
	 */
	public TextComponent getMsgSpigot () {
		TextComponent tx = new TextComponent("");
		for (TextComponent t : msgs) {
			tx.addExtra(t);
		}
		return tx;
	}

	/**
	 * Limpar a mensagem e todas as linhas dela.
	 *
	 * @return O message
	 */
	public Message clear () {
		recipients = new ArrayList<Player>();
		if (!str) {
			msgs.clear();
		} else {
			msg.clear();
		}
		return this;
	}

	/**
	 * Limpar os recebdores da mensagem.
	 *
	 * @return O message
	 */
	public Message clearRecipients () {
		recipients.clear();
		return this;
	}

	/**
	 * Adicionar uma nova parte à mensagem.
	 *
	 * @param text a nova parte
	 * @return O message
	 */
	public Message add (String text) {
		if (!str) {
			msgs.add(new TextComponent(text));
		} else {
			msg.add(text);
		}
		return this;
	}

	/**
	 * Adicionar evento de clique à última linha inserida.
	 *
	 * @param text o que vai ser executado ao clicar
	 * @param type o tipo de ação no clique
	 * @return O message
	 */
	public Message onClick (String text, Integer type) {
		TextComponent m = getLastMsg();
		if (m != null) {
			switch (type) {
				case 1: // Executar comando
					m.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + text));
					break;
				case 2: // Sugerir comando
					m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + text));
					break;
				case 3: // Abrir url
					m.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, text));
					break;
			}
		}
		return this;
	}

	/**
	 * Adicionar evento de hover à última linha inserida (hover com texto).
	 *
	 * @param text o texto que vai aparecer no hover
	 * @return O message
	 */
	public Message onHover (String text) {
		TextComponent m = getLastMsg();
		if (m != null) {
			m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
		}
		return this;
	}

	/**
	 * Adicionar evento de hover à última linha inserida (hover com item).
	 *
	 * @param item o item que vai aparecer no hover
	 * @return O message
	 */
	public Message onHover (ItemStack item) {
		TextComponent m = getLastMsg();
		if (m != null) {
			m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(getItemAsJson(item)).create()));
		}
		return this;
	}

	/**
	 * Adicionar evento de hover à última linha inserida (hover com conquista).
	 *
	 * @param ach o aquievment que vai aparecer no hover
	 * @return O message
	 */
	public Message onHover (Achievement ach) {
		TextComponent m = getLastMsg();
		if (m != null) {
			m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ComponentBuilder(ach.name()).create()));
		}
		return this;
	}

	/**
	 * Enviar a mensagem para um jogador.
	 *
	 * @param p o jgador a receber a mensgaem
	 */
	public void send (Player p) {
		if (str) {
			for (String l : getMsgList()) {
				p.sendMessage(l);
			}
			return;
		}
		p.spigot().sendMessage(getMsgSpigot());
	}

	/**
	 * Enviar a mensagem para o jogador (já armazenado).
	 */
	public void send () {
		for (Player p : recipients) {
			send(p);
		}
	}

	/**
	 * Pegar a última linha inseria na mensagem.
	 *
	 * @return A última mensagem
	 */
	private TextComponent getLastMsg () {
		return msgs.get(msgs.size() - 1);
	}

	/**
	 * Pegar um item como string JSON.
	 *
	 * @param item o item a ser transformado em JSON
	 * @return O item como json
	 */
	private String getItemAsJson (ItemStack item) {
		net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound c = new NBTTagCompound();
		c = nms.save(c);
		return c.toString();
	}
}
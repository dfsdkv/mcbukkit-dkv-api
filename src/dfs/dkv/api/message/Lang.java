/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.message;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dfs.dkv.api.DkvAPI;
import dfs.dkv.api.utils.ColorInfo;
import dfs.dkv.api.utils.ConfigFile;
import dfs.dkv.api.utils.Util;

/**
 * Class Lang.
 */
@SuppressWarnings("all")
public class Lang {
	/**
	 * Enum CooldownType.
	 */
	public static enum CooldownType {		
		/** O milisegundo. */
		MILISECOND("milisec"),

		/** Tipo segundo. */
		SECOND("second"),

		/** Tipo minuto. */
		MINUTE("minute"),

		/** Tipo hora. */
		HOUR("hour"),

		/** Tipo dia. */
		DAY("day"),

		/** Tipo mês. */
		MONTH("month"),

		/** Tipo ano. */
		YEAR("year");

		/** O tipo. */
		private String type = "milisec";
		
		/**
		 * Construtor de CooldownType.
		 *
		 * @param type o tipo de tempo de espera
		 */
		CooldownType (String type) {
			this.type = type;
		}

		/**
		 * Pegar o tipo de tempo de espera.
		 *
		 * @return O tipo
		 */
		public String getType () {
			return type;
		}
	};

	/** O plugin. */
	private JavaPlugin plugin;

	/** Os arquivos de idiomas. */
	private Map<String, ConfigFile> langs = new HashMap<>();

	/** O nome da pasta. */
	private String foldername = "";

	/**
	 * Construtor de Land.
	 *
	 * @param pl o plugin
	 */
	public Lang (JavaPlugin pl) {
		plugin = pl;
		foldername = pl.getName().toLowerCase();
		setup();
	}

	/**
	 * Setup.
	 */
	private void setup () {
		String fn = DkvAPI.getPlug().getDataFolder() + File.separator + "lang" + File.separator + foldername;
		File d = new File(fn);
		/*if (!d.exists()) { // Pasta /lang ainda não existe
			d.mkdirs();
		}*/
		if (d.list() != null && d.list().length > 0) {
			Pattern pt = Pattern.compile("^([a-z]{2})\\.yml$");
			for (File f : d.listFiles()) {
				Matcher m = pt.matcher(f.getName().toLowerCase());
				if (m.find()) {
					langs.put(m.group(1), new ConfigFile("lang" + File.separator + foldername + File.separator + m.group(1), DkvAPI.getPlug()));
				}
			}
		} else {
			langs.put(DkvAPI.conf.get("defaultlanguage"),
				new ConfigFile(
					"lang",
					"lang" + File.separator + foldername + File.separator + DkvAPI.conf.get("defaultlanguage"),
					plugin,
					DkvAPI.getPlug()
				)
			);
		}
	}

	/**
	 * Pegar o plugin.
	 *
	 * @return O plugin
	 */
	public JavaPlugin getPlugin () {
		return plugin;
	}

	/**
	 * Pegar os arquivos.
	 *
	 * @return Os arquivos
	 */
	public Map<String, ConfigFile> getFiles () {
		return langs;
	}

	/**
	 * Enviar uma mensagem para um jogador.
	 *
	 * @param key o caminho para a mensagem
	 * @param p o jogador a receber a mensagem
	 */
	public void send (String key, Player p) {
		ConfigFile l = getLang(p);
		if (l != null) {
			if (l.isSet(key)) {
				String text = getMsg(key, p);
				if (!text.isEmpty()) {
					p.sendMessage(text);
				}
			} else {
				p.sendMessage(ColorInfo.repColor(key));
			}
		}
	}

	/**
	 * Pegar uma mensagem do arquivo de mensagens.
	 *
	 * @param key o caminho para a mensagem no arquivo
	 * @param p o jogador (para pegar o idioma dele)
	 * @return A mensagem
	 */
	public String getMsg (String key, Player p) {
		ConfigFile l = getLang(p);
		if (l != null) {
			String text = l.get(key);
			if (text != null && !text.isEmpty()) {
				return text;
			}
		}
		return "";
	}

	/**
	 * Pegar uma lista de mensagens do arquivo de mensagens.
	 *
	 * @param key o caminho para a lista de mensagen no arquivo
	 * @param p o jogador (para pegar o idioma dele)
	 * @return A lista de mensagens
	 */
	public String[] getMsgList (String key, Player p) {
		ConfigFile l = getLang(p);
		if (l != null) {
			List<String> list = l.getListStr(key, false);
			if (list != null && !list.isEmpty()) {
				return list.toArray(new String[]{});
			}
		}
		return new String[]{};
	}

	/**
	 * Mensagem com ações.
	 *
	 * @return A nova instância do Message
	 */
	public Message msg () {
		return new Message();
	}

	/**
	 * Mensagens normais, sem ações
	 *
	 * @param m a mensagem
	 * @param lg o prefíxo do idioma da mensagem
	 * @return A nova instância do Message
	 */
	public Message msg (String m, String lg) {
		List<String> text = langs.get(lg).getListStr(m, false);
		if (text != null && !text.isEmpty()) {
			return new Message(text);
		}
		return new Message(langs.get(lg).get(m));
	}

	/**
	 * Mensagens normais (com player), sem ações
	 *
	 * @param m a mensagem
	 * @param p o jogador a receber a mensagem
	 * @return A nova instância do Message
	 */
	public Message msg (String m, Player p) {
		return msg(m, getPlayerCountryCode(p)).addRecipient(p);
	}

	/**
	 * Enviar o modo de uso de um comando.
	 *
	 * @param p o jogador a receber a mensagem
	 * @param cmd o comando que está incorreto
	 * @param us o modo de uso do comando
	 */
	public void sendCommandUsage (Player p, String cmd, String... us) {
		msg("commandusage", p)
			.repVar("cmd", getCommandUsage(cmd, us))
			.send();
	}

	/**
	 * Pegar o modo de uso de um comando.
	 *
	 * @param cmd o comando que está incorreto
	 * @param args o os argumentos que o comando precisa
	 * @return O modo de uso
	 */
	public String getCommandUsage (String cmd, String... args) {
		String u = cmd.trim();
		for (String a : args) {
			boolean r = true;
			if (a.substring(a.length() - 1).equals("+")) {
				r = false;
				a = a.substring(0, a.length() - 1);
			}
			u += " " + (r ? "<" : "[") +
				a.toLowerCase().trim().replace("\\s+", " ") +
				(r ? ">" : "]");
		}
		return u.trim();
	}

	/**
	 * Enviar uma mensagem de espera para usar um item.
	 *
	 * @param p o jogador a receber a mensagem
	 * @param t o o tipo de tempo de espera
	 * @param tm o tempo para usar o item
	 */
	public void itemCooldown (Player p, CooldownType t, String tm) {
		String tt = CooldownType.MILISECOND.getType();
		if (t != null) {
			tt = t.getType();
		}
		msg("general.itemcooldown." + tt, p)
			.repVar(tt + "s", tm)
			.send();
	}

	/**
	 * Pegar arquivo de idioma.
	 *
	 * @param p o jogador (para pegar o idioma dele)
	 * @return O arquivo de idioma
	 */
	private ConfigFile getLang (Player p) {
		ConfigFile c = null;
		if (p != null) {
			String lang = getPlayerCountryCode(p);
			if (langs.containsKey(lang)) {
				c = langs.get(lang);
			} else {
				c = langs.get(DkvAPI.conf.get("defaultlanguage"));
			}
		}
		return c;
	}

	/**
	 * Pegar o prefíxo do idioma atual do jogador.
	 *
	 * @param p o jogador
	 * @return O prefíxo do idioma do jogador (ou o padrão)
	 */
	public static String getPlayerCountryCode (Player p) {
		try {
			Object ep = Util.getMethod("getHandle", p.getClass()).invoke(p, null);
			Field f = Util.getField("locale", ep.getClass());
			String code = (String) f.get(ep);
			return code.trim().substring(0, 2).toLowerCase();
		} catch (Exception ex) {}
		return DkvAPI.conf.get("defaultlanguage");
	}
}
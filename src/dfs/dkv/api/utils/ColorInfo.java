/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.material.Wool;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("all")

/**
 * Class ColorInfo.
 */
public class ColorInfo {
	/**
	 * Pegar informações de um código de cor.
	 *
	 * @param c o código da cor
	 * @return As informações sobre a cor
	 */
	private static Map<String, Object> getColorInfo (String c) {
		int id = 0;
		String name = "unknow";
		String ptname = "Desconhecida";
		int dec = 0;
		Color col = null;
		Map<String, Object> data = new HashMap<String, Object>();
		if (c != null && !c.isEmpty()) {
			c = c.replace("&", "").replace("§", "").toLowerCase().trim();
			switch (c) {
				case "a":
					id = 5;
					name = "green";
					ptname = "Verde";
					dec = 5635925;
					break;
				case "b":
					id = 3;
					name = "aqua";
					ptname = "Ciano";
					dec = 5636095;
					break;
				case "c":
					id = 14;
					name = "red";
					ptname = "Vermelho";
					dec = 16733525;
					break;
				case "d":
					id = 6;
					name = "light_purple";
					ptname = "Rosa";
					dec = 16733695;
					break;
				case "e":
					id = 4;
					name = "yellow";
					ptname = "Amarelo";
					dec = 16777045;
					break;
				case "f":
					id = 0;
					name = "white";
					ptname = "Branco";
					dec = 16777215;
					break;
				case "0":
					id = 15;
					name = "black";
					ptname = "Preto";
					dec = 0;
					break;
				case "1":
					id = 11;
					name = "dark_blue";
					ptname = "Azul escuro";
					dec = 170;
					break;
				case "2":
					id = 13;
					name = "dark_green";
					ptname = "Verde escuro";
					dec = 43520;
					break;
				case "3":
					id = 9;
					name = "dark_aqua";
					ptname = "Ciano escuro";
					dec = 43690;
					break;
				case "4":
					id = 14;
					name = "dark_red";
					ptname = "Vermelho escuro";
					dec = 11141120;
					break;
				case "5":
					id = 10;
					name = "dark_purple";
					ptname = "Roxo";
					dec = 11141290;
					break;
				case "6":
					id = 1;
					name = "gold";
					ptname = "Laranja";
					dec = 16755200;
					break;
				case "7":
					id = 8;
					name = "gray";
					ptname = "Cinza";
					dec = 11184810;
					break;
				case "8":
					id = 7;
					name = "dark_gray";
					ptname = "Cinza escuro";
					dec = 5592405;
					break;
				case "9":
					id = 11;
					name = "blue";
					ptname = "Azul";
					dec = 5592575;
					break;
			}
		}
		Wool wool = new Wool(Material.WOOL, (byte) id);
		col = wool.getColor().getFireworkColor();
		data.put("colorcode", c);
		data.put("color", col);
		data.put("id", id);
		data.put("decimal", dec);
		data.put("name", name);
		data.put("displayname", ptname);
		return data;
	}

	/**
	 * Pegar o id da cor.
	 *
	 * @param c o código da cor
	 * @return O id da cor
	 */
	public static int getColorId (String c) {
		return (int) getColorInfo(c).get("id");
	}

	/**
	 * Pegar o obejeto Color de uma cor.
	 *
	 * @param c o código da cor
	 * @return O Color
	 */
	public static Color getColor (String c) {
		return (Color) getColorInfo(c).get("color");
	}

	/*---------------------*
	 *    Cores do Chat    *
	 *---------------------*/
	/**
	 * Substituir códigos de cores em textos.
	 *
	 * @param text o texto a ser transformado
	 * @return O novo texto com código de cores alternativoa
	 */
	public static String repColor (String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	/**
	 * Remover códigos de cores de um texto.
	 *
	 * @param text o texto a ser limpo
	 * @return O novo texto sem códigos de cores
	 */
	public static String removeColor (String text) {
		return ChatColor.stripColor(repColor(text));
	}
}
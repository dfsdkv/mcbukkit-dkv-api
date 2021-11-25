/**
 * @author Deivison (DKV_DFS)
 * @year 2019
 */
package dfs.dkv.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public class ConfigFile {
	private String filename = "";
	private File file;
	private YamlConfiguration config;
	private JavaPlugin plugin;
	public ConfigFile (String fln, String nfln, JavaPlugin mfrom, JavaPlugin mto) {
		plugin = mto;
		setup(mfrom, fln, nfln);
	}
	public ConfigFile (String fln, JavaPlugin main) {
		plugin = main;
		setup(main, fln, fln);
	}
	private void setup (JavaPlugin pl, String fn, String nfn) {
		filename = nfn;
		file = new File(plugin.getDataFolder() + File.separator + nfn + ".yml");
		config = YamlConfiguration.loadConfiguration(file); // Transforma o arquivo em um arquivo de configuração YML
		File fold = new File(file.getParent());
		if (!fold.exists()) {
			fold.mkdirs();
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
				InputStream inpt = pl.getResource(fn + ".yml");
				OutputStream o = new FileOutputStream(file);
				int l = 0;
				byte[] b = new byte[1024];
				while ((l = inpt.read(b)) > 0) {
					o.write(b, 0, l);
				}
				o.close();
				inpt.close();
			}
			config.load(file);
		} catch (Exception e) {}
	}
	/*
	 * Remover um valor
	 */
	public void remove (String path) {
		config.set(path, null);
		save();
	}
	/*
	 * Setar o valor de uma chave
	 */
	public void set (String path, Object value){
		config.set(path, value);
		save();
	}
	/*
	 * Setar o valor de uma chave tipo Location
	 */
	public void set (String path, Location value){
		config.set(path, getLocationStr(value));
		save();
	}
	/*
	 * Setar o valor de uma chave tipo Item
	 */
	public void set (String path, ItemStack value) {
		ItemMeta m = value.getItemMeta();
		config.set(path + ".item", getItemStr(value));
		if (m.hasEnchants()) {
			ArrayList<String> e = new ArrayList<>();
			for (Enchantment ee : m.getEnchants().keySet()) {
				e.add(ee.getName() + "|" + m.getEnchants().get(ee));
			}
			config.set(path + ".ench", e);
		}
		save();
	}
	/*
	 * Verificar se uma chave existe ou não
	 */
	public boolean isSet (String key) {
		return config.isSet(key);
	}
	/*
	 * Salvar a configuração para o arquivo
	 */
	public void save (){
		try {
			config.save(file);
		} catch (Exception e){}
	}
	/*
	 * Recarregar o arquivo de configurações
	 */
	public void reload () {
		try {
			config.load(file);
		} catch (Exception e) {}
		
	}
	
	/*
	 * Métodos
	 */
	public FileConfiguration getConfig (){
		return config;
	}
	
	public File getFile (){
		return file;
	}
	public String getFilename () {
		return filename;
	}

	public String get (String key, boolean nocolor) {
		if (config.getString(key) != null) {
			return config.getString(key);
		}
		return "";
	}
	
	public String get (String key) {
		return ChatColor.translateAlternateColorCodes('&', get(key, true));
	}
	
	public Integer getInt (String key) {
		return config.getInt(key);
	}
	
	public Double getDouble (String key) {
		return config.getDouble(key);
	}
	
	public Float getFloat (String key) {
		String f = get(key, true);
		if (!f.isEmpty()) {
			return Float.parseFloat(f);
		}
		return (float) 0;
	}
	
	public boolean getBool (String key) {
		return config.getBoolean(key);
	}
	
	public Location getLocation (String key) {
		return getParsedLocation(get(key, true));
	}
	
	public List<ItemStack> getItems (String key) {
		return getParsedItem(get(key + ".item", true), (List<String>) getList(key + ".ench"));
	}
	
	public ItemStack getItem (String key) {		
		List<ItemStack> i = getItems(key);
		return (i.size() > 0 ? i.get(0) : null);
	}
	
	public Set<String> getKeys (String key) {
		Set<String> keys = config.getConfigurationSection(key).getKeys(false);
		if (keys != null) {
			return keys;
		}
		return (Set<String>) new ArrayList();
	}

	public Set<String> getKeys () {
		Set<String> keys = config.getKeys(false);
		if (keys != null) {
			return keys;
		}
		return (Set<String>) new ArrayList();
	}

	public List<?> getList (String key) {
		if (config.getList(key) != null) {
			return config.getList(key);
		}
		return null;
	}

	public List<String> getListStr (String key, boolean nocolor) {
		if (config.getList(key) != null) {
			List<String> l = config.getStringList(key);
			List<String> list = new ArrayList<>();
			for (String str : l) {
				list.add(ChatColor.translateAlternateColorCodes('&', str));
			}
			return (nocolor ? l : list);
		}
		return null;
	}
	public static Location getParsedLocation (String c) {
		if (!c.isEmpty()) {
			String[] sp = c.split("\\|");
			if (sp.length == 6) { // WORLD|X|Y|Z|YAW|PITCH
				World w = Bukkit.getWorld(sp[0]);
				if (w != null) {
					return new Location(w,
							Double.parseDouble(sp[1]),
							Double.parseDouble(sp[2]),
							Double.parseDouble(sp[3]),
							Float.parseFloat(sp[4]),
							Float.parseFloat(sp[5])
					);
				}
			}
		}
		return null;
	}
	public static String getLocationStr (Location loc) {
		return loc.getWorld().getName() + "|" +
				loc.getX() + "|" +
				loc.getY() + "|" +
				loc.getZ() + "|" +
				loc.getYaw() + "|" +
				loc.getPitch();
	}
	public static List<ItemStack> getParsedItem (String c, List<String> e) {
		List<ItemStack> it = new ArrayList<>();
		if (!c.isEmpty()) {
			String[] s = c.split("\\|");
			ItemStack i = null;
			if (s.length > 0) {
				switch (s.length) {
					case 1: // ITEM
						it.add(new ItemStack(Material.getMaterial(s[0].toUpperCase())));
						break;
					case 2: // ITEM|DATA VALUE
						it.add(new ItemStack(Material.getMaterial(s[0].toUpperCase()), 1, (byte) Integer.parseInt(s[1])));
						break;
					case 3: // ITEM|DATA VALUE|QUANTIDADE
						i = new ItemStack(Material.getMaterial(s[0].toUpperCase()), 1, (byte) Integer.parseInt(s[1]));
						i.setAmount(Integer.parseInt(s[2]));
						it.add(i);
						break;
				}
				if (s.length > 3) { // ITEM|DATA VALUE|QUANTIDADE|STACKAVEL
					if (s[3].equals("0")) { // "false" (item não stackavel)
						for (int q = 0; q < Integer.parseInt(s[2]); q++) {
							i = new ItemStack(Material.getMaterial(s[0]), 1, (byte) Integer.parseInt(s[1]));
							i.setAmount(1);
							it.add(i);
						}
					} else {
						i = new ItemStack(Material.matchMaterial(s[0]), 1, (byte) Integer.parseInt(s[1]));
						i.setAmount(Integer.parseInt(s[2]));
						it.add(i);
					}
				}
				if (s.length > 4) { // ITEM|DATA VALUE|QUANTIDADE|STACKAVEL|NOME DE EXIBIÇÃO
					for (ItemStack ii : it) {
						ItemMeta iim = ii.getItemMeta();
						iim.setDisplayName(ChatColor.translateAlternateColorCodes('&', s[4]));
						ii.setItemMeta(iim);
					}
				}
				if (s.length > 5) { // ITEM|DATA VALUE|QUANTIDADE|STACKAVEL|NOME DE EXIBIÇÃO|DESCRIÇÃO
					for (ItemStack ii : it) {
						ItemMeta iim = ii.getItemMeta();
						List<String> l = new ArrayList<String>();
						for (int ll = 5; ll < s.length; ll++) {
							l.add(ChatColor.translateAlternateColorCodes('&', s[ll]));
						}
						iim.setLore(l);
						ii.setItemMeta(iim);
					}
				}
			}
			if (e != null) {
				for (String se : e) {
					String[] sen = se.split("\\|");
					if (sen.length >= 2) { // ENCANTAMENTO|NIVEL
						for (ItemStack ii : it) {
							ItemMeta iim = ii.getItemMeta();
							iim.addEnchant(Enchantment.getByName(sen[0].toUpperCase()), Integer.parseInt(sen[1]), true);
							if (sen.length >= 3) { // ENCANTAMENTO|NIVEL|ATRIBUTOS
								for (int in = 2; in < sen.length; in++) {
									iim.addItemFlags(ItemFlag.valueOf(sen[in]));
								}
							}
							ii.setItemMeta(iim);
						}
					}
				}
			}
		}
		return it;
	}
	public static String getItemStr (ItemStack item) {
		/*
		 * ITEM|DATA VALUE|QUANTIDADE|STACKAVEL|NOME DE EXIBIÇÂO|DESCRIÇÃO
		 */
		String v = item.getType() + "|" + item.getData().getData() + "|" + item.getAmount() + "|";
		if (item.getMaxStackSize() == 1) { // Não pode ser stackado
			v += "0";
		} else {
			v += "1";
		}
		ItemMeta m = item.getItemMeta();
		if (m.hasDisplayName()) {
			v += "|" + m.getDisplayName();
		}
		if (m.hasLore()) {
			for (String l : m.getLore()) {
				v += "|" + l;
			}
		}
		return v;
	}
}
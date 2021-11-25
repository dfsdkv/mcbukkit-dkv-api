package dfs.dkv.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater {
	private JavaPlugin plugin;
	private String filename;
	private String url;
	private String oldversion;
	private String currentversion;
	private boolean needupdate;
//	private int taskid;

	public Updater (JavaPlugin pl) {
		plugin = pl;
		filename = pl.getName() + ".jar";
		url = "https://framemc.000webhostapp.com/p/" + plugin.getName().toLowerCase() + "/";
		oldversion = pl.getDescription().getVersion();
		/*Bukkit.getScheduler().runTaskLater(plugin, new Runnable () {
			@Override
			public void run() {
				checkForUpdate();
			}
		}, 20L);*/
	}
	public void download () {
		if (!needupdate) {
			return;
		}
		Bukkit.getConsoleSender().sendMessage("§2Atualizando \"" + plugin.getName() + "§r§2\"...");
		try {
			URL url = new URL(this.url + filename);
			InputStream in = url.openStream();
			Files.copy(in, Paths.get(plugin.getDataFolder().getParent() + File.separator + filename), StandardCopyOption.REPLACE_EXISTING);
			in.close();
			Bukkit.getConsoleSender().sendMessage("§a\"" + plugin + "\" foi atualizado da versão §fv" + oldversion + " §r§apara §fv" + currentversion + "§r§a!");
			oldversion = currentversion;
			currentversion = "";
			needupdate = false;
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§cFalha ao atualizar o plugin \"" + plugin + "\"!");
		}
	}
	public boolean isUpdateAvaiable () {
		return needupdate;
	}
	public Updater checkForUpdate () {
		try {
			URL u = new URL(url + "version.txt");
			InputStream in = u.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String v;
			if ((v = br.readLine()) != null) {
				if (!v.equals(oldversion)) {
					currentversion = v;
					needupdate = true;
					Bukkit.getConsoleSender().sendMessage("§6Atualização para o plugin \"" + plugin + "\" encontrada v" + v);
					download();
				}
			}
			in.close();
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§cFalha ao verificar por atualizações do plugin \"" + plugin + "\"! " + e.getMessage());
		}
		return this;
	}
}
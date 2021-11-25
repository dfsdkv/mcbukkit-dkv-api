/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api;

import org.bukkit.plugin.java.JavaPlugin;

import dfs.dkv.api.inventory.InventoryListener;
import dfs.dkv.api.message.Lang;
import dfs.dkv.api.utils.ConfigFile;
import dfs.dkv.api.utils.DB;
import dfs.dkv.api.utils.Updater;

public class DkvAPI extends JavaPlugin {
	private static DkvAPI instance;
	public static ConfigFile conf;
	public static Lang lang;
	public static DB hologramsdb;
	public static Updater updater;
	public void onEnable () {
		instance = this;
//		updater = new Updater(this);
		setupFiles();
		setupDBs();
		registerEvents();
	}
	public void onDisable () {
		closeDBs();
	}
	
	
	
	private void setupDBs () {
//		hologramsdb = new DB("");
	}
	private void closeDBs () {
//		hologramsdb.closeCon();
	}
	private void setupFiles () {
		conf = new ConfigFile("config", this);
		lang = new Lang(this);
	}
	private void registerEvents () {
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
	}



	public static DkvAPI getPlug () {
		return instance;
	}
}
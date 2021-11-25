package dfs.dkv.api.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dfs.dkv.api.utils.ColorInfo;
import dfs.dkv.api.utils.ConfigFile;

public class Item {
	protected ItemStack item;
	private ItemMeta meta;
	
	public Item (ItemStack i) {
		item = i;
		meta = item.getItemMeta();
	}
	public Item (String itstr) {
		this(itstr, new ArrayList<String>());
	}
	public Item (String itstr, List<String> e) {
		List<ItemStack> i = ConfigFile.getParsedItem(itstr, e);
		if (i.size() > 0 && i.get(0) != null) {
			item = i.get(0);
			meta = item.getItemMeta();
		}
	}
	public Item (String it, String dt) {
		this(it + "|" + dt);
	}
	
	public Item setName (String text) {
		if (meta != null) {
			meta.setDisplayName(ColorInfo.repColor(text));
			updateMeta();
		}
		return this;
	}
	public Item setLore (List<String> lore) {
		if (meta != null) {
			for (int i = 0; i < lore.size(); i++) {
				lore.set(i, ColorInfo.repColor(lore.get(i)));
			}
			meta.setLore(lore);
			updateMeta();
		}
		return this;
	}
	public Item setLore (String[] lore) {
		List<String> l = new ArrayList<>();
		for (String ll : lore) {
			l.add(ll);
		}
		setLore(l);
		return this;
	}
	public Item setLore (String lore) {
		setLore(lore.split("\\|"));
		return this;
	}
	public Item setMeta (ItemMeta m) {
		if (item != null && m != null) {
			item.setItemMeta(m);
		}
		return this;
	}
	public Item setItem (ItemStack it) {
		item = it;
		updateMeta();
		return this;
	}



	public ItemMeta getMeta () {
		return meta;
	}
	public ItemStack getItem () {
		return item;
	}


	private void updateMeta () {
		setMeta(meta);
	}
}
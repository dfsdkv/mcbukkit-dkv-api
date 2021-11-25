package dfs.dkv.api.inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryDataStorage {
	private static List<InventoryMenu> invs = new ArrayList<>();
	public static List<InventoryMenu> getAll () {
		List<InventoryMenu> l = new ArrayList<>();
		try {
			for (InventoryMenu in : invs) {
				l.add(in);
			}
		} catch (Exception e) {}
		return l;
	}
	public static void add (InventoryMenu inv) {
		if (exists(inv)) { return; }
		invs.add(inv);
	}
	public static void remove (InventoryMenu inv) {
		if (!exists(inv)) { return; }
		invs.remove(inv);
	}
	public static boolean exists (InventoryMenu inv) {
		return (invs.contains(inv) == true);
	}
}
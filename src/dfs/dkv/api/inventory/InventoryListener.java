package dfs.dkv.api.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
	@EventHandler
	/*
	 * Ao jogador clicar em um item de um inventário
	 */
	public void onClick (InventoryClickEvent e) {
		for (InventoryMenu i : InventoryDataStorage.getAll()) {
			Inventory einv = e.getView().getTopInventory();
			Inventory inv = i.getInventory();
			HumanEntity clicker = e.getWhoClicked();
			Player p = null;
			if (clicker instanceof Player) {
				p = (Player) clicker;
			}
			if (inv.equals(einv)) {
				ItemStack item = e.getCurrentItem();
				ItemStack inext = i.getItemNextPage();
				ItemStack iprev = i.getItemPrevPage();
				if (i.getActions() != null) {
					i.getActions().onClick(p, i, item, e.getAction(), e);
				}
				if (item != null) {
					e.setCancelled(true);
					if (inext != null && item.equals(inext)) {
						i.nextPage();
					}
					if (iprev != null && item.equals(iprev)) {
						i.prevPage();
					}
				}
			}
		}
	}
	@EventHandler
	/*
	 * Ao jogador fechar um inventário
	 */
	public void onClose (InventoryCloseEvent e) {
		List<InventoryMenu> todestroy = new ArrayList<>();
		for (InventoryMenu i : InventoryDataStorage.getAll()) {
			Inventory einv = e.getView().getTopInventory();
			Inventory inv = i.getInventory();
			HumanEntity human = e.getPlayer();
			Player p = null;
			if (human instanceof Player) {
				p = (Player) human;
			}
			if (inv.equals(einv)) {
				todestroy.add(i);
				if (i.getActions() != null) {
					i.getActions().onClose(p, i, e);
				}
			}
		}
		for (InventoryMenu i : todestroy) {
			i.destroy();
		}
		todestroy = null;
	}
	@EventHandler
	/*
	 * Ao jogador abrir um inventário
	 */
	public void onOpen (InventoryOpenEvent e) {
		for (InventoryMenu i : InventoryDataStorage.getAll()) {
			Inventory einv = e.getView().getTopInventory();
			Inventory inv = i.getInventory();
			HumanEntity human = e.getPlayer();
			Player p = null;
			if (human instanceof Player) {
				p = (Player) human;
			}
			if (inv.equals(einv)) {
				if (i.getActions() != null) {
					i.getActions().onOpen(p, i, e);
				}
			}
		}
	}
	@EventHandler
	/*
	 * Quando qualquer evento relacionado a inventário for chamado
	 */
	public void onInventory (InventoryEvent e) {
		for (InventoryMenu i : InventoryDataStorage.getAll()) {
			Inventory einv = e.getView().getTopInventory();
			Inventory inv = i.getInventory();
			if (inv.equals(einv)) {
				if (i.getActions() != null) {
					i.getActions().onInventory(i, e);
				}
			}
		}
	}
}
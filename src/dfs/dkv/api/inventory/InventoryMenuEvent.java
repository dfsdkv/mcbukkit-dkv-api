package dfs.dkv.api.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public interface InventoryMenuEvent {
	void onClick (Player player, InventoryMenu inventory, ItemStack item, InventoryAction action, InventoryClickEvent event);
	void onClose (Player player, InventoryMenu inventory, InventoryCloseEvent event);
	void onOpen (Player player, InventoryMenu inventory, InventoryOpenEvent event);
	void onInventory (InventoryMenu inventory, InventoryEvent event);
}
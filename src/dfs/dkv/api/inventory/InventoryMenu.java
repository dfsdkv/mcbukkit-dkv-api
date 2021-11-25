/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Class InventoryMenu.
 */
public class InventoryMenu {	
	/** Os items. */
	private List<ItemStack[]> items = new ArrayList<>();

	/** O invent�rio. */
	private Inventory inv;

	/** A p�gina atual. */
	private int page = 1;

	/** O n�mero m�ximo de p�ginas. */
	private int maxpages = 1;

	/** O n�mero de itens por p�gina. */
	private int size;

	/** O t�tulo do invent�rio. */
	private String title;

	/** O item clic�vel para ir pra pr�xima p�gina. */
	private ItemStack itemnext;

	/** A slot do item de pr�xima p�gina. */
	private int slotitemnext;

	/** O item clic�vel para ir pra p�gina anterior. */
	private ItemStack itemprev;

	/** A slot do item de p�gina anterior. */
	private int slotitemprev;

	/** As a��es do menu. */
	private InventoryMenuEvent actions;

	/** Setar o n�mero m�ximo de p�ginas automaticamente?. */
	private boolean automaxpages = true;

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o t�tulo do menu
	 * @param size o n�mero de linhas do menu
	 * @param ev as a��es registradas pelo menu
	 */
	public InventoryMenu (String name, int size, InventoryMenuEvent ev) {
		setActions(ev);
		init(name, size);
	}

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o t�tulo do menu
	 * @param size o n�mero de linhas do menu
	 */
	public InventoryMenu (String name, int size) {
		init(name, size);
	}

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o t�tulo do menu
	 * @param size o n�mero de linhas do menu
	 * @param mx o n�mero m�ximo de p�ginas
	 */
	public InventoryMenu (String name, int size, int mx) {
		setMaxPages(mx);
		init(name, size);
	}

	/**
	 * Iniciar a classe.
	 *
	 * @param name o t�tulo do menu
	 * @param size o n�mero de linhas do menu
	 */
	private void init (String tit, int sz) {
		size = (sz > 2 ? (sz - 2) * 7 : 7);
		title = tit;
		inv = Bukkit.createInventory(null, sz * 9, title);
		for (int i = 1; i <= maxpages; i++) {
			items.add(new ItemStack[size]);
		}
		update();
		InventoryDataStorage.add(this);
	}

	/**
	 * Pegar o n�mero da p�gina atual.
	 *
	 * @return O n�mero da p�gina
	 */
	public int getPage () {
		return page;
	}

	/**
	 * Pegar o item de pr�xima p�gina.
	 *
	 * @return O item de pr�xima p�gina
	 */
	public ItemStack getItemNextPage () {
		return itemnext;
	}

	/**
	 * Pegar o item de p�gina anterior.
	 *
	 * @return O item de p�gina anterior
	 */
	public ItemStack getItemPrevPage () {
		return itemprev;
	}

	/**
	 * Pegar o invent�rio.
	 *
	 * @return O invent�rio
	 */
	public Inventory getInventory () {
		return inv;
	}

	/**
	 * Pegar os itens de uma p�gina.
	 *
	 * @param pg o n�mero da p�gina
	 * @return Os itens da p�gina
	 */
	public ItemStack[] getPageItems (int pg) {
		if (items.size() >= pg) {
			return items.get(pg - 1);
		}
		return null;
	}

	/**
	 * Pegar as a��es do menu.
	 *
	 * @return As a��es
	 */
	public InventoryMenuEvent getActions () {
		return actions;
	}

	/**
	 * Setar o item de pr�xima p�gina.
	 *
	 * @param item o novo item de pr�xima p�gina
	 * @param slot a slot que ele vai ficar
	 * @return O InventoryMenu
	 */
	public InventoryMenu setNextItem (ItemStack item, Integer slot) {
		itemnext = item;
		slotitemnext = slot;
		return this;
	}

	/**
	 * Setar o item de p�gina anterior.
	 *
	 * @param item o novo item de p�gina anterior
	 * @param slot a slot que ele vai ficar
	 * @return O InventoryMenu
	 */
	public InventoryMenu setPrevItem (ItemStack item, Integer slot) {
		itemprev = item;
		slotitemprev = slot;
		return this;
	}

	/**
	 * Setar o n�mero m�ximo de p�ginas.
	 *
	 * @param mx o novo n�mero de p�ginas
	 * @return O InventoryMenu
	 */
	public InventoryMenu setMaxPages (int mx) {
		if (mx >= 1) {
			automaxpages = false;
			maxpages = mx;
		}
		return this;
	}

	/**
	 * Setar os itens de uma p�gina.
	 *
	 * @param pg o n�mero da p�gina
	 * @param its os itens
	 * @return O InventoryMenu
	 */
	public InventoryMenu setPageItems (int pg, ItemStack[] its) {
		if (items.size() >= pg) {
			items.set(pg - 1, its);
			update();
		}
		return this;
	}

	/**
	 * Setar as a��es/eventos do menu.
	 *
	 * @param ac as novas a��es
	 * @return O InventoryMenu
	 */
	public InventoryMenu setActions (InventoryMenuEvent ac) {
		actions = ac;
		return this;
	}

	/**
	 * Adicionar um item em uma p�gina espec�fica.
	 *
	 * @param it o item a ser adicionado
	 * @param p o n�mero da p�gina
	 * @return O InventoryMenu
	 */
	public InventoryMenu addItem (ItemStack it, int p) {
		checkPages();
		ItemStack[] pit = getPageItems(p);
		if (pit != null) {
			for (int i = 0; i < pit.length; i++) {
				if (pit[i] == null) {
					pit[i] = it;
					break;
				}
			}
			setPageItems(p, pit);
		}
		return this;
	}

	/**
	 * Adicionar um item sem uma slot e p�gina espec�fica..
	 *
	 * @param it o item a ser adicionado
	 * @param sl as slot que o item deve ficar
	 * @param p o n�mero da p�gina
	 * @return O InventoryMenu
	 */
	public InventoryMenu addItem (ItemStack it, int sl, int p) {
		checkPages();
		ItemStack[] pit = getPageItems(p);
		if (pit != null) {
			pit[sl] = it;
			setPageItems(p, pit);
		}
		return this;
	}

	/**
	 * Adicionar um item na primeira p�gina dispon�vel.
	 *
	 * @param it o item a ser adicionado
	 * @return O InventoryMenu
	 */
	public InventoryMenu addItem (ItemStack it) {
		checkPages();
		int j = 0;
		for (ItemStack[] its : items) {
			j++;
			for (int i = 0; i < its.length; i++) {
				if (its[i] == null) {
					its[i] = it;
					setPageItems(j, its);
					return this;
				}
			}
		}
		return this;
	}

	/**
	 * Ir para a pr�xima p�gina.
	 *
	 * @return O InventoryMenu
	 */
	public InventoryMenu nextPage () {
		page++;
		if (page > maxpages) {
			page = maxpages;
		}
		return this;
	}

	/**
	 * Ir para a p�gina anterior.
	 *
	 * @return O InventoryMenu
	 */
	public InventoryMenu prevPage () {
		page--;
		if (page < 1) {
			page = 1;
		}
		return this;
	}

	/**
	 * Abrir o menu para um jogador.
	 *
	 * @param p o jogador que vai abrir o menu
	 * @return O InventoryMenu
	 */
	public InventoryMenu open (Player p) {
		if (p != null) {
			p.openInventory(inv);
		}
		return this;
	}

	/**
	 * Atualizar o invent�rio.
	 *
	 * @return O InventoryMenu
	 */
	public InventoryMenu update () {
		inv.clear();
		if (page > 1 && itemprev != null) {
			inv.setItem(slotitemprev, itemprev);
		}
		if (page < maxpages && itemnext != null) {
			inv.setItem(slotitemnext, itemnext);
		}
		ItemStack[] its = getPageItems(page);
		if (its != null) {
			int x = 10;
			int y = 0;
			for (ItemStack it : its) {
				if (it != null) {
					inv.setItem(x, it);
				}
				x++;
				y++;
				if (y == 7) {
					x += 2;
					y = 0;
				}
			}
		}
		for (HumanEntity v : inv.getViewers()) {
			if (v instanceof Player) {
				((Player) v).updateInventory();
			}
		}
		return this;
	}

	/**
	 * Destruir o menu.
	 */
	public void destroy () {
		InventoryDataStorage.remove(this);
	}
	
	/**
	 * Checagem para adi��o de nova p�gina caso "automaxpages" estiver habilitado.
	 */
	private void checkPages () {
		if (automaxpages) {
			int ck = 0;
			for (ItemStack[] its : items) {
				int j = 0;
				for (int i = 0; i < its.length; i++) {
					if (its[i] != null) {
						j++;
					}
				}
				if (j == size) {
					ck++;
				}
			}
			if (ck >= size * maxpages) {
				maxpages++;
				items.add(new ItemStack[size]);
			}
		}
	}
}
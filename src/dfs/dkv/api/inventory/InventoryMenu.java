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

	/** O inventário. */
	private Inventory inv;

	/** A página atual. */
	private int page = 1;

	/** O número máximo de páginas. */
	private int maxpages = 1;

	/** O número de itens por página. */
	private int size;

	/** O título do inventário. */
	private String title;

	/** O item clicável para ir pra próxima página. */
	private ItemStack itemnext;

	/** A slot do item de próxima página. */
	private int slotitemnext;

	/** O item clicável para ir pra página anterior. */
	private ItemStack itemprev;

	/** A slot do item de página anterior. */
	private int slotitemprev;

	/** As ações do menu. */
	private InventoryMenuEvent actions;

	/** Setar o número máximo de páginas automaticamente?. */
	private boolean automaxpages = true;

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o título do menu
	 * @param size o número de linhas do menu
	 * @param ev as ações registradas pelo menu
	 */
	public InventoryMenu (String name, int size, InventoryMenuEvent ev) {
		setActions(ev);
		init(name, size);
	}

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o título do menu
	 * @param size o número de linhas do menu
	 */
	public InventoryMenu (String name, int size) {
		init(name, size);
	}

	/**
	 * Construtor de InventoryMenu.
	 *
	 * @param name o título do menu
	 * @param size o número de linhas do menu
	 * @param mx o número máximo de páginas
	 */
	public InventoryMenu (String name, int size, int mx) {
		setMaxPages(mx);
		init(name, size);
	}

	/**
	 * Iniciar a classe.
	 *
	 * @param name o título do menu
	 * @param size o número de linhas do menu
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
	 * Pegar o número da página atual.
	 *
	 * @return O número da página
	 */
	public int getPage () {
		return page;
	}

	/**
	 * Pegar o item de próxima página.
	 *
	 * @return O item de próxima página
	 */
	public ItemStack getItemNextPage () {
		return itemnext;
	}

	/**
	 * Pegar o item de página anterior.
	 *
	 * @return O item de página anterior
	 */
	public ItemStack getItemPrevPage () {
		return itemprev;
	}

	/**
	 * Pegar o inventário.
	 *
	 * @return O inventário
	 */
	public Inventory getInventory () {
		return inv;
	}

	/**
	 * Pegar os itens de uma página.
	 *
	 * @param pg o número da página
	 * @return Os itens da página
	 */
	public ItemStack[] getPageItems (int pg) {
		if (items.size() >= pg) {
			return items.get(pg - 1);
		}
		return null;
	}

	/**
	 * Pegar as ações do menu.
	 *
	 * @return As ações
	 */
	public InventoryMenuEvent getActions () {
		return actions;
	}

	/**
	 * Setar o item de próxima página.
	 *
	 * @param item o novo item de próxima página
	 * @param slot a slot que ele vai ficar
	 * @return O InventoryMenu
	 */
	public InventoryMenu setNextItem (ItemStack item, Integer slot) {
		itemnext = item;
		slotitemnext = slot;
		return this;
	}

	/**
	 * Setar o item de página anterior.
	 *
	 * @param item o novo item de página anterior
	 * @param slot a slot que ele vai ficar
	 * @return O InventoryMenu
	 */
	public InventoryMenu setPrevItem (ItemStack item, Integer slot) {
		itemprev = item;
		slotitemprev = slot;
		return this;
	}

	/**
	 * Setar o número máximo de páginas.
	 *
	 * @param mx o novo número de páginas
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
	 * Setar os itens de uma página.
	 *
	 * @param pg o número da página
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
	 * Setar as ações/eventos do menu.
	 *
	 * @param ac as novas ações
	 * @return O InventoryMenu
	 */
	public InventoryMenu setActions (InventoryMenuEvent ac) {
		actions = ac;
		return this;
	}

	/**
	 * Adicionar um item em uma página específica.
	 *
	 * @param it o item a ser adicionado
	 * @param p o número da página
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
	 * Adicionar um item sem uma slot e página específica..
	 *
	 * @param it o item a ser adicionado
	 * @param sl as slot que o item deve ficar
	 * @param p o número da página
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
	 * Adicionar um item na primeira página disponível.
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
	 * Ir para a próxima página.
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
	 * Ir para a página anterior.
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
	 * Atualizar o inventário.
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
	 * Checagem para adição de nova página caso "automaxpages" estiver habilitado.
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
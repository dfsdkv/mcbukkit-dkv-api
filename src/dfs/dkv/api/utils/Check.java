/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import dfs.dkv.api.DkvAPI;

@SuppressWarnings("all")

/**
 * Class Check.
 */
public class Check {
	/**
	 * Checar o n�mero de argumentos de um comando.
	 *
	 * @param p o jogador a receber a mensagem
	 * @param cmd o nome do comando executado
	 * @param args os argumentos do comando
	 * @param length o tamanho da lista de argumentos
	 * @param equals o tamanaho da lista precisa ser igual (true) ou pode ser maior (false)?
	 * @param usage os argumentos necess�rios para o comando ser v�lido 
	 * @return true, se obtiver sucesso
	 */
	public static boolean arguments (Player p, String cmd, String[] args, int length, boolean equals, String... usage) {
		boolean c = false;
		int l = args.length;
		if (equals && l == length) {
			c = true;
		} else if (!equals && l >= length) {
			c = true;
		}
		if (!c) { // N�o est� correto
			if (p != null) {
				DkvAPI.lang.sendCommandUsage(p, cmd, usage);
			}
			return false;
		}
		return true;
	}

	/**
	 * Checar a exist�ncia de um jogador no servidor.
	 *
	 * @param p o jogador a receber a mensagem
	 * @param opn o nome do jogador a ser checado
	 * @return O OfflinePlayer
	 */
	public static OfflinePlayer isPlayer (Player p, String opn) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(opn.trim());
		if (op != null && (op.hasPlayedBefore() || p.isOnline())) {
			return op;
		}
		DkvAPI.lang.msg("noplayermatch", p)
			.repVar("nick", opn)
			.send();
		return null;
	}

	/**
	 * Verificar se um jogador possui certa permiss�o.
	 *
	 * @param perm o perm
	 * @param p o p
	 * @return true, se obtiver sucesso
	 */
	public static boolean hasPermission (String perm, Player p) {
		if (p.hasPermission(perm)) {
			return true;
		}
		DkvAPI.lang.msg("nopermission", p)
			.send();
		/*if (p.hasPermission(perm)) {
			return true;
		}
		List<GroupPerm> l = new ArrayList<>();
		// Adicionar os grupos por ordem de �ndice
		for (GroupPerm g : GroupPerm.getAll()) {
			if (g.hasPermission(perm) && !l.contains(g)) {
				l.add(g);
			}
		}
		GroupPerm group = null;
		// Pegar o �ltimo grupo da lista (no caso, ser� o menor grupo que cont�m tal permiss�o)
		if (!l.isEmpty()) {
			group = l.get(l.size() - 1);
		}
		String mkey = "nopermission";
		if (group != null) {
			mkey = "nopermissiongroup";
		}
		DkvAPI.lang.msg(mkey, p)
			.repVar("group", (group != null ? ColorInfo.removeColor(group.getName()) : ""))
			.send();
		(new Message(msg))
			.repVar("group", (group != null ? Helper.removeColor(group.getName()) : ""))
			.send(p);*/
		return false;
	}

	/*--------------------*
	 *    Localiza��es    *
	 *--------------------*/
	/**
	 * Pegar blocos ao redor de um centro.
	 *
	 * @param loc a localiza��o central
	 * @param r o raio de dist�ncia do centro
	 * @param checkY checar blocos no raio da altura tamb�m (true) ou n�o (false)?
	 * @return A lista de blocos
	 */
	public static List<Block> getBlocksAround (Location loc, int r, boolean checkY) {
		Block b = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).getBlock();
		List<Block> blks = new ArrayList<>();
		r += 1;
		if (checkY) {
			for (int x = b.getX() - r; x <= b.getX() + r; x++) { // X
				for (int y = b.getY() - r; y <= b.getY() + r; y++) { // Y
					for (int z = b.getZ() - r; z <= b.getZ() + r; z++) { // Z
						blks.add(loc.getWorld().getBlockAt(x, y, z));
					}
				}
			}
		} else {
			for (int x = b.getX() - r; x <= b.getX() + r; x++) { // X
				for (int z = b.getZ() - r; z <= b.getZ() + r; z++) { // Z
					blks.add(loc.getWorld().getBlockAt(x, b.getY(), z));
				}
			}
		}
		return blks;
	}

	/**
	 * Pegar blocos ao redor de cada raio de um centro de X, Y e Z.
	 *
	 * @param loc a localiza��o central
	 * @param rx o raio de dist�ncia da posi��o X
	 * @param rz o raio de dist�ncia da posi��o Z
	 * @param ry o raio de dist�ncia da posi��o Y
	 * @return A lista de blocos
	 */
	public static List<Block> getBlocksAroundXYZ (Location loc, int rx, int rz, int ry) {
		Block b = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).getBlock();
		List<Block> blks = new ArrayList<>();
		if (rx > 0) {
			for (int x = b.getX() - rx; x <= b.getX() + rx; x++) { // X
				Block bb = loc.getWorld().getBlockAt(x, b.getY(), b.getZ());
				if (!blks.contains(bb)) {
					blks.add(bb);
				}
			}
		}
		if (rz > 0) {
			for (int z = b.getZ() - rz; z <= b.getZ() + rz; z++) { // Z
				Block bb = loc.getWorld().getBlockAt(b.getX(), b.getY(), z);
				if (!blks.contains(bb)) {
					blks.add(bb);
				}
			}
		}
		if (ry > 0) {
			for (int y = b.getY() - ry; y <= b.getY() + ry; y++) { // Y
				Block bb = loc.getWorld().getBlockAt(b.getX(), y, b.getZ());
				if (!blks.contains(bb)) {
					blks.add(bb);
				}
			}
		}
		return blks;
	}

	/**
	 * Subistituir um bloco pelo outro at� uma certa dist�ncia.
	 *
	 * @param fin o bloco a ser substituido
	 * @param rep o bloco de substitui��o
	 * @param loc a localiza��o central
	 * @param r o raio de dist�ncia
	 */
	public static void replaceBlockNearest (Block fin, Block rep, Location loc, int r) {
		Block b = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()).getBlock();
		r += 1;
		for (int x = b.getX() - r; x <= b.getX() + r; x++) { // X
			for (int y = b.getY() - r; y <= b.getY() + r; y++) { // Y
				for (int z = b.getZ() - r; z <= b.getZ() + r; z++) { // Z
					Block blk = loc.getWorld().getBlockAt(x, y, z);
					if (blk.getType().equals(fin.getType()) && blk.getData() == fin.getData()) {
						blk.setType(rep.getType());
						blk.setData(rep.getData());
					}
				}
			}
		}
	}

	/**
	 * Verificar se uma localiza��o est� pr�xima a outra.
	 *
	 * @param loc a localiza��o da posi��o 1
	 * @param loc2 a localiza��o da posi��o 2
	 * @param r o raido de dist�ncia
	 * @return true, se estiver pr�ximo
	 */
	public static boolean isNearest (Location loc, Location loc2, double r) {
		return (loc2.distance(loc) < r);
	}

	/**
	 * Pegar os blocos de uma determinada �rea.
	 *
	 * @param pos1 a localiza��o da posi��o 1
	 * @param pos2 a localiza��o da posi��o 2
	 * @return Os blocos dentro da sele��o
	 */
	public static List<BlockState> getBlocksInSelection (Location pos1, Location pos2) {
		List<BlockState> bs = new ArrayList<BlockState>();
		if (pos1 != null && pos2 != null && pos1.getWorld().equals(pos2.getWorld())) { // Mesmo mundo
			Map<String, Location> pos = getPosition(pos1.getBlock().getLocation(), pos2.getBlock().getLocation());
			pos1 = pos.get("pos1");
			pos2 = pos.get("pos2");
			for (int x = (int) pos1.getX(); x <= pos2.getX(); x++) {
				for (int y = (int) pos1.getY(); y <= pos2.getY(); y++) {
					for (int z = (int) pos1.getZ(); z <= pos2.getZ(); z++) {
						Block blk = pos1.getWorld().getBlockAt(x, y, z);
						bs.add(blk.getState());
					}
				}
			}
		}
		return bs;
	}

	/**
	 * Checar se uma localiza��o est� dentro de uma determinada �rea
	 *
	 * @param loc a localiza��o a ser checada
	 * @param pos1 a localiza��o da posi��o 1
	 * @param pos2 a localiza��o da posi��o 2
	 * @param checkY verificar o raio de altura (true) ou n�o (false)?
	 * @return true, se estiver dentro
	 */
	public static boolean isInSelection (Location loc, Location pos1, Location pos2, boolean checkY) {
		boolean xzok = false;
		if (pos1 != null && pos2 != null && pos1.getWorld().equals(pos2.getWorld()) &&
				loc != null && loc.getWorld().equals(pos1.getWorld())) { // Mesmo mundo
			Map<String, Location> pos = getPosition(pos1.getBlock().getLocation(), pos2.getBlock().getLocation());
			pos1 = pos.get("pos1");
			pos2 = pos.get("pos2");
			if (loc.getX() <= pos1.getX() && loc.getX() >= pos2.getX()) {
				if (loc.getZ() <= pos1.getZ() && loc.getZ() >= pos2.getZ()) {
					xzok = true;
				}
			} else if (loc.getX() >= pos1.getX() && loc.getX() <= pos2.getX()) {
				if (loc.getZ() >= pos1.getZ() && loc.getZ() <= pos2.getZ()) {
					xzok = true;
				}
			}
			if (checkY) {
				if (xzok && loc.getY() >= pos1.getY() && loc.getY() <= pos2.getY()) {
					return true;
				} else {
					return false;
				}
			} else {
				return (xzok == true);
			}
		}
		return false;
	}

	/**
	 * Pegar duas localiza��es para um sele��o.
	 *
	 * @param pos1 a localiza��o da posi��o 1
	 * @param pos2 a localiza��o da posi��o 2
	 * @return O posi��o correta das localiza��es
	 */
	public static Map<String, Location> getPosition (Location pos1, Location pos2) {
		Map<String, Location> l = new HashMap<>();
		if (pos1 != null && pos2 != null && pos1.getWorld().equals(pos2.getWorld())) {
			if (pos1.getY() > pos2.getY()) { // Posi��o 1 na verdade � a posi��o 2
				Location p1 = pos1;
				pos1 = pos2.clone();
				pos2 = p1;
				p1 = null;
			}
		}
		l.put("pos1", pos1);
		l.put("pos2", pos2);
		return l;
	}
}
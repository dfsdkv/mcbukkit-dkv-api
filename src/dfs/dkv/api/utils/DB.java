/*
 * @author Deivison (DKV)
 * @year 2019
 */
package dfs.dkv.api.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.bukkit.plugin.java.JavaPlugin;

import dfs.dkv.api.DkvAPI;

/**
 * Class DB.
 */
public class DB {	
	/** A conexão. */
	public Connection con;

	/**
	 * Enum QueryType.
	 */
	public static enum QueryType {

		/** Tipo criar. */
		CREATE,

		/** Tipo ler. */
		READ,

		/** Tipo atualizar. */
		UPDATE,

		/** Tipo deletar. */
		DELETE
	};

	/** O nome do bando de dados. */
	private String bdn;

	/** O plugin. */
	private JavaPlugin main;

	/**
	 * Construtor de db.
	 *
	 * @param bdname o nome do banco de dados
	 * @param main o plugin
	 */
	public DB (String bdname, JavaPlugin main) {
		bdn = bdname;
		this.main = main;
	}

	/**
	 * Iniciar uma conexão com um banco de dados SqLite ou MySql.
	 *
	 * @param dr o tipo de conexão
	 */
	public void connect (String dr) {
		if (dr == "sqlite") {
			String folder = main.getDataFolder() + File.separator + DkvAPI.conf.get("database.sqlite.folder");
			File dataFolder = new File(folder);
			if (!dataFolder.exists()) { // Pasta /data ainda não existe
				dataFolder.mkdir();
			}
			File bfile = new File(folder, bdn + ".db");
			String url = "jdbc:sqlite:" + bfile;
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(url);
			} catch (Exception e) {}
		} else {
			String url = "jdbc:mysql://" + DkvAPI.conf.get("database.mysql.host") + ":" + DkvAPI.conf.get("database.mysql.port") + "/" + bdn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, DkvAPI.conf.get("database.mysql.user"), DkvAPI.conf.get("database.mysql.password"));
			} catch (Exception e) {}
		}
	}

	/**
	 * Criar uma tabela.
	 *
	 * @param tblname o nome da tabela
	 * @param tbfields os campos da tabela
	 */
	public void createTbl (String tblname, String tbfields) {
		Statement stm = null;
		try {
			stm = con.createStatement();
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS `" + tblname + "` (" + tbfields + ")");
			stm.close();
		} catch (SQLException e) {}
	}

	/***********
	 *   CRUD  *
	 ***********/
	/**
	 * Executar uma QUERY inteira.
	 *
	 * @param query o sql a ser executado
	 * @param type o tipo de execução
	 * @param fields os campos selecionados
	 * @return O ResultSet
	 */
	public ResultSet execQuery (String query, QueryType type, Object... fields) {
		// type: 1 = read, 2 = update, 3 = create, 4 delete
		PreparedStatement stm = null;
		ResultSet res = null;
		query += ";";
		try {
			stm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < fields.length; i++) {
				Object v = fields[i];
				if (v == null) {
					stm.setNull(i + 1, Types.NULL);
				} else if (v instanceof String) {
					stm.setString(i + 1, (String) v);
				} else if (v instanceof Integer) {
					stm.setInt(i + 1, (int) v);
				} else if (v instanceof Long) {
					stm.setLong(i + 1, (long) v);
				} else if (v instanceof Short) {
					stm.setShort(i + 1, (short) v);
				} else if (v instanceof Double) {
					stm.setDouble(i + 1, (double) v);
				}
			}
			if (!QueryType.READ.equals(type)) {
				stm.executeUpdate();
				res = stm.getGeneratedKeys();
			} else {
				res = stm.executeQuery();
			}
		} catch (SQLException e) {}
		return res;
	}

	/**
	 * Ler registros de uma tabela.
	 *
	 * @param table a tabela a ser lida
	 * @param fields os campos selecionados
	 * @param where a condição
	 * @param values os valores
	 * @return O ResultSet
	 */
	public ResultSet read (String table, String[] fields, String where, Object... values) {
		if (table.trim().isEmpty()) {
			return null;
		}
		String pfd = parseFields(fields);
		String sql = "SELECT " + (pfd.isEmpty() ? "*" : pfd) + " ";
		sql += "FROM `" + table + "`";
		if (!where.isEmpty()) {
			sql += " WHERE " + where;
		}
		return execQuery(sql, QueryType.READ, values);
	}

	/**
	 * Criar um novo registro em uma tabela.
	 *
	 * @param table a tabela a ser inserida
	 * @param fields os campos selecionados
	 * @param values os valores
	 * @return O ResultSet
	 */
	public ResultSet create (String table, String[] fields, Object... values) {
		String sql = "INSERT INTO `" + table + "` (" + parseFields(fields) + ") ";
		String v = "";
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				v += ", ";
			}
			v += "?";
		}
		sql += "VALUES (" + v + ")";
		return execQuery(sql, QueryType.CREATE, values);
	}

	/**
	 * Atualizar um registro de uma tabela.
	 *
	 * @param table a tabela a ser atualizada
	 * @param fields os campos a serem atualizados
	 * @param where a condição
	 * @param values os valores
	 * @return O ResultSet
	 */
	public ResultSet update (String table, String fields, String where, Object... values) {
		String sql = "UPDATE `" + table + "` SET " + fields + " WHERE " + where;
		return execQuery(sql, QueryType.UPDATE, values);
	}

	/**
	 * Deletar um registro de uam tabela.
	 *
	 * @param table a tabela a ser deletado o registro
	 * @param where a condição
	 * @param values os valores
	 * @return O ResultSet
	 */
	public ResultSet delete (String table, String where, Object... values) {
		String sql = "DELETE FROM `" + table + "` WHERE " + where;
		return execQuery(sql, QueryType.DELETE, values);
	}

	/**
	 * Fechar a conexão.
	 */
	public void closeCon () {
		if (con != null) { // Há uma conexão com o banco de dados
			try {
				con.close();
				con = null;
			} catch (SQLException e) {}
		}
	}

	/**
	 * Formatar os campos de uma tabela para string.
	 *
	 * @param fields os campos em formato de array
	 * @return Os campos formatados para sql
	 */
	private String parseFields (String[] fields) {
		String fd = "";
		int i = 0;
		for (String f : fields) {
			if (i > 0) {
				fd += " ";
			}
			fd += "`" + f.trim() + "`";
			if (i < fields.length - 1) {
				fd += ", ";
			}
			i++;
		}
		return fd;
	}
}
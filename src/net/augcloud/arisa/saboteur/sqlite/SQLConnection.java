/**
All rights Reserved, Designed By www.aug.cloud
SQLConnection.java
@Package net.augcloud.arisa.akits.sqlite
@Description:
@author: Arisa
@date:   2018年7月23日 上午11:29:05
@version V1.0
@Copyright: 2018
*/
package net.augcloud.arisa.saboteur.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2018年7月23日 上午11:29:05*/
public class SQLConnection extends PluginData {

	//改
	/**
	SQLConnection
	@Description:  */
	public SQLConnection(String path) {
		this.path = path;
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
		} catch (Exception e) {
			PluginData.logger.info(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		try {
			if (! this.connection.isClosed()) PluginData.logger.info("Opened database successfully");
			this.stmt = this.connection.createStatement();
		} catch (SQLException e) {
			PluginData.logger.info("打开数据库失败!!");
			PluginData.logger.info("插件即将卸载");
			PluginData.logger.info("请将报错内容发给作者");
			Bukkit.getPluginManager().disablePlugin(Main.plugin);

			e.printStackTrace();
		}
		this.blockhealthdata = "blockhealthdata";
		this.brokenerdata = "brokenerdata";
		this.createTable();
	}

	private final String path;
	private Connection connection = null;
	private Statement stmt = null;
	private final String blockhealthdata;
	private final String brokenerdata;

	public void OpenConnection() {
		if (this.connection == null) try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
		} catch (Exception e) {
			PluginData.logger.info(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		try {
			this.stmt = this.connection.createStatement();
		} catch (SQLException e) {
			PluginData.logger.info("打开连接失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			if (! this.connection.isClosed()) try {
				this.stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			PluginData.logger.info("关闭连接失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
	}

	//改
	public void createTable() {
		this.OpenConnection();
		String createServerkit = "CREATE TABLE If not exists " + this.blockhealthdata + " "
				+ "(ID INTEGER PRIMARY KEY     NOT NULL," //占位
				+ " location           TEXT    NOT NULL, "//location位置，可自写个序列化
				+ " health            INTEGER     NOT NULL, "//方块剩余生命值
				+ " state            INTEGER     NOT NULL, "//是否破坏，boolean 0 ->false 1 ->true
				+ " last_broken_date            BIGINT, "//可留空，转成数字存储日期
				+ " brokener_uuid            TEXT,"//可留空 破坏者的uuid
				+ " plunder_success_date			BIGINT)"; //箱子掠夺日期

		if (this.execute(createServerkit, false)) PluginData.logger.info("Blockhealthdata表 成功创建");
		String createPlayerkit = "CREATE TABLE If not exists " + this.brokenerdata + " "
				+ "(ID INTEGER PRIMARY KEY     NOT NULL, " //占位
				+ " brokener_uuid           TEXT    NOT NULL, "//破坏者uuid
				+ " peace_state            INTEGER     NOT NULL, "//当前和平与否 boolean 0 ->false 1 ->true
				+ " switch_peace_date            BIGINT, "//可留空，上一次切换和平模式日期
				//				+ " plunder_success_date            BIGINT,"//可留空，上一次成功掠夺日期
				+ " safemodeenddate			 BIGINT)";//玩家安全模式结束日期
		//留空以null形式
		if (this.execute(createPlayerkit, false)) PluginData.logger.info("Brokenerdata表 成功创建");
		this.closeConnection();
	}

	//改 brokenerdata有改动 plunder_success_date有改动
	public boolean Insert(String TableName, Object... args) {
		StringBuilder sb = new StringBuilder();
		switch (TableName) {
			case "blockhealthdata":
				sb.append("INSERT INTO " + this.blockhealthdata
						+ " (ID,location,health,state,last_broken_date,brokener_uuid,plunder_success_date) ");
				break;
			case "brokenerdata":
				sb.append("INSERT INTO " + this.brokenerdata
						+ " (ID,brokener_uuid,peace_state,switch_peace_date,safemodeenddate) ");
				break;
			default:
				return false;
		}
		sb.append("VALUES (null, ");
		String result;
		for (Object arg : args) {
			StringBuilder Value = new StringBuilder();
			if (arg instanceof String) Value.append("'" + String.valueOf(arg) + "'");
			else if (arg instanceof Integer) Value.append(String.valueOf(arg));
			else Value.append(String.valueOf(arg));
			Value.append(", ");
			sb.append(Value);
		}
		result = sb.substring(0, sb.length() - 2) + " )";
		return this.execute(result, false);
	}

	//改
	/**
	getdata
	@Description:
	@param TableName
	@param rs
	@return
	Map<String,Object>*/
	public Map<String, Object> getdata(String TableName, ResultSet rs) {
		Map<String, Object> data = new HashMap<>();
		try {
			switch (TableName) {
				case "blockhealthdata":
					data.put("id", rs.getInt("ID"));
					data.put("location", rs.getString("location"));
					data.put("health", rs.getInt("health"));
					data.put("state", rs.getInt("state"));
					data.put("last_broken_date", rs.getLong("last_broken_date"));

					data.put("brokener_uuid", rs.getString("brokener_uuid"));
					data.put("plunder_success_date", rs.getLong("plunder_success_date"));
					break;
				case "brokenerdata":
					data.put("id", rs.getInt("ID"));
					data.put("brokener_uuid", rs.getString("brokener_uuid"));
					data.put("peace_state", rs.getInt("peace_state"));
					data.put("switch_peace_date", rs.getLong("switch_peace_date"));
					data.put("safemodeenddate", rs.getLong("safemodeenddate"));
					break;
				default:

					break;
			}
		} catch (SQLException e) {
			PluginData.logger.info("读取数据库失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();

		}
		return data;
	}

	public List<Map<String, Object>> selectAll(String TableName) {
		List<Map<String, Object>> result = new ArrayList<>();

		try {
			this.stmt = this.connection.createStatement();
			ResultSet rs = this.stmt.executeQuery("SELECT * FROM " + TableName);
			while (rs.next())
				result.add(this.getdata(TableName, rs));
			rs.close();
			this.closeConnection();
		} catch (SQLException e) {
			PluginData.logger.info("读取数据库失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
		return result;

	}

	//	public Map<String, Map<String, Object>> selectAssign(String TableName) {
	//		Map<String, Map<String, Object>> result = new HashMap<>();
	//
	//		try {
	//			this.stmt = this.connection.createStatement();
	//			ResultSet rs = this.stmt.executeQuery("SELECT * FROM " + TableName);
	//			while (rs.next()) {
	//				result.put(getdata(TableName, rs));
	//			}
	//			rs.close();
	//			this.closeConnection();
	//		} catch (SQLException e) {
	//			// why make this exception?
	//			e.printStackTrace();
	//		}
	//		return result;
	//
	//	}

	/**
	containsKey
	@Description:
	@param TableName
	@param Column
	@param arg
	@return
	boolean*/
	public boolean containsKey(String TableName, String Column, Object arg) {
		return this.select(TableName, Column, arg).isEmpty() ? false : true;
	}

	/**
	select
	@Description: 从数据库中获取数据，如果没有则返回null
	@param TableName 表名
	@param Column 数据库字段名
	@param arg 参考数据
	@return
	Map<String,Object>*/
	public Map<String, Object> select(String TableName, String Column, Object arg) {
		this.OpenConnection();
		Map<String, Object> value = new HashMap<>();
		boolean isNumber = false;
		int arg_int = - 1;
		String arg_string = "";
		if (arg instanceof Integer) {
			isNumber = true;
			arg_int = ((Integer) arg).intValue();
		} else arg_string = (String) arg;
		try {
			this.stmt = this.connection.createStatement();
			ResultSet rs = this.stmt.executeQuery("SELECT * FROM " + TableName);
			while (rs.next())
				if (isNumber) {
					int id = rs.getInt(Column);
					if (id == arg_int) value = this.getdata(TableName, rs);
				} else {
					String str = rs.getString(Column);
					if (arg_string.equals(str)) value = this.getdata(TableName, rs);
				}
			rs.close();
			this.closeConnection();
		} catch (SQLException e) {
			PluginData.logger.info("读取数据库失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
		return value;
	}

	/**
	_select
	@Description:
	@param TableName
	@param Column
	@param arg
	@param Column2
	@param arg2
	@return
	Map<String,Object>*/
	public Map<String, Object> _select(String TableName, String Column, Object arg, String Column2, Object arg2) {
		this.OpenConnection();
		List<Map<String, Object>> data = new ArrayList<>();
		boolean isNumber = false;
		int arg_int = - 1;
		String arg_string = "";
		if (arg instanceof Integer) {
			isNumber = true;
			arg_int = ((Integer) arg).intValue();
		} else arg_string = (String) arg;
		try {
			this.stmt = this.connection.createStatement();
			ResultSet rs = this.stmt.executeQuery("SELECT * FROM " + TableName);
			while (rs.next()) {
				Map<String, Object> value = new HashMap<>();
				if (isNumber) {
					int id = rs.getInt(Column);

					if (id == arg_int) value = this.getdata(TableName, rs);
				} else {
					String str = rs.getString(Column);
					if (arg_string.equals(str)) value = this.getdata(TableName, rs);
				}
				data.add(value);
			}
			rs.close();
			this.closeConnection();
		} catch (SQLException e) {
			PluginData.logger.info("读取数据库失败!!");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
		for (Map<String, Object> map : data)
			if (map.get(Column2).equals(arg2)) return map;
		return null;
	}

	/**
	updata
	@Description:  更新数据库数据，成功返回true，失败返回false
	@param TableName 表名
	@param Column 数据列名
	@param arg 参考位置
	@param Columnvalue 修改数据列名
	@param value 修改值
	@return
	boolean*/
	public boolean updata(String TableName, String Columnvalue, Object value, String Column, Object arg) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(TableName).append(" SET ").append(Column).append(" = ");
		//		if (value instanceof String) sb.append("'").append(arg).append("'");
		//		else if (value instanceof Integer) sb.append(arg);
		//		else sb.append(String.valueOf(arg));
		sb.append(arg);
		sb.append(" WHERE ").append(Columnvalue).append(" = ");
		//		if (arg instanceof String) sb.append("'").append(value).append("'");
		//		else sb.append("'").append(String.valueOf(value)).append("'");
		sb.append("'").append(value).append("'");
		return this.execute(sb.toString(), false);
	}

	/**
	updata
	@Description:  更新数据库数据，成功返回true，失败返回false
	@param TableName 表名
	@param Column 数据列名
	@param arg 参考位置
	@param Columnvalue 修改数据列名
	@param value 修改值
	@return
	boolean*/
	public boolean _updata(String TableName, String Columnvalue, Object value, String Column, Object arg) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(TableName).append(" SET ").append(Column).append(" = ");
		//		if (value instanceof String) sb.append("'").append(arg).append("'");
		//		else if (value instanceof Integer) sb.append(arg);
		//		else sb.append(String.valueOf(arg));
		sb.append("'").append(arg).append("'");
		sb.append(" WHERE ").append(Columnvalue).append(" = ");
		//		if (arg instanceof String) sb.append("'").append(value).append("'");
		//		else sb.append("'").append(String.valueOf(value)).append("'");
		sb.append("'").append(value).append("'");
		return this.execute(sb.toString(), false);
	}

	/**
	updata
	@Description:  更新数据库数据，成功返回true，失败返回false
	@param TableName 表名
	@param Column 数据列名
	@param arg 参考位置
	@param Columnvalue 修改数据列名
	@param value 修改值
	@return
	boolean*/
	public boolean __updata(String TableName, String Column, Object arg, String Columnvalue, Object value) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(TableName).append(" SET ").append(Column).append(" = ");
		//		if (value instanceof String) sb.append("'").append(arg).append("'");
		//		else if (value instanceof Integer) sb.append(arg);
		//		else sb.append(String.valueOf(arg));
		sb.append("'").append(arg).append("'");
		sb.append(" WHERE ").append(Columnvalue).append(" = ");
		//		if (arg instanceof String) sb.append("'").append(value).append("'");
		//		else sb.append("'").append(String.valueOf(value)).append("'");
		sb.append(value);
		//		System.out.println(sb.toString());
		return this.execute(sb.toString(), false);
	}

	/**
	delect
	@Description: 移除数据库的一条记录，成功返回true，失败返回false
	@param TableName 表名
	@param Column 数据列名
	@param arg 参考的数据
	@return
	boolean*/
	public boolean delect(String TableName, String Column, Object arg) {
		StringBuilder sb = new StringBuilder("DELETE from ");
		sb.append(TableName).append(" where ").append(Column);
		if (arg instanceof String) sb.append(" = '").append(arg).append("'");
		else sb.append(" = '").append(String.valueOf(arg)).append("'");
		return this.execute(sb.toString(), false);
	}

	public boolean execute(String sql, boolean useupdata) {
		this.OpenConnection();
		boolean success = false;
		try {
			if (useupdata) {
				success = true;
				this.stmt.executeUpdate(sql);
			} else success = this.stmt.execute(sql);
			/**
			 * 不同点
			不同1：
			execute可以执行查询语句
			然后通过getResultSet，把结果集取出来
			executeUpdate不能执行查询语句

			不同2:
			execute返回boolean类型，true表示执行的是查询语句，false表示执行的是insert,delete,update等等
			executeUpdate返回的是int，表示有多少条数据受到了影响
			 */
		} catch (SQLException e) {
			PluginData.logger.info("执行sql指令失败");
			PluginData.logger.info("请将报错内容发给作者");
			e.printStackTrace();
		}
		this.closeConnection();
		return success;
	}

}

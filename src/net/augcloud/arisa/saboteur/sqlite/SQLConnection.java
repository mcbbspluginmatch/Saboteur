/**
All rights Reserved, Designed By www.aug.cloud
SQLConnection.java
@Package net.augcloud.arisa.akits.sqlite
@Description:
@author: Arisa
@date:   2018��7��23�� ����11:29:05
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
@date 2018��7��23�� ����11:29:05*/
public class SQLConnection extends PluginData {

	//��
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
			PluginData.logger.info("�����ݿ�ʧ��!!");
			PluginData.logger.info("�������ж��");
			PluginData.logger.info("�뽫�������ݷ�������");
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
			PluginData.logger.info("������ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
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
			PluginData.logger.info("�ر�����ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
			e.printStackTrace();
		}
	}

	//��
	public void createTable() {
		this.OpenConnection();
		String createServerkit = "CREATE TABLE If not exists " + this.blockhealthdata + " "
				+ "(ID INTEGER PRIMARY KEY     NOT NULL," //ռλ
				+ " location           TEXT    NOT NULL, "//locationλ�ã�����д�����л�
				+ " health            INTEGER     NOT NULL, "//����ʣ������ֵ
				+ " state            INTEGER     NOT NULL, "//�Ƿ��ƻ���boolean 0 ->false 1 ->true
				+ " last_broken_date            BIGINT, "//�����գ�ת�����ִ洢����
				+ " brokener_uuid            TEXT,"//������ �ƻ��ߵ�uuid
				+ " plunder_success_date			BIGINT)"; //�����Ӷ�����

		if (this.execute(createServerkit, false)) PluginData.logger.info("Blockhealthdata�� �ɹ�����");
		String createPlayerkit = "CREATE TABLE If not exists " + this.brokenerdata + " "
				+ "(ID INTEGER PRIMARY KEY     NOT NULL, " //ռλ
				+ " brokener_uuid           TEXT    NOT NULL, "//�ƻ���uuid
				+ " peace_state            INTEGER     NOT NULL, "//��ǰ��ƽ��� boolean 0 ->false 1 ->true
				+ " switch_peace_date            BIGINT, "//�����գ���һ���л���ƽģʽ����
				//				+ " plunder_success_date            BIGINT,"//�����գ���һ�γɹ��Ӷ�����
				+ " safemodeenddate			 BIGINT)";//��Ұ�ȫģʽ��������
		//������null��ʽ
		if (this.execute(createPlayerkit, false)) PluginData.logger.info("Brokenerdata�� �ɹ�����");
		this.closeConnection();
	}

	//�� brokenerdata�иĶ� plunder_success_date�иĶ�
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

	//��
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
			PluginData.logger.info("��ȡ���ݿ�ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
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
			PluginData.logger.info("��ȡ���ݿ�ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
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
	@Description: �����ݿ��л�ȡ���ݣ����û���򷵻�null
	@param TableName ����
	@param Column ���ݿ��ֶ���
	@param arg �ο�����
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
			PluginData.logger.info("��ȡ���ݿ�ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
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
			PluginData.logger.info("��ȡ���ݿ�ʧ��!!");
			PluginData.logger.info("�뽫�������ݷ�������");
			e.printStackTrace();
		}
		for (Map<String, Object> map : data)
			if (map.get(Column2).equals(arg2)) return map;
		return null;
	}

	/**
	updata
	@Description:  �������ݿ����ݣ��ɹ�����true��ʧ�ܷ���false
	@param TableName ����
	@param Column ��������
	@param arg �ο�λ��
	@param Columnvalue �޸���������
	@param value �޸�ֵ
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
	@Description:  �������ݿ����ݣ��ɹ�����true��ʧ�ܷ���false
	@param TableName ����
	@param Column ��������
	@param arg �ο�λ��
	@param Columnvalue �޸���������
	@param value �޸�ֵ
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
	@Description:  �������ݿ����ݣ��ɹ�����true��ʧ�ܷ���false
	@param TableName ����
	@param Column ��������
	@param arg �ο�λ��
	@param Columnvalue �޸���������
	@param value �޸�ֵ
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
	@Description: �Ƴ����ݿ��һ����¼���ɹ�����true��ʧ�ܷ���false
	@param TableName ����
	@param Column ��������
	@param arg �ο�������
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
			 * ��ͬ��
			��ͬ1��
			execute����ִ�в�ѯ���
			Ȼ��ͨ��getResultSet���ѽ����ȡ����
			executeUpdate����ִ�в�ѯ���

			��ͬ2:
			execute����boolean���ͣ�true��ʾִ�е��ǲ�ѯ��䣬false��ʾִ�е���insert,delete,update�ȵ�
			executeUpdate���ص���int����ʾ�ж����������ܵ���Ӱ��
			 */
		} catch (SQLException e) {
			PluginData.logger.info("ִ��sqlָ��ʧ��");
			PluginData.logger.info("�뽫�������ݷ�������");
			e.printStackTrace();
		}
		this.closeConnection();
		return success;
	}

}

/**
All rights Reserved, Designed By www.aug.cloud
StroageUtils.java
@Package net.augcloud.arisa.akits.sqlite
@Description:
@author: Arisa
@date:   2018年7月23日 上午11:28:53
@version V1.0
@Copyright: 2018
*/
package net.augcloud.arisa.saboteur.sqlite;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.SetFiles;

/**
@author Arisa
@date 2018年7月23日 上午11:28:53*/
public class StroageUtils {
	public static String database_Name = "database.db";
	public static String path;
	public static SQLConnection SQLConnection;

	public static void init() {
		database_Name = SetFiles.getConfig().getString("database_Name");
		StringBuilder sb = new StringBuilder(Main.plugin.getDataFolder().getPath());
		sb.append("\\").append(StroageUtils.database_Name);
		StroageUtils.path = sb.toString();
		StroageUtils.SQLConnection = new SQLConnection(StroageUtils.path);
	}

}

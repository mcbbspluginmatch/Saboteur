/**  
All rights Reserved, Designed By www.aug.cloud
DatabaseClear.java   
@Package net.augcloud.arisa.saboteur.sqlite   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午4:21:46   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.sqlite;

import java.util.List;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月29日 下午4:21:46*/
public class DatabaseClear extends PluginData {

	public static boolean closeClear = false;

	public static void closeClear() {
		logger.info("DatabaseClear数据库清理者 已关闭...");
		closeClear = true;
	}

	/**   
	DatabaseClear   
	@Description:*/
	public static void startClear() {
		new BukkitRunnable() {
			@Override

			public void run() {
				List<Map<String, Object>> AllData = StroageUtils.SQLConnection.selectAll("brokenerdata");
				if (AllData.size() <= 10) { return; }
				for (int i = 10, size = AllData.size(); i < size; i++ ) {
					Map<String, Object> data = AllData.get(i);
					if ((Long) data.get("plunder_success_date") != 0) {
						continue;
					}
					StroageUtils.SQLConnection.delect("brokenerdata", "id", data.get("id"));
				}
				if (DatabaseClear.closeClear) this.cancel();
			}
		}.runTaskTimer(Main.plugin, 1200, 1200);
		logger.info("DatabaseClear数据库清理者 已启动!");
	}

}

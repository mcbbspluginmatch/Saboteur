/**  
All rights Reserved, Designed By www.aug.cloud
PlayerSafeModeHook.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午6:20:57   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;
import net.augcloud.arisa.saboteur.sqlite.StroageUtils;

/**
@author Arisa
@date 2019年7月29日 下午6:20:57*/
public class PlayerSafeModeHook extends PluginData {

	public static boolean stop = false;

	public static void stop() {
		stop = true;
		logger.info("CheckMaturity线程 已关闭!");
	}

	public static void checkPlayerSafeMode(Player player) {
		String player_uuid = player.getUniqueId().toString();
		Map<String, Object> data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		if (data == null || data.isEmpty()) {
			System.out.println("开通玩家保护期异常，强行修复");
			SQLUtils.NewFilesOfPlayer(player);
			data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		}
		long safemodeenddate = (Long) data.get("safemodeenddate");
		long NewTime = new Date().getTime();
		if (safemodeenddate != 0) {
			if (NewTime >= safemodeenddate) {
				PlayerSafeModeManager.closePlayerSafeMode(player);
				Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_close_safemode"));
				StroageUtils.SQLConnection._updata("brokenerdata", "brokener_uuid", player_uuid, "safemodeenddate", 0);

			} else {
				if (! PlayerSafeModeManager.getSafeMode(player)) {
					PlayerSafeModeManager.openPlayerSafeMode(player);
				}
			}
		}

	}

	public static void startCheckThread() {
		new BukkitRunnable() {
			@Override
			public void run() {

				Collection<? extends Player> Players = Bukkit.getOnlinePlayers();
				for (Player player : Players) {
					checkPlayerSafeMode(player);
				}
				if (stop) this.cancel();
			}
		}.runTaskTimer(Main.plugin, 1200, 1200);
		logger.info("CheckMaturity线程 已启动!");
	}

	public static void openSafeMode(Player player, int lastTime) {
		//两种可能分开写，一种是续费一种是开通还有到期后再开通可能性，必须注意
		String player_uuid = player.getUniqueId().toString();
		Map<String, Object> data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		if (data == null || data.isEmpty()) {
			System.out.println("开通玩家保护期异常，强行修复");
			SQLUtils.NewFilesOfPlayer(player);
			data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		}
		long safemodeenddate = (Long) data.get("safemodeenddate");
		long NewTime = new Date().getTime();
		long openTime = lastTime * 3600000;
		if (NewTime > safemodeenddate) {
			StroageUtils.SQLConnection._updata("brokenerdata", "brokener_uuid", player_uuid, "safemodeenddate",
					NewTime + openTime);
			Logger.SendToPlayer(player,
					SetFiles.getConfig().getString("when_open_safemode").replace("$time", String.valueOf(lastTime)));
			return;
		}
		StroageUtils.SQLConnection._updata("brokenerdata", "brokener_uuid", player_uuid, "safemodeenddate",
				safemodeenddate + openTime);
		Logger.SendToPlayer(player,
				SetFiles.getConfig().getString("when_open_safemode").replace("$time", String.valueOf(lastTime)));

	}

}

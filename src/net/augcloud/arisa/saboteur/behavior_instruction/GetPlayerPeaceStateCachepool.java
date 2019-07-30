/**  
All rights Reserved, Designed By www.aug.cloud
GetPlayerPeaceStateCachepool.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午6:45:35   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;
import net.augcloud.arisa.saboteur.sqlite.StroageUtils;

/**
@author Arisa
@date 2019年7月29日 下午6:45:35*/
public class GetPlayerPeaceStateCachepool extends PluginData {

	protected HashMap<String, Boolean> Cachepool = null;
	public boolean stop = false;
	protected String lock = "lock";

	//	public static void Run() {
	//		PluginData.GetPlayerPeaceStateCachepool = new GetPlayerPeaceStateCachepool();
	//	}

	//适用且更快
	public boolean getPlayerPeaceState(Player player) {
		String player_name = player.getName();
		return this.Cachepool.containsKey(player_name) ? this.Cachepool.get(player_name)
				: SQLUtils.isPeaceState(player);
	}

	public boolean getPlayerPeaceState(String player_name) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(player_name);
		return this.Cachepool.containsKey(player_name) ? this.Cachepool.get(player_name)
				: SQLUtils.isPeaceState(player.getUniqueId().toString());
	}

	public boolean getPlayerPeaceState_uuid(String player_uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(player_uuid));
		String player_name = player.getName();
		return this.Cachepool.containsKey(player_name) ? this.Cachepool.get(player_name)
				: SQLUtils.isPeaceState(player_uuid);
	}

	public void closeCachePool() {
		this.stop = true;
		logger.info("CachePool线程 已停止工作");
	}

	public void startCachePool() {
		new BukkitRunnable() {
			@Override
			public void run() {
				synchronized (this) {
					Cachedata();
					if (GetPlayerPeaceStateCachepool.this.stop) this.cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 200);
		logger.info("CachePool1线程 已开始工作");
	}

	public void removeThread() {
		new BukkitRunnable() {

			@Override
			public void run() {
				synchronized (this) {
					removeOfflinePlayer();
					if (GetPlayerPeaceStateCachepool.this.stop) this.cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 200, 400);
		logger.info("CachePoolClear线程 已开始工作");
	}

	public GetPlayerPeaceStateCachepool() {
		this.Cachepool = new HashMap<>();
		startCachePool();
		removeThread();
	}

	public void removeOfflinePlayer() {
		if (this.Cachepool.isEmpty()) { return; }
		Collection<? extends Player> Players = Bukkit.getOnlinePlayers();
		if (Players.isEmpty()) { return; }
		List<String> PlayerNames = new ArrayList<>();
		for (Player player : Players) {
			PlayerNames.add(player.getName());
		}
		Iterator<Entry<String, Boolean>> data_iter = this.Cachepool.entrySet().iterator();
		while (data_iter.hasNext()) {
			String next = data_iter.next().getKey();
			if (! PlayerNames.contains(next)) {
				this.Cachepool.remove(next);
			}
		}
	}

	public void Cachedata() {
		Collection<? extends Player> Players = Bukkit.getOnlinePlayers();
		if (Players.isEmpty()) { return; }
		List<Map<String, Object>> data = StroageUtils.SQLConnection.selectAll("brokenerdata");
		HashMap<String, Map<String, Object>> p_data = new HashMap<>();
		for (Map<String, Object> _data : data)
			p_data.put((String) _data.get("brokener_uuid"), _data);
		for (Player player : Players) {
			String player_uuid = player.getUniqueId().toString();
			Map<String, Object> a = p_data.get(player_uuid);
			if (a == null || a.isEmpty()) continue;
			boolean b = (Integer) a.get("peace_state") == 1 ? true : false;
			this.Cachepool.put(player.getName(), b);
		}
	}
}

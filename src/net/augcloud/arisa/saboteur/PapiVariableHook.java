/**  
All rights Reserved, Designed By www.aug.cloud
PapiVariableHook.java   
@Package net.augcloud.arisa.vipmanage   
@Description: 
@author: Arisa   
@date:   2018年8月16日 上午1:05:12   
@version V1.0 
@Copyright: 2018 
*/
package net.augcloud.arisa.saboteur;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.augcloud.arisa.saboteur.sqlite.StroageUtils;

/**
@author Arisa
@date 2018年8月16日 上午1:05:12*/
public class PapiVariableHook extends EZPlaceholderHook {

	public static Plugin $Main = Main.plugin;

	/**   
	PapiVariableHook   
	@Description:*/
	public PapiVariableHook(Plugin plugin) {
		super(plugin, "starry_land");
		$Main = plugin;
	}

	/**   
	onPlaceholderRequest   
	@Description: 
	@param p
	@param identifier
	@return   
	@see me.clip.placeholderapi.PlaceholderHook#onPlaceholderRequest(org.bukkit.entity.Player, java.lang.String)*/
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		String result = "";
		if (identifier.equals("peace")) {
			if (PluginData.GetPlayerPeaceStateCachepool.getPlayerPeaceState(player)) {
				result = "是";
			} else result = "否";
		} else if (identifier.equals("safemode")) {
			long lasttime = (long) StroageUtils.SQLConnection
					.select("brokenerdata", "brokener_uuid", player.getUniqueId().toString()).get("safemodeenddate");
			long NewTime = new Date().getTime();
			long hasTime = lasttime - NewTime;
			if (hasTime <= 0) {
				hasTime = 0;
			}
			result = String.valueOf(hasTime);
		}
		return result;
	}
}

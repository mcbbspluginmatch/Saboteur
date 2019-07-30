/**
All rights Reserved, Designed By www.aug.cloud
ActionAPILocalization.java
@Package net.augcloud.arisa.saboteur.message_module
@Description:
@author: Arisa
@date:   2019年7月25日 下午10:38:17
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.message_module;

import org.bukkit.entity.Player;

/**
@author Arisa
@date 2019年7月25日 下午10:38:17*/
public class ActionAPILocalization {

	public static void sendActionMessage(Player player, String text) {
		ActionbarAPI.sendActionBar(player, text, 50);
	}

	public static void sendActionMessage(Player player, String text, boolean a) {
		ActionbarAPI.sendActionBar(player, text, 100);
	}

}

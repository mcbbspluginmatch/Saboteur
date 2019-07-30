/**
All rights Reserved, Designed By www.aug.cloud
PlayerQuitListener.java
@Package net.augcloud.arisa.saboteur.menu
@Description:
@author: Arisa
@date:   2019年7月30日 下午12:43:23
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月30日 下午12:43:23*/
public class PlayerQuitListener extends PluginData implements Listener {

	/**
	PlayerQuitListener
	@Description:*/
	public PlayerQuitListener() {
	}

	@EventHandler
	public void whenPlayerQuit(PlayerQuitEvent e) {
		HumanEntity player = e.getPlayer();
		if (! (player instanceof Player)) return;
		PluginData.MenuManager.delete((Player) player);
	}

}

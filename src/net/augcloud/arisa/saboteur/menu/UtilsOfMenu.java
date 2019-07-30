/**  
All rights Reserved, Designed By www.aug.cloud
UtilsOfMenu.java   
@Package net.augcloud.arisa.saboteur.menu   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午6:57:21   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.menu;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月29日 下午6:57:21*/
public class UtilsOfMenu extends PluginData {

	public static void OpenMenu(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 1, 1);

		Inventory inv = MenuManager.get(player);
		if (inv == null) {
			MenuSetup menuSetup = new MenuSetup(player);
			menuSetup.initOfMenu();
			player.openInventory(menuSetup.getInventory());
			MenuManager.add(player, menuSetup);
		} else player.openInventory(inv);
	}

	public static void CloseMenu(Player player, Inventory inv) {
		MenuManager.add(player, inv);
	}

	public static void MenuInits() {
		MenuManager = new MenuManager();
	}

	/**   
	UtilsOfMenu   
	@Description:*/
	public UtilsOfMenu() {
	}

}

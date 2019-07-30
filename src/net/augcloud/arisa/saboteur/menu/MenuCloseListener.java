/**  
All rights Reserved, Designed By www.aug.cloud
MenuCloseListener.java   
@Package net.augcloud.arisa.saboteur.menu   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午9:52:02   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.menu;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.Utils;

/**
@author Arisa
@date 2019年7月29日 下午9:52:02*/
public class MenuCloseListener extends PluginData implements Listener {

	public static String Menu_title;

	/**   
	MenuCloseListener   
	@Description:*/
	public MenuCloseListener() {

	}

	public static void inits() {
		Menu_title = Utils._ReplaceColour(SetFiles.getConfig().getString("menu_title"));
	}

	@EventHandler
	public void whenMenuClose(InventoryCloseEvent e) {
		HumanEntity player = e.getPlayer();
		if (! (player instanceof Player)) return;
		Inventory inv = e.getInventory();
		if (inv == null) { return; }

		ItemStack item = inv.getItem(0);
		if (item == null || ! item.hasItemMeta()) { return; }
		ItemMeta ids = item.getItemMeta();
		if (ids.getDisplayName().equals("您")) {

			MenuManager.add((Player) player, inv);
			((Player) player).playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);

		}
	}

}

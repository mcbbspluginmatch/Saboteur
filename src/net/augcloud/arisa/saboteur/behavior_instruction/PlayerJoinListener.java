/**  
All rights Reserved, Designed By www.aug.cloud
PlayerJoinListener.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月27日 下午1:07:14   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.menu.OpenItemer;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;

/**
@author Arisa
@date 2019年7月27日 下午1:07:14*/
public class PlayerJoinListener extends PluginData implements Listener {

	public static String book_name;

	public static void inits() {
		book_name = _ReplaceColour(SetFiles.getConfig().getString("Open_BookName"));
	}

	@EventHandler
	public void WhenPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		SQLUtils.NewFilesOfPlayer(player);
		//		PlayerSafeModeManager.NewRecordOfPlayer(player);
		PlayerSafeModeHook.checkPlayerSafeMode(player);

		if (Main.useOpen_Book) {
			ItemStack[] items = player.getInventory().getContents();
			for (ItemStack item : items) {
				if (item == null || ! item.hasItemMeta()) {
					continue;
				}
				if (item.getItemMeta().getDisplayName().equals(book_name)) {

					return;
				}
			}
			OpenItemer.givebook(player);
		}

	}

}

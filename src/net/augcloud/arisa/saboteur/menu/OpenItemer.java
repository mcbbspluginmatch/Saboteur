/**  
All rights Reserved, Designed By www.aug.cloud
OpenItemer.java   
@Package net.augcloud.arisa.saboteur.menu   
@Description: 
@author: Arisa   
@date:   2019年7月30日 下午8:07:13   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.menu;
/**
@author Arisa
@date 2019年7月30日 下午8:07:13*/

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;

public class OpenItemer extends PluginData {

	private static ItemStack item = new ItemStack(Material.BOOK);

	/**   
	OpenItemer   
	@Description:*/
	public OpenItemer() {
	}

	public static void inits() {
		ItemMeta ids = item.getItemMeta();
		ids.setDisplayName(_ReplaceColour(SetFiles.getConfig().getString("Open_BookName")));
		List<String> lore = new ArrayList<>();
		lore.add("§aSaboteur随身打开器");
		lore.add("§a右键打开");
		lore.add("");
		lore.add("");
		lore.add("  §7- §3您可以在此进行掠夺");
		lore.add("  §7- §3活动，也可以选择§a和平");
		ids.setLore(lore);
		item.setItemMeta(ids);
	}

	public static void givebook(Player player) {
		player.getInventory().addItem(item);
	}

}

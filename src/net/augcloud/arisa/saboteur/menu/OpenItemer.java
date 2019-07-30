/**  
All rights Reserved, Designed By www.aug.cloud
OpenItemer.java   
@Package net.augcloud.arisa.saboteur.menu   
@Description: 
@author: Arisa   
@date:   2019��7��30�� ����8:07:13   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.menu;
/**
@author Arisa
@date 2019��7��30�� ����8:07:13*/

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
		lore.add("��aSaboteur�������");
		lore.add("��a�Ҽ���");
		lore.add("");
		lore.add("");
		lore.add("  ��7- ��3�������ڴ˽����Ӷ�");
		lore.add("  ��7- ��3���Ҳ����ѡ���a��ƽ");
		ids.setLore(lore);
		item.setItemMeta(ids);
	}

	public static void givebook(Player player) {
		player.getInventory().addItem(item);
	}

}

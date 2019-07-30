/**  
All rights Reserved, Designed By www.aug.cloud
MenuManager.java   
@Package net.augcloud.arisa.saboteur.menu   
@Description: 
@author: Arisa   
@date:   2019年7月29日 下午9:06:27   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.menu;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月29日 下午9:06:27*/
public class MenuManager extends PluginData {

	protected static HashMap<String, MenuSetup> playdatas = new HashMap<>();
	public static boolean stop = false;

	public static void stop() {
		stop = true;
		logger.info("MenuManager线程 已关闭!");
	}

	public void add(Player player, MenuSetup mSet) {
		playdatas.put(player.getName(), mSet);
	}

	public void add(Player player, Inventory inv) {
		MenuSetup menuSetup = new MenuSetup(player);
		menuSetup.setInv(inv);
		playdatas.put(player.getName(), menuSetup);
	}

	public Inventory get(Player player) {
		MenuSetup menuSetup = playdatas.get(player.getName());
		return menuSetup != null ? menuSetup.getInventory() : null;
	}

	public MenuSetup _get(Player player) {
		return playdatas.get(player.getName());
	}

	public void delete(Player player) {
		playdatas.remove(player.getName());
	}

	/**   
	MenuManager   
	@Description:*/
	public MenuManager() {
	}

	public static void startMenuManagerThread() {
		new BukkitRunnable() {
			@Override
			public void run() {

				playdatas.clear();
				if (stop) this.cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 100);
		logger.info("MenuManager线程 已启动!");
	}

	/**  
	isStop 
	@Description: 外部成员通过getter访问isStop字段
	@return: boolean*/
	public static boolean isStop() {
		return stop;
	}

	/**  
	@Title:  setStop
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public static void setStop(boolean stop) {
		MenuManager.stop = stop;
	}

	/**  
	getPlaydatas 
	@Description: 外部成员通过getter访问getPlaydatas字段
	@return: HashMap<String,MenuSetup>*/
	public static HashMap<String, MenuSetup> getPlaydatas() {
		return playdatas;
	}

	/**  
	@Title:  setPlaydatas
	@Description: 外部成员通过setter方法修改字段
	@return: HashMap<String,MenuSetup>*/
	public static void setPlaydatas(HashMap<String, MenuSetup> playdatas) {
		MenuManager.playdatas = playdatas;
	}

}

/**  
All rights Reserved, Designed By www.aug.cloud
RewardNot_PeacePlayer.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019��7��29�� ����6:38:22   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;

/**
@author Arisa
@date 2019��7��29�� ����6:38:22*/
public class RewardNot_PeacePlayer extends PluginData {

	protected static boolean stop = false;
	protected static int RewardNot_PeacePlayerIntervalTime = 12000;
	protected static List<String> cmds = new ArrayList<>();
	protected static List<Player> NotAirPlayer;
	protected static String lock = "lock";

	public static void RewardPlayer(Player player) {
		for (String cmd : cmds) {
			Main.plugin.getServer().dispatchCommand(Main.plugin.getServer().getConsoleSender(),
					cmd.replace("$player", player.getName()));
		}
	}

	public static void closeRewardPlayer() {
		stop = true;
		logger.info("RewardNot_PeacePlayer ��������ƽ��� �ѹر�");
		logger.info("checkInventoryThread ��鱳���Ƿ�Ϊ���߳� �ѹر�");
	}

	public static boolean checkPlayerInventory(Player player) {
		PlayerInventory pinv = player.getInventory();
		ItemStack[] items = pinv.getContents();
		for (ItemStack item : items) {
			if (item == null || item.getType().equals(Material.AIR)) { return true; }
		}
		return false;

	}

	public static void addPlayerInventoryNotAir(Player player) {
		NotAirPlayer.add(player);
	}

	public static void StartcheckInventoryThread() {
		new BukkitRunnable() {
			@Override

			public void run() {
				synchronized (this) {
					if (NotAirPlayer.isEmpty()) { return; }
					for (Player player : NotAirPlayer) {
						if (! checkPlayerInventory(player)) {
							logger.SendToPlayer(player, SetFiles.getConfig().getString("when_PlayerInventoryNotAir"));
							continue;
						}
						NotAirPlayer.remove(player);
						RewardPlayer(player);
					}
					if (stop) {
						this.cancel();
					}
				}
			}
		}.runTaskTimer(Main.plugin, 100, 100);
		logger.info("checkInventoryThread ��鱳���Ƿ�Ϊ���߳� �ѿ���");
	}

	public static void startRewardPlayer() {
		StartcheckInventoryThread(); //������鱳���߳�
		NotAirPlayer = new ArrayList<>();
		cmds = SetFiles.getConfig().getStringList("RewardCmd");
		RewardNot_PeacePlayerIntervalTime = SetFiles.getConfig().getInt("RewardNot_PeacePlayerIntervalTime") * 1200;
		new BukkitRunnable() {
			@Override

			public void run() {
				synchronized (this) {
					Collection<? extends Player> Players = Bukkit.getOnlinePlayers();
					for (Player player : Players) {
						//��ȡ��Һ�ƽ״̬����...
						boolean PeaceState = GetPlayerPeaceStateCachepool.getPlayerPeaceState(player);
						if (PeaceState) continue;
						if (! checkPlayerInventory(player)) {
							addPlayerInventoryNotAir(player);
							continue;
						}
						RewardPlayer(player);
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1L, 1L);
						logger.SendToPlayer(player, SetFiles.getConfig().getString("when_reward_time"));
					}
				}
			}
		}.runTaskTimer(Main.plugin, RewardNot_PeacePlayerIntervalTime, RewardNot_PeacePlayerIntervalTime);

		logger.info("RewardNot_PeacePlayer ��������ƽ��� �ѿ���");
	}

}

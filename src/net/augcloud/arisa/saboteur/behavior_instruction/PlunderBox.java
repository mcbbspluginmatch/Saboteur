/**  
All rights Reserved, Designed By www.aug.cloud
PlunderBox.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月27日 上午9:18:52   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.sqlite.StroageUtils;

/**
@author Arisa
@date 2019年7月27日 上午9:18:52*/
public class PlunderBox extends PluginData {

	private static double MaxRate = 0.0;
	private static double MinRate = 0.0;

	public static void PlunderRateInit() {
		MaxRate = SetFiles.getConfig().getDouble("Plunder_MaxRate");
		MinRate = SetFiles.getConfig().getDouble("Plunder_MinRate");
	}

	public static double getRate() {
		return ThreadLocalRandom.current().nextDouble(MinRate, MaxRate);
	}

	//219 234
	public static boolean PlunderCheck(Block block) {
		Material id = block.getBlockData().getMaterial();
		if (id.equals(Material.CHEST)) return true;
		switch (id) {
			case BLACK_SHULKER_BOX:
				return true;
			case BROWN_SHULKER_BOX:
				return true;
			case CYAN_SHULKER_BOX:
				return true;
			case GREEN_SHULKER_BOX:
				return true;
			case BLUE_SHULKER_BOX:
				return true;
			case GRAY_SHULKER_BOX:
				return true;
			case LIGHT_BLUE_SHULKER_BOX:
				return true;
			case LIGHT_GRAY_SHULKER_BOX:
				return true;
			case LIME_SHULKER_BOX:
				return true;
			case MAGENTA_SHULKER_BOX:
				return true;
			case ORANGE_SHULKER_BOX:
				return true;
			case PINK_SHULKER_BOX:
				return true;
			case PURPLE_SHULKER_BOX:
				return true;
			case RED_SHULKER_BOX:
				return true;
			case SHULKER_BOX:
				return true;
			case WHITE_SHULKER_BOX:
				return true;
			case YELLOW_SHULKER_BOX:
				return true;
			default:
				return false;
		}

	}

	//更新箱子掠夺时间
	public static void UpdataPlunderTime(Block block) {
		String a = new LocationSerialize(block.getLocation()).Serializedata2String();
		Map<String, Object> block_data = StroageUtils.SQLConnection.select("blockhealthdata", "location", a);
		long NewTime = new Date().getTime();
		StroageUtils.SQLConnection._updata("blockhealthdata", "location", a, "plunder_success_date", NewTime);

	}

	//检查箱子掠夺间隔时间
	public static boolean PlunderInterval(Player Brokener, Block block) {
		String player_uuid = Brokener.getUniqueId().toString();
		if (Brokener.isOp()) {
			Logger.SendToPlayer(Brokener, "§c您是OP，不受掠夺间隔时间限制!");
			return true;
		}
		String a = new LocationSerialize(block.getLocation()).Serializedata2String();
		Map<String, Object> block_data = StroageUtils.SQLConnection.select("blockhealthdata", "location", a);

		//		Map<String, Object> data = StroageUtils.SQLConnection.select("blockhealthdata", "brokener_uuid", player_uuid);
		//		if (data == null || data.isEmpty()) {
		//			System.out.println("掠夺时间间隔检测异常...强行修复!");
		//			SQLUtils.NewFilesOfPlayer(Brokener);
		//			data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		//		}
		long BrokenerPlunderTime = (Long) block_data.get("plunder_success_date");
		long PlunderInterval = SetFiles.getConfig().getLong("PlunderBoxInterval") * 1000;
		long NewTime = new Date().getTime();
		long SurplusTime = BrokenerPlunderTime + PlunderInterval - NewTime;
		if (SurplusTime > 0) {
			Logger.SendToPlayer(Brokener, SetFiles.getConfig().getString("when_their_cant_break").replace("$time",
					String.valueOf(SurplusTime / 1000)));
			return false;
			//还没到时间
		}
		return true;

	}

	private static int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	public static void CompletePlunder(Player Brokener, Block block) {
		if (! PlunderCheck(block)) return;
		if (! PlunderInterval(Brokener, block)) return;

		BlockState BlockState = block.getState();
		Inventory inv = null;
		if (BlockState instanceof Chest) {
			Chest box = (Chest) block.getState();
			inv = box.getBlockInventory();
		} else {
			Container box = (Container) block.getState();
			inv = box.getInventory();
		}
		//可能存在这种情况
		if (inv == null) {
			Logger.SendToPlayer(Brokener, "客户端连接超时...掠夺失败!");
			return;
		}
		ItemStack[] items = inv.getContents();
		//		ItemStack air = new ItemStack(0);
		ArrayList<ItemStack> arrayList = new ArrayList<>(Arrays.asList(items));
		//		HashMap<Integer, ItemStack> m_items = new HashMap<>();

		ItemStack air = new ItemStack(Material.AIR);

		if (! arrayList.isEmpty()) {
			for (; arrayList.contains(null);)
				arrayList.remove(null);
			for (; arrayList.contains(air);)
				arrayList.remove(air);
			int wholeItem = 0;
			if (! arrayList.isEmpty()) {
				for (ItemStack item : arrayList)
					wholeItem += item.getAmount();
				if (wholeItem != 0) {
					HashMap<Integer, ItemStack> SurplusItem = new HashMap<>();
					PlayerInventory pinv = Brokener.getInventory();
					int Successfullooting = 0;

					int number = (int) (wholeItem * getRate());
					int Max = items.length * 2;
					//两种可能性，箱内物品小于预期物品？不可能!
					List<Integer> dontCheck = new ArrayList<>();
					for (; Successfullooting < number;) {
						int index = randomInt(0, items.length);
						if (! dontCheck.contains(index)) {
							ItemStack item = items[index];
							if (item == null || item.getType().equals(Material.AIR)) {
								dontCheck.add(index);
								continue;
							}
							int SurplusAmount = item.getAmount() - 1;
							if (SurplusAmount <= 0) inv.setItem(index, air);
							ItemStack _item = item.clone();
							item.setAmount(SurplusAmount);
							Successfullooting++ ;
							_item.setAmount(1);
							SurplusItem.putAll(pinv.addItem(_item));
							inv.setItem(index, item);
						}
					}

					if (! SurplusItem.isEmpty()) {
						Iterator<Entry<Integer, ItemStack>> _iter = SurplusItem.entrySet().iterator();
						World world = Brokener.getWorld();
						Location loc = Brokener.getLocation();
						while (_iter.hasNext()) {
							Entry<Integer, ItemStack> next = _iter.next();
							world.dropItem(loc, next.getValue());
						}
					}
					UpdataPlunderTime(block);
					Brokener.playSound(Brokener.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1L, 1L);
					Logger.SendToPlayer(Brokener, SetFiles.getConfig().getString("when_success_breaking2").replace("$a",
							String.valueOf(Successfullooting) + "(" + number + ")"));
					return;
				}
			}
		}

		Logger.SendToPlayer(Brokener, "§c这是一个空箱...");
		//		box.setCustomName("储物箱  §c§l● 被 " + Brokener.getName() + " 劫掠!!");
	}
}

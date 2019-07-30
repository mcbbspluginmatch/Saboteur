/**  
All rights Reserved, Designed By www.aug.cloud
SQLUtils.java   
@Package net.augcloud.arisa.saboteur.sqlite   
@Description: 
@author: Arisa   
@date:   2019年7月27日 上午7:55:08   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.sqlite;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.behavior_instruction.AttackQualityMeta;
import net.augcloud.arisa.saboteur.behavior_instruction.BlockBreakQualityMeta;
import net.augcloud.arisa.saboteur.behavior_instruction.LocationSerialize;
import net.augcloud.arisa.saboteur.behavior_instruction.PlunderBox;
import net.augcloud.arisa.saboteur.logger.Logger;

/**
@author Arisa
@date 2019年7月27日 上午7:55:08*/
public class SQLUtils extends PluginData {

	public static boolean isPeaceState(Player player) {
		Map<String, Object> data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid",
				player.getUniqueId().toString());
		if (data == null || data.isEmpty()) {
			System.out.println("检测和平状态异常，强行修复");
			SQLUtils.NewFilesOfPlayer(player);
			data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player.getUniqueId().toString());
		}
		return data != null && (Integer) data.get("peace_state") == 0 ? false : true;
	}

	public static boolean isPeaceState(String player_uuid) {
		Map<String, Object> data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		if (data == null || data.isEmpty()) {
			System.out.println("检测和平状态异常，强行修复");
			SQLUtils.NewFilesOfPlayer(player_uuid);
			data = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid", player_uuid);
		}
		return data != null && (Integer) data.get("peace_state") == 0 ? false : true;
	}

	public static Map<String, Object> readBlockbySQLite(Location loc) {
		Map<String, Object> data = StroageUtils.SQLConnection.select("blockhealthdata", "location",
				new LocationSerialize(loc).Serializedata2String());
		if (data == null || data.isEmpty()) return null;
		return data;
	}

	public static boolean DistinguishPlayer(Player player) {
		String Player_uuid = player.getUniqueId().toString();
		if (StroageUtils.SQLConnection.containsKey("brokenerdata", "brokener_uuid", Player_uuid)) {
			return UpdataPlayerPeaceState(player);
		}

		return true;
	}

	public static boolean UpdataPlayerPeaceState(Player player) {
		int TimeInterval = SetFiles.getConfig().getInt("SwitchPeaceStateInterval");
		String Player_uuid = player.getUniqueId().toString();
		Map<String, Object> Brokenerdata = StroageUtils.SQLConnection.select("brokenerdata", "brokener_uuid",
				Player_uuid);
		int peace_state = (Integer) Brokenerdata.get("peace_state");

		long switch_peace_date = (Long) Brokenerdata.get("switch_peace_date");
		long NewTime = new Date().getTime();
		long SwitchPeaceStateInterval = SetFiles.getConfig().getLong("SwitchPeaceStateInterval") * 1000;
		long LastTime = switch_peace_date + SwitchPeaceStateInterval - NewTime;
		if (player.isOp() || switch_peace_date == 0 || LastTime < 0) {
			if (player.isOp()) {
				Logger.SendToPlayer(player, "§c您是OP，不受切换和平间隔时间限制!");
			}
			if (peace_state == 1) {
				Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_close_peace"));
				peace_state = 0;
			} else {
				peace_state = 1;
				Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_enable_peace"));
			}

			StroageUtils.SQLConnection.updata("brokenerdata", "brokener_uuid", Player_uuid, "peace_state", peace_state);
			StroageUtils.SQLConnection.updata("brokenerdata", "brokener_uuid", Player_uuid, "switch_peace_date",
					NewTime);
			return true;

			// when_enable_peace when_close_peace
		}
		Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_Switching_interval").replace("$time",
				String.valueOf(LastTime / 1000)));
		return false;
	}

	//新玩家加入时，新建一条记录
	// 1 -> 和平
	// 0 -> 不和平
	public static void NewFilesOfPlayer(Player player) {
		String player_uuid = player.getUniqueId().toString();
		if (! StroageUtils.SQLConnection.containsKey("brokenerdata", "brokener_uuid", player_uuid)) {
			StroageUtils.SQLConnection.Insert("brokenerdata", player_uuid, 1, 0, 0);
		}
	}

	public static void NewFilesOfPlayer(String player_uuid) {
		if (! StroageUtils.SQLConnection.containsKey("brokenerdata", "brokener_uuid", player_uuid)) {
			StroageUtils.SQLConnection.Insert("brokenerdata", player_uuid, 1, 0, 0);
		}
	}

	//奖励成功破坏方块玩家
	public static void rewardSuccessBrokener(Player player, Block block, int AttackValue) {
		if (PlunderBox.PlunderCheck(block)) {
			PlunderBox.CompletePlunder(player, block);
			removeBrokenBlockRecord(block);
			return;
		}

		World world = player.getWorld();
		Location loc = player.getLocation();
		block.setType(Material.AIR);
		Collection<ItemStack> items = block.getDrops();
		Iterator<ItemStack> _iter = items.iterator();
		while (_iter.hasNext()) {
			ItemStack next = _iter.next();
			world.dropItem(loc, next);
		}
		BrokenerFeedback.sendActionbarWhenTheySuccessBroken(player, AttackValue);
		Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_success_breaking"));
		removeBrokenBlockRecord(block);
	}

	public static void removeBrokenBlockRecord(Block block) {
		if (PlunderBox.PlunderCheck(block)) return;
		StroageUtils.SQLConnection.delect("blockhealthdata", "location",
				new LocationSerialize(block.getLocation()).Serializedata2String());
	}

	//如果有方块被成功破坏了一次
	public static void beBrokenerOnce(Player player, Block block, ItemStack item) {
		String _LocationSerialize = new LocationSerialize(block.getLocation()).Serializedata2String();
		if (StroageUtils.SQLConnection.containsKey("blockhealthdata", "location", _LocationSerialize)) {
			UpdataBrokenBlock(player, block, item, _LocationSerialize);
		} else {
			NewFileBrokenBlockToSQLite(player, block, item, _LocationSerialize);
		}
	}

	//更新玩家破坏数据，已不必 使用此方法前请注意判断方块是否可以破坏以及使用武器有破坏力，数据库存在可更新数据！
	public static void UpdataBrokenBlock(Player player, Block block, ItemStack item, String _LocationSerialize) {
		AttackQualityMeta _attackqualitymeta = AttackValueLoreData.AccordingtoAttackValue(item);
		if (_attackqualitymeta == null || ! _attackqualitymeta.isHasAttackValue()) return; //item没有破坏力
		BlockBreakQualityMeta _blockbreakqualitymeta = AntiAttackValueBlockData.AccordingtoBreakQuality(block);
		if (_blockbreakqualitymeta == null || ! _blockbreakqualitymeta.isHasAntiAttackValue()) return; //block没有防御力
		Map<String, Object> data = StroageUtils.SQLConnection.select("blockhealthdata", "location", _LocationSerialize);
		if (data == null || data.isEmpty()) return; //sqlite不存在数据
		int AttackValue = _attackqualitymeta.getAttackValue();
		int SurplusHealth = (int) data.get("health") - AttackValue;
		int state = 0;
		if (SurplusHealth <= 0) state = 1;
		long lastbrokendate = new Date().getTime();
		String Brokener_uuid = null;
		if (state == 1) {
			Brokener_uuid = player.getUniqueId().toString();
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "health", 0);
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "last_broken_date",
					lastbrokendate);
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "state", 1);
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "brokener_uuid",
					Brokener_uuid);
			SQLUtils.rewardSuccessBrokener(player, block, AttackValue);
		} else if (state == 0) {
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "health",
					SurplusHealth);
			StroageUtils.SQLConnection._updata("blockhealthdata", "location", _LocationSerialize, "last_broken_date",
					lastbrokendate);

		}
		BrokenerFeedback.sendActionbar(player, _blockbreakqualitymeta.getAntiAttackValue(), AttackValue, SurplusHealth);
	}

	//第一次被破坏掉了触发方法
	//减了伤害值再存入数据库
	public static void NewFileBrokenBlockToSQLite(Player player, Block block, ItemStack item,
			String _LocationSerialize) {
		AttackQualityMeta _attackqualitymeta = AttackValueLoreData.AccordingtoAttackValue(item);
		if (_attackqualitymeta == null || ! _attackqualitymeta.isHasAttackValue()) return; //item没有破坏力
		BlockBreakQualityMeta _blockbreakqualitymeta = AntiAttackValueBlockData.AccordingtoBreakQuality(block);
		if (_blockbreakqualitymeta == null || ! _blockbreakqualitymeta.isHasAntiAttackValue()) return; //block没有防御力
		int AntiAttackValue = _blockbreakqualitymeta.getAntiAttackValue();
		int AttackValue = _attackqualitymeta.getAttackValue();
		int SurplusHealth = AntiAttackValue - AttackValue;
		int state = 0;
		if (SurplusHealth <= 0) state = 1;
		long lastbrokendate = new Date().getTime();
		String Brokener_uuid = null;
		if (state == 1) {
			Brokener_uuid = player.getUniqueId().toString();
			StroageUtils.SQLConnection.Insert("blockhealthdata", _LocationSerialize, 0, lastbrokendate, state,
					Brokener_uuid, 0);
			SQLUtils.rewardSuccessBrokener(player, block, AttackValue);
		} else if (state == 0) {
			StroageUtils.SQLConnection.Insert("blockhealthdata", _LocationSerialize, SurplusHealth, lastbrokendate,
					state, Brokener_uuid, 0);
		}
		BrokenerFeedback.sendActionbar(player, _blockbreakqualitymeta.getAntiAttackValue(), AttackValue, SurplusHealth);
	}

	//第一次被破坏掉了触发方法
	//减了伤害值再存入数据库
	//缺少Player参数
	public static void NewFileBrokenBlockToSQLite(Block block, ItemStack item) {
		AttackQualityMeta _attackqualitymeta = AttackValueLoreData.AccordingtoAttackValue(item);
		if (_attackqualitymeta == null || ! _attackqualitymeta.isHasAttackValue()) return; //item没有破坏力
		BlockBreakQualityMeta _blockbreakqualitymeta = AntiAttackValueBlockData.AccordingtoBreakQuality(block);
		if (_blockbreakqualitymeta == null || ! _blockbreakqualitymeta.isHasAntiAttackValue()) return; //block没有防御力
		int AntiAttackValue = _blockbreakqualitymeta.getAntiAttackValue();
		int AttackValue = _attackqualitymeta.getAttackValue();
		int SurplusHealth = AntiAttackValue - AttackValue;
		String _location = new LocationSerialize(block.getLocation()).Serializedata2String();
		int state = 0;
		if (SurplusHealth <= 0) state = 1;
		long lastbrokendate = new Date().getTime();
		String Brokener_uuid = null;
		if (state == 1) {
			StroageUtils.SQLConnection.Insert("blockhealthdata", _location, 0, lastbrokendate, state, Brokener_uuid, 0);
		} else if (state == 0) {
			StroageUtils.SQLConnection.Insert("blockhealthdata", _location, SurplusHealth, lastbrokendate, state,
					Brokener_uuid, 0);
		}

	}

	public static int getBlockHealth(Location loc) {
		Map<String, Object> data = readBlockbySQLite(loc);
		if (data == null) return - 1;
		return (int) data.get("health");
	}

	public static BlockState getBlockState(Location loc) {
		Map<String, Object> data = readBlockbySQLite(loc);
		if (data == null) return null;
		BlockState _blockstate = new BlockState();
		_blockstate.setExist(true);
		int plunderstate = (int) data.get("state");
		if (plunderstate == 1) _blockstate.setBeplunder(true);
		else _blockstate.setBeplunder(false);
		return _blockstate;
	}

	/**   
	isPeaceState   
	@Description:
	@param offlinePlayer
	@return     
	boolean*/
	public static boolean isPeaceState(OfflinePlayer offlinePlayer) {
		return false;
	}

}

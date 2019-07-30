/**
All rights Reserved, Designed By www.aug.cloud
BrokenerFeedback.java
@Package net.augcloud.arisa.saboteur.sqlite
@Description:
@author: Arisa
@date:   2019�?1�?27�? 上午8:54:19
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.sqlite;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.message_module.ActionAPILocalization;

/**
@author Arisa
@date 2019�?1�?27�? 上午8:54:19*/
public class BrokenerFeedback {

	private static String formatBrokenRateOfProgress(int MaxHealth, int SurplusHealth) {
		double Rate = Double.valueOf(SurplusHealth) / Double.valueOf(MaxHealth);

		String _Rate = String.valueOf(Rate * 100);
		if (_Rate.length() > 4) _Rate = _Rate.substring(0, 4);
		if (Rate >= 0) {
			if (Rate == 1) return "§a♥♥♥♥♥" + " §->" + _Rate + "%";
			if (Rate == 0.9) return "§e♥§a♥♥♥♥" + " §->" + _Rate + "%";
			if (Rate >= 0.8) return "§e♥♥§a♥♥♥" + " §->" + _Rate + "%";
			if (Rate >= 0.7) return "§e♥♥♥§a♥♥" + " §->" + _Rate + "%";
			if (Rate >= 0.6) return "§e♥♥♥♥§a♥" + " §->" + _Rate + "%";
			if (Rate >= 0.5) return "§e♥♥♥♥♥" + " §->" + _Rate + "%";

			if (Rate >= 0.4) return "§c♥§e♥♥♥♥" + " §->" + _Rate + "%";
			if (Rate >= 0.3) return "§c♥♥§e♥♥♥" + " §->" + _Rate + "%";
			if (Rate >= 0.2) return "§c♥♥♥§e♥♥" + " §->" + _Rate + "%";
			if (Rate < 0.1) return "§c♥♥♥♥§e♥" + " §->" + _Rate + "%";
			if (Rate <= 0) return "§c♥♥♥♥♥" + " §->" + _Rate + "%";
		}
		return "§c♥♥♥♥♥" + " §->" + _Rate + "%";
	}

	//当玩家成功破坏时
	public static void sendActionbarWhenTheySuccessBroken(Player player, int AttackValue) {
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
		ActionAPILocalization.sendActionMessage(player, SetFiles.getConfig().getString("when_be_block_breken")//占位
				.replace("$a", "§c♥♥♥♥♥") //剩余�?量比
				.replace("$c", String.valueOf(AttackValue)), true); //造成伤害
	}

	public static void sendActionbar(Player player, int MaxHealth, int AttackValue, int SurplusHealth) {
		player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
		ActionAPILocalization.sendActionMessage(player, SetFiles.getConfig().getString("when_be_block_breken")//占位
				.replace("$a", BrokenerFeedback.formatBrokenRateOfProgress(MaxHealth, SurplusHealth) //剩余�?量比
				).replace("$c", String.valueOf(AttackValue))); //造成伤害
	}

}

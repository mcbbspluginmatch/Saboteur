/**
All rights Reserved, Designed By www.aug.cloud
PlayerSafeModeManager.java
@Package net.augcloud.arisa.saboteur.behavior_instruction
@Description:
@author: Arisa
@date:   2019年7月27日 下午10:16:49
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
@author Arisa
@date 2019年7月27日 下午10:16:49*/
public class PlayerSafeModeManager {

	private final List<String> Players;

	/**
	PlayerSafeModeManager
	@Description:*/
	public PlayerSafeModeManager() {
		this.Players = new ArrayList<>();
	}

	public void openPlayerSafeMode(Player player) {
		this.Players.add(player.getName());
	}

	public void closePlayerSafeMode(Player player) {
		this.Players.remove(player.getName());
	}

	public boolean getSafeMode(Player player) {
		return this.Players.contains(player.getName());
	}

	public boolean getSafeMode(String player_name) {
		return this.Players.contains(player_name);
	}

}

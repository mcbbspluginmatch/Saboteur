/**  
All rights Reserved, Designed By www.aug.cloud
PluginData.java   
@Package net.augcloud.arisa.saboteur   
@Description: 
@author: Arisa   
@date:   2019年7月25日 下午11:31:52   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur;

import net.augcloud.arisa.saboteur.behavior_instruction.AntiAttackValueBlockData;
import net.augcloud.arisa.saboteur.behavior_instruction.AttackValueLoreData;
import net.augcloud.arisa.saboteur.behavior_instruction.GetPlayerPeaceStateCachepool;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerSafeModeManager;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.menu.MenuManager;

/**
@author Arisa
@date 2019年7月25日 下午11:31:52*/
public class PluginData extends Utils {
	public static Logger logger = new Logger();
	public static AttackValueLoreData AttackValueLoreData = null;
	public static AntiAttackValueBlockData AntiAttackValueBlockData = null;
	public static PlayerSafeModeManager PlayerSafeModeManager = null;
	public static GetPlayerPeaceStateCachepool GetPlayerPeaceStateCachepool = null;
	public static MenuManager MenuManager = null;
}

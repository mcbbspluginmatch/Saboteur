/**  
All rights Reserved, Designed By www.aug.cloud
Logger.java   
@Package net.augcloud.arisa.akits.logger   
@Description: 
@author: Arisa   
@date:   2018年7月23日 上午11:32:22   
@version V1.0 
@Copyright: 2018 
*/
package net.augcloud.arisa.saboteur.logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.augcloud.arisa.saboteur.SetFiles;

/**
@author Arisa
@date 2018年7月23日 上午11:32:22*/
public class Logger {

	private static String Plugin_Prefix = "";

	public static void loginit() {
		Plugin_Prefix = SetFiles.getConfig().getString("Plugin_Prefix").replaceAll("&", "§");
	}

	/**   
	Logger   
	@Description:  */
	public Logger() {
	}

	//发送消息带前缀办法
	public static void SendToPlayer(Player player, String msg) {
		player.sendMessage(Plugin_Prefix + msg.replaceAll("&", "§"));
	}

	public static void SendToAllPlayer(String msg) {
		Bukkit.broadcastMessage(Plugin_Prefix + msg.replaceAll("&", "§"));
	}

	public void println(String arg) {
		System.out.println(arg);
	}

	public void info(String arg) {
		System.out.println("Saboteur >> " + arg);
	}

	public void Error(String arg) {
		System.out.println("ERROR Saboteur >> " + arg);
	}

	/**   
	toString   
	@Description: 
	@return   
	@see java.lang.Object#toString()*/
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Logger [");

		builder.append("getClass()=");
		builder.append(getClass());
		builder.append(", ");

		builder.append("hashCode()=");
		builder.append(hashCode());
		builder.append(", ");
		if (super.toString() != null) {
			builder.append("toString()=");
			builder.append(super.toString());
		}
		builder.append("]");
		return builder.toString();
	}

}

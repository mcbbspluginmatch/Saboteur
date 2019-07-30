package net.augcloud.arisa.saboteur;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.augcloud.arisa.saboteur.api.PlotSquaredAPI;
import net.augcloud.arisa.saboteur.api.ResidenceAPI;
import net.augcloud.arisa.saboteur.behavior_instruction.GetPlayerPeaceStateCachepool;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerInteractListener;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerJoinListener;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerSafeModeHook;
import net.augcloud.arisa.saboteur.behavior_instruction.RewardNot_PeacePlayer;
import net.augcloud.arisa.saboteur.menu.MenuClickListener;
import net.augcloud.arisa.saboteur.menu.MenuCloseListener;
import net.augcloud.arisa.saboteur.menu.MenuManager;
import net.augcloud.arisa.saboteur.menu.OpenItemer;
import net.augcloud.arisa.saboteur.menu.PlayerQuitListener;
import net.augcloud.arisa.saboteur.menu.UtilsOfMenu;
import net.augcloud.arisa.saboteur.message_module.ActionbarAPI;
import net.augcloud.arisa.saboteur.sqlite.DatabaseClear;
import net.augcloud.arisa.saboteur.sqlite.StroageUtils;

public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin = null;
	public static boolean Debug = true;
	public static boolean useRes = false;
	public static boolean usePlot = false;
	public static boolean useOpen_Book = false;
	public static List<String> Res_noSaboteur = new ArrayList<>();
	public static List<String> Plot_noSaboteur = new ArrayList<>();
	public static boolean boom = false;

	@Override
	public void onEnable() {
		long startTime = System.nanoTime();
		if (! this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		plugin = this;
		try {
			SetFiles.RegConfig();
		} catch (Exception e) {
			// why make this exception?
			e.printStackTrace();
		} //加载配置

		if (useRes) {
			ResidenceAPI.resinit(); //加载resinence钩子
			ResidenceAPI.startMenuManagerThread();
		}
		if (usePlot) {
			PlotSquaredAPI.resinit();
			PlotSquaredAPI.startPlotSquaredAPIThread();
		}
		if (useOpen_Book) {
			OpenItemer.inits();
		}

		if (boom) { return; }

		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new MenuCloseListener(), this);
		Bukkit.getPluginManager().registerEvents(new MenuClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
		this.getCommand("Saboteur").setExecutor(new MainCmd());
		ActionbarAPI.APIInit();
		StroageUtils.init();
		UtilsOfMenu.MenuInits();

		PluginData.GetPlayerPeaceStateCachepool = new GetPlayerPeaceStateCachepool();
		new PapiVariableHook(plugin);
		DatabaseClear.startClear();
		PlayerSafeModeHook.startCheckThread();
		RewardNot_PeacePlayer.startRewardPlayer();
		MenuManager.startMenuManagerThread();

		getLogger().info("Plugin Enable now!");
		getLogger().info("Plugin version 1.0");
		getLogger().info("插件定制/Plug-in customization");
		getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
		getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
		final long endTime = System.nanoTime();
		getLogger().info("Loading completed!Used " + (endTime - startTime) / 1000000 + "ms");
	}

	@Override
	public void onDisable() {
		if (! boom) {
			DatabaseClear.closeClear();
			StroageUtils.SQLConnection.closeConnection();
			PluginData.GetPlayerPeaceStateCachepool.closeCachePool();
			PlayerSafeModeHook.stop();
			RewardNot_PeacePlayer.closeRewardPlayer();
			MenuManager.stop();

			if (useRes) {
				ResidenceAPI.stop();
			}

			if (usePlot) {
				PlotSquaredAPI.stop();
			}
		}

		HandlerList.unregisterAll((Listener) this);
		getLogger().info("Plugin disable now!");
		getLogger().info("插件定制/Plug-in customization");
		getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
		getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
	}

	public static boolean isNumber(char c) {
		String str = String.valueOf(c);
		return str != null && ! str.contains(" ") && ! str.equals("") ? str.matches("^[0-9]*$") : false;
	}

	public static boolean isNumber(String str) {
		return str != null && ! str.contains(" ") && ! str.equals("") ? str.matches("^[0-9]*$") : false;
	}

}

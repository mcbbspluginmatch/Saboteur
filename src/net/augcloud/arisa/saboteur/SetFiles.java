package net.augcloud.arisa.saboteur;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import net.augcloud.arisa.saboteur.behavior_instruction.AntiAttackValueBlockData;
import net.augcloud.arisa.saboteur.behavior_instruction.AttackValueLoreData;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerJoinListener;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerSafeModeManager;
import net.augcloud.arisa.saboteur.behavior_instruction.PlunderBox;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.menu.MenuClickListener;
import net.augcloud.arisa.saboteur.menu.MenuCloseListener;

public class SetFiles extends PluginData {

	protected static YamlConfiguration Settings = null;
	protected static YamlConfiguration ValueSetup = null;

	public static YamlConfiguration getConfig() {
		return Settings;
	}

	public static YamlConfiguration getValueSetup() {
		return ValueSetup;
	}

	public static void RegConfig() throws Exception {
		File DataFolder = Main.plugin.getDataFolder();
		File file = new File(DataFolder, "Settings.yml");
		File file1 = new File(DataFolder, "ValueSetup.yml");
		if (! file.exists()) {
			Main.plugin.saveResource("Settings.yml", true);
		}
		if (! file1.exists()) {
			Main.plugin.saveResource("ValueSetup.yml", true);
		}
		file = new File(DataFolder, "Settings.yml");
		file1 = new File(DataFolder, "ValueSetup.yml");

		Settings = FileConfig.loadConfiguration(file);
		ValueSetup = FileConfig.loadConfiguration(file1);
		if (! Settings.getString("Version").equals("1.1")) {
			Main.plugin.getLogger().info("抱歉，您的配置不是最新的，插件无法工作！");
			Main.plugin.getLogger().info("请备份原有配置再删除，让插件自行生成新版本的:)");
			Main.boom = true;
			Bukkit.getPluginManager().disablePlugin(Main.plugin);
			throw new Exception("Configuration version does not correspond : 配置版本不对应");
		}

		Main.useRes = Settings.getBoolean("Residence_Enabled");
		Main.usePlot = Settings.getBoolean("PlotSquared_Enabled");
		Main.useOpen_Book = Settings.getBoolean("Open_Book");
		Main.Plot_noSaboteur = Settings.getStringList("PlotSquared_blacklist");
		Main.Res_noSaboteur = Settings.getStringList("Residence_Blacklist");

		AttackValueLoreData = new AttackValueLoreData();
		AntiAttackValueBlockData = new AntiAttackValueBlockData();
		PlunderBox.PlunderRateInit();
		PlayerSafeModeManager = new PlayerSafeModeManager();
		Logger.loginit();

		PlayerJoinListener.inits();
		MenuClickListener.inits();
		MenuCloseListener.inits();
	}

	public static String getPlugin_Prefix() {
		return getConfig().getString("Plugin_Prefix").replaceAll("&", "§");
	}
}

package net.augcloud.arisa.saboteur;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.augcloud.arisa.saboteur.api.PlotSquaredAPI;
import net.augcloud.arisa.saboteur.behavior_instruction.PlayerSafeModeHook;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.menu.UtilsOfMenu;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;

public class MainCmd extends PluginData implements CommandExecutor {

	private static void sendMessage(final CommandSender sender, String msg) {
		boolean isPlayer = false;
		if (sender instanceof Player) {
			isPlayer = true;
		} else if (! (sender instanceof ConsoleCommandSender)) { return; }
		if (isPlayer) {
			sender.sendMessage(SetFiles.getPlugin_Prefix() + msg);
			return;
		}
		Main.plugin.getLogger().info(msg.replaceAll("§", "").replaceAll("a", ""));
	}

	private static void Help(final CommandSender sender) {
		boolean isPlayer = false;
		if (sender instanceof Player) {
			isPlayer = true;
		} else if (! (sender instanceof ConsoleCommandSender)) { return; }
		if (isPlayer) {
			sender.sendMessage(" §fSaboteur Used Help #Power by ?");
			sender.sendMessage("§7- §3/s §a显示全部命令");
			sender.sendMessage("§7- §3/s peace §a启用或关闭和平模式");
			sender.sendMessage("§7- §3/s safe <player> <time> §a给予玩家保护期");
			sender.sendMessage("§7- §3/s test §a给予一把测试稿子");
			sender.sendMessage("§7- §3/s menu §a打开掠夺菜单");
			sender.sendMessage("§7- §3/s reload §a重新加载插件");
			return;
		}
		Main.plugin.getLogger().info(" Saboteur Used Help #Power by Arisa");
		Main.plugin.getLogger().info("- /s 显示全部命令");
		Main.plugin.getLogger().info("- /s peace 启用或关闭和平模式");
		Main.plugin.getLogger().info("- /s safe <player> <time> 给予玩家保护期");
		Main.plugin.getLogger().info("- /s test 给予一把测试稿子");
		Main.plugin.getLogger().info("- /s menu 打开掠夺菜单");
		Main.plugin.getLogger().info("- /s reload 重新加载插件");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command myCommand, String lable, String[] args) {
		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {

				case "sound":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.admin")) {
					} else if (! (sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.admin 权限，或非控制台指令");
						return true;
					} {
					Player player = (Player) sender;
					player.playSound(player.getLocation(), Sound.valueOf(args[1]), 1, 1);
					return true;
				}
				case "debug":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.admin")) {
					} else if (! (sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.admin 权限，或非控制台指令");
						return true;
					} {
					Player player = (Player) sender;
					Logger.SendToPlayer(player, AntiAttackValueBlockData.toString());
					Logger.SendToPlayer(player, AttackValueLoreData.toString());
					Logger.SendToPlayer(player, PlayerSafeModeManager.toString());
				}
					return true;
				case "safe":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.admin")) {
					} else if (! (sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.admin 权限，或非控制台指令");
						return true;
					}
					if (args.length < 3) {
						sendMessage(sender, "使用/s safe <player> <time>指令参数必须全填");
						return true;
					} {

					PlayerSafeModeHook.openSafeMode(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
					//					if (FeedBack == 1) {
					//						Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_open_safemode"));
					//					} else {
					//						Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_close_safemode"));
					//					}
				}
					return true;
				case "peace":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.use")) {
					} else if ((sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.use 权限，或非控制台指令");
						return true;
					} {
					Player player = (Player) sender;
					SQLUtils.DistinguishPlayer(player);
				}
					return true;
				case "reload":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.admin")) {
						//双重条件
					} else if (! (sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.admin 权限");
						return true;
					}
					long startTime = System.nanoTime();
					try {
						SetFiles.RegConfig();
					} catch (Exception e) {
						// why make this exception?
						e.printStackTrace();
					}
					final long endTime = System.nanoTime();
					sendMessage(sender, "§a重载完成!耗时 " + (endTime - startTime) / 1000000 + "ms");
					return true;
				case "test":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.admin")) {
						//双重条件
					} else if (! (sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.admin 权限");
						return true;
					} {
					Player player = (Player) sender;
					ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
					List<String> Lore = new ArrayList<>();
					Lore.add("§f- §c破坏力: 10");
					ItemMeta ids = item.getItemMeta();
					ids.setLore(Lore);
					item.setItemMeta(ids);
					player.getInventory().addItem(item);
					PlotSquaredAPI.isPlots(player.getLocation());

				}
					return true;
				case "menu":
					if (sender instanceof Player && sender.isOp() || sender.hasPermission("Saboteur.use")) {
					} else if ((sender instanceof ConsoleCommandSender)) {
						sendMessage(sender, "执行此指令需 Saboteur.use 权限，或非控制台指令");
						return true;
					} {
					Player player = (Player) sender;
					UtilsOfMenu.OpenMenu(player);

				}

					return true;
				default:
					break;
			}
		}
		Help(sender);
		return true;
	}

	public String formatter(boolean args) {
		return args ? "成功" : "失败(不一定)";
	}

}

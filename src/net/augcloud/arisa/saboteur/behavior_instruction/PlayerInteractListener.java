/**  
All rights Reserved, Designed By www.aug.cloud
BlockBreakEvent.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月25日 下午11:38:43   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotId;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.api.PlotSquaredAPI;
import net.augcloud.arisa.saboteur.api.ResidenceAPI;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.menu.UtilsOfMenu;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;

/**
@author Arisa
@date 2019年7月25日 下午11:38:43*/
public class PlayerInteractListener extends PluginData implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void WhenPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			ItemStack _item = player.getItemInHand();
			if (_item == null || ! _item.hasItemMeta()) return;
			String item_name = _item.getItemMeta().getDisplayName();
			if (item_name.equalsIgnoreCase(_ReplaceColour(SetFiles.getConfig().getString("Open_BookName")))) {
				UtilsOfMenu.OpenMenu(player);
			}
		}
		Block block = e.getClickedBlock();
		if (block == null) { return; }
		if (Main.useRes) {

			ClaimedResidence Cres = ResidenceAPI.getClaimedResidence(block);

			if (Cres != null) {
				for (String res : Main.Res_noSaboteur) {
					if (Cres.getName().equalsIgnoreCase(res)) {
						Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_system_save"));
						return;
					}

				}
				boolean a = SQLUtils.isPeaceState(Bukkit.getOfflinePlayer(Cres.getOwner()).getUniqueId().toString());
				boolean b = SQLUtils.isPeaceState(player);
				boolean c = Cres.getOwner().equals(player.getName());
				boolean d = PlayerSafeModeManager.getSafeMode(Cres.getOwner());
				if (a) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break2"));
					return;
				}
				if (b) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break3"));
					return;
				}
				if (c) { return; }
				if (d) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break4"));
					return;
				}
				e.setCancelled(true);
				ItemStack item = player.getItemInHand();
				SQLUtils.beBrokenerOnce(player, block, item);
			}
		}

		if (Main.usePlot) {
			Plot plot = PlotSquaredAPI.getPlot(block);
			if (plot != null && plot.getOwner() != null) {
				PlotId pi = plot.getId();

				for (String plotId : Main.Plot_noSaboteur) {
					if (pi.x == Integer.parseInt(plotId.substring(0, plotId.indexOf(";") + 1))
							&& pi.y == Integer.parseInt(plotId.substring(plotId.indexOf(";") + 1))) {
						Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_system_save"));
						return;
					}

				}
				boolean a = SQLUtils.isPeaceState(Bukkit.getOfflinePlayer(plot.getOwner()).getUniqueId().toString());
				boolean b = SQLUtils.isPeaceState(player);
				boolean c = plot.getOwner().equals(player.getName());
				boolean d = PlayerSafeModeManager.getSafeMode(Bukkit.getOfflinePlayer(plot.getOwner()).getName());
				if (a) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break2"));
					return;
				}
				if (b) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break3"));
					return;
				}
				if (c) { return; }
				if (d) {
					Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_cant_break4"));
					return;
				}
				e.setCancelled(true);

				ItemStack item = player.getItemInHand();
				SQLUtils.beBrokenerOnce(player, block, item);

			}
		}

	}

}

/**
All rights Reserved, Designed By www.aug.cloud
MenuClickListener.java
@Package net.augcloud.arisa.saboteur.menu
@Description:
@author: Arisa
@date:   2019��7��29�� ����10:24:20
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;
import net.augcloud.arisa.saboteur.SetFiles;
import net.augcloud.arisa.saboteur.Utils;
import net.augcloud.arisa.saboteur.api.PlayerPlotSquared;
import net.augcloud.arisa.saboteur.api.PlayerResidence;
import net.augcloud.arisa.saboteur.api.PlotSquaredAPI;
import net.augcloud.arisa.saboteur.api.ResidenceAPI;
import net.augcloud.arisa.saboteur.logger.Logger;
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;

/**
@author Arisa
@date 2019��7��29�� ����10:24:20*/
public class MenuClickListener extends PluginData implements Listener {
	public static String Menu_title;
	private final int[] slots = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

	/**
	MenuClickListener
	@Description:*/
	public MenuClickListener() {
	}

	public static void inits() {
		MenuClickListener.Menu_title = Utils._ReplaceColour(SetFiles.getConfig().getString("menu_title"));
	}

	@EventHandler
	public void whenMenuClick(InventoryClickEvent e) {
		HumanEntity _player = e.getWhoClicked();
		if (! (_player instanceof Player)) return;
		Player player = (Player) _player;
		Inventory inv = e.getClickedInventory();
		if (inv == null) return;
		ItemStack item = inv.getItem(0);
		if ((item == null) || ! item.hasItemMeta()) return;
		ItemMeta ids = item.getItemMeta();
		if (ids.getDisplayName().equals("��")) {
			e.setCancelled(true);
			ItemStack CurrentItem = e.getCurrentItem();
			if ((CurrentItem == null) || (CurrentItem.hasItemMeta() == false)) return;
			String Item_Name = CurrentItem.getItemMeta().getDisplayName();
			if (Item_Name.equalsIgnoreCase("?")) return;
			if (Item_Name.equalsIgnoreCase("��һҳ")) {
				MenuSetup menuSetup = PluginData.MenuManager._get(player);
				if (menuSetup == null) {
					menuSetup = new MenuSetup(player);
					menuSetup.initOfMenu();
					PluginData.MenuManager.add(player, menuSetup);
					menuSetup = PluginData.MenuManager._get(player);
				}

				menuSetup.nextPage();
				PluginData.MenuManager.add(player, menuSetup);

				return;
			}

			if (Item_Name.equalsIgnoreCase("��һҳ")) {
				MenuSetup menuSetup = PluginData.MenuManager._get(player);
				if (menuSetup == null) {
					menuSetup = new MenuSetup(player);
					menuSetup.initOfMenu();
					PluginData.MenuManager.add(player, menuSetup);

					menuSetup = PluginData.MenuManager._get(player);
					PluginData.MenuManager.add(player, menuSetup);
				}

				menuSetup.lastPage();
				return;
			}

			if (Item_Name.equalsIgnoreCase("Ѱ���Ӷ����")) {

				ItemStack _item = null;
				ItemMeta _ids = null;
				List<String> lore = new ArrayList<>();

				Map<String, PlayerResidence> reses = ResidenceAPI._getResidences();
				item = new ItemStack(Material.LIME_STAINED_GLASS);
				Set<String> keys = reses.keySet();
				int j = 0;

				for (String Key : keys) {

					lore.clear();
					PlayerResidence pr = reses.get(Key);
					lore.add("��e-> ��aResidence���");
					lore.add(" ��7- ��3�� " + pr.getName());
					Location location = pr.getLoc();
					lore.add(" ��7- ��3���� ��a" + location.getWorld().getName());
					lore.add(" ��7- ��3��λ ��a" + location.getX() + " " + location.getZ());
					lore.add("");
					lore.add("");
					lore.add("��e-> ��a�ɷ��Ӷ�");
					boolean a = ! SQLUtils.isPeaceState(Bukkit.getOfflinePlayer(Key).getUniqueId().toString());
					boolean b = ! SQLUtils.isPeaceState(player);
					boolean c = ! PluginData.PlayerSafeModeManager.getSafeMode(Key);
					lore.add(" ��7- ��3�Է�����ƽ? " + MenuClickListener.format(a));
					lore.add(" ��7- ��3������ƽ? " + MenuClickListener.format(b));
					lore.add(" ��7- ��3�Է���������? " + MenuClickListener.format(c));

					if (! (a && b && c)) item = new ItemStack(Material.RED_STAINED_GLASS);
					lore.add("");
					lore.add("��7- ��3�Ӷ� " + MenuClickListener.format(a && b && c));
					lore.add("��7- ��a����ɴ��͹�ȥ");
					ids = item.getItemMeta();
					ids.setDisplayName("��a" + Key);
					ids.setLore(lore);
					item.setItemMeta(ids);
					inv.setItem(this.slots[j], item);
					j++ ;
				}

				if (Main.usePlot) {
					Map<String, PlayerPlotSquared> plots = PlotSquaredAPI.getPrs();
					item = new ItemStack(Material.EMERALD_BLOCK);
					Set<String> _keys = plots.keySet();

					for (String Key : _keys) {

						lore.clear();
						PlayerPlotSquared pp = plots.get(Key);
						lore.add("��e-> ��aPlotSquared��Ƥ");
						com.github.intellectualsites.plotsquared.plot.object.Location location = pp.getLoc();
						lore.add(" ��7- ��3���� ��a" + location.getWorld());
						lore.add(" ��7- ��3��λ ��a" + location.getX() + " " + location.getZ());
						lore.add("");
						lore.add("");
						lore.add("��e-> ��a�ɷ��Ӷ�");
						boolean a = ! SQLUtils.isPeaceState(Bukkit.getOfflinePlayer(Key).getUniqueId().toString());
						boolean b = ! SQLUtils.isPeaceState(player);
						boolean c = ! PluginData.PlayerSafeModeManager.getSafeMode(Key);
						lore.add(" ��7- ��3�Է�����ƽ? " + MenuClickListener.format(a));
						lore.add(" ��7- ��3������ƽ? " + MenuClickListener.format(b));
						lore.add(" ��7- ��3�Է���������? " + MenuClickListener.format(c));
						boolean d = a && b && c;
						//abcd����û�а취����������ʱ������������һЩ
						if (! d) item = new ItemStack(Material.REDSTONE_BLOCK);
						lore.add("");
						lore.add("��7- ��3�Ӷ� " + MenuClickListener.format(a && b && c));
						lore.add("��7- ��a����ɴ��͹�ȥ");
						ids = item.getItemMeta();
						ids.setDisplayName("��a��ء�Ƥ" + Key);
						ids.setLore(lore);
						item.setItemMeta(ids);
						inv.setItem(this.slots[j], item);
						j++ ;
					}
				}
				return;
			}
			if (Item_Name.equalsIgnoreCase("��ѯ��Ʒ")) return;

			if (Item_Name.equalsIgnoreCase("��")) {
				SQLUtils.DistinguishPlayer(player);
				player.closeInventory();
				PluginData.MenuManager.delete(player);
				return;
			}

			if (Item_Name.indexOf("��c") != - 1) return;
			if (Item_Name.indexOf("��a��ء�Ƥ") != - 1) {
				PlayerPlotSquared pp = PlotSquaredAPI.getPrs().get(Item_Name.replace("��a��ء�Ƥ", ""));
				player.teleport(new Location(Bukkit.getWorld(pp.getLoc().getWorld()), pp.getLoc().getX(),
						pp.getLoc().getY(), pp.getLoc().getZ()));
				Logger.SendToAllPlayer(SetFiles.getConfig().getString("when_their_gotosaboteur")
						.replace("%wanttosaboteur%", player.getName())
						.replace("%player_name%", Item_Name.replace("��a", "")));
				return;
			}
			Map<String, PlayerResidence> reses = ResidenceAPI._getResidences();

			PlayerResidence pr = reses.get(Item_Name.replace("��a", ""));
			if (pr == null) return;

			player.teleport(pr.getLoc());
			Logger.SendToPlayer(player, SetFiles.getConfig().getString("when_their_tploc"));
			Logger.SendToAllPlayer(SetFiles.getConfig().getString("when_their_gotosaboteur")
					.replace("%wanttosaboteur%", player.getName())
					.replace("%player_name%", Item_Name.replace("��a", "")));
			player.playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 1, 1);
		}
	}

	private static String format(Boolean b) {
		return b ? "��a��" : "��c��";
	}

}

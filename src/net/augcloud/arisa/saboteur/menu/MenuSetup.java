/**
All rights Reserved, Designed By www.aug.cloud
MenuSetup.java
@Package net.augcloud.arisa.saboteur.menu
@Description:
@author: Arisa
@date:   2019��7��29�� ����7:01:54
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import net.augcloud.arisa.saboteur.sqlite.SQLUtils;

/**
@author Arisa
@date 2019��7��29�� ����7:01:54*/
public class MenuSetup extends Utils {

	private Player player = null;
	private Inventory inv = null;
	//21��
	private int[] slots = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };
	private LinkedHashMap<Integer, ItemStack> items = new LinkedHashMap<>();
	private int page = 1;

	/**
	MenuSetup
	@Description: ���캯��*/
	public MenuSetup(Player _player) {
		this.player = _player;
	}

	public void open() {
		this.player.openInventory(this.inv);
	}

	public Inventory getInventory() {
		return this.inv;

	}

	public void initOfMenu() {
		List<ItemStack> OKitems = new ArrayList<>();
		List<ItemStack> Non_OKitems = new ArrayList<>();
		this.inv = Bukkit.createInventory(this.player, 45,
				super.ReplaceColour(SetFiles.getConfig().getString("menu_title")));

		ItemStack item = null;
		ItemMeta ids = null;
		List<String> lore = new ArrayList<>();

		//��ɫ����
		item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
		ids = item.getItemMeta();
		ids.setDisplayName("?");
		item.setItemMeta(ids);
		this.inv.setItem(1, item);
		this.inv.setItem(9, item);
		this.inv.setItem(44, item);
		this.inv.setItem(43, item);
		this.inv.setItem(35, item);

		//��һҳ/��һҳ
		item = new ItemStack(Material.RED_WOOL);
		ids = item.getItemMeta();
		ids.setDisplayName("��f��һҳ");
		item.setItemMeta(ids);
		this.inv.setItem(40, item);

		item = new ItemStack(Material.WHITE_WOOL);
		ids = item.getItemMeta();
		ids.setDisplayName("��f��һҳ");
		item.setItemMeta(ids);
		this.inv.setItem(39, item);

		//��
		item = new ItemStack(Material.DIAMOND_SWORD);
		ids = item.getItemMeta();
		ids.setDisplayName("��fѰ���Ӷ����");
		item.setItemMeta(ids);
		this.inv.setItem(42, item);

		//��ѯ����
		item = new ItemStack(Material.ENDER_CHEST);
		ids = item.getItemMeta();
		ids.setDisplayName("��f��ѯ��Ʒ");
		item.setItemMeta(ids);
		this.inv.setItem(41, item);

		//ͷ­
		item = new ItemStack(Material.PLAYER_HEAD);
		ids = item.getItemMeta();
		ids.setDisplayName("��f��");
		lore.add("��e-> " + this.player.getName());
		lore.add("  ��7- ��3��ƽ " + MenuSetup.format(SQLUtils.isPeaceState(this.player)));
		lore.add(
				"  ��7- ��3�ܱ��� " + MenuSetup.format(PluginData.PlayerSafeModeManager.getSafeMode(this.player.getName())));
		lore.add("");
		lore.add("");
		lore.add("  ��7- ��3�رպ�ƽ�����Ӷ�!");
		lore.add("  ��7- ��3����л���ƽģʽ");
		ids.setLore(lore);
		item.setItemMeta(ids);
		this.inv.setItem(0, item);

		if (Main.useRes) {
			Map<String, PlayerResidence> reses = ResidenceAPI._getResidences();
			item = new ItemStack(Material.LIME_STAINED_GLASS);
			Set<String> keys = reses.keySet();

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
				//abcd����û�а취����������ʱ������������һЩ
				boolean a = ! SQLUtils.isPeaceState(Bukkit.getOfflinePlayer(Key).getUniqueId().toString());
				boolean b = ! SQLUtils.isPeaceState(this.player);
				boolean c = ! PluginData.PlayerSafeModeManager.getSafeMode(Key);
				lore.add(" ��7- ��3�Է�����ƽ? " + MenuSetup.format(a));
				lore.add(" ��7- ��3������ƽ? " + MenuSetup.format(b));
				lore.add(" ��7- ��3�Է���������? " + MenuSetup.format(c));
				boolean d = a && b && c;
				if (! d) item = new ItemStack(Material.RED_STAINED_GLASS);
				lore.add("");
				lore.add("��7- ��3�Ӷ� " + MenuSetup.format(a && b && c));
				lore.add("��7- ��a����ɴ��͹�ȥ");
				ids = item.getItemMeta();
				ids.setDisplayName("��a" + Key);
				ids.setLore(lore);
				item.setItemMeta(ids);
				if (d) OKitems.add(item);
				else Non_OKitems.add(item);
				//				this.inv.setItem(this.slots[j], item);

			}

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
				boolean b = ! SQLUtils.isPeaceState(this.player);
				boolean c = ! PluginData.PlayerSafeModeManager.getSafeMode(Key);
				lore.add(" ��7- ��3�Է�����ƽ? " + MenuSetup.format(a));
				lore.add(" ��7- ��3������ƽ? " + MenuSetup.format(b));
				lore.add(" ��7- ��3�Է���������? " + MenuSetup.format(c));
				boolean d = a && b && c;
				//abcd����û�а취����������ʱ������������һЩ
				if (! d) item = new ItemStack(Material.REDSTONE_BLOCK);
				lore.add("");
				lore.add("��7- ��3�Ӷ� " + MenuSetup.format(a && b && c));
				lore.add("��7- ��a����ɴ��͹�ȥ");
				ids = item.getItemMeta();
				ids.setDisplayName("��a��ء�Ƥ" + Key);
				ids.setLore(lore);
				item.setItemMeta(ids);
				if (d) OKitems.add(item);
				else Non_OKitems.add(item);
				//				this.inv.setItem(this.slots[i], item);

			}
		}

		int i = 0;
		for (ItemStack _item : OKitems) {
			this.items.put(i, _item);
			i++ ;
		}
		for (ItemStack $item : Non_OKitems) {
			this.items.put(i, $item);
			i++ ;
		}
		int j = 0;
		Set<Entry<Integer, ItemStack>> sets = this.items.entrySet();
		for (Entry<Integer, ItemStack> set : sets) {
			if (j >= 20) return;
			ItemStack __item = set.getValue();
			this.inv.setItem(this.slots[j], __item);
			j++ ;
		}
	}

	//��Ȼ��һ͵���ͻ�д�����õĴ��밡
	public void lastPage() {
		this.setPage(this.getPage() - 1);
		for (int i = 0, size = 21; i < size; i++ ) {

			if (((this.getPage() * 21) - i) < 0) return;

			this.inv.setItem(this.slots[i], this.items.get((this.getPage() * 21) - i));
		}
	}

	public void nextPage() {
		this.setPage(this.getPage() + 1);
		for (int i = 0, size = 21; i < size; i++ ) {
			if (this.items.size() > i) return;
			if (((this.getPage() * 21) + i) >= this.items.size()) return;

			this.inv.setItem(this.slots[i], this.items.get(i + (this.getPage() * 21)));
		}
	}

	private static String format(Boolean b) {
		return b ? "��a��" : "��c��";
	}

	/**
	getPage
	@Description: �ⲿ��Աͨ��getter����getPage�ֶ�
	@return: int*/
	public int getPage() {
		return this.page;
	}

	/**
	@Title:  setPage
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: int*/
	public void setPage(int page) {
		this.page = page;
	}

	/**
	getPlayer
	@Description: �ⲿ��Աͨ��getter����getPlayer�ֶ�
	@return: Player*/
	public Player getPlayer() {
		return this.player;
	}

	/**
	@Title:  setPlayer
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: Player*/
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	getInv
	@Description: �ⲿ��Աͨ��getter����getInv�ֶ�
	@return: Inventory*/
	public Inventory getInv() {
		return this.inv;
	}

	/**
	@Title:  setInv
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: Inventory*/
	public void setInv(Inventory inv) {
		this.inv = inv;
	}

	/**
	getSlots
	@Description: �ⲿ��Աͨ��getter����getSlots�ֶ�
	@return: int[]*/
	public int[] getSlots() {
		return this.slots;
	}

	/**
	@Title:  setSlots
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: int[]*/
	public void setSlots(int[] slots) {
		this.slots = slots;
	}

	/**
	getItems
	@Description: �ⲿ��Աͨ��getter����getItems�ֶ�
	@return: LinkedHashMap<Integer,ItemStack>*/
	public LinkedHashMap<Integer, ItemStack> getItems() {
		return this.items;
	}

	/**
	@Title:  setItems
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: LinkedHashMap<Integer,ItemStack>*/
	public void setItems(LinkedHashMap<Integer, ItemStack> items) {
		this.items = items;
	}

	/**
	hashCode
	@Description:
	@return
	@see java.lang.Object#hashCode()*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.inv == null) ? 0 : this.inv.hashCode());
		result = (prime * result) + ((this.items == null) ? 0 : this.items.hashCode());
		result = (prime * result) + this.page;
		result = (prime * result) + ((this.player == null) ? 0 : this.player.hashCode());
		result = (prime * result) + Arrays.hashCode(this.slots);
		return result;
	}

	/**
	equals
	@Description:
	@param obj
	@return
	@see java.lang.Object#equals(java.lang.Object)*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof MenuSetup)) return false;
		MenuSetup other = (MenuSetup) obj;
		if (this.inv == null) {
			if (other.inv != null) return false;
		} else if (! this.inv.equals(other.inv)) return false;
		if (this.items == null) {
			if (other.items != null) return false;
		} else if (! this.items.equals(other.items)) return false;
		if (this.page != other.page) return false;
		if (this.player == null) {
			if (other.player != null) return false;
		} else if (! this.player.equals(other.player)) return false;
		if (! Arrays.equals(this.slots, other.slots)) return false;
		return true;
	}

	/**
	toString
	@Description:
	@return
	@see java.lang.Object#toString()*/
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuSetup [");
		if (this.player != null) {
			builder.append("player=");
			builder.append(this.player);
			builder.append(", ");
		}
		if (this.inv != null) {
			builder.append("inv=");
			builder.append(this.inv);
			builder.append(", ");
		}
		if (this.slots != null) {
			builder.append("slots=");
			builder.append(Arrays.toString(this.slots));
			builder.append(", ");
		}
		if (this.items != null) {
			builder.append("items=");
			builder.append(this.items);
			builder.append(", ");
		}
		builder.append("page=");
		builder.append(this.page);
		builder.append("]");
		return builder.toString();
	}
}

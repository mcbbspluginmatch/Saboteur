/**
All rights Reserved, Designed By www.aug.cloud
PlotSquaredAPI.java
@Package net.augcloud.arisa.saboteur.api
@Description:
@author: Arisa
@date:   2019年7月30日 下午3:07:54
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.intellectualsites.plotsquared.plot.PlotSquared;
import com.github.intellectualsites.plotsquared.plot.object.Plot;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月30日 下午3:07:54*/
public class PlotSquaredAPI extends PluginData {

	private static PlotSquared plots;
	public static boolean stop = false;
	//kv:玩家名字,rp
	private static Map<String, PlayerPlotSquared> prs = new HashMap<>();

	public static void stop() {
		PlotSquaredAPI.stop = true;
		PluginData.logger.info("PlotSquaredAPI线程 已关闭!");
	}

	public static void startPlotSquaredAPIThread() {
		new BukkitRunnable() {
			@Override
			public void run() {

				PlotSquaredAPI.Updatadata();
				if (PlotSquaredAPI.stop) this.cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 12000);
		PluginData.logger.info("PlotSquaredAPI线程 已启动!");
	}

	protected static void Updatadata() {
		Set<Plot> _plots = plots.getPlots();
		for (Plot plot : _plots) {
			PlayerPlotSquared pp = new PlayerPlotSquared(Bukkit.getOfflinePlayer(plot.getOwner()).getName(),
					plot.getDefaultHome());
			prs.put(Bukkit.getOfflinePlayer(plot.getOwner()).getName(), pp);

		}
	}

	/**
	PlotSquaredAPI
	@Description:*/
	public PlotSquaredAPI() {

	}

	public static void resinit() {
		PlotSquaredAPI.plots = PlotSquared.get();
	}

	public static boolean isPlots(Location loc) {
		com.github.intellectualsites.plotsquared.plot.object.Location _loc = new com.github.intellectualsites.plotsquared.plot.object.Location(
				loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		Plot plot = _loc.getPlot();
		return plot != null;
	}

	public static Plot getPlot(Location loc) {
		com.github.intellectualsites.plotsquared.plot.object.Location _loc = new com.github.intellectualsites.plotsquared.plot.object.Location(
				loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		return _loc.getPlot();
	}

	public static Plot getPlot(Block block) {
		Location loc = block.getLocation();
		com.github.intellectualsites.plotsquared.plot.object.Location _loc = new com.github.intellectualsites.plotsquared.plot.object.Location(
				loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		return _loc.getPlot();
	}

	//是否可以建造
	// 0 -> 不在地皮
	// 1 -> 可以破坏
	// 2 -> 自己的地皮地，无需往下

	// 3 -> 因为和平状态，无法破坏
	// 4 -> 因为保护期，无法破坏

	public static int PlotCanBreken(Player player) {
		Plot plot = PlotSquaredAPI.getPlot(player.getLocation());
		if (plot == null) return 0;
		String PlayerName = player.getName();
		if (plot.getOwner().equals(PlayerName)) return 2;

		return 1;
	}

	/**
	getPlots
	@Description: 外部成员通过getter访问getPlots字段
	@return: PlotSquared*/
	public static PlotSquared getPlots() {
		return PlotSquaredAPI.plots;
	}

	/**
	@Title:  setPlots
	@Description: 外部成员通过setter方法修改字段
	@return: PlotSquared*/
	public static void setPlots(PlotSquared plots) {
		PlotSquaredAPI.plots = plots;
	}

	/**
	toString
	@Description:
	@return
	@see java.lang.Object#toString()*/
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlotSquaredAPI [");
		if (this.getClass() != null) {
			builder.append("getClass()=");
			builder.append(this.getClass());
			builder.append(", ");
		}
		builder.append("hashCode()=");
		builder.append(this.hashCode());
		builder.append(", ");
		if (super.toString() != null) {
			builder.append("toString()=");
			builder.append(super.toString());
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	isStop
	@Description: 外部成员通过getter访问isStop字段
	@return: boolean*/
	public static boolean isStop() {
		return PlotSquaredAPI.stop;
	}

	/**
	@Title:  setStop
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public static void setStop(boolean stop) {
		PlotSquaredAPI.stop = stop;
	}

	/**  
	getPrs 
	@Description: 外部成员通过getter访问getPrs字段
	@return: Map<String,PlayerPlotSquared>*/
	public static Map<String, PlayerPlotSquared> getPrs() {
		return prs;
	}

	/**  
	@Title:  setPrs
	@Description: 外部成员通过setter方法修改字段
	@return: Map<String,PlayerPlotSquared>*/
	public static void setPrs(Map<String, PlayerPlotSquared> prs) {
		PlotSquaredAPI.prs = prs;
	}

}

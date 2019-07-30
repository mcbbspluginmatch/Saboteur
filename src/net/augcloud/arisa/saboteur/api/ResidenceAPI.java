/**
All rights Reserved, Designed By www.aug.cloud
ResidenceAPI.java
@Package net.augcloud.arisa.saboteur.behavior_instruction
@Description:
@author: Arisa
@date:   2019年7月26日 下午12:27:15
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.RandomLoc;
import com.bekvon.bukkit.residence.listeners.ResidencePlayerListener;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;

import net.augcloud.arisa.saboteur.Main;
import net.augcloud.arisa.saboteur.PluginData;

/**
@author Arisa
@date 2019年7月26日 下午12:27:15*/
public class ResidenceAPI extends PluginData {

	private static Residence _Residence = null;
	public static boolean stop = false;

	public static void stop() {
		ResidenceAPI.stop = true;
		PluginData.logger.info("ResidenceAPI线程 已关闭!");
	}

	public static void startMenuManagerThread() {
		new BukkitRunnable() {
			@Override
			public void run() {

				ResidenceAPI.Updatadata();
				if (ResidenceAPI.stop) this.cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 12000);
		PluginData.logger.info("ResidenceAPI线程 已启动!");
	}

	public static void resinit() {
		ResidenceAPI._Residence = Residence.getInstance();
	}

	//kv:玩家名字,rp
	public static Map<String, PlayerResidence> prs = new HashMap<>();

	private static CuboidArea _getAreaByLoc(Location loc, ClaimedResidence _res) {
		Map<String, CuboidArea> areas = null;
		Class<? extends ClaimedResidence> clazz = _res.getClass();
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {

				field.setAccessible(true);
				if (field.getName().equalsIgnoreCase("areas")) areas = (Map<String, CuboidArea>) field.get(_res);
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// why make this exception?
			e.printStackTrace();
		}

		if (areas != null) for (CuboidArea thisarea : areas.values())
			if (thisarea.containsLoc(loc)) return thisarea;
		return null;
	}

	private static Location getMiddleFreeLoc(Location insideLoc, ClaimedResidence _res) {
		CuboidArea area = ResidenceAPI._getAreaByLoc(insideLoc, _res);
		if (area == null) return insideLoc;

		int y = area.getHighLoc().getBlockY();
		int lowY = area.getLowLoc().getBlockY();

		int x = area.getLowLoc().getBlockX() + (area.getXSize() / 2);
		int z = area.getLowLoc().getBlockZ() + (area.getZSize() / 2);

		Location newLoc = new Location(area.getWorld(), x + 0.5, y, z + 0.5);
		boolean found = false;
		int it = 1;
		int maxIt = newLoc.getBlockY() + 1;
		while (it < maxIt) {
			it++ ;

			if (newLoc.getBlockY() < lowY) break;

			newLoc.add(0, - 1, 0);

			Block block = newLoc.getBlock();
			Block block2 = newLoc.clone().add(0, 1, 0).getBlock();
			Block block3 = newLoc.clone().add(0, - 1, 0).getBlock();
			if (ResidencePlayerListener.isEmptyBlock(block) && ResidencePlayerListener.isEmptyBlock(block2)
					&& ! ResidencePlayerListener.isEmptyBlock(block3)) {
				found = true;
				break;
			}
		}
		return ResidenceAPI.getOutsideFreeLoc(insideLoc, _res);
	}

	private static Location getOutsideFreeLoc(Location insideLoc, ClaimedResidence _res) {
		CuboidArea area = ResidenceAPI._getAreaByLoc(insideLoc, _res);
		if (area == null) return insideLoc;

		List<RandomLoc> randomLocList = new ArrayList<>();

		for (int z = - 1; z < (area.getZSize() + 1); z++ ) {
			randomLocList.add(new RandomLoc(area.getLowLoc().getX(), 0, area.getLowLoc().getZ() + z));
			randomLocList.add(new RandomLoc(area.getLowLoc().getX() + area.getXSize(), 0, area.getLowLoc().getZ() + z));
		}

		for (int x = - 1; x < (area.getXSize() + 1); x++ ) {
			randomLocList.add(new RandomLoc(area.getLowLoc().getX() + x, 0, area.getLowLoc().getZ()));
			randomLocList.add(new RandomLoc(area.getLowLoc().getX() + x, 0, area.getLowLoc().getZ() + area.getZSize()));
		}

		Location loc = insideLoc.clone();

		boolean found = false;
		int it = 0;
		int maxIt = 30;
		while (! found && (it < maxIt)) {
			it++ ;

			Random ran = new Random(System.currentTimeMillis());
			if (randomLocList.isEmpty()) break;
			int check = ran.nextInt(randomLocList.size());
			RandomLoc place = randomLocList.get(check);
			randomLocList.remove(check);
			double x = place.getX();
			double z = place.getZ();

			loc.setX(x);
			loc.setZ(z);
			loc.setY(area.getHighLoc().getBlockY());

			int max = area.getHighLoc().getBlockY();
			max = loc.getWorld().getEnvironment() == Environment.NETHER ? 100 : max;

			for (int i = max; i > area.getLowLoc().getY(); i-- ) {
				loc.setY(i);
				Block block = loc.getBlock();
				Block block2 = loc.clone().add(0, 1, 0).getBlock();
				Block block3 = loc.clone().add(0, - 1, 0).getBlock();
				if (! ResidencePlayerListener.isEmptyBlock(block3) && ResidencePlayerListener.isEmptyBlock(block)
						&& ResidencePlayerListener.isEmptyBlock(block2)) break;
			}
		}
		return loc;

	}

	private static Location getTeleportLocation(ClaimedResidence reses) {
		CuboidArea MainArea = reses.getMainArea();
		if (MainArea == null) return null;
		Location low = MainArea.getLowLoc();
		Location high = MainArea.getHighLoc();
		Location t = new Location(low.getWorld(), (low.getBlockX() + high.getBlockX()) / 2,
				(low.getBlockY() + high.getBlockY()) / 2, (low.getBlockZ() + high.getBlockZ()) / 2);
		return ResidenceAPI.getMiddleFreeLoc(t, reses);
	}

	protected static void Updatadata() {
		Map<String, ClaimedResidence> reses = ResidenceAPI.getResidences();
		Set<Entry<String, ClaimedResidence>> resessets = reses.entrySet();
		for (Entry<String, ClaimedResidence> res : resessets) {
			String res_name = res.getKey();
			ClaimedResidence _res = res.getValue();
			String player_name = _res.getOwner();

			ResidenceAPI.prs.put(player_name,
					new PlayerResidence(ResidenceAPI.getTeleportLocation(_res), _res.getName()));

			//貌似不行
			//			CuboidArea MainArea = _res.getMainArea();
			//			Location loc = new Location(MainArea.getWorld(), MainArea.getXSize(), MainArea.getYSize(),
			//					MainArea.getZSize());
			//			System.out.println(loc);

			//			//反射，神tm tpLoc 居然只是个摆设，浪费我时间
			//			Class clazz = _res.getClass();
			//			try {
			//				Field[] fields = clazz.getDeclaredFields();
			//				for (Field field : fields) {
			//
			//					field.setAccessible(true);
			//					if (field.getName().equalsIgnoreCase("tpLoc")) {
			//						System.out.println(field.get(_res));
			//					}
			//				}
			//			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			//				// why make this exception?
			//				e.printStackTrace();
			//			}

		}

	}

	public static Map<String, PlayerResidence> _getResidences() {
		return ResidenceAPI.prs;
	}

	private static Map<String, ClaimedResidence> getResidences() {
		return ResidenceAPI._Residence.getResidenceManager().getResidences();
	}

	//是否可以建造
	// 0 -> 不在领地
	// 1 -> 可以破坏
	// 2 -> 自己的领地，无需往下

	// 3 -> 因为和平状态，无法破坏s
	// 4 -> 因为保护期，无法破坏

	public static int ResidentCanBreken(Player player) {
		ClaimedResidence claimedresidence = ResidenceAPI.getClaimedResidence(player);
		if (claimedresidence == null) return 0;
		String PlayerName = player.getName();
		if (claimedresidence.getOwner().equals(PlayerName)) return 2;

		return 1;
	}

	public static ClaimedResidence getClaimedResidence(Block block) {
		return ResidenceAPI._Residence.getResidenceManager().getByLoc(block.getLocation());
	}

	//玩家是否在领地中
	public static ClaimedResidence getClaimedResidence(Player player) {
		return ResidenceAPI._Residence.getResidenceManager().getByLoc(player.getLocation());
	}

}

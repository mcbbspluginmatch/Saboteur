package net.augcloud.arisa.saboteur.message_module;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.augcloud.arisa.saboteur.Main;

public class ActionbarAPI {
	private static String nmsver;
	private static boolean useOldMethods = false;

	public static void APIInit() {

		ActionbarAPI.nmsver = Bukkit.getServer().getClass().getPackage().getName();
		ActionbarAPI.nmsver = ActionbarAPI.nmsver.substring(ActionbarAPI.nmsver.lastIndexOf(".") + 1);

		if (ActionbarAPI.nmsver.equalsIgnoreCase("v1_8_R1")
				|| ActionbarAPI.nmsver.startsWith("v1_7_")) ActionbarAPI.useOldMethods = true;

	}

	public static void sendActionBar(Player player, String message) {
		if (! player.isOnline()) return;

		try {
			Class<?> craftPlayerClass = Class
					.forName("org.bukkit.craftbukkit." + ActionbarAPI.nmsver + ".entity.CraftPlayer");
			Object craftPlayer = craftPlayerClass.cast(player);
			Object packet;
			Class<?> packetPlayOutChatClass = Class
					.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".PacketPlayOutChat");
			Class<?> packetClass = Class.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".Packet");
			if (ActionbarAPI.useOldMethods) {
				Class<?> chatSerializerClass = Class
						.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".ChatSerializer");
				Class<?> iChatBaseComponentClass = Class
						.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".IChatBaseComponent");
				Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
				Object cbc = iChatBaseComponentClass
						.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
				packet = packetPlayOutChatClass.getConstructor(new Class<?>[] { iChatBaseComponentClass, byte.class })
						.newInstance(cbc, (byte) 2);
			} else {
				Class<?> chatComponentTextClass = Class
						.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".ChatComponentText");
				Class<?> iChatBaseComponentClass = Class
						.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".IChatBaseComponent");
				try {
					Class<?> chatMessageTypeClass = Class
							.forName("net.minecraft.server." + ActionbarAPI.nmsver + ".ChatMessageType");
					Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
					Object chatMessageType = null;
					for (Object obj : chatMessageTypes)
						if (obj.toString().equals("GAME_INFO")) chatMessageType = obj;
					Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[] { String.class })
							.newInstance(message);
					packet = packetPlayOutChatClass
							.getConstructor(new Class<?>[] { iChatBaseComponentClass, chatMessageTypeClass })
							.newInstance(chatCompontentText, chatMessageType);
				} catch (ClassNotFoundException cnfe) {
					Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[] { String.class })
							.newInstance(message);
					packet = packetPlayOutChatClass
							.getConstructor(new Class<?>[] { iChatBaseComponentClass, byte.class })
							.newInstance(chatCompontentText, (byte) 2);
				}
			}
			Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
			Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
			Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
			Object playerConnection = playerConnectionField.get(craftPlayerHandle);
			Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
			sendPacketMethod.invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendActionBar(final Player player, final String message, int duration) {
		ActionbarAPI.sendActionBar(player, message);
		if (duration >= 0) new BukkitRunnable() {
			@Override
			public void run() {
				ActionbarAPI.sendActionBar(player, "");
			}

		}.runTaskLater(Main.plugin, duration + 1);

		while (duration > 10) {
			duration -= 10;
			new BukkitRunnable() {
				@Override
				public void run() {
					ActionbarAPI.sendActionBar(player, message);
				}
			}.runTaskLater(Main.plugin, duration);
		}
	}

	public static void sendActionBarToAllPlayers(String message) {
		ActionbarAPI.sendActionBarToAllPlayers(message, - 1);
	}

	public static void sendActionBarToAllPlayers(String message, int duration) {
		for (Player p : Bukkit.getOnlinePlayers())
			ActionbarAPI.sendActionBar(p, message, duration);
	}
}
package me.Warper.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpsData {
	Warper plugin;

	Inventory mainMenu;
	WarpsList globalWarps;
	Warp spawn;
	HashMap<UUID, WarpsList> privateWarps = new HashMap<UUID, WarpsList>();

	public FileConfiguration warpsFileConfig;
	public File warpsFile;

	public WarpsData(Warper plugin) {
		this.plugin = plugin;
		globalWarps = new WarpsList(plugin);

		ItemStack[] menuItems = new ItemStack[27];
		ItemMeta meta;

		// Global warps item
		ItemStack globalWarps = new ItemStack(Material.OAK_HANGING_SIGN);
		meta = globalWarps.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Global Warps");
		globalWarps.setItemMeta(meta);
		menuItems[10] = globalWarps;
		// -------------

		// Private Warps
		ItemStack privateWarps = new ItemStack(Material.ACACIA_HANGING_SIGN);
		meta = privateWarps.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Private Warps");
		privateWarps.setItemMeta(meta);
		menuItems[13] = privateWarps;
		// -------------

		// Spawn
		ItemStack spawn = new ItemStack(Material.PALE_OAK_HANGING_SIGN);
		meta = spawn.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Spawn");
		spawn.setItemMeta(meta);
		menuItems[16] = spawn;
		// -------------

		// Close Menu
		ItemStack close = new ItemStack(Material.BARRIER);
		meta = close.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		close.setItemMeta(meta);
		menuItems[22] = close;

		ItemStack backgroundIcon = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		ItemMeta backgroundMeta = backgroundIcon.getItemMeta();
		backgroundMeta.setDisplayName("");
		backgroundIcon.setItemMeta(backgroundMeta);

		for (int i = 18; i < 27; i++) {
			if (i != 22) {
				menuItems[i] = backgroundIcon.clone();
			}
		}

		mainMenu = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Warper Menu");
		mainMenu.setContents(menuItems);
	}

	public void saveSpawnWarp() {
		if (spawn != null) {
			warpsFileConfig.set("Spawn-Warp." + "world", spawn.worldName);
			warpsFileConfig.set("Spawn-Warp." + "x", spawn.x);
			warpsFileConfig.set("Spawn-Warp." + "y", spawn.y);
			warpsFileConfig.set("Spawn-Warp." + "z", spawn.z);
			warpsFileConfig.set("Spawn-Warp." + "yaw", spawn.yaw);
			warpsFileConfig.set("Spawn-Warp." + "pitch", spawn.pitch);
			warpsFileConfig.set("Spawn-Warp." + "icon", spawn.icon.toString());
		} else {
			warpsFileConfig.set("Spawn-Warp", null);
		}

		saveWarpsData();
	}

	public void saveGlobalWarps() {
		for (Warp warp : globalWarps.warps) {
			String path = "Global-Warps." + warp.warpName;
			warpsFileConfig.set(path + ".world", warp.worldName);
			warpsFileConfig.set(path + ".x", warp.x);
			warpsFileConfig.set(path + ".y", warp.y);
			warpsFileConfig.set(path + ".z", warp.z);
			warpsFileConfig.set(path + ".yaw", warp.yaw);
			warpsFileConfig.set(path + ".pitch", warp.pitch);
			warpsFileConfig.set(path + ".icon", warp.icon.toString());
		}

		saveWarpsData();
	}

	public void savePlayerWarps(UUID playerID) {
		WarpsList warpsList = getPrivateWarps(playerID);

		for (Warp warp : warpsList.warps) {
			String path = "Private-Warps." + playerID + "." + warp.warpName;
			warpsFileConfig.set(path + ".world", warp.worldName);
			warpsFileConfig.set(path + ".x", warp.x);
			warpsFileConfig.set(path + ".y", warp.y);
			warpsFileConfig.set(path + ".z", warp.z);
			warpsFileConfig.set(path + ".yaw", warp.yaw);
			warpsFileConfig.set(path + ".pitch", warp.pitch);
			warpsFileConfig.set(path + ".icon", warp.icon.toString());
		}

		saveWarpsData();
	}

	public void saveWarpsData() {
		try {
			warpsFileConfig.save(warpsFile);
		} catch (IOException e) {
			plugin.getServer().getLogger().info(ChatColor.RED + "Could not save warps.yml file");
		}
	}

	public WarpsList getPrivateWarps(UUID playerID) {
		return privateWarps.get(playerID);
	}

	public boolean teleportToSpawn(Player player) {
		if (spawn == null) {
			return false;
		}

		Location location = new Location(plugin.getServer().getWorld(spawn.worldName), spawn.x, spawn.y, spawn.z,
				spawn.yaw, spawn.pitch);
		player.teleport(location);
		return true;
	}

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		warpsFile = new File(plugin.getDataFolder(), "warps.yml");

		if (!warpsFile.exists()) {
			try {
				warpsFile.createNewFile();
			} catch (IOException e) {
				plugin.getServer().getLogger().info(ChatColor.RED + "Could not create warps.yml file");
			}
		}

		warpsFileConfig = YamlConfiguration.loadConfiguration(warpsFile);
		loadWarps();
	}

	private void loadWarps() {
		// ---Global Warps
		Set<String> warps;
		try {
			warps = warpsFileConfig.getConfigurationSection("Global-Warps").getKeys(false);
		} catch (NullPointerException e) {
			warps = null;
		}

		if (warps != null) {
			for (String warp : warps) {
				String path = "Global-Warps." + warp;

				String world = warpsFileConfig.getString(path + ".world");
				double x = warpsFileConfig.getDouble(path + ".x");
				double y = warpsFileConfig.getDouble(path + ".y");
				double z = warpsFileConfig.getDouble(path + ".z");
				float yaw = (float) warpsFileConfig.getDouble(path + ".yaw");
				float pitch = (float) warpsFileConfig.getDouble(path + ".pitch");
				Material icon;
				try {
					icon = Material.valueOf(warpsFileConfig.getString(path + ".icon"));
				} catch (IllegalArgumentException e) {
					icon = plugin.defaultIcon;
				}
				globalWarps.addWarp(new Warp(warp, world, x, y, z, yaw, pitch, icon));
			}
		}

		Set<String> uuids;

		// ---Private warps
		try {
			uuids = warpsFileConfig.getConfigurationSection("Private-Warps").getKeys(false);
		} catch (NullPointerException e) {
			uuids = null;
		}

		if (uuids != null) { // No private warps
			for (String uuidString : uuids) { // For each players warps
				UUID uuid = UUID.fromString(uuidString);
				privateWarps.put(UUID.fromString(uuidString), new WarpsList(plugin));
				try {
					warps = warpsFileConfig.getConfigurationSection("Private-Warps." + uuidString).getKeys(false);
				} catch (NullPointerException e) {
					warps = null;
				}

				if (warps != null) { // If player has warps
					for (String warpName : warps) { // for each warp
						String path = "Private-Warps." + uuidString + "." + warpName;

						String world = warpsFileConfig.getString(path + ".world");
						double x = warpsFileConfig.getDouble(path + ".x");
						double y = warpsFileConfig.getDouble(path + ".y");
						double z = warpsFileConfig.getDouble(path + ".z");
						float yaw = (float) warpsFileConfig.getDouble(path + ".yaw");
						float pitch = (float) warpsFileConfig.getDouble(path + ".pitch");
						Material icon;
						try {
							icon = Material.valueOf(warpsFileConfig.getString(path + ".icon"));
						} catch (IllegalArgumentException e) {
							icon = plugin.defaultIcon;
						}

						privateWarps.get(uuid).addWarp(new Warp(warpName, world, x, y, z, yaw, pitch, icon));
					}
				}

			}
		}

		// ---Spawn warp
		if (warpsFileConfig.getConfigurationSection("Spawn-Warp") != null) {
			String worldName = warpsFileConfig.getString("Spawn-Warp" + ".world");
			double x = warpsFileConfig.getDouble("Spawn-Warp" + ".x");
			double y = warpsFileConfig.getDouble("Spawn-Warp" + ".y");
			double z = warpsFileConfig.getDouble("Spawn-Warp" + ".z");
			float yaw = (float) warpsFileConfig.getDouble("Spawn-Warp" + ".yaw");
			float pitch = (float) warpsFileConfig.getDouble("Spawn-Warp" + ".pitch");
			Material icon;
			try {
				icon = Material.valueOf(warpsFileConfig.getString("Spawn-Warp" + ".icon"));
			} catch (IllegalArgumentException e) {
				icon = plugin.defaultIcon;
			}

			spawn = new Warp("Spawn-Warp", worldName, x, y, z, yaw, pitch, icon);
		}

	}
}

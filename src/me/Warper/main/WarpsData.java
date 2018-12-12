package me.Warper.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class WarpsData {
	Warper plugin;

	Inventory mainMenu;
	WarpsList globalWarps;
	HashMap<UUID, WarpsList> privateWarps = new HashMap<UUID, WarpsList>();

	public FileConfiguration warpsFileConfig;
	public File warpsFile;

	public WarpsData(Warper plugin) {
		this.plugin = plugin;
		globalWarps  = new WarpsList(plugin);

		ItemStack[] menuItems = new ItemStack[27];
		ItemMeta meta;

		// Global warps item
		ItemStack globalWarps = new ItemStack(Material.SIGN);
		meta = globalWarps.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Global Warps");
		globalWarps.setItemMeta(meta);
		menuItems[10] = globalWarps;
		// -------------

		// Private Warps
		ItemStack privateWarps = new ItemStack(Material.ACACIA_DOOR);
		meta = privateWarps.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Private Warps");
		privateWarps.setItemMeta(meta);
		menuItems[13] = privateWarps;
		// -------------

		// Spawn
		ItemStack spawn = new ItemStack(Material.ARMOR_STAND);
		meta = spawn.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Spawn");
		spawn.setItemMeta(meta);
		menuItems[16] = spawn;
		// -------------

		// Close Menu
		ItemStack close = new ItemStack(Material.RED_CONCRETE);
		meta = close.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		close.setItemMeta(meta);
		menuItems[22] = close;

		mainMenu = Bukkit.createInventory(null, 27, "Warper Menu");
		mainMenu.setContents(menuItems);
	}

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		warpsFile = new File(plugin.getDataFolder(), "groups.yml");

		if (!warpsFile.exists()) {
			try {
				warpsFile.createNewFile();
			} catch (IOException e) {
				plugin.getServer().getLogger().info(ChatColor.RED + "Could not create groups.yml file");
			}

		}

		warpsFileConfig = YamlConfiguration.loadConfiguration(warpsFile);
		loadWarps();
	}

	private void loadWarps() {

	}
}

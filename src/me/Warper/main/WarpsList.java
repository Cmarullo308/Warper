package me.Warper.main;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class WarpsList {
	Warper plugin;

	ArrayList<Warp> warps = new ArrayList<Warp>();
	ArrayList<ItemStack[]> contentPages = new ArrayList<ItemStack[]>();

	int numberOfPages;

	public WarpsList(Warper plugin) {
		this.plugin = plugin;
		updateNumberOfPages();
	}

	public WarpsList(ArrayList<Warp> warps, Warper plugin) {
		this.plugin = plugin;
		this.warps = warps;
		updateNumberOfPages();
	}

	private void updateNumberOfPages() {
		if (warps.size() == 0) {
			this.numberOfPages = 0;
		} else if (warps.size() % 45 == 0) {
			this.numberOfPages = (int) (warps.size() / 45);
		} else {
			this.numberOfPages = (int) (warps.size() / 45) + 1;
		}
	}

	public void addWarp(Warp newWarp) {
		warps.add(newWarp);
		Collections.sort(warps);
	}

	public void warpPlayerTo(Player player, String warpName) {
		for (Warp warp : warps) {
			if (warp.warpName.equals(warpName)) {
				Location location = new Location(plugin.getServer().getWorld(warp.worldName), warp.x, warp.y, warp.z,
						warp.yaw, warp.pitch);
				player.teleport(location);
			}
		}
	}

	public void warpPlayerTo(Player player, Warp warp) {
		Location location = new Location(plugin.getServer().getWorld(warp.worldName), warp.x, warp.y, warp.z, warp.yaw,
				warp.pitch);
		player.teleport(location);
	}

	public Warp getWarp(String warpName) {
		for (Warp warp : warps) {
			if (warp.warpName.equals(warpName)) {
				return warp;
			}
		}

		return null;
	}

	public boolean warpExists(String warpName) {
		for (Warp warp : warps) {
			if (warp.warpName.equals(warpName)) {
				return true;
			}
		}

		return false;
	}

	public ItemStack[] getPage(int pageNum) {
		updateNumberOfPages();

		if (pageNum > numberOfPages && pageNum != 1) {
			plugin.consoleMessageD("Page num error");
		}

		ItemStack[] contents = new ItemStack[54];

		int lastNum = (45 * pageNum) - 1;
		int startNum = lastNum - 44;

		if (pageNum != 1) {
			ItemStack prevPage = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			ItemMeta meta = prevPage.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Page " + (pageNum - 1));
			prevPage.setItemMeta(meta);
			contents[45] = prevPage;
		}

		int contentsLocation = 0;
		for (int i = startNum; i <= lastNum; i++) {
			if (i >= warps.size()) {
				break;
			}

			Warp warp = warps.get(i);

			ItemStack warpItem = new ItemStack(warp.icon);
			ItemMeta meta = warpItem.getItemMeta();
			meta.setDisplayName(ChatColor.BLUE + warp.warpName);

			ArrayList<String> lore = new ArrayList<String>();
			lore.add(warp.worldName);
			lore.add("X: " + (int) warp.x + "");
			lore.add("Y: " + (int) warp.y + "");
			lore.add("Z: " + (int) warp.z + "");
			meta.setLore(lore);

			warpItem.setItemMeta(meta);
			contents[contentsLocation] = warpItem;
			contentsLocation++;
		}

		if (pageNum < numberOfPages) {
			ItemStack nextPage = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			ItemMeta meta = nextPage.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Page " + (pageNum + 1));
			nextPage.setItemMeta(meta);
			contents[53] = nextPage;
		}

		// Close button
		ItemStack closeButton = new ItemStack(Material.BARRIER);
		ItemMeta meta = closeButton.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		closeButton.setItemMeta(meta);
		contents[49] = closeButton;

		ItemStack background = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		ItemMeta backgroundMeta = background.getItemMeta();
		backgroundMeta.setDisplayName("");
		background.setItemMeta(backgroundMeta);

		for (int i = 45; i < 54; i++) {
			if (i != 49 && contents[i] == null) {
				contents[i] = background.clone();
			}
		}

		return contents;
	}
}

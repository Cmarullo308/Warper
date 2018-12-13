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

//	private void setupPages() {
//		contentPages = new ArrayList<ItemStack[]>();
//
//		int numOfPages = (int) (warps.size() / 45) + 1;
//		int upTo = 0;
//
//		plugin.consoleMessageD(numOfPages + "~");
//		for (int pageNum = 1; pageNum <= numOfPages; pageNum++) { // For each page
//			ItemStack[] page = new ItemStack[54];
//			if (pageNum != 1) {
//				ItemStack prevButton = new ItemStack(Material.SLIME_BALL);
//				ItemMeta meta = prevButton.getItemMeta();
//				meta.setDisplayName(ChatColor.RED + "Page " + (pageNum - 1));
//				prevButton.setItemMeta(meta);
//				page[45] = prevButton;
//			}
//
//			for (int i = 0; i < 45; i++) {
//				if (i >= warps.size()) {
//					plugin.consoleMessageD(i + "]]]]");
//					break;
//				}
//				ItemStack warp = new ItemStack(warps.get(upTo).icon);
//				ItemMeta meta = warp.getItemMeta();
//				meta.setDisplayName(warps.get(upTo).warpName);
//				warp.setItemMeta(meta);
//				page[i] = warp;
//				upTo++;
//			}
//
//			if (pageNum == numOfPages) {
//				ItemStack nextButton = new ItemStack(Material.SLIME_BALL);
//				ItemMeta meta = nextButton.getItemMeta();
//				meta.setDisplayName(ChatColor.RED + "Page " + (pageNum + 1));
//				nextButton.setItemMeta(meta);
//				page[53] = nextButton;
//			}
//			contentPages.add(page);
//		}
//
//	}

	public ItemStack[] getPage(int pageNum) {
		updateNumberOfPages();

		if (pageNum > numberOfPages && pageNum != 1) {
			plugin.consoleMessageD("Page num error");
		}

		ItemStack[] contents = new ItemStack[54];

		int lastNum = (45 * pageNum) - 1;
		int startNum = lastNum - 44;

		if (pageNum != 1) {
			ItemStack prevPage = new ItemStack(Material.SLIME_BALL);
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

			ItemStack warpItem = new ItemStack(warps.get(i).icon);
			ItemMeta meta = warpItem.getItemMeta();
			meta.setDisplayName(warps.get(i).warpName);
			warpItem.setItemMeta(meta);
			contents[contentsLocation] = warpItem;
			contentsLocation++;
		}

		if (pageNum < numberOfPages) {
			ItemStack nextPage = new ItemStack(Material.SLIME_BALL);
			ItemMeta meta = nextPage.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Page " + (pageNum + 1));
			nextPage.setItemMeta(meta);
			contents[53] = nextPage;
		}

		return contents;

//		return contentPages.get(pageNum - 1);
	}
}

package me.Warper.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {
	Warper plugin;
	WarpsData warpsData;

	public EventListener(Warper plugin) {
		this.plugin = plugin;
		warpsData = plugin.warpsData;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		String inventoryName = event.getView().getTitle();

		try {
			if (inventoryName.substring(2).equals("Warper Menu")) {
				event.setCancelled(true);
				switch (event.getRawSlot()) {
				case 10: // Global Warps
					openGlobalWarpsList(event.getWhoClicked(), 1);
					break;
				case 13: // Private warps
					openPrivateWarpsList(event.getWhoClicked(), 1);
					break;
				case 16:
					warpToSpawn(event.getWhoClicked());
					break;
				case 22:
					event.getWhoClicked().getOpenInventory().close();
					break;
				default:
					break;
				}
			}
			// Global warps
			else if (inventoryName.substring(2, 14).equals("Global Warps")) {
				event.setCancelled(true);
				if (event.getSlot() == 53) {
					int pageCurrentlyOn = Integer.parseInt(inventoryName.substring(22));
					if (pageCurrentlyOn < plugin.warpsData.globalWarps.numberOfPages) {
						openGlobalWarpsList(event.getWhoClicked(), pageCurrentlyOn + 1);
					}
				} else if (event.getSlot() == 45) {
					int pageCurrentlyOn = Integer.parseInt(inventoryName.substring(22));
					if (pageCurrentlyOn > 1) {
						openGlobalWarpsList(event.getWhoClicked(), pageCurrentlyOn - 1);
					}
				} else if (event.getSlot() == 49) {
					event.getWhoClicked().openInventory(plugin.warpsData.mainMenu);
				} else if (event.getInventory().getContents()[event.getSlot()] != null) {
					String warpName = event.getInventory().getContents()[event.getSlot()].getItemMeta()
							.getDisplayName();
					warpsData.globalWarps.warpPlayerTo((Player) event.getWhoClicked(), ChatColor.stripColor(warpName));
				}
			}
			// Private Warps
			else if (inventoryName.substring(2, 15).equals("Private Warps")) {

				event.setCancelled(true);
				WarpsList warpsList = warpsData.getPrivateWarps(event.getWhoClicked().getUniqueId());
				if (event.getSlot() == 53) {
					int pageCurrentlyOn = Integer.parseInt(inventoryName.substring(23));
					if (pageCurrentlyOn < warpsList.numberOfPages) {
						openPrivateWarpsList(event.getWhoClicked(), pageCurrentlyOn + 1);
					}
				} else if (event.getSlot() == 45) {
					int pageCurrentlyOn = Integer.parseInt(inventoryName.substring(23));
					if (pageCurrentlyOn > 1) {
						openPrivateWarpsList(event.getWhoClicked(), pageCurrentlyOn - 1);
					}
				} else if (event.getSlot() == 49) {
					event.getWhoClicked().openInventory(plugin.warpsData.mainMenu);
				} else if (event.getInventory().getContents()[event.getSlot()] != null) { // ----------
					String warpName = event.getInventory().getContents()[event.getSlot()].getItemMeta()
							.getDisplayName();
					warpsList.warpPlayerTo((Player) event.getWhoClicked(), ChatColor.stripColor(warpName));
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			// Normal inventory
		} catch (ArrayIndexOutOfBoundsException e) {
			// Sometimes randomly thinks there's a -999th slot
		}
	}

	private void warpToSpawn(HumanEntity whoClicked) {
		if (warpsData.teleportToSpawn((Player) whoClicked) == false) {
			whoClicked.sendMessage(ChatColor.RED + "No spawn set");
			return;
		}
	}

	private void openPrivateWarpsList(HumanEntity player, int pageNum) {
		WarpsList privateWarpList = warpsData.getPrivateWarps(player.getUniqueId());
		if (privateWarpList == null) {
			player.sendMessage(ChatColor.RED + "No private warps set");
			return;
		}
		Inventory page = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Private Warps - Page " + pageNum);
		page.setContents(privateWarpList.getPage(pageNum));
		player.openInventory(page);
	}

	private void openGlobalWarpsList(HumanEntity player, int pageNum) {
		Inventory page = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Global Warps - Page " + pageNum);
		page.setContents(plugin.warpsData.globalWarps.getPage(pageNum));
		player.openInventory(page);
	}

	@EventHandler
	public void ballFiring(PlayerInteractEvent event) {
		// If not a right click
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		// If item in hand isn't a clock
		if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.CLOCK) {
			return;
		}

		event.getPlayer().openInventory(plugin.warpsData.mainMenu);
	}
}

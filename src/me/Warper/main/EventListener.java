package me.Warper.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {
	Warper plugin;

	public EventListener(Warper plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getInventory().getName().equals("Warper Menu")) {
			event.setCancelled(true);
			switch (event.getSlot()) {
			case 10: // Global Warps
				event.setCancelled(true);
				openGlobalWarpsList(event.getWhoClicked(), 1);
				break;
			default:
				break;
			}
		} else if (event.getInventory().getName().substring(2, 14).equals("Global Warps")) {
			if (event.getSlot() == 53) {
				event.setCancelled(true);
				int pageCurrentlyOn = Integer.parseInt(event.getInventory().getName().substring(22));
				if (pageCurrentlyOn < plugin.warpsData.globalWarps.numberOfPages) {
					openGlobalWarpsList(event.getWhoClicked(), pageCurrentlyOn + 1);
				}
			} else if (event.getSlot() == 45) {
				event.setCancelled(true);
				int pageCurrentlyOn = Integer.parseInt(event.getInventory().getName().substring(22));
				plugin.consoleMessageD(pageCurrentlyOn + "////");
				if (pageCurrentlyOn > 1) {
					openGlobalWarpsList(event.getWhoClicked(), pageCurrentlyOn - 1);
				}
			}
		}
	}

	private void openGlobalWarpsList(HumanEntity whoClicked, int pageNum) {
		Inventory page = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Global Warps - Page " + pageNum);
		page.setContents(plugin.warpsData.globalWarps.getPage(pageNum));
		whoClicked.openInventory(page);
	}

	@EventHandler
	public void ballFiring(PlayerInteractEvent event) {
		// If not a right click
		if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}

		// If item in hand isn't a clock
		if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.CLOCK) {
			return;
		}

		event.getPlayer().openInventory(plugin.warpsData.mainMenu);
	}
}

package me.Warper.main;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Warper.main.EventListener;

public class Warper extends JavaPlugin {
	WarpsData warpsData = new WarpsData(this);
	CommandHandler commandHandler = new CommandHandler(this);

	boolean debugMessagesEnabled = true;

	public static void main(String args[]) {
		ItemStack[] fuck = new ItemStack[54];

		System.out.println(fuck.length);
	}

	@Override
	public void onEnable() {
		consoleMessageD("SHITTTTTT");

		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return commandHandler.onCommand(sender, command, label, args);
	}

	@SuppressWarnings("unused")
	private void testCommand(CommandSender sender, String[] args) {

		Player player = (Player) sender;

		if (args.length == 3) {
			for (int i = 0; i < 43; i++) {
				Warp testWarp = new Warp("" + i, "world", 0, 100, 0, 0, 0, Material.GRASS);
				warpsData.globalWarps.addWarp(testWarp);
			}
			return;
		}

		Warp testWarp = new Warp(args[1], "world", 0, 100, 0, 0, 0, Material.GRASS);
		warpsData.globalWarps.addWarp(testWarp);
		consoleMessageD(warpsData.globalWarps.warps.size() + "");

//		ItemStack[] contents = new ItemStack[54];
//		contents[0] = new ItemStack(Material.DIAMOND, 12);
////		Inventory inv = Bukkit.createInventory(player, 27);
//		Inventory inv = Bukkit.createInventory(player, 54, "Kek");
//		inv.setContents(contents);
//
//		player.openInventory(inv);
	}

	@Override
	public void onDisable() {

	}

	public void consoleMessage(String message) {
		getLogger().info(message);
	}

	public void consoleMessageD(String message) {
		if (debugMessagesEnabled) {
			getLogger().info(message);
		}
	}
}

package me.Warper.main;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Warper extends JavaPlugin {
	WarpsData warpsData = new WarpsData(this);
	CommandHandler commandHandler = new CommandHandler(this);

	Material defaultIcon = Material.GRASS_BLOCK;

	String helpMessage;

	boolean debugMessagesEnabled = false;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		warpsData.setup();

		createHelpMessage();

		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		consoleMessage("Warper loaded");
	}

	private void createHelpMessage() {
		helpMessage = "Commands\n";
		helpMessage += "/setglobalwarp <WarpName> <Material for icon>\n";
		helpMessage += "/removeglobalwarp <WarpName>\n";
		helpMessage += "/setprivatewarp <WarpName> <Material for icon>\n";
		helpMessage += "/removeprivatewarp <WarpName>\n";
		helpMessage += "/setspawnlocation\n";
		helpMessage += "/removespawnlocation\n";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return commandHandler.onCommand(sender, command, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return commandHandler.onTabComplete(sender, command, label, args);
	}

	@SuppressWarnings("unused")
	private void testCommand(CommandSender sender, String[] args) {

		Player player = (Player) sender;

		if (args.length == 3) {
			for (int i = 0; i < 43; i++) {
				Warp testWarp = new Warp("" + i, "world", 0, 100, 0, 0, 0, Material.GRASS_BLOCK);
				warpsData.globalWarps.addWarp(testWarp);
			}
			return;
		}

		Warp testWarp = new Warp(args[1], "world", 0, 100, 0, 0, 0, Material.GRASS_BLOCK);
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

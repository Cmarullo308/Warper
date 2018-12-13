package me.Warper.main;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler {
	Warper plugin;
	WarpsData warpsData;

	public CommandHandler(Warper plugin) {
		this.plugin = plugin;
		warpsData = plugin.warpsData;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		switch (command.getLabel().toLowerCase()) {
		case "setglobalwarp":
			setGlobalWarp(sender, args);
			break;
		case "setprivatewarp":
			setPrivateWarp(sender, args);
			break;
		case "setspawnlocation":
			setSpawnLocation(sender, args);
			break;
		default:
			break;
		}

		return true;
	}

	private void setSpawnLocation(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			mustBeAPlayer(sender);
			return;
		}

		Location newSpawnLocation = ((Player) sender).getLocation();
		warpsData.spawn = new Warp("Spawn", newSpawnLocation.getWorld().getName(), newSpawnLocation.getX(),
				newSpawnLocation.getY(), newSpawnLocation.getZ(), newSpawnLocation.getYaw(),
				newSpawnLocation.getPitch(), Material.GRASS_BLOCK);
	}

	private void setPrivateWarp(CommandSender sender, String[] args) {
		if (args.length < 1 || args.length > 2) {
			invalidNumOfArgsMessage(sender);
			return;
		}

		if (!(sender instanceof Player)) {
			mustBeAPlayer(sender);
			return;
		}

		Player player = (Player) sender;
		UUID playerID = player.getUniqueId();
		Location loc = player.getLocation();

		String warpName = args[0];
		Material warpIcon;

		WarpsList warpsList = warpsData.getPrivateWarps(playerID);
		if (warpsList == null) {
			warpsData.privateWarps.put(playerID, new WarpsList(plugin));
			warpsList = warpsData.getPrivateWarps(playerID);
		}

		if (warpsList.warpExists(warpName)) {
			sender.sendMessage(
					ChatColor.RED + "A warp named " + ChatColor.YELLOW + warpName + ChatColor.RED + " already exists");
			return;
		}

		if (args.length == 2) {
			try {
				warpIcon = Material.valueOf(args[1]);
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED + "Invalid material name");
				return;
			}
		} else {
			warpIcon = Material.GRASS_BLOCK;
		}

		warpsList.addWarp(new Warp(warpName, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
				loc.getPitch(), warpIcon));
	}

	private void setGlobalWarp(CommandSender sender, String[] args) {
		if (args.length < 1 || args.length > 2) {
			invalidNumOfArgsMessage(sender);
			return;
		}

		if (!(sender instanceof Player)) {
			mustBeAPlayer(sender);
			return;
		}

		Player player = (Player) sender;
		Location loc = player.getLocation();

		String warpName = args[0];
		Material warpIcon;

		if (warpsData.globalWarps.warpExists(args[0])) {
			sender.sendMessage(
					ChatColor.RED + "A warp named " + ChatColor.YELLOW + warpName + ChatColor.RED + " already exists");
			return;
		}

		if (args.length == 2) {
			try {
				warpIcon = Material.valueOf(args[1]);
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED + "Invalid material name");
				return;
			}
		} else {
			warpIcon = Material.GRASS_BLOCK;
		}

		warpsData.globalWarps.addWarp(new Warp(warpName, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch(), warpIcon));
	}

	private void mustBeAPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Must be a player to run this command");
	}

	private void invalidNumOfArgsMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Invalid number of arguements");
	}
}

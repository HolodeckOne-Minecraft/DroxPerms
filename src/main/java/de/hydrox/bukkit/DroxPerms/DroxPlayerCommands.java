package de.hydrox.bukkit.DroxPerms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DroxPlayerCommands implements CommandExecutor {
    
    private DroxPerms plugin;

    public DroxPlayerCommands(DroxPerms plugin) {
        this.plugin = plugin;
    }
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		Player caller = (Player) sender;
		boolean result = false;
		if (split.length == 0) {
			return false;
		} else if (caller.getName().equalsIgnoreCase(split[1]) && !(sender.hasPermission("droxperms.player.self"))) {
			sender.sendMessage("You don't have permission to modify your Permissions.");
			return true;			
		} else if (!(sender.hasPermission("droxperms.players.others"))) {
			sender.sendMessage("You don't have permission to modify other Players Permissions.");
			return true;
		}
		// add permission
		if (split[0].equalsIgnoreCase("addperm")) {
			if (split.length == 3) {
				// add global permission
				result = plugin.dataProvider.addPlayerPermission(split[1], null, split[2]);
			} else if (split.length == 4) {
				// add world permission
				result = plugin.dataProvider.addPlayerPermission(split[1], split[3], split[2]);				
			}
			plugin.refreshPlayer((Player) sender);
			return result;
		}

		// remove permission
		if (split[0].equalsIgnoreCase("remperm")) {
			if (split.length == 3) {
				// remove global permission
				result = plugin.dataProvider.removePlayerPermission(split[1], null, split[2]);
			} else if (split.length == 4) {
				// remove world permission
				result = plugin.dataProvider.removePlayerPermission(split[1], split[3], split[2]);				
			}
			plugin.refreshPlayer((Player) sender);
			return result;
		}

		// add subgroup
		if (split[0].equalsIgnoreCase("addsub")) {
			if (split.length == 3) {
				result = plugin.dataProvider.addPlayerSubgroup(split[1], split[2]);
			}
			plugin.refreshPlayer((Player) sender);
			return result;
		}

		// remove subgroup
		if (split[0].equalsIgnoreCase("remperm")) {
			if (split.length == 3) {
				result = plugin.dataProvider.removePlayerSubgroup(split[1],split[2]);
			}
			plugin.refreshPlayer((Player) sender);
			return result;
		}

		// set group
		if (split[0].equalsIgnoreCase("setgroup")) {
			if (split.length == 3) {
				result = plugin.dataProvider.setPlayerGroup(split[1],split[2]);
			}
			plugin.refreshPlayer((Player) sender);
			return result;
		}
		return true;
	}

}

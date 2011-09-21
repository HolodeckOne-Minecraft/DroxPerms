package de.hydrox.bukkit.DroxPerms;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

/**
 * This Class is the API of DroxPerms for external Plug-ins. <br>
 * To use it, use the following code-snippet in your plugin-class.
 * <p>
 * 
 * <pre>
 * DroxPermsAPI API = null;
 * DroxPerms droxPerms = ((DroxPerms) this.getServer().getPluginManager()
 * 		.getPlugin(&quot;DroxPerms&quot;));
 * if (droxPerms != null) {
 * 	DroxPermsAPI API = APIdroxPerms.getAPI();
 * }
 * </pre>
 * 
 * @author Matthias Söhnholz
 * @since 0.1.0
 */
public class DroxPermsAPI {
	DroxPerms plugin = null;
	FakeCommandSender fakeCS = null;

	public DroxPermsAPI(DroxPerms plugin) {
		this.plugin = plugin;
		this.fakeCS = new FakeCommandSender();
	}

	/**
	 * Returns the Group of a Player.
	 * 
	 * @param player
	 *            Player to be queried
	 * @return main group of the Player, or "" if player doesn't exist
	 * @since 0.1.0
	 */
	public String getPlayerGroup(String player) {
		return plugin.dataProvider.getPlayerGroup(player);
	}

	/**
	 * Sets a Players Group.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param group
	 *            Group to be set
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean setPlayerGroup(String player, String group) {
		boolean result = plugin.dataProvider.setPlayerGroup(fakeCS, player,
				group);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Returns the sub-groups of the Player.
	 * 
	 * @param player
	 *            Player to be queried
	 * @return ArrayList<String> containing the subgroups, null if Player
	 *         doesn't exist
	 * @since 0.1.0
	 */
	public ArrayList<String> getPlayerSubgroups(String player) {
		return plugin.dataProvider.getPlayerSubgroups(player);
	}

	/**
	 * Adds a Subgroup to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param subgroup
	 *            Subgroup to be added
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addPlayerSubgroup(String player, String subgroup) {
		boolean result = plugin.dataProvider.addPlayerSubgroup(fakeCS, player,
				subgroup);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Removes a Subgroup from a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param subgroup
	 *            Subgroup to be removed
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removePlayerSubgroup(String player, String subgroup) {
		boolean result = plugin.dataProvider.removePlayerSubgroup(fakeCS,
				player, subgroup);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Adds a global Permission to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param node
	 *            Permission-node to be added
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addPlayerPermission(String player, String node) {
		boolean result = addPlayerPermission(player, null, node);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Adds a Permission for a given world to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param world
	 *            World the Permission belongs to
	 * @param node
	 *            Permission-node to be added
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addPlayerPermission(String player, String world, String node) {
		boolean result = plugin.dataProvider.addPlayerPermission(fakeCS,
				player, world, node);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Removes a global Permission to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param node
	 *            Permission-node to be removed
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removePlayerPermission(String player, String node) {
		boolean result = removeGroupPermission(player, null, node);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Removes a Permission for a given world from a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param world
	 *            World the Permission belongs to
	 * @param node
	 *            Permission-node to be removed
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removePlayerPermission(String player, String world,
			String node) {
		boolean result = plugin.dataProvider.removePlayerPermission(fakeCS,
				player, world, node);
		plugin.refreshPlayer(plugin.getServer().getPlayer(player));
		return result;
	}

	/**
	 * Returns Data from the Players Info-node.
	 * 
	 * @param player
	 *            Player to be queried
	 * @param node
	 *            Info-node to be read
	 * @return Info-node, is null if Player or Info-node doesn't exist
	 * @since 0.1.0
	 */
	public String getPlayerInfo(String player, String node) {
		return plugin.dataProvider.getPlayerInfo(fakeCS, player, node);
	}

	/**
	 * Sets Data in the Players Info-node.
	 * 
	 * @param player
	 *            Player to be modified.
	 * @param node
	 *            Info-node to be set.
	 * @param data
	 *            Data to be written.
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean setPlayerInfo(String player, String node, String data) {
		return plugin.dataProvider.setPlayerInfo(fakeCS, player, node, data);
	}

	/**
	 * Returns all Subgroups of a given Group.
	 * 
	 * @param group
	 *            Group to be queried
	 * @return ArrayList<String> containing the subgroups, null if Group doesn't
	 *         exist
	 * @since 0.1.0
	 */
	public ArrayList<String> getGroupSubgroups(String group) {
		return plugin.dataProvider.getGroupSubgroups(group);
	}

	/**
	 * Adds a Subgroup to a given Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param subgroup
	 *            Subgroup to be added
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addGroupSubgroup(String group, String subgroup) {
		boolean result = plugin.dataProvider.addGroupSubgroup(fakeCS, group,
				subgroup);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Removes a Subgroup from a given Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param subgroup
	 *            Subgroup to be removed
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removeGroupSubgroup(String group, String subgroup) {
		boolean result = plugin.dataProvider.removeGroupSubgroup(fakeCS, group,
				subgroup);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Adds a global Permission to a Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param node
	 *            Permission-node to add
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addGroupPermission(String group, String node) {
		boolean result = addGroupPermission(group, null, node);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Adds a Permission for a given world to a Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param world
	 *            World the Permission belongs to
	 * @param node
	 *            Permission-node to add
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean addGroupPermission(String group, String world, String node) {
		boolean result = plugin.dataProvider.addGroupPermission(fakeCS, group,
				world, node);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Removes a global Permission from a Group. permission.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param node
	 *            Permission-node to remove
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removeGroupPermission(String group, String node) {
		boolean result = removeGroupPermission(group, null, node);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Removes a Permission for a given world from a Group. permission.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param world
	 *            World the Permission belongs to
	 * @param node
	 *            Permission-node to remove
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean removeGroupPermission(String group, String world, String node) {
		boolean result = plugin.dataProvider.removeGroupPermission(fakeCS,
				group, world, node);
		plugin.refreshPermissions();
		return result;
	}

	/**
	 * Gets Data from the Groups Info-node.
	 * 
	 * @param group
	 *            Group to be queried
	 * @param node
	 *            Info-node to be read
	 * @return Info-node, is null if Player or Info-node doesn't exist
	 * @since 0.1.0
	 */
	public String getGroupInfo(String group, String node) {
		return plugin.dataProvider.getGroupInfo(fakeCS, group, node);
	}

	/**
	 * Sets Data in the Groups Info-node.
	 * 
	 * @param group
	 *            Group to be modified.
	 * @param node
	 *            Info-node to be set.
	 * @param data
	 *            Data to be written.
	 * @return success of operation
	 * @since 0.1.0
	 */
	public boolean setGroupInfo(String group, String node, String data) {
		return plugin.dataProvider.setGroupInfo(fakeCS, group, node, data);
	}
}

class FakeCommandSender implements CommandSender {

	public PermissionAttachment addAttachment(Plugin arg0) {
		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1,
			boolean arg2) {
		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1,
			boolean arg2, int arg3) {
		return null;
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return null;
	}

	public boolean hasPermission(String arg0) {
		return true;
	}

	public boolean hasPermission(Permission arg0) {
		return true;
	}

	public boolean isPermissionSet(String arg0) {
		return false;
	}

	public boolean isPermissionSet(Permission arg0) {
		return false;
	}

	public void recalculatePermissions() {
	}

	public void removeAttachment(PermissionAttachment arg0) {
	}

	public boolean isOp() {
		return false;
	}

	public void setOp(boolean arg0) {
	}

	public Server getServer() {
		return null;
	}

	public void sendMessage(String arg0) {
	}
}

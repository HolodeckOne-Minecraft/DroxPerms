package de.hydrox.bukkit.DroxPerms;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

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
	 * @return Group-name
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
	 */
	public boolean setPlayerGroup(String player, String group) {
		return plugin.dataProvider.setPlayerGroup(fakeCS, player, group);
	}

	/**
	 * Returns all Subgroups of a given Player.
	 * 
	 * @param player
	 *            Player to be queried
	 * @return Array of Subgroup-Strings
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
	 */
	public boolean addPlayerSubgroup(String player, String subgroup) {
		return plugin.dataProvider.addPlayerSubgroup(fakeCS, player, subgroup);
	}

	/**
	 * Removes a Subgroup from a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param subgroup
	 *            Subgroup to be removed
	 * @return success of operation
	 */
	public boolean removePlayerSubgroup(String player, String subgroup) {
		return plugin.dataProvider.removePlayerSubgroup(fakeCS, player,
				subgroup);
	}

	/**
	 * Adds a global Permission to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param node
	 *            Permission-node to be added
	 * @return success of operation
	 */
	public boolean addPlayerPermission(String player, String node) {
		return addPlayerPermission(player, null, node);
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
	 */
	public boolean addPlayerPermission(String player, String world, String node) {
		return plugin.dataProvider.addPlayerPermission(fakeCS, player, world,
				node);
	}

	/**
	 * Removes a global Permission to a Player.
	 * 
	 * @param player
	 *            Player to be modified
	 * @param node
	 *            Permission-node to be removed
	 * @return success of operation
	 */
	public boolean removePlayerPermission(String player, String node) {
		return removeGroupPermission(player, null, node);
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
	 */
	public boolean removePlayerPermission(String player, String world,
			String node) {
		return plugin.dataProvider.removePlayerPermission(fakeCS, player,
				world, node);
	}

	/**
	 * Returns Data from the Players Info-node.
	 * 
	 * @param player
	 *            Player to be queried
	 * @param node
	 *            Info-node to be read
	 * @return Info-node
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
	 * @return
	 */
	public boolean setPlayerInfo(String player, String node, String data) {
		return plugin.dataProvider.setPlayerInfo(fakeCS, player, node, data);
	}

	/**
	 * Returns all Subgroups of a given Group.
	 * 
	 * @param group
	 *            Group to be queried
	 * @return Array of Subgroup-Strings
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
	 */
	public boolean addGroupSubgroup(String group, String subgroup) {
		return plugin.dataProvider.addGroupSubgroup(fakeCS, group, subgroup);
	}

	/**
	 * Removes a Subgroup from a given Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param subgroup
	 *            Subgroup to be removed
	 * @return success of operation
	 */
	public boolean removeGroupSubgroup(String group, String subgroup) {
		return plugin.dataProvider.removeGroupSubgroup(fakeCS, group, subgroup);
	}

	/**
	 * Adds a global Permission to a Group.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param node
	 *            Permission-node to add
	 * @return success of operation
	 */
	public boolean addGroupPermission(String group, String node) {
		return addGroupPermission(group, null, node);
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
	 */
	public boolean addGroupPermission(String group, String world, String node) {
		return plugin.dataProvider.addGroupPermission(fakeCS, group, world,
				node);
	}

	/**
	 * Removes a global Permission from a Group. permission.
	 * 
	 * @param group
	 *            Group to be modified
	 * @param node
	 *            Permission-node to remove
	 * @return success of operation
	 */
	public boolean removeGroupPermission(String group, String node) {
		return removeGroupPermission(group, null, node);
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
	 */
	public boolean removeGroupPermission(String group, String world, String node) {
		return plugin.dataProvider.removeGroupPermission(fakeCS, group, world,
				node);
	}

	/**
	 * Gets Data from the Groups Info-node.
	 * 
	 * @param group
	 *            Group to be queried
	 * @param node
	 *            Info-node to be read
	 * @return Info-node
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
	 * @return
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
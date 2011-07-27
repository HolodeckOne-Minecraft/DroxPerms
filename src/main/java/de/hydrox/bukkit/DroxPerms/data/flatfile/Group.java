package de.hydrox.bukkit.DroxPerms.data.flatfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.permissions.Permission;
import org.bukkit.util.config.ConfigurationNode;

import de.hydrox.bukkit.DroxPerms.data.Config;

public class Group {
	private static HashMap<String, Group> groups = new HashMap<String, Group>();
	
	private String name;
	private HashMap<String, ArrayList<String>> permissions;
	private ArrayList<String> globalPermissions;
	private ArrayList<String> subgroups;

	private HashMap<String, Permission> bukkitPermissions;

	public Group() {
		this("default");
	}

	public Group(String name) {
		this.name = name;
		this.subgroups = new ArrayList<String>();
		this.globalPermissions = new ArrayList<String>();
		this.permissions = new HashMap<String, ArrayList<String>>();
	}

	public Group(String name, ConfigurationNode node) {
		this.name = name;
		System.out.println("groups" + node.getKeys().toString());
		System.out.println("groups.subgroups" + node.getStringList("subgroups", new ArrayList<String>()));
		this.subgroups = (ArrayList<String>) node.getStringList("subgroups", new ArrayList<String>());
		System.out.println("subgroups: " + subgroups.size());
		this.globalPermissions = (ArrayList<String>) node.getStringList("globalpermissions", new ArrayList<String>());
		System.out.println("globalpermissions: " + globalPermissions.size());
		this.permissions = new HashMap<String, ArrayList<String>>();
		ConfigurationNode tmp = node.getNode("permissions");
		Iterator<String> iter = tmp.getKeys().iterator();
		while (iter.hasNext()) {
			String world = iter.next();
			permissions.put(world, (ArrayList<String>) tmp.getStringList(world, new ArrayList<String>()));
			System.out.println("permissions "+world+": " + permissions.get(world).size());
		}

		//create Permission for default world
		if (!permissions.containsKey(Config.getDefaultWorld())) {
			HashMap<String, Boolean> children = new HashMap<String, Boolean>();
			for (String subgroup : subgroups) {
				children.put("droxperms.meta.group." + subgroup + "." + Config.getDefaultWorld(), false);
			}
			children.put("droxperms.meta.group." + name, false);

			Permission permission = new Permission("droxperms.meta.group." + name + "." + Config.getDefaultWorld(), "Group-Permissions for group " + name + " on world " + Config.getDefaultWorld(), children);
			bukkitPermissions.put(Config.getDefaultWorld(), permission);
		}

		//create Permissions for other worlds
		for (String world : permissions.keySet()) {
			HashMap<String, Boolean> children = new HashMap<String, Boolean>();
			for (String subgroup : subgroups) {
				children.put("droxperms.meta.group." + subgroup + "." + world, false);
			}

			for (String permission : permissions.get(world)) {
				children.put(permission, false);
			}

			children.put("droxperms.meta.group." + name, false);

			Permission permission = new Permission("droxperms.meta.group." + name + "." + world, "Group-Permissions for group " + name + " on world " + world, children);
			bukkitPermissions.put(world, permission);
		}
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Object> toConfigurationNode() {
		HashMap<String, Object> output = new HashMap<String, Object>();
		output.put("subgroups", subgroups);
		output.put("permissions", permissions);
		output.put("globalpermissions", globalPermissions);
		return output;
	}

	public boolean addPermission(String world, String permission) {
		ArrayList<String> permArray = permissions.get(world.toLowerCase());
		if (permArray != null) {
			if (permArray.contains(permission)) {
				return false;
			}
			permArray.add(permission);
			return true;
		}
		return false;
	}

	public boolean removePermission(String world, String permission) {
		ArrayList<String> permArray = permissions.get(world.toLowerCase());
		if (permArray != null) {
			if (permArray.contains(permission)) {
				permArray.remove(permission);
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean addSubgroup(String subgroup) {
		if(Group.existGroup(subgroup.toLowerCase())) {
			if(!subgroups.contains(subgroup.toLowerCase())) {
				subgroups.add(subgroup.toLowerCase());
				return true;
			}
			
		} 
		return false;
	}

	public boolean removeSubgroup(String subgroup) {
		if(subgroups.contains(subgroup.toLowerCase())) {
			subgroups.remove(subgroup.toLowerCase());
			return true;
		}
		return false;
	}

	public boolean hasPermission(String world, String permission) {
		ArrayList<String> permArray = permissions.get(world.toLowerCase());
		if (permArray != null) {
			if (permArray.contains(permission)) {
				return true;
			}
		}

		for (String subgroup : subgroups) {
			if (Group.getGroup(subgroup) != null) {
				if (Group.getGroup(subgroup).hasPermission(world.toLowerCase(), permission)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addWorld(String world) {
		if (permissions.containsKey(world.toLowerCase())) {
			return false;
		}
		permissions.put(world.toLowerCase(), new ArrayList<String>());
		return true;
	}

	public static boolean addGroup(Group group) {
		if (existGroup(group.name.toLowerCase())) {
			return false;
		}
		groups.put(group.name.toLowerCase(), group);
		return true;
	}

	public static boolean removeGroup(String name) {
		if (existGroup(name.toLowerCase())) {
			groups.remove(name.toLowerCase());
			return true;
		}
		return false;
	}

	public static Group getGroup(String name) {
		return groups.get(name.toLowerCase());
	}

	public static boolean existGroup(String name) {
		if (groups.containsKey(name.toLowerCase())) {
			return true;
		}
		return false;
	}

	public static void clearGroups() {
		groups.clear();
	}
	
	public static Iterator<Group> iter() {
		return groups.values().iterator();
	}
}

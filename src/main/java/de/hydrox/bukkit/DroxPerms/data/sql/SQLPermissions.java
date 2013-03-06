package de.hydrox.bukkit.DroxPerms.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.data.APermissions;
import de.hydrox.bukkit.DroxPerms.data.AUser;
import de.hydrox.bukkit.DroxPerms.data.flatfile.Group;

public class SQLPermissions extends APermissions {

	public static final String NODE = "MySQL";

	protected Connection conn;

	private String host;
	private String database;
	protected static String tableprefix;
	private String username;
	private String password;
	private int port;
	protected Logger logger;

	//prepared statements
	private PreparedStatement prepGetAllPlayerNames;
	private PreparedStatement prepGetExactPlayer;
	private PreparedStatement prepGetPartialPlayer;

	protected PreparedStatement prepGetUserGroup;
	protected PreparedStatement prepGetUserSubgroups;
	protected PreparedStatement prepGetUserPermissions;
	protected PreparedStatement prepGetUserInfoComplete;
	protected PreparedStatement prepGetUserInfoNode;

	protected PreparedStatement prepGetAllGroups;

	protected PreparedStatement prepGetGroupSubgroups;
	protected PreparedStatement prepGetGroupPermissions;
	protected PreparedStatement prepGetGroupInfoComplete;
	protected PreparedStatement prepGetGroupInfoNode;

//	public SQLPermissions(String host,int port,String database,String table,String username,String password, DroxPerms plugin) throws SQLException{
	public SQLPermissions(ConfigurationSection config, DroxPerms plugin) throws SQLException{
		APermissions.plugin = plugin;
		this.logger = plugin.getLogger();
		this.host = config.getString("host", "localhost");
		this.port = config.getInt("port",3306);
		this.database = config.getString("database");
		tableprefix = config.getString("prefix");
		this.username = config.getString("username");
		this.password = config.getString("password");
		try {
			Class.forName("com.mysql.jdbc.Driver");

			createConnection();

			checkAndMakeTable();
			prepareStatements();
			if(conn == null){
				throw new SQLException("Failed to start");
			}
		} catch (ClassNotFoundException e) {
			logger.severe("MySQL Library not found!");
		}
		
		try {
			ResultSet rs = prepGetAllGroups.executeQuery();
			while(rs.next()){
				String adding = rs.getString(1); 
				Group.addGroup(new SQLGroup(rs.getInt(1), rs.getString(2), this));
				logger.info("GROUPNAME: " + adding);
			}
			rs.close();

		} catch (SQLException e) {
			checkConnection();
			mysqlError(e);
		}

	}

	/**
	 * Connection to the database.
	 * @throws SQLException
	 */
	private void createConnection() {
		String conUrl = String.format("jdbc:mysql://%s:%s/%s",
				host,
				port,
				database);

		logger.info("Configuring....");
		Properties conStr = new Properties();
		conStr.put("user",username);
		conStr.put("password",password);
		conStr.put("autoReconnect", "true");

		logger.info("Connecting....");

		try {
			conn = DriverManager.getConnection(conUrl,conStr);

		} catch (SQLException e) {
			mysqlError(e);
			conn = null;
		}


	}

	protected synchronized Connection getConnection() {
		return conn;
	}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized boolean checkConnection(){
//		logger.info("Checking connection");
		try {
			if(conn == null || !conn.isValid(0)){
				logger.info("Something is derp, rebooting connection.");
				createConnection();
				if(conn!=null){
					logger.info("Rebuilding statements");
					prepareStatements();
				}
				else
				{
					logger.info("Reboot failed!");
				}

			}
		} catch (SQLException e) {
			conn = null;
			return false;
		}
//		logger.info("Checking is " + conn != null ? "up" : "down");
		return conn != null;
	}

	protected void checkAndMakeTable(){
		try{
			ResultSet rs = conn.getMetaData().getTables(null, null, tableprefix + "groups", null);
			logger.info("Checking for table: " + tableprefix + "groups");
			if (!rs.next()) {
				logger.info(tableprefix + "groups" + " not found, creating tables");

				java.util.Scanner s = new java.util.Scanner(plugin.getResource("MySQL-Tables")).useDelimiter(";");
				while (s.hasNext()) {
					String query = s.next();
					query = query.replace("$PREFIX", tableprefix);

					PreparedStatement ps = conn.prepareStatement(query);

					ps.executeUpdate();
					ps.close();
					
				}
				logger.info("created tables");
			}
			else
			{
				logger.info("Table found");
			}
			rs.close();
		} catch (SQLException e) {
			mysqlError(e);
		}
	}
	
	protected void prepareStatements(){
		try{
			logger.fine("Preparing statements");

			prepGetAllPlayerNames = conn.prepareStatement("SELECT playerName FROM " + tableprefix + "players");
			prepGetExactPlayer = conn.prepareStatement("SELECT playerID, playerName FROM " + tableprefix + "players WHERE playerName=?");
			prepGetPartialPlayer = conn.prepareStatement("SELECT playerID, playerName FROM " + tableprefix + "players WHERE playerName LIKE ?");

			prepGetUserGroup = conn.prepareStatement("SELECT groupName FROM " + SQLPermissions.tableprefix + "groups AS groups JOIN " + SQLPermissions.tableprefix + "players AS players ON groups.groupID=players.groupID WHERE players.playerID=?");
			prepGetUserSubgroups = conn.prepareStatement("SELECT groupName FROM " + SQLPermissions.tableprefix + "groups AS groups JOIN " + SQLPermissions.tableprefix + "playerSubgroups AS subgroups ON subgroups.subgroupID=groups.groupID JOIN " + SQLPermissions.tableprefix + "players AS players ON players.playerID=subgroups.playerID WHERE players.playerID=?");
			prepGetUserPermissions = conn.prepareStatement("SELECT permissionNode, value FROM " + SQLPermissions.tableprefix + "playerPermissions AS permissions JOIN " + SQLPermissions.tableprefix + "players AS players ON players.playerID=permissions.playerID JOIN " + SQLPermissions.tableprefix + "worlds AS worlds ON worlds.worldID=permissions.worldID WHERE players.playerID=? AND worlds.worldName=?");
			prepGetUserInfoComplete = conn.prepareStatement("SELECT nodeName, nodeData FROM " + SQLPermissions.tableprefix + "playerInfoNodes AS infonodes JOIN " + SQLPermissions.tableprefix + "players AS players ON players.playerID=infonodes.playerID WHERE players.playerID=?");
			prepGetUserInfoNode = conn.prepareStatement("SELECT nodeData FROM " + SQLPermissions.tableprefix + "playerInfoNodes AS infonodes JOIN " + SQLPermissions.tableprefix + "players AS players ON players.playerID=infonodes.playerID WHERE players.playerID=? AND infonodes.nodeName=?");
			
			prepGetAllGroups = conn.prepareStatement("SELECT groupID, groupName FROM " + tableprefix + "groups");

			prepGetGroupSubgroups = conn.prepareStatement("SELECT groupName FROM " + SQLPermissions.tableprefix + "groups AS groups JOIN " + SQLPermissions.tableprefix + "groupSubgroups AS subgroups ON subgroups.subgroupID=groups.groupID WHERE groups.groupID=subgroups.groupID AND groups.groupID=?");
			prepGetGroupPermissions = conn.prepareStatement("SELECT permissionNode, value FROM " + SQLPermissions.tableprefix + "groupPermissions AS permissions JOIN " + SQLPermissions.tableprefix + "groups AS groups ON groups.groupID=permissions.groupID JOIN " + SQLPermissions.tableprefix + "worlds AS worlds ON worlds.worldID=permissions.worldID WHERE groups.groupID=? AND worlds.worldName=?");
			prepGetGroupInfoComplete = conn.prepareStatement("SELECT nodeName, nodeData FROM " + SQLPermissions.tableprefix + "groupInfoNodes AS infonodes JOIN " + SQLPermissions.tableprefix + "groups AS groups ON groups.groupID=infonodes.groupID WHERE groups.groupID=?");
			prepGetGroupInfoNode = conn.prepareStatement("SELECT nodeData FROM " + SQLPermissions.tableprefix + "groupInfoNodes AS infonodes JOIN " + SQLPermissions.tableprefix + "groups AS groups ON groups.groupID=infonodes.groupID WHERE groups.groupID=? AND infonodes.nodeName=?");

			logger.fine("Set player stat statement created");
			logger.info("Initaised MySQL Data Provider.");
		} catch (SQLException e) {
			mysqlError(e);
		}
	}

	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean createPlayer(String name) {
		throw new NotImplementedException();
		// TODO Auto-generated method stub
	}

	@Override
	public boolean deletePlayer(CommandSender sender, String name) {
		throw new NotImplementedException();
		// TODO Auto-generated method stub
	}

	@Override
	public boolean createGroup(CommandSender sender, String name) {
		throw new NotImplementedException();
		// TODO Auto-generated method stub
	}

	@Override
	protected AUser getExactUser(String name) {
		AUser player = null;
		try {
			prepGetExactPlayer.clearParameters();
			prepGetExactPlayer.setString(1, name);
			ResultSet rs = prepGetExactPlayer.executeQuery();
			rs.last(); int numrows = rs.getRow(); rs.beforeFirst();
//			logger.info("ROWS: " + numrows);
			if(numrows == 1) {
				rs.next();
				
//				String adding = rs.getString(2); 
//				logger.info("PLAYERNAME: " + adding);
				player = new SQLUser(rs.getInt(1), rs.getString(2), this);
				rs.close();
				
			}

		} catch (SQLException e) {
			checkConnection();
			mysqlError(e);
		}
		return player;
	}

	@Override
	protected AUser getPartialUser(String name) {
		AUser player = null;
		try {
			prepGetPartialPlayer.clearParameters();
			prepGetPartialPlayer.setString(1, name + "%");
			ResultSet rs = prepGetPartialPlayer.executeQuery();
			rs.last(); int numrows = rs.getRow(); rs.beforeFirst();
//			logger.info("ROWS: " + numrows);
			if(numrows == 1) {
				rs.next();
				
//				String adding = rs.getString(2); 
//				logger.info("PLAYERNAME: " + adding);
				player = new SQLUser(rs.getInt(1), rs.getString(2), this);
				rs.close();
				
			}

		} catch (SQLException e) {
			checkConnection();
			mysqlError(e);
		}
		return player;
	}

	@Override
	public Set<String> getAllUserNames() {
		Set<String> set = new HashSet<String>();
		try {
			ResultSet rs = prepGetAllPlayerNames.executeQuery();
			while(rs.next()){
				String adding = rs.getString(1); 
				set.add(adding);
//				logger.info("PLAYERNAME: " + adding);
			}
			rs.close();

		} catch (SQLException e) {
			checkConnection();
			mysqlError(e);
		}
		return set;
	}

    /**
     * Show nicer error messages for mysql errors
     * @param e
     */
    public static void mysqlError(SQLException e){
    	Logger logger = plugin.getLogger();
        logger.severe("=========================================");
        logger.severe("              DATABASE ERROR             ");
        logger.severe("=========================================");
        logger.severe("An error occured while trying to connect to the DroxPerms database");
        logger.severe("Mysql error code: "+ e.getErrorCode());

        switch(e.getErrorCode()){
        case 1042:logger.severe("Cannot find hostname provided");break;
        case 1044:
        case 1045:logger.severe("Cannot connect to database, check user credentials, database exists and that user is able to log in from this remote machine");break;
        case 1049:logger.severe("Cannot locate database, check you spelt it correctly and username has access rights.");break;

        default:logger.severe("Error code not found, either check the error code online, or post on the dev.bukkit.org/server-mods/beardstat page");break; 
        }

        logger.severe("=========================================");
        logger.severe("            Begin error dump             ");
        logger.severe("=========================================");
        e.printStackTrace();
        logger.severe("=========================================");
        logger.severe("             End error dump              ");
        logger.severe("=========================================");

    }
}

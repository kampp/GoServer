package com.mc2.dev.goserver;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class DBConnector {

	//-------------------------------------------------------
	// singleton attributes
	private static DBConnector singleton;
	
	public static DBConnector getInstance() {
		if (singleton == null) {
			singleton = new DBConnector();
		}
		return singleton;
	}
	//-------------------------------------------------------
	
	private static final Logger LOGGER = Logger.getLogger( DBConnector.class.getName() );
	
	private Connection connection;
	private String url = "jdbc:mysql://localhost:3306/server_data";
	private String user = "root";
	private String password = "";
	
	public DBConnector() {
		connect();
	}
	
	public boolean connect() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			connection = DriverManager.getConnection(url, user, password);
		}
		catch (SQLException sqlEx) {
			LOGGER.log(Level.ALL, sqlEx.getMessage());
			return false;
		}
		catch (Exception e) {
			LOGGER.log(Level.ALL, e.getMessage());
		}
		return true;
	}
	
	public boolean reconnect() {
		if (connection != null) {
			disconnect();
		}
		return connect();
	}
	
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException sqlEx) {
				LOGGER.log(Level.ALL, sqlEx.getMessage());
			}
		}

	}
	
	//-------------------------------------------------------
	// boolean insertMatchRequest
	// String token: the firebase token of the client app
	// metaInformation: all gameMetaInformation elements
	//
	// inserts a client app into the match_requested table
	//
	// return true if the insert was successfull
	//-------------------------------------------------------
	public boolean insertMatchRequest(String token, String jsonString) {
    	JSONParser parser = new JSONParser();
    	String boardSizeStr = "";
    	String playerName = "";
    	String playerRankStr = "";
    	
    	try {
    		JSONObject jsobj = (JSONObject) parser.parse(jsonString);
    		boardSizeStr = (String) jsobj.get("boardisze");
        	playerName = (String) jsobj.get("nickname");
        	playerRankStr = (String) jsobj.get("rank");
    	}
    	catch (Exception e) {
    		LOGGER.log(Level.ALL, e.getMessage());
    		return false;
    	}
    	
    	int boardSize = Integer.parseInt(boardSizeStr);
    	int rank = Integer.parseInt(playerRankStr);
    	
    	try {
    		Statement stmt = connection.createStatement();
    		String query = "insert into match_requested values (" 
    		+ token + ","
    		+ System.currentTimeMillis() + ","
    		+ boardSize + ","
    		+ playerName + ","
    		+ rank + ");";
    		
    		return true;
    	}
    	catch (Exception e) {
    		LOGGER.log(Level.ALL, e.getMessage());
    		return false;
    	}
    	
	}
	
	//-------------------------------------------------------
	// boolean deleteMatchRequest
	// String token: the firebase token of the client app
	//
	// removes a registered client from the match_requested table
	//
	// return true if a client has been found and removed
	//-------------------------------------------------------
	public boolean deleteMatchRequest(String token) {

		try {
			Statement stmt = connection.createStatement();
			String query = "delete match_requested where token = " + token;
			stmt.executeQuery(query);
			return true;
		}
		catch (Exception e) {
			LOGGER.log(Level.ALL, e.getMessage());
			return false;
		}
	}
	
	
	//-------------------------------------------------------
	// ResultSet getMatchRequested
	// 
	// return a ResultSet containing all entries
	// of the match_requested table
	// the first element in the result set is the last one inserted
	//-------------------------------------------------------
	public ResultSet getMatchRequested()  {
		try {
			Statement stmt = connection.createStatement();
			String query = "select * from match_requested order by created_at desc";
			return stmt.executeQuery(query);
		}
		catch (Exception e) {
			LOGGER.log(Level.ALL, e.getMessage());
			return null;
		}
	}
	
	
	public String getTestString() {
		
		String query = "select * from test_data where id = 1";
		try {
			Statement st = singleton.connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				return rs.getString("text");
			}
		}
		catch(SQLException sqlEx) {
			LOGGER.log(Level.ALL, sqlEx.getMessage());
		}
		
		return "the mouse is dead";
	}
	

}

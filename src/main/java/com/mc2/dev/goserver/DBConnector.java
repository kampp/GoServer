package com.mc2.dev.goserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mariadb.jdbc.MySQLDataSource;

import com.mc2.dev.gogame.MoveNode;
import com.mc2.dev.gogame.RunningGame;


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
    	String playerName = "";
    	Long boardSize;
    	Long playerRank;
    	
    	try {
    		JSONObject jsobj = (JSONObject) parser.parse(jsonString);
        	playerName = (String) jsobj.get("nickname");
    		boardSize = (Long) jsobj.get("boardsize");
        	playerRank = (Long) jsobj.get("rank");
    	}
    	catch (Exception e) {
    		return false;
    	}
    	
    	
    	try {
    		Statement stmt = connection.createStatement();
    		String query = "insert into match_requested values (null," 
    		+ "'" + token + "',"
    		+ "'" + new Timestamp(System.currentTimeMillis()).toString() + "',"
    		+ "'" + boardSize + "',"
    		+ "'" + playerName + "',"
    		+ "'" + playerRank + "');";
    		
    		stmt.execute(query);
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
			String query = "delete from match_requested where token like '" + token + "';";
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
	
	//-------------------------------------------------------
	// boolean insertMoveNode
	// 
	// inserts the given MoveNode into the database
	//-------------------------------------------------------
	public boolean insertMoveNode(MoveNode move, int gameID, int parentID) {
		try {
    		Statement stmt = connection.createStatement();
    		String query = "insert into movenodes values (null," 
    		+ "'" + gameID + "',"
    		+ "'" + parentID + "',"
    		+ "'" + move.getPosition()[0] + "',"
    		+ "'" + move.getPosition()[1] + "',";
    	     		
    		if (move.isBlacksMove()) {
    			query += "'1');";
    		}
    		else {
    			query += "'0');";
    		}
    		
    		ResultSet rs = stmt.executeQuery(query);
    		return true;
    	}
    	catch (Exception e) {
    		LOGGER.log(Level.ALL, e.getMessage());
    		return false;
    	}
	}
	
	//-------------------------------------------------------
	// int insertGame
	// 
	// inserts the given game into the database
	// returns the id of the entry
	//-------------------------------------------------------
	public int insertGame(RunningGame game, String tokenA, String tokenB, int boardSize) {
		try {
						
			String query = "insert into running_games (created_at, tokenA, tokenB, boardSize, prisonerA, prisonerB) values (?,?,?,?,0,0);";
    		PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
    		stmt.setString(2, tokenA);
    		stmt.setString(3, tokenB);
    		stmt.setInt(4, boardSize);
    		stmt.execute();
    		
    		ResultSet rs = stmt.getGeneratedKeys();
    		if (rs.next()) {
    		   return rs.getInt(1);
    		}
    		else {
    			return 0;
    		}
    	}
    	catch (Exception e) {
    		LOGGER.log(Level.ALL, e.getMessage());
    		System.out.println("Failure in DBConnector: " + e.getMessage());
    		return -1;
    	}
	}
	

	//-------------------------------------------------------
	// int getGameIDbyToken
	// 
	// returns the game id of the game which contains token as
	// a player. returns 0, if no game is found
	//-------------------------------------------------------
	public int getGameIDbyToken(String token) {
	 	String query = "select id from running_games where tokenA = ? or tokenB = ?;";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, token);
			stmt.setString(2, token);
			stmt.execute();
		 	
		 	ResultSet rs = stmt.getResultSet();
		 	while (rs.next()) {
		 		return rs.getInt(1);
	 		}
	 	}
	 	catch(SQLException sqlEx) {
	 		System.out.println("getGameIDByToken: " + sqlEx.getMessage());
	 		LOGGER.log(Level.ALL, sqlEx.getMessage());
	 	}
	 	return -1;
	}
	
	//-------------------------------------------------------
	// String[] getTokensByGameID
	// 
	// returns a String array containing the tokens of both 
	// players participating in the game indicated by gameID.
	// Returns null if gameID does not exist or on Error. 
	//-------------------------------------------------------
	public String[] getTokensByGameID(int gameID) {
		String query = "select * from running_games where id = ?;";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, gameID);
			stmt.execute();
			
			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				return new String[]{rs.getString("tokenA"), rs.getString("tokenB")};
			}
		} catch (SQLException sqlE) {
			System.out.println("getTokenByGameID: " + sqlE.getMessage());
			LOGGER.log(Level.ALL, sqlE.getMessage());
		}
		return null;
	}
	
	//-------------------------------------------------------
	// boolean insertPrisonerCount
	// 
	// sets the prisoner count in the given game for the player
	// given in the move
	// 
	// returns true if the db-update was successful, false otherwise
	//-------------------------------------------------------
	public boolean setPrisonerCount(int gameID, JSONObject inputMove) {
		int count = Integer.parseInt(inputMove.get("prisonerCount").toString());
		String query = "";
		
		if (inputMove.get("isBlacksMove") == "false") {
			query = "update running_games where id = " + gameID + " set prisonerA = " + count + ";";   
		}
		else {
			query = "update running_games where id = " + gameID + " set prisonerB = " + count + ";";  
		}
		
		try {
			Statement st = singleton.connection.createStatement();
			st.executeQuery(query);
			return true;
		}
		catch (Exception e) {
			System.out.println("setPrisonerCount: " + e.getMessage());
			LOGGER.log(Level.ALL, e.getMessage());
		}
	
		return false;
		
	}
	
	//-------------------------------------------------------
	// String getLatestMove
	// 
	// returns the latest MoveNode of the given game as
	// json-formatted string
	//-------------------------------------------------------
	public JSONObject getLatestMove(int gameID) {
		String query = "select * from movenodes where gameID = "+ gameID + ";";
		JSONObject json = new JSONObject();
		
		try {
			Statement st = singleton.connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				json.put("id", rs.getInt("id"));
				json.put("posX", rs.getInt("posX"));
				json.put("posY", rs.getInt("posY"));
				json.put("isBlacksMove", rs.getInt("isBlacksMove"));
			}
		}
		
		catch(SQLException sqlEx) {
			System.out.println("getLatestMoveNode: " + sqlEx.getMessage());
			LOGGER.log(Level.ALL, sqlEx.getMessage());
		}
		
		return json;
	}
	
	//-------------------------------------------------------
	// ArrayList<MoveNode getMoveNodes
	// 
	// gets all move nodes belonging to the given game
	//-------------------------------------------------------
	public ArrayList<MoveNode> getMoveNodes(int gameID) {
			return null;
	}
	
}

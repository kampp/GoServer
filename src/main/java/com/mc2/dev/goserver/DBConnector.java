package com.mc2.dev.goserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    		+ "'" + move.getPosition()[1] + "',"
    		+ "'" + move.isBlacksMove() + "');"
    		+ "'" + move.getPosition()[1] + "',";
    		     		
    		 if (move.isBlacksMove()) {
    		     	query += "'1');";
    		  }
    		  else {
    			  query += "'1');";
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
			String query = "insert into running_games values (null,?,?,?)";
    		PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		stmt.setString(1, tokenA);
    		stmt.setString(2, tokenB);
    		stmt.setInt(3, boardSize);
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
    		return 0;
    	}
	}
	

	//-------------------------------------------------------
	// int getGameIDbyToken
	// 
	// returns the game id of the game which contains token as
	// a player. returns 0, if no game is found
	//-------------------------------------------------------
	public int getGameIDbyToken(String token) {
	 	String query = "select * from test_data where token = "+ token + ";";
	 	try {
	 	Statement st = singleton.connection.createStatement();
	 	ResultSet rs = st.executeQuery(query);
	 	while (rs.next()) {
	 		return rs.getInt("id");
	 		}
	 	}
	 	catch(SQLException sqlEx) {
	 		LOGGER.log(Level.ALL, sqlEx.getMessage());
	 	}
	 		return 0;
	}
	
	//-------------------------------------------------------
	// String getLatestMove
	// 
	// returns the latest MoveNode of the given game as
	// json-formatted string
	//-------------------------------------------------------
	public JSONObject getLatestMove(int gameID) {
		String query = "select * from test_data where id = "+ gameID + ";";
		JSONObject json = new JSONObject();
		
		try {
			Statement st = singleton.connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				json.put("id", rs.getInt("id"));
				json.put("posX", rs.getInt("posX"));
				json.put("posY", rs.getInt("posY"));
				json.put("isBlacksNove", rs.getBoolean("isBlacksMove"));
			}
		}
		
		catch(SQLException sqlEx) {
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

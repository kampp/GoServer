package com.mc2.dev.goserver;

import java.sql.*;

import org.apache.tomcat.jdbc.*;

public class DBConnector {

	//-------------------------------------------------------
	// singleton attributes
	private DBConnector singleton;
	
	public DBConnector getInstance() {
		if (singleton == null) {
			singleton = new DBConnector();
		}
		return singleton;
	}
	//-------------------------------------------------------
	
	private Connection connection;
	private String url;
	private String user;
	private String password;
	
	public boolean connect() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		}
		catch (SQLException sqlEx) {
			// TODO push message to logger
			System.out.println(sqlEx.getMessage());
			return false;
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
				// TODO push message to logger
				System.out.println(sqlEx.getMessage());
			}
		}

	}
	
	//-------------------------------------------------------
	// int insertMatchRequest
	// String token: the firebase token of the client app
	// metaInformation: all gameMetaInformation elements
	//
	// inserts a client app into the match_requested table
	//
	// return the entry-id or 0 if failed
	//-------------------------------------------------------
	public int insertMatchRequest(String token, GameMetaInformation metaInformation) {
		return 0;
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
		return true;
	}
	
	
	//-------------------------------------------------------
	// ResultSet getMatchRequested
	// 
	// return a ResultSet containing all entries
	// of the match_requested table
	//-------------------------------------------------------
	public ResultSet getMatchRequested()  {
		return null;
	}
	
	

}

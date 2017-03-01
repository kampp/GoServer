package com.mc2.dev.resource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mc2.dev.goserver.DBConnector;

//-------------------------------------------------------
// class FirebaseMsgService 
//
// this class serves as a wrapper for the firebase messaging service
// it contains Methods to send GoApp conform messages to the clients of
// type GoApp
//
//
//-------------------------------------------------------
public class FirebaseMsgService {
	
	private static final Logger LOGGER = Logger.getLogger( DBConnector.class.getName() );
	private static final String SERVERKEY = "AAAAo1itxNg:APA91bFmvy88fJirDEJSCpxFNVQHjrdHuvJxk0MJ9lT6sSGCH8PYYs82IWd_YV6ljRyx9V1KreQ1ZzL-ZouxkjNo4XU_-kVEFJTBF1yHAMN2PYdJcK-FSHnI_a6znJQ1l4DLO4xUtb7RQC0t0x8fdUWjqjfWQdhTHg";
	private static final String FIREBASEURL = "https://fcm.googleapis.com/fcm/send";	
	
	private boolean send(String token, JSONObject jsobj) {
		HttpURLConnection connection = null;
		
		jsobj.put("to", token);
		
		 try {
		    //Create connection
		    URL url = new URL(FIREBASEURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Authorization", "key=" + SERVERKEY);
		    
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(jsobj.toJSONString());
		    wr.close();		

		 } catch (Exception e) {
			 LOGGER.log(Level.ALL, e.getMessage());
			 return false;
		 }
		 
		 // catch response
		 try {
			InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    System.out.println("Response: " + response.toString() + "\nStatusCode: " + connection.getResponseCode());
		    return checkResponse(response.toString());
		  } catch (Exception e) {
			  LOGGER.log(Level.ALL, e.getMessage());
			  return false;
		  }
		 
	}

	//-------------------------------------------------------
	// boolean checkResponse 
	//
	// returns true, if the response from the firebase-server 
	// confirms the message has been succesfully send
	//
	// String response : response from firebase server
	//-------------------------------------------------------
	private boolean checkResponse(String response) {
		return false;
	}
	
	
	//-------------------------------------------------------
	// void sendTest
	// TODO remove after usage
	// this Method is only for testing the firebase messages
	//-------------------------------------------------------
	public void sendTest(String token) {
		
		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();

		data.put("type", "matched");
		data.put("start", true);
		data.put("whiteName", "wawawa");
		data.put("whiteRank", "2");
		output.put("data", data);
		
		send(token, output);
	}

	//-------------------------------------------------------
	// notifyPairingSuccess
	//
	// send a message to both clients given in token1 and token2
	// if a match if found between them 
	//
	// token1 : firebase token of client app
	// token2 : firebase token of client app
	// aStart : if set to true, the first token is notified, that the player
	//			will begin with the first move
	//-------------------------------------------------------
	public boolean notifyPairingSuccess(String token1, String token2, boolean aStart) {
		
		boolean success1 = false;
		boolean success2 = false;
		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();
		// TODO which user starts? send the info to the user
		
		
		data.put("type", "matched");
		data.put("start", aStart);
		output.put("data", data);
		
		if (send(token1, output)) {
			success1 = true;
		}
		// TODO else: retry
		
		output.clear();
		data.remove("start");
		data.put("start", !aStart);
		output.put("data", data);
		if (send(token2, output)) {
			success1 = true;
		}
		// TODO else: retry
		
		return success1 && success2;
	}
	
	//-------------------------------------------------------
	// notifyMatchTimeout
	//
	// send a message to the client, that no match (other player)
	// has been found during the given time
	//
	// token: firebase token of client app
	//-------------------------------------------------------
	public boolean notifyMatchTimeout(String token) {
		
		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();

		data.put("type", "match_timeout");
		output.put("data", data);
		
		if (send(token, output)) {
			return true;
		}
		// TODO else : retry
		return false;
	}
	
	
	//-------------------------------------------------------
	// notifyMovePlay
	//
	// send a message to the client, that the opponent has played 
	//
	// token: firebase token of client app
	//-------------------------------------------------------
	public boolean notifyMovePlayed(String token) {

		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();

		data.put("type", "move_played");
		output.put("data", data);
		
		if (send(token, output)) {
			return true;
		}
		// TODO else : retry
		return false;
	}
	
	
	//-------------------------------------------------------
	// notifyPlayTimeout
	//
	// send a message to the client, that the opponent has not
	// played within the given timelimit
	//
	// token: firebase token of client app
	//-------------------------------------------------------
	public boolean notifyPlayTimeout(String token) {
		
		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();

		data.put("type", "play_timeout");
		output.put("data", data);
		
		if (send(token, output)) {
			return true;
		}
		// TODO else : retry
		return false;
	}
	
}

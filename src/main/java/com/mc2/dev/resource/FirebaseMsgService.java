package com.mc2.dev.resource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	
	private boolean send(String token, JSONObject jsobj) {
		String urlStr = "https://fcm.googleapis.com/fcm/send";
		String serverKey ="AAAAo1itxNg:APA91bFmvy88fJirDEJSCpxFNVQHjrdHuvJxk0MJ9lT6sSGCH8PYYs82IWd_YV6ljRyx9V1KreQ1ZzL-ZouxkjNo4XU_-kVEFJTBF1yHAMN2PYdJcK-FSHnI_a6znJQ1l4DLO4xUtb7RQC0t0x8fdUWjqjfWQdhTHg";
		HttpURLConnection connection = null;
		
		jsobj.put("to", token);
		
		 try {
		    //Create connection
		    URL url = new URL(urlStr);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Authorization", "key=" + serverKey);
		    
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(jsobj.toJSONString());
		    wr.close();		

		 } catch (Exception e) {
			 // TODO push to logger
			 System.out.println(e.getMessage());
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
		    return checkResponse(response.toString());
		  } catch (Exception e) {
			  // TODO push to logger
		    e.printStackTrace();
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
	// notifyPairingSuccess
	//
	// send a message to both clients given in token1 and token2
	// if a match if found between them 
	//
	// token1 : firebase token of client app
	// token2 : firebase token of client app
	//-------------------------------------------------------
	public boolean notifyPairingSuccess(String token1, String token2) {
		
		boolean success1 = false;
		boolean success2 = false;
		JSONObject data = new JSONObject();
		JSONObject output = new JSONObject();
		// TODO which user starts? send the info to the user
		
		
		data.put("type", "matched");
		data.put("start", true);
		output.put("data", data);
		
		if (send(token1, output)) {
			success1 = true;
		}
		// TODO else: retry
		
		output.clear();
		data.remove("start");
		data.put("start", false);
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

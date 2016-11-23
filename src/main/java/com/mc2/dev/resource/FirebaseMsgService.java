package com.mc2.dev.resource;

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
		return false;
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
		return false;
	}
	
}

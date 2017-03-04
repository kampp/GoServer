package com.mc2.dev.goserver;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mc2.dev.gogame.GameMetaInformation;
import com.mc2.dev.resource.FirebaseMsgService;

//-------------------------------------------------------
// class Matcher
//
// is called as thread that simply dies after finishing its task
// on run() every entry in match_requested will be checked for
// as possible match 
// if a match is found, the game will be created and both 
// participants will be notified
//-------------------------------------------------------
public class Matcher implements Runnable {
	
	private static Logger LOGGER = Logger.getLogger( Matcher.class.getName() );
	
	private static long TIMEOUTITME = 300000; // 5 minutes

	public void run() {
		
		FirebaseMsgService fms = new FirebaseMsgService();
		ResultSet entries = DBConnector.getInstance().getMatchRequested();
		
		try {
			entries.next(); // get first element, which is inserted the latest
			String tokenA = entries.getString("token");
			String nameA = entries.getString("player_name");
			int boardSize = entries.getInt("board_size");
			int rankA = entries.getInt("rank");
		
			String tokenB = "";
			String nameB = "";
			int rankB = 0;
			boolean matched = false;
			
			Timestamp currentSysTime = new Timestamp(System.currentTimeMillis());
			long time = currentSysTime.getTime() - TIMEOUTITME;
			Timestamp currentTime = new Timestamp(time);
			
			while (entries.next()) {
				if (entries.getTimestamp("created_at").before(currentTime)) {
					tokenB = entries.getString("token");
					fms.notifyMatchTimeout(tokenB);
					DBConnector.getInstance().deleteMatchRequest(tokenB);
					continue;
				}			

				if (entries.getInt("board_size") == boardSize) {
					tokenB = entries.getString("token");
					nameB = entries.getString("player_name");
					rankB = entries.getInt("rank");
					matched = true;
					break;
				}
				
			}
			if (matched) {
				GameMetaInformation gmi = new GameMetaInformation();
				gmi.setBoardSize(boardSize);
				Random rand = new Random();
				boolean playerAStarting = rand.nextBoolean();
				if (playerAStarting)
				{
					gmi.setBlackName(nameA);
					gmi.setWhiteName(nameB);
				} else {
					gmi.setBlackName(nameB);
					gmi.setWhiteName(nameA);
				}
				DBConnector.getInstance().deleteMatchRequest(tokenA);
				DBConnector.getInstance().deleteMatchRequest(tokenB);
				fms.notifyPairingSuccess(tokenA, tokenB, playerAStarting, nameA, nameB, rankA, rankB);
				
				GameController.getInstance().createOnlineGame(gmi, tokenA, tokenB);
			}
		}
		catch (Exception e) {
			LOGGER.log(Level.ALL, e.getMessage());
		}
	}

}

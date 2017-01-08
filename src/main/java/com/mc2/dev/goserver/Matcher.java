package com.mc2.dev.goserver;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mc2.dev.resource.FirebaseMsgService;

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
			String nameB;
			int rankB;
			
			boolean matched = false;
			
			while (entries.next()) {
				if (System.currentTimeMillis() - TIMEOUTITME > entries.getLong("created_at")) {
					tokenB = entries.getString("token");
					fms.notifyMatchTimeout(tokenB);
					DBConnector.getInstance().deleteMatchRequest(tokenB);
					continue;
				}
				
				if (entries.getInt("board_size") == boardSize) {
					// 	TODO check if rank is near enough
					tokenB = entries.getString("token");
					nameB = entries.getString("player_name");
					rankB = entries.getInt("rank");
					matched = true;
				}
			}
			if (matched) {
				// TODO generate meta info
				DBConnector.getInstance().deleteMatchRequest(tokenA);
				DBConnector.getInstance().deleteMatchRequest(tokenB);
				fms.notifyPairingSuccess(tokenA, tokenB, true);
			}
		}
		catch (Exception e) {
			LOGGER.log(Level.ALL, e.getMessage());
		}
	}

}

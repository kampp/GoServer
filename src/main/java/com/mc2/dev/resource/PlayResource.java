package com.mc2.dev.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mc2.dev.gogame.MoveNode;
import com.mc2.dev.goserver.DBConnector;

@Path("play")
public class PlayResource implements IPlayResource {

    @GET
    @Path("{token}")
	public Response getPlayByToken(@PathParam("token") String token) throws Exception {
		int gameID = DBConnector.getInstance().getGameIDbyToken(token);
		JSONObject jsonMoveNode = DBConnector.getInstance().getLatestMove(gameID);
		return Response.ok(jsonMoveNode, MediaType.APPLICATION_JSON).build();
	}

    @POST
    @Path("{token}")
    @Consumes(	MediaType.APPLICATION_JSON	)
	public Response postPlayByToken(@PathParam("token") String token, String jsonString) throws Exception {
    	
    	jsonString.replace("\\", "");
    	JSONParser parser = new JSONParser();
    	JSONObject jsonMove = (JSONObject) parser.parse(jsonString);
    	System.out.println("");
    	System.out.println(jsonMove.toJSONString());
    	
		int gameID = DBConnector.getInstance().getGameIDbyToken(token);
		System.out.println("gameID: " + gameID);
		JSONObject lastMoveNode = DBConnector.getInstance().getLatestMove(gameID);
		System.out.println("lastMoveNode: " + lastMoveNode.toJSONString());
		int parentID = Integer.parseInt(lastMoveNode.get("id").toString());
		System.out.println("parentID: "+ parentID);
		
		DBConnector.getInstance().setPrisonerCount(gameID, jsonMove);
		DBConnector.getInstance().insertMoveNode(new MoveNode(jsonMove), gameID, parentID);
		
		FirebaseMsgService fmsg = new FirebaseMsgService();
		String tokens[] = DBConnector.getInstance().getTokensByGameID(gameID);
		if (token.equals(tokens[0])) {
			if (fmsg.notifyMovePlayed(tokens[1])) return Response.ok().build();
		} else {
			if (fmsg.notifyMovePlayed(tokens[0])) return Response.ok().build();
		}
		
		return Response.serverError().build();
	}

}

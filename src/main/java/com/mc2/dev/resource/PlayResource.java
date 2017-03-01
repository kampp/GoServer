package com.mc2.dev.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

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
		int gameID = DBConnector.getInstance().getGameIDbyToken(token);
		//DBConnector.getInstance().insertPrisonerCount(gameID, jsonString);
		JSONObject lastMoveNode = DBConnector.getInstance().getLatestMove(gameID);
		return null;
	}

}

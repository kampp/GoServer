package com.mc2.dev.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.mc2.dev.goserver.DBConnector;
import com.mc2.dev.goserver.Matcher;


@Path("match")
public class MatchResource implements IMatchResource {

    @POST
    @Path("{token}")
    @Consumes(	MediaType.APPLICATION_JSON	)
	public Response postMatch(@PathParam("token") String token, String jsonString) throws Exception {
		
    	if (DBConnector.getInstance().insertMatchRequest(token, jsonString)) {
    		Matcher matcher = new Matcher();
    		matcher.run();
    		return Response.ok().build();
    	}
    	return Response.serverError().build();
	}

    @DELETE
    @Path("{token}")
	public Response deleteMatchByToken(@PathParam("token") String token) throws Exception {
		
    	if (DBConnector.getInstance().deleteMatchRequest(token)) {
    		return Response.ok().build();
    	};
    	
    	return Response.noContent().build();
	}
    
    @GET
    @Path("testFirebase")
	public String testIt() {
    	FirebaseMsgService fms = new FirebaseMsgService();
    	fms.sendTest("etDwyj6LdeU:APA91bHpLEyxnrAiCp55PzhabFlA6yThyYyFBmzH9-YIweSWC58NRRoT1a7P1mczFEL1FugokIozITl9oOQOUoUspKqbnQPqmIL9A2zUNBQlwo9A_lBeR0FeXP79lKtLtOZieXB6wJ84");
		return "";
	}

}

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
    @Path("test")
	public String testIt() {
    	FirebaseMsgService fms = new FirebaseMsgService();
    	fms.sendTest("hardcode");
		return DBConnector.getInstance().getTestString();
	}
    
    @GET
    @Path("testFirebase")
	public String testIT2() {
    	FirebaseMsgService fms = new FirebaseMsgService();
    	fms.sendTest("ckgrSw6RDDY:APA91bG6OZb4FnGq7IPoDyEjH6OSd_Ezi3AsI96OHHqK0Xlz4dscPMwqVh1Cw42uwGtBdjJ--TvfKuKNHbkch4b7VARuUxZuylvjY3IuZI1NXUQIxLApbHirVJ2IXPwew26VOy6LJQUG");
		return "";
	}

}

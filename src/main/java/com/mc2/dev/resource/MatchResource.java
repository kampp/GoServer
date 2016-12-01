package com.mc2.dev.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mc2.dev.goserver.DBConnector;


@Path("match")
public class MatchResource implements IMatchResource {

    @POST
    @Consumes(	MediaType.APPLICATION_JSON	)
	public Response postMatch() throws Exception {
		return null;
	}

    @DELETE
    @Path("{token}")
	public Response deleteMatchByToken(String token) throws Exception {
		
    	if (DBConnector.getInstance().deleteMatchRequest(token)) {
    		Response.ok();
    	};
    	
    	return null;
	}

    @GET
    @Path("test")
	public String testIt() {
		return DBConnector.getInstance().getTestString();
	}

}

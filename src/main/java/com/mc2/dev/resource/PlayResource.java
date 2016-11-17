package com.mc2.dev.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("play")
public class PlayResource implements IPlayResource {

    @GET
    @Path("{token}")
	public Response getPlayByToken(String token) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    @POST
    @Path("{token}")
	public Response postPlayByToken(String token) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

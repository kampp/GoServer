
package com.mc2.dev.resource;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;

public interface IMatchResource {

	String testIt();
	
    /**
     * send data for matchmaking
     * 
     */
    Response postMatch() throws Exception;

    /**
     * get matched partner
     * 
     * @param token
     *     
     */
    Response getMatchByToken(@PathParam("token")String token)throws Exception;

    /**
     * delete user entry from queue
     * 
     * @param token
     *     
     */
    Response deleteMatchByToken(@PathParam("token")String token)  throws Exception;



}

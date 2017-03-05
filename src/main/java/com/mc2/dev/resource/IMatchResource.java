
package com.mc2.dev.resource;

import javax.ws.rs.core.*;

public interface IMatchResource {
	
    /**
     * send data for matchmaking
     * 
     * @param token
     * @param message
     * 
     */
    Response postMatch(String token, String message) throws Exception;

    /**
     * delete user entry from queue
     * 
     * @param token
     *     
     */
    Response deleteMatchByToken(String token)  throws Exception;



}

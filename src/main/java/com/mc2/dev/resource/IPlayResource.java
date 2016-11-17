
package com.mc2.dev.resource;


import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


public interface IPlayResource {


    /**
     * get move information done by enemy
     * 
     * @param token
     *     
     */
    Response getPlayByToken(@PathParam("token")String token)throws Exception;

    /**
     * send move information
     * 
     * @param token
     *     
     */
    Response postPlayByToken(@PathParam("token")String token)throws Exception;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.utilities;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Hp
 */
public class ReturnConfig {
    public static final Response isSuccess( String outPut){
        return Response.ok(outPut, MediaType.WILDCARD_TYPE).build();
    }
    public static final Response isFailed(Status statusCode, String cause){
        return Response.status(statusCode).entity("Error: "+cause).build();
    }
}

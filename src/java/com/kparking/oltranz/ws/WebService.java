/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.ws;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.logic.AppReceiver;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hp
 */
@Path("/system")
@Stateless
public class WebService {
    @EJB
            AppReceiver appReceiver;
    @POST
    @Path("/ussdCarIn")
    public Response ussdCarIn(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.clientCarIn(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/ussdCarOut")
    public Response ussdCarOut(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.clientCarOut(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/checkCar")
    public Response checkCar(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.checkCar(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @GET
    @Path("/schedCallBack")
    public Response schedCallBack(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.scheduleCallBack(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.ws;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.logic.AppReceiver;
import com.kparking.oltranz.logic.CurrentCar;
import com.kparking.oltranz.logic.DependencyProcessor;
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
    @EJB
            DependencyProcessor dependencyProcessor;
    @EJB
            CurrentCar currentCar;
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
    
    @GET
    @Path("/progressCallBack")
    public Response progressCallBack(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.progressCallBack(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @GET
    @Path("/tempTicket")
    public Response tempTicket(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.tempTicket(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @GET
    @Path("/currentCar")
    public Response getCurrentCar(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return currentCar.getCurrentCar();//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/inParking")
    public Response inParking(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return currentCar.getCurrentCarPerDate(body);//.clientReceiver(headers, body);
    }
    
    @GET
    @Path("/tNotication")
    public Response tNotication(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.tNotification(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/getAllTickets")
    public Response getAllTickets(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.getAllTickets(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/verify")
    public Response verifyCar(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.verifyCar(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/verifyReport")
    public Response verifyReport(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.verifyCarReport(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @GET
    @Path("/getParkInfo")
    public Response getParkInfo(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.getParkInfo(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/getReport")
    public Response getReport(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.getReport(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/signUp")
    public Response signUpCar(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return appReceiver.signupCar(requestContext.getRemoteAddr(), requestContext.getRemotePort(), headers, body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/stateLink")
    public Response stateLink(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return dependencyProcessor.createLinkState(body);//.clientReceiver(headers, body);
    }
    
    @POST
    @Path("/getDependecy")
    public Response getDependecy(@Context HttpServletRequest requestContext, @Context HttpHeaders headers, String body){
        out.print(AppDesc.APP_DESC+" Source IP: " + requestContext.getRemoteAddr() + ", Port: " + requestContext.getRemotePort() + ", Host: " + requestContext.getRemoteHost());
        out.print(AppDesc.APP_DESC+" Node Received Headers "+headers.toString()+" and Body  "+body);
        return dependencyProcessor.getDepencyStatus(body);//.clientReceiver(headers, body);
    }
}

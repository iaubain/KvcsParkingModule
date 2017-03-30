/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hp
 */
@Stateless
public class AppReceiver {
    @EJB
            AppProcessor appProcessor;
    @EJB
            CallBackHandler callBackHandler;
    public Response clientCarIn(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return appProcessor.carInprocessor(headers, body);
    }
    public Response clientCarOut(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return appProcessor.carOutprocessor(headers, body);
    }
    public Response checkCar(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return appProcessor.checkCar(headers, body);
    }
    public Response signupCar(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return appProcessor.signupCar(headers, body);
    }
    public Response scheduleCallBack(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return callBackHandler.callBackreceiver(headers);
    }
    public Response progressCallBack(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return callBackHandler.progressCallBack(headers);
    }
    public Response tempTicket(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return callBackHandler.tempTicket(headers);
    }
     public Response tNotification(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return callBackHandler.tNotification(headers);
    }
    public Response getAllTickets(String sourceIp, int sourcePort, HttpHeaders headers, String body){
        return appProcessor.getAllTickets();
    }
}

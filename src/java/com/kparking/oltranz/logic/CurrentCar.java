/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.currentcar.CurrentCarData;
import com.kparking.oltranz.simplebeans.currentcar.CurrentCarRequest;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class CurrentCar {
    @EJB
    TicketFacade ticketFacade;

    public Response getCurrentCar(){
        try {
            List<CurrentCarData> mCurrentCar = new ArrayList<>();
            List<Ticket> mTickets = ticketFacade.getCurrentTicket();
            for(Ticket ticket : mTickets){
                mCurrentCar.add(new CurrentCarData(ticket.getNumberPlate(), ticket.getTicketType(), ticket.getMsisdn()));
            }
            return ReturnConfig.isSuccess(DataFactory.objectToString(mCurrentCar));
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CurrentCar getCurrentCar process failed due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, e.getMessage());
        }
    }
    
    public Response getCurrentCarPerDate(String body){
        try {
            CurrentCarRequest cCar = (CurrentCarRequest) DataFactory.stringToObject(CurrentCarRequest.class, body);
            List<CurrentCarData> mCurrentCar = new ArrayList<>();
            SimpleDateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");            
            List<Ticket> mTickets = ticketFacade.getCurrentTicket(dFormat.format(dFormat.parse(cCar.getPointDate())));
            if(mTickets == null)
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "No car in parking.");
            for(Ticket ticket : mTickets){
                mCurrentCar.add(new CurrentCarData(ticket.getNumberPlate(), ticket.getTicketType(), ticket.getMsisdn()));
            }
            return ReturnConfig.isSuccess(DataFactory.objectToString(mCurrentCar));
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CurrentCar getCurrentCar process failed due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, e.getMessage());
        }
    }
}

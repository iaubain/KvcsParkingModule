/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.ticketsreport.PublishTicketRequest;
import com.kparking.oltranz.simplebeans.ticketsreport.PublishTicketResponse;
import static java.lang.System.out;
import javax.ejb.EJB;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class BackGroundPublishTicket implements Runnable {
    @EJB
    ApInterface apInterface;
    @EJB
    TicketFacade ticketFacade;
    PublishTicketRequest publishTicketRequest;

    public BackGroundPublishTicket(ApInterface apInterface, TicketFacade ticketFacade, PublishTicketRequest publishTicketRequest) {
        this.apInterface = apInterface;
        this.ticketFacade = ticketFacade;
        this.publishTicketRequest = publishTicketRequest;
    }

    @Override
    public void run() {
        PublishTicketResponse publishTicketResponse = apInterface.publishTicket(publishTicketRequest);
        if(publishTicketResponse != null){
            out.print(AppDesc.APP_DESC+"BackGroundPublishTicket run going to edit ticket according to the publishTicketResponse");
            if(publishTicketResponse.getCode() == 100){
                Ticket ticket = ticketFacade.getTicketById(publishTicketRequest.getTicketId());
                if(ticket != null){
                    out.print(AppDesc.APP_DESC+"BackGroundPublishTicket run ticket: "+ticket.getTicketId()+" is being set to published.");
                    ticket.setRecorded(true);
                    ticketFacade.edit(ticket);
                    ticketFacade.refreshTicket();
                }
            }
        }
    }
    
}

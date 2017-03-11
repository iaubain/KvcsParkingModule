/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.HeaderConfig;
import com.kparking.oltranz.entities.Progressive;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.entities.UnfinishedTicket;
import com.kparking.oltranz.facades.ProgressiveFacade;
import com.kparking.oltranz.facades.UnfinishedTicketFacade;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class CallBackHandler {
    
    @EJB
            TicketManager ticketManager;
    @EJB
            ProgressiveFacade progressiveFacade;
    @EJB
            UnfinishedTicketFacade unfinishedTicketFacade;
    @EJB
            SmsSender smsSender;
    public Response callBackreceiver(HttpHeaders headers){
        if(headers == null){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received empty header.");
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
        }
        String jobId = headers.getHeaderString(HeaderConfig.JOB_ID);
        String status = headers.getHeaderString(HeaderConfig.JOB_STATUS);
        
        if(jobId == null){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received JobId: null with a status: "+status);
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
        }
        
        out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received JobId: "+jobId+" with a status: "+status);
        if(!ticketManager.genAdditionalTicket(jobId)){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver failed to generate additional tictet JobId: "+jobId+" with a status: "+status);
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Client faillure");
        }
        out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver succeeded to generate additional tictet JobId: "+jobId+" with a status: "+status);
        return ReturnConfig.isSuccess("Success");
    }
    
    public Response progressCallBack(HttpHeaders headers){
        try {
            if(headers == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack received empty header.");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            String jobId = headers.getHeaderString(HeaderConfig.JOB_ID);
            String status = headers.getHeaderString(HeaderConfig.JOB_STATUS);
            
            if(jobId == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack received JobId: null with a status: "+status);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack received JobId: "+jobId+" with a status: "+status);
            Progressive progressive = progressiveFacade.getProgressById(jobId);
            if(progressive == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack did not find Progressive with ID: "+jobId);
                return ReturnConfig.isSuccess("Success");
            }
            
            if(!progressive.isIsFinished()){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack did not find Progressive with ID: "+jobId);
                UnfinishedTicket unfinishedTicket = unfinishedTicketFacade.getUnFinishedTicketById(jobId);
                if(unfinishedTicket == null){
                    out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack creating initial unfinished ticket: "+jobId);
                    unfinishedTicket = new UnfinishedTicket(progressive.getProgressId(),
                            progressive.getInitMsisdn(),
                            progressive.getNumberPlate(),
                            progressive.getTicketType(),
                            progressive.getCreatedOn(),
                            progressive.getExpireIn(),
                            progressive.isIsFinished(),
                            0);
                    unfinishedTicketFacade.create(unfinishedTicket);
                    unfinishedTicketFacade.refreshUnFinTicket();
                }else{
                    out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack updating initial unfinished ticket: "+jobId+" with a count: "+unfinishedTicket.getCount());
                    if(unfinishedTicket.getCount() <= 0){
                        unfinishedTicket.setCount(unfinishedTicket.getCount()+1);
                        unfinishedTicketFacade.edit(unfinishedTicket);
                        unfinishedTicketFacade.refreshUnFinTicket();
                        
                        //send sms to conductor(Init ID)
                        Ticket ticket = new Ticket();
                        ticket.setMsisdn(progressive.getInitMsisdn());
                        if(unfinishedTicket.getCount()<=6){
                            Thread smsThread = new Thread(new BackgroundSMS(smsSender, null, ticket, "Imodoka: "+progressive.getNumberPlate()+" ifite agaticket utarangije. Wayandikiye: "+progressive.getCreatedOn(), true));
                            smsThread.start();
                        }else{
                            //send notification on admin eMail // I will implement this later
                        }
                    }
                    out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack removing progressive task: "+jobId+" of initiator: "+progressive.getInitMsisdn());
                    
                }
            }
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack succeeded to generate additional tictet JobId: "+jobId+" with a status: "+status);
            return ReturnConfig.isSuccess("Success");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack error occured due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
        }
    }
}

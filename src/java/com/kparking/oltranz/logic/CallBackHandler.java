/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.HeaderConfig;
import com.kparking.oltranz.entities.CallBackCounter;
import com.kparking.oltranz.entities.TempTicket;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.CallBackCounterFacade;
import com.kparking.oltranz.facades.ProgressiveFacade;
import com.kparking.oltranz.facades.TempTicketFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.facades.UnfinishedTicketFacade;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import net.manzi.frs.config.SessionDataStatus;
import net.manzi.frs.config.StepsConfig;
import net.manzi.frs.databeans.SessionTicketData;
import net.manzi.frs.databeans.userbeans.UserBean;
import net.manzi.frs.entities.SessionData;
import net.manzi.frs.entities.SessionStatus;
import net.manzi.frs.facades.SessionDataFacade;
import net.manzi.frs.facades.SessionStatusFacade;

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
    @EJB
            SessionStatusFacade sessionStatusFacade;
    @EJB
            SessionDataFacade sessionDataFacade;
    @EJB
            TempTicketFacade tempTicketFacade;
    @EJB
            ApInterface apInterface;
    @EJB
            TicketFacade ticketFacade;
    @EJB
            CustomerProvider customerProvider;
    @EJB
            CallBackCounterFacade callBackCounterFacade;
    
    public Response callBackreceiver(HttpHeaders headers){
        if(headers == null){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received empty header.");
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
        }
        String jobId = headers.getHeaderString(HeaderConfig.JOB_ID);
        String status = headers.getHeaderString(HeaderConfig.JOB_STATUS);
        
        if(jobId == null){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received JobId: null with a status: "+status);
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty jobId in headers");
        }
        
        CallBackCounter callBackCounter = callBackCounterFacade.getLastSession(jobId);
        if(callBackCounter == null){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received callbackCounter: "+jobId+" is being created due to empty result");
            callBackCounter = new CallBackCounter(jobId, 1, new Date());
            callBackCounterFacade.create(callBackCounter);
            callBackCounterFacade.refreshCounter();
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver created callback counter instance: "+jobId);
            return ReturnConfig.isSuccess("Success");
        }
        if(callBackCounter.getCallbackCount() >= 1){
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver received JobId: "+jobId+" with a status: "+status);
            if(!ticketManager.genAdditionalTicket(jobId)){
                out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver failed to generate additional tictet JobId: "+jobId+" with a status: "+status);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Client faillure");
            }
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver succeeded to generate additional tictet sessionId: "+jobId);
            return ReturnConfig.isSuccess("Success");
        }else{
            out.print(AppDesc.APP_DESC+"CallBackHandler callBackreceiver callback counter has invalid data: "+callBackCounter.getCallbackCount());
            return ReturnConfig.isSuccess("Success");
        }        
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
            
            String sessionId = DataFactory.splitString(jobId, "^")[1];
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack sessionId: "+sessionId);
            
            SessionStatus sessionStatus = sessionStatusFacade.getLastSession(sessionId);
            if(sessionStatus == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack did not find Progressive with ID: "+sessionId);
                return ReturnConfig.isSuccess("Success");
            }
            
            if(!sessionStatus.isCompleted() && sessionStatus.getCallbackCount() > 0){
                out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack unfinished session  ID: "+sessionId);
                List<SessionData> sessionDataList = sessionDataFacade.getSessionData(sessionId);
                if(sessionDataList != null){
                    String sData = "";
                    for(SessionData sessionData : sessionDataList){
                        sData+=sessionData.getSessionkey()+":"+sessionData.getSessionInput();
                    }
                    sData = sData.isEmpty() ? " " : sData;
                    Ticket ticket = new Ticket();
                    ticket.setMsisdn(sessionStatus.getInitTel());
                    Thread smsThread = new Thread(new BackgroundSMS(smsSender, null, ticket, "Ufite Itike itarangiye: "+sData, true));
                    smsThread.start();
                }
            }
            sessionStatus.setCallbackCount(sessionStatus.getCallbackCount()+1);
            sessionStatusFacade.edit(sessionStatus);
            sessionStatusFacade.refreshSessionStatus();
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack succeeded to generate additional tictet JobId: "+jobId+" with a status: "+status);
            return ReturnConfig.isSuccess("Success");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CallBackHandler progressCallBack error occured due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Schedule initiator failed.");
        }
    }
    
    public Response tempTicket(HttpHeaders headers){
        try {
            if(headers == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket received empty header.");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            String jobId = headers.getHeaderString(HeaderConfig.JOB_ID);
            String status = headers.getHeaderString(HeaderConfig.JOB_STATUS);
            
            if(jobId == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket received JobId: null with a status: "+status);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            
            out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket received JobId: "+jobId+" with a status: "+status);
            String sessionId = DataFactory.splitString(jobId, "^")[1];
            out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket SessionId: "+sessionId);
            TempTicket tempTicket = tempTicketFacade.getLastTempBySession(sessionId);
            if(tempTicket == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket did not find TempTicket with SessionId: "+sessionId);
                return ReturnConfig.isSuccess("Success");
            }
            if(tempTicket.getTicketStatus().equals(SessionDataStatus.CANCELLED_STATUS)){
                tempTicketFacade.remove(tempTicket);
                tempTicketFacade.refreshTemp();
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket found a cancelled TempTicket with SessionId: "+sessionId);
                return ReturnConfig.isSuccess("Success");
            }
            
            if(tempTicket.getCount() == 0){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket SessionId: "+sessionId+" and count of: "+tempTicket.getCount());
                tempTicket.setCount(1);
                tempTicketFacade.edit(tempTicket);
                tempTicketFacade.refreshTemp();
                return ReturnConfig.isSuccess("Success");
            }
            
            UserBean userBean = apInterface.getUserByTel(tempTicket.getMsisdn());
            if(userBean == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket did not find User with TEL: "+tempTicket.getMsisdn());
                return ReturnConfig.isSuccess("Success");
            }
            SessionStatus sessionStatus = sessionStatusFacade.getLastSession(sessionId);
            if(sessionStatus == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket did not find Session with ID: "+sessionId);
                return ReturnConfig.isSuccess("Success");
            }
            SessionTicketData sessionTicketData = new SessionTicketData();
            sessionTicketData.setSessionId(sessionId);
            List<SessionData> sessionDatas = sessionDataFacade.getSessionData(sessionId);
            if(sessionDatas != null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket tempTicket session data size: "+sessionDatas.size());
                for(SessionData sData : sessionDatas){
                    switch (sData.getSessionkey()) {
                        case StepsConfig.ENTER_CAR_BRAND:
                            sessionTicketData.setCarBrand(sData.getSessionInput());
                            break;
                        case StepsConfig.ENTER_TICKET_TYPE:
                            sessionTicketData.setTicketType(sData.getSessionInput());
                            break;
                        case StepsConfig.ENTER_NUMBER_PLATE:
                            sessionTicketData.setnPlate(sData.getSessionInput());
                            break;
                        default:
                            break;
                    }
                }
            }
            String result = ticketManager.genTicket(sessionStatus, userBean, sessionTicketData);
            tempTicketFacade.remove(tempTicket);
            tempTicketFacade.refreshTemp();
            out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket succeeded to new ticket from temp ticket SessionId: "+sessionId+" ticketManager result: "+result);
            return ReturnConfig.isSuccess("Success");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CallBackHandler tempTicket error occured due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
        }
    }
    
    public Response tNotification(HttpHeaders headers){
        try {
            if(headers == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification received empty header.");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            String jobId = headers.getHeaderString(HeaderConfig.JOB_ID);
            String status = headers.getHeaderString(HeaderConfig.JOB_STATUS);
            
            if(jobId == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification received JobId: null with a status: "+status);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty headers");
            }
            
            out.print(AppDesc.APP_DESC+"CallBackHandler tNotification received JobId: "+jobId+" with a status: "+status);
            String sessionId = DataFactory.splitString(jobId, "^")[1];
            CallBackCounter callBackCounter = callBackCounterFacade.getLastSession(jobId);
            if(callBackCounter == null){
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification received callbackCounter: "+jobId+" is being created due to empty result");
                callBackCounter = new CallBackCounter(jobId, 1, new Date());
                callBackCounterFacade.create(callBackCounter);
                callBackCounterFacade.refreshCounter();
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification created callback counter instance: "+jobId);
                return ReturnConfig.isSuccess("Success");
            }
            if(callBackCounter.getCallbackCount() == 1){
                Ticket ticket = ticketFacade.getSessionLastTicket(sessionId);
                if(ticket == null){
                    out.print(AppDesc.APP_DESC+"CallBackHandler tNotification did not find Ticket with sessionId: "+sessionId);
                    return ReturnConfig.isSuccess("Success");
                }
                
                if(!ticket.getTicketStatus().equals(SessionDataStatus.COMPLETED_STATUS)){
                    Thread smsThread2 = new Thread(new BackgroundSMS(smsSender, customerProvider, ticket, ticket.getConductorName()+" harabura iminota  "+5+" kugirango Imodoka "+ticket.getNumberPlate()+ " yandikirwe itike.", true));//(60-DataFactory.printDifference(ticket.getInDate(), new Date()))
                    smsThread2.start();
                }
                
                callBackCounter.setCallbackCount(callBackCounter.getCallbackCount()+1);
                callBackCounterFacade.edit(callBackCounter);
                callBackCounterFacade.refreshCounter();
                
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification succeeded to acknowledge conductor with Tel:"+DataFactory.splitString(jobId, "^")[0]+" about ticket: "+ticket.getTicketId()+" sessionId: "+sessionId+"Time remaining to complete an hour: "+(60-DataFactory.printDifference(ticket.getInDate(), new Date())));
                return ReturnConfig.isSuccess("Success");
            }else{
                out.print(AppDesc.APP_DESC+"CallBackHandler tNotification a call back : "+jobId+" came at: "+callBackCounter.getCallbackCount()+"th and its has no effect");
                callBackCounter.setCallbackCount(callBackCounter.getCallbackCount()+1);
                callBackCounterFacade.edit(callBackCounter);
                callBackCounterFacade.refreshCounter();
            }
            
            out.print(AppDesc.APP_DESC+"CallBackHandler tNotification succeeded ac ounter did match the counting indexes for calback counter instance: "+jobId);
            return ReturnConfig.isSuccess("Success");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"CallBackHandler tNotification error occured due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error with this job initiator.");
        }
    }
}

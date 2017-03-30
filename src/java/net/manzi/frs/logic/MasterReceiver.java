/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.logic.UssdProcessor;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdRequest;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import net.manzi.frs.config.MenuMessage;
import net.manzi.frs.config.SessionDataStatus;
import net.manzi.frs.entities.SessionStatus;
import net.manzi.frs.facades.SessionDataFacade;
import net.manzi.frs.facades.SessionStatusFacade;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class MasterReceiver {
    @EJB
    UssdProcessor ussdProcessor;
    @EJB
    SessionDataFacade sessionDataFacade;
    @EJB
    SessionStatusFacade sessionStatusFacade;
    public Response receive(UssdRequest request){
        try{
            //Validation of new incoming MSISDN from Manzi URL and continue with sessions
            SessionStatus sessionStatus = sessionStatusFacade.getClientLastSession(request.getMsisdn());
            if(request.getNewRequest() == 1){                
                if(sessionStatus != null){
                    if(!sessionStatus.isCompleted()){
                        out.print(AppDesc.APP_DESC+"MasterReceiver receive uncomplete session: "+sessionStatus.getId());
                        sessionStatus.setSessionStatus(SessionDataStatus.RESUME_STATUS);
                        sessionStatusFacade.edit(sessionStatus);
                        sessionStatusFacade.refreshSessionStatus();
                        return ReturnConfig.isSuccess(ussdProcessor.continueInput(MenuMessage.RESUME_MENU, request));
                    }
                }
                out.print(AppDesc.APP_DESC+"MasterReceiver receive new session coming from: "+request.getMsisdn());
                return ReturnConfig.isSuccess(ussdProcessor.continueInput(MenuMessage.CHOICE_MENU, request));
            }
            
            if(sessionStatus != null){
                if(sessionStatus.getSessionStatus().equals(SessionDataStatus.RESUME_STATUS) && request.getInput().equals("1")){
                    //resume from where we left
                    sessionStatus.setSessionStatus(SessionDataStatus.ONGOING_STATUS);
                    sessionStatusFacade.edit(sessionStatus);
                    sessionStatusFacade.refreshSessionStatus();
                }else if(sessionStatus.getSessionStatus().equals(SessionDataStatus.RESUME_STATUS) && request.getInput().equals("2")){
                    //cancel last session
                    sessionStatus.setSessionStatus(SessionDataStatus.CANCELLED_STATUS);
                    sessionStatus.setCompleted(true);
                    sessionStatusFacade.edit(sessionStatus);
                    sessionStatusFacade.refreshSessionStatus();
                }else{
                    //when resuming and user put invalid entry
                    return ReturnConfig.isSuccess(ussdProcessor.continueInput(MenuMessage.RETRY+MenuMessage.RESUME_MENU, request));
                }
            }
            
            //dealing with sessions. new session, ongoing session, completing session
            
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"MasterReceiver receive failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(ussdProcessor.faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
        return null;
    }
}

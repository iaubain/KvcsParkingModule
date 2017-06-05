/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.apiclient.OpenExternal;
import com.kparking.oltranz.config.ApiConfig;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Car;
import com.kparking.oltranz.entities.ParkingInfo;
import com.kparking.oltranz.entities.Progressive;
import com.kparking.oltranz.entities.TempTicket;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.entities.Verification;
import com.kparking.oltranz.facades.CallBackFacade;
import com.kparking.oltranz.facades.CarFacade;
import com.kparking.oltranz.facades.ParkingInfoFacade;
import com.kparking.oltranz.facades.ProgressiveFacade;
import com.kparking.oltranz.facades.TempTicketFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.facades.VerificationFacade;
import com.kparking.oltranz.simplebeans.commonbeans.StatusBean;
import com.kparking.oltranz.simplebeans.conductors.ResponseConductor;
import com.kparking.oltranz.simplebeans.schedule.JobTasks;
import com.kparking.oltranz.simplebeans.schedule.MyFrequency;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import com.kparking.oltranz.simplebeans.ussdbeans.Menu;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdRequest;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdResponse;
import com.kparking.oltranz.simplebeans.validation.RequestValidation;
import com.kparking.oltranz.simplebeans.validation.ResponseValidation;
import com.kparking.oltranz.simplebeans.verification.ResponseUserDetails;
import com.kparking.oltranz.simplebeans.verification.VerificationData;
import com.kparking.oltranz.simplebeans.verification.VerifyRequest;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.IdGenerator;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import net.manzi.frs.config.MenuMessage;
import net.manzi.frs.config.SessionDataStatus;
import net.manzi.frs.config.SessionType;
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
public class UssdProcessor {
    @EJB
            ApInterface apInterface;
    @EJB
            IdGenerator idGenerator;
    @EJB
            TicketFacade ticketFacade;
    @EJB
            TicketManager ticketManager;
    @EJB
            ScheduleManager scheduleManager;
    @EJB
            SmsSender smsSender;
    @EJB
            CustomerProvider customerProvider;
    @EJB
            CallBackFacade callBackFacade;
    @EJB
            CarFacade carFacade;
    @EJB
            ProgressiveFacade progressiveFacade;
    @EJB
            OpenExternal openExternal;
    @EJB
            SessionStatusFacade sessionStatusFacade;
    @EJB
            SessionDataFacade sessionDataFacade;
    @EJB
            TempTicketFacade tempTicketFacade;
    @EJB
            ParkingInfoFacade parkingInfoFacade;
    @EJB
            VerificationFacade verificationFacade;
    
    private HashMap<String, VerificationData> mVerifierData = new HashMap<>();
    private LoadingCache<String, VerificationData> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, VerificationData>() {
                @Override
                public VerificationData load(String key) throws Exception {
                    return loadObject(key);
                }
                
            });
    
    
    private VerificationData loadObject(String key) {
        return new VerificationData();        
    }
    
    public Response receiveCarInRequest(UssdRequest request){
        try{
            //check user validity by MSISDN
            UserBean userBean = getUser(request.getMsisdn());
            if(!validateEntry(userBean)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest no conductor found for: "+request.getMsisdn());
                SessionStatus sessionStatus = sessionStatusFacade.getClientLastSession(request.getMsisdn());
                if(sessionStatus != null){
                    sessionStatus.setCompleted(true);
                    sessionStatus.setLastAccessDate(new Date());
                    sessionStatus.setLastAction("USER NOT ALLOWED");
                    sessionStatus.setSessionStatus(SessionDataStatus.CANCELLED_STATUS);
                    sessionStatusFacade.edit(sessionStatus);
                    sessionStatusFacade.refreshSessionStatus();
                }
                return ReturnConfig.isSuccess(faillureGen(request, "Ntago mwemerewe gukoresha iyi service. Kubindi bisobanuro mwahamagara 799.^Mwongere mukanya."));
            }
            
            String conductorNames = userBean.getfName() != null ? userBean.getfName() : "" +userBean.getOtherNames() != null ? userBean.getOtherNames() : "";
            String conductorLoc = userBean.getLocationName()+"-"+userBean.getAgentZoneName();
            
            String msisdn = request.getMsisdn();
            
            if(userBean.getRoleId() != 4){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ntago mwemerewe gukoresha iyi service. Kubindi bisobanuro mwahamagara 799.^Mwongere mukanya."));
            }
            
            SessionStatus sessionStatus = sessionStatusFacade.getClientLastSession(msisdn);
            if(request.getNewRequest() == 1){
                if(sessionStatus == null){
                    //create new session
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest generate new session for: "+msisdn+" Names: "+conductorNames);
                    sessionStatus = createSession(request, SessionType.CAR_IN, StepsConfig.ENTER_NUMBER_PLATE);
                }else{
                    if(!sessionStatus.isCompleted() && sessionStatus.getSessionStatus().equals(SessionDataStatus.ONGOING_STATUS)){
                        //resume the session
                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest resuming session: "+sessionStatus.getSessionId()+" for: "+msisdn+" Names: "+conductorNames);
                        sessionStatus.setSessionStatus(SessionDataStatus.RESUME_STATUS);
                        sessionStatus.setLastAccessDate(new Date());
                        sessionStatus.setLastAction("RECOVER FROM UNCOMPLETED SESSION");
                        updateSession(sessionStatus);
                        conductorNames += "^"+conductorLoc;
                        return ReturnConfig.isSuccess(continueInput(conductorNames+" ("+userBean.getAgentZoneId() +")"+MenuMessage.RESUME_MENU, request));
                    }else{
                        //Last session completed and create new session
                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest last session: "+sessionStatus.getSessionId()+" completed generating nee session for: "+msisdn+" Names: "+conductorNames);
                        sessionStatus = createSession(request, SessionType.CAR_IN, StepsConfig.ENTER_NUMBER_PLATE);
                    }
                }
            }
            if(sessionStatus.getSessionType().equals(SessionType.CAR_IN)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest processing"+msisdn+" Names: "+conductorNames+" Session type: "+SessionType.CAR_IN);
                switch(sessionStatus.getSessionStatus()){
                    case SessionDataStatus.RESUME_STATUS:
                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest resuming "+msisdn+" Names: "+conductorNames+" Session type: "+SessionType.CAR_IN);
                        SessionStatus resumeSession = resumeProcessor(request, sessionStatus);
                        if(resumeSession == null){
                            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest resuming "+msisdn+" Names: "+conductorNames+" Session type: "+SessionType.CAR_IN+" Invalid resume entry.");
                            return ReturnConfig.isSuccess(continueInput(conductorNames+"^Mwashyizemo ibitaribyo"+MenuMessage.RESUME_MENU, request));
                        }
                        return processEntry(resumeSession, userBean, request, conductorNames, true);
                    case SessionDataStatus.ONGOING_STATUS:
                        return processEntry(sessionStatus, userBean, request, conductorNames, false);
                    default:
                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest resuming "+msisdn+" Names: "+conductorNames+" Session type: Missing session Type");
                        return ReturnConfig.isSuccess(faillureGen(request,"KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
                }
            }else{
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest action failed due to: Bad redirection from USSD agregator.");
                return ReturnConfig.isSuccess(faillureGen(request,"KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
            }
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request,"KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    public Response receiveCarOut(UssdRequest request){
        try{
            //check user validity by MSISDN
            DateFormat dFomat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date date = new Date();
            UserBean userBean = getUser(request.getMsisdn());
            if(!validateEntry(userBean)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ntago mwemerewe gkoresha iyi service. Kubindi bisobanuro mwahamagara 799.^Mwongere mukanya."));
            }
            
            String conductorNames = userBean.getfName() != null ? userBean.getfName() : userBean.getOtherNames() != null ? userBean.getOtherNames() : "No_Name";
            //String msisdn = request.getMsisdn();
            
            if(userBean.getRoleId() != 4){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarInRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ntago mwemerewe gukoresha iyi service. Kubindi bisobanuro mwahamagara 799.^Mwongere mukanya."));
            }
            
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut a new request received from"+request.getMsisdn());
                return ReturnConfig.isSuccess(carInOutMenu("Kuvana imodoka muri parikingi^",request));
            }
            
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            if(request.getInput().isEmpty()){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Reba niba purake yanditse neza."));
            }
            
            Ticket ticket = ticketFacade.getCustormerLastTicket(request.getInput());
            TempTicket tempTicket = tempTicketFacade.getLastCustomerTempTicket(request.getInput());
            if(tempTicket == null){
                if(ticket == null){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with no ticket: by conductor: "+request.getMsisdn());
                    return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Ntago ibonetse."));
                }
            }
            
            /*
            (Un)Coment this if the one that put the car in prking is (not) the one that must remove it.
            */
            
            String carOutDate = null;
            ParkingInfo parkingInfo = parkingInfoFacade.getCustomerLastParkiInfo(request.getInput());
            if(parkingInfo != null && parkingInfo.getOutDate() != null){
                carOutDate = dFomat.format(parkingInfo.getOutDate());
            }
            
            if(carOutDate == null){
                if(ticket.getOutDate().getTime() < date.getTime())
                    carOutDate = dFomat.format(ticket.getOutDate());
                else{
                    long min = DataFactory.diffMinutes(date, ticket.getOutDate());
                    min = min < 0 ? min*-1 : min;
                    carOutDate = "Mu minota "+ min +" Itambutse";
                }
            }
            
            
            if(tempTicket != null &&
                    tempTicket.getTicketStatus().equals(SessionDataStatus.ONGOING_STATUS) &&
                    tempTicket.getExpire().getTime() > new Date().getTime()){
                if(!tempTicket.getMsisdn().equals(request.getMsisdn())){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with temp ticket: "+tempTicket.getId()+" request by conductor: "+request.getMsisdn()+" Initiated by: "+tempTicket.getConductorId());
                    return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Iri muri parking: "+tempTicket.getParkingDesc()+"^Yahageze: "+dFomat.format(tempTicket.getCreated())));
                }
                tempTicket.setTicketStatus(SessionDataStatus.CANCELLED_STATUS);
                tempTicketFacade.edit(tempTicket);
                tempTicketFacade.refreshTemp();
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut Succeeded to take out car"+request.getInput());
                return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Gukura imodoka. "+request.getInput()+" muri parikingi.^Birakozwe."));
            }
            
            if(!ticket.getMsisdn().equals(request.getMsisdn())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with ticket: "+ticket.getTicketId()+" request by conductor: "+request.getMsisdn()+" Initiated by: "+ticket.getConductorId());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Yashyizwe muri parking na: "+ticket.getConductorName()+"^Saa: "+dFomat.format(ticket.getInDate())+"^Kuri: "+ticket.getParkingDesc()));
            }
            
            
            if(ticket.getTicketStatus().equals(SessionDataStatus.COMPLETED_STATUS)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with ticket: "+ticket.getTicketId()+" by conductor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Yamaze gukurwa muri parikingi.^Isaha yakuwemo: "+carOutDate));
            }
            
            if(!ticketManager.setTicketOutDate(true, request.getInput(), request.getMsisdn())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: numberPlate "+request.getInput()+" not found");
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
            }
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut Succeeded to take out car"+request.getInput());
            return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Gukura imodoka. "+request.getInput()+" muri parikingi.^Birakozwe."));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    public Response checkCar(UssdRequest request){
        try{
            if(request.getNewRequest() == 1){
                ResponseUserDetails responseUserDetails = apInterface.getUser(request.getMsisdn());
                if(responseUserDetails == null){
                    return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka"));
                }else if(!responseUserDetails.getStatus().getCode().equals("100")){
                    return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka"));
                }
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a new request received from"+request.getMsisdn());
                VerificationData verificationData = cache.get(request.getSessionId());
                VerifyRequest verifyRequest = new VerifyRequest();
                verifyRequest.setVerifierMsisdn(request.getMsisdn());
                
                verificationData.setStepCount(1);
                verificationData.setVerifyRequest(verifyRequest);
                cache.put(request.getSessionId(), verificationData);
                
//                if(!isSessionAvailable(request.getSessionId())){
//                    VerifyRequest verifyRequest = new VerifyRequest();
//                    verifyRequest.setVerifierMsisdn(request.getMsisdn());
//                    mVerifierData.put(request.getSessionId(), new VerificationData(1, new VerifyRequest()));
//                }else{
//                    VerificationData verificationData = getVerificationData(request.getSessionId());
//                    removeVerification(request.getSessionId(), verificationData);
//                    mVerifierData.put(request.getSessionId(), new VerificationData(1, new VerifyRequest()));
//                }                
                return ReturnConfig.isSuccess(carInOutMenu("Kureba Imodoka.",request));
            }
            
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            
            VerificationData verificationData = getVerificationData(request.getSessionId());
            switch (verificationData.getStepCount()) {
                case 1:
                {
                    VerifyRequest verifyRequest = verificationData.getVerifyRequest();
                    verifyRequest.setNumberPlate(request.getInput());
                    
                    verificationData.setStepCount(2);
                    verificationData.setVerifyRequest(verifyRequest);
                    cache.put(request.getSessionId(), verificationData);
                    //mVerifierData.replace(request.getSessionId(), verificationData);
                    
                    return ReturnConfig.isSuccess(continueInput("Shyiramo parikingi", request));
                }
            //ResponseValidation responseValidation = apInterface.validateNumberPlate(new RequestValidation(request.getInput(), "USSD-PARKIN"));
            //if(responseValidation == null){
            //  out.print(AppDesc.APP_DESC+"Failed to validate number plate: "+request.getInput());
            //return ReturnConfig.isSuccess(carInOutMenu("^Plaque "+request.getInput()+"^Ntibikunze kumenya niba yemewe^Mwongere mukanya",request));
            //}
            //if(!responseValidation.getStatusCode().equals("100")){
            //  out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
            //return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza."));
            //}
//            if(request.getInput().isEmpty()){
//                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
//                return ReturnConfig.isSuccess(faillureGen(request, "Purake reba niba yanditse neza."));
//            }
//            Ticket ticket = ticketFacade.getCustormerLastTicket(request.getInput());
//            if(ticket == null){
//                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" with no ticket: by conductor: "+request.getMsisdn());
//                return ReturnConfig.isSuccess(faillureGen(request, request.getInput()+"^Ntirikugaragara muri parikingi"));
//            }
//            
//            String message;
//            String lastTime = ticket.getInDate() != null ? new SimpleDateFormat("").format(ticket.getInDate()):"^igihe : ntikibonetse";
//            if(ticket.getTicketStatus().equals(SessionDataStatus.ONGOING_STATUS)){
//                message = "Imodoka:"+ticket.getNumberPlate()+"^Iparitse:"+ticket.getParkingDesc()+"^Yagiyemo"+lastTime+"^Yashyizwemo:"+ticket.getConductorName();
//                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime != null?lastTime:""+" Requestor: "+request.getMsisdn()+" message: "+message);
//            }else{
//                TempTicket tempTicket = tempTicketFacade.getLastCustomerTempTicket(request.getInput());
//                if(tempTicket != null){
//                    if(tempTicket.getTicketStatus().equals(SessionDataStatus.ONGOING_STATUS)){
//                        lastTime = tempTicket.getCreated()!= null ? new SimpleDateFormat("").format(tempTicket.getCreated()):"^igihe : ntikibonetse";
//                        message = "Imodoka:"+tempTicket.getNumberPlate()+"^Iparitse:"+tempTicket.getParkingDesc()+"^Yagiyemo"+lastTime+"^Yashyizwemo:"+tempTicket.getConductorName();
//                        out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime != null?lastTime:""+" Requestor: "+request.getMsisdn()+" message: "+message);
//                    }else{
//                        message = "Imodoka:"+ticket.getNumberPlate()+"^Yavuye muri parikingi:"+ticket.getParkingDesc();//+"^Yagiyemo:"+lastTime+"^Yashyizwemo:"+ticket.getConductorName();
//                        out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime+" Requestor: "+request.getMsisdn()+" message: "+message);
//                    }
//                }else{
//                    message = "Imodoka:"+ticket.getNumberPlate()+"^Yavuye muri parikingi:"+ticket.getParkingDesc();//+"^Yagiyemo:"+lastTime+"^Yashyizwemo:"+ticket.getConductorName();
//                    out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime+" Requestor: "+request.getMsisdn()+" message: "+message);
//                }
//                
//            }
//            
//            out.print(AppDesc.APP_DESC+"UssdProcessor checkCar Succeeded to check car: "+request.getInput()+" by requestor: "+request.getMsisdn());
//            return ReturnConfig.isSuccess(successGen(request, message));
                case 2:
                {
                    VerifyRequest verifyRequest = verificationData.getVerifyRequest();
                    verifyRequest.setParkingId(request.getInput());
                    
                    StatusBean statusBean = apInterface.verifyCar(DataFactory.objectToString(verifyRequest));
                    //mVerifierData.remove(request.getSessionId());
                    
                    if(statusBean == null){
                        return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka"));
                    }else if(statusBean.getStatusCode() == 100){
                        return ReturnConfig.isSuccess(faillureGen(request, "Imodoka: "+verifyRequest.getNumberPlate()+"^iri muri parkingi: "+verifyRequest.getParkingId()));
                    }else{
                        return ReturnConfig.isSuccess(faillureGen(request, "Imodoka: "+verifyRequest.getNumberPlate()+"^ntago ibonetse muri parkingi: "+verifyRequest.getParkingId()));
                    }
                }
                default:
                    return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka"));
            }
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    public Response verifyCar(String body){
        try {
            VerifyRequest verifyRequest = (VerifyRequest) DataFactory.stringToObject(VerifyRequest.class, body);
            if(verifyRequest == null){
                out.print(AppDesc.APP_DESC+"UssdProcessor verifyCar action failed due to: Empty request");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error: Invalid or empty request");
            }
            DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            boolean isCarIn = false;
            String conductorId = "noId", conductorNames = "noNames", conductorTel = null, conductorParking = verifyRequest.getParkingId();
            Ticket ticket = ticketFacade.getCustormerLastTicket(verifyRequest.getNumberPlate());
            TempTicket tempTicket = tempTicketFacade.getLastCustomerTempTicket(verifyRequest.getNumberPlate());
            if(tempTicket != null &&
                    new Date().getTime() < tempTicket.getExpire().getTime() &&
                    tempTicket.getTicketStatus().equals(SessionDataStatus.ONGOING_STATUS) &&
                    tempTicket.getParkingId().equals(verifyRequest.getParkingId())){
                conductorNames = tempTicket.getConductorName();
                conductorId = tempTicket.getConductorId();
                conductorTel = tempTicket.getMsisdn();
                conductorParking = tempTicket.getParkingDesc();
                isCarIn = true;
                
                verificationHelper(new Verification(verifyRequest.getVerifierMsisdn(),
                        verifyRequest.getParkingId(),
                        verifyRequest.getNumberPlate(),
                        conductorId,
                        conductorNames,
                        dFormat.parse(dFormat.format(new Date())),
                        isCarIn),
                        conductorTel);
            }else if(ticket != null &&
                    DataFactory.printDifference(ticket.getInDate(), new Date()) <= 60 &&
                    ticket.getParkingId().equals(verifyRequest.getParkingId())){
                conductorNames = ticket.getConductorName();
                conductorId = ticket.getConductorId();
                conductorTel = ticket.getMsisdn();
                conductorParking = ticket.getParkingDesc();
                isCarIn = true;
                
                verificationHelper(new Verification(verifyRequest.getVerifierMsisdn(),
                        verifyRequest.getParkingId(),
                        verifyRequest.getNumberPlate(),
                        conductorId,
                        conductorNames,
                        dFormat.parse(dFormat.format(new Date())),
                        isCarIn),
                        conductorTel);
            }else{
                //get conductor per parking ID
                String conductors = apInterface.getParkingConductor(verifyRequest.getParkingId());
                if(conductors == null){
                    out.print(AppDesc.APP_DESC+" Verify car got empty result from get conductors in the parking");
                    return ReturnConfig.isSuccess(DataFactory.objectToString(new StatusBean(101, "car not found")));
                }
                List<Object> mConveted = DataFactory.stringToObjectList(UserBean.class, conductors);
                for(Object object : mConveted){
                    UserBean mUser = (UserBean) object;
                    verificationHelper(new Verification(verifyRequest.getVerifierMsisdn(),
                            verifyRequest.getParkingId(),
                            verifyRequest.getNumberPlate(),
                            mUser.getUserId()+"",
                            mUser.getfName()+" "+mUser.getOtherNames(),
                            dFormat.parse(dFormat.format(new Date())),
                            isCarIn),
                            mUser.getPhoneNumber());
                }
                
            }
           
            return ReturnConfig.isSuccess(DataFactory.objectToString(new StatusBean(isCarIn ? 100 : 101, isCarIn ? "Car Found" : "car not found")));
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"UssdProcessor verifyCar action failed due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, ""+e.getMessage());
        }
    }
    
    private boolean isSessionAvailable(String sessionId){
        return mVerifierData.isEmpty() ? false : mVerifierData.containsKey(sessionId);
    }
    
    private VerificationData getVerificationData(String sessionId) throws ExecutionException{
            return cache.get(sessionId);
            //return mVerifierData.isEmpty() ? null : mVerifierData.get(sessionId);
    }
    
    private boolean removeVerification(String sessionId, VerificationData verificationData){
        return mVerifierData.isEmpty() ? true : mVerifierData.remove(sessionId, verificationData);
    }
    
    private void verificationHelper(Verification verification, String contact){
        DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        verificationFacade.create(verification);
        verificationFacade.refreshVerify();
        
        if(contact != null){
            Ticket ticket1 = new Ticket();
            ticket1.setMsisdn(contact);
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, ticket1, "Umaze gukorerwa igenzurwa muri parkingi: "+verification.getParkingId()+" kuwa "+dFormat.format(new Date()), true));
            smsThread.start();
        }
    }
    
    public Response signupCar(UssdRequest request){
        try{
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar a new request received from"+request.getMsisdn());
                
                return ReturnConfig.isSuccess(carInOutMenu("Andikisha imodoka. Shyiramo purake^Signup Car. Fill the number plate.",request));
            }
            
            Car car;
            //check user validity by MSISDN
            
            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar no conductor found for: "+request.getMsisdn());
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            
            ResponseValidation responseValidation = apInterface.validateNumberPlate(new RequestValidation(request.getInput(), "USSD-PARKIN"));
            if(responseValidation == null){
                out.print(AppDesc.APP_DESC+"Failed to validate number plate: "+request.getInput());
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
            }if(!responseValidation.getStatusCode().equals("100")){
                out.print(AppDesc.APP_DESC+"Condition applied to number plate: "+request.getInput()+" Status: "+responseValidation.getStatusCode()+" Message: "+responseValidation.getMessage());
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
            }
            
//                if(! DataFactory.numberPlateValidator(request.getInput())){
//                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
//                    return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
//                }
car = new Car(request.getInput().toUpperCase(), request.getMsisdn(), "Owner", new Date());
carFacade.create(car);
carFacade.refreshCar();
return ReturnConfig.isSuccess(successGen(request, "Birakozwe, Well Done, Bien fait!"));

//            ConductorBean conductorBean = responseConductor.getConductor();
//            String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
//
//            String[] data = DataFactory.splitString(request.getInput(), " ");
//            String numberPlate = data[0].toUpperCase();
//            String tel = data[1];
//            if(! DataFactory.numberPlateValidator(numberPlate)){
//                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
//                return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
//            }
//            String tempTel = tel;
//            tel = DataFactory.phoneFormat(tel);
//            if(tel.isEmpty()){
//                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar action failed due to: Empty tel requestor: "+request.getMsisdn());
//                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Reba niba"+tempTel != null? tempTel:""+" numero yumukiriya wayanditse neza."));
//
//            }
//            car = new Car(numberPlate,
//                    tel,
//                    "Conductor:ID"+responseConductor.getConductor().getConductorId()+" Names:"+conductorNames,
//                    new Date());
//            carFacade.create(car);
//            carFacade.refreshCar();
//
//            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar Succeeded to signup car: "+request.getInput()+" by requestor: "+request.getMsisdn());
//            return ReturnConfig.isSuccess(successGen(request, conductorNames+"^Kwandika imodoka birakozwe"));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    private SessionStatus resumeProcessor(UssdRequest request, SessionStatus sessionStatus){
        out.print(AppDesc.APP_DESC+"UssdProcessor resumeProcessor resuming "+request.getMsisdn()+" SessionType: "+sessionStatus.getSessionType()+" SessionId: "+sessionStatus.getSessionId());
        switch(request.getInput()){
            case "1":
                out.print(AppDesc.APP_DESC+"UssdProcessor resumeProcessor resume Input: 1 for: "+request.getMsisdn());
                sessionStatus.setSessionStatus(SessionDataStatus.ONGOING_STATUS);
                sessionStatus.setCompleted(false);
                sessionStatus.setLastAccessDate(new Date());
                sessionStatus.setLastAction("RECOVER SESSION TO ON GOING");
                sessionStatusFacade.edit(sessionStatus);
                sessionStatusFacade.refreshSessionStatus();
                return sessionStatus;
            case "2":
                out.print(AppDesc.APP_DESC+"UssdProcessor resumeProcessor resume Input: 2 for: "+request.getMsisdn());
                sessionStatus.setSessionStatus(SessionDataStatus.CANCELLED_STATUS);
                sessionStatus.setCompleted(true);
                sessionStatus.setLastAccessDate(new Date());
                sessionStatus.setLastAction("CANCELLING SESSION");
                sessionStatusFacade.edit(sessionStatus);
                sessionStatusFacade.refreshSessionStatus();
                
                return createSession(request, SessionType.CAR_IN, StepsConfig.ENTER_NUMBER_PLATE);
            default:
                out.print(AppDesc.APP_DESC+"UssdProcessor resumeProcessor failed due to resume Input: "+request.getInput()+" for: "+request.getMsisdn());
                return null;
        }
    }
    
    private Response processEntry(SessionStatus sessionStatus, UserBean userBean, UssdRequest request, String conductorNames, boolean isResume){
        Date date = new Date();
        String conductorLoc = userBean.getLocationName()+"-"+userBean.getAgentZoneName();
        if(isResume){
            switch(sessionStatus.getStepsCount()){
                case 1:
                    conductorNames += "^"+conductorLoc;
                    return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+" ("+userBean.getAgentZoneId() +") ",request));
                case 2:
                    List<SessionData> sDatas = sessionDataFacade.getSessionData(sessionStatus.getSessionId());
                    if(sDatas == null){
                        sessionStatus.setCompleted(true);
                        sessionStatus.setLastAccessDate(date);
                        sessionStatus.setLastAction("CANCELLING SESSION DUE TO MISSING SESSION DATA");
                        sessionStatus.setSessionType(SessionDataStatus.CANCELLED_STATUS);
                        updateSession(sessionStatus);
                        conductorNames += "^"+conductorLoc;
                        return ReturnConfig.isSuccess(faillureGen(request, conductorNames+" ("+userBean.getAgentZoneId() +")^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
                    }
                    String numberPlate = null;
                    for(SessionData sData : sDatas){
                        if(sData.getSessionkey().equals(StepsConfig.ENTER_NUMBER_PLATE)){
                            numberPlate = sData.getSessionInput();
                            break;
                        }
                    }
                    numberPlate = numberPlate == null ? "purake" : numberPlate;
                    return ReturnConfig.isSuccess(continueInput(conductorNames+"^Hitamo ubwoko bwatike.^Imodoka: "+numberPlate+"^1. 100Rwf^2. 200Rwf^3. 300Rwf^4. 400Rwf^5. 1000Rwf",request));
                case 3:
                    sDatas = sessionDataFacade.getSessionData(sessionStatus.getSessionId());
                    if(sDatas == null){
                        sessionStatus.setCompleted(true);
                        sessionStatus.setLastAccessDate(date);
                        sessionStatus.setLastAction("CANCELLING SESSION DUE TO MISSING SESSION DATA");
                        sessionStatus.setSessionStatus(SessionDataStatus.CANCELLED_STATUS);
                        updateSession(sessionStatus);
                        return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
                    }
                    numberPlate = null;
                    for(SessionData sData : sDatas){
                        if(sData.getSessionkey().equals(StepsConfig.ENTER_NUMBER_PLATE)){
                            numberPlate = sData.getSessionInput();
                            break;
                        }
                    }
                    numberPlate = numberPlate == null ? "purake" : numberPlate;
                    return ReturnConfig.isSuccess(continueInput(conductorNames+"^Andika ubwoko bw'imodoka^Ifite: "+ numberPlate ,request));
            }
        }
        
        out.print(AppDesc.APP_DESC+"UssdProcessor processEntry process "+request.getMsisdn()+" SessionType: "+sessionStatus.getSessionType()+" SessionId: "+sessionStatus.getSessionId());
        if(sessionStatus.getSessionType().equals(SessionType.CAR_IN)){
            if(sessionStatus.getCurrentStep().equals(StepsConfig.WLCM) && sessionStatus.getStepsCount() == 0 && sessionStatus.getNextStep().equals(StepsConfig.ENTER_NUMBER_PLATE)){
                out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
                sessionStatus.setStepsCount(1);
                sessionStatus.setLastAccessDate(date);
                sessionStatus.setLastAction("REQUESTING NUMBER PLATE CAPTURE");
                updateSession(sessionStatus);
                conductorNames += "^"+conductorLoc;
                return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+" ("+userBean.getAgentZoneId() +") ",request));
            }else if(sessionStatus.getCurrentStep().equals(StepsConfig.WLCM) && sessionStatus.getStepsCount() == 1 && sessionStatus.getNextStep().equals(StepsConfig.ENTER_NUMBER_PLATE)){
                out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
                
                request.setInput(request.getInput().replace(" ", "").toUpperCase());
                
                ResponseValidation responseValidation = apInterface.validateNumberPlate(new RequestValidation(request.getInput(), "USSD-PARKIN"));
                if(responseValidation == null){
                    out.print(AppDesc.APP_DESC+"Failed to validate number plate: "+request.getInput());
                    conductorNames += "^"+conductorLoc;
                    return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+" ("+userBean.getAgentZoneId() +")^Plaque "+request.getInput()+" Ntibikunze kumenya niba yemewe^Mwongere mukanya",request));
                }
                if(responseValidation.getStatusCode().equals("102")){
                    out.print(AppDesc.APP_DESC+"Condition applied to number plate: "+request.getInput()+" Status: "+responseValidation.getStatusCode()+" Message: "+responseValidation.getMessage());
                    return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+"^Plaque "+request.getInput()+" ^"+responseValidation.getMessage(),request));
                }else if(responseValidation.getStatusCode().equals("101")){
                    out.print(AppDesc.APP_DESC+"Condition applied to number plate: "+request.getInput()+" Status: "+responseValidation.getStatusCode()+" Message: "+responseValidation.getMessage());
                    return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+"^Plaque "+request.getInput()+" Ntiyemewe^",request));
                }else if(!responseValidation.getStatusCode().equals("100")){
                    out.print(AppDesc.APP_DESC+"Condition applied to number plate: "+request.getInput()+" Status: "+responseValidation.getStatusCode()+" Message: "+responseValidation.getMessage());
                    return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+"^Plaque "+request.getInput()+" ^"+responseValidation.getMessage(),request));
                }
                //if(! DataFactory.numberPlateValidator(request.getInput())){
                //  return ReturnConfig.isSuccess(carInOutMenu("Guparika^"+conductorNames+"^Plaque "+request.getInput()+" Ntiyemewe^",request));
                //}
                SessionData sessionData = new SessionData(sessionStatus.getSessionId(),
                        StepsConfig.ENTER_NUMBER_PLATE,
                        request.getInput(),
                        new Date());
                createSessionData(sessionData);
                
                sessionStatus.setCurrentStep(StepsConfig.ENTER_NUMBER_PLATE);
                sessionStatus.setNextStep(StepsConfig.ENTER_TICKET_TYPE);
                sessionStatus.setCurrentInput(request.getInput());
                sessionStatus.setLastAccessDate(date);
                sessionStatus.setLastAction("REQUESTING TICKET TYPE CAPTURE");
                sessionStatus.setStepsCount(2);
                updateSession(sessionStatus);
                
                reminder(sessionStatus);
                
                String carBalance = apInterface.getCarBalance(request.getInput());
                String message = "";
                try{
                    carBalance = carBalance != null ? carBalance : "0.0";
                    Double balance = Double.valueOf(carBalance);
                    message = balance < 0 ? "umwenda: " : "ubwizigame: ";
                    
                }catch(NumberFormatException | NullPointerException e){
                    e.printStackTrace(out);
                    carBalance = "0.0";
                }
                return ReturnConfig.isSuccess(continueInput(conductorNames+"^Hitamo ubwoko bwatike.^Imodoka: "+request.getInput()+"^Ifite "+message+carBalance+"^1. 100Rwf^2. 200Rwf^3. 300Rwf^4. 400Rwf^5. 1000Rwf",request));
            }else if(sessionStatus.getCurrentStep().equals(StepsConfig.ENTER_NUMBER_PLATE) && sessionStatus.getStepsCount() == 2 && sessionStatus.getNextStep().equals(StepsConfig.ENTER_TICKET_TYPE)){
                out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
                String ticketType;
                switch(request.getInput()){
                    case "1":
                        ticketType = "100";
                        break;
                    case "2":
                        ticketType = "200";
                        break;
                    case "3":
                        ticketType = "300";
                        break;
                    case "4":
                        ticketType = "400";
                        break;
                    case "5":
                        ticketType = "1000";
                        break;
                    default:
                        return ReturnConfig.isSuccess(continueInput(conductorNames+"^Mwahisemo ibitaribyo^Hitamo ubwoko bwatike.^Imodoka: "+sessionStatus.getCurrentInput()+"^1. 100Rwf^2. 200Rwf^3. 300Rwf^4. 400Rwf^5. 1000Rwf",request));
                }
                SessionData sessionData = new SessionData(sessionStatus.getSessionId(),
                        StepsConfig.ENTER_TICKET_TYPE,
                        ticketType,
                        new Date());
                createSessionData(sessionData);
                
                sessionData = sessionDataFacade.getBeforeLastSessionData(sessionStatus.getSessionId());
                String numberPlate = null;
                if(sessionData != null){
                    if(sessionData.getSessionkey().equals(StepsConfig.ENTER_NUMBER_PLATE))
                        numberPlate = sessionData.getSessionInput();
                }
                
                sessionStatus.setCurrentStep(StepsConfig.ENTER_TICKET_TYPE);
                sessionStatus.setNextStep(StepsConfig.ENTER_TICKET_TYPE);
                
                //commented this step
                //sessionStatus.setNextStep(StepsConfig.ENTER_CAR_BRAND);
                sessionStatus.setCurrentInput(request.getInput());
                sessionStatus.setLastAccessDate(date);
                
                //concluding ticket on this step
                sessionStatus.setCompleted(true);
                sessionStatus.setLastAction("CAR TICKET CONCLUDED");
                sessionStatus.setSessionStatus(SessionDataStatus.COMPLETED_STATUS);
                //sessionStatus.setLastAction("REQUESTING CAR BRAND CAPTURE");
                sessionStatus.setStepsCount(3);
                updateSession(sessionStatus);
                numberPlate = numberPlate == null ? "purake" : numberPlate;
                SessionTicketData sessionTicketData = new SessionTicketData();
                sessionTicketData.setSessionId(sessionStatus.getSessionId());
                List<SessionData> sessionDatas = sessionDataFacade.getSessionData(sessionStatus.getSessionId());
                if(sessionDatas != null){
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
                if(sessionTicketData.getnPlate().isEmpty()){
                    return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Hari ikibazo cyabaye mukwandika purake y'imodoka.^Mwongere mukanya."));
                }
                String result = ticketManager.genTicket(sessionStatus, userBean, sessionTicketData);
                if(!result.equals("SUCCESS")){
                    out.print(AppDesc.APP_DESC+" UssdProcessor processEntry failed to generate ticket result: "+result);
                    return ReturnConfig.isSuccess(successGen(request, conductorNames+"^"+result));
                }
                String carBalance = apInterface.getCarBalance(sessionTicketData.getnPlate());
                String message = "";
                try{
                    carBalance = carBalance != null ? carBalance : "0.0";
                    Double balance = Double.valueOf(carBalance);
                    message = balance < 0 ? "umwenda: " : "ubwizigame: ";
                    
                }catch(NumberFormatException | NullPointerException e){
                    e.printStackTrace(out);
                    carBalance = "0.0";
                }
                return ReturnConfig.isSuccess(successGen(request, conductorNames+"^Imodoka: "+sessionTicketData.getnPlate()+"^Ifite "+message+carBalance+"^Ishyizwe muri parikingi."));
                //return ReturnConfig.isSuccess(continueInput(conductorNames+"^Andika ubwoko bw'imodoka^Ifite: "+ numberPlate ,request));
            }else if(sessionStatus.getCurrentStep().equals(StepsConfig.ENTER_TICKET_TYPE) && sessionStatus.getStepsCount() == 3 && sessionStatus.getNextStep().equals(StepsConfig.ENTER_CAR_BRAND)){
                out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
                SessionData sessionData = new SessionData(sessionStatus.getSessionId(),
                        StepsConfig.ENTER_CAR_BRAND,
                        request.getInput(),
                        new Date());
                createSessionData(sessionData);
                
                sessionStatus.setCurrentStep(StepsConfig.ENTER_CAR_BRAND);
                sessionStatus.setNextStep(StepsConfig.ENTER_CAR_BRAND);
                sessionStatus.setCurrentInput(request.getInput());
                sessionStatus.setLastAccessDate(date);
                sessionStatus.setCompleted(true);
                sessionStatus.setLastAction("CAR TICKET CONCLUDED");
                sessionStatus.setSessionStatus(SessionDataStatus.COMPLETED_STATUS);
                sessionStatus.setStepsCount(4);
                updateSession(sessionStatus);
                ticketManager.setCarBrandOnTicket(sessionStatus, request.getInput());
                
                SessionTicketData sessionTicketData = new SessionTicketData();
                sessionTicketData.setSessionId(sessionStatus.getSessionId());
                List<SessionData> sessionDatas = sessionDataFacade.getSessionData(sessionStatus.getSessionId());
                if(sessionDatas != null){
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
                if(sessionTicketData.getnPlate() != null){
                    return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Imodoka: "+sessionTicketData.getCarBrand()+"^Ifite: "+sessionTicketData.getnPlate()+"^Ishyizwe muri parikingi."));
                }
                return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Hari ikibazo cyabaye mukwandika purake y'imodoka.^Mwongere mukanya."));
            }else{
                out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
                return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Hari ikibazo cyabaye mukwandika tiket y'imodoka.^Mwongere mukanya."));
            }
        }else{
            out.print(AppDesc.APP_DESC+"UssdProcessor processEntry requestor "+request.getMsisdn()+" Session count: "+sessionStatus.getStepsCount()+" Current Step: "+sessionStatus.getCurrentStep()+" Next Step: "+sessionStatus.getNextStep());
            return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Hari ikibazo cyabaye mukwandika tiket y'imodoka.^Mwongere mukanya."));
        }
    }
    private boolean validateEntry(UserBean userBean){
        if(userBean == null){
            out.print(AppDesc.APP_DESC+"UssdProcessor validateEntry no user found.");
            return false;
        }
        out.print(AppDesc.APP_DESC+"UssdProcessor validateEntry conductor found."+ userBean.getUserId());
        return true;
    }
    private ResponseConductor getConductor(String msisdn){
        return apInterface.getConductor(msisdn);
    }
    
    private void reminder(SessionStatus sessionStatus){
        String extension = idGenerator.generate();
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        //add 9 minutes to the current date
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MINUTE, 9);
        timestamp = new Timestamp(calendar.getTime().getTime());
        
        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Schedule for progressive initiator: "+sessionStatus.getInitTel()+" will expire on: "+timestamp);
        MyFrequency myFrequency = new MyFrequency("minute", "180000");
        List<JobTasks> mTasks = new ArrayList<>();
        JobTasks jobTasks = new JobTasks(extension+"^"+sessionStatus.getSessionId(),
                getClass().getName(),
                myFrequency,
                ApiConfig.PROGRESS_CALLBACK_URL,
                1);
        mTasks.add(jobTasks);
        
        MyJob mJob = new MyJob(extension+"^"+sessionStatus.getSessionId(),
                "daily",
                getClass().getName(),
                ApiConfig.PROGRESS_CALLBACK_URL,
                dateFormat.format(calendar.getTime()),
                true,
                myFrequency,
                mTasks);
        createSchedule(mJob);
    }
    
    private UserBean getUser(String msisdn){
        return apInterface.getUserByTel(msisdn);
    }
    public String faillureGen(UssdRequest request, String message){
        List<String> menus = new ArrayList<>();
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "B", message, 0, menu);
        String outPut = DataFactory.objectToXmlString(response);
        return outPut;
    }
    
    public String successGen(UssdRequest request, String message){
        List<String> menus = new ArrayList<>();
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "B", message, 0, menu);
        String outPut = DataFactory.objectToXmlString(response);
        return outPut;
    }
    
    public String continueInput(String message, UssdRequest request){
        List<String> menus = new ArrayList<>();
        String outPut;
        
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "C", message, 0, menu);
        outPut = DataFactory.objectToXmlString(response);
        
        return outPut;
    }
    
    public String carInOutMenu(String message, UssdRequest request){
        List<String> menus = new ArrayList<>();
        String outPut;
        
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "C", message+"Andika purake yimodoka.", 0, menu);
        outPut = DataFactory.objectToXmlString(response);
        
        return outPut;
    }
    
    private void createSchedule(MyJob mJob){
        out.print(AppDesc.APP_DESC+"UssdProcessor createSchedule creating a schedule: "+mJob.getJobId());
        
        Thread thread = new Thread(new BackgroundSchedule(true, mJob, openExternal, apInterface));
        thread.start();
    }
    
    private String sessionIdGen(String ussdSesssionId){
        try {
            boolean check = false;
            do{
                SessionStatus sessionStatus = sessionStatusFacade.getLastSession(ussdSesssionId);
                if(sessionStatus == null){
                    return ussdSesssionId;
                }
                ussdSesssionId = idGenerator.generate();
            }while(!check);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            out.print(AppDesc.APP_DESC+"UssdProcessor sessionIdGen thread sleep failed due to: "+ ex.getLocalizedMessage());
            Logger.getLogger(UssdProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return ussdSesssionId;
        }
        return ussdSesssionId;
    }
    
    private void updateSession (SessionStatus sessionStatus){
        out.print(AppDesc.APP_DESC+"UssdProcessor updateing Session for: "+sessionStatus.getSessionId());
        sessionStatusFacade.edit(sessionStatus);
        sessionStatusFacade.refreshSessionStatus();
    }
    private SessionStatus createSession(UssdRequest request, String sessionType, String nextStep){
        out.print(AppDesc.APP_DESC+"UssdProcessor createSession for: "+request.getMsisdn());
        
        Date date = new Date();
        SessionStatus sessionStatus = new SessionStatus(sessionIdGen(request.getSessionId()),
                request.getMsisdn(),
                StepsConfig.WLCM,
                null,
                nextStep,
                false,
                SessionDataStatus.ONGOING_STATUS,
                date,
                date,
                "INITIALIZE SESSION",
                sessionType,
                0,
                0);
        sessionStatusFacade.create(sessionStatus);
        sessionStatusFacade.refreshSessionStatus();
        return sessionStatus;
    }
    
    private SessionData createSessionData(SessionData sessionData){
        sessionDataFacade.create(sessionData);
        sessionDataFacade.refreshSessiondata();
        return sessionData;
    }
    
    private String idGenerator(){
        String genId = idGenerator.generate();
        try {
            boolean check = false;
            do{
                Progressive progressive = progressiveFacade.getProgressById(genId);
                if(progressive == null){
                    return genId;
                }
                genId = idGenerator.generate();
            }while(!check);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            out.print(AppDesc.APP_DESC+"UssdProcessor idGenerator thread sleep failed due to: "+ ex.getLocalizedMessage());
            Logger.getLogger(UssdProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return genId;
        }
        return genId;
    }
}

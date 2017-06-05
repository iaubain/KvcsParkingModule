/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.apiclient;

import com.kparking.oltranz.config.ApiConfig;
import com.kparking.oltranz.config.HeaderConfig;
import com.kparking.oltranz.config.KpmCmdConfig;
import com.kparking.oltranz.config.SchedCommandConfig;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorTel;
import com.kparking.oltranz.simplebeans.commonbeans.RequestGetElementById;
import com.kparking.oltranz.simplebeans.commonbeans.StatusBean;
import com.kparking.oltranz.simplebeans.conductors.ResponseConductor;
import com.kparking.oltranz.simplebeans.schedule.CancelSceduledJob;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import com.kparking.oltranz.simplebeans.sms.SmsSendResponse;
import com.kparking.oltranz.simplebeans.ticketsreport.PublishTicketRequest;
import com.kparking.oltranz.simplebeans.ticketsreport.PublishTicketResponse;
import com.kparking.oltranz.simplebeans.validation.RequestValidation;
import com.kparking.oltranz.simplebeans.validation.ResponseValidation;
import com.kparking.oltranz.simplebeans.verification.ResponseUserDetails;
import com.kparking.oltranz.utilities.DataFactory;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import net.manzi.frs.databeans.userbeans.UserBean;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class ApInterface {
     @EJB
            OpenExternal openExternal;
    MultivaluedMap<String, Object> headers;
    public ResponseConductor getConductor(String msisdn){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.CMD, KpmCmdConfig.GET_TEL_COND);
        headers.putSingle(HeaderConfig.CONTRACT, ApiConfig.KPM_CONTRACT);
        
        return (ResponseConductor) openExternal.doPost(ApiConfig.KPM_URL,
                headers,
                DataFactory.objectToString(new ConductorTel(msisdn)),
                MediaType.APPLICATION_JSON, ResponseConductor.class);   
    }
    
    public String getConductorDep(String conductorId){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.CMD, KpmCmdConfig.GET_DEPLOYEE_DEP);
        headers.putSingle(HeaderConfig.CONTRACT, ApiConfig.KPM_CONTRACT);
        
        return (String) openExternal.doPost(ApiConfig.KPM_URL,
                headers,
                DataFactory.objectToString(new RequestGetElementById(conductorId)),
                MediaType.APPLICATION_JSON, null); 
    }
    
    public String getParkingConductor(String parkingId){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.CMD, KpmCmdConfig.GET_DEPLOYEE_DEP);
        headers.putSingle(HeaderConfig.CONTRACT, ApiConfig.KPM_CONTRACT);
        
        return (String) openExternal.goGet(ApiConfig.GET_PARKING_USERS+parkingId,
                headers,
                MediaType.APPLICATION_JSON, null); 
    }
    
    public String getCarBalance(String numberPlate){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.WILDCARD);
        headers.putSingle(HeaderConfig.CMD, ApiConfig.GET_CAR_BALANCE_CMD);        
        headers.putSingle(HeaderConfig.DOMAIN, ApiConfig.CAR_ACCOUNT_DOMAIN);
        
        return (String) openExternal.doPost(ApiConfig.REQUEST_CAR_BALANCE,
                headers,
                numberPlate,
                MediaType.WILDCARD, null); 
    }
    
    public String getCarContact(String numberPlate){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.WILDCARD);
        headers.putSingle(HeaderConfig.CMD, ApiConfig.GET_CONTACT_CMD);        
        headers.putSingle(HeaderConfig.DOMAIN, ApiConfig.CAR_ACCOUNT_DOMAIN);
        
        return (String) openExternal.doPost(ApiConfig.GET_NUMBERPLATE_CONTACT,
                headers,
                numberPlate,
                MediaType.WILDCARD, null); 
    }
    
    public StatusBean verifyCar(String body){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        
        return (StatusBean) openExternal.doPost(ApiConfig.CAR_VERIFICATION,
                headers,
                body,
                MediaType.WILDCARD, StatusBean.class); 
    }
    
    public ResponseUserDetails getUser(String body){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        
        return (ResponseUserDetails) openExternal.doPost(ApiConfig.GET_SYSTEM_USER,
                headers,
                body,
                MediaType.WILDCARD, ResponseUserDetails.class); 
    }
    
    public String createSchedule(MyJob mJob){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.CMD, SchedCommandConfig.CREATE_JOB);
        headers.putSingle(HeaderConfig.CONTRACT, ApiConfig.SCHED_CONTRACT);
        
        return (String) openExternal.doPost(ApiConfig.SCHED_URL,
                headers,
                DataFactory.objectToString(mJob),
                MediaType.APPLICATION_JSON, null); 
    }
    
    public String cancelSchedule(CancelSceduledJob cancelSceduledJob){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.CMD, SchedCommandConfig.CANCEL_SCHEDULE);
        headers.putSingle(HeaderConfig.CONTRACT, ApiConfig.SCHED_CONTRACT);
        
        return (String) openExternal.doPost(ApiConfig.SCHED_URL,
                headers,
                DataFactory.objectToString(cancelSceduledJob),
                MediaType.APPLICATION_JSON, null); 
    }
    
    public SmsSendResponse sendSMS(String outStream){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.SIGNATURE, KpmCmdConfig.GET_TEL_COND);
        
        return (SmsSendResponse) openExternal.doPost(ApiConfig.SMS_URL,
                headers,
                outStream,
                MediaType.APPLICATION_JSON, SmsSendResponse.class);
    }
    
    public UserBean getUserByTel(String msisdn){
        return (UserBean) openExternal.goGet(ApiConfig.GET_USER_BY_TEL+""+msisdn, 
        null, 
        MediaType.APPLICATION_JSON, 
        UserBean.class);
    }
    
    public ResponseValidation validateNumberPlate(RequestValidation requestValidation){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        return (ResponseValidation) openExternal.doPost(ApiConfig.VALIDATE_NUMBER_PLATE,
                headers,
                DataFactory.objectToString(requestValidation),
                MediaType.APPLICATION_JSON, ResponseValidation.class);
    }
    
    public PublishTicketResponse publishTicket(PublishTicketRequest publishTicketRequest){
        headers = new MultivaluedHashMap<>();
        headers.putSingle(HeaderConfig.CONTENT, MediaType.APPLICATION_JSON);
        headers.putSingle(HeaderConfig.DOMAIN, ApiConfig.PUBLISH_TICKET_DOMAIN);
        headers.putSingle(HeaderConfig.CMD, ApiConfig.PUBLISH_TICKET_CMD);
        return (PublishTicketResponse) openExternal.doPost(ApiConfig.PUBLISH_TICKET,
                headers,
                DataFactory.objectToString(publishTicketRequest),
                MediaType.APPLICATION_JSON,
                PublishTicketResponse.class);
    }
}

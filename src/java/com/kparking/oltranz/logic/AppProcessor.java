/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.entities.Verification;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.facades.VerificationFacade;
import com.kparking.oltranz.simplebeans.commonbeans.DateRangeBean;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdRequest;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.ReturnConfig;
import com.kparking.oltranz.utilities.TicketFactory;
import static java.lang.System.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class AppProcessor {
    
    @EJB
            UssdProcessor ussdProcessor;
    @EJB
            TicketFacade ticketFacade;
    @EJB
            TicketFactory ticketFactory;
    @EJB
            VerificationFacade verificationFacade;
    @EJB
            SmsSender smsSender;
    
    public Response carInprocessor(HttpHeaders headers, String body){
        try{
            out.print(AppDesc.APP_DESC+"received from USSD agregator: "+body);
            UssdRequest request = (UssdRequest) DataFactory.xmlStringToObject(UssdRequest.class, body);
            if(request == null){
                out.print(AppDesc.APP_DESC+"failed to process request due to: null Request");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
            }
            return ussdProcessor.receiveCarInRequest(request);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
        }
    }
    
    public Response carOutprocessor(HttpHeaders headers, String body){
        try{
            out.print(AppDesc.APP_DESC+"received from USSD agregator: "+body);
            UssdRequest request = (UssdRequest) DataFactory.xmlStringToObject(UssdRequest.class, body);
            if(request == null){
                out.print(AppDesc.APP_DESC+"failed to process request due to: null Request");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
            }
            return ussdProcessor.receiveCarOut(request);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
        }
    }
    
    public Response checkCar(HttpHeaders headers, String body){
        try{
            out.print(AppDesc.APP_DESC+"received from USSD agregator: "+body);
            UssdRequest request = (UssdRequest) DataFactory.xmlStringToObject(UssdRequest.class, body);
            if(request == null){
                out.print(AppDesc.APP_DESC+"failed to process request due to: null Request");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
            }
            return ussdProcessor.checkCar(request);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
        }
    }
    
    public Response signupCar(HttpHeaders headers, String body){
        try{
            out.print(AppDesc.APP_DESC+"received from USSD agregator: "+body);
            UssdRequest request = (UssdRequest) DataFactory.xmlStringToObject(UssdRequest.class, body);
            if(request == null){
                out.print(AppDesc.APP_DESC+"failed to process request due to: null Request");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
            }
            return ussdProcessor.signupCar(request);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
        }
    }
    
    public Response verifyCar(String body){
        try{
            out.print(AppDesc.APP_DESC+"received from verifier app: "+body);
            return ussdProcessor.verifyCar(body);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Bad format.");
        }
    }
    
    public Response getAllTickets(String body){
        try{
            DateRangeBean dateRangeBean = (DateRangeBean)DataFactory.stringToObject(DateRangeBean.class, body);
            if(dateRangeBean == null){
                out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: empty request date range");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty request date range");
            }
            DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date startDate = dFormat.parse(dateRangeBean.getStartDate());
            Date endDate = dFormat.parse(dateRangeBean.getEndDate());
            List<Ticket> mTickets = ticketFacade.getTicketPerDate(startDate, endDate);
            String outPut = ticketFactory.tuneTicketList(mTickets);
            if(outPut != null){
                out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes succeeded with outPut: "+outPut);
                return ReturnConfig.isSuccess(outPut);
            }
            out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: null result");
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Expectation failed due to empty result.");
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, ""+ e.getMessage());
        }
    }
    
    public Response verifyCarReport(String body){
        try{
            DateRangeBean dateRangeBean = (DateRangeBean)DataFactory.stringToObject(DateRangeBean.class, body);
            if(dateRangeBean == null){
                out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: empty request date range");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Empty request date range");
            }
            DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date startDate = dFormat.parse(dateRangeBean.getStartDate());
            Date endDate = dFormat.parse(dateRangeBean.getEndDate());
            List<Verification> mVerifications = verificationFacade.getTicketPerDate(startDate, endDate);
            String outPut = DataFactory.objectToString(mVerifications);
            if(outPut != null){
                out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes succeeded with outPut: "+outPut);
                return ReturnConfig.isSuccess(outPut);
            }
            out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: null result");
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Expectation failed due to empty result.");
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" AppProcessor getAllTicktes failed to process request due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error: "+ e.getMessage());
        }
    }
}

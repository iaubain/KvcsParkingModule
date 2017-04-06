/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.entities.ParkingInfo;
import com.kparking.oltranz.facades.ParkingInfoFacade;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.ReturnConfig;
import com.kparking.oltranz.utilities.TicketFactory;
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
public class ParkingInfoExposed {
    @EJB
            ParkingInfoFacade parkingInfoFacade;
    @EJB
            TicketFactory ticketFactory;
    
    /*
    Apply an HTTP method: GET
    URL on Oltranz testSever
    41.74.172.132:8080/Kparking/system/getParkInfo
    URL on KVCS Server
    10.10.79.228:8080/Kparking/system/getParkInfo
    
    In http request header put the following field and their value in order to get results:
    MSISDN(this stands for an MSISDN of conductor or any other party that can generate ticket for cars)
    N_PLATE(this is a customer's car number plate)
    PARK_SES(This is the ticket's parking sessionId)
    
    eg of header value:
    N_PLATE:RAC000E
    */
    
    public Response receiver(HttpHeaders headers){
//        String what;

if(headers.getHeaderString("MSISDN") != null){
    return parkInfoByMsisdn(headers.getHeaderString("MSISDN"));
}
if(headers.getHeaderString("N_PLATE") != null){
    return parkInfoByNmberPlate(headers.getHeaderString("N_PLATE"));
}
if(headers.getHeaderString("PARK_SES") != null){
    return parkInfoBySession(headers.getHeaderString("PARK_SES"));
}
//        if(headers.getHeaderString("CMD") != null){
//            what = headers.getHeaderString("CMD");
//            switch(what){
//                case "MSISDN":
//                    return parkInfoByMsisdn(headers.getHeaderString("MSISDN"));
//                case "N_PLATE":
//                    return parkInfoByNmberPlate(headers.getHeaderString("N_PLATE"));
//                case "PARK_SES":
//                    return parkInfoBySession(headers.getHeaderString("PARK_SES"));
//                default:
//                    return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Invalid command.");
//            }
//        }
return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Try again, something went wrong");
    }
    private Response parkInfoByMsisdn(String msisdn){
        List<ParkingInfo> parkingInfos = parkingInfoFacade.getConductorParkInfo(msisdn);
        if(parkingInfos == null){
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Try again, something went wrong or no data found");
        }
        return ReturnConfig.isSuccess(DataFactory.objectToString(ticketFactory.genParkingInfoList(parkingInfos)));
    }
    private Response parkInfoByNmberPlate(String numberPlate){
        List<ParkingInfo> parkingInfos = parkingInfoFacade.getCustomerParkInfo(numberPlate.toUpperCase());
        if(parkingInfos == null){
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Try again, something went wrong or no data found");
        }
        return ReturnConfig.isSuccess(DataFactory.objectToString(ticketFactory.genParkingInfoList(parkingInfos)));
    }
    private Response parkInfoBySession(String sessionId){
        List<ParkingInfo> parkingInfos = parkingInfoFacade.getSessionParkInfo(sessionId);
        if(parkingInfos == null){
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Try again, something went wrong or no data found");
        }
        return ReturnConfig.isSuccess(DataFactory.objectToString(ticketFactory.genParkingInfoList(parkingInfos)));
    }
}

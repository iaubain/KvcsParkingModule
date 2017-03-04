/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.HeaderConfig;
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
}

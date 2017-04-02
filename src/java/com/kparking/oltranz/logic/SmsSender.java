/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.config.ApiConfig;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.SMSConfig;
import com.kparking.oltranz.simplebeans.sms.SendSmsRequest;
import com.kparking.oltranz.simplebeans.sms.SmsSendResponse;
import com.kparking.oltranz.utilities.DataFactory;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class SmsSender {
    @EJB
            ApInterface apInterface;
    public void send(String destMsisdn, String message){
        try{
            if(destMsisdn.isEmpty()){
                out.print(AppDesc.APP_DESC+"SmsSender send missing destination address for message: "+message);
            }
            String outStream = DataFactory.objectToString(new SendSmsRequest(SMSConfig.SRC_1, destMsisdn, message, 0, ApiConfig.SMS_CONTRACT));
            out.print(AppDesc.APP_DESC+"SmsSender send sending: "+ outStream);
            SmsSendResponse smsSendResponse = apInterface.sendSMS(outStream);
            out.print(AppDesc.APP_DESC+"SmsSender send received: "+ DataFactory.objectToString(smsSendResponse));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"SmsSender send sending to: "+ destMsisdn+" with message: "+message+" failed due to: "+e.getLocalizedMessage());
        }
    }
}

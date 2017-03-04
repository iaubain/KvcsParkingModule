/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.SMSConfig;
import com.kparking.oltranz.entities.Ticket;
import static java.lang.System.out;
import javax.ejb.EJB;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class BackgroundSMS  implements Runnable{
    @EJB
            SmsSender smsSender;
    @EJB
            CustomerProvider customerProvider;
    Ticket ticket;
    boolean isCarIn;
    boolean isCarOut;
    boolean isAddCharge;
    
    public BackgroundSMS(SmsSender smsSender, CustomerProvider customerProvider, Ticket ticket, boolean isCarIn, boolean isCarOut, boolean isAddCharge) {
        this.smsSender = smsSender;
        this.customerProvider = customerProvider;
        this.ticket = ticket;
        this.isCarIn = isCarIn;
        this.isCarOut = isCarOut;
        this.isAddCharge = isAddCharge;
    }
    
    @Override
    public void run() {
        exec();
    }
    
    private void exec(){
        try{
            if(isCarIn)
                smsSender.send(customerProvider.genMsisdn(ticket.getNumberPlate(), ticket.getMsisdn()), SMSConfig.CAR_IN_MSG+ticket.getParkingDesc());
            if(isAddCharge)
                smsSender.send(customerProvider.genMsisdn(ticket.getNumberPlate(), ticket.getMsisdn()), SMSConfig.CAR_ADDED_VALUE+ticket.getParkingDesc());
            if(isCarOut)
                smsSender.send(customerProvider.genMsisdn(ticket.getNumberPlate(), ticket.getMsisdn()), SMSConfig.CAR_OUT_MSG+ticket.getParkingDesc());
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"BackgroundSMS exec sending SMS failed due to: "+e.getMessage());
        }
    }
    
}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
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
    String message;    
    boolean isConductor;    
    String action;
    String numberPlate;
    
    public BackgroundSMS(SmsSender smsSender, CustomerProvider customerProvider, Ticket ticket, String message, boolean isConductor) {
        this.smsSender = smsSender;
        this.customerProvider = customerProvider;
        this.ticket = ticket;
        this.message = message;
        this.isConductor = isConductor;
    }

    public BackgroundSMS(SmsSender smsSender, CustomerProvider customerProvider, String message, String action, String numberPlate) {
        this.smsSender = smsSender;
        this.customerProvider = customerProvider;
        this.message = message;
        this.action = action;
        this.numberPlate = numberPlate;
    }
    
    @Override
    public void run() {
        exec();
    }
    
    private void exec(){
        if(isConductor){
            try{
                smsSender.send(ticket.getMsisdn(), message);
            }catch(Exception e){
                out.print(AppDesc.APP_DESC+"BackgroundSMS exec sending SMS failed due to: "+e.getMessage());
            }
        }else{
            try{
                out.print(AppDesc.APP_DESC+"BackgroundSMS exec: Sending SMS to customer");
                if(customerProvider.genCustomerSms(numberPlate, action, message)){
                    out.print(AppDesc.APP_DESC+"BackgroundSMS exec Customerprovided returned positive result");
                }else{
                    out.print(AppDesc.APP_DESC+"BackgroundSMS exec Customerprovided results in possible faillure");
                }
                //smsSender.send(customerProvider.genMsisdn(ticket.getNumberPlate(), ticket.getMsisdn()), message);
            }catch(Exception e){
                out.print(AppDesc.APP_DESC+"BackgroundSMS exec sending SMS failed due to: "+e.getMessage());
            }
        }
        
    }
    
}

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
    
    public BackgroundSMS(SmsSender smsSender, CustomerProvider customerProvider, Ticket ticket, String message) {
        this.smsSender = smsSender;
        this.customerProvider = customerProvider;
        this.ticket = ticket;
        this.message = message;
    }
    
    @Override
    public void run() {
        exec();
    }
    
    private void exec(){
        try{
                smsSender.send(customerProvider.genMsisdn(ticket.getNumberPlate(), ticket.getMsisdn()), message);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"BackgroundSMS exec sending SMS failed due to: "+e.getMessage());
        }
    }
    
}

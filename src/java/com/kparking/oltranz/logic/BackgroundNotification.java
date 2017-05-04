/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.NotificationConfig;
import static java.lang.System.out;
import javax.ejb.EJB;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class BackgroundNotification implements Runnable{
    @EJB
            SmsSender smsSender;
    String message;
    String subject;
    String[] phoneDestinations;
    String[] mailDestinations;
    String source;
    String type;
    
    public BackgroundNotification(SmsSender smsSender, String message, String subject, String[] phoneDestinations, String[] mailDestinations, String source, String type) {
        this.smsSender = smsSender;
        this.message = message;
        this.subject = subject;
        this.phoneDestinations = phoneDestinations;
        this.mailDestinations = mailDestinations;
        this.source = source;
        this.type = type;
    } 
    
    @Override
    public void run() {
        switch(type){
            case NotificationConfig.ALL_NOTIF:
                sendSMS();
                sendMail();
                break;
            case NotificationConfig.EMAIL_NOTIF:
                sendMail();
                break;
            case NotificationConfig.SMS_NOTIF:
                sendSMS();
                break;
            case NotificationConfig.URL_NOTIF:
                sendToUrl();
                break;
            default:
                out.print(AppDesc.APP_DESC+"Notification type not supported yet");
                break;
        }
    }
    
    private void sendSMS(){
        try {
            for(String destination : phoneDestinations){
                smsSender.send(destination, message);
            }
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"sending SMS failed due to: "+e.getMessage());
        }
    }
    
    private void sendMail(){
        try {
            for(String to : mailDestinations){
                out.print(AppDesc.APP_DESC+"Sending mail To:"+to+" Subject: "+subject+" Message: "+message);
                EmailSender.sendMail(to, subject, message);
                //pauseExec();
            }
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"Sending mail failed due to: "+e.getMessage());
        }
    }
    
    private void pauseExec(){
        try {
            Thread.sleep(1000*2);
        } catch (InterruptedException e) {
            out.print(AppDesc.APP_DESC+"Sending mail Thread paused interrupted"+e.getMessage());
        }
    }
    private void sendToUrl(){
        try {
            out.print(AppDesc.APP_DESC+"sending to URL not supported now");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"sending to URL failed due to: "+e.getMessage());
        }
    }
}

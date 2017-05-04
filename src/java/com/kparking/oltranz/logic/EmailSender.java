/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import static java.lang.System.out;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class EmailSender {
    
//    public String sendEmail(String from, String to, String subject, String messageToSend){
//        String host="outlook.office365.com";
//        final String user="oltanznotiffications@outlook.com";
//        final String password="oltranz2day!";
//        
//        //Get the session object
//        Properties props = new Properties();
//        
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.host",host);
//        
//        Session session = Session.getInstance (props,new javax.mail.Authenticator(){
//            protected PasswordAuthentication getPasswordAuthentication(){
//                return new PasswordAuthentication(user, password);
//            }});
//        out.print(AppDesc.APP_DESC+"EmailSender sendEmail Sending mail To:"+to+" Subject: "+subject+" Message: "+messageToSend);
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(user));
//            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
//            message.setSubject(subject != null ? subject : "KVCS Gateway Module Report");
//            message.setSentDate(new Date());
//            message.setContent(AppDesc.APP_DESC+""+messageToSend, "text/html");
//            //message.setText(AppDesc.APP_DESC+""+messageToSend);
//            
//            //send the message
//            Transport.send(message);
//            
//            
//            out.print(AppDesc.APP_DESC+"message sent successfully... TO: "+to+" subject: "+subject+" message: "+messageToSend);
//            return "SUCCESS";
//        } catch (MessagingException e) {
//            out.print(AppDesc.APP_DESC+" Sending mail faillure: "+e.getMessage());
//            out.print(AppDesc.APP_DESC+"message sent Failed... TO: "+to+" subject: "+subject+" message: "+messageToSend+" Due to: "+e.getMessage());
//            return "FAILLURE";
//        }
//    }
    
    public static String sendMail(String to,String subject,String messageToSend){
        
        final String sender = "oltanznotiffications@outlook.com";
        final String credential = "oltranz2day!";
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance (props,new javax.mail.Authenticator()
                
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(sender, credential);
            }
        }
        );
        
        try {
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(messageToSend, "text/html");
            //message.setText(msg);
            
            Transport.send(message);
            
        }
        catch (MessagingException e) {
            out.print(AppDesc.APP_DESC+" Sending mail faillure: "+e.getMessage());
            out.print(AppDesc.APP_DESC+"message sent Failed... TO: "+to+" subject: "+subject+" message: "+messageToSend+" Due to: "+e.getMessage());
            throw new RuntimeException(e);
        }
        
        out.print(AppDesc.APP_DESC+"message sent successfully... TO: "+to+" subject: "+subject+" message: "+messageToSend);
        return "SUCCESS";
    }
}

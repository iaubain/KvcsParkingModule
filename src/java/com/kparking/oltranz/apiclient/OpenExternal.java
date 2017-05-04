/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.apiclient;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.LinkStateConfig;
import com.kparking.oltranz.config.NotificationConfig;
import com.kparking.oltranz.entities.DependencyStatus;
import com.kparking.oltranz.entities.LinkStatus;
import com.kparking.oltranz.facades.DependencyStatusFacade;
import com.kparking.oltranz.facades.LinkStatusFacade;
import com.kparking.oltranz.logic.BackgroundNotification;
import com.kparking.oltranz.logic.SmsSender;
import com.kparking.oltranz.utilities.DataFactory;
import static java.lang.System.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class OpenExternal {
    @EJB
            LinkStatusFacade linkStatusFacade;
    @EJB
            DependencyStatusFacade dependencyStatusFacade;
    @EJB
            SmsSender smsSender;
    
    public Object doPost(String url, MultivaluedMap<String, Object> headers, String body, String mediaType, Class mappingClass){
        out.print(AppDesc.APP_DESC+" OpenExternal doPost going to post to External link: "+url+" and "+body);
        try{
            Client client = ClientBuilder.newClient();
            WebTarget target =client.target(url);
            Response response;
            if(headers == null){
                response = target.request().post(Entity.entity(body, mediaType));
            }else{
                response = target.request()
                        .headers(headers)
                        .post(Entity.entity(body, mediaType));
            }
            
            if(response == null){
                out.print(AppDesc.APP_DESC+"OpenExternal doPost failed to get result: null Response object from: "+url);
                return null;
            }
            
            int statusCode = response.getStatus();
            String received = response.readEntity(String.class).trim();
            out.print(AppDesc.APP_DESC+" received from External link "+url+" HTTP Status: "+statusCode+" and body: "+received);
            linkState(url, statusCode, received);
            if(statusCode != 200){
                out.print(AppDesc.APP_DESC+"OpenExternal doPost failed to get result from  "+url+" : HTTP status: "+statusCode+" with message: "+received);
                return null;
            }
            
            if(mappingClass == null){
                out.print(AppDesc.APP_DESC+"OpenExternal doPost no mapping class for  "+url+" : HTTP status: "+statusCode+" with message: "+received);
                return received;
            }
            return mapString(received, mappingClass);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" OpenExternal doPost Error contacting External  "+url+"  due to: "+e.getLocalizedMessage());
            return null;
        }
    }
    
    public Object goGet(String url, MultivaluedMap<String, Object> headers, String mediaType, Class mappingClass){
        out.print(AppDesc.APP_DESC+" OpenExternal doPost going to get from the External link: "+url);
        try{
            Client client = ClientBuilder.newClient();
            WebTarget target =client.target(url);
            Response response;
            
            if(headers == null){
                response = target.request().get();
            }else{
                response = target.request()
                        .headers(headers)
                        .get();
            }
            
            int statusCode = response.getStatus();
            String received = response.readEntity(String.class).trim();
            out.print(AppDesc.APP_DESC+" received from External link "+url+" HTTP Status: "+statusCode+" and body: "+received);
            linkState(url, statusCode, received);
            if(statusCode != 200){
                out.print(AppDesc.APP_DESC+"OpenExternal doPost failed to get result from  "+url+" : HTTP status: "+statusCode+" with message: "+received);
                return null;
            }
            
            if(mappingClass == null){
                out.print(AppDesc.APP_DESC+"OpenExternal doPost no mapping class for  "+url+" : HTTP status: "+statusCode+" with message: "+received);
                return received;
            }
            return mapString(received, mappingClass);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" OpenExternal doPost Error contacting External due to: "+e.getLocalizedMessage());
            return null;
        }
    }
    
    private void linkState(String url, int responseStatus, String requestBody){
        try {
            DateFormat dFomat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date cDate = new Date();
            List<LinkStatus> statusList = linkStatusFacade.findAll();
            
            if(requestBody.length() >= 1020)
                requestBody = requestBody.substring(0, 900);
            
            DependencyStatus dependencyStatus = new DependencyStatus(url,
                    responseStatus == 200 ? LinkStateConfig.LINK_UP :
                            responseStatus == 404 ? LinkStateConfig.LINK_DOWN :
                                    responseStatus == 500 ? LinkStateConfig.LINK_SERVER_ERROR :
                                            responseStatus == 204 ? LinkStateConfig.LINK_SERVER_ERROR :
                                                    LinkStateConfig.LINK_SERVER_ERROR,
                    dFomat.parse(dFomat.format(cDate)),
                    responseStatus == 200 ? "Success" : requestBody,
                    responseStatus);
            
            dependencyStatusFacade.create(dependencyStatus);
            dependencyStatusFacade.refreshDependency();
            
            if(statusList.isEmpty()){
                out.print(AppDesc.APP_DESC+"no notification state found: "+ url+" response status: "+responseStatus);
                return;
            }
            
            for(LinkStatus linkStatus : statusList){
                if(responseStatus == linkStatus.getStatusCode()){
                    if(linkStatus.isNotify() && linkStatus.getNotificationStatus().equals(NotificationConfig.NOTIF_ACTIVE)){
                        out.print(AppDesc.APP_DESC+" Checking notification for: "+DataFactory.objectToString(linkStatus));
                        switch(linkStatus.getNotificationMedium()){
                            case NotificationConfig.ALL_NOTIF:
                                Thread bNotification = new Thread(new BackgroundNotification(smsSender,
                                        url+" reports: "+linkStatus.getNotificationType()+" with http response status: "+responseStatus,
                                        "KVCS PARKING GATEWAY NOTIFICATION",
                                        NotificationConfig.ADMIN_TEL,
                                        NotificationConfig.ADMIN_MAILS,
                                        AppDesc.APP_DESC,
                                        NotificationConfig.ALL_NOTIF));
                                bNotification.start();
                                break;
                            case NotificationConfig.EMAIL_NOTIF:
                                bNotification = new Thread(new BackgroundNotification(smsSender,
                                        url+" reports: "+linkStatus.getNotificationType()+" with http response status: "+responseStatus,
                                        "KVCS PARKING GATEWAY NOTIFICATION",
                                        NotificationConfig.ADMIN_TEL,
                                        NotificationConfig.ADMIN_MAILS,
                                        AppDesc.APP_DESC,
                                        NotificationConfig.EMAIL_NOTIF));
                                bNotification.start();
                                break;
                            case NotificationConfig.SMS_NOTIF:
                                bNotification = new Thread(new BackgroundNotification(smsSender,
                                        url+" reports: "+linkStatus.getNotificationType()+" with http response status: "+responseStatus,
                                        "KVCS PARKING GATEWAY NOTIFICATION",
                                        NotificationConfig.ADMIN_TEL,
                                        NotificationConfig.ADMIN_MAILS,
                                        AppDesc.APP_DESC,
                                        NotificationConfig.SMS_NOTIF));
                                bNotification.start();
                                break;
                            case NotificationConfig.URL_NOTIF:
                                bNotification = new Thread(new BackgroundNotification(smsSender,
                                        url+" reports status "+linkStatus.getNotificationType()+" with http response status: "+responseStatus,
                                        "KVCS PARKING GATEWAY NOTIFICATION",
                                        NotificationConfig.ADMIN_TEL,
                                        NotificationConfig.ADMIN_MAILS,
                                        AppDesc.APP_DESC,
                                        NotificationConfig.URL_NOTIF));
                                bNotification.start();
                                break;
                            case NotificationConfig.NO_NOTIF:
                                out.print(AppDesc.APP_DESC+" there is no notification set for:"+responseStatus+" status");
                                break;
                            default:
                                out.print(AppDesc.APP_DESC+" there is no notification found for:"+responseStatus+" status");
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"failed to manage link status of: "+ url+" response status: "+responseStatus+" due to: "+e.getMessage());
        }
    }
    
    private Object mapString(String input, Class mappingClass){
        try{
            return DataFactory.stringToObject(mappingClass, input);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" OpenExternal mapString mapping failed due to: "+e.getLocalizedMessage()+" input string: "+input);
            return null;
        }
    }
}

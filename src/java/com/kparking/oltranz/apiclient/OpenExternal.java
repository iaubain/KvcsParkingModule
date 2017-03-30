/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.apiclient;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.utilities.DataFactory;
import static java.lang.System.out;
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
    
    private Object mapString(String input, Class mappingClass){
        try{
            return DataFactory.stringToObject(mappingClass, input);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" OpenExternal mapString mapping failed due to: "+e.getLocalizedMessage()+" input string: "+input);
            return null;
        }
    }
}

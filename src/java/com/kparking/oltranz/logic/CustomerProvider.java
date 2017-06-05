/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.config.ActionConfig;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Car;
import com.kparking.oltranz.facades.CarFacade;
import com.kparking.oltranz.simplebeans.contacts.NumberPlateContact;
import com.kparking.oltranz.utilities.DataFactory;
import static java.lang.System.out;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Hp
 */
@Stateless
public class CustomerProvider {
    @EJB
            SmsSender smsSender;
    @EJB
            CarFacade carFacade;
    @EJB
            ApInterface apInterface;
    public String genMsisdn(String nPlate, String initMsisdn){
        nPlate = nPlate.toUpperCase();
        Car car = carFacade.getCustormerLastRecord(nPlate);
        if(car == null){
            out.print(AppDesc.APP_DESC+"CustomerProvider genMsisdn failed to get Car owner MSISDN");
            return null;
        }
        
        if(car.getCreatorTel() != null){
            String tel = car.getCreatorTel();
            out.print(AppDesc.APP_DESC+"CustomerProvider genMsisdn car owner MSISDN: "+tel);
            return tel;
        }else{
            out.print(AppDesc.APP_DESC+"CustomerProvider genMsisdn failed to get Car owner MSISDN");
            return null;
        }
    }
    
    public boolean genCustomerSms(String numberPlate, String action, String actionDesc){
        try{
            String rawResult = apInterface.getCarContact(numberPlate);
            if(rawResult == null){
                out.print(AppDesc.APP_DESC+"CustomerProvider genCustomerSms Failed to send SMS to customer with numberPlate: "+numberPlate+" due to: EMPTY RESULT FROM CUSTOMER CONTACT LINK");
                return false;
            }
            
            List<Object> rawCustomer = (List<Object>) DataFactory.stringToObjectList(NumberPlateContact.class, rawResult);
            if(rawCustomer.isEmpty()){
                out.print(AppDesc.APP_DESC+"CustomerProvider genCustomerSms Failed to send SMS to customer with numberPlate: "+numberPlate+" due to: NO CONTACT FOUND");
                return false;
            }
            
            for(Object object : rawCustomer){
                NumberPlateContact numberPlateContact = (NumberPlateContact) object;
                switch(action){
                    case ActionConfig.CAR_ADDITIONAL_TICKET:
                        if(numberPlateContact.getLang().equalsIgnoreCase("fr")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" l'auto avec:"+numberPlate+" a un ticket additionel "+actionDesc);
                        }else if(numberPlateContact.getLang().equalsIgnoreCase("us")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" a car with:"+numberPlate+" gets additional ticket "+actionDesc);
                        }else{
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" imodoka ifite:"+numberPlate+" yandikiwe indi tike "+actionDesc);
                        }
                        break;
                    case ActionConfig.CAR_IN_ACTION:
                        if(numberPlateContact.getLang().equalsIgnoreCase("fr")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" l'auto avec:"+numberPlate+" est garé "+actionDesc);
                        }else if(numberPlateContact.getLang().equalsIgnoreCase("us")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" a car with:"+numberPlate+" gets in the parking "+actionDesc);
                        }else{
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" imodoka ifite:"+numberPlate+" ishyizwe muri parkingi "+actionDesc);
                        }
                        break;
                    case ActionConfig.CAR_OUT_ACTION:
                        if(numberPlateContact.getLang().equalsIgnoreCase("fr")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" l'auto avec:"+numberPlate+" est sorti du parking "+actionDesc);
                        }else if(numberPlateContact.getLang().equalsIgnoreCase("us")){
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" a car with:"+numberPlate+" gets out of the parking "+actionDesc);
                        }else{
                            smsSender.send(numberPlateContact.getAddress(), numberPlateContact.getContactName()+" imodoka ifite:"+numberPlate+" ivanywe muri parkingi "+actionDesc);
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"CustomerProvider genCustomerSms Failed to send SMS to customer with numberPlate: "+numberPlate+" due to: "+e.getMessage());
            return false;
        }
        
    }
}

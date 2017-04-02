/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Car;
import com.kparking.oltranz.facades.CarFacade;
import static java.lang.System.out;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Hp
 */
@Stateless
public class CustomerProvider {
    @EJB
            CarFacade carFacade;
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
}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import javax.ejb.Stateless;

/**
 *
 * @author Hp
 */
@Stateless
public class CustomerProvider {
    public String genMsisdn(String nPlate, String initMsisdn){
        String checkPlate = nPlate.toUpperCase();
        switch(checkPlate){
            case "RAD520E":
                return "250788312609";
            case "RAD000E":
                return "250785534672";
            case "RAD120D":
                return "250736864662";
            case "RAD001Q":
                return "250788625722";
            case "RAD002W":
                return "250788251119";
            case "RAD003E":
                return "25086367970";
            case "RAD004R":
                return "250736864662";
            default:
                return initMsisdn;
        }
    }
}

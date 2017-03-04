/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.apiclient;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.utilities.DataFactory;
import static java.lang.System.out;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public abstract class AbstractMapper<T> {
    private Class<T> mappingClass;

    public AbstractMapper(Class<T> mappingClass) {
        this.mappingClass = mappingClass;
    }
    
    public T mapInput(T className, String input){
        try{
            return (T) DataFactory.stringToObject(className.getClass(), input);
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+" mapping failed due to: "+e.getLocalizedMessage()+" input string: "+input);
            return null;
        }
    }
}

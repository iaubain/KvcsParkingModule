/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.contacts;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class NumberPlateContact {
    private String address; //email, phone number, etc
    private String addressType;// email, msisdn, etc found in the contactTYpe table
    private String numberPlate;
    private String contactName;
    private String lang;  //  fr/rw/us

    public NumberPlateContact() {
    }

    public NumberPlateContact(String address, String addressType, String numberPlate, String contactName, String lang) {
        this.address = address;
        this.addressType = addressType;
        this.numberPlate = numberPlate;
        this.contactName = contactName;
        this.lang = lang;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
}

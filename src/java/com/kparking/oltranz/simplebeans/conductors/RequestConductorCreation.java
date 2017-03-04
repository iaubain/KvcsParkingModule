/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.conductors;

import com.kparking.oltranz.simplebeans.commonbeans.Address;


/**
 *
 * @author Hp
 */
public class RequestConductorCreation {
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String tel;
    private Address address;
    private String imagePath;

    public RequestConductorCreation() {
    }

    public RequestConductorCreation(String firstName, String middleName, String lastName, String gender, String tel, Address address, String imagePath) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.tel = tel;
        this.address = address;
        this.imagePath = imagePath;
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
}

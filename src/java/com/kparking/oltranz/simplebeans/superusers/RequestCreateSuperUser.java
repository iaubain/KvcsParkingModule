/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.superusers;

import com.kparking.oltranz.simplebeans.commonbeans.Address;


/**
 *
 * @author Hp
 */
public class RequestCreateSuperUser {
    private String userNames;
    private String tel;
    private String email;
    private Address address;
    private String permissions;

    public RequestCreateSuperUser() {
    }

    public RequestCreateSuperUser(String userNames, String tel, String email, Address address, String permissions) {
        this.userNames = userNames;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.permissions = permissions;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
}

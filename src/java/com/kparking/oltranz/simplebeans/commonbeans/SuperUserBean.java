/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.commonbeans;

/**
 *
 * @author Hp
 */
public class SuperUserBean {
    private String userId;
    private String userNames;
    private String tel;
    private String email;
    private Address address;
    private StatusBean status;

    public SuperUserBean() {
    }

    public SuperUserBean(String userId, String userNames, String tel, String email, Address address, StatusBean status) {
        this.userId = userId;
        this.userNames = userNames;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.status = status;
    }
    
    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}

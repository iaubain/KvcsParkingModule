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
public class StatusBean {
    private int statusCode;
    private String statusDesc;

    public StatusBean() {
    }

    public StatusBean(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
}

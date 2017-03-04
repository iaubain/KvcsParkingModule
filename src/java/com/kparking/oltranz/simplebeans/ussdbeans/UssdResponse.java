/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.ussdbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hp
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "COMMAND")
public class UssdResponse {
    @XmlElement(name = "MSISDN")
    private String msisdn;
    @XmlElement(name = "SESSIONID")
    private String sessionId;
    @XmlElement(name = "FREEFLOW")
    private String freeFlow;
    @XmlElement(name = "MESSAGE")
    private String message;
    @XmlElement(name = "NEWREQUEST")
    private int newRequest;
    @XmlElement(name = "MENUS")
    private Menu menus;

    public UssdResponse() {
    }

    public UssdResponse(String msisdn, String sessionId, String freeFlow, String message, int newRequest, Menu menus) {
        this.msisdn = msisdn;
        this.sessionId = sessionId;
        this.freeFlow = freeFlow;
        this.message = message;
        this.newRequest = newRequest;
        this.menus = menus;
    }

    public Menu getMenus() {
        return menus;
    }

    public void setMenus(Menu menus) {
        this.menus = menus;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFreeFlow() {
        return freeFlow;
    }

    public void setFreeFlow(String freeFlow) {
        this.freeFlow = freeFlow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(int newRequest) {
        this.newRequest = newRequest;
    }
    
}

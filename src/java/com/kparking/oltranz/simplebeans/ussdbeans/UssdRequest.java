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
public class UssdRequest {
    @XmlElement(name = "MSISDN")
    private String msisdn;
    @XmlElement(name = "SESSIONID")
    private String sessionId;
    @XmlElement(name = "NEWREQUEST")
    private int newRequest;
    @XmlElement(name = "AGENTID")
    private String agentId;
    @XmlElement(name = "INPUT")
    private String input;
    @XmlElement(name = "SPID")
    private String spId;
    @XmlElement(name = "FROMMULTIUSSD")
    private boolean fromMultiUssd;
    @XmlElement(name = "RESUME")
    private String resume;

    public UssdRequest() {
    }

    public UssdRequest(String msisdn, String sessionId, int newRequest, String agentId, String input, String spId, boolean fromMultiUssd, String resume) {
        this.msisdn = msisdn;
        this.sessionId = sessionId;
        this.newRequest = newRequest;
        this.agentId = agentId;
        this.input = input;
        this.spId = spId;
        this.fromMultiUssd = fromMultiUssd;
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
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

    public int getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(int newRequest) {
        this.newRequest = newRequest;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public boolean isFromMultiUssd() {
        return fromMultiUssd;
    }

    public void setFromMultiUssd(boolean fromMultiUssd) {
        this.fromMultiUssd = fromMultiUssd;
    }
    
}

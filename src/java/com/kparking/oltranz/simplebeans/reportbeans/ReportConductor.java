/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.reportbeans;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class ReportConductor {
    private String conductorId;
    private String conductorNames;
    private String conductorMsisdn;

    public ReportConductor() {
    }

    public ReportConductor(String conductorId, String conductorNames, String conductorMsisdn) {
        this.conductorId = conductorId;
        this.conductorNames = conductorNames;
        this.conductorMsisdn = conductorMsisdn;
    }

    public String getConductorMsisdn() {
        return conductorMsisdn;
    }

    public void setConductorMsisdn(String conductorMsisdn) {
        this.conductorMsisdn = conductorMsisdn;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getConductorNames() {
        return conductorNames;
    }

    public void setConductorNames(String conductorNames) {
        this.conductorNames = conductorNames;
    }
    
}

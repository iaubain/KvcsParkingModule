/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.currentcar;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class CurrentCarData {
    private String numberPlate;
    private String ticketType;
    private String conductorMsisdn;

    public CurrentCarData() {
    }

    public CurrentCarData(String numberPlate, String ticketType, String conductorMsisdn) {
        this.numberPlate = numberPlate;
        this.ticketType = ticketType;
        this.conductorMsisdn = conductorMsisdn;
    }

    public String getConductorMsisdn() {
        return conductorMsisdn;
    }

    public void setConductorMsisdn(String conductorMsisdn) {
        this.conductorMsisdn = conductorMsisdn;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
    
}

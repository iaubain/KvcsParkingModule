/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.verification;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class VerifyRequest {
    private String verifierMsisdn;
    private String parkingId;
    private String numberPlate;

    public VerifyRequest() {
    }

    public VerifyRequest(String verifierMsisdn, String parkingId, String numberPlate) {
        this.verifierMsisdn = verifierMsisdn;
        this.parkingId = parkingId;
        this.numberPlate = numberPlate;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getVerifierMsisdn() {
        return verifierMsisdn;
    }

    public void setVerifierMsisdn(String verifierMsisdn) {
        this.verifierMsisdn = verifierMsisdn;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
    
}

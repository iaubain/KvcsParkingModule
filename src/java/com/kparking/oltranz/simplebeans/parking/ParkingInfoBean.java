/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.parking;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class ParkingInfoBean {
    private String id;
    private String numberPlate;
    private String parkingInDate;
    private String parkingOutDate;
    private String msisdn;
    private String parkingSession;
    private String parkingId;
    private String createdOn;

    public ParkingInfoBean() {
    }

    public ParkingInfoBean(String id, String numberPlate, String parkingInDate, String parkingOutDate, String msisdn, String parkingSession, String parkingId, String createdOn) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.parkingInDate = parkingInDate;
        this.parkingOutDate = parkingOutDate;
        this.msisdn = msisdn;
        this.parkingSession = parkingSession;
        this.parkingId = parkingId;
        this.createdOn = createdOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getParkingInDate() {
        return parkingInDate;
    }

    public void setParkingInDate(String parkingInDate) {
        this.parkingInDate = parkingInDate;
    }

    public String getParkingOutDate() {
        return parkingOutDate;
    }

    public void setParkingOutDate(String parkingOutDate) {
        this.parkingOutDate = parkingOutDate;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getParkingSession() {
        return parkingSession;
    }

    public void setParkingSession(String parkingSession) {
        this.parkingSession = parkingSession;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
    
}

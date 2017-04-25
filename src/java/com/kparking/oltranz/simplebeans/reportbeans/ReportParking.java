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
public class ReportParking {
    private String parkingId;
    private String parkingName;
    private String numberOfCars;

    public ReportParking() {
    }

    public ReportParking(String parkingId, String parkingName, String numberOfCars) {
        this.parkingId = parkingId;
        this.parkingName = parkingName;
        this.numberOfCars = numberOfCars;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(String numberOfCars) {
        this.numberOfCars = numberOfCars;
    }
    
}

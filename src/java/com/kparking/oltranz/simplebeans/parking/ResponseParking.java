/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.parking;

import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.commonbeans.StatusBean;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ResponseParking {
    private ParkingBean parking;
    private List<ConductorBean> deployed;
    private StatusBean systemStatus;

    public ResponseParking() {
    }

    public ResponseParking(ParkingBean parking, List<ConductorBean> deployed, StatusBean systemStatus) {
        this.parking = parking;
        this.deployed = deployed;
        this.systemStatus = systemStatus;
    }

    public ParkingBean getParking() {
        return parking;
    }

    public void setParking(ParkingBean parking) {
        this.parking = parking;
    }

    public List<ConductorBean> getDeployed() {
        return deployed;
    }

    public void setDeployed(List<ConductorBean> deployed) {
        this.deployed = deployed;
    }

    public StatusBean getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(StatusBean systemStatus) {
        this.systemStatus = systemStatus;
    }
    
}

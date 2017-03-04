/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.deployment;

import com.kparking.oltranz.simplebeans.superusers.RequestCreateSuperUser;

/**
 *
 * @author Hp
 */
public class RequestDeploymentCreation {
    private RequestCreateSuperUser deployer;
    private String conductorId;
    private String parkingId;
    private String startDate;
    private String endDate;

    public RequestDeploymentCreation() {
    }

    public RequestDeploymentCreation(RequestCreateSuperUser deployer, String conductorId, String parkingId, String startDate, String endDate) {
        this.deployer = deployer;
        this.conductorId = conductorId;
        this.parkingId = parkingId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public RequestCreateSuperUser getDeployer() {
        return deployer;
    }

    public void setDeployer(RequestCreateSuperUser deployer) {
        this.deployer = deployer;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
}

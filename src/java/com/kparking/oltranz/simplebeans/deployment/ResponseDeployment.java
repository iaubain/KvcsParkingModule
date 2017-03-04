/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.deployment;

import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.commonbeans.StatusBean;
import com.kparking.oltranz.simplebeans.commonbeans.SuperUserBean;


/**
 *
 * @author Hp
 */
public class ResponseDeployment {
    private String deploymentId;
    private SuperUserBean deployer;
    private ConductorBean conductor;
    private ParkingBean parking;
    
    private StatusBean deploymentStatus;

    public ResponseDeployment() {
    }

    public ResponseDeployment(String deploymentId, SuperUserBean deployer, ConductorBean conductor, ParkingBean parking, StatusBean deploymentStatus) {
        this.deploymentId = deploymentId;
        this.deployer = deployer;
        this.conductor = conductor;
        this.parking = parking;
        this.deploymentStatus = deploymentStatus;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public ConductorBean getConductor() {
        return conductor;
    }

    public void setConductor(ConductorBean conductor) {
        this.conductor = conductor;
    }

    public ParkingBean getParking() {
        return parking;
    }

    public void setParking(ParkingBean parking) {
        this.parking = parking;
    }

    public StatusBean getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(StatusBean deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public SuperUserBean getDeployer() {
        return deployer;
    }

    public void setDeployer(SuperUserBean deployer) {
        this.deployer = deployer;
    }
    
}

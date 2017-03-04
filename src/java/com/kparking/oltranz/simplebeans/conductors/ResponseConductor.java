/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.conductors;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.commonbeans.StatusBean;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ResponseConductor {
    private ConductorBean conductor;
    private List<ParkingBean> deployedOn;
    private StatusBean systemStatus;
    

    public ResponseConductor() {
    }

    public ResponseConductor(ConductorBean conductor, StatusBean systemStatus, List<ParkingBean> deployedOn) {
        this.conductor = conductor;
        this.deployedOn = deployedOn;
        this.systemStatus = systemStatus;
    }

  
    public StatusBean getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(StatusBean systemStatus) {
        this.systemStatus = systemStatus;
    }

    public ConductorBean getConductor() {
        return conductor;
    }

    public void setConductor(ConductorBean conductor) {
        this.conductor = conductor;
    }

    public List<ParkingBean> getDeployedOn() {
        return deployedOn;
    }

    public void setDeployedOn(List<ParkingBean> deployedOn) {
        this.deployedOn = deployedOn;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.deployment;

/**
 *
 * @author Hp
 */
public class RequestUndeployment {
    private String deploymentId;

    public RequestUndeployment() {
    }

    public RequestUndeployment(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
}

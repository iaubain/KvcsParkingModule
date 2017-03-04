/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.schedule;

/**
 *
 * @author Hp
 */
public class CancelSceduledJob {
    private String jobId;

    public CancelSceduledJob() {
    }

    public CancelSceduledJob(String jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
}

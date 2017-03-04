/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.schedule;

import java.io.Serializable;

/**
 *
 * @author Hp
 */
public class JobTasks implements Serializable {
    private String jobId;
    private String objectDomain;
    private MyFrequency mFrequency;
    private String reportChanel;
    private int position;

    public JobTasks() {
    }

    public JobTasks(String jobId, String objectDomain, MyFrequency mFrequency, String reportChanel, int position) {
        this.jobId = jobId;
        this.objectDomain = objectDomain;
        this.mFrequency = mFrequency;
        this.reportChanel = reportChanel;
        this.position = position;
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

    /**
     * @return the objectDomain
     */
    public String getObjectDomain() {
        return objectDomain;
    }

    /**
     * @param objectDomain the objectDomain to set
     */
    public void setObjectDomain(String objectDomain) {
        this.objectDomain = objectDomain;
    }

    /**
     * @return the mFrequency
     */
    public MyFrequency getmFrequency() {
        return mFrequency;
    }

    /**
     * @param mFrequency the mFrequency to set
     */
    public void setmFrequency(MyFrequency mFrequency) {
        this.mFrequency = mFrequency;
    }

    /**
     * @return the reportChanel
     */
    public String getReportChanel() {
        return reportChanel;
    }

    /**
     * @param reportChanel the reportChanel to set
     */
    public void setReportChanel(String reportChanel) {
        this.reportChanel = reportChanel;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
    
}

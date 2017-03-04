/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.schedule;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Hp
 */
public class MyJob implements Serializable{
    private String jobId;
    private String timeFrame;
    private String objectDomain;
    private String reportChanel;
    private String expiration;
    private boolean isChained;
    private MyFrequency frequency;
    private List<JobTasks> mTasks;
    
    public MyJob() {
    }

    public MyJob(String jobId, String timeFrame, String objectDomain, String reportChanel, String expiration, boolean isChained, MyFrequency frequency, List<JobTasks> mTasks) {
        this.jobId = jobId;
        this.timeFrame = timeFrame;
        this.objectDomain = objectDomain;
        this.reportChanel = reportChanel;
        this.expiration = expiration;
        this.isChained = isChained;
        this.frequency = frequency;
        this.mTasks = mTasks;
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
     * @return the timeFrame
     */
    public String getTimeFrame() {
        return timeFrame;
    }

    /**
     * @param timeFrame the timeFrame to set
     */
    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
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
     * @return the expiration
     */
    public String getExpiration() {
        return expiration;
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the isChained
     */
    public boolean isIsChained() {
        return isChained;
    }

    /**
     * @param isChained the isChained to set
     */
    public void setIsChained(boolean isChained) {
        this.isChained = isChained;
    }

    /**
     * @return the mTasks
     */
    public List<JobTasks> getmTasks() {
        return mTasks;
    }

    /**
     * @param mTasks the mTasks to set
     */
    public void setmTasks(List<JobTasks> mTasks) {
        this.mTasks = mTasks;
    }

    /**
     * @return the frequency
     */
    public MyFrequency getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(MyFrequency frequency) {
        this.frequency = frequency;
    }
    
    
}

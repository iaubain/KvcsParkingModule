/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.apiclient.OpenExternal;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.simplebeans.schedule.CancelSceduledJob;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import static java.lang.System.out;
import javax.ejb.EJB;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class BackgroundSchedule implements Runnable{
    MyJob mJob;
    @EJB
            OpenExternal openExternal;
    @EJB
            ApInterface apInterface;
    
    boolean isCreateSched;
    
    public BackgroundSchedule(boolean isCreateSched, MyJob mJob, OpenExternal openExternal, ApInterface apInterface) {
        this.isCreateSched = isCreateSched;
        this.mJob = mJob;
        this.openExternal = openExternal;
        this.apInterface = apInterface;
    }
    
    @Override
    public void run() {
        if(isCreateSched)
            createSched();
        else
            cancelShed();
        
    }
    
    private void createSched(){
        String outPut = apInterface.createSchedule(mJob);
        out.print(AppDesc.APP_DESC+"BackgroundSchedule createSched external result: "+outPut != null?outPut:"null");
    }
    
    private void cancelShed(){
        CancelSceduledJob cancelSceduledJob = new CancelSceduledJob(mJob.getJobId());
        String outPut = apInterface.cancelSchedule(cancelSceduledJob);
        out.print(AppDesc.APP_DESC+"BackgroundSchedule cancelShed external result: "+outPut != null?outPut:"null");
    }
}

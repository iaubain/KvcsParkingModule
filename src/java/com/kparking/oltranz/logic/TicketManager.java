/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.apiclient.OpenExternal;
import com.kparking.oltranz.config.ApiConfig;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.config.SMSConfig;
import com.kparking.oltranz.config.StatusConfig;
import com.kparking.oltranz.entities.CallBack;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.CallBackFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.schedule.JobTasks;
import com.kparking.oltranz.simplebeans.schedule.MyFrequency;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import com.kparking.oltranz.utilities.IdGenerator;
import static java.lang.System.out;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class TicketManager {
    @EJB
            TicketFacade ticketFacade;
    @EJB
            IdGenerator idGenerator;
    @EJB
            CallBackFacade callBackFacade;
    @EJB
            ApInterface apInterface;
    @EJB
            OpenExternal openExternal;
    @EJB
            SmsSender smsSender;
    @EJB
            CustomerProvider customerProvider;
    public boolean genNewTicket(ConductorBean conductorBean, ParkingBean parkingBean, String numberPlate){
        try{
            Ticket ticket = new Ticket(idGenerator(),
                    numberPlate,
                    conductorBean.getTel(),
                    conductorBean.getConductorId(),
                    conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"",
                    parkingBean.getParkingId(),
                    parkingBean.getDescription(),
                    new Date(),
                    null);
            ticketFacade.create(ticket);
            ticketFacade.refreshTicket();
            
            CallBack callBack = callBackFacade.getTicketById(ticket.getTicketId());
            if(callBack == null){
                out.print(AppDesc.APP_DESC+"TicketManager genNewTicket No callback entry found and creating an entry for this ticket: "+ticket.getTicketId());
                
                callBack = new CallBack(ticket.getTicketId(),
                        ticket.getNumberPlate(),
                        0,
                        StatusConfig.CREATED,
                        StatusConfig.CREATED_DESC,
                        new Date());
                callBackFacade.create(callBack);
                callBackFacade.refreshCallBack();
            }
            // create schedule
//            MyFrequency myFrequency = new MyFrequency("minute", "180000");
            MyFrequency myFrequency = new MyFrequency("hour", "3600000");
List<JobTasks> mTasks = new ArrayList<>();
JobTasks jobTasks = new JobTasks(ticket.getTicketId(),
        getClass().getName(),
        myFrequency,
        ApiConfig.CALLBACK_URL,
        1);
mTasks.add(jobTasks);

//Get the current date
DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
Timestamp timestamp = new Timestamp(new Date().getTime());
Calendar calendar = Calendar.getInstance();
out.print(AppDesc.APP_DESC+"TicketManager genNewTicket Schedule for ticket "+ticket.getTicketId()+" is triged now: "+timestamp);

//add 1 day to the current date
calendar.setTimeInMillis(timestamp.getTime());
calendar.add(Calendar.HOUR, 24);
timestamp = new Timestamp(calendar.getTime().getTime());

out.print(AppDesc.APP_DESC+"TicketManager genNewTicket Schedule for ticket "+ticket.getTicketId()+" will expire on: "+timestamp);

MyJob mJob = new MyJob(ticket.getTicketId(),
        "daily",
        getClass().getName(),
        ApiConfig.CALLBACK_URL,
        dateFormat.format(timestamp),
        true,
        myFrequency,
        mTasks);

createSchedule(mJob);

Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, ticket, SMSConfig.CAR_IN_MSG+ticket.getNumberPlate()+" "+ticket.getParkingDesc()+" / "+timestamp));
smsThread.start();

return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager genNewTicket Failed to generate ticket due to: "+e.getMessage());
            return false;
        }
    }
    
    public boolean setTicketOutDate(boolean isCancelSched, String numberPlate){
        try{
            Ticket oTicket = ticketFacade.getCustormerLastTicket(numberPlate);
            if(oTicket == null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: no ticket found for : "+numberPlate);
                return false;
            }
            Date date = new Date();
            if(isCancelSched){
                CallBack callBack = callBackFacade.getCustormerLastCallback(numberPlate);
                if(callBack != null){
                    MyJob mJob = new MyJob();
                    mJob.setJobId(callBack.getTicketId());
                    cancelSchedule(mJob);
                    
                    callBack.setNumberOfCallBack(callBack.getNumberOfCallBack()+1);
                    callBackFacade.edit(callBack);
                    callBackFacade.refreshCallBack();
                }
                DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                
                Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, oTicket, SMSConfig.CAR_OUT_MSG+oTicket.getNumberPlate()+" "+oTicket.getParkingDesc()+"/"+dateFormat.format(date)));
                smsThread.start();
            }
            if(oTicket.getOutDate() != null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket ticket already timedout for: "+numberPlate);
                return true;
            }
            oTicket.setOutDate(date);
            ticketFacade.edit(oTicket);
            ticketFacade.refreshTicket();
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager setTicketOutDate Failed to generate ticket due to:" +e.getLocalizedMessage());
            return false;
        }
    }
    
    public boolean genAdditionalTicket(String oldTicket){
        try{
            Ticket checkTicket = ticketFacade.getTicketById(oldTicket);
            MyJob mJob = new MyJob();
            mJob.setJobId(oldTicket);
            if(checkTicket == null){
                //cancel the scheduler
                cancelSchedule(mJob);
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: null old ticket from ticket id: "+oldTicket);
                return false;
            }
            
            CallBack callBack = callBackFacade.getTicketById(oldTicket);
            if(callBack == null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket No callback entry found for this ticket: "+oldTicket);
                cancelSchedule(mJob);
                return true;
            }else{
                if(callBack.getNumberOfCallBack()<=0){
                    out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket first callBack for this ticket: "+oldTicket);
                    callBack.setNumberOfCallBack(callBack.getNumberOfCallBack()+1);
                    callBackFacade.edit(callBack);
                    callBackFacade.refreshCallBack();
                    return true;
                }
                callBack.setNumberOfCallBack(callBack.getNumberOfCallBack()+1);
                callBackFacade.edit(callBack);
                callBackFacade.refreshCallBack();
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket "+callBack.getNumberOfCallBack()+" callBack for this ticket: "+oldTicket);
            }
            
            Ticket oTicket = ticketFacade.getCustormerLastTicket(checkTicket.getNumberPlate());
            if(oTicket == null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: no ticket found for : "+oldTicket);
                return false;
            }
            
            if(oTicket.getOutDate() != null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Car "+oTicket.getNumberPlate()+" ticket "+oldTicket+" was taken already out of the parking: "+oTicket.getParkingId());
                return true;
            }
            Date date = new Date();
            oTicket.setOutDate(new Date());
            ticketFacade.edit(oTicket);
            long elapsedTime = elapsed(callBack.getCreatedOn(), date);
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, checkTicket, SMSConfig.CAR_ADDED_VALUE+" isaha "+elapsedTime+" (z)irashize, hour(s) elapsed, heure(s) ecoule "+oTicket.getNumberPlate()+" / "+oTicket.getParkingDesc()));
            smsThread.start();
            
            Ticket nTicket = new Ticket(idGenerator(),
                    oTicket.getNumberPlate(),
                    oTicket.getMsisdn(),
                    oTicket.getConductorId(),
                    oTicket.getConductorName(),
                    oTicket.getParkingId(),
                    oTicket.getParkingDesc(),
                    date,
                    null);
            ticketFacade.create(nTicket);
            ticketFacade.refreshTicket();
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: "+e.getMessage());
            return false;
        }
    }
    
    public static long elapsed(Date startDate, Date endDate) {
        long diffMills = endDate.getTime() - startDate.getTime();
        return (diffMills/1000)/60;
    }
    
    private void createSchedule(MyJob mJob){
        out.print(AppDesc.APP_DESC+"TicketManager createSchedule creating a schedule: "+mJob.getJobId());
        
        Thread thread = new Thread(new BackgroundSchedule(true, mJob, openExternal, apInterface));
        thread.start();
    }
    
    private void cancelSchedule(MyJob mJob){
        out.print(AppDesc.APP_DESC+"TicketManager cancelSchedule canceling a schedule: "+mJob.getJobId());
        
        Thread thread = new Thread(new BackgroundSchedule(false, mJob, openExternal, apInterface));
        thread.start();
    }
    private String idGenerator(){
        String genId = idGenerator.generate();
        try {
            boolean check = false;
            do{
                Ticket ticket = ticketFacade.getTicketById(genId);
                if(ticket == null){
                    return genId;
                }
                genId = idGenerator.generate();
            }while(!check);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            out.print(AppDesc.APP_DESC+"TicketManager idGenerator thread sleep failed due to: "+ ex.getLocalizedMessage());
            Logger.getLogger(UssdProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return genId;
        }
        return genId;
    }
}

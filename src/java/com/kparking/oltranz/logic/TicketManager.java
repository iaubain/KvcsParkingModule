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
import com.kparking.oltranz.entities.ParkingInfo;
import com.kparking.oltranz.entities.Progressive;
import com.kparking.oltranz.entities.TempTicket;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.CallBackFacade;
import com.kparking.oltranz.facades.ParkingInfoFacade;
import com.kparking.oltranz.facades.ProgressiveFacade;
import com.kparking.oltranz.facades.TempTicketFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.schedule.JobTasks;
import com.kparking.oltranz.simplebeans.schedule.MyFrequency;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.IdGenerator;
import com.kparking.oltranz.utilities.TicketFactory;
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
import net.manzi.frs.config.MenuMessage;
import net.manzi.frs.config.SessionDataStatus;
import net.manzi.frs.databeans.SessionTicketData;
import net.manzi.frs.databeans.userbeans.UserBean;
import net.manzi.frs.entities.SessionStatus;

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
    @EJB
            ProgressiveFacade progressiveFacade;
    @EJB
            TempTicketFacade tempTicketFacade;
    @EJB
            ParkingInfoFacade parkingInfoFacade;
    
    public void setCarBrandOnTicket(SessionStatus sessionStatus, String carBrand){
        Ticket ticket = ticketFacade.getSessionLastTicket(sessionStatus.getSessionId());
        if(ticket == null){
            out.print(AppDesc.APP_DESC+"No ticket found to set the brand "+carBrand+" for session: "+sessionStatus.getSessionId());
            return;
        }
        ticket.setCarBrand(carBrand);
        ticketFacade.edit(ticket);
        ticketFacade.refreshTicket();
    }
    
    public String genTicket(SessionStatus sessionStatus, UserBean userBean, SessionTicketData sessionTicketData){
        try {
            Date date = new Date();
            String conductorNames = userBean.getfName() != null ? userBean.getfName() : "" +userBean.getOtherNames() != null ? userBean.getOtherNames() : "";
            
            ParkingInfo parkingInfo = parkingInfoFacade.getCustomerLastParkiInfo(sessionTicketData.getnPlate());
            if(parkingInfo != null){
                if(parkingInfo.getOutDate() == null){
                    parkingInfo.setOutDate(date);
                    parkingInfoFacade.edit(parkingInfo);
                    parkingInfoFacade.refreshParkInfo();
                }
            }
            
            parkingInfo = new ParkingInfo(sessionTicketData.getnPlate(),
                    date,
                    null,
                    sessionStatus.getInitTel(),
                    sessionStatus.getSessionId(),
                    userBean.getAgentZoneId()+"",
                    date);
            parkingInfoFacade.create(parkingInfo);
            parkingInfoFacade.refreshParkInfo();
            
            Ticket ticket = ticketFacade.getCustormerLastTicket(sessionTicketData.getnPlate());
            if(ticket != null){
                
                DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket last ticket: "+ticket.getTicketId()+" startDate: "+dateFormat.parse(dateFormat.format(ticket.getInDate())));
                Date startDate = dateFormat.parse(dateFormat.format(ticket.getInDate()));
                
                Date endDate = dateFormat.parse(dateFormat.format(new Date()));
                out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket ticket endDate: "+endDate);
                
                long durationMinutes = DataFactory.printDifference(startDate, endDate);
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+ticket.getNumberPlate()+" was parked in: "+ticket.getParkingDesc()+" by conductor: "+ticket.getConductorName()+" and Id: "+ticket.getConductorId()+" elapsed minutes from last ticket: "+durationMinutes+" minutes.");
                
                if(ticket.getInDate().compareTo(date) < 0)
                    if(durationMinutes > 0 && durationMinutes < 60){
                        if(ticket.getTicketStatus().equals(SessionDataStatus.ONGOING_STATUS)){
                            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest an ongoing ticket for car with "+ticket.getNumberPlate()+" was parked in: "+ticket.getParkingDesc()+" by conductor: "+ticket.getConductorName()+" and Id: "+ticket.getConductorId()+" elapsed minutes from last ticket: "+durationMinutes+" minutes.");
                            return "Imodoka: "+ticket.getNumberPlate()+"^Iparitse: "+ticket.getParkingDesc()+"^Yashyizwemo: "+new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(ticket.getInDate());
                        }
                        Timestamp timestamp = new Timestamp(new Date().getTime());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp.getTime());
                        calendar.add(Calendar.MINUTE, (int) durationMinutes);
                        
                        long nextReminder = (60 - durationMinutes)*60*1000;
                        
                        TempTicket tempTicket = new TempTicket(sessionStatus.getSessionId(),
                                sessionStatus.getInitTel(),
                                userBean.getUserId()+"",
                                conductorNames,
                                sessionTicketData.getnPlate(),
                                sessionTicketData.getTicketType(),
                                date,
                                userBean.getAgentZoneId()+"",
                                userBean.getLocationName()+"-"+userBean.getAgentZoneName(),
                                calendar.getTime(),
                                SessionDataStatus.ONGOING_STATUS,
                                0);
                        tempTicketFacade.create(tempTicket);
                        tempTicketFacade.refreshTemp();
                        
                        reminder(sessionStatus, nextReminder, calendar.getTime());
                        return "SUCCESS";
                    }
            }
            Timestamp timestamp = new Timestamp(date.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            calendar.add(Calendar.HOUR, 1);
            
            ticket = new Ticket(idGenerator(),
                    sessionTicketData.getnPlate(),
                    "carBrand",
                    sessionStatus.getInitTel(),
                    userBean.getUserId()+"",
                    conductorNames,
                    userBean.getAgentZoneId()+"",
                    userBean.getLocationName()+"-"+userBean.getAgentZoneName(),
                    new Date(),
                    calendar.getTime(),
                    sessionTicketData.getTicketType(),
                    false,
                    sessionStatus.getSessionId(),
                    SessionDataStatus.ONGOING_STATUS);
            ticketFacade.create(ticket);
            ticketFacade.refreshTicket();
            
            Thread thread = new Thread(new BackGroundPublishTicket(apInterface,
                    ticketFacade,
                    new TicketFactory().genTicketToPublish(ticket)));
            thread.start();
            // create schedule
            ticketSchedule(sessionStatus.getSessionId());
            
            Thread.sleep(100);
            
            newTicketReminder(sessionStatus.getSessionId());
            
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, ticket, SMSConfig.CAR_IN_MSG+ticket.getNumberPlate()+" "+ticket.getParkingDesc()+" / "+timestamp, false));
            smsThread.start();
            
            return "SUCCESS";
            
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"TicketManager genNewTicket Failed to generate ticket due to: "+e.getMessage());
            return MenuMessage.TICKET_PROBLEM;
        }
    }
    public boolean genNewTicket(ConductorBean conductorBean, ParkingBean parkingBean, String initmsisdn){
        try{
            Progressive progressive = progressiveFacade.getConductorLastProgressive(initmsisdn);
            if(progressive == null){
                out.print(AppDesc.APP_DESC+"TicketManager genNewTicket no progressive found for "+initmsisdn);
                return false;
            }
            if(progressive.getNumberPlate() == null || progressive.getTicketType()== null){
                out.print(AppDesc.APP_DESC+"TicketManager genNewTicket null value in progressive number plate or ticketNumber for: "+initmsisdn);
                return false;
            }
            Ticket ticket = new Ticket(idGenerator(),
                    progressive.getNumberPlate(),
                    "",
                    conductorBean.getTel(),
                    conductorBean.getConductorId(),
                    conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"",
                    parkingBean.getParkingId(),
                    parkingBean.getDescription(),
                    new Date(),
                    null,
                    progressive.getTicketType(),
                    false,
                    "sessionId",
                    SessionDataStatus.ONGOING_STATUS);
            ticketFacade.create(ticket);
            ticketFacade.refreshTicket();
            
            progressive.setIsFinished(true);
            progressiveFacade.edit(progressive);
            progressiveFacade.refreshProgressive();
            
            CallBack callBack = callBackFacade.getTicketById(ticket.getTicketId());
            if(callBack == null){
                out.print(AppDesc.APP_DESC+"TicketManager genNewTicket No callback entry found and creating an entry for this ticket: "+ticket.getTicketId());
                Date date= new Date();
                callBack = new CallBack(ticket.getTicketId(),
                        ticket.getNumberPlate(),
                        0,
                        StatusConfig.CREATED,
                        StatusConfig.CREATED_DESC,
                        date,
                        date);
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
            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
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
            
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, ticket, SMSConfig.CAR_IN_MSG+ticket.getNumberPlate()+" "+ticket.getParkingDesc()+" / "+timestamp, false));
            smsThread.start();
            
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager genNewTicket Failed to generate ticket due to: "+e.getMessage());
            return false;
        }
    }
    
    public boolean setTicketOutDate(boolean isCancelSched, String numberPlate){
        try{
            
            ParkingInfo parkingInfo = parkingInfoFacade.getCustomerLastParkiInfo(numberPlate);
            if(parkingInfo != null){
                if(parkingInfo.getOutDate() == null){
                    parkingInfo.setOutDate(new Date());
                    parkingInfoFacade.edit(parkingInfo);
                    parkingInfoFacade.refreshParkInfo();
                }
            }
            Ticket oTicket = ticketFacade.getCustormerLastTicket(numberPlate);
            if(oTicket == null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: no ticket found for : "+numberPlate);
                return false;
            }
            Date date = new Date();
            
            TempTicket tempTicket = tempTicketFacade.getLastCustomerTempTicket(numberPlate);
            if(tempTicket != null){
                tempTicket.setTicketStatus(SessionDataStatus.CANCELLED_STATUS);
                tempTicketFacade.edit(tempTicket);
                tempTicketFacade.refreshTemp();
            }
            
            if(!oTicket.getTicketType().equals(SessionDataStatus.COMPLETED_STATUS)){
                oTicket.setTicketStatus(SessionDataStatus.COMPLETED_STATUS);
                oTicket.setOutDate(date);
                ticketFacade.edit(oTicket);
                ticketFacade.refreshTicket();
            }
            
            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, oTicket, SMSConfig.CAR_OUT_MSG+oTicket.getNumberPlate()+" "+oTicket.getParkingDesc()+"/"+dateFormat.format(date), false));
            smsThread.start();
            
            MyJob mJob = new MyJob();
            mJob.setJobId(oTicket.getSessionId());
            cancelSchedule(mJob);
            
            mJob = new MyJob();
            mJob.setJobId(oTicket.getMsisdn()+oTicket.getSessionId());
            cancelSchedule(mJob);
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager setTicketOutDate Failed to generate ticket due to:" +e.getLocalizedMessage());
            return false;
        }
    }
    
    public boolean genAdditionalTicket(String ticketSessionId){
        try{
            
            Ticket oTicket = ticketFacade.getSessionLastTicket(ticketSessionId);
            if(oTicket == null){
                //cancel the schedule
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to get session last ticket: "+ticketSessionId);
                return false;
            }
            
            Ticket firstTicket = ticketFacade.getSessionFirstTicket(ticketSessionId);
            if(firstTicket == null){
                out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to get session first ticket: "+ticketSessionId);
                return false;
            }
            
            Date date = new Date();
            oTicket.setTicketStatus(SessionDataStatus.COMPLETED_STATUS);
            ticketFacade.edit(oTicket);
            
            long elapsedTime = elapsed(firstTicket.getInDate(), date);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 1);
            
            Ticket nTicket = new Ticket(idGenerator(),
                    oTicket.getNumberPlate(),
                    oTicket.getCarBrand() != null?oTicket.getCarBrand():"carBrand",
                    oTicket.getMsisdn(),
                    oTicket.getConductorId(),
                    oTicket.getConductorName(),
                    oTicket.getParkingId(),
                    oTicket.getParkingDesc(),
                    date,
                    calendar.getTime(),
                    oTicket.getTicketType(),
                    false,
                    oTicket.getSessionId(),
                    SessionDataStatus.ONGOING_STATUS);
            ticketFacade.create(nTicket);
            ticketFacade.refreshTicket();
            
            ParkingInfo parkingInfo = parkingInfoFacade.getCustomerLastParkiInfo(nTicket.getNumberPlate());
            if(parkingInfo == null){
                parkingInfo = new ParkingInfo(nTicket.getSessionId(),
                        date,
                        null,
                        nTicket.getMsisdn(),
                        nTicket.getSessionId(),
                        nTicket.getParkingId(),
                        date);
                parkingInfoFacade.create(parkingInfo);
                parkingInfoFacade.refreshParkInfo();
            }
            
            Thread smsThread = new Thread(new BackgroundSMS(smsSender, customerProvider, oTicket, SMSConfig.CAR_ADDED_VALUE+" isaha "+elapsedTime+" (z)irashize, hour(s) elapsed, heure(s) ecoule "+oTicket.getNumberPlate()+" / "+oTicket.getParkingDesc(), false));
            smsThread.start();
            
            if(!oTicket.isRecorded()){
                Thread thread = new Thread(new BackGroundPublishTicket(apInterface,
                        ticketFacade,
                        new TicketFactory().genTicketToPublish(oTicket)));
                thread.start();
            }
            Thread thread = new Thread(new BackGroundPublishTicket(apInterface,
                    ticketFacade,
                    new TicketFactory().genTicketToPublish(nTicket)));
            thread.start();
            
            newTicketReminder(oTicket.getSessionId());
            return true;
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"TicketManager genAdditionalTicket Failed to generate ticket due to: "+e.getMessage());
            return false;
        }
    }
    
    public static long elapsed(Date startDate, Date endDate) {
        long diffMills = endDate.getTime() - startDate.getTime();
        return (diffMills/1000)/3600;
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
    
    private void reminder(SessionStatus sessionStatus, long frequency, Date expireIn){
        String extension = idGenerator.generate();
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(expireIn.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MINUTE, 3);
        timestamp = new Timestamp(calendar.getTime().getTime());
        
        out.print(AppDesc.APP_DESC+"UssdProcessor reminder Schedule for progressive initiator: "+sessionStatus.getInitTel()+" will expire on: "+timestamp);
        MyFrequency myFrequency = new MyFrequency("minute", ""+frequency);
        List<JobTasks> mTasks = new ArrayList<>();
        JobTasks jobTasks = new JobTasks(extension+"^"+sessionStatus.getSessionId(),
                getClass().getName(),
                myFrequency,
                ApiConfig.TEMPTICKET_CALLBACK_URL,
                1);
        mTasks.add(jobTasks);
        
        MyJob mJob = new MyJob(extension+"^"+sessionStatus.getSessionId(),
                "daily",
                getClass().getName(),
                ApiConfig.TEMPTICKET_CALLBACK_URL,
                dateFormat.format(timestamp),
                true,
                myFrequency,
                mTasks);
        createSchedule(mJob);
    }
    
    private void ticketSchedule(String sessionId){
        out.print(AppDesc.APP_DESC+"TicketManager ticketSchedule generating ticket reminder for sessionId: "+ sessionId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 4);
        MyFrequency myFrequency = new MyFrequency("minute", "3600000");
        List<JobTasks> mTasks = new ArrayList<>();
        JobTasks jobTasks = new JobTasks(sessionId,
                getClass().getName(),
                myFrequency,
                ApiConfig.CALLBACK_URL,
                1);
        mTasks.add(jobTasks);
        
        MyJob mJob = new MyJob(sessionId,
                "daily",
                getClass().getName(),
                ApiConfig.CALLBACK_URL,
                new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(calendar.getTime()),
                true,
                myFrequency,
                mTasks);
        createSchedule(mJob);
        out.print(AppDesc.APP_DESC+"TicketManager newTicketReminder generating ticket one time reminder for sessionId: "+ sessionId+" Expires in: "+new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(calendar.getTime()));
    }
    
    private void newTicketReminder(String sessionId){
        out.print(AppDesc.APP_DESC+"TicketManager newTicketReminder generating ticket one time reminder for sessionId: "+ sessionId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        String extension = idGenerator.generate();
        MyFrequency myFrequency = new MyFrequency("minute", "3300000");
        List<JobTasks> mTasks = new ArrayList<>();
        JobTasks jobTasks = new JobTasks(extension+"^"+sessionId,
                getClass().getName(),
                myFrequency,
                ApiConfig.TICKET_NOTIFICATION_URL,
                1);
        mTasks.add(jobTasks);
        
        MyJob mJob = new MyJob(extension+"^"+sessionId,
                "daily",
                getClass().getName(),
                ApiConfig.TICKET_NOTIFICATION_URL,
                new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(calendar.getTime()),
                true,
                myFrequency,
                mTasks);
        createSchedule(mJob);
        out.print(AppDesc.APP_DESC+"TicketManager newTicketReminder generating ticket one time reminder for sessionId: "+ sessionId+" Expires in: "+new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(calendar.getTime()));
    }
}

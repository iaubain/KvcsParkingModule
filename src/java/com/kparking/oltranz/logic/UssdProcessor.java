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
import com.kparking.oltranz.entities.CallBack;
import com.kparking.oltranz.entities.Car;
import com.kparking.oltranz.entities.Progressive;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.CallBackFacade;
import com.kparking.oltranz.facades.CarFacade;
import com.kparking.oltranz.facades.ProgressiveFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.conductors.ResponseConductor;
import com.kparking.oltranz.simplebeans.deployment.ResponseDeployment;
import com.kparking.oltranz.simplebeans.schedule.JobTasks;
import com.kparking.oltranz.simplebeans.schedule.MyFrequency;
import com.kparking.oltranz.simplebeans.schedule.MyJob;
import com.kparking.oltranz.simplebeans.ussdbeans.Menu;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdRequest;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdResponse;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.IdGenerator;
import com.kparking.oltranz.utilities.ReturnConfig;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class UssdProcessor {
    @EJB
            ApInterface apInterface;
    @EJB
            IdGenerator idGenerator;
    @EJB
            TicketFacade ticketFacade;
    @EJB
            TicketManager ticketManager;
    @EJB
            ScheduleManager scheduleManager;
    @EJB
            SmsSender smsSender;
    @EJB
            CustomerProvider customerProvider;
    @EJB
            CallBackFacade callBackFacade;
    @EJB
            CarFacade carFacade;
    @EJB
            ProgressiveFacade progressiveFacade;
    @EJB
            OpenExternal openExternal;
    private String nPlate;
    public Response receiveCarInRequest(UssdRequest request){
        try{
            
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a non new request received from"+request.getMsisdn());
                ResponseConductor responseConductor = getConductor(request.getMsisdn());
                if(!validateEntry(responseConductor)){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no conductor found for: "+request.getMsisdn());
                    return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka.^Mwongere mukanya."));
                }
                Progressive progressive = progressiveFacade.getConductorLastUnfinishedProgressive(request.getMsisdn(), false);
                if(progressive != null){
                    ConductorBean conductorBean = responseConductor.getConductor();
                    String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
                    
                    return ReturnConfig.isSuccess(continueInput(conductorNames+"^Andika neza ubwoko bwatike.^Imodoka: "+progressive.getNumberPlate()+"^1. 100Rwf^2. 200Rwf^3. 400Rwf^4. 1000Rwf",request));
                }
                
                return ReturnConfig.isSuccess(carInOutMenu("Guparika^",request));
            }
            
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            ResponseConductor responseConductor = getConductor(request.getMsisdn());
            if(!validateEntry(responseConductor)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka.^Mwongere mukanya."));
            }
            ConductorBean conductorBean = responseConductor.getConductor();
            String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
            
            Progressive progressive = progressiveFacade.getConductorLastUnfinishedProgressive(request.getMsisdn(), false);
            if(progressive != null){
                this.nPlate = progressive.getNumberPlate();
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest progressive found for: "+request.getMsisdn());
                switch(request.getInput()){
                    case "1":
                        progressive.setTicketType("100");
                        break;
                    case "2":
                        progressive.setTicketType("200");
                        break;
                    case "3":
                        progressive.setTicketType("400");
                        break;
                    case "4":
                        progressive.setTicketType("1000");
                        break;
                    default:
                        return ReturnConfig.isSuccess(continueInput(conductorNames+"^Andika neza ubwoko bwatike.^Imodoka: "+progressive.getNumberPlate()+"^1. 100Rwf^2. 200Rwf^3. 400Rwf^4. 1000Rwf",request));
                }
            }else{
                Date date = new Date();
                Ticket ticket = ticketFacade.getConductorLastTicket(request.getMsisdn());
                if(ticket == null){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest ticket not available: "+request.getMsisdn() +" of: "+conductorBean.getFirstName());
                    return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Ibyomushaka ntibishobotse."));
                }
                progressive = progressiveFacade.getCustomerLastProgressive(request.getMsisdn(), ticket.getNumberPlate());
                if(progressive != null){
                    if((ticket.getCarBrand().isEmpty() || ticket.getCarBrand().equals("")) && progressive.isIsFinished()){
                        ticket.setCarBrand(request.getInput());
                        ticketFacade.edit(ticket);
                        ticketFacade.refreshTicket();
                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Succeeded to set a brand to ticket for: "+ticket.getNumberPlate());
                        return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Imodoka. "+ticket.getNumberPlate()+"^Ubwoko: "+request.getInput()+"^Iparitse: "+ticket.getParkingDesc()));
                    }
                    
//                    else{
//                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest failed to set car brand ticket for: "+ticket.getNumberPlate());
//                        return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Imodoka. "+ticket.getNumberPlate()+"^Ubwoko: "+request.getInput()+"^Iparitse: "+ticket.getParkingDesc()+"^Ibyo mwanditse ntibishyizwemo."));
//                    }


//                    if(! DataFactory.numberPlateValidator(request.getInput())){
//                        if(progressive.isIsFinished()){
//                            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput()+" and this is a brand.");
//                            if(ticket.getCarBrand().isEmpty() || ticket.getCarBrand().equals("")){
//                                ticket.setCarBrand(request.getInput());
//                                ticketFacade.edit(ticket);
//                                ticketFacade.refreshTicket();
//                            }
//                            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Succeeded to set a brand to ticket for: "+ticket.getNumberPlate());
//                            return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Imodoka. "+ticket.getNumberPlate()+"^Ubwoko: "+request.getInput()+"^Iparitse: "+ticket.getParkingDesc()));
//                        }
//                    }else{
//                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Succeeded to generate ticket for: "+ticket.getNumberPlate());
//                        return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Imodoka. "+ticket.getNumberPlate()+"^Ubwoko: "+request.getInput()+"^Iparitse: "+ticket.getParkingDesc()));
//                    }
                }
                if(! DataFactory.numberPlateValidator(request.getInput())){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                    return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Plaque: "+request.getInput()+" Reba niba yanditse neza."));
                }
                
                this.nPlate = request.getInput();
                
                progressive = new Progressive(idGenerator(),
                        request.getMsisdn(),
                        request.getInput(),
                        "0",
                        date,
                        date,
                        false);
                progressiveFacade.create(progressive);
                progressiveFacade.refreshProgressive();
                //Get the current date
                DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                Timestamp timestamp = new Timestamp(new Date().getTime());
                Calendar calendar = Calendar.getInstance();
                
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Schedule for progressive initiator: "+progressive.getInitMsisdn()+", number plate"+progressive.getNumberPlate()+" and ID: "+progressive.getId()+" . Trigered: "+timestamp);
                
                //add 3 minutes to the current date
                calendar.setTimeInMillis(timestamp.getTime());
                calendar.add(Calendar.MINUTE, 21);
                timestamp = new Timestamp(calendar.getTime().getTime());
                
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Schedule for progressive initiator: "+progressive.getInitMsisdn()+", number plate"+progressive.getNumberPlate()+" and ID: "+progressive.getId()+" will expire on: "+timestamp);
                MyFrequency myFrequency = new MyFrequency("minute", "180000");
                List<JobTasks> mTasks = new ArrayList<>();
                JobTasks jobTasks = new JobTasks(progressive.getProgressId(),
                        getClass().getName(),
                        myFrequency,
                        ApiConfig.PROGRESS_CALLBACK_URL,
                        1);
                mTasks.add(jobTasks);
                
                MyJob mJob = new MyJob(progressive.getProgressId(),
                        "daily",
                        getClass().getName(),
                        ApiConfig.PROGRESS_CALLBACK_URL,
                        dateFormat.format(timestamp),
                        true,
                        myFrequency,
                        mTasks);
                createSchedule(mJob);
                
                return ReturnConfig.isSuccess(continueInput(conductorNames+"^Andika ubwoko bwatike.^Imodoka: "+request.getInput()+"^1. 100Rwf^2. 200Rwf^3. 400Rwf^4. 1000Rwf",request));
            }
            
            Thread.sleep(10);
            
            List<ResponseDeployment> mDeploymetList =(List<ResponseDeployment>)(Object)DataFactory.stringToObjectList(ResponseDeployment.class, apInterface.getConductorDep(responseConductor.getConductor().getConductorId()));
            if(mDeploymetList == null){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no available parking found for: "+request.getMsisdn() +" of: "+conductorBean.getFirstName());
                return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Nta parikingi mwashyizwemo."));
            }
            ResponseDeployment mDeployment = mDeploymetList.get(0);
            ParkingBean parkingBean = mDeployment.getParking();
            if(parkingBean == null){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no available parking found for: "+request.getMsisdn() +" of: "+conductorBean.getFirstName());
                return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Nta parikingi mwashyizwemo."));
            }
            
            //            List<Ticket> todayTickets = ticketFacade.getTicketsByNumberPlate(request.getInput());
            //            if(todayTickets != null)
            //                for(Ticket ticket : todayTickets){
            //                    if(ticket.getOutDate() == null){
            //                        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+ticket.getNumberPlate()+" already parked in: "+ticket.getParkingDesc()+" by conductor: "+ticket.getConductorName()+" and Id: "+ticket.getConductorId());
            //                        return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^ Iyi Imodoka. "+ticket.getNumberPlate()+" iragaragara nk'igiparitse ^ Parikingi: "+ticket.getParkingDesc()+"^Yashyizwemo na: "+ticket.getConductorName()));
            //                    }
            //                }
            
            Ticket checkLastTicket = ticketFacade.getCustormerLastTicket(nPlate);
            
            if(checkLastTicket != null){
                if(checkLastTicket.getOutDate() != null){
                    DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                    out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket last ticket: "+checkLastTicket.getTicketId()+" startDate: "+dateFormat.parse(dateFormat.format(checkLastTicket.getInDate())));
                    Date startDate = dateFormat.parse(dateFormat.format(checkLastTicket.getInDate()));
                    
                    Date date = new Date();
                    
                    Date endDate = dateFormat.parse(dateFormat.format(new Date()));
                    out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket ticket endDate: "+endDate);
                    
                    long durationMinutes = DataFactory.printDifference(startDate, endDate);
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+" elapsed minutes from last ticket: "+durationMinutes+" minutes.");
                    if(checkLastTicket.getInDate().compareTo(date) < 0)
                        if(durationMinutes > 0 && durationMinutes < 60){
                            
                            progressive.setIsFinished(true);
                            progressiveFacade.edit(progressive);
                            
                            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+" has remained time before an hour get elapsed: "+durationMinutes+" minutes");
                            return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^ Iyi Imodoka. "+checkLastTicket.getNumberPlate()+" iracyafite iminota "+(60-durationMinutes)+" kugirango isaha ishire.^ Yinjiriye muri Parikingi: "+checkLastTicket.getParkingDesc()+"^Yashyizwemo na: "+checkLastTicket.getConductorName()));
                        }
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+"  elapsed minutes from last ticket: "+durationMinutes+" minutes");
                    
                }else{
                    progressive.setIsFinished(true);
                    progressiveFacade.edit(progressive);
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" already parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId());
                    return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^ Iyi Imodoka. "+checkLastTicket.getNumberPlate()+" iragaragara nk'igiparitse ^ Parikingi: "+checkLastTicket.getParkingDesc()+"^Yashyizwemo na: "+checkLastTicket.getConductorName()));
                }
            }
            if(!ticketManager.genNewTicket(responseConductor.getConductor(), parkingBean, request.getMsisdn())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest failed to generate ticket for: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+" Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
            }
            
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Succeeded to generate ticket for: "+nPlate);
            return ReturnConfig.isSuccess(continueInput(conductorNames+" ^Imodoka. "+nPlate+"^Ishyizwe muri parikingi.^Andika ubwoko", request));
            
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request,"KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    
    
    public Response receiveCarOut(UssdRequest request){
        try{
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut a new request received from"+request.getMsisdn());
                ResponseConductor responseConductor = getConductor(request.getMsisdn());
                if(!validateEntry(responseConductor)){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no conductor found for: "+request.getMsisdn());
                    return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka.^Mwongere mukanya."));
                }
                return ReturnConfig.isSuccess(carInOutMenu("Kuvana imodoka muri parikingi^",request));
            }
            
            ResponseConductor responseConductor = getConductor(request.getMsisdn());
            if(!validateEntry(responseConductor)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka.^Mwongere mukanya."));
            }
            
            ConductorBean conductorBean = responseConductor.getConductor();
            String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
            
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            if(! DataFactory.numberPlateValidator(request.getInput())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Plaque: "+request.getInput()+" Reba niba yanditse neza."));
            }
            
            Ticket ticket = ticketFacade.getCustormerLastTicket(request.getInput());
            if(ticket == null){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with no ticket: by conductor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Ntago ibonetse."));
            }
            if(ticket.getOutDate() != null){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with ticket: "+ticket.getTicketId()+" by conductor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Yamaze gukurwa muri parikingi.^Isaha yakuwemo: "+ticket.getOutDate()));
            }
            if(!ticket.getConductorId().equals(conductorBean.getConductorId())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with ticket: "+ticket.getTicketId()+" request by conductor: "+request.getMsisdn()+" Initiated by: "+ticket.getConductorId());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Imodoka. "+request.getInput()+"^Yashyizwe muri parking na: "+ticket.getConductorName()+"^Saa: "+ticket.getOutDate()+"^Kuri: "+ticket.getParkingDesc()));
            }
            if(!ticketManager.setTicketOutDate(true, request.getInput())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: numberPlate "+request.getInput()+" not found");
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
            }
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut Succeeded to take out car"+request.getInput());
            return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Gukura imodoka. "+request.getInput()+" muri parikingi.^Birakozwe."));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    public Response checkCar(UssdRequest request){
        try{
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a new request received from"+request.getMsisdn());
                
                return ReturnConfig.isSuccess(carInOutMenu("Kureba Imodoka.^Check car.^Vérifier l'auto.^Shyiramo puraki, Fill number plate, Entre la plaque.",request));
            }
            
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            if(! DataFactory.numberPlateValidator(request.getInput())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza."));
            }
            Ticket ticket = ticketFacade.getCustormerLastTicket(request.getInput());
            if(ticket == null){
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" with no ticket: by conductor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, request.getInput()+"^Ntibonetse, Not available, N'est pas trouvé"));
            }
            
            String message;
            String lastTime = null;
            CallBack callBack = callBackFacade.getCustormerLastCallback(request.getInput());
            if(callBack != null){
                DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                lastTime = dateFormat.format(callBack.getCreatedOn());
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime+" Requestor: "+request.getMsisdn());
                
            }
            lastTime = lastTime != null?lastTime:"time not available";
            if(ticket.getOutDate() == null){
                message = ticket.getNumberPlate()+" Iri muri parikingi, Is parked, est garé^"+ticket.getParkingDesc()+" / Yagiyemo, In time, Entre "+lastTime;
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime != null?lastTime:""+" Requestor: "+request.getMsisdn()+" message: "+message);
            }else{
                message = ticket.getNumberPlate()+" Bwanyuma yari, Lastly seen, Dernierement vu^"+ticket.getParkingDesc()+" / "+lastTime;
                out.print(AppDesc.APP_DESC+"UssdProcessor checkCar a car with "+request.getInput()+" last date: "+lastTime+" Requestor: "+request.getMsisdn()+" message: "+message);
            }
            
            out.print(AppDesc.APP_DESC+"UssdProcessor checkCar Succeeded to check car: "+request.getInput()+" by requestor: "+request.getMsisdn());
            return ReturnConfig.isSuccess(successGen(request, message));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    public Response signupCar(UssdRequest request){
        try{
            if(request.getNewRequest() == 1){
                //check the session for possible list of parking in there
                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar a new request received from"+request.getMsisdn());
                
                return ReturnConfig.isSuccess(carInOutMenu("Andikisha imodoka.^Signup Car.^Enregistre l'auto.^Shyiramo puraki, Fill number plate, Entre la plaque.",request));
            }
            ResponseConductor responseConductor = getConductor(request.getMsisdn());
            Car car;
            if(!validateEntry(responseConductor)){
                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar no conductor found for: "+request.getMsisdn());
                request.setInput(request.getInput().replace(" ", "").toUpperCase());
                if(! DataFactory.numberPlateValidator(request.getInput())){
                    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                    return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
                }
                car = new Car(request.getInput().toUpperCase(), request.getMsisdn(), "Owner", new Date());
                carFacade.create(car);
                carFacade.refreshCar();
                return ReturnConfig.isSuccess(successGen(request, "Birakozwe, Well Done, Bien fait!"));
            }
            ConductorBean conductorBean = responseConductor.getConductor();
            String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
            
            String[] data = DataFactory.splitString(request.getInput(), " ");
            String nPlate = data[0].toUpperCase();
            String tel = data[1];
            if(! DataFactory.numberPlateValidator(nPlate)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Plaque: "+request.getInput()+" Reba niba yanditse neza, Typo Error."));
            }
            String tempTel = tel;
            tel = DataFactory.phoneFormat(tel);
            if(tel.isEmpty()){
                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar action failed due to: Empty tel requestor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^Reba niba"+tempTel != null? tempTel:""+" numero yumukiriya wayanditse neza."));
                
            }
            car = new Car(nPlate,
                    tel,
                    "Conductor:ID"+responseConductor.getConductor().getConductorId()+" Names:"+conductorNames,
                    new Date());
            carFacade.create(car);
            carFacade.refreshCar();
            
            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar Succeeded to signup car: "+request.getInput()+" by requestor: "+request.getMsisdn());
            return ReturnConfig.isSuccess(successGen(request, conductorNames+"^Kwandika imodoka birakozwe"));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar action failed due to: "+e.getMessage());
            return ReturnConfig.isSuccess(faillureGen(request, "KVCS PARKING SYSTEM^Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
        }
    }
    
    private boolean validateEntry(ResponseConductor responseConductor){
        if(responseConductor == null){
            out.print(AppDesc.APP_DESC+"UssdProcessor validateEntry no conductor found.");
            return false;
        }
        
        if(responseConductor.getConductor() == null){
            out.print(AppDesc.APP_DESC+"UssdProcessor validateEntry no conductor found.");
            return false;
        }
        out.print(AppDesc.APP_DESC+"UssdProcessor validateEntry conductor found."+ responseConductor.getConductor().getConductorId());
        return true;
    }
    private ResponseConductor getConductor(String msisdn){
        return apInterface.getConductor(msisdn);
    }
    private String faillureGen(UssdRequest request, String message){
        List<String> menus = new ArrayList<>();
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "B", message, 0, menu);
        String outPut = DataFactory.objectToXmlString(response);
        return outPut;
    }
    
    private String successGen(UssdRequest request, String message){
        List<String> menus = new ArrayList<>();
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "B", message, 0, menu);
        String outPut = DataFactory.objectToXmlString(response);
        return outPut;
    }
    
    private String continueInput(String message, UssdRequest request){
        List<String> menus = new ArrayList<>();
        String outPut;
        
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "C", message, 0, menu);
        outPut = DataFactory.objectToXmlString(response);
        
        return outPut;
    }
    
    private String carInOutMenu(String message, UssdRequest request){
        List<String> menus = new ArrayList<>();
        String outPut;
        
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "C", message+"Andika purake yimodoka.", 0, menu);
        outPut = DataFactory.objectToXmlString(response);
        
        return outPut;
    }
    
    private void createSchedule(MyJob mJob){
        out.print(AppDesc.APP_DESC+"UssdProcessor createSchedule creating a schedule: "+mJob.getJobId());
        
        Thread thread = new Thread(new BackgroundSchedule(true, mJob, openExternal, apInterface));
        thread.start();
    }
    
    private String idGenerator(){
        String genId = idGenerator.generate();
        try {
            boolean check = false;
            do{
                Progressive progressive = progressiveFacade.getProgressById(genId);
                if(progressive == null){
                    return genId;
                }
                genId = idGenerator.generate();
            }while(!check);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            out.print(AppDesc.APP_DESC+"UssdProcessor idGenerator thread sleep failed due to: "+ ex.getLocalizedMessage());
            Logger.getLogger(UssdProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return genId;
        }
        return genId;
    }
}

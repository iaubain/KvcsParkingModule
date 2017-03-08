/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.apiclient.ApInterface;
import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.CallBack;
import com.kparking.oltranz.entities.Car;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.CallBackFacade;
import com.kparking.oltranz.facades.CarFacade;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;
import com.kparking.oltranz.simplebeans.commonbeans.ParkingBean;
import com.kparking.oltranz.simplebeans.conductors.ResponseConductor;
import com.kparking.oltranz.simplebeans.deployment.ResponseDeployment;
import com.kparking.oltranz.simplebeans.ussdbeans.Menu;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdRequest;
import com.kparking.oltranz.simplebeans.ussdbeans.UssdResponse;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.IdGenerator;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
                return ReturnConfig.isSuccess(carInOutMenu("Guparika^",request));
            }
            request.setInput(request.getInput().replace(" ", "").toUpperCase());
            ResponseConductor responseConductor = getConductor(request.getMsisdn());
            if(!validateEntry(responseConductor)){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest no conductor found for: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, "Ibyo mushaka ntibibashije kuboneka.^Mwongere mukanya."));
            }
            
            Thread.sleep(10);
            
            ConductorBean conductorBean = responseConductor.getConductor();
            String conductorNames = conductorBean.getFirstName() != null?conductorBean.getFirstName():"" +conductorBean.getMiddleName()!= null?conductorBean.getMiddleName():"" + conductorBean.getLastName()!= null?conductorBean.getLastName():"";
            
            if(! DataFactory.numberPlateValidator(request.getInput())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest from: "+request.getMsisdn() +" and Input: "+request.getInput());
                return ReturnConfig.isSuccess(faillureGen(request, "Ikaze, "+conductorNames+"^Plaque: "+request.getInput()+" Reba niba yanditse neza."));
            }
            
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

Ticket checkLastTicket = ticketFacade.getCustormerLastTicket(request.getInput());

if(checkLastTicket != null){
    if(checkLastTicket.getOutDate() != null){
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket last ticket: "+checkLastTicket.getTicketId()+" startDate: "+dateFormat.parse(dateFormat.format(checkLastTicket.getInDate())));
        Date startDate = dateFormat.parse(dateFormat.format(checkLastTicket.getInDate()));
        
        
        Date endDate = dateFormat.parse(dateFormat.format(new Date()));
        out.print(AppDesc.APP_DESC+" UssdProcessor genNewTicket ticket endDate: "+endDate);
        
        long durationMinutes = DataFactory.printDifference(startDate, endDate);
        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+" elapsed minutes from last ticket: "+durationMinutes+" minutes.");
        
        if(durationMinutes < 60){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+" has remained time before an hour get elapsed: "+durationMinutes+" minutes");
            return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^ Iyi Imodoka. "+checkLastTicket.getNumberPlate()+" iracyafite iminota "+(60-durationMinutes)+" kugirango isaha ishire.^ Yinjiriye muri Parikingi: "+checkLastTicket.getParkingDesc()+"^Yashyizwemo na: "+checkLastTicket.getConductorName()));
        }
        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" was parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId()+"  elapsed minutes from last ticket: "+durationMinutes+" minutes");
        
    }else{
        out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+checkLastTicket.getNumberPlate()+" already parked in: "+checkLastTicket.getParkingDesc()+" by conductor: "+checkLastTicket.getConductorName()+" and Id: "+checkLastTicket.getConductorId());
        return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"^ Iyi Imodoka. "+checkLastTicket.getNumberPlate()+" iragaragara nk'igiparitse ^ Parikingi: "+checkLastTicket.getParkingDesc()+"^Yashyizwemo na: "+checkLastTicket.getConductorName()));
    }
}
if(!ticketManager.genNewTicket(responseConductor.getConductor(), parkingBean, request.getInput())){
    out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest failed to generate ticket for: "+request.getInput());
    return ReturnConfig.isSuccess(faillureGen(request, conductorNames+" Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
}

out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest Succeeded to generate ticket for: "+request.getInput());
return ReturnConfig.isSuccess(successGen(request, conductorNames+" ^Gushyira Imodoka. "+request.getInput()+" muri parikingi.^Birakozwe."));

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
            
            List<Ticket> todayTickets = ticketFacade.getTicketsByNumberPlate(request.getInput());
            int check=0;
            if(todayTickets != null){
                for(Ticket ticket : todayTickets){
                    if(ticket.getOutDate() == null){
                        check++;
                    }
                }
            }
            
            if(check <= 0){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveRequest a car with "+request.getInput()+" with no ticket: by conductor: "+request.getMsisdn());
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+"Iyi Imodoka. "+request.getInput()+"^Ntago irikugaragara muri parikingi."));
            }
            
            if(!ticketManager.setTicketOutDate(true, request.getInput())){
                out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: numberPlate "+request.getInput()+" not found");
                return ReturnConfig.isSuccess(faillureGen(request, conductorNames+" Ibyo mushaka ntibibonetse.^Mwongere mukanya."));
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
                
                return ReturnConfig.isSuccess(carInOutMenu("Shyiramo imodoka.^Signup Car.^Enregistre l'auto.^Shyiramo puraki, Fill number plate, Entre la plaque.",request));
            }
            ResponseConductor responseConductor = getConductor(request.getMsisdn());
            Car car;
            if(!validateEntry(responseConductor)){
                out.print(AppDesc.APP_DESC+"UssdProcessor signupCar no conductor found for: "+request.getMsisdn());
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
            car = new Car(nPlate,
                    tel,
                    "Conductor:ID"+responseConductor.getConductor().getConductorId()+" Names:"+conductorNames,
                    new Date());
            carFacade.create(car);
            carFacade.refreshCar();
            
            out.print(AppDesc.APP_DESC+"UssdProcessor signupCar Succeeded to signup car: "+request.getInput()+" by requestor: "+request.getMsisdn());
            return ReturnConfig.isSuccess(successGen(request, conductorNames+"^Kwandika imodoka birakozwe"));
        }catch(Exception e){
            out.print(AppDesc.APP_DESC+"UssdProcessor receiveCarOut action failed due to: "+e.getMessage());
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
    
    private String carInOutMenu(String message, UssdRequest request){
        List<String> menus = new ArrayList<>();
        String outPut;
        
        Menu menu = new Menu(menus);
        UssdResponse response = new UssdResponse(request.getMsisdn(), request.getSessionId(), "C", message+"Andika purake yimodoka.", 0, menu);
        outPut = DataFactory.objectToXmlString(response);
        
        return outPut;
    }
}

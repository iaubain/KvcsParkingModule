/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.utilities;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.ParkingInfo;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.simplebeans.parking.ParkingInfoBean;
import com.kparking.oltranz.simplebeans.ticketsreport.PublishTicketRequest;
import com.kparking.oltranz.simplebeans.ticketsreport.TicketBean;
import static java.lang.System.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class TicketFactory {
    
    public String tuneTicketList(List<Ticket> mTickets){
        List<TicketBean> outTickets = new ArrayList<>();
        for(Ticket ticket : mTickets){
            if(ticket != null){
                TicketBean ticketBean = genTicketBean(ticket);
                outTickets.add(ticketBean);
            }
        }
        String outPut = DataFactory.objectToString(outTickets);
        out.print(AppDesc.APP_DESC+"TicketFactory tuneTicketList got out put: "+ outPut);
        return outPut;
    }
    
    public TicketBean genTicketBean(Ticket ticket){
        DateFormat dFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        return new TicketBean(ticket.getTicketId(),
                ticket.getParkingId(),
                ticket.getParkingDesc(),
                ticket.getConductorId(),
                ticket.getConductorName(),
                ticket.getMsisdn(),
                ticket.getNumberPlate(),
                ticket.getCarBrand() != null?ticket.getCarBrand():"",
                ticket.getTicketType() != null?ticket.getTicketType():"100",
                ticket.getInDate() != null?dFormat.format(ticket.getInDate()):"",
                ticket.getOutDate() != null?dFormat.format(ticket.getOutDate()):"");
    }
    
    public PublishTicketRequest genTicketToPublish(Ticket ticket){
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        return new PublishTicketRequest(new Date().getTime()+"",
                "1",
                ticket.getTicketId(),
                ticket.getNumberPlate(),
                ticket.getConductorId(),
                ticket.getCarBrand() != null ? ticket.getCarBrand() : "",
                Integer.parseInt(ticket.getTicketType()),
                Integer.parseInt(ticket.getParkingId()),
                dateFormat.format(ticket.getInDate()),
                dateFormat.format(ticket.getOutDate()));
    }
    
    public ParkingInfoBean genParkingInfo(ParkingInfo parkingInfo){
        return new ParkingInfoBean(parkingInfo.getId()+"",
                parkingInfo.getNumberPlate(),
                new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(parkingInfo.getInDate()),
                new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(parkingInfo.getOutDate()),
                parkingInfo.getMsisdn(),
                parkingInfo.getParkingSession(),
                parkingInfo.getParkingId(),
                new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(parkingInfo.getCreatedOn()));
    }
    
    public List<ParkingInfoBean> genParkingInfoList(List<ParkingInfo> parkingInfoList){
        List<ParkingInfoBean> parkingInfoBeans = new ArrayList<>();
        for(ParkingInfo parkingInfo : parkingInfoList){
            parkingInfoBeans.add(genParkingInfo(parkingInfo));
        }
        
        return parkingInfoBeans;
    }
}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Ticket;
import com.kparking.oltranz.facades.TicketFacade;
import com.kparking.oltranz.simplebeans.reportbeans.ReportConductor;
import com.kparking.oltranz.simplebeans.reportbeans.ReportEntity;
import com.kparking.oltranz.simplebeans.reportbeans.ReportParking;
import com.kparking.oltranz.simplebeans.reportbeans.RequestReportBean;
import com.kparking.oltranz.simplebeans.reportbeans.ResponseReport;
import com.kparking.oltranz.utilities.DataFactory;
import com.kparking.oltranz.utilities.ReturnConfig;
import static java.lang.System.out;
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
public class ReportGenerator {
    @EJB
            TicketFacade ticketFacade;
    public Response genReport(String body){
        try {
            RequestReportBean requestReportBean = (RequestReportBean) DataFactory.stringToObject(RequestReportBean.class, body);
            Date startDate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(requestReportBean.getStartDate());
            Date endDate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(requestReportBean.getEndDate());
            if(startDate == null || endDate == null){
                out.print(AppDesc.APP_DESC+"ReportGenerator genReport generate report failed due to: invalid date range "+body);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error: Invalid date range");
            }
            out.print(AppDesc.APP_DESC+"ReportGenerator genReport generate report startDate: "+startDate+" endDate: "+endDate);
            List<Ticket> tickets = ticketFacade.getActiveParkingByDate(startDate, endDate);
            if(tickets == null){
                out.print(AppDesc.APP_DESC+"ReportGenerator genReport generate report failed due to: No data found "+body);
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error: No data found");
            }
            out.print(AppDesc.APP_DESC+"ReportGenerator genReport generated parking report of size: "+tickets.size());
            List<ReportEntity> reportEntityList = new ArrayList<>();
            int row = 1;
            for(Ticket ticket : tickets){
                ReportConductor reportConductor = new ReportConductor(ticket.getConductorId(),
                        ticket.getConductorName(),
                        ticket.getMsisdn());
                Long numberOfCar = ticketFacade.getParkingCarCount(startDate, endDate, ticket.getParkingId());
                ReportParking reportParking = new ReportParking(ticket.getParkingId(),
                        ticket.getParkingDesc(),
                        numberOfCar+"");
                ReportEntity reportEntity = new ReportEntity(reportConductor, reportParking);
                reportEntityList.add(reportEntity);
                out.print(AppDesc.APP_DESC+"ReportGenerator genReport generate report row: "+row+" Data: "+ DataFactory.objectToString(reportEntity));
                row++;
            }
            ResponseReport responseReport = new ResponseReport(reportEntityList);
            String report = DataFactory.objectToString(responseReport);
            out.print(AppDesc.APP_DESC+"ReportGenerator genReport generated report: "+report);
            return ReturnConfig.isSuccess(report);
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"ReportGenerator genReport generate report failed due to: "+e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "Error: "+e.getLocalizedMessage());
        }
    }
}

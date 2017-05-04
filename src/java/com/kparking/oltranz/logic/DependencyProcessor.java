/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.logic;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.DependencyStatus;
import com.kparking.oltranz.entities.LinkStatus;
import com.kparking.oltranz.facades.DependencyStatusFacade;
import com.kparking.oltranz.facades.LinkStatusFacade;
import com.kparking.oltranz.simplebeans.commonbeans.DateRangeBean;
import com.kparking.oltranz.simplebeans.linkstate.CreateLinkStateRequest;
import com.kparking.oltranz.simplebeans.linkstate.DependencyStatusBean;
import com.kparking.oltranz.utilities.DataFactory;
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
public class DependencyProcessor {
    @EJB
            LinkStatusFacade linkFacade;
    @EJB
            DependencyStatusFacade dependencyFacade;
    public Response createLinkState(String body){
        try {
            out.print(AppDesc.APP_DESC+"DependencyProcessor createLinkState received: "+body);
            CreateLinkStateRequest cLinkStateRequest = (CreateLinkStateRequest) DataFactory.stringToObject(CreateLinkStateRequest.class, body);
            if(cLinkStateRequest == null){
                out.print(AppDesc.APP_DESC+"failed to create link State. EMPTY REQUEST");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to create link State. EMPTY REQUEST");
            }
            if(!isValidRequest(cLinkStateRequest)){
                out.print(AppDesc.APP_DESC+"failed to create link State. INVALID REQUEST");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to create link State. INVALID REQUEST");
            }
            DateFormat dFomat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date cDate = new Date();
            LinkStatus linkStatus = linkFacade.getStatus(cLinkStateRequest.getStatusCode());
            if(linkStatus != null){
                linkStatus.setNotify(cLinkStateRequest.isNotify());
                linkStatus.setNotificationType(cLinkStateRequest.getNotificationType());
                linkStatus.setNotificationMedium(cLinkStateRequest.getNotificationMedium());
                linkStatus.setNotificationStatus(cLinkStateRequest.getNotificationStatus());
                linkStatus.setLastAccessAction("UPDATE");
                linkStatus.setLastAccess(dFomat.parse(dFomat.format(cDate)));
                linkFacade.edit(linkStatus);
                linkFacade.refreshLink();
            }else{
                linkStatus = new LinkStatus();
                linkStatus.setNotify(cLinkStateRequest.isNotify());
                linkStatus.setNotificationType(cLinkStateRequest.getNotificationType());
                linkStatus.setNotificationMedium(cLinkStateRequest.getNotificationMedium());
                linkStatus.setNotificationStatus(cLinkStateRequest.getNotificationStatus());
                linkStatus.setLastAccessAction("CREATION");
                linkStatus.setStatusCode(cLinkStateRequest.getStatusCode());
                linkStatus.setLastAccess(dFomat.parse(dFomat.format(cDate)));
                linkStatus.setCreatedOn(dFomat.parse(dFomat.format(cDate)));
                linkFacade.create(linkStatus);
                linkFacade.refreshLink();
            }
            out.print(AppDesc.APP_DESC+"Request successfully processed. Body: "+body);
            return ReturnConfig.isSuccess("Success");
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"failed to create link State"+ e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to create link State. "+e.getMessage());
        }
    }
    
    public Response getDepencyStatus(String body){
        try {
            DateRangeBean dateRangeBean = (DateRangeBean) DataFactory.stringToObject(DateRangeBean.class, body);
            if(dateRangeBean == null){
                out.print(AppDesc.APP_DESC+"failed to get dependency status. EMPTY REQUEST");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to get dependency status. EMPTY REQUEST");
            }
            DateFormat dFomat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            Date startDate = dFomat.parse(dateRangeBean.getStartDate());
            Date endDate = dFomat.parse(dateRangeBean.getEndDate());
            List<DependencyStatus> dependencyStatuses = dependencyFacade.linkStatusDateRange(startDate, endDate);
            if(dependencyStatuses == null || dependencyStatuses.isEmpty()){
                out.print(AppDesc.APP_DESC+"failed to get dependency status. EMPTY RESPONSE");
                return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to get dependency status. EMPTY RESPONSE");
            }
            List<DependencyStatusBean> dependencyStatusBeans = new ArrayList<>();
            for(DependencyStatus dependencyStatus : dependencyStatuses){
                dependencyStatusBeans.add(tuneBean(dependencyStatus));
            } 
            String outPut = DataFactory.objectToString(dependencyStatusBeans);
            out.print(AppDesc.APP_DESC+"Produced depency report: "+outPut);
            return ReturnConfig.isSuccess(outPut);
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"failed to get dependency status"+ e.getMessage());
            return ReturnConfig.isFailed(Response.Status.EXPECTATION_FAILED, "failed to get dependency status. "+e.getMessage());
        }
    }
    
    private DependencyStatusBean tuneBean(DependencyStatus dependencyStatus){
        DateFormat dFomat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        return new DependencyStatusBean(dependencyStatus.getUrl(),
                dependencyStatus.getLinkStatus(),
                dFomat.format(dependencyStatus.getLastAccess()),
                dependencyStatus.getContentBody(),
                dependencyStatus.getResponseStatus());
    }
    private boolean isValidRequest(CreateLinkStateRequest cLink){
        return cLink.getNotificationMedium() != null &&
                cLink.getNotificationStatus() != null &&
                cLink.getNotificationType() != null &&
                cLink.getStatusCode() != 0;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.linkstate;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class CreateLinkStateRequest {
    private int statusCode;
    private boolean notify;
    private String notificationType;
    private String notificationMedium;
    private String notificationStatus;

    public CreateLinkStateRequest() {
    }

    public CreateLinkStateRequest(int statusCode, boolean notify, String notificationType, String notificationMedium, String notificationStatus) {
        this.statusCode = statusCode;
        this.notify = notify;
        this.notificationType = notificationType;
        this.notificationMedium = notificationMedium;
        this.notificationStatus = notificationStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationMedium() {
        return notificationMedium;
    }

    public void setNotificationMedium(String notificationMedium) {
        this.notificationMedium = notificationMedium;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
    
}

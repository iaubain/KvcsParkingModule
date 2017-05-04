/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Entity
public class LinkStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="statusCode", nullable = false, unique = true)
    private int statusCode;
    @Column(name="notify", nullable = false)
    private boolean notify;
    @Column(name="notificationType", nullable = false)
    private String notificationType;
    @Column(name="notificationMedium", nullable = false)
    private String notificationMedium;
    @Column(name="notificationStatus", nullable = false)
    private String notificationStatus;
    @Column(name="createdOn", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name="lastAccess", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name="lastAccessAction", nullable = false)
    private String lastAccessAction;

    public LinkStatus(int statusCode, boolean notify, String notificationType, String notificationMedium, String notificationStatus, Date createdOn, Date lastAccess, String lastAccessAction) {
        this.statusCode = statusCode;
        this.notify = notify;
        this.notificationType = notificationType;
        this.notificationMedium = notificationMedium;
        this.notificationStatus = notificationStatus;
        this.createdOn = createdOn;
        this.lastAccess = lastAccess;
        this.lastAccessAction = lastAccessAction;
    }

    public LinkStatus() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LinkStatus)) {
            return false;
        }
        LinkStatus other = (LinkStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.LinkStatus[ id=" + id + " ]";
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getLastAccessAction() {
        return lastAccessAction;
    }

    public void setLastAccessAction(String lastAccessAction) {
        this.lastAccessAction = lastAccessAction;
    }
    
}

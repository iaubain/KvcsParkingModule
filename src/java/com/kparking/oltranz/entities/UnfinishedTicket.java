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
public class UnfinishedTicket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="progressId", length = 522, nullable = false, unique = true)
            private String progressId;
    @Column(name="initMsisdn", length = 30, nullable = false)
            private String initMsisdn;
    @Column(name="numberPlate", length = 12, nullable = false)
            private String numberPlate;
    @Column(name="ticketType", length = 12, nullable = true)
            private String ticketType;
    @Column(name="createdOn", length = 12, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
            private Date createdOn;
    @Column(name="expireIn", length = 12, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
            private Date expireIn;
    @Column(name="isFinished")
    private boolean isFinished;
    @Column(name="count", length = 10, nullable = false)
    private int count;

    public UnfinishedTicket() {
    }

    public UnfinishedTicket(String progressId, String initMsisdn, String numberPlate, String ticketType, Date createdOn, Date expireIn, boolean isFinished, int count) {
        this.progressId = progressId;
        this.initMsisdn = initMsisdn;
        this.numberPlate = numberPlate;
        this.ticketType = ticketType;
        this.createdOn = createdOn;
        this.expireIn = expireIn;
        this.isFinished = isFinished;
        this.count = count;
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
        if (!(object instanceof UnfinishedTicket)) {
            return false;
        }
        UnfinishedTicket other = (UnfinishedTicket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.UnfinishedTicket[ id=" + id + " ]";
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public String getInitMsisdn() {
        return initMsisdn;
    }

    public void setInitMsisdn(String initMsisdn) {
        this.initMsisdn = initMsisdn;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Date expireIn) {
        this.expireIn = expireIn;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}

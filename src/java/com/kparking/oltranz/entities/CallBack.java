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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Entity
@Table(name = "callback",uniqueConstraints = {@UniqueConstraint(columnNames = {"ticketId"})})
public class CallBack implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="CallBackSeqGen",
            table="SEQUENCE_CALLBACK",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="CALLBACK_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CallBackSeqGen")
    private String id;
    
    @Column(name="ticketId", length = 522, nullable = false, unique = true)
    private String ticketId;
    @Column(name="numberPlate", length = 20)
    private String numberPlate;
    @Column(name="numberOfCallBack")
    private int numberOfCallBack;
    @Column(name="status", nullable = false)
    private int status;
    @Column(name="statusDesc", nullable = false)
    private String statusDesc;
    @Column(name="createdOn", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdOn;
    
    public CallBack() {
    }
    
    public CallBack(String ticketId, String numberPlate, int numberOfCallBack, int status, String statusDesc, Date createdOn) {
        this.ticketId = ticketId;
        this.numberPlate = numberPlate;
        this.numberOfCallBack = numberOfCallBack;
        this.status = status;
        this.statusDesc = statusDesc;
        this.createdOn = createdOn;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
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
        if (!(object instanceof CallBack)) {
            return false;
        }
        CallBack other = (CallBack) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.CallBack[ id=" + id + " ]";
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
    public int getNumberOfCallBack() {
        return numberOfCallBack;
    }
    
    public void setNumberOfCallBack(int numberOfCallBack) {
        this.numberOfCallBack = numberOfCallBack;
    }
    
    public Date getCreatedOn() {
        return createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getNumberPlate() {
        return numberPlate;
    }
    
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getStatusDesc() {
        return statusDesc;
    }
    
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    
}

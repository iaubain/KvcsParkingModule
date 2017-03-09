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
@Table(name = "progressive",uniqueConstraints = {@UniqueConstraint(columnNames = {"initMsisdn"})})
public class Progressive implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="ProgressiveSeqGen",
            table="SEQUENCE_PROGRESSIVE",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="PROGRESSIVE_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ProgressiveSeqGen")
    private String id;
    
    @Column(name="initMsisdn", length = 522, nullable = false, unique = true)
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

    public Progressive() {
    }

    public Progressive(String initMsisdn, String numberPlate, String ticketType, Date createdOn, Date expireIn) {
        this.initMsisdn = initMsisdn;
        this.numberPlate = numberPlate;
        this.ticketType = ticketType;
        this.createdOn = createdOn;
        this.expireIn = expireIn;
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
        if (!(object instanceof Progressive)) {
            return false;
        }
        Progressive other = (Progressive) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.Progressive[ id=" + id + " ]";
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
    
}

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
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Entity
public class ParkingInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="ParkingInfoGen",
            table="SEQUENCE_PARKINFO",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="PARKINFO_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ParkingInfoGen")
    private Long id;
    @Column(name="numberPlate", length = 30, nullable = false)
    private String numberPlate;
    @Column(name="inDate", length = 30, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inDate;
    @Column(name="outDate", length = 30, nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date outDate;
    @Column(name="msisdn", length = 30, nullable = false)
    private String msisdn;
    @Column(name="parkingSession", length = 30, nullable = false)
    private String parkingSession;
    @Column(name="parkingId", length = 30, nullable = false)
    private String parkingId;
    @Column(name="createdOn", length = 30, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdOn;

    public ParkingInfo() {
    }

    public ParkingInfo(String numberPlate, Date inDate, Date outDate, String msisdn, String parkingSession, String parkingId, Date createdOn) {
        this.numberPlate = numberPlate;
        this.inDate = inDate;
        this.outDate = outDate;
        this.msisdn = msisdn;
        this.parkingSession = parkingSession;
        this.parkingId = parkingId;
        this.createdOn = createdOn;
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
        if (!(object instanceof ParkingInfo)) {
            return false;
        }
        ParkingInfo other = (ParkingInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.ParkingInfo[ id=" + id + " ]";
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getParkingSession() {
        return parkingSession;
    }

    public void setParkingSession(String parkingSession) {
        this.parkingSession = parkingSession;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
}

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
@Table(name = "ticket",uniqueConstraints = {@UniqueConstraint(columnNames = {"ticketId"})})
public class Ticket implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="TicketSeqGen",
            table="SEQUENCE_TICKET",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="TICKET_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="TicketSeqGen")
    private Long id;
    
    @Column(name="ticketId", length = 522, nullable = false, unique = true)
    private String ticketId;
    @Column(name="numberPlate", length = 12, nullable = false)
    private String numberPlate;
    @Column(name="carBrand", length = 12, nullable = true)
    private String carBrand;
    @Column(name="msisdn", length = 30, nullable = false)
    private String msisdn;
    @Column(name="conductorId", length = 522, nullable = false)
    private String conductorId;
    @Column(name="conductorName", length = 30, nullable = true)
    private String conductorName;
    @Column(name="parkingId", length = 522, nullable = false)
    private String parkingId;
    @Column(name="parkingDesc", length = 522, nullable = true)
    private String parkingDesc;
    @Column(name="inDate", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inDate;
    @Column(name="outDate", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date outDate;
    @Column(name="ticketType", nullable = false)
    private String ticketType;
    @Column(name="recorded", nullable = false)
    private boolean recorded;
    
    public Ticket() {
    }

    public Ticket(String ticketId, String numberPlate, String carBrand, String msisdn, String conductorId, String conductorName, String parkingId, String parkingDesc, Date inDate, Date outDate, String ticketType, boolean recorded) {
        this.ticketId = ticketId;
        this.numberPlate = numberPlate;
        this.carBrand = carBrand;
        this.msisdn = msisdn;
        this.conductorId = conductorId;
        this.conductorName = conductorName;
        this.parkingId = parkingId;
        this.parkingDesc = parkingDesc;
        this.inDate = inDate;
        this.outDate = outDate;
        this.ticketType = ticketType;
        this.recorded = recorded;
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
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.Ticket[ id=" + id + " ]";
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getNumberPlate() {
        return numberPlate;
    }
    
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public String getConductorId() {
        return conductorId;
    }
    
    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }
    
    public String getConductorName() {
        return conductorName;
    }
    
    public void setConductorName(String conductorName) {
        this.conductorName = conductorName;
    }
    
    public String getParkingId() {
        return parkingId;
    }
    
    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
    
    public String getParkingDesc() {
        return parkingDesc;
    }
    
    public void setParkingDesc(String parkingDesc) {
        this.parkingDesc = parkingDesc;
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
    
    public String getTicketType() {
        return ticketType;
    }
    
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
    
    public boolean isRecorded() {
        return recorded;
    }
    
    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
    
}

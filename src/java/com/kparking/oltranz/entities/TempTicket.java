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
public class TempTicket implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="TempTicket",
            table="TempTicketSequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="pk_tempTicket")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="TempTicket")
    private Long id;
    @Column(name="sessionId", length = 522, nullable = false, unique = true)
    private String sessionId;
    @Column(name="msisdn", length = 30, nullable = false)
    private String msisdn;
    @Column(name="conductorId", length = 522, nullable = false)
    private String conductorId;
    @Column(name="conductorName", length = 60, nullable = false)
    private String conductorName;
    @Column(name="numberPlate", length = 30, nullable = false)
    private String numberPlate;
    @Column(name="ticketType", length = 6, nullable = false)
    private String ticketType;
    @Column(name="created", length = 30, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    @Column(name="parkingId", length = 522, nullable = false)
    private String parkingId;
    @Column(name="parkingDesc", length = 522, nullable = true)
    private String parkingDesc;
    @Column(name="expire", length = 30, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date expire;
    
    public TempTicket() {
    }

    public TempTicket(String sessionId, String msisdn, String conductorId, String conductorName, String numberPlate, String ticketType, Date created, String parkingId, String parkingDesc, Date expire) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.conductorId = conductorId;
        this.conductorName = conductorName;
        this.numberPlate = numberPlate;
        this.ticketType = ticketType;
        this.created = created;
        this.parkingId = parkingId;
        this.parkingDesc = parkingDesc;
        this.expire = expire;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
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
        if (!(object instanceof TempTicket)) {
            return false;
        }
        TempTicket other = (TempTicket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.TempTicket[ id=" + id + " ]";
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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
    
    public Date getCreated() {
        return created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    
    public Date getExpire() {
        return expire;
    }
    
    public void setExpire(Date expire) {
        this.expire = expire;
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
    
}

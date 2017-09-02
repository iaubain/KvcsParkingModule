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
import javax.persistence.Temporal;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Entity
@Table(name = "verification")
public class Verification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @Column(name="verifierMsisdn", length = 21, nullable = false)
    private String verifierMsisdn;
    @Column(name="parkingId", length = 21, nullable = false)
    private String parkingId;
    @Column(name="numberPlate", length = 12, nullable = false)
    private String numberPlate;
    @Column(name="conductorId", length = 522, nullable = false)
            private String conductorId;
    @Column(name="conductorNames", length = 225, nullable = true)
            private String conductorNames;
    @Column(name="verifiedOn", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
            private Date verifiedOn;
    @Column(name="verified", nullable = false)
            private boolean verified;

    public Verification() {
    }

    public Verification(String verifierMsisdn, String parkingId, String numberPlate, String conductorId, String conductorNames, Date verifiedOn, boolean verified) {
        this.verifierMsisdn = verifierMsisdn;
        this.parkingId = parkingId;
        this.numberPlate = numberPlate;
        this.conductorId = conductorId;
        this.conductorNames = conductorNames;
        this.verifiedOn = verifiedOn;
        this.verified = verified;
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
        if (!(object instanceof Verification)) {
            return false;
        }
        Verification other = (Verification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.Verification[ id=" + id + " ]";
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getConductorNames() {
        return conductorNames;
    }

    public void setConductorNames(String conductorNames) {
        this.conductorNames = conductorNames;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerifierMsisdn() {
        return verifierMsisdn;
    }

    public void setVerifierMsisdn(String verifierMsisdn) {
        this.verifierMsisdn = verifierMsisdn;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    
}

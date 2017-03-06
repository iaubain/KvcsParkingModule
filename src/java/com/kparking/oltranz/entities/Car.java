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
public class Car implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="CarSeqGen",
            table="SEQUENCE_CARBACK",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="CAR_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CarSeqGen")
    private String id;
    @Column(name="numberPlate", length = 522, nullable = false)
            private String numberPlate;
    @Column(name="creatorTel", length = 522, nullable = false)
            private String creatorTel;
    @Column(name="creatorName", length = 522, nullable = false)
            private String creatorName;
    @Column(name="created", length = 522, nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
            private Date created;

    public Car() {
    }

    public Car(String numberPlate, String creatorTel, String creatorName, Date created) {
        this.numberPlate = numberPlate;
        this.creatorTel = creatorTel;
        this.creatorName = creatorName;
        this.created = created;
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
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.Car[ id=" + id + " ]";
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getCreatorTel() {
        return creatorTel;
    }

    public void setCreatorTel(String creatorTel) {
        this.creatorTel = creatorTel;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
}

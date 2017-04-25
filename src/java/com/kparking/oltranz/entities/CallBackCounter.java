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
public class CallBackCounter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="CallBackCounterSeqGen",
            table="SEQUENCE_CallBackCounter",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT",
            pkColumnValue="CBackC_PK")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CallBackCounterSeqGen")
    private Long id;
    @Column(name="jobId", nullable = false, length = 522, unique = true)
    private String jobId;
    @Column(name="sessionId", nullable = false, length = 522)
    private String sessionId;
    @Column(name="callbackCount", nullable = false)
    private int callbackCount;
    @Column(name="creationDate", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;
    
    public CallBackCounter() {
    }

    public CallBackCounter(String jobId, String sessionId, int callbackCount, Date creationDate) {
        this.jobId = jobId;
        this.sessionId = sessionId;
        this.callbackCount = callbackCount;
        this.creationDate = creationDate;
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
        if (!(object instanceof CallBackCounter)) {
            return false;
        }
        CallBackCounter other = (CallBackCounter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.TicketNotify[ id=" + id + " ]";
    }
    
    public String getJobId() {
        return jobId;
    }
    
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
    public int getCallbackCount() {
        return callbackCount;
    }
    
    public void setCallbackCount(int callbackCount) {
        this.callbackCount = callbackCount;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
}

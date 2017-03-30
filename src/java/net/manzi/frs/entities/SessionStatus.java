/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.entities;

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
public class SessionStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="sessionId", nullable = false, unique = true)
    private String sessionId;
    @Column(name="initTel", nullable = false)
    private String initTel;
    @Column(name="currentStep", nullable = false)
    private String currentStep;
    @Column(name="currentInput", nullable = true)
    private String currentInput;
    @Column(name="nextStep", nullable = false)
    private String nextStep;
    @Column(name="completed", nullable = false)
    private boolean completed;
    @Column(name="sessionStatus", nullable = false)
    private String sessionStatus;
    @Column(name="creationDate", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name="lastAccessDate", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastAccessDate;
    @Column(name="lastAction", nullable = true)
    private String lastAction;
    @Column(name="sessionType", nullable = false)
    private String sessionType;
    @Column(name="stepsCount", nullable = false)
    private int stepsCount;

    public SessionStatus() {
    }

    public SessionStatus(String sessionId, String initTel, String currentStep, String currentInput, String nextStep, boolean completed, String sessionStatus, Date creationDate, Date lastAccessDate, String lastAction, String sessionType, int stepsCount) {
        this.sessionId = sessionId;
        this.initTel = initTel;
        this.currentStep = currentStep;
        this.currentInput = currentInput;
        this.nextStep = nextStep;
        this.completed = completed;
        this.sessionStatus = sessionStatus;
        this.creationDate = creationDate;
        this.lastAccessDate = lastAccessDate;
        this.lastAction = lastAction;
        this.sessionType = sessionType;
        this.stepsCount = stepsCount;
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
        if (!(object instanceof SessionStatus)) {
            return false;
        }
        SessionStatus other = (SessionStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.manzi.frs.entities.SessionStatus[ id=" + id + " ]";
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getInitTel() {
        return initTel;
    }

    public void setInitTel(String initTel) {
        this.initTel = initTel;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }
    
}

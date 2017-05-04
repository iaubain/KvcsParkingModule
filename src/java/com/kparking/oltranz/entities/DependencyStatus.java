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
public class DependencyStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="url", length = 522, nullable = false)
    private String url;
    @Column(name="linkStatus", nullable = false)
    private String linkStatus;
    @Column(name="lastAccess", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name="contentBody", length = 1023, nullable = true)
    private String contentBody;
    @Column(name="responseStatus", nullable = false)
    private int responseStatus;

    public DependencyStatus() {
    }

    public DependencyStatus(String url, String linkStatus, Date lastAccess, String contentBody, int responseStatus) {
        this.url = url;
        this.linkStatus = linkStatus;
        this.lastAccess = lastAccess;
        this.contentBody = contentBody;
        this.responseStatus = responseStatus;
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
        if (!(object instanceof DependencyStatus)) {
            return false;
        }
        DependencyStatus other = (DependencyStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kparking.oltranz.entities.DependencyStatus[ id=" + id + " ]";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }
    
}

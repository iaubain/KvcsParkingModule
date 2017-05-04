/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.linkstate;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class DependencyStatusBean {
    private String url;
    private String linkStatus;
    private String lastAccess;
    private String contentBody;
    private int responseStatus;

    public DependencyStatusBean() {
    }

    public DependencyStatusBean(String url, String linkStatus, String lastAccess, String contentBody, int responseStatus) {
        this.url = url;
        this.linkStatus = linkStatus;
        this.lastAccess = lastAccess;
        this.contentBody = contentBody;
        this.responseStatus = responseStatus;
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

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
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

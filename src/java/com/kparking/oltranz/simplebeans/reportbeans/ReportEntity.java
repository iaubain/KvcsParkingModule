/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.reportbeans;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class ReportEntity {
    private ReportConductor reportConductor;
    private ReportParking reportParking;

    public ReportEntity() {
    }

    public ReportEntity(ReportConductor reportConductor, ReportParking reportParking) {
        this.reportConductor = reportConductor;
        this.reportParking = reportParking;
    }

    public ReportParking getReportParking() {
        return reportParking;
    }

    public void setReportParking(ReportParking reportParking) {
        this.reportParking = reportParking;
    }

    public ReportConductor getReportConductor() {
        return reportConductor;
    }

    public void setReportConductor(ReportConductor reportConductor) {
        this.reportConductor = reportConductor;
    }
    
}

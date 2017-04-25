/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.reportbeans;

import java.util.List;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class ResponseReport {
    private List<ReportEntity> mReport;

    public ResponseReport() {
    }

    public ResponseReport(List<ReportEntity> mReport) {
        this.mReport = mReport;
    }

    public List<ReportEntity> getmReport() {
        return mReport;
    }

    public void setmReport(List<ReportEntity> mReport) {
        this.mReport = mReport;
    }
    
}

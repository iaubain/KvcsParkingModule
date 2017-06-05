/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.verification;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class VerificationData {
    private int stepCount;
    private VerifyRequest verifyRequest;

    public VerificationData() {
    }

    public VerificationData(int stepCount, VerifyRequest verifyRequest) {
        this.stepCount = stepCount;
        this.verifyRequest = verifyRequest;
    }

    public VerifyRequest getVerifyRequest() {
        return verifyRequest;
    }

    public void setVerifyRequest(VerifyRequest verifyRequest) {
        this.verifyRequest = verifyRequest;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
    
}

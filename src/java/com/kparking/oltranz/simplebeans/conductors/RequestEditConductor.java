/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.conductors;

import com.kparking.oltranz.simplebeans.commonbeans.ConductorBean;


/**
 *
 * @author Hp
 */
public class RequestEditConductor {
    private ConductorBean conductor;

    public RequestEditConductor() {
    }

    public RequestEditConductor(ConductorBean conductor) {
        this.conductor = conductor;
    }

    public ConductorBean getConductor() {
        return conductor;
    }

    public void setConductor(ConductorBean conductor) {
        this.conductor = conductor;
    }
    
}

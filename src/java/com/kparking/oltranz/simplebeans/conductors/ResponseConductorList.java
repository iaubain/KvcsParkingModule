/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.conductors;

import java.util.List;

/**
 *
 * @author Hp
 */
public class ResponseConductorList {
    private List<ResponseConductor> conductorList;

    public ResponseConductorList() {
    }

    public ResponseConductorList(List<ResponseConductor> conductorList) {
        this.conductorList = conductorList;
    }

    public List<ResponseConductor> getConductorList() {
        return conductorList;
    }

    public void setConductorList(List<ResponseConductor> conductorList) {
        this.conductorList = conductorList;
    }
    
}

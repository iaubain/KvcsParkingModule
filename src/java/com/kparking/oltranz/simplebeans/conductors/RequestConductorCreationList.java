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
public class RequestConductorCreationList {
    private List<RequestConductorCreation> conductorList;

    public RequestConductorCreationList() {
    }

    public RequestConductorCreationList(List<RequestConductorCreation> conductorList) {
        this.conductorList = conductorList;
    }

    public List<RequestConductorCreation> getConductorList() {
        return conductorList;
    }

    public void setConductorList(List<RequestConductorCreation> conductorList) {
        this.conductorList = conductorList;
    }
    
}

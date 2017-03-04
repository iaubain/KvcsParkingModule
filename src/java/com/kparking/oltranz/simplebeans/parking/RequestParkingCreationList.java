/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.parking;

import java.util.List;

/**
 *
 * @author Hp
 */
public class RequestParkingCreationList {
    private List<RequestParkingCreation> parkingList;

    public RequestParkingCreationList() {
    }

    public RequestParkingCreationList(List<RequestParkingCreation> parkingList) {
        this.parkingList = parkingList;
    }

    public List<RequestParkingCreation> getParkingList() {
        return parkingList;
    }

    public void setParkingList(List<RequestParkingCreation> parkingList) {
        this.parkingList = parkingList;
    }
    
}

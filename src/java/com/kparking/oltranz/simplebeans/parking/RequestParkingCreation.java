/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.parking;

import com.kparking.oltranz.simplebeans.commonbeans.Address;
import com.kparking.oltranz.simplebeans.commonbeans.Coordinates;

/**
 *
 * @author Hp
 */
public class RequestParkingCreation {
    private Address address;
    private Coordinates coordinates;
    private String description;

    public RequestParkingCreation() {
    }

    public RequestParkingCreation(Address address, Coordinates coordinates, String description) {
        this.address = address;
        this.coordinates = coordinates;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
}

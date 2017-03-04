/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.commonbeans;

/**
 *
 * @author Hp
 */
public class ParkingBean {
    private String parkingId;
    private String description;
    private Address address;
    private Coordinates coordinates;
    private StatusBean currentStatus;

    public ParkingBean() {
    }

    public ParkingBean(String parkingId, String description, Address address, Coordinates coordinates, StatusBean currentStatus) {
        this.parkingId = parkingId;
        this.description = description;
        this.address = address;
        this.coordinates = coordinates;
        this.currentStatus = currentStatus;
    }

    public StatusBean getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(StatusBean currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}

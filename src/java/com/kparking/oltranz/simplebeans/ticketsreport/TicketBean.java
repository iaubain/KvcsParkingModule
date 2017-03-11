/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.ticketsreport;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class TicketBean {
    private String ticketId;
    private String parkingId;
    private String parkingDesc;
    private String conductorId;
    private String conductorNames;
    private String nPlate;
    private String carBrand;
    private String ticketType;
    private String inTime;
    private String outTime;

    public TicketBean() {
    }

    public TicketBean(String ticketId, String parkingId, String parkingDesc, String conductorId, String conductorNames, String nPlate, String carBrand, String ticketType, String inTime, String outTime) {
        this.ticketId = ticketId;
        this.parkingId = parkingId;
        this.parkingDesc = parkingDesc;
        this.conductorId = conductorId;
        this.conductorNames = conductorNames;
        this.nPlate = nPlate;
        this.carBrand = carBrand;
        this.ticketType = ticketType;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingDesc() {
        return parkingDesc;
    }

    public void setParkingDesc(String parkingDesc) {
        this.parkingDesc = parkingDesc;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getConductorNames() {
        return conductorNames;
    }

    public void setConductorNames(String conductorNames) {
        this.conductorNames = conductorNames;
    }

    public String getnPlate() {
        return nPlate;
    }

    public void setnPlate(String nPlate) {
        this.nPlate = nPlate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
    
}

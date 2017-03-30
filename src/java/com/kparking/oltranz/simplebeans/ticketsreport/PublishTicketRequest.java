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
public class PublishTicketRequest {
    private String requestId;
    private String requestorUserId;
    private String ticketId;
    private String plateNumber;
    private String conductorId;
    private String brandName;
    private int ticketAmount;
    private int parkingId;
    private String startTime;
    private String endTime;

    public PublishTicketRequest() {
    }

    public PublishTicketRequest(String requestId, String requestorUserId, String ticketId, String plateNumber, String conductorId, String brandName, int ticketAmount, int parkingId, String startTime, String endTime) {
        this.requestId = requestId;
        this.requestorUserId = requestorUserId;
        this.ticketId = ticketId;
        this.plateNumber = plateNumber;
        this.conductorId = conductorId;
        this.brandName = brandName;
        this.ticketAmount = ticketAmount;
        this.parkingId = parkingId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestorUserId() {
        return requestorUserId;
    }

    public void setRequestorUserId(String requestorUserId) {
        this.requestorUserId = requestorUserId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(int ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
}

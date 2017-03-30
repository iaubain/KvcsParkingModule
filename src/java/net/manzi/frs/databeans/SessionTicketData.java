/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.databeans;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class SessionTicketData {
    private String sessionId;
    private String nPlate;
    private String ticketType;
    private String carBrand;

    public SessionTicketData() {
    }

    public SessionTicketData(String sessionId, String nPlate, String ticketType, String carBrand) {
        this.sessionId = sessionId;
        this.nPlate = nPlate;
        this.ticketType = ticketType;
        this.carBrand = carBrand;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getnPlate() {
        return nPlate;
    }

    public void setnPlate(String nPlate) {
        this.nPlate = nPlate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
    
}

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
public class Address {
    private String province;
    private String district;
    private String sector;
    private String cell;

    public Address() {
    }

    public Address(String province, String district, String sector, String cell) {
        this.province = province;
        this.district = district;
        this.sector = sector;
        this.cell = cell;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
    
}

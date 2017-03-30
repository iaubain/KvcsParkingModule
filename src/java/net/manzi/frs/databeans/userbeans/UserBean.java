/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.databeans.userbeans;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class UserBean {
    private int agentZoneId;
    private String agentZoneName;
    @JsonProperty("fname")
    private String fName;
    private String locationId;
    private String locationName;
    private String otherNames;
    private String phoneNumber;
    private int roleId;
    private String roleName;
    private int supervisorId;
    private String supervisorMsisdn;
    private String supervisorNames;
    private int userId;

    public UserBean() {
    }

    public UserBean(int agentZoneId, String agentZoneName, String fName, String locationId, String locationName, String otherNames, String phoneNumber, int roleId, String roleName, int supervisorId, String supervisorMsisdn, String supervisorNames, int userId) {
        this.agentZoneId = agentZoneId;
        this.agentZoneName = agentZoneName;
        this.fName = fName;
        this.locationId = locationId;
        this.locationName = locationName;
        this.otherNames = otherNames;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
        this.roleName = roleName;
        this.supervisorId = supervisorId;
        this.supervisorMsisdn = supervisorMsisdn;
        this.supervisorNames = supervisorNames;
        this.userId = userId;
    }

    public int getAgentZoneId() {
        return agentZoneId;
    }

    public void setAgentZoneId(int agentZoneId) {
        this.agentZoneId = agentZoneId;
    }

    public String getAgentZoneName() {
        return agentZoneName;
    }

    public void setAgentZoneName(String agentZoneName) {
        this.agentZoneName = agentZoneName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorMsisdn() {
        return supervisorMsisdn;
    }

    public void setSupervisorMsisdn(String supervisorMsisdn) {
        this.supervisorMsisdn = supervisorMsisdn;
    }

    public String getSupervisorNames() {
        return supervisorNames;
    }

    public void setSupervisorNames(String supervisorNames) {
        this.supervisorNames = supervisorNames;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
 
}

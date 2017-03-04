/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.config;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
public class KpmCmdConfig {
    //Conductors commands
    public static final String CREATE_COND = "CREATE_CONDUCTOR";
    public static final String CREATE_BULK_COND = "CREATE_BULK_CONDUCTORS";
    
    public static final String GET_COND = "GET_CONDUCTOR";
    public static final String GET_TEL_COND = "GET_TEL_CONDUCTOR";
    public static final String UNDEPLOY_COND = "UNDEPLOY_CONDUCTOR";
    public static final String GET_ALL_COND = "GET_ALL_CONDUCTOR";
    public static final String GET_REGIONAL_COND = "GET_REGIONAL_CONDUCTOR";
    public static final String GET_STATUS_COND = "GET_CONDUCTORS_PER_STATUS";
    public static final String GET_DEPLOYMENT_COND = "GET_CONDUCTORS_DEPLOYMENT";
    
    public static final String EDIT_COND = "EDIT_CONDUCTOR";
    public static final String DELETE_COND = "DELETE_CONDUCTOR";
    
    //Parking commands
    public static final String CREATE_PARK = "CREATE_PARKING";
    public static final String CREATE_BULK_PARK = "CREATE_BULK_PARKINGS";
    
    public static final String GET_PARK = "GET_PARKING";
    public static final String GET_ALL_PARK = "GET_ALL_PARKING";
    public static final String GET_REGIONAL_PARK = "GET_REGIONAL_PARKING";
    public static final String GET_STATUS_PARK = "GET_PARKINGS_PER_STATUS";
    public static final String GET_DEPLOYMENT_PARK = "GET_PARKINGS_DEPLOYMENT";
    
    public static final String EDIT_PARK = "EDIT_PARKING";
    public static final String DELETE_PARK = "DELETE_PARKING";
    
    //Deployment commands
    public static final String DEPLOY = "DEPLOY";
    public static final String CANCEL_DEP = "CANCEL_DEPLOYMENT";
    public static final String GET_DEP = "GET_DEPLOYMENT";
    public static final String GET_REGIONAL_DEP = "GET_REGIONAL_DEPLOYMENT";
    public static final String GET_ALL_DEP = "GET_ALL_DEPLOYMENT";
    public static final String GET_DEPLOYER_DEP = "GET_DEPLOYER_DEPLOYMENT";
    public static final String GET_DEPLOYEE_DEP = "GET_DEPLOYEE_DEPLOYMENT";
}

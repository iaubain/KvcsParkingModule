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
public class ApiConfig {
    public static final String SCHED_CONTRACT = "1488458455882KvcsParkingGateway";
    public static final String SCHED_URL = "http://localhost:8080/shedule/declare/action";
    
    public static final String KPM_CONTRACT = "8a3dff6567ede4b3f6e21f8b73c7bd8dd73f5cff";
    public static final String KPM_URL = "http://localhost:8080/ParkingModule/system/actions";
    
    public static final int SMS_CONTRACT = 15603452;
    public static final String SMS_SIGNATURE = "648eb83555bab00b3f732e19ce65126f";
    public static final String SMS_URL = "http://localhost:8080/SMSServiceProvider/sendSMS";
    
    public static final String CALLBACK_URL = "http://localhost:8080/Kparking/system/schedCallBack";
    public static final String PROGRESS_CALLBACK_URL = "http://localhost:8080/Kparking/system/progressCallBack";
}

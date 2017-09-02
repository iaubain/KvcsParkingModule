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
    public static final String SCHED_URL = "http://10.10.79.229:80/shedule/declare/action";
    
    public static final String KPM_CONTRACT = "8a3dff6567ede4b3f6e21f8b73c7bd8dd73f5cff";
    public static final String KPM_URL = "http://10.10.79.228:80/ParkingModule/system/actions";
    
    public static final int SMS_CONTRACT = 15603452;
    public static final String SMS_SIGNATURE = "648eb83555bab00b3f732e19ce65126f";
    public static final String SMS_URL = "http://41.74.172.132:8080/SMSServiceProvider/sendSMS";
    
    public static final String CALLBACK_URL = "http://10.10.79.228:80/Kparking/system/schedCallBack";
    public static final String PROGRESS_CALLBACK_URL = "http://10.10.79.228:80/Kparking/system/progressCallBack";
    public static final String TEMPTICKET_CALLBACK_URL = "http://10.10.79.228:80/Kparking/system/tempTicket";
    public static final String TICKET_NOTIFICATION_URL = "http://10.10.79.228:80/Kparking/system/tNotication";
    
    public static final String GET_USER_BY_TEL = "http://10.10.79.228:80/KVCSUsersManager/FieldsOpsAgents/fieldAgentByMsisdn/";
    public static final String CREATE_USER = "http://10.10.79.228:80/calhost:8080KVCSUsersManager/FieldsOpsAgents/fieldAgentByMsisdn/";
    
    public static final String PUBLISH_TICKET_CMD = "001";
    public static final String PUBLISH_TICKET_DOMAIN = "kvcsServices";
    public static final String PUBLISH_TICKET = "http://10.10.79.228:80/KVCSParkingTicketsManager/TicketsManager/loadTicket";
    
    public static final String VALIDATE_NUMBER_PLATE = "http://10.10.79.228:80/KVCSValidator/NumberPlateValdation";
    
    public static final String GET_PARKING_USERS = "http://10.10.79.228:80/KVCSUsersManager/FieldsOpsAgents/fieldAgentsByParkingId/";
    //public static final String GET_PARKING_USERS = "http://41.74.172.131:8080/RequestsDispatcherV2/RequestsHandler";//http://41.74.172.131:8080/RequestsDispatcherV2/RequestsHandler
    public static final String CAR_ACCOUNT_DOMAIN = "KvcsAccounts";
    public static final String GET_CONTACT_CMD = "012";
    //public static final String GET_NUMBERPLATE_CONTACT = "http://localhost:8080/KVCSMotorsAccountsManager/MotorsContacts/getListPerNumberPlate/";
    public static final String GET_NUMBERPLATE_CONTACT = "http://41.74.172.131:8080/RequestsDispatcherV2/RequestsHandler";// for kvcs server http://41.74.172.131:8080/RequestsDispatcherV2/RequestsHandler for Oltranz http://localhost:8080/RequestsDispatcherV2/RequestsHandler
    public static final String GET_CAR_BALANCE_CMD = "013";
    public static final String REQUEST_CAR_BALANCE = "http://41.74.172.131:8080/RequestsDispatcherV2/RequestsHandler";
    
    public static final String CAR_VERIFICATION = "http://10.10.79.228:80/Kparking/system/verify";
    
    public static final String GET_SYSTEM_USER = "http://10.10.79.228:80/KVCSUsersManager/UserManagementService/userByMsisdn";
}

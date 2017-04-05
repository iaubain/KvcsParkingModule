/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.ParkingInfo;
import static java.lang.System.out;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class ParkingInfoFacade extends AbstractFacade<ParkingInfo> {
    
    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ParkingInfoFacade() {
        super(ParkingInfo.class);
    }
    public void refreshParkInfo(){
        em.flush();
    }
    public ParkingInfo getCustomerLastParkiInfo(String numberPlate){
        try{
            if(numberPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select P from ParkingInfo P WHERE P.numberPlate = :numberPlate ORDER BY P.id DESC");
            q.setParameter("numberPlate", numberPlate)
                    .setMaxResults(1);
            List<ParkingInfo> list = (List<ParkingInfo>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<ParkingInfo> getSessionParkInfo(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select P from ParkingInfo P WHERE P.parkingSession = :sessionId ORDER BY P.id DESC");
            q.setParameter("sessionId", sessionId);
            List<ParkingInfo> list = (List<ParkingInfo>)q.getResultList();
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<ParkingInfo> getConductorParkInfo(String msisdn){
         try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select P from ParkingInfo P WHERE P.msisdn = :msisdn ORDER BY P.id DESC");
            q.setParameter("msisdn", msisdn);
            List<ParkingInfo> list = (List<ParkingInfo>)q.getResultList();
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<ParkingInfo> getCustomerParkInfo(String numberPlate){
         try{
            if(numberPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select P from ParkingInfo P WHERE P.numberPlate = :numberPlate ORDER BY P.id DESC");
            q.setParameter("numberPlate", numberPlate);
            List<ParkingInfo> list = (List<ParkingInfo>)q.getResultList();
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
}

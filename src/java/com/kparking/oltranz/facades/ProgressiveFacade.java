/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.Progressive;
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
public class ProgressiveFacade extends AbstractFacade<Progressive> {
    
    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ProgressiveFacade() {
        super(Progressive.class);
    }
    public void refreshProgressive(){
        em.flush();
    }
    
    public Progressive getProgressById(String progressId){
        try{
            if(progressId.isEmpty())
                return null;
            Query q= em.createQuery("Select P from Progressive P WHERE P.progressId = :progressId");
            q.setParameter("progressId", progressId);
            List<Progressive> list = (List<Progressive>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Progressive getConductorLastProgressive(String msisdn){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select P from Progressive P WHERE P.initMsisdn = :msisdn ORDER BY P.id DESC");
            q.setParameter("msisdn", msisdn)
                    .setMaxResults(1);
            List<Progressive> list = (List<Progressive>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Progressive getCustomerLastProgressive(String msisdn, String nPlate){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select P from Progressive P WHERE P.numberPlate = :nPlate AND P.initMsisdn = :msisdn ORDER BY P.id DESC");
            q.setParameter("msisdn", msisdn)
                    .setParameter("nPlate", nPlate)
                    .setMaxResults(1);
            List<Progressive> list = (List<Progressive>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Progressive getConductorLastUnfinishedProgressive(String msisdn, boolean taskState){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select P from Progressive P WHERE P.initMsisdn = :msisdn AND P.isFinished = :taskState ORDER BY P.id DESC");
            q.setParameter("msisdn", msisdn)
                    .setParameter("taskState", taskState)
                    .setMaxResults(1);
            List<Progressive> list = (List<Progressive>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
}

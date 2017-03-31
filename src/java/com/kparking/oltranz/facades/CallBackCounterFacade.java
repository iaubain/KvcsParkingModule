/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.facades;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.CallBackCounter;
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
public class CallBackCounterFacade extends AbstractFacade<CallBackCounter> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CallBackCounterFacade() {
        super(CallBackCounter.class);
    }
    public void refreshCounter(){
        em.flush();
    }
    
    public void getLAstCallBack(String sessionId){
    }
    
    public CallBackCounter getLastSession(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select C from CallBackCounter C WHERE C.sessionId = :sessionId ORDER BY C.id DESC");
            q.setParameter("sessionId", sessionId)
                    .setMaxResults(1);
            List<CallBackCounter> list = (List<CallBackCounter>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            out.print(AppDesc.APP_DESC+" SessionStatusFacade getLastSession failed due to: "+ex.getMessage());
            return null;
        }
    }
}

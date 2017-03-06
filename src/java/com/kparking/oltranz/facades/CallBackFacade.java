/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.CallBack;
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
public class CallBackFacade extends AbstractFacade<CallBack> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CallBackFacade() {
        super(CallBack.class);
    }
    public void refreshCallBack(){
        em.flush();
    }
    public CallBack getTicketById(String ticketId){
        try{
            if(ticketId.isEmpty())
                return null;
            Query q= em.createQuery("Select C from CallBack C WHERE C.ticketId = :ticketId");
            q.setParameter("ticketId", ticketId);
            List<CallBack> list = (List<CallBack>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    public CallBack getCustormerLastCallback(String numberPlate){
        try{
            if(numberPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select C from CallBack C WHERE C.numberPlate = :numberPlate ORDER BY C.id ASC");
            q.setParameter("numberPlate", numberPlate)
                    .setMaxResults(1);
            List<CallBack> list = (List<CallBack>)q.getResultList();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.UnfinishedTicket;
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
public class UnfinishedTicketFacade extends AbstractFacade<UnfinishedTicket> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public void refreshUnFinTicket(){
        em.flush();
    }

    public UnfinishedTicketFacade() {
        super(UnfinishedTicket.class);
    }
    
    public UnfinishedTicket getUnFinishedTicketById(String progressId){
        try{
            if(progressId.isEmpty())
                return null;
            Query q= em.createQuery("Select U from UnfinishedTicket U WHERE U.progressId = :progressId");
            q.setParameter("progressId", progressId);
            List<UnfinishedTicket> list = (List<UnfinishedTicket>)q.getResultList();
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

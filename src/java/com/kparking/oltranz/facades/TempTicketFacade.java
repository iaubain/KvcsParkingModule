/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.TempTicket;
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
public class TempTicketFacade extends AbstractFacade<TempTicket> {
    
    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public TempTicketFacade() {
        super(TempTicket.class);
    }
    public void refreshTemp(){
        em.flush();
    }
    
    public TempTicket getLastTempBySession(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from TempTicket T WHERE T.sessionId = :sessionId ORDER BY T.id DESC");
            q.setParameter("sessionId", sessionId)
                    .setMaxResults(1);
            List<TempTicket> list = (List<TempTicket>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public TempTicket getLastCustomerTempTicket(String nPlate){
        try{
            if(nPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select T from TempTicket T WHERE T.numberPlate = :nPlate ORDER BY T.id DESC");
            q.setParameter("nPlate", nPlate)
                    .setMaxResults(1);
            List<TempTicket> list = (List<TempTicket>)q.getResultList();
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

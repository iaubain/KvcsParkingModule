/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.entities.Ticket;
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
public class TicketFacade extends AbstractFacade<Ticket> {
    
    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public TicketFacade() {
        super(Ticket.class);
    }
    
    public void refreshTicket(){
        em.flush();
    }
    
    public Ticket getTicketById(String ticketId){
        try{
            if(ticketId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.ticketId = :ticketId");
            q.setParameter("ticketId", ticketId);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getTicketsByConductor(String msisdn){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.msisdn = :msisdn");
            q.setParameter("msisdn", msisdn);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            return list.isEmpty()?null:list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getTicketsByNumberPlate(String nPlate){
        try{
            if(nPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.numberPlate = :nPlate");
            q.setParameter("nPlate", nPlate);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            return list.isEmpty()?null:list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Ticket getConductorLastTicket(String msisdn){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.msisdn = :msisdn ORDER BY T.id ASC");
            q.setParameter("msisdn", msisdn)
                    .setMaxResults(1);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Ticket getCustormerLastTicket(String numberPlate){
        try{
            if(numberPlate.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.numberPlate = :numberPlate ORDER BY T.id ASC");
            q.setParameter("numberPlate", numberPlate)
                    .setMaxResults(1);
            List<Ticket> list = (List<Ticket>)q.getResultList();
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

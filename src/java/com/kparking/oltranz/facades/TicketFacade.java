/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Ticket;
import static java.lang.System.out;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.manzi.frs.config.SessionDataStatus;

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
    
    public List<Ticket> getAllTickets(){
         try{
            Query q= em.createQuery("Select T from Ticket T ORDER BY T.id DESC");
            List<Ticket> list = (List<Ticket>)q.getResultList();
                return list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getCurrentTicket(){
         try{
            Query q= em.createQuery("Select T from Ticket T WHERE T.ticketStatus = :ticketStatus ORDER BY T.id DESC");
            q.setParameter("ticketStatus", SessionDataStatus.ONGOING_STATUS);
            List<Ticket> list = (List<Ticket>)q.getResultList();
                return list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getCurrentTicket(String cDate){
         try{
            Query q= em.createQuery("Select T from Ticket T WHERE T.ticketStatus = :ticketStatus AND T.inDate >= :cDate ORDER BY T.id DESC");
            q.setParameter("ticketStatus", SessionDataStatus.ONGOING_STATUS)
                    .setParameter("cDate", cDate);
            List<Ticket> list = (List<Ticket>)q.getResultList();
                return list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
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
    
    public List<Ticket> getTicketBySessionId(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.sessionId = :sessionId");
            q.setParameter("sessionId", sessionId);
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
            Query q= em.createQuery("Select T from Ticket T WHERE T.msisdn = :msisdn ORDER BY T.id DESC");
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
            Query q= em.createQuery("Select T from Ticket T WHERE T.numberPlate = :numberPlate ORDER BY T.id DESC");
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
    
    public Ticket getSessionLastTicket(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.sessionId = :sessionId ORDER BY T.id DESC");
            q.setParameter("sessionId", sessionId)
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
    
     public Ticket getSessionFirstTicket(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.sessionId = :sessionId ORDER BY T.id ASC");
            q.setParameter("sessionId", sessionId)
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
    
    public List<Ticket> getSessionTickets(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select T from Ticket T WHERE T.sessionId = :sessionId ORDER BY T.id DESC");
            q.setParameter("sessionId", sessionId);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getActiveParkingByDate(Date startDate, Date endDate){
        try{
            Query q= em.createQuery("Select T from Ticket T WHERE T.inDate >= :startDate AND T.inDate <= :endDate GROUP BY T.parkingId ORDER BY T.id DESC");
            q.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Ticket verifyCar(String parkingId, String numberPlate){
        try {
            if(parkingId == null || numberPlate == null){
                out.print(AppDesc.APP_DESC+"TicketFacade verifyCar go empty params");
                return null;
            }
            Query q= em.createQuery("Select T from Ticket T WHERE T.numberPlate = :numberPlate AND T.parkingId = :parkingId ORDER BY T.id DESC");
            q.setParameter("numberPlate", numberPlate)
                    .setParameter("parkingId", parkingId);
            return (Ticket) q.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace(out);
            return null;
        }
    }
    
    public List<Ticket> getTicketPerDate(Date startDate, Date endDate){
        try{
            if(startDate == null || endDate == null){
                out.print(AppDesc.APP_DESC+"TicketFacade getTicketPerDate got an empty date range");
                return null;
            }
            Query q= em.createQuery("Select T from Ticket T WHERE T.inDate >= :startDate AND T.inDate <= :endDate ORDER BY T.id DESC");
            q.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
    
    public Long getParkingCarCount(Date startDate, Date endDate, String parkingId){
        try{
            if(parkingId.isEmpty()){
                out.print(AppDesc.APP_DESC+"TicketFacade getParkingCarCount got an empty parkingId");
                return null;
            }
            Query q= em.createQuery("Select T from Ticket T WHERE (T.inDate BETWEEN :startDate AND :endDate) AND (T.parkingId = :parkingId) GROUP BY T.numberPlate ORDER BY T.id DESC");
            q.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .setParameter("parkingId", parkingId);
            List<Ticket> list = (List<Ticket>)q.getResultList();
            
            Long count = Long.parseLong(list.size()+"");
            out.print(AppDesc.APP_DESC+"TicketFacade getParkingCarCount got a parkingId: "+parkingId+" and was frequented by car:"+count);
            return count;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
}

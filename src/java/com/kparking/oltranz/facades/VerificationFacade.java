/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.facades;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.Verification;
import static java.lang.System.out;
import java.util.Date;
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
public class VerificationFacade extends AbstractFacade<Verification> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VerificationFacade() {
        super(Verification.class);
    }
    public void refreshVerify(){
        em.flush();
    }
    public List<Verification> getTicketPerDate(Date startDate, Date endDate){
        try{
            if(startDate == null || endDate == null){
                out.print(AppDesc.APP_DESC+"TicketFacade getTicketPerDate got an empty date range");
                return null;
            }
            Query q= em.createQuery("Select V from Verification V WHERE V.verifiedOn >= :startDate AND V.verifiedOn <= :endDate ORDER BY V.id DESC");
            q.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            List<Verification> list = (List<Verification>)q.getResultList();
            
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            ex.printStackTrace(out);
            return null;
        }
    }
}

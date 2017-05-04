/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.facades;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.LinkStatus;
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
public class LinkStatusFacade extends AbstractFacade<LinkStatus> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LinkStatusFacade() {
        super(LinkStatus.class);
    }
    public void refreshLink(){
        em.flush();
    }
    public LinkStatus getStatus(int status){
        try {
            Query q= em.createQuery("Select L from LinkStatus L WHERE L.statusCode = :status ORDER BY L.id DESC");
            q.setParameter("status", status);
            List<LinkStatus> list = (List<LinkStatus>)q.getResultList();
            
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"Failed to get status link. "+e.getMessage());
            return null;
        }
    }
}

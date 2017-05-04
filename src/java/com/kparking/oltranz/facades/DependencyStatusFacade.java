/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.kparking.oltranz.facades;

import com.kparking.oltranz.config.AppDesc;
import com.kparking.oltranz.entities.DependencyStatus;
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
public class DependencyStatusFacade extends AbstractFacade<DependencyStatus> {
    
    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public DependencyStatusFacade() {
        super(DependencyStatus.class);
    }
    public void refreshDependency(){em.flush();}
    public List<DependencyStatus> linkStatusDateRange(Date startDate, Date endDate){
        try {
            Query q= em.createQuery("Select D from DependencyStatus D WHERE D.lastAccess >= :startDate AND D.lastAccess <= :endDate ORDER BY D.id DESC");
            q.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            List<DependencyStatus> list = (List<DependencyStatus>)q.getResultList();
            
            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            out.print(AppDesc.APP_DESC+"Faled to get dependency status. "+e.getMessage());
            return null;
        }
    }
}

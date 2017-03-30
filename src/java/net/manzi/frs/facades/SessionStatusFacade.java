/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.manzi.frs.facades;

import com.kparking.oltranz.config.AppDesc;
import static java.lang.System.out;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.manzi.frs.entities.SessionStatus;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class SessionStatusFacade extends AbstractFacade<SessionStatus> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SessionStatusFacade() {
        super(SessionStatus.class);
    }
    
    public void refreshSessionStatus(){
        em.flush();
    }
    
    public SessionStatus getClientLastSession(String msisdn){
        try{
            if(msisdn.isEmpty())
                return null;
            Query q= em.createQuery("Select S from SessionStatus S WHERE S.initTel = :msisdn ORDER BY S.id DESC");
            q.setParameter("msisdn", msisdn)
                    .setMaxResults(1);
            List<SessionStatus> list = (List<SessionStatus>)q.getResultList();
            if(!list.isEmpty())
                return list.get(0);
            else
                return null;
        }catch(Exception ex){
            out.print(AppDesc.APP_DESC+" SessionStatusFacade getClientLastSession failed due to: "+ex.getMessage());
            return null;
        }
    }
    
       public SessionStatus getLastSession(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select S from SessionStatus S WHERE S.sessionId = :sessionId ORDER BY S.id DESC");
            q.setParameter("sessionId", sessionId)
                    .setMaxResults(1);
            List<SessionStatus> list = (List<SessionStatus>)q.getResultList();
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

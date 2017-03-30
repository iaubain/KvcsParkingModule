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
import net.manzi.frs.entities.SessionData;

/**
 *
 * @author ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr /
 * aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662
 */
@Stateless
public class SessionDataFacade extends AbstractFacade<SessionData> {

    @PersistenceContext(unitName = "KvcsParkingGatewayModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SessionDataFacade() {
        super(SessionData.class);
    }
    public void refreshSessiondata(){
        em.flush();
    }
    
    public List<SessionData> getSessionData(String sessionId){
        try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select S from SessionData S WHERE S.sessionId = :sessionId ORDER BY S.id DESC");
            q.setParameter("sessionId", sessionId);
            List<SessionData> list = (List<SessionData>)q.getResultList();            
            return list.isEmpty() ? null : list;
        }catch(Exception ex){
            out.print(AppDesc.APP_DESC+" SessionDataFacade getSessionData failed due to: "+ex.getMessage());
            return null;
        }
    }
    
    public SessionData getBeforeLastSessionData(String sessionId){
         try{
            if(sessionId.isEmpty())
                return null;
            Query q= em.createQuery("Select S from SessionData S WHERE S.sessionId = :sessionId ORDER BY S.id DESC");
            q.setParameter("sessionId", sessionId)
                    .setMaxResults(2);
            List<SessionData> list = (List<SessionData>)q.getResultList();            
           if(!list.isEmpty())
                return list.get(1);
            else
                return null;
        }catch(Exception ex){
            out.print(AppDesc.APP_DESC+" SessionDataFacade getBeforeLastSessionData failed due to: "+ex.getMessage());
            return null;
        }
    }
}

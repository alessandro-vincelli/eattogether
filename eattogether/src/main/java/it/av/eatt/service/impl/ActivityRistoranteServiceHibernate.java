package it.av.eatt.service.impl;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.EaterRelation;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.service.ActivityRistoranteService;
import it.av.eatt.service.UserRelationService;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ActivityRistoranteServiceHibernate extends ApplicationServiceHibernate<ActivityRistorante> implements ActivityRistoranteService{
    
    private UserRelationService userRelationService;
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.ActivityService#findByRistorante(it.av.eatt.ocm.model.Ristorante)
     */
    @Override
    public List<ActivityRistorante> findByRistorante(Ristorante risto) throws JackWicketException {
        Criterion crit = Restrictions.eq(ActivityRistorante.RISTORANTE, risto);
        List<ActivityRistorante> results = findByCriteria(crit);
        return results;
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.ActivityRistoranteService#findByUser(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<ActivityRistorante> findByUser(Eater user) throws JackWicketException {
        Criterion crit = Restrictions.eq(ActivityRistorante.USER, user);
        List<ActivityRistorante> results = findByCriteria(crit);
        return results;
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.ActivityRistoranteService#findByUserFriend(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<ActivityRistorante> findByUserFriend(Eater ofUser) throws JackWicketException {
        List<ActivityRistorante> results = new ArrayList<ActivityRistorante>();
        for (EaterRelation relation : userRelationService.getAllActiveRelations(ofUser)) {
            results.addAll(findByUser(relation.getToUser()));
        }
        return results;
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.ActivityRistoranteService#findByUserAndType(it.av.eatt.ocm.model.Eater, java.lang.String)
     */
    @Override
    public List<ActivityRistorante> findByUserRistoType(Eater user, Ristorante risto, String activityType) throws JackWicketException {
        Criterion critByUser = Restrictions.eq(ActivityRistorante.USER, user);
        Criterion critByRisto = Restrictions.eq(ActivityRistorante.RISTORANTE, risto);
        Criterion critByType = Restrictions.eq(ActivityRistorante.TYPE, activityType);
        List<ActivityRistorante> results = findByCriteria(critByUser, critByType, critByRisto);
        return results;
    }
    
    public ActivityRistorante save(ActivityRistorante activityRistorante) throws JackWicketException{
        return super.save(activityRistorante);
        
    }
    
    public void setUserRelationService(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }
}

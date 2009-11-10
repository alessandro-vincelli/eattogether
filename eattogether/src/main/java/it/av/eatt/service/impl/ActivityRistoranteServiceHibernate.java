package it.av.eatt.service.impl;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Activity;
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.EaterRelation;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.service.ActivityRistoranteService;
import it.av.eatt.service.UserRelationService;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ActivityRistoranteServiceHibernate extends ApplicationServiceHibernate<ActivityRistorante> implements ActivityRistoranteService{
    
    private UserRelationService userRelationService;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityRistorante> findByRistorante(Ristorante risto) throws JackWicketException {
        Criterion crit = Restrictions.eq(ActivityRistorante.RISTORANTE, risto);
        List<ActivityRistorante> results = findByCriteria(crit);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityRistorante> findByUser(Eater user) throws JackWicketException {
        Criterion crit = Restrictions.eq(ActivityRistorante.USER, user);
        Order orderByDate = Order.desc(Activity.DATE);
        List<ActivityRistorante> results = findByCriteria(orderByDate, crit);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityRistorante> findByUserFriend(Eater ofUser) throws JackWicketException {
        List<ActivityRistorante> results = new ArrayList<ActivityRistorante>();
        for (EaterRelation relation : userRelationService.getAllActiveRelations(ofUser)) {
            results.addAll(findByUser(relation.getToUser()));
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityRistorante> findByUserRistoType(Eater user, Ristorante risto, String activityType) throws JackWicketException {
        Criterion critByUser = Restrictions.eq(ActivityRistorante.USER, user);
        Criterion critByRisto = Restrictions.eq(ActivityRistorante.RISTORANTE, risto);
        Criterion critByType = Restrictions.eq(ActivityRistorante.TYPE, activityType);
        List<ActivityRistorante> results = findByCriteria(critByUser, critByType, critByRisto);
        return results;
    }
    
    /**
     * {@inheritDoc}
     */
    public ActivityRistorante save(ActivityRistorante activityRistorante) throws JackWicketException{
        return super.save(activityRistorante); 
    }
    
    public void setUserRelationService(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }
}

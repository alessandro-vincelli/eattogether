package it.av.eatt.service;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.Ristorante;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface ActivityRistoranteService extends ApplicationService<ActivityRistorante> {
    /**
     * Find activities on the given restaurant
     * 
     * @param risto
     * @return activities on the given restaurant
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByRistorante(Ristorante risto) throws JackWicketException;
    
    /**
     * Find activities on ristoranti for the given user
     * 
     * @param user
     * @return activities for the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUser(Eater user) throws JackWicketException;
    
    /**
     * Find activities on ristoranti for the given user
     * 
     * @param user
     * @param firstResult first result
     * @param maxResults number of results
     * @return activities for the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUser(Eater user, int firstResult ,int maxResults) throws JackWicketException;

    /**
     * Find activities on ristoranti for the given user and activityType
     * see {@link ActivityRistorante} for the list of type
     * 
     * @param user
     * @param risto
     * @param activityType
     * @return activities for the given user
     * @throws JackWicketException
     */
    List<ActivityRistorante> findByUserRistoType(Eater user, Ristorante risto, String activityType) throws JackWicketException;
    
    /**
     * Find activities on restaurants for the friends given user
     * 
     * @param ofUser
     * @return activities for the friends of the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUserFriend(Eater ofUser) throws JackWicketException;
    
    /**
     * Find activities on restaurants for the friends given user
     * 
     * @param ofUser
     * @param firstResult first result
     * @param maxResults number of results
     * @return activities for the friends of the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUserFriend(Eater ofUser, int firstResult ,int maxResults) throws JackWicketException;
    
    /**
     * Find activities on ristoranti for the given users
     * 
     * @param users
     * @param firstResult first result
     * @param maxResults number of results
     * @return activities for the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUsers(List<Eater> users, int firstResult ,int maxResults) throws JackWicketException;
    
    ActivityRistorante save(ActivityRistorante activityRistorante) throws JackWicketException;
}

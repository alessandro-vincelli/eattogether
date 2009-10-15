package it.av.eatt.service;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.User;

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
    List<ActivityRistorante> findByUser(User user) throws JackWicketException;

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
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUserRistoType(User user, Ristorante risto, String activityType) throws JackWicketException;
    
    /**
     * Find activities on restaurants for the friends given user
     * 
     * @param ofUser
     * @return activities for the friends of the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<ActivityRistorante> findByUserFriend(User ofUser) throws JackWicketException;
}

/**
 * Copyright 2009 the original author or authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.av.eatt.service.impl;

import it.av.eatt.JackWicketException;
import it.av.eatt.JackWicketRunTimeException;
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.RateOnRistorante;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.RistoranteRevision;
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.ocm.model.TagOnRistorante;
import it.av.eatt.ocm.util.DateUtil;
import it.av.eatt.service.ActivityRistoranteService;
import it.av.eatt.service.RateRistoranteService;
import it.av.eatt.service.RistoranteRevisionService;
import it.av.eatt.service.RistoranteService;
import it.av.eatt.service.TagOnRistoranteService;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class RistoranteServiceHibernate extends ApplicationServiceHibernate<Ristorante> implements RistoranteService {

    private ActivityRistoranteService activityRistoranteService;
    private TagOnRistoranteService tagOnRistoranteService;
    @Autowired
    private RistoranteRevisionService ristoranteRevisionService;
    @Autowired
    private RateRistoranteService rateRistoranteService;
    
    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#update(it.av.eatt.ocm.model.Ristorante, it.av.eatt.ocm.model.Eater)
     */
    @Override
    public Ristorante update(Ristorante risto, Eater user) throws JackWicketException {
        risto.setModificationTime(DateUtil.getTimestamp());
        risto.setVersionNumber(risto.getVersionNumber() + 1);
        super.save(risto);
        risto.addRevision(ristoranteRevisionService.insert(new RistoranteRevision(risto)));
        risto.addActivity(activityRistoranteService.save(new ActivityRistorante(DateUtil.getTimestamp(), user, risto, ActivityRistorante.TYPE_MODIFICATION)));
        return save(risto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#insert(it.av.eatt.ocm.model.Ristorante, it.av.eatt.ocm.model.Eater)
     */
    @Override
    public Ristorante insert(Ristorante risto, Eater user) throws JackWicketException {
        risto.setCreationTime(DateUtil.getTimestamp());
        risto.setModificationTime(DateUtil.getTimestamp());
        risto.setVersionNumber(1);
        super.save(risto);
        risto.addRevision(ristoranteRevisionService.insert(new RistoranteRevision(risto)));
        risto.addActivity(activityRistoranteService.save(new ActivityRistorante(DateUtil.getTimestamp(), user, risto, ActivityRistorante.TYPE_ADDED)));
        return super.save(risto);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.av.eatt.service.RistoranteService#addRate(it.av.eatt.ocm.model.Ristorante , it.av.eatt.ocm.model.Eater, java.lang.String)
	 */
	@Override
	public Ristorante addRate(Ristorante risto, Eater user, int rate) throws JackWicketException {
		if (user != null && risto != null && !(hasUsersAlreadyRated(risto, user))) {
			if (rate >= 0 && rate <= 5) {
				ActivityRistorante activity = activityRistoranteService.save(new ActivityRistorante(user, risto, ActivityRistorante.TYPE_VOTED));
				risto.addARate(rateRistoranteService.insert(new RateOnRistorante(rate, activity)));
				return save(risto);
			} else {
				throw new JackWicketRunTimeException("Rate non valid");
			}
		} else {
			throw new JackWicketRunTimeException("Probably the given user has already voted on the given restaurant");
		}
	}

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#addTag(it.av.eatt.ocm.model.Ristorante, it.av.eatt.ocm.model.Eater, java.lang.String)
     */
    @Override
    public Ristorante addTag(Ristorante risto, Eater user, String tag) throws JackWicketException {
        List<TagOnRistorante> tags = tagOnRistoranteService.find(tag, risto);
        if (tags.size() == 0) {
            risto.addTag(tagOnRistoranteService.insert(tag));
            save(risto);
        } else {
            // tag already presents, don't do anything
        }
        return risto;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#removeTag(it.av.eatt.ocm.model.Ristorante, it.av.eatt.ocm.model.Eater, java.lang.String)
     */
    @Override
    public void removeTag(TagOnRistorante tag) throws JackWicketException {
        tagOnRistoranteService.remove(tag);
    }

    @Override
    public boolean hasUsersAlreadyRated(Ristorante risto, Eater user) throws JackWicketException {
        List<ActivityRistorante> results = activityRistoranteService.findByUserRistoType(user, risto, ActivityRistorante.TYPE_VOTED);
        if (results.size() == 0) {
            return false;
        } else
            return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#find(java.lang.String)
     */
    @Override
    public Collection<Ristorante> find(String pattern) throws JackWicketException {
        Criterion critByName = Restrictions.ilike(Ristorante.NAME, pattern);
        List<Ristorante> results = findByCriteria(critByName);
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.impl.ApplicationServiceHibernate#remove(java.lang.Object)
     */
    @Override
    public void remove(Ristorante object) throws JackWicketException {
//        for (ActivityRistorante activityRistorante : object.getActivities()) {
//            getJpaTemplate().remove(activityRistorante);
//        }
//        for (TagOnRistorante tag : object.getTags()) {
//            getJpaTemplate().remove(tag);
//        }
//        for (RateOnRistorante rate : object.getRates()) {
//            getJpaTemplate().remove(rate);
//        }
//        for(RistoranteRevision revision :object.getRevisions()){
//            getJpaTemplate().remove(revision);
//        }
        //getJpaTemplate().flush();
        super.remove(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.RistoranteService#findTagsNotLinked(java.lang.String, it.av.eatt.ocm.model.Ristorante)
     */
    @Override
    public List<Tag> findTagsNotLinked(String pettern, Ristorante risto) throws JackWicketException {
        return tagOnRistoranteService.findTagsNotLinked(pettern, risto);
    }

    /**
     * @param activityRistoranteService
     */
    public void setActivityRistoranteService(ActivityRistoranteService activityRistoranteService) {
        this.activityRistoranteService = activityRistoranteService;
    }

    /**
     * @param tagOnRistoranteService
     */
    public void setTagOnRistoranteService(TagOnRistoranteService tagOnRistoranteService) {
        this.tagOnRistoranteService = tagOnRistoranteService;
    }
}
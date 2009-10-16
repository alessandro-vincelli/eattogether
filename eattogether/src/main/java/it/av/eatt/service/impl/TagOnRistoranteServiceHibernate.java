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
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.ocm.model.TagOnRistorante;
import it.av.eatt.service.TagOnRistoranteService;
import it.av.eatt.service.TagService;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@Transactional
public class TagOnRistoranteServiceHibernate extends ApplicationServiceHibernate<TagOnRistorante> implements TagOnRistoranteService {

    private TagService tagService;

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.TagOnRistoranteService#insert(it.av.eatt.ocm.model.Tag, it.av.eatt.ocm.model.ActivityRistorante)
     */
    @Override
    public TagOnRistorante insert(Tag tag, Ristorante ristorante) throws JackWicketException {
        return super.save(new TagOnRistorante(tag));
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.TagOnRistoranteService#insert(java.lang.String, it.av.eatt.ocm.model.ActivityRistorante)
     */
    @Override
    public TagOnRistorante insert(String tag) throws JackWicketException {
        return super.save(new TagOnRistorante(tagService.insert(tag)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.TagOnRistoranteService#find(java.lang.String, it.av.eatt.ocm.model.Ristorante)
     */
    @Override
    public List<TagOnRistorante> find(String tag, Ristorante risto) throws JackWicketException {
        Query query = getJpaTemplate().getEntityManager().createQuery("select tag from Ristorante as risto join risto.tags as tag where risto = :risto and tag.tag.tag = :tag");
        query.setParameter("risto", risto);
        query.setParameter("tag", tag);
        return query.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.TagOnRistoranteService#find(java.lang.String)
     */
    @Override
    public List<TagOnRistorante> find(String pattern) throws JackWicketException {
        Tag tagItem = tagService.getByTagValue(pattern);
        if (tagItem != null) {
            Criterion critOnTag = Restrictions.ilike(TagOnRistorante.TAG, tagItem);
            return findByCriteria(critOnTag);
        } else {
            // no tag with this value, means also no TagOnRistorante, query skipped
            return new ArrayList<TagOnRistorante>(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.service.TagOnRistoranteService#findTagsNotLinked(java.lang.String, it.av.eatt.ocm.model.Ristorante)
     */
    @Override
    public List<Tag> findTagsNotLinked(String pattern, Ristorante risto) throws JackWicketException {

        List<TagOnRistorante> alreadyLinkedTagsOnRisto = find(pattern, risto);
        List<Long> alreadyLinkedTags = new ArrayList<Long>(alreadyLinkedTagsOnRisto.size());
        for (TagOnRistorante tagOnRistorante : alreadyLinkedTagsOnRisto) {
            alreadyLinkedTags.add(tagOnRistorante.getTag().getId());
        }
        
        if(alreadyLinkedTags.size() >0){
            Criterion crit = Restrictions.not(Restrictions.in("id", alreadyLinkedTags));
            return tagService.findByCriteria(crit);
        }
        else{
            return tagService.getAll();
        }

    }

}

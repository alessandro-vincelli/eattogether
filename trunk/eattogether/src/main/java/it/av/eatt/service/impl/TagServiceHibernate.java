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
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.service.TagService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class TagServiceHibernate extends ApplicationServiceHibernate<Tag> implements TagService {
    @Override
    public Tag insert(String tag) throws JackWicketException {
        Tag result = getByTagValue(tag); 
        if (result != null) {
            return result;
        } else {
            return super.save(new Tag(tag));
        }
    }

    @Override
    public List<Tag> find(String query) throws JackWicketException {
        Criterion crit = Restrictions.ilike(Tag.TAG, query);
        List<Tag> results = findByCriteria(crit);
        return results;
    }

    @Override
    public Tag getByTagValue(String tagValue) throws JackWicketException {
        Criterion crit = Restrictions.eq(Tag.TAG, tagValue);
        List<Tag> results = findByCriteria(crit);
        if(results.size() == 1){
            return results.get(0);
        }
        else{
            return null;
        }
    }
    
}

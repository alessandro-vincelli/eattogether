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
package it.av.eatt.service;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Activity;
import it.av.eatt.ocm.model.User;

import java.util.Collection;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Operations on {@Link Activity}
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
@Transactional
public interface ActivityService extends ApplicationService<Activity>{
    
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#save(java.lang.Object)
     */
    @Transactional
    Activity save(Activity object) throws JackWicketException;
    /**
     * Return the activity on the given date  
     * 
     * @param date
     * @return activities on the given date
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Activity> findByDate(Date date) throws JackWicketException;
    
    /**
     * Find activities of the given user
     * 
     * @param user
     * @return activities for the given user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Activity> findByUser(User user) throws JackWicketException;

    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#remove(java.lang.Object)
     */
    @Transactional
    void remove(Activity object) throws JackWicketException;

    
}
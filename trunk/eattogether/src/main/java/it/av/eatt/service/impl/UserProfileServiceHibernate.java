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
import it.av.eatt.ocm.model.UserProfile;
import it.av.eatt.service.UserProfileService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class UserProfileServiceHibernate extends ApplicationServiceHibernate<UserProfile> implements UserProfileService{

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserProfileService#getRegolarUserProfile()
     */
    @Override
    public UserProfile getRegolarUserProfile() throws JackWicketException {
        return getByName("USER");
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserProfileService#getByName(java.lang.String)
     */
    @Override
    public UserProfile getByName(String name) {
        Criterion crit = Restrictions.eq(UserProfile.NAME, name);
        List<UserProfile> result = super.findByCriteria(crit);
        if(result != null && result.size() > 0){
            return result.get(0);
        }
        else{
            return null;
        }
    }
}

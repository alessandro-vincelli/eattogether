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
import it.av.eatt.ocm.model.EaterProfile;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Services on the Eater Profile
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
public interface UserProfileService {
    /**
     * Save a EaterProfile
     * 
     * @param object
     * @return just saved profile
     * @throws JackWicketException
     */
    @Transactional
    EaterProfile save(EaterProfile object) throws JackWicketException;
    /**
     * Get all the user profile
     * 
     * @return all the user profile
     * @throws JackWicketException
     */
    @Transactional(readOnly=true)
    Collection<EaterProfile> getAll() throws JackWicketException;

    @Transactional
    void remove(EaterProfile object) throws JackWicketException;
    /**
     * Return the regular user profile, it must be the "USER" profile
     * 
     * @return EaterProfile
     * @throws JackWicketException
     */
    EaterProfile getRegolarUserProfile() throws JackWicketException;
    /**
     * Return the user with the passed name, there's a unique constraint on the user profile name
     * 
     * @param id
     * @return the user with the given name
     */
    @Transactional(readOnly=true)
    EaterProfile getByName(String id);

}
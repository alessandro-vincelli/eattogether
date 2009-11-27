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
import it.av.eatt.ocm.model.Eater;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Operation in user/s
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@Service
public interface EaterService {

    /**
     * Update a user
     * 
     * @param object
     * @return just updated user
     * @throws JackWicketException
     */
    @Transactional
    Eater update(Eater object) throws JackWicketException;

    /**
     * Add a new user, if the role is empty, it's used the USER role
     * 
     * @param object
     * @return just added user
     * @throws JackWicketException
     */
    @Transactional
    Eater add(Eater object) throws JackWicketException;

    /**
     * Insert a new user, during the insert is also encrypted the users's password
     * 
     * @param object
     * @return just inserted user
     * @throws JackWicketException
     */
    @Transactional
    Eater addRegolarUser(Eater object) throws JackWicketException;

    /**
     * Return all the users
     * 
     * @return all the users
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Eater> getAll() throws JackWicketException;

    /**
     * Search users
     * 
     * @param pattern
     * @return the found users
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Eater> find(String pattern) throws JackWicketException;

    /**
     * Return users without relations with the passed user and the given pattern the search is performed on the
     * firstname and lastname of the user
     * 
     * @param forUser
     * @param pattern
     * @return all found users
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Eater> findUserWithoutRelation(Eater forUser, String pattern) throws JackWicketException;

    /**
     * Return users without relations with the passed user
     * 
     * @param forUser
     * @return all users not related to this user
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<Eater> findUserWithoutRelation(Eater forUser) throws JackWicketException;

    /**
     * Remove the user
     * 
     * @param user
     * @throws JackWicketException
     */
    @Transactional
    void remove(Eater user) throws JackWicketException;

    /**
     * Return the user with this email, there is an unique constraint on the user email
     * 
     * @param email
     * @return user with the passed email
     */
    @Transactional(readOnly = true)
    Eater getByEmail(String email);

    /**
     * Return the user by id
     * 
     * @param id
     * @return user with the passed email
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Eater getByID(String id) throws JackWicketException;
}
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
import it.av.eatt.ocm.model.User;
import it.av.eatt.ocm.model.UserRelation;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Operations to manage releations between users
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
@Transactional
public interface UserRelationService {

    /**
     * Save a realtion
     * 
     * @param relation
     * @return UserRelation just saved
     * @throws JackWicketException
     */
    @Transactional
    UserRelation save(UserRelation relation) throws JackWicketException;
    
    /**
     * Remove a realation
     * 
     * @param object
     * @throws JackWicketException
     */
    @Transactional(propagation=Propagation.MANDATORY)
    void remove(UserRelation object) throws JackWicketException;

    /**
     * Create a follow realation
     * 
     * @param fromUser
     * @param toUser
     * @return UserRelation, just new relation
     * @throws JackWicketException
     */
    @Transactional
    UserRelation addFollowUser(User fromUser, User toUser) throws JackWicketException;

    /**
     * Create a friend realation with {@link UserRelation} with STATUS_PENDING
     * 
     * @param fromUser
     * @param toUser
     * @return UserRelation, just new relation
     * @throws JackWicketException
     */
    @Transactional
    UserRelation addFriendRequest(User fromUser, User toUser) throws JackWicketException;

    /**
     * Update the relation to status STATUS_CONFIRMED
     * 
     * @param relation
     * @return UserRelation, just updated relation
     * @throws JackWicketException
     */
    @Transactional
    UserRelation performFriendRequestConfirm(UserRelation relation) throws JackWicketException;

    /**
     * Update the relation to status STATUS_IGNORE
     * 
     * @param relation
     * @return UserRelation, just updated relation
     * @throws JackWicketException
     */
    @Transactional
    UserRelation performFriendRequestIgnore(UserRelation relation) throws JackWicketException;

    /**
     * Return all the friends relations of the passed user
     * 
     * @param ofUser
     * @return list of all al frieds realations
     */
    @Transactional(readOnly = true)
    List<UserRelation> getAllFriendUsers(User ofUser);

    /**
     * Return all the follow relations of the passed user
     * 
     * @param ofUser
     * @return list of all al frieds realations
     */
    @Transactional(readOnly = true)
    List<UserRelation> getAllFollowUsers(User ofUser);
    
    /**
     * Return all the relations of the passed user, are included also the pending relations
     * 
     * @param ofUser
     * @return list of all al frieds realations
     */
    @Transactional(readOnly = true)
    List<UserRelation> getAllRelations(User ofUser);
 
    /**
     * Return all the active relations of the passed user
     * 
     * @param ofUser
     * @return list of all al frieds realations
     */
    @Transactional(readOnly = true)
    List<UserRelation> getAllActiveRelations(User ofUser);
}

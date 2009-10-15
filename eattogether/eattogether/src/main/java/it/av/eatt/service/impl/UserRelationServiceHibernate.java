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
import it.av.eatt.ocm.model.User;
import it.av.eatt.ocm.model.UserRelation;
import it.av.eatt.service.UserRelationService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class UserRelationServiceHibernate extends ApplicationServiceHibernate<UserRelation> implements UserRelationService{
    

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#addFollowUser(it.av.eatt.ocm.model.User, it.av.eatt.ocm.model.User)
     */
    @Override
    public UserRelation addFollowUser(User fromUser, User toUser) throws JackWicketException {
        UserRelation relation = UserRelation.createFollowRelation(fromUser, toUser);
        return save(relation);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#addFriendRequest(it.av.eatt.ocm.model.User, it.av.eatt.ocm.model.User)
     */
    @Override
    public UserRelation addFriendRequest(User fromUser, User toUser) throws JackWicketException {
        UserRelation relation = UserRelation.createFriendRelation(fromUser, toUser);
        return save(relation);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#performFriendRequestConfirm(it.av.eatt.ocm.model.UserRelation)
     */
    @Override
    public UserRelation performFriendRequestConfirm(UserRelation relation) throws JackWicketException {
        if(relation != null && relation.getStatus().equals(UserRelation.STATUS_PENDING) && relation.getType().equals(UserRelation.TYPE_FRIEND)){
            relation.setStatus(UserRelation.STATUS_ACTIVE);
            return save(relation);    
        }
        else{
            throw new JackWicketException("Relation cannot be updated");
        }
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#performFriendRequestIgnore(it.av.eatt.ocm.model.UserRelation)
     */
    @Override
    public UserRelation performFriendRequestIgnore(UserRelation relation) throws JackWicketException {
        if(relation != null && relation.getStatus().equals(UserRelation.STATUS_PENDING) && relation.getType().equals(UserRelation.TYPE_FRIEND)){
            relation.setStatus(UserRelation.STATUS_IGNORED);
            return save(relation);    
        }
        else{
            throw new JackWicketException("Relation cannot be updated");
        }
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllFollowUsers(it.av.eatt.ocm.model.User)
     */
    @Override
    public List<UserRelation> getAllFollowUsers(User ofUser) {
        Criterion critUser = Restrictions.eq(UserRelation.FROM_USER, ofUser);
        Criterion critType = Restrictions.eq(UserRelation.TYPE, UserRelation.TYPE_FOLLOW);
        Criterion critStatus = Restrictions.eq(UserRelation.STATUS, UserRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critType, critStatus);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllFriendUsers(it.av.eatt.ocm.model.User)
     */
    @Override
    public List<UserRelation> getAllFriendUsers(User ofUser) {
        Criterion critUser = Restrictions.eq(UserRelation.FROM_USER, ofUser);
        Criterion critType = Restrictions.eq(UserRelation.TYPE, UserRelation.TYPE_FRIEND);
        Criterion critStatus = Restrictions.eq(UserRelation.STATUS, UserRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critType, critStatus);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllRelatedUsers(it.av.eatt.ocm.model.User)
     */
    @Override
    public List<UserRelation> getAllRelations(User ofUser) {
        Criterion critUser = Restrictions.eq(UserRelation.FROM_USER, ofUser);
        return findByCriteria(critUser);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllActiveRelations(it.av.eatt.ocm.model.User)
     */
    @Override
    public List<UserRelation> getAllActiveRelations(User ofUser) {
        Criterion critUser = Restrictions.eq(UserRelation.FROM_USER, ofUser);
        Criterion critRelationActive = Restrictions.eq(UserRelation.STATUS, UserRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critRelationActive);
    }
}

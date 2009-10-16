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
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.EaterRelation;
import it.av.eatt.service.UserRelationService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class UserRelationServiceHibernate extends ApplicationServiceHibernate<EaterRelation> implements UserRelationService{
    

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#addFollowUser(it.av.eatt.ocm.model.Eater, it.av.eatt.ocm.model.Eater)
     */
    @Override
    public EaterRelation addFollowUser(Eater fromUser, Eater toUser) throws JackWicketException {
        EaterRelation relation = EaterRelation.createFollowRelation(fromUser, toUser);
        return save(relation);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#addFriendRequest(it.av.eatt.ocm.model.Eater, it.av.eatt.ocm.model.Eater)
     */
    @Override
    public EaterRelation addFriendRequest(Eater fromUser, Eater toUser) throws JackWicketException {
        EaterRelation relation = EaterRelation.createFriendRelation(fromUser, toUser);
        return save(relation);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#performFriendRequestConfirm(it.av.eatt.ocm.model.EaterRelation)
     */
    @Override
    public EaterRelation performFriendRequestConfirm(EaterRelation relation) throws JackWicketException {
        if(relation != null && relation.getStatus().equals(EaterRelation.STATUS_PENDING) && relation.getType().equals(EaterRelation.TYPE_FRIEND)){
            relation.setStatus(EaterRelation.STATUS_ACTIVE);
            return save(relation);    
        }
        else{
            throw new JackWicketException("Relation cannot be updated");
        }
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#performFriendRequestIgnore(it.av.eatt.ocm.model.EaterRelation)
     */
    @Override
    public EaterRelation performFriendRequestIgnore(EaterRelation relation) throws JackWicketException {
        if(relation != null && relation.getStatus().equals(EaterRelation.STATUS_PENDING) && relation.getType().equals(EaterRelation.TYPE_FRIEND)){
            relation.setStatus(EaterRelation.STATUS_IGNORED);
            return save(relation);    
        }
        else{
            throw new JackWicketException("Relation cannot be updated");
        }
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllFollowUsers(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<EaterRelation> getAllFollowUsers(Eater ofUser) {
        Criterion critUser = Restrictions.eq(EaterRelation.FROM_USER, ofUser);
        Criterion critType = Restrictions.eq(EaterRelation.TYPE, EaterRelation.TYPE_FOLLOW);
        Criterion critStatus = Restrictions.eq(EaterRelation.STATUS, EaterRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critType, critStatus);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllFriendUsers(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<EaterRelation> getAllFriendUsers(Eater ofUser) {
        Criterion critUser = Restrictions.eq(EaterRelation.FROM_USER, ofUser);
        Criterion critType = Restrictions.eq(EaterRelation.TYPE, EaterRelation.TYPE_FRIEND);
        Criterion critStatus = Restrictions.eq(EaterRelation.STATUS, EaterRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critType, critStatus);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllRelatedUsers(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<EaterRelation> getAllRelations(Eater ofUser) {
        Criterion critUser = Restrictions.eq(EaterRelation.FROM_USER, ofUser);
        return findByCriteria(critUser);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserRelationService#getAllActiveRelations(it.av.eatt.ocm.model.Eater)
     */
    @Override
    public List<EaterRelation> getAllActiveRelations(Eater ofUser) {
        Criterion critUser = Restrictions.eq(EaterRelation.FROM_USER, ofUser);
        Criterion critRelationActive = Restrictions.eq(EaterRelation.STATUS, EaterRelation.STATUS_ACTIVE);
        return findByCriteria(critUser, critRelationActive);
    }
}

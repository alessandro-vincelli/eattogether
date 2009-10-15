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
import it.av.eatt.UserAlreadyExistsException;
import it.av.eatt.ocm.model.User;
import it.av.eatt.ocm.model.UserRelation;
import it.av.eatt.service.UserProfileService;
import it.av.eatt.service.UserRelationService;
import it.av.eatt.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.dao.DataAccessException;

/**
 * 
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class UserServiceHibernate extends ApplicationServiceHibernate<User> implements UserService{
    
    private StrongPasswordEncryptor passwordEncoder;
    private UserProfileService userProfileService;
    private UserRelationService userRelationService;
    
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#creteRegolarUser(it.av.eatt.ocm.model.User)
     */
    @Override
    public User addRegolarUser(User object) throws JackWicketException {
        object.setUserProfile(userProfileService.getRegolarUserProfile());
        try {
	        return add(object);
        } catch (ConstraintViolationException e) {
	        throw new UserAlreadyExistsException(e.getMessage());
        }
    }
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#add(it.av.eatt.ocm.model.User)
     */
    @Override
    public User add(User object) throws JackWicketException {
        if(object == null || StringUtils.isBlank(object.getEmail())){
            throw new JackWicketException("User is null or email is empty");
        }
        object.setPassword(passwordEncoder.encryptPassword(object.getPassword()));
        if(object.getUserProfile() == null){
            object.setUserProfile(userProfileService.getRegolarUserProfile());
        }
        return update(object);
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#update(it.av.eatt.ocm.model.User)
     */
    @Override
    public User update(User object) throws JackWicketException {
        try {
            super.save(object);
        } catch (DataAccessException e) {
            throw new JackWicketException(e);
        }
        return object;
    }
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#getByEmail(java.lang.String)
     */
    @Override
    public User getByEmail(String email) {
        Criterion crit = Restrictions.eq(User.EMAIL, email);
        List<User> result = super.findByCriteria(crit);
        if(result != null && result.size() == 1){
            return result.get(0);
        }
        else{
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#findUserWithoutRelation(it.av.eatt.ocm.model.User)
     */
    @Override
    public Collection<User> findUserWithoutRelation(User forUser, String pattern) throws JackWicketException {
        //TODO can be improved with an outer join
        Collection<UserRelation> relatedUser = userRelationService.getAllRelations(forUser);
        ArrayList<Long> relatedUserId = new ArrayList<Long>(relatedUser.size());
        for (UserRelation userRelation : relatedUser) {
            relatedUserId.add(userRelation.getToUser().getId());
        }
        List<Criterion> criterions = new ArrayList<Criterion>(3);
        if(StringUtils.isNotBlank(pattern)){
            criterions.add(Restrictions.or(Restrictions.ilike(User.FIRSTNAME, pattern), Restrictions.ilike(User.LASTNAME, pattern)));    
        }
        if(relatedUserId.size() > 0){
            criterions.add(Restrictions.not(Restrictions.in(User.ID, relatedUserId)));    
        }
        //escludes the forUser from the search
        criterions.add(Restrictions.not(Restrictions.idEq(forUser.getId())));
        List<User> results = super.findByCriteria(criterions.toArray(new Criterion[criterions.size()]));
        return results;
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.UserService#findUserWithoutRelation(it.av.eatt.ocm.model.User)
     */
    @Override
    public Collection<User> findUserWithoutRelation(User forUser) throws JackWicketException {
        return findUserWithoutRelation(forUser, null);
    }

    @Override
    public void remove(User user) throws JackWicketException {
        super.remove(user);
    }

    public void setPasswordEncoder(StrongPasswordEncryptor passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public void setUserRelationService(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }

    @Override
    public Collection<User> find(String pattern) throws JackWicketException {
        Criterion critByName = Restrictions.ilike(User.LASTNAME, pattern);
        List<User> results = findByCriteria(critByName);
        return results;
    }

}

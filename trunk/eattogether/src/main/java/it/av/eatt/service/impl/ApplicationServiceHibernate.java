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
import it.av.eatt.ocm.model.BasicEntity;
import it.av.eatt.service.ApplicationService;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 * @param <T>
 */
public class ApplicationServiceHibernate<T extends BasicEntity> extends JpaDaoSupport implements ApplicationService<T> {
    
    /**
     * 
     * @param entityManager
     */
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    public void setInternalEntityManager(final EntityManager entityManager) {
        setEntityManager(entityManager);
    }
    

    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#save(java.lang.Object)
     */
    public T save(T obj) throws JackWicketException {
        if (obj == null) {
            throw new JackWicketException("Object is null");
        }
        try {
            if (obj.getId() != 0) {
                getJpaTemplate().merge(obj);

            } else {
                getJpaTemplate().persist(obj);
            }
            return obj;
        } catch (DataAccessException e) {
            throw new JackWicketException(e);
        }
    }
    

    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#getAll()
     */
    @Transactional(readOnly=true)
    public List<T> getAll() throws JackWicketException {
        return findByCriteria();
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#find(java.lang.String)
     */
    @Transactional(readOnly=true)
    @Deprecated
    public List<T> findFullText(String query) throws JackWicketException {
/*        //DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        //return getHibernateTemplate().findByCriteria(criteria, 0, -1);
        //TODO implement a default search strategy
        FullTextSession fullTextSession = Search.getFullTextSession(getSession(false));
        org.apache.lucene.queryParser.QueryParser parser = new QueryParser("tag", new StopAnalyzer() );

        org.apache.lucene.search.Query luceneQuery;
        try {
            String[] ddd = new String[1];
            ddd[0] = "tag";
            parser = new MultiFieldQueryParser( ddd, new StandardAnalyzer());
            Term t = new Term("tag", "*tag1*");
            TermQuery query2 = new TermQuery(t);
            parser.setAllowLeadingWildcard( true );
            Query queryinutile= parser.parse(query);
            org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery( query2, getPersistentClass() );
            
            List<T> result = fullTextQuery.list();
            return result;
        } catch (ParseException e) {
            throw new JackWicketException(e);
        }*/
        throw new JackWicketException("not implememted yet");
    }

    /* (non-Javadoc)
     * @see it.av.eatt.service.ApplicationService#remove(java.lang.Object)
     */
    public void remove(T object) throws JackWicketException {
        try {
            getJpaTemplate().remove(object);
            //getJpaTemplate().flush();
        } catch (DataAccessException e) {
            throw new JackWicketException(e);
        }
    }

    public Class<T> getPersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public List<T> findByCriteria(Criterion... criterion) {
        return findByCriteria(getPersistentClass(), null, criterion);
    }

    protected List<T> findByCriteria(Order order, Criterion... criterion) {
        return findByCriteria(getPersistentClass(), order, criterion);
    }

    public List<T> findByCriteria(Class actualClass, Order order, Criterion... criterion) {
        //DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        Criteria criteria = getHibernateSession().createCriteria(getPersistentClass());
        if (order != null)
            criteria.addOrder(order);

        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return criteria.list();
        /*Criteria crit = getSession().createCriteria(actualClass);
        if (order != null)
            crit.addOrder(order);

        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();*/
    }

    @Override
    public T getByID(Long id) throws JackWicketException {
        Criterion crit = Restrictions.idEq(id);
        return findByCriteria(crit).iterator().next();
    }
    
    protected Session getHibernateSession() {
        return (Session) getJpaTemplate().getEntityManager().getDelegate();
    }

}

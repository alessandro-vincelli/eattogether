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
import it.av.eatt.ocm.model.DataRistorante;
import it.av.eatt.ocm.model.ProvIta;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.service.DataRistoranteService;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the operation on {@link DataRistorante}
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class DataRistoranteServiceHibernate extends ApplicationServiceHibernate<DataRistorante> implements
        DataRistoranteService {

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRistorante update(DataRistorante risto) throws JackWicketException {
        super.save(risto);
        getJpaTemplate().flush();
        return risto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRistorante insert(DataRistorante risto) throws JackWicketException {
        super.save(risto);
        return risto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRistorante> find(String pattern) throws JackWicketException {
        Criterion critByName = Restrictions.ilike(Ristorante.NAME, "%" + pattern + "%");
        List<DataRistorante> results = findByCriteria(critByName);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(DataRistorante object) throws JackWicketException {
        super.remove(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProvIta> getAllProv() throws JackWicketException {
        Criteria criteria = getHibernateSession().createCriteria(ProvIta.class);
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRistorante> find(String pattern, String city, String country) throws JackWicketException {
        Criterion critByName = Restrictions.ilike(Ristorante.NAME, "%" + pattern + "%");
        Criterion critByCity = Restrictions.eq(Ristorante.CITY, city);
        Criterion critByCountry = Restrictions.eq(Ristorante.COUNTRY, country);
        List<DataRistorante> results = findByCriteria(critByName, critByCity, critByCountry);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataRistorante> getBy(String pattern, String city, String country) throws JackWicketException {
        Criterion critByName = Restrictions.eq(Ristorante.NAME, pattern);
        Criterion critByCity = Restrictions.eq(Ristorante.CITY, city);
        Criterion critByCountry = Restrictions.eq(Ristorante.COUNTRY, country);
        List<DataRistorante> results = findByCriteria(critByName, critByCity, critByCountry);
        return results;
    }

}

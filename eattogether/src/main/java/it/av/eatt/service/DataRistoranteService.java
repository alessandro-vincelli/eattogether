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
import it.av.eatt.ocm.model.DataRistorante;
import it.av.eatt.ocm.model.ProvIta;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Services on {@link DataRistorante}
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@Service
@Repository
public interface DataRistoranteService {

    /**
     * Update a restaurant
     * 
     * @param risto the Risto to be modified
     * @return DataRistorante
     * @throws JackWicketException
     */
    @Transactional
    DataRistorante update(DataRistorante risto) throws JackWicketException;

    /**
     * Insert a new restaurant
     * 
     * @param risto the Risto to be inserted
     * @return DataRistorante
     * @throws JackWicketException
     */
    @Transactional
    DataRistorante insert(DataRistorante risto) throws JackWicketException;

    /**
     * Return all the restaurant
     * 
     * @return Collection<DataRistorante>
     */
    @Transactional(readOnly = true)
    Collection<DataRistorante> getAll() throws JackWicketException;

    /**
     * Find the restaurant by the given pattern
     * 
     * @param pattern
     * @return Collection<DataRistorante>
     */
    @Transactional(readOnly = true)
    Collection<DataRistorante> find(String pattern) throws JackWicketException;

    /**
     * Find the restaurant by the given pattern, city, country
     * 
     * @param pattern
     * @param city
     * @param country
     * @return Collection<DataRistorante>
     */
    @Transactional(readOnly = true)
    Collection<DataRistorante> find(String pattern, String city, String country) throws JackWicketException;

    /**
     * Remove a restaurant from the repository
     * 
     * @param risto
     * @throws JackWicketException
     */
    @Transactional
    void remove(DataRistorante risto) throws JackWicketException;

    /**
     * Get a restaurant by ID
     * 
     * @param id
     * @return DataRistorante
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    DataRistorante getByID(String id) throws JackWicketException;

    @Transactional(readOnly = true)
    public List<ProvIta> getAllProv() throws JackWicketException;

    /**
     * exact match search on given parameters
     * 
     * @param pattern
     * @param city
     * @param country
     * @return found datRisto
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    Collection<DataRistorante> getBy(String pattern, String city, String country) throws JackWicketException;

}

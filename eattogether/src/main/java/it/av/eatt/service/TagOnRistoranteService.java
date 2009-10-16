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
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.ocm.model.TagOnRistorante;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Services on {@Link TagonRistorante}
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
public interface TagOnRistoranteService {

    /**
     * insert a new tag
     * 
     * @param tag
     * @param ristorante
     * @return just created TagOnRistorante
     * @throws JackWicketException
     */
    @Transactional
    TagOnRistorante insert(Tag tag, Ristorante ristorante) throws JackWicketException;

    /**
     * Insert a new tag
     * 
     * @param tag
     * @return TagOnRistorante
     * @throws JackWicketException
     */
    @Transactional
    TagOnRistorante insert(String tag) throws JackWicketException;
	
    /**
     * Return all the tags
     * 
     * @return List<TagOnRistorante>
     */
    @Transactional(readOnly = true)
    List<TagOnRistorante> getAll() throws JackWicketException;
    
    /**
     * Find tags by the given string
     * 
     * @param pattern
     * @return List<TagOnRistorante>
     */
    @Transactional(readOnly = true)
    List<TagOnRistorante> find(String pattern) throws JackWicketException;
    
    /**
     * Find tags by the given string and restaurant
     * 
     * @param pattern
     * @param risto
     * @return List<TagOnRistorante>
     */
    @Transactional(readOnly = true)
    List<TagOnRistorante> find(String pattern, Ristorante risto) throws JackWicketException;
    
    /**
     * Remove a tag
     * 
     * @param tag
     * @throws JackWicketException
     */
    @Transactional
    void remove(TagOnRistorante tag) throws JackWicketException;

    /**
     * return a list with the tags non associated to the given restaurant
     * 
     * @param string search pattern
     * @param risto
     * @return list of tags
     * @throws JackWicketException
     */
    @Transactional(readOnly = true)
    List<Tag> findTagsNotLinked(String pettern, Ristorante risto) throws JackWicketException;
}

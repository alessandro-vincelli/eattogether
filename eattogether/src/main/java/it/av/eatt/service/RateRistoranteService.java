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
import it.av.eatt.ocm.model.RateOnRistorante;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Services on RateOnRistorante
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
@Repository
public interface RateRistoranteService {

    /**
     * Insert a new rate on restaurant
     * 
     * @param R the Risto to be inserted
     * @param user the user that is performing the modification
     * @return RistoranteRevision
     * @throws JackWicketException
     */
    @Transactional
    RateOnRistorante insert(RateOnRistorante rate) throws JackWicketException;

}

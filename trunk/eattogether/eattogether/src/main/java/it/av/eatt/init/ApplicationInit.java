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
package it.av.eatt.init;

import org.springframework.transaction.annotation.Transactional;

import it.av.eatt.JackWicketException;
import it.av.eatt.repo.util.RepositoryInitStructure;

/**
 * Verifies/Creates the zero conf/data on the repository.  
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a> 
 * 
 */
@Transactional
public class ApplicationInit {

    private RepositoryInitStructure repositoryInitStructure;

    public ApplicationInit(RepositoryInitStructure repositoryInitStructure) throws JackWicketException {
        this.repositoryInitStructure = repositoryInitStructure;
        this.repositoryInitStructure.checkBasePaths();
        this.repositoryInitStructure.checkBaseData();
    }
}
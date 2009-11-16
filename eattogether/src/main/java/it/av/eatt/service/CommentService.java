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
import it.av.eatt.ocm.model.Comment;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Operations on Comments
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Service
@Transactional
public interface CommentService {

    /**
     * Save a comment
     * 
     * @param comment
     * @return Comment
     * @throws JackWicketException
     */
    @Transactional
    Comment save(Comment comment) throws JackWicketException;

    /**
     * Get all the comment
     * 
     * @return Collection<Comment>
     */
    @Transactional
    Collection<Comment> getAll() throws JackWicketException;

    /**
     * Remove an comment from the repository
     * 
     * @param comment
     * @throws JackWicketException
     */
    @Transactional
    void remove(Comment comment) throws JackWicketException;
    
    
}
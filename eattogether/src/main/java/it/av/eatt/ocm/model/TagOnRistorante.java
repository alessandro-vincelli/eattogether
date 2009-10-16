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
package it.av.eatt.ocm.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Entity
@Table//(uniqueConstraints = { @UniqueConstraint(columnNames = { "tag_id", "ristorante_id" }) })
public class TagOnRistorante extends BasicEntity {
    
    public static final String TAG = "tag";
    public static final String RISTORANTE = "ristorante";
    public static final String ACTIVITY_RISTORANTE = "activityRistorante";
    
    //@ManyToOne(cascade = {CascadeType.ALL})
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, optional=false)
    //@ForeignKey(name="message_to_folder_fk")
    private Tag tag;
    
    public TagOnRistorante() {
        super();
    }

    public TagOnRistorante(Tag tag) {
        super();
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "tag_id", "ristorante_id" }) })
public class TagOnRistorante extends BasicEntity {
    
    public static final String TAG = "tag";
    public static final String RISTORANTE = "ristorante";
    public static final String ACTIVITY_RISTORANTE = "activityRistorante";
    
    @ManyToOne(cascade = {CascadeType.ALL})
    private Tag tag;
    @ManyToOne
    private Ristorante ristorante;
    
    public TagOnRistorante() {
        super();
    }

    public TagOnRistorante(Tag tag, Ristorante ristorante) {
        super();
        this.tag = tag;
        this.ristorante = ristorante;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }
 
}
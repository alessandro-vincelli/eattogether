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

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames={"id"})}
)
public class Activity extends BasicEntity {

    public static final String DATE = "date";
    public static final String USER = "eater";
    @Column(nullable=false)
    private Timestamp date;
    @ManyToOne(optional=false)
    private Eater eater;
    
    /** default constructor */
    public Activity() {
    }

    public Activity(Timestamp date, Eater eater) {
        this.date = date;
        this.eater = eater;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Eater getEater() {
        return eater;
    }

    public void setEater(Eater user) {
        this.eater = user;
    }    
}
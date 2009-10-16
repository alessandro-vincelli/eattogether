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

import it.av.eatt.JackWicketException;
import it.av.eatt.JackWicketRunTimeException;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.BeanUtils;


/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Entity
public class RistoranteRevision extends BasicEntity implements Cloneable{

	public static final String VERSION = "version";
    public static final String ID_RISTORANTE = "ristorante_id";
    
    @Embedded
    //@Column(updatable = false, nullable = false)
    //@AttributeOverrides( { @AttributeOverride(name = "id", column = @Column(name = "id_risto", nullable=true))})
    private Ristorante ristoranteRevision;
    
    public RistoranteRevision() {
    }

    public RistoranteRevision(Ristorante risto) throws JackWicketException {
        super();
        this.ristoranteRevision = new Ristorante();
        try {
            BeanUtils.copyProperties(this.ristoranteRevision, risto);
            this.ristoranteRevision.setActivities(null);
            this.ristoranteRevision.setRates(null);
            this.ristoranteRevision.setTags(null);
            this.ristoranteRevision.setRevisions(null);
        } catch (IllegalAccessException e) {
           throw new JackWicketException(e);
        } catch (InvocationTargetException e) {
            throw new JackWicketException(e);
        }
    }
    


    /**
     * @return the ristoranteRevision
     */
    public Ristorante getRistoranteRevision() {
        return ristoranteRevision;
    }

    /**
     * @param ristoranteRevision the ristoranteRevision to set
     */
    public void setRistoranteRevision(Ristorante ristoranteRevision) {
        this.ristoranteRevision = ristoranteRevision;
    }

    @Override
    public RistoranteRevision clone() {
	    try {
	        return (RistoranteRevision) new RistoranteRevision(getRistoranteRevision());
        } catch (JackWicketException e) {
        	throw new JackWicketRunTimeException();
        }
    }

}
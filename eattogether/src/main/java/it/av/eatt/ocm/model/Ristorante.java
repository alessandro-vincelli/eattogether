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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Node(jcrType = "nt:unstructured", jcrMixinTypes = "mix:versionable, mix:lockable", extend=BasicEntity.class)
@Entity
@Embeddable
public class Ristorante extends BasicEntity implements BasicNode{

    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String POSTALCODE = "postalCode";
    public static final String COUNTRY = "country";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String WWW = "www";
    public static final String RATES = "rates";
    public static final String TAGS = "tags";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String MOBILE_PHONE_NUMBER = "mobilePhoneNumber";
    public static final String FAX_NUMBER = "faxNumber";

    @Field(path = true)
    private String path;
    @Field(uuid = true)
    private String uuid;
    @Deprecated
    private String version;
    @Field
    private String name;
    @Field
    private String address;
    @Field
    private String postalCode;
    @Field
    private String country;
    @Field
    private String province;
    @Field
    private String city;
    @Field
    private String type;
    @Field
    private String description;
    @Field
    private String www;
    @Field
    private Timestamp creationTime;
    @Field
    private Timestamp modificationTime;
    @Field
    private String phoneNumber;
    @Field
    private String mobilePhoneNumber;
    @Field
    private String faxNumber;
    private Integer revisionNumber;
    //@Collection(collectionConverter = MultiValueCollectionConverterImpl.class)
    @OneToMany(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    //@JoinTable
    //@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<RateOnRistorante> rates;
    //@Collection(collectionConverter = MultiValueCollectionConverterImpl.class)
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    //@JoinTable
    //@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Tag> tags;
    //@OneToMany(mappedBy = "ristorante", cascade = { CascadeType.ALL })
    //@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable
    //@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<ActivityRistorante> activities;
    //@OneToMany(mappedBy = "ristorante", cascade = { CascadeType.ALL })
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    //@JoinTable
    //@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OrderBy("ristoranteRevision.revisionNumber DESC")
    private List<RistoranteRevision> revisions;

    public Ristorante() {
        rates = new ArrayList<RateOnRistorante>();
        tags = new ArrayList<Tag>();
        activities = new ArrayList<ActivityRistorante>();
        revisions = new ArrayList<RistoranteRevision>();
    }

    public final String getPath() {
        return path;
    }

    public final void setPath(String path) {
        this.path = path;
    }

    @Field(uuid = true)
    public final String getUuid() {
        return uuid;
    }

    public final void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @Deprecated
    public String getVersion() {
        return this.version;
    }
    @Deprecated
    public void setVersion(String version) {
        this.version = version;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        this.address = address;
    }

    public final String getPostalCode() {
        return postalCode;
    }

    public final void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public final String getCountry() {
        return country;
    }

    public final void setCountry(String country) {
        this.country = country;
    }

    public final String getProvince() {
        return province;
    }

    public final void setProvince(String province) {
        this.province = province;
    }

    public final String getCity() {
        return city;
    }

    public final void setCity(String city) {
        this.city = city;
    }

    public final String getType() {
        return type;
    }

    public final void setType(String type) {
        this.type = type;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public List<RateOnRistorante> getRates() {
        return rates;
    }

    public void setRates(List<RateOnRistorante> rates) {
        this.rates = rates;
    }
    
    public void addARate(RateOnRistorante rate) {
        this.rates.add(rate);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public List<ActivityRistorante> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityRistorante> activities) {
        this.activities = activities;
    }

    public void addActivity(ActivityRistorante activity) {
        this.activities.add(activity);
    }
    
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the modificationTime
     */
    public Timestamp getModificationTime() {
        return modificationTime;
    }

    /**
     * @param modificationTime the modificationTime to set
     */
    public void setModificationTime(Timestamp modificationTime) {
        this.modificationTime = modificationTime;
    }

    /**
     * @return the revisions
     */
    public List<RistoranteRevision> getRevisions() {
        return revisions;
    }

    /**
     * @param revisions the revisions to set
     */
    public void setRevisions(List<RistoranteRevision> revisions) {
        this.revisions = revisions;
    }
    
    /**
     * @param revisions the revisions to set
     */
    public void addRevision(RistoranteRevision revision) {
        this.revisions.add(revision);
    }
    
    /**
     * @return the versionNumber
     */
    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * @param versionNumber the versionNumber to set
     */
    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    /**
     * @return the www
     */
    public String getWww() {
    	return www;
    }

	/**
     * @param www the www to set
     */
    public void setWww(String www) {
    	this.www = www;
    }

	/**
     * @return the faxNumber
     */
    public String getFaxNumber() {
    	return faxNumber;
    }

	/**
     * @param faxNumber the faxNumber to set
     */
    public void setFaxNumber(String faxNumber) {
    	this.faxNumber = faxNumber;
    }

	/**
     * 
     * @return the average rate, 0 if no rate presents
     */
    public int getRating() {
        if (getRates().size() > 0) {
            int average = 0;
            for (RateOnRistorante rate : getRates()) {
                average = average + rate.getRate();
            }
            average = average / getRates().size();
            return average;
        } else {
            return 0;
        }
    }
}
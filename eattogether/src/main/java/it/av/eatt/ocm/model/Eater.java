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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Indexed;

/**
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames={"email"})}
)
@Indexed
public class Eater extends BasicEntity {
    
    public static final String ID = "id"; 
    public static final String PASSWORD = "password";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String EMAIL = "email";
    public static final String USERPROFILE = "userProfile";
    public static final String COUNTRY = "country";
    public static final String LANGUAGE = "language";
    
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String country;
    @ManyToOne
    private Language language;
    @ManyToOne
    private EaterProfile userProfile;
    @OneToMany//( cascade = {CascadeType.ALL} , mappedBy="user")
    @OrderBy(Activity.DATE)
    private List<Activity> activities;
    //@Collection(collectionClassName=EaterRelation.class)// The proxy doesn't work the session is closed (proxy=true)
    //@NaturalId
    @OneToMany(mappedBy="fromUser", cascade = {CascadeType.ALL})
    private List<EaterRelation> userRelation;
    
    /** default constructor */
    public Eater() {
    }

    /** minimal constructor */
    public Eater(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /** full constructor */
    public Eater(String usPassword, String usFirstname, String usSurname, String usEmail) {
        this.password = usPassword;
        this.firstname = usFirstname;
        this.lastname = usSurname;
        this.email = usEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EaterProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(EaterProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<EaterRelation> getUserRelation() {
        return userRelation;
    }

    public void setUserRelation(List<EaterRelation> userRelation) {
        this.userRelation = userRelation;
    }

    public String getCountry() {
    	return country;
    }

    public void setCountry(String country) {
    	this.country = country;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
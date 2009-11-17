package it.av.eatt.ocm.model.data;

import it.av.eatt.ocm.model.BasicEntity;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Indexed;

/**
 * countries
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 */
@Entity
@Indexed
public class Country extends BasicEntity{
    
    public static final String NAME = "name";
    
    private String iso2;
    private String iso3;
    private String name;
    /**
     * @return the iso2
     */
    public String getIso2() {
        return iso2;
    }
    /**
     * @param iso2 the iso2 to set
     */
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }
    /**
     * @return the iso3
     */
    public String getIso3() {
        return iso3;
    }
    /**
     * @param iso3 the iso3 to set
     */
    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
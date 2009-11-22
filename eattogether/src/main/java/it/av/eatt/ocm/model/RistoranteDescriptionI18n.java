package it.av.eatt.ocm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Alessandro Vincelli
 */
@Entity
public class RistoranteDescriptionI18n extends BasicEntity {

    private Language language;
    @Column(length = 10000)
    private String description;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

package it.av.eatt.web.components;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.service.RistoranteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.Model;
/**
 * suggests tags already presents and non yet used by this restaurant 
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 *
 */
public class TagBox extends AutoCompleteTextField<String> {
    private static final long serialVersionUID = 1L;
    private RistoranteService ristoranteService;
    private Ristorante ristorante;

    /**
     * @param model
     * @param id
     * @param ristoranteService
     * @param ristorante
     */
    public TagBox(Model<String> model, String id, RistoranteService ristoranteService, Ristorante ristorante) {
        super(id, model);
        this.ristoranteService = ristoranteService;
        this.ristorante = ristorante;
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField#getChoices(java.lang.String)
     */
    @Override
    protected Iterator<String> getChoices(String input) {
        Collection<String> choises = new ArrayList<String>();
            //FIXME change the implementation
            //for (Tag tag : ristoranteService.findTagsNotLinked(input + "%", ristorante)) {
             //   choises.add(tag.getTag());
            //}
        
        return choises.iterator();
    }

}

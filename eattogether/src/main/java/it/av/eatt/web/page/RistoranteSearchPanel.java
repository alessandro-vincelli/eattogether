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
package it.av.eatt.web.page;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.service.RistoranteService;
import it.av.eatt.web.components.RistoranteDataTable;
import it.av.eatt.web.data.RistoranteSortableDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.IClusterable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;

/**
 * The panel provides the search form for ..
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class RistoranteSearchPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private SearchBean searchBean = new SearchBean();

    /**
     * Constructor
     * 
     * @param id
     * @param dataProvider
     * @param dataTable
     * @param feedbackPanel
     */
    public RistoranteSearchPanel(String id, final RistoranteSortableDataProvider dataProvider,
            final RistoranteDataTable<Ristorante> dataTable, final FeedbackPanel feedbackPanel) {
        super(id);
        Form<String> form = new Form<String>("searchForm", new CompoundPropertyModel(searchBean));
        add(form);
        form.setOutputMarkupId(true);
        FormComponent<String> fc;
        // fc = new TextField<String>("searchData");
        AutoCompleteSettings autoCompleteSettings = new AutoCompleteSettings();
        autoCompleteSettings.setCssClassName("autocomplete-risto");
        autoCompleteSettings.setAdjustInputWidth(false);
        fc = new searchBox("searchData", autoCompleteSettings, dataProvider.getRistoranteService());
        form.add(fc);
        // event and throttle it down to once per second
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onkeyup", Duration.ONE_SECOND);

        form.add(new AjaxButton("ajax-button", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                try {
                    String pattern = getRequest().getParameter("searchData");
                    dataProvider.fetchResults(pattern);
                } catch (JackWicketException e) {
                    feedbackPanel.error(e.getMessage());
                }
                target.addComponent(dataTable);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form form) {
                target.addComponent(dataTable);
            }
        });
    }

    /**
     * 
     * Simple Bean to store the Form data
     * 
     * @author Alessandro Vincelli
     * 
     */
    public static class SearchBean implements IClusterable {
        private static final long serialVersionUID = 1L;
        private String searchData;

        /**
         * @return the searchData
         */
        public final String getSearchData() {
            return searchData;
        }

        /**
         * @param searchData the searchData to set
         */
        public final void setSearchData(String searchData) {
            this.searchData = searchData;
        }
    }

    private static class searchBox extends AutoCompleteTextField<String> {
        private static final long serialVersionUID = 1L;
        private RistoranteService ristoranteService;

        public searchBox(String id, AutoCompleteSettings autoCompleteSettings, RistoranteService ristoranteService) {
            super(id, autoCompleteSettings);
            this.ristoranteService = ristoranteService;
        }

        @Override
        protected Iterator<String> getChoices(String input) {
            Collection<String> choises = new ArrayList<String>();
            try {
                if (!input.isEmpty() && input.length() > 2)
                    for (Ristorante risto : ristoranteService.freeTextSearch(input + "*")) {
                        choises.add(risto.getName());
                    }
            } catch (JackWicketException e) {
            }
            return choises.iterator();
        }

    }

}

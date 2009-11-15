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
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.service.RistoranteService;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * The page shows all the {@link Ristorante} informations.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class RistoranteViewPage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = "ristoranteService")
    private RistoranteService ristoranteService;

    private Ristorante ristorante = new Ristorante();;
    
    private ModalWindow revisionsPanel;
    private boolean hasVoted = Boolean.FALSE;
    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @throws JackWicketException
     */
    public RistoranteViewPage(PageParameters parameters) throws JackWicketException {
        String ristoranteId = parameters.getString("ristoranteId", "");
        if (StringUtils.isNotBlank(ristoranteId)) {
            this.ristorante = ristoranteService.getByID(ristoranteId);
        } else {
            
            setRedirect(true);
            setResponsePage(getApplication().getHomePage());
        }
        setOutputMarkupId(true);

        Form<Ristorante> form = new Form<Ristorante>("ristoranteForm", new CompoundPropertyModel<Ristorante>(ristorante));
        add(form);
        form.setOutputMarkupId(true);
        form.add(new Label(Ristorante.NAME));
        form.add(new Label(Ristorante.TYPE));
        form.add(new Label(Ristorante.WWW));
        form.add(new ListView<Tag>(Ristorante.TAGS){
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<Tag> item) {
                item.add(new Label("tagItem", item.getModelObject().getTag()));
            } 
        });
        form.add(new MultiLineLabel(Ristorante.DESCRIPTION));
        // form.add(new DropDownChoice<EaterProfile>("userProfile", new ArrayList<EaterProfile>(userProfileService.getAll()), new UserProfilesList()).setOutputMarkupId(true));
        form.add(new Label("revisionNumber"));
          
        Form<Ristorante> formAddress = new Form<Ristorante>("ristoranteAddressForm", new CompoundPropertyModel<Ristorante>(ristorante));
        add(formAddress);
        formAddress.add(new Label(Ristorante.ADDRESS));
        formAddress.add(new Label(Ristorante.CITY));
        formAddress.add(new Label(Ristorante.PROVINCE));
        formAddress.add(new Label(Ristorante.POSTALCODE));
        formAddress.add(new Label(Ristorante.COUNTRY));
        formAddress.add(new Label(Ristorante.MOBILE_PHONE_NUMBER));
        formAddress.add(new Label(Ristorante.PHONE_NUMBER));
        formAddress.add(new Label(Ristorante.FAX_NUMBER));
        formAddress.add(new RatingPanel("rating1", new PropertyModel<Integer>(getRistorante(), "rating"), new Model<Integer>(5), new PropertyModel<Integer>(getRistorante(), "rates.size"), new PropertyModel<Boolean>(this, "hasVoted"), true ) {
            @Override
            protected boolean onIsStarActive(int star) {
                return star < ((int) (getRistorante().getRating() + 0.5));
            }

            @Override
            protected void onRated(int rating, AjaxRequestTarget target) {
                try {
                    setHasVoted(Boolean.TRUE);
                    ristoranteService.addRate(getRistorante(), getLoggedInUser(), rating);
                } catch (JackWicketException e) {
                    error(e);
                }
            }
        });

        
        AjaxFallbackLink<String> editRistorante = new AjaxFallbackLink<String>("editRistorante") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    setResponsePage(new RistoranteEditPage(getRistorante()));
                } catch (JackWicketException e) {
                    error(new StringResourceModel("genericErrorMessage", this, null).getString());
                }
            }
        };
        editRistorante.setOutputMarkupId(true);
        if (getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(RistoranteEditPage.class)) {
            editRistorante.setVisible(true);
        } else {
            editRistorante.setVisible(false);
        }
        add(editRistorante);

        add(revisionsPanel = new ModalWindow("revisionsPanel"));
        revisionsPanel.setWidthUnit("%");
        revisionsPanel.setInitialHeight(450);
        revisionsPanel.setInitialWidth(100);
        revisionsPanel.setResizable(false);
        revisionsPanel.setContent(new RistoranteRevisionsPanel(revisionsPanel.getContentId(), getFeedbackPanel()));
        revisionsPanel.setTitle("Revisions list");
        revisionsPanel.setCookieName("SC-revisionLists");

        revisionsPanel.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                return true;
            }
        });

        revisionsPanel.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {

            }
        });

        add(new AjaxLink("showsAllRevisions") {
            public void onClick(AjaxRequestTarget target) {
                ((RistoranteRevisionsPanel) revisionsPanel.get(revisionsPanel.getContentId())).refreshRevisionsList(ristorante);
                revisionsPanel.show(target);
            }
        });

        setHasVoted(ristoranteService.hasUsersAlreadyRated(getRistorante(), getLoggedInUser()) || getLoggedInUser() == null);
        
    }

    public RistoranteViewPage(Ristorante ristorante) throws JackWicketException {
        this(new PageParameters("ristoranteId=" + ristorante.getId()));
    }

    
    public final Ristorante getRistorante() {
        return ristorante;
    }

    public final void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    /**
     * @return the hasVoted
     */
    private boolean isHasVoted() {
        return hasVoted;
    }

    /**
     * @param hasVoted the hasVoted to set
     */
    private void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

}

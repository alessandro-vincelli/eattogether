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
import it.av.eatt.ocm.model.data.Country;
import it.av.eatt.service.RistoranteService;
import it.av.eatt.service.TagService;
import it.av.eatt.web.components.TagBox;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Edit a {@link Ristorante}.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@AuthorizeInstantiation( { "USER", "ADMIN", "EDITOR" })
public class RistoranteEditDataPage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = "ristoranteService")
    private RistoranteService ristoranteService;
    @SpringBean
    private TagService tagService;

    private Ristorante ristorante;
    private Form<Ristorante> form;

    /**
     * 
     * @param ristorante
     * @throws JackWicketException
     */
    public RistoranteEditDataPage(Ristorante ristorante) throws JackWicketException {
        this(new PageParameters("ristoranteId=" + ristorante.getId()));
    }

    /**
     * 
     * @param parameters
     * @throws JackWicketException
     */
    public RistoranteEditDataPage(PageParameters parameters) throws JackWicketException {

        String ristoranteId = parameters.getString("ristoranteId", "");
        if (StringUtils.isNotBlank(ristoranteId)) {
            this.ristorante = ristoranteService.getByID(ristoranteId);
        } else {
            setRedirect(true);
            setResponsePage(getApplication().getHomePage());
        }

        form = new Form<Ristorante>("ristoranteForm", new CompoundPropertyModel<Ristorante>(ristorante));
        form.setOutputMarkupId(true);
        form.add(new RequiredTextField<String>(Ristorante.NAME));

        form.add(new TextField<String>(Ristorante.WWW).setOutputMarkupId(true));
        form.add(new TagBox(new Model<String>(""), "tagBox", ristorante));

        form.add(new CheckBox("types.ristorante"));
        form.add(new CheckBox("types.pizzeria"));
        form.add(new CheckBox("types.bar"));

        form.add(new ListView<Tag>(Ristorante.TAGS) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Tag> item) {
                item.add(new Label("tagItem", item.getModelObject().getTag()));
                item.add(new AjaxFallbackLink<String>("buttonTagItemRemove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        getList().remove(getParent().getDefaultModelObject());
                        if (target != null) {
                            target.addComponent(form);
                        }
                    }
                });
            }
        });
        form.add(new TextArea<String>(Ristorante.DESCRIPTION));
        // form.add(new DropDownChoice<EaterProfile>("userProfile", new
        // ArrayList<EaterProfile>(userProfileService.getAll()), new UserProfilesList()).setOutputMarkupId(true));

        form.add(new AjaxFallbackButton("addTag", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                String tagValue = ((TagBox) form.get("tagBox")).getModelObject();
                if (StringUtils.isNotBlank(tagValue)) {
                    Ristorante risto = ((Ristorante) form.getModelObject());
                    try {
                        risto.getTags().add(tagService.insert(tagValue));
                        form.setModelObject(risto);
                        if (target != null) {
                            target.addComponent(form);
                        }
                    } catch (JackWicketException e) {
                        error("ERROR: " + e.getMessage());
                        if (target != null) {
                            target.addComponent(getFeedbackPanel());
                        }
                    }
                }
                // after clean up the tagBox
                ((TagBox) form.get("tagBox")).setModelObject(null);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                if (target != null) {
                    target.addComponent(getFeedbackPanel());
                }
            }
        });

        form.add(new AjaxFallbackLink<Ristorante>("buttonClearForm", new Model<Ristorante>(ristorante)) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                form.setModelObject(new Ristorante());
                if (target != null) {
                    target.addComponent(form);
                }
            }
        });
        form.add(new SubmitButton("submitRestaurant", form));
        add(form);
        add(new SubmitButton("submitRestaurantRight", form));
    }

    private class SubmitButton extends AjaxFallbackButton {
        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<Ristorante> form) {
            super(id, form);
        }

        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            tag.getAttributes().put("value", getString("button.update"));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form form) {
            try {
                if (StringUtils.isNotBlank(ristorante.getId())) {
                    ristorante = ristoranteService.update(ristorante, getLoggedInUser());
                    getFeedbackPanel().info(getString("info.ristoranteupdated"));
                } else {
                    getFeedbackPanel().error(getString("error.onUpdate"));
                }
                form.setModelObject(ristorante);
            } catch (JackWicketException e) {
                getFeedbackPanel().error("ERROR" + e.getMessage());
            }
            if (target != null) {
                target.addComponent(form);
                target.addComponent(getFeedbackPanel());
            }
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form form) {
            getFeedbackPanel().anyErrorMessage();
            target.addComponent(getFeedbackPanel());
        }
    }

    public final Form<Ristorante> getForm() {
        return form;
    }

    private class CountryChoiceRenderer implements IChoiceRenderer<Country> {

        @Override
        public Object getDisplayValue(Country object) {
            return object.getName();
        }

        @Override
        public String getIdValue(Country object, int index) {
            return object.getId();
        }

    }

}

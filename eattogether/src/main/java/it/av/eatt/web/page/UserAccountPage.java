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
import it.av.eatt.ocm.model.ActivityRistorante;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.util.DateUtil;
import it.av.eatt.service.ActivityRistoranteService;
import it.av.eatt.service.EaterService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User account manager page.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 */
@AuthorizeInstantiation( { "USER" })
public class UserAccountPage extends BasePage {
    private static final long serialVersionUID = 1L;
    @SpringBean
    private EaterService eaterService;
    private String confirmPassword = "";

    public UserAccountPage(PageParameters pageParameters) throws JackWicketException {
        if (!pageParameters.containsKey(YoueatHttpParams.PARAM_YOUEAT_ID)) {
            throw new JackWicketException("Missing user id");
        }

        String eaterId = pageParameters.getString(YoueatHttpParams.PARAM_YOUEAT_ID, "");
        Eater eater = eaterService.getByID(eaterId);

        Form<Eater> form = new Form<Eater>("account", new CompoundPropertyModel<Eater>(eater));
        form.add(new RequiredTextField<String>("firstName"));
        form.add(new RequiredTextField<String>("lastName"));
        form.add(new PasswordTextField("password"));
        form.add(new PasswordTextField("confirm-password"));

    }

    private class SubmitButton extends AjaxFallbackButton {
        public SubmitButton(String id, Form<Eater> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            try {
                eaterService.update((Eater)form.getModelObject());
            } catch (JackWicketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}

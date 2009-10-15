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
import it.av.eatt.ocm.model.UserProfile;
import it.av.eatt.service.UserProfileService;
import it.av.eatt.web.data.UserProfileSortableDataProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * The page provides some basic operation on the {@link UserProfile} bean.
 * 
 * <ul>
 * <li>{@link Form} to edit and add {@link UserProfile} with ajax buttons</li>
 * <li>{@link AjaxFallbackDefaultDataTable} to show and enable operations on the
 * {@link UserProfile}</li>
 * </ul>
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@AuthorizeInstantiation( { "USER", "ADMIN", "EDITOR" })
public class UserProfilePage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name="userProfileService")
    private UserProfileService userProfileService;

    private UserProfile userProfile;
    private AjaxFallbackDefaultDataTable<UserProfile> usersProfileDataTable;
    private UserProfileSortableDataProvider dataProvider;
    private Form<UserProfile> form;

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @throws JackWicketException
     */
    public UserProfilePage() throws JackWicketException {
        userProfile = new UserProfile();
        
        form = new Form<UserProfile>("userProfileForm", new CompoundPropertyModel<UserProfile>(userProfile));
        form.setOutputMarkupId(true);
        form.add(new RequiredTextField<String>("name"));
        form.add(new TextField<String>("description"));

        form.add(new AjaxLink<UserProfile>("buttonClearForm", new Model<UserProfile>(userProfile)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                form.setModelObject(new UserProfile());
                target.addComponent(form);
            }
        });
        form.add(new SubmitButton("ajax-button", form));
        add(form);

        List<IColumn<UserProfile>> columns = new ArrayList<IColumn<UserProfile>>();
        columns.add(new AbstractColumn<UserProfile>(new Model<String>(new StringResourceModel("datatableactionpanel.actions", this, null).getString())) {
            public void populateItem(Item<ICellPopulator<UserProfile>> cellItem, String componentId, IModel<UserProfile> model) {
                cellItem.add(new UserProfileTableActionPanel(componentId, model));
            }
        });
        columns.add(new PropertyColumn<UserProfile>(new Model<String>(new StringResourceModel("name", this, null).getString()), "name"));
        columns.add(new PropertyColumn<UserProfile>(new Model<String>(new StringResourceModel("description", this, null).getString()), "description"));
        dataProvider = new UserProfileSortableDataProvider(userProfileService);
        refreshDataTable();
        usersProfileDataTable = new AjaxFallbackDefaultDataTable<UserProfile>("usersDataTable", columns, dataProvider, 10);
        add(usersProfileDataTable);

    }

    private class SubmitButton extends AjaxButton {
        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<UserProfile> form) {
            super(id, form);
        }

        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            if (form.getModelObject().getId() != 0) {
                tag.getAttributes().put("value", new StringResourceModel("button.create", this, null).getString());
            } else {
                tag.getAttributes().put("value", new StringResourceModel("button.update", this, null).getString());
            }
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form form) {
            try {
                UserProfile user = (UserProfile) form.getModelObject();
                user = userProfileService.save(user);
                getFeedbackPanel().info(new StringResourceModel("info.userprofileupdated", this, null).getString());
                refreshDataTable();
                target.addComponent(usersProfileDataTable);
                target.addComponent(form);
            } catch (JackWicketException e) {
                getFeedbackPanel().error("ERROR" + e.getMessage());
            }
            target.addComponent(getFeedbackPanel());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form form) {
            getFeedbackPanel().anyErrorMessage();
            target.addComponent(getFeedbackPanel());
        }
    }

    /**
     * Fill with fresh data the repetear
     * @throws JackWicketException 
     */
    public final void refreshDataTable() throws JackWicketException {
        dataProvider.fetchResults();
    }

    public final Form<UserProfile> getForm() {
        return form;
    }

    public final UserProfile getUserProfile() {
        return userProfile;
    }

    public final void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public final UserProfileService getUsersProfileServices() {
        return userProfileService;
    }

    public final AjaxFallbackDefaultDataTable<UserProfile> getUsersProfileDataTable() {
        return usersProfileDataTable;
    }

}

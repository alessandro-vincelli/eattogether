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
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.EaterProfile;
import it.av.eatt.service.UserProfileService;
import it.av.eatt.service.UserService;
import it.av.eatt.web.data.UserSortableDataProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Administration page for {@link Eater} bean.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@AuthorizeInstantiation( { "USER", "ADMIN", "EDITOR" })
public class UserManagerPage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name="userService")
    private UserService userService;
    @SpringBean(name="userProfileService")
    private UserProfileService userProfileService;

    private Eater user;
    private AjaxFallbackDefaultDataTable<Eater> usersDataTable;
    private UserSortableDataProvider dataProvider;
    private Form<Eater> form;
    private PropertyListView<Eater> usersVersionsList;
    private UsersVersionsListFrag usersVersionsListFrag;

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @throws JackWicketException
     */
    public UserManagerPage() throws JackWicketException {
        user = new Eater();
        
        form = new Form<Eater>("userForm", new CompoundPropertyModel<Eater>(user));
        form.setOutputMarkupId(true);
        form.add(new TextField<String>("password"));
        form.add(new TextField<String>("lastname"));
        form.add(new TextField<String>("firstname"));
        form.add(new TextField<String>("email"));
        form.add(new DropDownChoice<EaterProfile>("userProfile", new ArrayList<EaterProfile>(userProfileService.getAll()), new UserProfilesList()).setOutputMarkupId(true));

        form.add(new AjaxLink<Eater>("buttonClearForm", new Model<Eater>(user)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                form.setModelObject(new Eater());
                target.addComponent(form);
            }
        });
        form.add(new SubmitButton("ajax-button", form));
        add(form);

        List<IColumn<Eater>> columns = new ArrayList<IColumn<Eater>>();
        columns.add(new AbstractColumn<Eater>(new Model<String>(new StringResourceModel("datatableactionpanel.actions", this, null).getString())) {
            public void populateItem(Item<ICellPopulator<Eater>> cellItem, String componentId, IModel<Eater> model) {
                cellItem.add(new UserTableActionPanel(componentId, model));
            }
        });
        columns.add(new PropertyColumn<Eater>(new Model<String>(new StringResourceModel("password", this, null).getString()), "password"));
        columns.add(new PropertyColumn<Eater>(new Model<String>(new StringResourceModel("firstname", this, null).getString()), "firstname"));
        columns.add(new PropertyColumn<Eater>(new Model<String>(new StringResourceModel("lastname", this, null).getString()), "lastname"));
        columns.add(new PropertyColumn<Eater>(new Model<String>(new StringResourceModel("email", this, null).getString()), "email"));
        columns.add(new PropertyColumn<Eater>(new Model<String>(new StringResourceModel("userProfile", this, null).getString()), "userProfile.name"));
        dataProvider = new UserSortableDataProvider(userService);
        refreshDataTable();
        usersDataTable = new AjaxFallbackDefaultDataTable<Eater>("usersDataTable", columns, dataProvider, 10);
        add(usersDataTable);

        add(new SearchPanel(dataProvider, usersDataTable, "searchPanel", getFeedbackPanel()));
    }

    private class SubmitButton extends AjaxButton {
        private static final long serialVersionUID = 1L;

        public SubmitButton(String id, Form<Eater> form) {
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
                Eater userToSave = (Eater) form.getModelObject();
                if(userToSave.getId() != 0l){
                    userToSave = userService.update(userToSave);
                    getFeedbackPanel().info(new StringResourceModel("info.userupdated", this, null).getString());
                }
                else{
                    userToSave = userService.add(userToSave);
                    getFeedbackPanel().info(new StringResourceModel("info.useradded", this, null).getString());
                }
                form.setModelObject(userToSave);
                refreshDataTable();
                target.addComponent(usersDataTable);
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

    private class UsersVersionsListFrag extends Fragment {
        private static final long serialVersionUID = 1L;

        public UsersVersionsListFrag(String id, String markupId, MarkupContainer container) throws JackWicketException {
            super(id, markupId, container);
            setOutputMarkupId(true);
            usersVersionsList = new PropertyListView<Eater>("versions", new ArrayList<Eater>()) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Eater> item) {
                    item.add(new Label("email"));
                    item.add(new Label("password"));
                    item.add(new Label("firstname"));
                    item.add(new Label("lastname"));
                    item.add(new Label("userProfile.name"));
                    item.add(new Label("version"));
                }
            };
            add(usersVersionsList.setOutputMarkupId(true));
        }
    }

    /**
     * Fill with fresh data the repetear
     * @throws JackWicketException 
     */
    public final void refreshDataTable() throws JackWicketException {
        dataProvider.fetchResults(this.getRequest());
    }

    public final  Form<Eater> getForm() {
        return form;
    }

    public final Eater getUser() {
        return user;
    }

    public final void setUser(Eater user) {
        this.user = user;
    }

    public final UsersVersionsListFrag getUsersVersionsListFrag() {
        return usersVersionsListFrag;
    }

    public final UserService getUsersServices() {
        return userService;
    }

    public final AjaxFallbackDefaultDataTable<Eater> getUsersDataTable() {
        return usersDataTable;
    }

    private class UserProfilesList implements IChoiceRenderer<EaterProfile> {
        private static final long serialVersionUID = 1L;
        @Override
        public Object getDisplayValue(EaterProfile object) {
            return object.getName();
        }

        @Override
        public String getIdValue(EaterProfile object, int index) {
            return object.getName();
        }

    }
}
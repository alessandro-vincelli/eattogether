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
import it.av.eatt.ocm.model.UserRelation;
import it.av.eatt.service.UserRelationService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Add and remove friends.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@AuthorizeInstantiation( { "USER", "ADMIN", "EDITOR" })
public class FriendPage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private UserRelationService userRelationService;
    private PropertyListView<UserRelation> friendsList;

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @throws JackWicketException
     */
    public FriendPage() throws JackWicketException {
        setOutputMarkupId(true);
        friendsList = new PropertyListView<UserRelation>("friendsList", userRelationService.getAllRelations(getLoggedInUser())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<UserRelation> item) {
                item.add(new Label(UserRelation.TO_USER + ".firstname"));
                item.add(new Label(UserRelation.TO_USER + ".lastname"));
                item.add(new Label(UserRelation.TYPE));
                item.add(new Label(UserRelation.STATUS));
//                item.add(new Label(UserRelation.TO_USER + ".userRelation"));
                item.add(new AjaxLink<UserRelation>("remove", new Model<UserRelation>(item.getModelObject())) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            ((FriendPage) getPage()).userRelationService.remove(getModelObject());
                            ((FriendPage) target.getPage()).friendsList.setModelObject(userRelationService.getAllRelations(getLoggedInUser()));
                            target.addComponent((target.getPage()));
                            // info(new StringResourceModel("info.userRelationRemoved", this, null).getString());
                        } catch (JackWicketException e) {
                            error(new StringResourceModel("genericErrorMessage", this, null).getString());
                        }
                        target.addComponent(((FriendPage) target.getPage()).getFeedbackPanel());
                    }
                });
            }
        };
        add(friendsList.setOutputMarkupId(true));
    }

}

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
import it.av.eatt.ocm.model.EaterRelation;
import it.av.eatt.service.UserRelationService;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.MarkupStream;
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
public class FriendsPage extends BasePage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private UserRelationService userRelationService;
    private PropertyListView<EaterRelation> friendsList;

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @throws JackWicketException
     */
    public FriendsPage() throws JackWicketException {
       
        final List<EaterRelation> allRelations = userRelationService.getAllRelations(getLoggedInUser());
        Label noYetFriends = new Label("noYetFriends"){
            @Override
            protected void onRender(MarkupStream markupStream) {
                super.onRender(markupStream);
                setVisible(allRelations.size() == 0);
            }  
        };
        add(noYetFriends);
        friendsList = new PropertyListView<EaterRelation>("friendsList", allRelations) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<EaterRelation> item) {
                item.add(new Label(EaterRelation.TO_USER + ".firstname"));
                item.add(new Label(EaterRelation.TO_USER + ".lastname"));
                item.add(new Label(EaterRelation.TYPE));
                item.add(new Label(EaterRelation.STATUS));
//                item.add(new Label(EaterRelation.TO_USER + ".userRelation"));
                item.add(new AjaxLink<EaterRelation>("remove", new Model<EaterRelation>(item.getModelObject())) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            ((FriendsPage) getPage()).userRelationService.remove(getModelObject());
                            ((FriendsPage) target.getPage()).friendsList.setModelObject(allRelations);
                            target.addComponent((target.getPage()));
                            // info(new StringResourceModel("info.userRelationRemoved", this, null).getString());
                        } catch (JackWicketException e) {
                            error(new StringResourceModel("genericErrorMessage", this, null).getString());
                        }
                        target.addComponent(((FriendsPage) target.getPage()).getFeedbackPanel());
                    }
                });
            }
        };
        add(friendsList.setOutputMarkupId(true));
    }

}

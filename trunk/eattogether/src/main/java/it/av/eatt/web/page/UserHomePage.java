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
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.util.DateUtil;
import it.av.eatt.service.ActivityRistoranteService;
import it.av.eatt.service.RistoranteService;
import it.av.eatt.web.data.RistoranteSortableDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Personal user home page.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
@AuthorizeInstantiation( { "USER" })
public class UserHomePage extends BasePage {
    private static final long serialVersionUID = 1L;
    @SpringBean(name="activityRistoranteService")
    private ActivityRistoranteService activityRistoranteService;
    @SpringBean(name="ristoranteService")
    private RistoranteService ristoranteService;
    
    public UserHomePage() {

        setOutputMarkupId(true);
        RistoranteSortableDataProvider ristoranteSortableDataProvider = new RistoranteSortableDataProvider(ristoranteService);
        List<IColumn<Ristorante>> columns = new ArrayList<IColumn<Ristorante>>();
        columns.add(new AbstractColumn<Ristorante>(new Model<String>(new StringResourceModel("datatableactionpanel.actions", this, null).getString())) {
            public void populateItem(Item<ICellPopulator<Ristorante>> cellItem, String componentId, IModel<Ristorante> model) {
                cellItem.add(new RistoranteTableActionPanel(componentId, model));
            }
        });
        
        columns.add(new PropertyColumn<Ristorante>(new Model<String>(new StringResourceModel("name", this, null).getString()), "name"));
        columns.add(new PropertyColumn<Ristorante>(new Model<String>(new StringResourceModel("type", this, null).getString()), "type"));
        columns.add(new PropertyColumn<Ristorante>(new Model<String>(new StringResourceModel("city", this, null).getString()), "city"));

        AjaxFallbackDefaultDataTable<Ristorante> ristoranteDataTable = new AjaxFallbackDefaultDataTable<Ristorante>("ristoranteDataTable", columns, ristoranteSortableDataProvider, 10);
        add(ristoranteDataTable);
        
        RistoranteSearchPanel ristoranteSearchPanel = new RistoranteSearchPanel("ristoranteSearchPanel", ristoranteSortableDataProvider, ristoranteDataTable, getFeedbackPanel());
        add(ristoranteSearchPanel);
        
        //My activities List
        Collection<ActivityRistorante> activities;
        try {
            activities = activityRistoranteService.findByUser(getLoggedInUser());
        } catch (JackWicketException e) {
            activities = new ArrayList<ActivityRistorante>();
            error(new StringResourceModel("error.errorGettingListActivities", this, null).getString());
        }

        PropertyListView<ActivityRistorante> activitiesList = new PropertyListView<ActivityRistorante>("activitiesList", new ArrayList<ActivityRistorante>(activities)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<ActivityRistorante> item) {
                item.add(new Label("type"));
                item.add(new Label("date.time", DateUtil.sdf2Show.format(item.getModelObject().getDate().getTime())));
                item.add(new BookmarkablePageLink<String>("ristorante.name", RistoranteViewPage.class, new PageParameters("ristoranteId=" + item.getModelObject().getRistorante().getId())));
            }
        };
        add(activitiesList);

        //My Friends activities List
        Collection<ActivityRistorante> friendsActivities;
        try {
            friendsActivities = activityRistoranteService.findByUserFriend(getLoggedInUser());
        } catch (JackWicketException e) {
            friendsActivities = new ArrayList<ActivityRistorante>();
            error(new StringResourceModel("error.errorGettingListActivities", this, null).getString());
        }

        PropertyListView<ActivityRistorante> friendsActivitiesList = new PropertyListView<ActivityRistorante>("friendsActivitiesList", new ArrayList<ActivityRistorante>(friendsActivities)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<ActivityRistorante> item) {
                item.add(new Label("type"));
                item.add(new Label("date.time", DateUtil.sdf2Show.format(item.getModelObject().getDate().getTime())));
                item.add(new Label("ristorante.name"));
                item.add(new Label("user.lastname"));
            }
        };
        add(friendsActivitiesList);
    }

}

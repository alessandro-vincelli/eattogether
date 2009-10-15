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
package it.av.eatt.web.data;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.User;
import it.av.eatt.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Request;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class SearchUserFriendSortableDataProvider extends SortableDataProvider<User> {
    private static final long serialVersionUID = 1L;
    private UserService usersService;
    private Collection<User> results;
    private User forUser;

    /**
     * @param usersService
     */
    public SearchUserFriendSortableDataProvider(UserService usersService, User forUser) {
        super();
        results = new ArrayList<User>();
        this.forUser = forUser;
        this.usersService = usersService;
        // setSort(LightVac.SortedFieldNames.dateTime.value(), true);
    }

    /**
     * 
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int,
     *      int)
     */
    @Override
    public final Iterator<User> iterator(int first, int count) {
        return Collections.synchronizedList(new ArrayList<User>(results)).subList(first, first + count).iterator();
    }

    /**
     * 
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    @Override
    public final int size() {
        return results.size();
    }

    /**
     * @param user
     * @return IModel<User>
     * 
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
     */
    @Override
    public final IModel<User> model(User user) {
        return new UserDetachableModel(user, usersService);
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    @Override
    public void detach() {
    }

    /**
     * Performs the search
     * 
     * @param request
     * @throws JackWicketException 
     */
    public final void fetchResults(Request request) throws JackWicketException {
        String pattern = request.getParameter("searchData");
        if (StringUtils.isNotBlank(pattern)) {
            results = usersService.findUserWithoutRelation(forUser, pattern);
        } else {
            results = usersService.findUserWithoutRelation(forUser);
        }
    }

}
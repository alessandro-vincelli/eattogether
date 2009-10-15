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
import it.av.eatt.ocm.model.UserProfile;
import it.av.eatt.service.UserProfileService;

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
public class UserProfileSortableDataProvider extends SortableDataProvider<UserProfile> {
    private static final long serialVersionUID = 1L;
    private UserProfileService userProfileService;
    private Collection<UserProfile> results;

    /**
     * 
     * @param userProfileService
     */
    public UserProfileSortableDataProvider(UserProfileService userProfileService) {
        super();
        this.userProfileService = userProfileService;
        // setSort(LightVac.SortedFieldNames.dateTime.value(), true);
    }

    /**
     * 
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int,
     *      int)
     */
    @Override
    public final Iterator<UserProfile> iterator(int first, int count) {
        return Collections.synchronizedList(new ArrayList<UserProfile>(results)).subList(first, first + count).iterator();
    }

    /**
     * 
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    @Override
    public final int size() {
        return results.size();
    }

    @Override
    public final IModel<UserProfile> model(UserProfile userProfile) {
        return new UserProfileDetachableModel(userProfile, userProfileService);
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
    public final void fetchResults() throws JackWicketException {
        results = userProfileService.getAll();
    }

}
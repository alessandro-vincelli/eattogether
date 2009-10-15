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

import it.av.eatt.ocm.model.UserProfile;
import it.av.eatt.service.UserProfileService;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class UserProfileDetachableModel extends LoadableDetachableModel<UserProfile> {

    private static final long serialVersionUID = 1L;
    private final String id;
    private UserProfileService userProfileService;

    /**
     * 
     * @param c
     * @param userProfileService
     */
    public UserProfileDetachableModel(UserProfile c, UserProfileService userProfileService) {
        this(c.getName());
        this.userProfileService = userProfileService;
    }

    /**
     * @param id
     */
    public UserProfileDetachableModel(String id) {
        if (id.equals("")) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    /**
     * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
     * 
     * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof UserProfileDetachableModel) {
            UserProfileDetachableModel other = (UserProfileDetachableModel) obj;
            return other.id == id;
        }
        return false;
    }

    /**
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
    @Override
    protected final UserProfile load() {
        return userProfileService.getByName(id);
    }
}
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
package it.av.eatt.web.security;

import it.av.eatt.ocm.model.Eater;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.springframework.security.Authentication;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;

/**
 * Implements the authentication strategies
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class SecuritySession extends AuthenticatedWebSession {
    /**
     * Construct.
     * 
     * @param request The current request object
     */
    public SecuritySession(Request request) {
        super(request);
    }

    private static final long serialVersionUID = 1L;
    private Authentication auth;
    private String username = "";
    private String[] roles;
    private Eater loggedInUser;

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public boolean authenticate(final String username, final String password) {

        // Check username and password
        try {
            auth = AuthenticationProvider.authenticate(username, password);
            // Object credentials = auth.getCredentials();
            GrantedAuthority[] authss = auth.getAuthorities();
            this.roles = new String[authss.length];
            for (int i = 0; i < authss.length; i++) {
                this.roles[i] = authss[i].getAuthority();
            }
            loggedInUser = ((UserDetailsImpl) auth.getPrincipal()).getUser();
            return auth.isAuthenticated();
        } catch (BadCredentialsException e) {
            // in general this error on a not existing user
            return false;
        }
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles() {
        if (isSignedIn()) {
            return new Roles(this.roles);
        }
        return null;
    }

    /**
     * @return the auth
     */
    public Authentication getAuth() {
        return auth;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public Eater getLoggedInUser() {
        return loggedInUser;
    }

}
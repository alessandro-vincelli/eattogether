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
package it.av.eatt.web;

import it.av.eatt.web.page.ErrorPage;
import it.av.eatt.web.page.FriendsPage;
import it.av.eatt.web.page.HomePage;
import it.av.eatt.web.page.RistoranteEditAddressPage;
import it.av.eatt.web.page.RistoranteViewPage;
import it.av.eatt.web.page.SearchFriendPage;
import it.av.eatt.web.page.SignIn;
import it.av.eatt.web.page.SignUpPage;
import it.av.eatt.web.page.UserAccountPage;
import it.av.eatt.web.page.UserHomePage;
import it.av.eatt.web.page.UserManagerPage;
import it.av.eatt.web.page.UserProfilePage;
import it.av.eatt.web.page.ViewUserPage;
import it.av.eatt.web.security.SecuritySession;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Main class Application.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class WicketApplication extends AuthenticatedWebApplication {
    @SpringBean
    private String configurationType; 
    
    /**
     * Constructor
     */
    public WicketApplication() {
    }

    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return SecuritySession.class;
    }

    protected Class<? extends WebPage> getSignInPageClass() {
        return SignIn.class;
    }
    
    @Override
    protected final void init() {
        super.init();
        // THIS LINE IS IMPORTANT - IT INSTALLS THE COMPONENT INJECTOR THAT WILL
        // INJECT NEWLY CREATED COMPONENTS WITH THEIR SPRING DEPENDENCIES
        if(getSpringContext() != null){
            addComponentInstantiationListener(new SpringComponentInjector(this, getSpringContext()));
        }
        mount(new IndexedParamUrlCodingStrategy("/signIn", SignIn.class));
        mount(new HybridUrlCodingStrategy("/userProfile", UserProfilePage.class));
        mount(new HybridUrlCodingStrategy("/userPage", UserManagerPage.class));
        //mount(new HybridUrlCodingStrategy("/ristorante", RistoranteAddNewPage.class));
        mount(new HybridUrlCodingStrategy("/ristoranteEdit", RistoranteEditAddressPage.class));
        mount(new HybridUrlCodingStrategy("/ristoranteView", RistoranteViewPage.class));
        mount(new HybridUrlCodingStrategy("/searchFriends", SearchFriendPage.class));
        mount(new HybridUrlCodingStrategy("/friends", FriendsPage.class)); 
        mount(new IndexedParamUrlCodingStrategy("/signUp", SignUpPage.class));
        mount(new IndexedParamUrlCodingStrategy("/userHomePage", UserHomePage.class));
        mount(new HybridUrlCodingStrategy("/viewuser", ViewUserPage.class));
        mount(new HybridUrlCodingStrategy("/account", UserAccountPage.class));
        
        getApplicationSettings().setInternalErrorPage(ErrorPage.class);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    public final Class getHomePage() {
        //If loggedIn different Home Page
        if(getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(UserHomePage.class)){
            return UserHomePage.class;
        }
        return HomePage.class;
    }

    /**
     * @return WebApplicationContext
     */
    public final WebApplicationContext getSpringContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    @Override
    public String getConfigurationType() {
        return this.configurationType;
    }

    @Override
    protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
        return new UploadWebRequest(servletRequest);
    }
    
    public final void setConfigurationType(String configurationType) {
        this.configurationType = configurationType;
    }

}

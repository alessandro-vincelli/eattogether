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

import it.av.eatt.ocm.model.Eater;
import it.av.eatt.web.security.SecuritySession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.StringResourceModel;

/**
 * 
 * Contains some commons elements.
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 */

public class BasePage extends WebPage {

    //private static final CompressedResourceReference BASEPAGE_JS = new CompressedResourceReference(BasePage.class, "BasePage.js");
    private static final CompressedResourceReference STYLES_CSS = new CompressedResourceReference(BasePage.class, "resources/styles.css");
    private FeedbackPanel feedbackPanel;
    private boolean isAuthenticated = false;
    private Eater loggedInUser = null;
	/**
	 * Construct.
	 */
	public BasePage() {
        if(((SecuritySession)getSession()).getAuth() != null && ((SecuritySession)getSession()).getAuth().isAuthenticated()){
            isAuthenticated = true;
        }
        loggedInUser = ((SecuritySession)getSession()).getLoggedInUser();
	    //add(JavascriptPackageResource.getHeaderContribution(BASEPAGE_JS));
	    add(CSSPackageResource.getHeaderContribution(STYLES_CSS));

	    feedbackPanel = new FeedbackPanel("feedBackPanel");
	    feedbackPanel.setOutputMarkupId(true);
	    add(feedbackPanel);
        
	    add(new AjaxLink<String>("goUserPage") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(UserManagerPage.class)) {
                    setResponsePage(UserManagerPage.class);
                }
            }
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    private static final long serialVersionUID = 1L;
                    public CharSequence decorateScript(CharSequence script) {
                        if (!(getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(UserManagerPage.class))) {
                            return "alert('" + new StringResourceModel("basePage.notLogged", getPage(), null).getString() + "'); " + script;
                        }
                        return script;
                    }
                };
            }
        });

        add(new Link<String>("goUserProfilePage") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                setResponsePage(UserProfilePage.class);
            }
        });
        
        add(new AjaxLink<String>("goRistoranteAddNewPage") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(RistoranteAddNewPage.class)) {   
                    setResponsePage(RistoranteAddNewPage.class);
                }
            }
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    private static final long serialVersionUID = 1L;
                    public CharSequence decorateScript(CharSequence script) {
                        if (!(getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(RistoranteAddNewPage.class))) {
                            return "alert('" + new StringResourceModel("basePage.notLogged", getPage(), null).getString() + "'); " + script;
                        }
                        return script;
                    }
                };
            }
        });
        
        add(new AjaxLink<String>("goSearchFriendPage") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(SearchFriendPage.class)) {   
                    setResponsePage(SearchFriendPage.class);
                }
            }
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    private static final long serialVersionUID = 1L;
                    public CharSequence decorateScript(CharSequence script) {
                        if (!(getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(SearchFriendPage.class))) {
                            return "alert('" + new StringResourceModel("basePage.notLogged", getPage(), null).getString() + "'); " + script;
                        }
                        return script;
                    }
                };
            }
        });
        
        add(new AjaxLink<String>("goFriendPage") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(FriendPage.class)) {   
                    setResponsePage(FriendPage.class);
                }
            }
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    private static final long serialVersionUID = 1L;
                    public CharSequence decorateScript(CharSequence script) {
                        if (!(getApplication().getSecuritySettings().getAuthorizationStrategy().isInstantiationAuthorized(FriendPage.class))) {
                            return "alert('" + new StringResourceModel("basePage.notLogged", getPage(), null).getString() + "'); " + script;
                        }
                        return script;
                    }
                };
            }
        });
        
        Link<String> goSignOut = new Link<String>("goSignOut") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                setResponsePage(SignOut.class);
            }
        };
        Link<String> goSignIn = new Link<String>("goSignIn") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                setResponsePage(SignIn.class);
            }
        };
        
        goSignIn.setOutputMarkupId(true);
        goSignOut.setOutputMarkupId(true);
        
        if(isAuthenticated){
            goSignIn.setVisible(false);
            goSignOut.setVisible(true);
        }
        else{
            goSignOut.setVisible(false);
            goSignIn.setVisible(true);
        }
        add(goSignOut);
        add(goSignIn);   
        
	}

    public final FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    public Eater getLoggedInUser() {
        return loggedInUser;
    }

}

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
import it.av.eatt.UserAlreadyExistsException;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.data.Country;
import it.av.eatt.service.CountryService;
import it.av.eatt.service.UserService;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * The panel provides the Sign Up form
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 * 
 */
public class SignUpPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private Form<Eater> signUpForm;
    private FeedbackPanel feedbackPanel;
    private UserService userService;
    private CountryService countryService;
    private Link<String> goSignInAfterSignUp;
    private final CaptchaImageResource captchaImageResource;
    private final String imagePass = randomString(6, 8);
    private Image captchaImage;

    /**
     * Constructor
     * 
     * @param id
     * @param feedbackPanel
     * @param userService
     * @throws JackWicketException 
     */
    public SignUpPanel(String id, FeedbackPanel feedbackPanel, UserService userService, CountryService countryService) throws JackWicketException {
        super(id);
        this.userService = userService;
        this.countryService = countryService;
        this.feedbackPanel = feedbackPanel;

        RfcCompliantEmailAddressValidator emailAddressValidator = RfcCompliantEmailAddressValidator.getInstance();
        StringValidator pwdValidator = StringValidator.LengthBetweenValidator.lengthBetween(6, 20);

        Eater user = new Eater();
        signUpForm = new Form<Eater>("signUpForm", new CompoundPropertyModel<Eater>(user));
        signUpForm.setOutputMarkupId(true);
        signUpForm.add(new RequiredTextField<String>(Eater.FIRSTNAME));
        signUpForm.add(new RequiredTextField<String>(Eater.LASTNAME));
        signUpForm.add(new RequiredTextField<String>(Eater.EMAIL).add(emailAddressValidator));
        List<Country> countriyList =  countryService.getAll();
        Country userCountry = null;
        for (Country country : countriyList) {
            if(country.getIso3().equals(getRequest().getLocale().getISO3Country())){
                userCountry = country;
            }
        }
        DropDownChoice<Country> country = new DropDownChoice<Country>(Eater.COUNTRY, countryService.getAll());
        
        //country.setDefaultModelObject(userCountry);
        signUpForm.add(country);
        PasswordTextField pwd1 = new PasswordTextField(Eater.PASSWORD);
        pwd1.add(pwdValidator);
        signUpForm.add(pwd1);
        SubmitButton submitButton = new SubmitButton("buttonCreateNewAccount", signUpForm);
        signUpForm.add(submitButton);
        signUpForm.setDefaultButton(submitButton);

        goSignInAfterSignUp = new Link<String>("goSignInAfterSignUp") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(SignIn.class);
            }
        };
        goSignInAfterSignUp.setOutputMarkupId(true);
        goSignInAfterSignUp.setVisible(false);
        add(goSignInAfterSignUp);

        captchaImageResource = new CaptchaImageResource(imagePass);
        captchaImage = new Image("captchaImage", captchaImageResource);
        captchaImage.setOutputMarkupId(true);
        signUpForm.add(new Image("captchaImage", captchaImageResource));
        RequiredTextField<String> captchaInput = (new RequiredTextField<String>("captchaInput", new Model<String>("captchaInput")) {
            @Override
            protected final void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                // clear the field after each render
                tag.put("value", "");
            }
        });
        signUpForm.add(captchaInput);
        CaptchaValidator captchaValidator = new CaptchaValidator(captchaImageResource, captchaInput);
        signUpForm.add(captchaValidator);
        add(signUpForm);
    }

    private static class SubmitButton extends AjaxButton {
        private static final long serialVersionUID = 1L;
        
        public SubmitButton(String id, Form<Eater> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form form) {
            SignUpPanel panel = (SignUpPanel)getParent().getParent();
            try {
                Eater user = (Eater) form.getModelObject();
                if (user.getId() != 0) {
                    panel.getFeedbackPanel().info(new StringResourceModel("info.operationNotPermitted", this, null).getString());
                } else {
                	System.out.println(target.getPage().getRequest().getLocale());
                    user = panel.userService.addRegolarUser(user);
                    panel.signUpForm.setVisible(false);
                    panel.goSignInAfterSignUp.setVisible(true);
                }
            } catch (UserAlreadyExistsException e) {
                panel.getFeedbackPanel().error(new StringResourceModel("error.userAlreadyExistsException", this, null).getString());
            } catch (JackWicketException e) {
                panel.getFeedbackPanel().error(new StringResourceModel("error.operationNotPermitted", this, null).getString());
            }
            target.addComponent(getPage());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form form) {
            SignUpPanel panel = (SignUpPanel)getParent().getParent();
            panel.getFeedbackPanel().anyErrorMessage();
            target.addComponent(panel.getFeedbackPanel());
            target.addComponent(form);
        }
    }

    public final FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    private static String randomString(int min, int max) {
        int num = randomInt(min, max);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++)
            b[i] = (byte) randomInt('a', 'z');
        return new String(b);
    }

    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * Very simple CaptchaValidator
     * 
     * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
     */
    private class CaptchaValidator extends AbstractFormValidator {

        private static final long serialVersionUID = 1L;
        /** form components to be checked. */
        private final FormComponent<?>[] components;
        private final CaptchaImageResource captchaImageResource;

        /**
         * Construct.
         * 
         * @param CaptchaImageResource
         *            a captcha image
         * @param formComponent
         *            a form component
         */
        public CaptchaValidator(CaptchaImageResource captchaImageResource, FormComponent<?> formComponent) {
            if (captchaImageResource == null) {
                throw new IllegalArgumentException("argument captchaImageResource cannot be null");
            }
            if (formComponent == null) {
                throw new IllegalArgumentException("argument formComponent cannot be null");
            }
            this.captchaImageResource = captchaImageResource;
            this.components = new FormComponent[] { formComponent };
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return components;
        }

        @Override
        public void validate(Form<?> form) {
            // we have a choice to validate the type converted values or the raw
            // input values, we validate the raw input
            final FormComponent<?> formComponent = components[0];

            if (!Objects.equal(formComponent.getInput(), captchaImageResource.getChallengeId())) {
                // force redrawing
                captchaImageResource.invalidate();
                error(formComponent);
            }
        }
    }
}

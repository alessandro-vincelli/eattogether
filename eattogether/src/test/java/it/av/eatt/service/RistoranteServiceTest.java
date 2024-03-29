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
package it.av.eatt.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.Language;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.RistoranteDescriptionI18n;
import it.av.eatt.ocm.model.RistorantePicture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class RistoranteServiceTest {
    @Autowired
    private RistoranteService ristoranteService;
    @Autowired
    @Qualifier("userService")
    private EaterService userService;
    @Autowired
    private LanguageService languageService;

    private Eater user;

    @Before
    public void setUp() {
        user = new Eater();
        user.setFirstname("Alessandro");
        user.setLastname("Vincelli");
        user.setPassword("secret");
        user.setEmail("a.ristoranteService@test.com");

        try {
            user = userService.addRegolarUser(user);
        } catch (JackWicketException e) {
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() throws JackWicketException {

        // user = userService.getByEmail(user.getEmail());
        // userService.remove(user);

    }

    @Test
    public void testRistoranteService() throws JackWicketException, IOException {

        Ristorante a = new Ristorante();
        a.setName("RistoAlessandro");
        a.setDescriptions(getDescriptionI18ns());
        a = ristoranteService.insert(a, user);

        assertNotNull(a.getRevisions());
        assertEquals(a.getRevisions().size(), 1);

        // a = ristoranteService.getByPath(a.getPath());
        a = ristoranteService.getByID(a.getId());
        assertNotNull("A is null", a);
        assertEquals("Invalid value for test", "RistoAlessandro", a.getName());
        assertNotNull(a.getDescriptions());
        assertNotNull(a.getDescriptions().get(0));
        assertTrue(a.getDescriptions().get(0).getDescription().equals("description"));

        a = ristoranteService.addRate(a, user, 1);

        // assertTrue(ristoranteService.hasUsersAlreadyRated(a, user));

        assertTrue(a.getRates().size() > 0);
        assertTrue(a.getRates().get(0).getRate() == 1);

        try {
            a = ristoranteService.addRate(a, user, 1);
            fail("Exception doesn't throw on multi rated operation");
        } catch (Exception e) {
            // expected
        }

        a = ristoranteService.update(a, user);
        assertTrue("Rates losts after update ", a.getRates().get(0).getRate() == 1);

        Collection<Ristorante> all = ristoranteService.getAll();
        assertNotNull(all);
        assertTrue(all.size() > 0);

        a.setName("newName");
        a = ristoranteService.update(a, user);

        a = ristoranteService.getByID(a.getId());
        assertNotNull("A is null", a);
        assertEquals("Invalid value for test", "newName", a.getName());
        
        //add a picture
        RistorantePicture img = new RistorantePicture();
        img.setPicture(FileUtils.readFileToByteArray(new File(this.getClass().getResource("images/test.png").getFile())));
        a.addPicture(img);
        a = ristoranteService.update(a, user);

        a = ristoranteService.getByID(a.getId());
        assertNotNull("A is null", a.getPictures());
        assertEquals("Invalid value for test", 1, a.getPictures().size());

        ristoranteService.remove(a);

    }

    private List<RistoranteDescriptionI18n> getDescriptionI18ns() throws JackWicketException {
        List<Language> languages = languageService.getAll();
        List<RistoranteDescriptionI18n> descriptionI18ns = new ArrayList<RistoranteDescriptionI18n>(languages.size());
        for (Language language : languages) {
            RistoranteDescriptionI18n descriptionI18n = new RistoranteDescriptionI18n();
            descriptionI18n.setLanguage(language);
            descriptionI18n.setDescription("description");
            descriptionI18ns.add(descriptionI18n);
        }
        return descriptionI18ns;
    }

}
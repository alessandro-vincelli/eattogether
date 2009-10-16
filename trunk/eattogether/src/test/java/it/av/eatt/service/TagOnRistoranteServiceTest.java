/**
 * Copyright 2009 the original author or authors Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package it.av.eatt.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Eater;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.Tag;
import it.av.eatt.ocm.model.TagOnRistorante;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TagOnRistoranteServiceTest {
    @Autowired
    private TagOnRistoranteService tagOnRistoranteService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private RistoranteService ristoranteService;

    private Eater user;
    private Ristorante risto;

    @Before
    public void setUp() throws JackWicketException {
        user = new Eater();
        user.setFirstname("Alessandro");
        user.setLastname("Vincelli");
        user.setPassword("secret");
        user.setEmail("a.test@test.test");
        user = userService.addRegolarUser(user);
        assertNotNull("user is null", user);

        risto = new Ristorante();
        risto.setName("RistoAlessandro");
        risto = ristoranteService.insert(risto, user);
        assertNotNull("risto is null", risto);
    }

    @Test
    public void TagOnRistoranteService() throws JackWicketException {

        TagOnRistorante tag = tagOnRistoranteService.insert("tag1");
        assertNotNull(tag);
        List<TagOnRistorante> tags = tagOnRistoranteService.getAll();
        assertNotNull(tags);
        assertTrue(tags.size() > 0);
        risto.addTag(tag);
        ristoranteService.update(risto, user);
        tags = risto.getTags();
        assertNotNull(tags);
        assertTrue(tags.size() > 0);

        tags = tagOnRistoranteService.find(tag.getTag().getTag(), risto);
        assertNotNull(tags);
        assertTrue(tags.size() > 0);

        tags = tagOnRistoranteService.find("tag not existing", risto);
        assertNotNull(tags);
        assertTrue(tags.size() == 0);

        List<Tag> tagItems = tagOnRistoranteService.findTagsNotLinked(tag.getTag().getTag(), risto);
        assertNotNull(tagItems);

        risto.setTags(new ArrayList<TagOnRistorante>());
        ristoranteService.update(risto, user);
        tagOnRistoranteService.remove(tag);

        Tag tagNotLinked = tagService.insert("tagnotLinked");

        tagItems = tagOnRistoranteService.findTagsNotLinked(tag.getTag().getTag(), risto);
        assertNotNull(tagItems);
        assertTrue(tagItems.size() > 0);

        tagService.remove(tagNotLinked);

    }

    @After
    public void terDown() throws JackWicketException {
        ristoranteService.remove(ristoranteService.getByID(risto.getId()));
        userService.remove(user);
        for (Tag tag : tagService.getAll()) {
            tagService.remove(tag);
        }
    }

}

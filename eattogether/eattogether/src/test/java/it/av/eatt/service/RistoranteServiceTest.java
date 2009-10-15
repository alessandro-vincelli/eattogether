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
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.User;

import java.util.Collection;

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
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class RistoranteServiceTest {
    @Autowired
    private RistoranteService ristoranteService;
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    User user;

    @Before
    public void setUp() {
        user = new User();
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
    public void tearDown() {
        try {
            //refresh the user, to remove all the related activities
            user = userService.getByEmail(user.getEmail());
            userService.remove(user);
            /*List<ActivityRistorante> activities = activityRistoranteService.getAll();
            for (ActivityRistorante activityRistorante : activities) {
				activityRistoranteService.remove(activityRistorante);
			}*/
        } catch (JackWicketException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRistoranteService() throws JackWicketException {
        try {
            Ristorante a = new Ristorante();
            a.setName("RistoAlessandro");

            a = ristoranteService.insert(a, user);
            
            
           assertNotNull(a.getRevisions());
            assertEquals(a.getRevisions().size(), 1);
            
            //a = ristoranteService.getByPath(a.getPath());
            a = ristoranteService.getByID(a.getId());
            assertNotNull("A is null", a);
            assertEquals("Invalid value for test", "RistoAlessandro", a.getName());
            
            a = ristoranteService.addRate(a, user, 1);
            
            assertTrue(ristoranteService.hasUsersAlreadyRated(a, user));
            
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
            
            
            a = ristoranteService.addTag(a, user, "tag1");
            
            assertTrue(a.getTags().size() > 0);
            assertEquals(a.getTags().get(0).getTag().getTag(), "tag1");
            
            a = ristoranteService.addTag(a, user, "tag2");
            
            assertTrue(a.getTags().size() == 2);
            
            Collection<Ristorante> all = ristoranteService.getAll();
            assertNotNull(all);
            assertTrue(all.size() > 0);

            a.setName("newName");
            a = ristoranteService.update(a, user);

            a = ristoranteService.getByID(a.getId());
            assertNotNull("A is null", a);
            assertEquals("Invalid value for test", "newName", a.getName());

            ristoranteService.remove(a);
            

        } catch (JackWicketException e) {
            fail(e.getMessage());
        }
    }

}
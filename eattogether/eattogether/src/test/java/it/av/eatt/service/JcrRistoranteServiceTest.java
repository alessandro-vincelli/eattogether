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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.User;

import java.util.List;

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

import com.mchange.util.AssertException;

@ContextConfiguration(locations = "classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class JcrRistoranteServiceTest {
    @Autowired
    private RistoranteService ristoranteService;
    @Autowired
    private JcrRistoranteService jcrRistoranteService;
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
         } catch (JackWicketException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUsersBasic() throws JackWicketException {
        try {
            Ristorante a = new Ristorante();
            a.setName("RistoAlessandro");

            a = ristoranteService.insert(a, user);

            jcrRistoranteService.commit(a);
            
            jcrRistoranteService.commit(a);
            
            assertNotNull(a.getPath());
            
            List<Ristorante> revisions = jcrRistoranteService.getAllRevisions(a.getPath());
            assertNotNull(revisions);
            assertTrue(revisions.size() >= 1);
            
            jcrRistoranteService.remove(a);
            ristoranteService.remove(a);
            

        } catch (JackWicketException e) {
            fail(e.getMessage());
        }
    }

}
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
import it.av.eatt.ocm.model.UserProfile;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class UserProfileServiceTest{

	@Autowired
	private UserProfileService userProfileService;

	@Test
	public void testUsersBasic() throws JackWicketException {
		try {
			UserProfile a = new UserProfile();
			a.setName("ProfileTest");
			
			userProfileService.save(a);
			
			assertNotNull("A is null", a);
			assertEquals("Invalid value for test", "ProfileTest", a.getName());
		
			Collection<UserProfile> all = userProfileService.getAll();
			assertNotNull(all);
			assertTrue(all.size() > 0);
			
			userProfileService.remove(a);
			
		} 
		catch (JackWicketException e) {
			fail(e.getMessage());
		}
	}
}
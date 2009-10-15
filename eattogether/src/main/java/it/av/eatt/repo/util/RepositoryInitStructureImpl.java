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
package it.av.eatt.repo.util;

import it.av.eatt.JackWicketException;
import it.av.eatt.ocm.model.Folder;
import it.av.eatt.ocm.model.Ristorante;
import it.av.eatt.ocm.model.User;
import it.av.eatt.ocm.model.UserProfile;
import it.av.eatt.service.RistoranteService;
import it.av.eatt.service.UserProfileService;
import it.av.eatt.service.UserService;
import it.av.eatt.service.impl.JcrApplicationServiceJackrabbit;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

/**
 * Check the base SC repository structure and eventually create the base nodes
 * 
 * @author <a href='mailto:a.vincelli@gmail.com'>Alessandro Vincelli</a>
 */
@Transactional
public class RepositoryInitStructureImpl extends JcrApplicationServiceJackrabbit<Folder> implements RepositoryInitStructure {

    private List<String> paths;
    @Resource(name="userProfileService")
    private UserProfileService userProfileService;
    @Resource(name="userService")
    private UserService userService;
    @Resource(name="ristoranteService")
    private RistoranteService ristoranteService;

    /*
     * (non-Javadoc)
     * 
     * @see it.av.eatt.repo.util.RepositoryInitStructure#checkBasePaths()
     */
    @Override
    public void checkBasePaths() throws JackWicketException {
        for (String path : paths) {
            checkPath(path);
        }
    }

    private void checkPath(String path) throws JackWicketException {
        if (!(getJcrMappingtemplate().itemExists(path))) {
            createNode(path);
        }
    }

    private void createNode(String path) throws JackWicketException {
        Folder folder = new Folder();
        folder.setPath(path);
        getJcrMappingtemplate().insert(folder);
        getJcrMappingtemplate().save();
    }

    @Override
    public void checkBaseData() throws JackWicketException {
        if (userProfileService.getAll().size() == 0){
            UserProfile profileADMIN = new UserProfile();
            profileADMIN.setName("ADMIN");
            profileADMIN.setDescription("Administration role");
            UserProfile profileUSER = new UserProfile();
            profileUSER.setName("USER");
            profileUSER.setDescription("Regular user role");
            profileADMIN = userProfileService.save(profileADMIN);
            profileUSER = userProfileService.save(profileUSER);
            
            User userADMIN = new User();
            userADMIN.setEmail("admin@admin.demo");
            userADMIN.setFirstname("Admin FN");
            userADMIN.setLastname("Admin LN");
            userADMIN.setPassword("admin");
            userADMIN.setUserProfile(profileADMIN);
            
            User userUSER = new User();
            userUSER.setEmail("user@user.demo");
            userUSER.setFirstname("User FN");
            userUSER.setLastname("User LN");
            userUSER.setPassword("user");
            userUSER.setUserProfile(profileUSER);
            
            User userAUSER = new User();
            userAUSER.setEmail("a@user.demo");
            userAUSER.setFirstname("User FA");
            userAUSER.setLastname("User LA");
            userAUSER.setPassword("user");
            userAUSER.setUserProfile(profileUSER);
            
            
            User userBUSER = new User();
            userBUSER.setEmail("b@user.demo");
            userBUSER.setFirstname("User FB");
            userBUSER.setLastname("User LB");
            userBUSER.setPassword("user");
            userBUSER.setUserProfile(profileUSER);
            
            Ristorante risto = new Ristorante();
            risto.setName("Default Risto");
            risto.setAddress("via roma");
            risto.setCity("Terni");
            risto.setCountry("Italy");
            risto.setDescription("very short descrption");
            risto.setPostalCode("05100");
            risto.setProvince("TR");
            
            userService.add(userADMIN);
            userService.addRegolarUser(userAUSER);
            userService.addRegolarUser(userBUSER);
            userService.addRegolarUser(userUSER);
            ristoranteService.insert(risto, userAUSER);
        }
        
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    /*public final void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public final void setUserService(UserService userService) {
        this.userService = userService;
    }*/
    
}

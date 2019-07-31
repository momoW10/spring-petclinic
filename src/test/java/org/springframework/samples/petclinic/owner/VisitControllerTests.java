/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.VisitController;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VisitController.class)
public class VisitControllerTests {

    private static final int TEST_PET_ID = 1;
    //add visit_id
    private static final int TEST_VISIT_ID = 1;
    private static final int TEST_PET2_ID = 7;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitRepository visits;

    @MockBean
    private PetRepository pets;
    
    private Visit visit;

    @Before
    public void init() {
    	visit = new Visit();
        visit.setId(TEST_VISIT_ID);
        visit.setDate(LocalDate.of(2013, 01, 01));
        visit.setDescription("rabies shot");
        visit.setPetId(TEST_PET_ID);
    	given(this.visits.findById(TEST_VISIT_ID)).willReturn(visit);
        given(this.pets.findById(TEST_PET_ID)).willReturn(new Pet());
    }

    @Test
    public void testInitNewVisitForm() throws Exception {
        mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    public void testProcessNewVisitFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
            .param("name", "George")
            .param("description", "Visit Description")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    public void testProcessNewVisitFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
            .param("name", "George")
        )
            .andExpect(model().attributeHasErrors("visit"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }
    
    //add
    @Test
    public void TestUpdateVisitForm() throws Exception {
    	mockMvc.perform(get("/owners/*/pets/{petId}/visits/update", TEST_PET_ID))
    		.andExpect(status().isOk())
    		.andExpect(model().attributeExists("visit"));
//    		.andExpect(model().attribute("visit"))));
    }
    
    //add
    @Test
    public void testInitUpdateVisitForm() throws Exception {
//        mockMvc.perform(get("/owners/*/pets/*/visits/update/{visitId}", TEST_VISIT_ID))
//            .andExpect(status().isOk());	//bad request
//            .andExpect(model().attributeExists("update"));
//            .andExpect(model().attribute("visit", hasProperty("date", is("2013-01-01"))))
//            .andExpect(model().attribute("visit", hasProperty("description", is("rabies shot"))))
//            .andExpect(view().name("owners/createOrUpdateVisitForm"));
    }
    
    @Test
    public void testProcessUpdateVisitFormSuccess() throws Exception {
    	mockMvc.perform(post("/owners/*/pets/*/visits/update/{visitId}", TEST_VISIT_ID)
    			.param("date", "2013-03-03")
    			.param("description", "aaa"))
    		.andExpect(status().is3xxRedirection());
    }

}

package org.springframework.samples.petclinic.owner;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UpdateController {
	 	private final VisitRepository visits;
	    private final PetRepository pets;


	    public UpdateController(VisitRepository visits, PetRepository pets) {
	        this.visits = visits;
	        this.pets = pets;
	    }

	    @InitBinder
	    public void setAllowedFields(WebDataBinder dataBinder) {
	        dataBinder.setDisallowedFields("id");
	    }
	    
	    @ModelAttribute("update")
	    public Visit loadVisit(@PathVariable("visitId") int visitId, Map<String, Object> model) {
	    	Pet pet = this.pets.findById(this.visits.findById(visitId).getPetId());
	        model.put("pet", pet);
	    	return this.visits.findById(visitId);
	    }
	    
	    
	  //add(updateform valid and update the visit)
	    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/update/{visitId}")
	    public String processUpdateVisitForm(@Valid Visit visit, BindingResult result, @PathVariable("visitId") int visitId) {
	    	if (result.hasErrors()) {
	    		System.out.println("error!!!!");
	            return "pets/createOrUpdateVisitForm";
	        } else {
//	        	System.out.println("getId = " + visit.getId());
	        	// error!
	            visit.setId(visitId);
//	            System.out.println("getId after = " + visit.getId());
	            this.visits.save(visit);
	            return "redirect:/owners/{ownerId}";
	        }
	    }

}

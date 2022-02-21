package com.qpf.controllers.back;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Acompanyament;
import com.qpf.service.AcompanyamentService;

@Controller
public class AcompanyamentBackController {

	@Autowired
	private AcompanyamentService acompanyamentService;

    
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES ---------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    
    // restriccions:
    // - permet modificar el contingut de les preguntes però no eliminar-les.
    // - només pels administradors
	
    // --------------------------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------------------------
    
    // llista
	@GetMapping(value = { "/back/admin/contingut/acompanyament", "/back/admin/contingut/acompanyament/index" })
	public String index (Model model) {	
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		return "back/admin/contingut/acompanyament/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/contingut/acompanyament/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);  
	    Optional<Acompanyament> acompanyament = acompanyamentService.findById(id);
	    if (acompanyament.isPresent())
		    model.addAttribute("acompanyament",acompanyament.get());
		return "back/admin/contingut/acompanyament/edit";
	}
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/contingut/acompanyament/update/{id}" })
	public String update(@PathVariable("id") long id, Acompanyament acompanyament, BindingResult result, Model model) {
		try {
			acompanyamentService.save(acompanyament);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("aco_pos") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
				model.addAttribute("errors", "Error! Posició repetida!");
			else 
				model.addAttribute("errors", e.getMessage());
			return edit(id,model);
		}	    
		// operació ok
	    model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");		
		return edit(id,model);	    
	}
	
}

package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Horari;
import com.qpf.service.HorariService;

import java.util.Optional;

@Controller
public class HorariBackController {
	
	@Autowired
	private HorariService horariService;
    
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
	@GetMapping(value = { "/back/admin/contingut/horari", "/back/admin/contingut/horari/index" })
	public String index (Model model) {	
		model.addAttribute("llistaHoraris", horariService.findAll());
		return "back/admin/contingut/horari/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/contingut/horari/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);	    
	    Optional<Horari> horari = horariService.findById(id);
	    if (horari.isPresent())
		    model.addAttribute("horari",horari.get());
		return "back/admin/contingut/horari/edit";
	}
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/contingut/horari/update/{id}" })
	public String update(@PathVariable("id") long id, Horari horari, BindingResult result, Model model) {
		try {
			horariService.save(horari);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("hor_pos") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
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

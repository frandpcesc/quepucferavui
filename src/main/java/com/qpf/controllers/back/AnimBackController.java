package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Anim;
import com.qpf.service.AnimService;

import java.util.Optional;

@Controller
public class AnimBackController {
	
	@Autowired
	private AnimService animService;
    
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
	@GetMapping(value = { "/back/admin/contingut/anim", "/back/admin/contingut/anim/index" })
	public String index (Model model) {	
		model.addAttribute("llistaAnims", animService.findAll());
		return "back/admin/contingut/anim/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/contingut/anim/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);
	    Optional<Anim> anim = animService.findById(id);
	    if (anim.isPresent())
		    model.addAttribute("anim",anim.get());
		return "back/admin/contingut/anim/edit";
	}
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/contingut/anim/update/{id}" })
	public String update(@PathVariable("id") long id, Anim anim, BindingResult result, Model model) {
		try {
			animService.save(anim);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("ani_pos") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
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

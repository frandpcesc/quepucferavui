package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Distancia;
import com.qpf.service.DistanciaService;

import java.util.Optional;

@Controller
public class DistanciaBackController {
	
	@Autowired
	private DistanciaService distanciaService;
   
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
	@GetMapping(value = { "/back/admin/contingut/distancia", "/back/admin/contingut/distancia/index" })
	public String index (Model model) {	
		model.addAttribute("llistaDistancia", distanciaService.findAll());
		return "back/admin/contingut/distancia/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/contingut/distancia/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);
	    Optional<Distancia> distancia = distanciaService.findById(id);
	    if (distancia.isPresent())
		    model.addAttribute("distancia",distancia.get());
		return "back/admin/contingut/distancia/edit";
	}
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/contingut/distancia/update/{id}" })
	public String update(@PathVariable("id") long id, Distancia distancia, BindingResult result, Model model) {
		try {
		    distanciaService.save(distancia);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("dis_pos") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
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

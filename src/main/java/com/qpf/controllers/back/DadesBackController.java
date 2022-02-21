package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Dades;
import com.qpf.service.DadesService;
import com.qpf.service.UsuariService;

import java.util.Optional;

@Controller
public class DadesBackController {
	   
    @Autowired
    private DadesService dadesService;
    
    @Autowired
    private UsuariService usuariService;
    
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES ---------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    
    // restriccions:
    // - donar d'alta, baixa, modificar i ocultar llistes d'activitats
    // - permet escollir quin usuari gestiona les activitats de la llista
    // - en eliminar una llista, també s'eliminen les seves activitats (demana confirmació)
    // - només pels administradors
	
    // --------------------------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------------------------
    
    // llista
	@GetMapping(value = { "/back/admin/dades", "/back/admin/dades/index" })
	public String index (Model model) {	
		model.addAttribute("llistaDades", dadesService.findAll());
		return "back/admin/dades/index";
	}
	
	
	// formulari insert
	@GetMapping(value = { "/back/admin/dades/add" })
    public String add (Model model, Dades dades) {
		model.addAttribute("llistaUsuaris", usuariService.findByRolesAdminOrPromotor());
		return "back/admin/dades/add";
    }
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/dades/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);
		model.addAttribute("llistaUsuaris", usuariService.findByRolesAdminOrPromotor());	    
	    Optional<Dades> dades = dadesService.findById(id);
	    if (dades.isPresent())
		    model.addAttribute("dades",dades.get());
		return "back/admin/dades/edit";
	}
	
	
	// insert a la base de dades
	@PostMapping(value = { "/back/admin/dades/insert" })
	public String insert (Dades dades, BindingResult result, Model model) {
		try {
			dadesService.save(dades);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("dad_nom") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
				model.addAttribute("errors", "Error! Nom repetit!");
			else 
				model.addAttribute("errors", e.getMessage());
			return add(model,dades);
		}	
		// operació ok: va al formulari de modificació
	    model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");
		return edit(dades.getId(),model);
	}	
	
	
	// delete a la base de dades
	@GetMapping(value = { "/back/admin/dades/delete/{id}" })
	public String delete(@PathVariable long id,  Model model) {
		Optional<Dades> dades = dadesService.findById(id);
		try {
			if (dades.isPresent()) {
				dadesService.deleteById(id);
			}
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");			
			model.addAttribute("errors", e.getMessage());
			return edit(id,model);
		}				
		// operació ok: torna pàgina llista
	    return index(model); 
	}	
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/dades/update/{id}" })
	public String update(@PathVariable("id") long id, Dades dades, BindingResult result, Model model) {
		try {
			dadesService.save(dades);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("dad_nom") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
				model.addAttribute("errors", "Error! Nom repetit!");
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

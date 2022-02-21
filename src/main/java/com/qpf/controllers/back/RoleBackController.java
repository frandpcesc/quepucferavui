package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.qpf.model.Role;
import com.qpf.service.RoleService;

import java.util.Optional;

@Controller
public class RoleBackController {
	
	@Autowired
	private RoleService roleService;
    
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES ---------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    
    // restriccions:
    // - permet modificar només el nom curt del rol
    // - només pels administradors
	
    // --------------------------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------------------------
    
    // llista
	@GetMapping(value = { "/back/admin/rols", "/back/admin/rols/index" })
	public String index (Model model) {	
		model.addAttribute("llistaRoles", roleService.findAll());
		return "back/admin/rols/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/rols/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);	    
	    Optional<Role> role = roleService.findById(id);
	    if (role.isPresent())
		    model.addAttribute("role",role.get());
		return "back/admin/rols/edit";
	}
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/rols/update/{id}" })
	public String update(@PathVariable("id") long id, Role role, BindingResult result, Model model) {
		try {
			roleService.save(role);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			if (e.getMessage().toLowerCase().contains("rol_nomamigable") && e.getMessage().toLowerCase().contains("constraintviolationexception")) 
				model.addAttribute("errors", "Error! Nom curt repetit!");
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

package com.qpf.controllers.back;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
  
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES USUARI ADMINISTRADOR ------------------------------------------------------
    // --------------------------------------------------------------------------------------------
	
    // pàgina índex
	@GetMapping(value = { "/back/admin", "/back/admin/index" })
	public String index(Model model) {
		return "back/admin/index";
	}
	
    // pàgina preguntes
	@GetMapping(value = { "/back/admin/contingut", "/back/admin/contingut/index" })
	public String preguntes(Model model) {
		return "back/admin/contingut/index";
	}	
	
}

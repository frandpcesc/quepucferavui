package com.qpf.controllers.back;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PromotorController {

    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES USUARI PROMOTOR -----------------------------------------------------------
    // --------------------------------------------------------------------------------------------
	
    // pàgina índex
	@GetMapping(value = { "/back/promotor", "/back/promotor/index" })
	public void index(Model model, HttpServletResponse response) throws IOException {
		response.sendRedirect("/back/promotor/activitats");
	}
	
}

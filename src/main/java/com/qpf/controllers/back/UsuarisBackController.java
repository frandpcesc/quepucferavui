package com.qpf.controllers.back;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.qpf.model.Role;
import com.qpf.model.Usuari;
import com.qpf.service.RoleService;
import com.qpf.service.UsuariService;
import com.qpf.utils.ExceptionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UsuarisBackController {
	
	@Autowired
	private UsuariService usuariService;
	
	@Autowired
	private RoleService roleService;
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES ---------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    	
    // --------------------------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------------------------
    // restriccions:
    // - permet deshabilitar, canviar el rol i eliminar usuaris bàsics o promotors de la BBDD.
    // - només pels administradors
    // --------------------------------------------------------------------------------------------
    
    // llista
	@GetMapping(value = { "/back/admin/usuaris", "/back/admin/usuaris/index" })
	public String index (Model model) {	
		model.addAttribute("llistaUsuaris", usuariService.findAll());
		return "back/admin/usuaris/index";
	}
	
	
	// formulari modificació
	@GetMapping(value = { "/back/admin/usuaris/edit/{id}" })
	public String edit (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);
		model.addAttribute("llistaRoles", roleService.findAll());
	    Optional<Usuari> usuari = usuariService.findById(id);    
	    if (usuari.isPresent())
		    model.addAttribute("usuari",usuari.get());
		return "back/admin/usuaris/edit";
	}
	
	
	// delete a la base de dades
	@GetMapping(value = { "/back/admin/usuaris/delete/{id}" })
	public String delete(@PathVariable long id,  Model model) {
		Optional<Usuari> usuari = usuariService.findById(id);
		try {
			if (usuari.isPresent()) {
				// error si l'usuari té llistes assignades
				if (usuari.get().getDades().size()>0) 
					throw new ExceptionUtil("ERROR!! L'usuari té assignades llistes d'activitats");
				// error si esborram un administrador i fa que l'aplicació se quedi sense administradors
				if (usuari.isPresent() && usuari.get().getRole().getName().equals("ROLE_ADMIN") && usuariService.findByRoleAdmin().size()-1==0) 
					throw new ExceptionUtil("ERROR!! L'aplicació no tindrà administradors");				
				// no hi ha errors => esborra usuari
				usuariService.deleteById(id);
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
	@PostMapping(value = { "/back/admin/usuaris/update/{id}" })
	public String update(@PathVariable("id") long id, WebRequest request, Model model) {
		// accountNonLocked (true => usuari bloquejat, no actiu)
		String accountNonLockedString = request.getParameter("accountNonLocked");
		Boolean accountNonLocked = false;
		if (!ObjectUtils.isEmpty(accountNonLockedString) && accountNonLockedString.equals("on")) accountNonLocked = true;
		// id del rol
		Long roleId = Long.parseLong(request.getParameter("role"));
		try {
			// objecte role
			Optional<Role> role = roleService.findById(roleId);
			// error si l'usuari té llistes assignades i desactivam l'usuari o el nou rol de l'usuari és ROLE_USER i 
			Optional<Usuari> usuari = usuariService.findById(id);
			if (usuari.isPresent() && usuari.get().getDades().size()>0 && ((role.isPresent() && role.get().getName().equals("ROLE_USER")) || !accountNonLocked) ) 
				throw new ExceptionUtil("ERROR!! L'usuari té assignades llistes d'activitats");
			// error si és un administrador que li baixam el rol i fa que l'aplicació se quedi sense administradors
			if (usuari.isPresent() && role.isPresent() && 
					usuari.get().getRole().getName().equals("ROLE_ADMIN") && !role.get().getName().equals("ROLE_ADMIN") && 
					usuariService.findByRoleAdmin().size()-1==0) 
				throw new ExceptionUtil("ERROR!! L'aplicació no tindrà administradors");	
			// error si inactivam un administrador i fa que l'aplicació se quedi sense administradors
			if (usuari.isPresent() && usuari.get().getRole().getName().equals("ROLE_ADMIN") && !accountNonLocked && usuariService.findByRoleAdmin().size()-1==0) 
				throw new ExceptionUtil("ERROR!! L'aplicació no tindrà administradors");				
			// no hi ha errors => modifica usuari
			if (role.isPresent())
				usuariService.update(id, role, accountNonLocked);
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");
			model.addAttribute("errors", e.getMessage());
			return edit(id,model);
		}
		// operació ok
	    model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");	
	    return edit(id,model);
	}
	
	 	
    // --------------------------------------------------------------------------------------------
    // Usuari (inclou promotor i administrador)             
    // --------------------------------------------------------------------------------------------
	
	// modificar dades de l'usuari
	@RequestMapping(value = "/back/usuari/dadesUsuari", method = RequestMethod.POST)
	@ResponseBody
	public String dadesUsuari (
						@RequestParam String name, 
						@RequestParam String emailOld, @RequestParam String email, @RequestParam String confirmEmail,
						@RequestParam String passwordOld, @RequestParam String password, @RequestParam String confirmPassword,
						HttpServletRequest request, HttpServletResponse response) throws IOException {

		// usuari loguejat
		Usuari usuari = usuariService.findLoggedUser();

		// errors
   	 	JSONObject errorsJson = new JSONObject();
   	 	ArrayList<String> errors = new ArrayList<String>();
		Integer emailFields = 0;
   	 	Integer passwordFields = 0;
   	 	
		// error si el nou nom i cognoms és el mateix
		if (!name.isEmpty() && name.equals(usuari.getName())) {
			errors.add("El nom i cognoms són els mateixos que hi ha registrats");
		}
   	 	
   	 	// errors al canvi del correu electrònic
   	 	if (!emailOld.isEmpty()) 		emailFields++;
   	 	if (!email.isEmpty()) 			emailFields++;
   	 	if (!confirmEmail.isEmpty()) 	emailFields++;
		if (!emailOld.isEmpty()) {
			// verifica que el correu electrònic actual sigui correcte
   	 		if (!usuari.getEmail().equals(emailOld)) 
   	 			errors.add("El correu electrònic actual no és correcte");
   	 	} 
		if (emailFields>0 && emailFields<3)
	 		// manquen camps per informar pel nou correu electrònic
   	 		errors.add("Heu de confirmar el correu electrònic actual i el nou");	
   	 	else if (!email.isEmpty() && !confirmEmail.isEmpty() && !email.equals(confirmEmail))
   	 		// no són iguals els dos camps que informen del nou correu electrònic
   	 		errors.add("Els camps del nou correu electrònic han de coincidir");
   	 	else if (!emailOld.isEmpty() && !email.isEmpty() && emailOld.equals(email))
   	 		// el nou correu electrònic és el mateix que l'actual		
   	 		errors.add("El nou correu electrònic és el mateix que l'actual");
   	 	else if (!email.isEmpty() && !confirmEmail.isEmpty() && email.equals(confirmEmail) && !EmailValidator.getInstance().isValid(email)) 
	 		errors.add("El nou correu electrònic ha de ser una adreça amb format correcte");   	 		
   	 	
   	 	// errors al canvi de password (contrasenya)
   	 	if (!passwordOld.isEmpty()) 		passwordFields++;
   	 	if (!password.isEmpty()) 			passwordFields++;
   	 	if (!confirmPassword.isEmpty()) 	passwordFields++; 
		if (!passwordOld.isEmpty()) {
			// verifica que la contrasenya actual sigui correcta
   	 		if (!passwordEncoder.matches(passwordOld, usuari.getPassword()))
   	 			errors.add("La contrasenya actual no és correcta");
   	 	} 
		if (passwordFields>0 && passwordFields<3) 
   	 		// manquen camps per informar pel nou password
   	 		errors.add("Heu de confirmar la contrasenya actual i la nova");
		else if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword))
			// no són iguals els dos camps que informen del nou password
			errors.add("Els camps de la nova contrasenya han de coincidir");
		else if (!passwordOld.isEmpty() && !password.isEmpty() && passwordOld.equals(password))
   	 		// el nou password és el mateix que l'actual
			errors.add("La nova contraseya és la mateixa que l'actual");
   	 	else if (!password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword) && password.length()<8)
   	 		// com a mínim vuit caràcters per la contrasenya
	 		errors.add("La nova contrasenya ha de tenir com a mínim vuit caràcters");   
			
		// si no hi ha cap error i l'usuari ha ficat qualque dada per canviar (nom, correu o contrasenya), actualitza dades de l'usuari
		if (errors.size()==0 && (!name.isEmpty() || emailFields==3 || passwordFields==3)) {
			if (!name.isEmpty())	usuari.setName(name);
			if (emailFields==3)		usuari.setEmail(email);
			if (passwordFields==3)	usuari.setPassword(passwordEncoder.encode(password));
   			try {
   				usuariService.save(usuari);
   			} catch (Exception e) {
   				if (e.getMessage().toLowerCase().contains("constraint [usr_email]")) 
   					errors.add("Ja hi ha un compte registrat amb el nou correu electrònic");
   				else 
   					errors.add(e.getMessage());   				 
			}	
		} else if (errors.size()==0) {
			// no hi ha cap dada per canviar (formulari buit)
			errors.add("No ha hagut cap canvi!");
		}
		
   	 	return errorsJson.put("errors", errors).toString();
	}
	
}

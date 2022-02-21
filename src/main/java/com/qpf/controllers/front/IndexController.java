package com.qpf.controllers.front;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qpf.service.AnimService;
import com.qpf.service.DadesService;
import com.qpf.service.DistanciaService;
import com.qpf.service.HorariService;
import com.qpf.service.MunicipiService;
import com.qpf.service.RoleService;
import com.qpf.service.UsuariService;
import com.qpf.service.MailService;
import com.qpf.model.Activitat;
import com.qpf.model.Dades;
import com.qpf.model.Municipi;
import com.qpf.model.Usuari;
import com.qpf.service.AcompanyamentService;

@Controller
public class IndexController {

	@Autowired
	private AnimService animService;

	@Autowired
	private HorariService horariService;

	@Autowired
	private AcompanyamentService acompanyamentService;

	@Autowired
	private MunicipiService municipiService;

	@Autowired
	private DistanciaService distanciaService;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private DadesService dadesService;    
    
    @Autowired
    private UsuariService usuariService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    // --------------------------------------------------------------------------------------------
    // PÀGINES FRONTAL ----------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    
    
    // Pàgina índex -------------------------------------------------------------------------------
    
	// pàgina índex
	@GetMapping(value = { "", "/", "/home", "/index", "/welcome", "/login" })
	public String index(Model model) {
		/*
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		UserDetails ud = null;
		if (principal instanceof UserDetails) {
			ud = (UserDetails)principal;
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		Usuari usuari = usuariService.findByEmail(username);
		*/
		
		Usuari usuari = usuariService.findLoggedUser();

		Activitat activitat = new Activitat();

		model.addAttribute("titol", "index");
		model.addAttribute("activitat", activitat);
		model.addAttribute("llistaAnims", animService.findAll());
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		model.addAttribute("llistaHoraris", horariService.findAll());
		model.addAttribute("llistaMunicipis", municipiService.findAll());
		model.addAttribute("llistaDistancia", distanciaService.findAll());
		model.addAttribute("usuari", usuari);
		return "front/index";
	}
	
	
	// carrega per ajax els municipis segons codi postal
	@RequestMapping(value = "/front/loadMunicipis", method = RequestMethod.POST)
	@ResponseBody
	public List<Municipi> loadMunicipi(@RequestParam String cPostal, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<Municipi> municipis = new ArrayList<Municipi>();
		Boolean error = Boolean.FALSE;
		
		String regex = "\\d{5}";
	    if (!cPostal.matches(regex)) {
	    	// error: el codi postal no té un format vàlid	    	
	    	response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
	    	error = true;
	    }
		
	    if (!error) {
	    	municipis = municipiService.findByCodi(cPostal);
	    	if (municipis.size()==0)  { 
	    		// error : no hi ha cap municipi
	    		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    	}
	    }
	    
		municipis = municipiService.findByCodi(cPostal);
		
	    // tot ok
		return municipis;
	}


	// Contactar ----------------------------------------------------------------------------------
	
	// envia per ajax el correu de contacte
	@RequestMapping(value = "/front/contactar", method = RequestMethod.POST)
	@ResponseBody
	public String contactar(@RequestParam String nom, @RequestParam String telefon, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String message = "Dades de contacte: " + "\nNom: " + nom + "\nTelèfon: " + telefon;
		String subject = "quepucferavui.cat : Nou contacte";
		
        mailService.sendMail("no-reply@quepucferavui.cat","ioc.grup05.qpf@gmail.com",subject,message);		
	    
        // tot ok
		return "ok";
	}	
	
	
	// Recordar contrasenya -----------------------------------------------------------------------
	
	// per ajax genera una nova contrasenya i la envia per correu
	@RequestMapping(value = "/front/recordar", method = RequestMethod.POST)
	@ResponseBody
	public String recordar(@RequestParam String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// errors		
   	 	JSONObject errorsJson = new JSONObject();
   	 	ArrayList<String> errors = new ArrayList<String>();
   	 	
   	 	// usuari
 		Usuari usuari = null;
   	 	
  	 	if (email.isEmpty()) 
   	 		errors.add("El correu electrònic és obligatori");
  	 	else if (!EmailValidator.getInstance().isValid(email)) 
	 		errors.add("Ha de ser una adreça de correu electrònic amb format correcte");
  	 	else {
  	 		usuari = usuariService.findByEmail(email);
  	 		if (usuari==null) {
  	 			errors.add("No hi ha cap usuari amb aquest correu electrònic");
  	 		}
  	 	}
  	 	
  	 	if (errors.size()==0 && usuari!=null) {
  	 		// genera nova contrasenya
  	 		int leftLimit = 48; // numeral '0'
  	 	    int rightLimit = 122; // letter 'z'
  	 	    int targetStringLength = 8;
  	 	    Random random = new Random();
  	 	    String newPassword = random.ints(leftLimit, rightLimit + 1)
  	 	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
  	 	      .limit(targetStringLength)
  	 	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
  	 	      .toString();
  	 	    // actualitza contrasenya de l'usuari
  	 	    usuari.setPassword(passwordEncoder.encode(newPassword));
  	 	    usuariService.save(usuari);
  	 	    // li envia a l'usuari la nova contrasenya per correu
  			String message = "La nova contrasenya per l'usuari " + email + " és " + newPassword;
  			String subject = "quepucferavui.cat : Nova contrasenya";
  	        mailService.sendMail("no-reply@quepucferavui.cat",email,subject,message);		  	 	    
  	 	}
		
	    return errorsJson.put("errors", errors).toString();
	}		
	
	
	// Registre (nous usuaris) --------------------------------------------------------------------
	
	// registre per ajax de nous usuaris
	@RequestMapping(value = "/front/registration", method = RequestMethod.POST)
	@ResponseBody
	public String registration (
			@RequestParam String name, 
			@RequestParam String email, @RequestParam String confirmEmail,
			@RequestParam String password, @RequestParam String confirmPassword,
			@RequestParam String terms,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// errors		
   	 	JSONObject errorsJson = new JSONObject();
   	 	ArrayList<String> errors = new ArrayList<String>();
   	 	
   	 	// errors
   	 	if (name.isEmpty()) 
   	 		errors.add("El nom i cognoms són obligatoris");
   	 	if (email.isEmpty()) 
   	 		errors.add("El correu electrònic és obligatori");
   	 	if (confirmEmail.isEmpty())
   	 		errors.add("Heu de confirmar el correu electrònic");
   	 	if (!email.isEmpty() && !confirmEmail.isEmpty() && !email.equals(confirmEmail)) 
	 		errors.add("Els camps de correu electrònic han de coincidir");
   	 	if (!email.isEmpty() && !confirmEmail.isEmpty() && email.equals(confirmEmail) && !EmailValidator.getInstance().isValid(email)) 
	 		errors.add("Ha de ser una adreça de correu electrònic amb format correcte");   	 	
   	 	if (password.isEmpty()) 
   	 		errors.add("La contrasenya és obligatòria");
   	 	if (confirmPassword.isEmpty()) 		
   	 		errors.add("Heu de confirmar la contrasenya");
   	 	if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)) 
   	 		errors.add("Els camps de la contrasenya han de coincidir");
   	 	if (!password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword) && password.length()<8)  
   	 		errors.add("La contrasenya ha de tenir com a mínim vuit caràcters");   
   	 	if (!Boolean.parseBoolean(terms))
   	 		errors.add("Heu d'acceptar la política de privacitat");
   	 	  	 	
   	 	if (errors.size()==0) {
   	 		Usuari usuari = new Usuari();
   	 		usuari.setName(name);
   	 		usuari.setEmail(email);
   	 		usuari.setPassword(passwordEncoder.encode(password));
   	 		usuari.setAccountNonLocked(true);
   	   	 	usuari.setRole(roleService.findByName("ROLE_USER"));
   			try {
   				usuariService.save(usuari);
   			} catch (Exception e) {
   				if (e.getMessage().toLowerCase().contains("constraint [usr_email]")) 
   					errors.add("Ja hi ha un compte registrat amb aquest correu electrònic");
   				else 
   					errors.add(e.getMessage());   				 
			}	    				
   	 	}
   	 	 	   
	    return errorsJson.put("errors", errors).toString();
	}
	
	
	// Baixa usuari -------------------------------------------------------------------------------
	
	// baixa per ajax de l'usuari
	@RequestMapping(value = "/front/baixa", method = RequestMethod.POST)
	@ResponseBody
	public String baixa(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// errors		
   	 	JSONObject errorsJson = new JSONObject();
   	 	ArrayList<String> errors = new ArrayList<String>();
   	 	
   	 	// usuari
		Long usuari_id = 0L;
		String role = new String("");
		List<Dades> dadesList = new ArrayList<Dades>();
		Usuari usuari = usuariService.findLoggedUser();		
		if(usuari != null) {
			usuari_id = usuari.getId();
			role = usuari.getRole().getName();
			dadesList = usuari.getDades();
		}
		
		// error: l'usuari és administrador
  	 	if (usuari!=null && role.equals("ROLE_ADMIN")) 
   	 		errors.add("Un administrador no pot sol·licitar la baixa.");

  	 	// l'usuari no és un administrador i té llistes assignades => passen a un administrador
  	 	else if (usuari!=null && !role.equals("ROLE_ADMIN") && dadesList.size()>0) {
			List<Usuari> administradorsList = usuariService.findByRoleAdmin();
			List<String> dadesNames = new ArrayList<String>();
			for (Dades dades : dadesList) {
	           dades.setUsuari(administradorsList.get(0));
	           dadesNames.add(dades.getId()+" - "+dades.getName());
	           dadesService.save(dades);
	        }
			if (dadesNames.size()>0) {
				// l'administrador és informat per mail que ara té assignades les llistes d'un usuari que ha sol·licitat la baixa
				String message = "Usuari: "+usuari.getName()+". "+usuari.getEmail()+". Llistes: " + String.join(" ; ",dadesNames);
				String subject = "quepucferavui.cat : Llistes assignades d'un usuari que ha sol·licitat la baixa";
		        mailService.sendMail("no-reply@quepucferavui.cat",administradorsList.get(0).getEmail(),subject,message);
			}
			// error: no hi ha cap administrador per assignar les llistes de l'usuari que sol·licita la baixa
			if (administradorsList.size()==0)
				errors.add("No et podem donar de baixa, tens activitats assignades!");	
			else
				// tot ok => esborra usuari
				usuariService.deleteById(usuari_id);

		// tot ok => esborra usuari
  	 	} else usuariService.deleteById(usuari_id);
   	 	
  	 	
  	 	return errorsJson.put("errors", errors).toString();
	}		
	
	
	// Pàgines estàtiques -------------------------------------------------------------------------
	
	// Que fem
	@GetMapping("/quefem")
	public String quefem(Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		model.addAttribute("usuari",usuari);
		return "front/quefem";
	}
	
	// Anunciar-me aquí
	@GetMapping("/anunciat")
	public String anunciat(Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		model.addAttribute("usuari",usuari);
		return "front/anunciat";
	}
	
	// Politica de privacitat
	@GetMapping("/politica")
	public String politica(Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		model.addAttribute("usuari",usuari);
		return "front/politica";
	}	
	
	// Avís legal
	@GetMapping("/avis")
	public String avis(Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		model.addAttribute("usuari",usuari);
		return "front/avis";
	}	
	
	
	// errors -------------------------------------------------------------------------------------
	
	// error de permisos (403)
	@GetMapping("/access-denied")
	public String accessDenied (Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		model.addAttribute("usuari",usuari);		
		return "front/access-denied";
	}	
	
}

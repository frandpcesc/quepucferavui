package com.qpf.controllers.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.qpf.model.Activitat;
import com.qpf.model.Usuari;
import com.qpf.service.AcompanyamentService;
import com.qpf.service.ActivitatService;
import com.qpf.service.AnimService;
import com.qpf.service.CodiService;
import com.qpf.service.DadesService;
import com.qpf.service.HorariService;
import com.qpf.service.ImatgeService;
import com.qpf.service.MunicipiService;
import com.qpf.service.UsuariService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class ActivitatsBackController {
	
	@Autowired
	private AnimService animService;

	@Autowired
	private HorariService horariService;

	@Autowired
	private ActivitatService activitatService;

	@Autowired
	private AcompanyamentService acompanyamentService;
	
	@Autowired
	private CodiService codiService;

	@Autowired
	private MunicipiService municipiService;

	@Autowired
	private ImatgeService imatgeService;
   
    @Autowired
    private DadesService dadesService;
    
	@Autowired
	private UsuariService usuariService;	
    
    // --------------------------------------------------------------------------------------------
    // PÀGINES PRIVADES ---------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
	
    // --------------------------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------------------------
    
    // llista
	@GetMapping(value = { "/back/admin/activitats", "/back/admin/activitats/index" })
	public String indexAdmin (Model model) {	
		model.addAttribute("llistaActivitats", activitatService.findAll());
		return "back/admin/activitats/index";
	}
	
	
	// formulari insert
	@GetMapping(value = { "/back/admin/activitats/add" })
    public String insertAdminForm (Model model, Activitat activitat) {
		model.addAttribute("llistaAnims", animService.findAll());
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		model.addAttribute("llistaHoraris", horariService.findAll());
		model.addAttribute("llistaMunicipis", municipiService.findAll());
		model.addAttribute("llistaCodis", codiService.findAll());
		// un administrador pot crear activitats només a les llistes assignades als administradors
		model.addAttribute("llistaDadesRoleAdmin", dadesService.findAllByRoleAdmin());
		return "back/admin/activitats/add";
    }
	
	
	// formulari baixa i modificació
	@GetMapping(value = { "/back/admin/activitats/edit/{id}" })
	public String editAdminForm (@PathVariable(value ="id") long id, Model model) {
	    model.addAttribute("id",id);
		model.addAttribute("imatge", imatgeService);
	    Optional<Activitat> activitat = activitatService.findById(id);
	    // la llista on és l'activitat pertany a un usuari administrador o a un usuari promotor?
	    model.addAttribute("isLlistaAdmin",Boolean.FALSE);
	    model.addAttribute("isLlistaPromotor",Boolean.FALSE);
	    if (activitat.isPresent()) { 
		    model.addAttribute("activitat",activitat.get());
		    // la llista on és l'activitat pertany a un usuari administrador o a un usuari promotor?
		    model.addAttribute("isLlistaAdmin",activitat.get().getDades().getUsuari().getRole().getName().equals("ROLE_ADMIN"));
		    model.addAttribute("isLlistaPromotor",activitat.get().getDades().getUsuari().getRole().getName().equals("ROLE_PROMOTOR"));
	    }
		model.addAttribute("llistaAnims", animService.findAll());
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		model.addAttribute("llistaHoraris", horariService.findAll());
		model.addAttribute("llistaMunicipis", municipiService.findAll());
		model.addAttribute("llistaCodis", codiService.findAll());
		model.addAttribute("llistaDades", dadesService.findAll());
		model.addAttribute("llistaDadesRoleAdmin", dadesService.findAllByRoleAdmin());
		return "back/admin/activitats/edit";
	}
	
	
	// insert a la base de dades
	@PostMapping(value = { "/back/admin/activitats/insert" })
	public String insertAdmin (Activitat activitat, BindingResult result, Model model,
			@RequestParam(name = "dataInici") String dataInici, @RequestParam(name = "dataFi") String dataFi,
			@RequestParam(name="fileField",required=false) MultipartFile fileField) throws IOException {
		 try {
			Date dateInici=new SimpleDateFormat("yyyy-MM-dd").parse(dataInici);
			Date dateFi=new SimpleDateFormat("yyyy-MM-dd").parse(dataFi);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			activitat.setDataInici(sdf.parse(sdf.format(dateInici)));
			activitat.setDataFi(sdf.parse(sdf.format(dateFi)));
			activitat.setImatge(fileField.getBytes());
		    activitatService.save(activitat);
		} catch (ParseException e) {
			// errors
			model.addAttribute("ok", "");
			model.addAttribute("errors", e.getMessage());
			return insertAdminForm(model,activitat);
		} 
		// operació ok: va al formulari de modificació
	    model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");
		return editAdminForm(activitat.getId(),model);
	}	
	
	
	// delete a la base de dades
	@GetMapping(value = { "/back/admin/activitats/delete/{id}" })
	public String deleteAdmin(@PathVariable long id,  Model model) {
		Optional<Activitat> activitat = activitatService.findById(id);
		try {
			if (activitat.isPresent()) {
				activitatService.deleteById(id);
			}
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");			
			model.addAttribute("errors", e.getMessage());
			return editAdminForm(id,model);
		}
		// operació ok: torna pàgina llista
	    return indexAdmin(model); 
	}	
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/admin/activitats/update/{id}" })
	public String updateAdmin(@PathVariable("id") long id, Activitat activitat, BindingResult result, Model model,
			@RequestParam(name = "dataInici") String dataInici, @RequestParam(name = "dataFi") String dataFi,
			@RequestParam(name="fileField",required=false) MultipartFile fileField,
			@RequestParam(name="imatge") byte[] imatgeActual) throws IOException {
		 try {
			Date dateInici=new SimpleDateFormat("yyyy-MM-dd").parse(dataInici);
			Date dateFi=new SimpleDateFormat("yyyy-MM-dd").parse(dataFi);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			activitat.setDataInici(sdf.parse(sdf.format(dateInici)));
			activitat.setDataFi(sdf.parse(sdf.format(dateFi)));
			if(fileField == null) {
				activitat.setImatge(imatgeActual);
		    }else if(fileField.isEmpty()) {
		    	activitat.setImatge(imatgeActual);
		    }else {
		    	activitat.setImatge(fileField.getBytes());
		    }
		    activitatService.save(activitat);
		} catch (ParseException e) {
			// errors
			model.addAttribute("ok", "");
			model.addAttribute("errors", e.getMessage());
			return editAdminForm(id,model);
		}  
		// operació ok
		model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");		 
	    return editAdminForm(id,model);	    
	}
	
	
    // --------------------------------------------------------------------------------------------
    // Promotor
    // --------------------------------------------------------------------------------------------	
	  
    // llista
	@GetMapping(value = { "/back/promotor/activitats", "/back/promotor/activitats/index" })
	public String indexPromotor (Model model) {		
		// activitats assignades a l'usuari loguejat
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		if (usuari != null) {
			usuari_id = usuari.getId();
		}
		model.addAttribute("llistaActivitats", activitatService.findAllByUserId(usuari_id));
		model.addAttribute("imatge", imatgeService);
		return "back/promotor/activitats/index";
	}
	
	
	// formulari insert
	@GetMapping(value = { "/back/promotor/activitats/add" })
    public String insertPromotorForm (Model model, Activitat activitat) {
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		Boolean isAdminLoggedUser = Boolean.FALSE;
		Boolean isPromotorLoggedUser = Boolean.FALSE;
		if (usuari != null) {
			usuari_id = usuari.getId();
			isAdminLoggedUser = usuari.getRole().getName().equals("ROLE_ADMIN");
			isPromotorLoggedUser = usuari.getRole().getName().equals("ROLE_PROMOTOR");
		}		
		model.addAttribute("llistaAnims", animService.findAll());
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		model.addAttribute("llistaHoraris", horariService.findAll());
		model.addAttribute("llistaMunicipis", municipiService.findAll());
		model.addAttribute("llistaCodis", codiService.findAll());
		// llistes assignades a l'usuari loguejat
		model.addAttribute("llistaDades", dadesService.findAllByUserId(usuari_id));
	    // l'usuari loguejat és administrador o promotor?
	    model.addAttribute("isAdminLoggedUser", isAdminLoggedUser);
	    model.addAttribute("isPromotorLoggedUser", isPromotorLoggedUser);
		return "back/promotor/activitats/add";
    }
	
	
	// formulari baixa i modificació
	@GetMapping(value = { "/back/promotor/activitats/edit/{id}" })
	public String editPromotorForm (@PathVariable(value ="id") long id, Model model) {
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		Boolean isAdminLoggedUser = Boolean.FALSE;
		Boolean isPromotorLoggedUser = Boolean.FALSE;
		if (usuari != null) {
			usuari_id = usuari.getId();
			isAdminLoggedUser = usuari.getRole().getName().equals("ROLE_ADMIN");
			isPromotorLoggedUser = usuari.getRole().getName().equals("ROLE_PROMOTOR");
		}			
	    model.addAttribute("id",id);
		model.addAttribute("imatge", imatgeService);
	    Optional<Activitat> activitat = activitatService.findById(id);
	    if (activitat.isPresent()) 
		    model.addAttribute("activitat",activitat.get());
		model.addAttribute("llistaAnims", animService.findAll());
		model.addAttribute("llistaCompanys", acompanyamentService.findAll());
		model.addAttribute("llistaHoraris", horariService.findAll());
		model.addAttribute("llistaMunicipis", municipiService.findAll());
		model.addAttribute("llistaCodis", codiService.findAll());
		// llistes assignades a l'usuari loguejat
		model.addAttribute("llistaDades", dadesService.findAllByUserId(usuari_id));
	    // l'usuari loguejat és administrador o promotor?
	    model.addAttribute("isAdminLoggedUser", isAdminLoggedUser);
	    model.addAttribute("isPromotorLoggedUser", isPromotorLoggedUser);		
		return "back/promotor/activitats/edit";
	}
	
	
	// insert a la base de dades
	@PostMapping(value = { "/back/promotor/activitats/insert" })
	public String insertPromotor (Activitat activitat, BindingResult result, Model model,
			@RequestParam(name = "dataInici") String dataInici, @RequestParam(name = "dataFi") String dataFi,
			@RequestParam(name="fileField",required=false) MultipartFile fileField) throws IOException{
		 try {
			Date dateInici=new SimpleDateFormat("yyyy-MM-dd").parse(dataInici);
			Date dateFi=new SimpleDateFormat("yyyy-MM-dd").parse(dataFi);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			activitat.setDataInici(sdf.parse(sdf.format(dateInici)));
			activitat.setDataFi(sdf.parse(sdf.format(dateFi)));
			activitat.setImatge(fileField.getBytes());
			activitatService.save(activitat);
		} catch (ParseException e) {
			// errors
			model.addAttribute("ok", "");
			model.addAttribute("errors", e.getMessage());
			return insertPromotorForm(model,activitat);
		} 
		// operació ok: va al formulari de modificació
	    model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");
		return editPromotorForm(activitat.getId(),model);
	}	
	
	
	// delete a la base de dades
	@GetMapping(value = { "/back/promotor/activitats/delete/{id}" })
	public String deletePromotor(@PathVariable long id,  Model model) {
		Optional<Activitat> activitat = activitatService.findById(id);
		try {
			if (activitat.isPresent()) {
				activitatService.deleteById(id);
			}
		} catch (Exception e) {
			// errors
			model.addAttribute("ok", "");			
			model.addAttribute("errors", e.getMessage());
			return editPromotorForm(id,model);
		}
		// operació ok: torna pàgina llista
	    return indexPromotor(model); 
	}	
	
	
	// update a la base de dades
	@PostMapping(value = { "/back/promotor/activitats/update/{id}" })
	public String updatePromotor(@PathVariable("id") long id, Activitat activitat, BindingResult result, Model model,
			@RequestParam(name = "dataInici") String dataInici, @RequestParam(name = "dataFi") String dataFi,
			@RequestParam(name="fileField",required=false) MultipartFile fileField,
			@RequestParam(name="imatge") byte[] imatgeActual) throws IOException {
		 try {
			Date dateInici=new SimpleDateFormat("yyyy-MM-dd").parse(dataInici);
			Date dateFi=new SimpleDateFormat("yyyy-MM-dd").parse(dataFi);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			activitat.setDataInici(sdf.parse(sdf.format(dateInici)));
			activitat.setDataFi(sdf.parse(sdf.format(dateFi)));
			if(fileField.isEmpty()) {
				activitat.setImatge(imatgeActual);
		    }else {
		    	activitat.setImatge(fileField.getBytes());
		    }
		    activitatService.save(activitat);
		} catch (ParseException e) {
			// errors
			model.addAttribute("ok", "");
			model.addAttribute("errors", e.getMessage());
			return editPromotorForm(id,model);
		}  
		// operació ok
		model.addAttribute("ok", "Canvis guardats");
	    model.addAttribute("errors", "");		 
	    return editPromotorForm(id,model);	    
	}	
}

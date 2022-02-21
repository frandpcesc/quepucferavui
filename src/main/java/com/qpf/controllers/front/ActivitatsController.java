package com.qpf.controllers.front;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qpf.model.Activitat;
import com.qpf.model.Usuari;
import com.qpf.service.AcompanyamentService;
import com.qpf.service.ActivitatService;
import com.qpf.service.AnimService;
import com.qpf.service.DistanciaService;
import com.qpf.service.HorariService;
import com.qpf.service.ImatgeService;
import com.qpf.service.MunicipiService;
import com.qpf.service.UsuariService;
import com.qpf.service.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ActivitatsController {

	@Autowired
	private ActivitatService activitatService;

	@Autowired
	private ImatgeService imatgeService;
	
    @Autowired
    private MailService mailService;
	
	@Autowired
	private MunicipiService municipiService;
	
	@Autowired
	private UsuariService usuariService;
	
	@Autowired 
	private AnimService animService;
	
	@Autowired
	private AcompanyamentService acompanyamentService;
	
	@Autowired
	private HorariService horariService;
	
	@Autowired
	private DistanciaService distanciaService;
	
	
    // --------------------------------------------------------------------------------------------
    // PÀGINES FRONTAL ----------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------	
	
	
	// submit formulari per treure la llista d'activitats
	@PostMapping("/activitats")
	public String activitats(Model model, @RequestParam(name = "anims") String animId,
			@RequestParam(name = "companys") String companyId, @RequestParam(name = "horaris") String horariId,
			@RequestParam(required = false, name = "municipi") String municipiId, @RequestParam(required = false, name = "codi") String codiPostal,
			@RequestParam(required = false, name = "distancia") String distancia)

	{
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		if(usuari != null) {
			usuari_id = usuari.getId();
		}

		Long distance = 0L;
		
		if(distancia != "" && distancia != null) {
			distance = Long.parseLong(distancia);
		}
		
		List<Activitat> activitats = null;
		if(distance == 1) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListByFilters(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}else {
				activitats = activitatService.activitiesListByFiltersAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}
		}else if(distance == 2) {
			if(Long.parseLong(companyId) !=4) {
				activitats = activitatService.activitiesListAprop(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}else {
				activitats = activitatService.activitiesListApropAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}
		}else if(distance == 3) {
			Long provincia = municipiService.findProvinciaId(Long.parseLong(municipiId));
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListLluny(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}else {
				activitats = activitatService.activitiesListLlunyAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}
		}else if(distance == 4 || distance == 0) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListIndeferent(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}else {
				activitats = activitatService.activitiesListIndeferentAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListToday(activitats));
			}
		}
		
		if(codiPostal != "" && municipiId != null && municipiId != "") {
			try {
				model.addAttribute("llistaRespostes", activitatService.activitiesRespostesPreguntes(Long.parseLong(animId), Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal, distance));
				model.addAttribute("nomMunicipi", municipiService.getMunicipiNom(Long.parseLong(municipiId)));
				model.addAttribute("codiPostal", codiPostal);
				model.addAttribute("nomDistancia", distanciaService.getDistanciaNom(Long.parseLong(distancia)));
			} catch (NumberFormatException e) {
				model.addAttribute("llistaRespostes", activitatService.activitiesRespostesPreguntes(Long.parseLong(animId), Long.parseLong(horariId), Long.parseLong(companyId), 0L, codiPostal, distance));
				model.addAttribute("nomMunicipi", null);
				model.addAttribute("codiPostal", null);
				model.addAttribute("nomDistancia", null);
			}
		}else {
			model.addAttribute("llistaRespostes", activitatService.activitiesRespostesPreguntes(Long.parseLong(animId), Long.parseLong(horariId), Long.parseLong(companyId), 0L, codiPostal, distance));
			model.addAttribute("nomMunicipi", null);
			model.addAttribute("codiPostal", null);
			model.addAttribute("nomDistancia", null);
		}		
		
		model.addAttribute("estatAnim", animService.estatAnim(Long.parseLong(animId)));
		model.addAttribute("company", acompanyamentService.getAmbQui(Long.parseLong(companyId)));
		model.addAttribute("franja", horariService.getFranja(Long.parseLong(horariId)));
		
		model.addAttribute("imatge", imatgeService);
		model.addAttribute("titol", "Llista d'activitats");
		model.addAttribute("activitatsPreferides", activitatService.findActivitatsPreferides(usuari_id));
		model.addAttribute("usuari", usuari);
		return "front/resultat";
	}	

	
	@PostMapping("/activitats-dema")
	public String activitatsDema(Model model, @RequestParam("estat") String animId,
			@RequestParam(name = "companys") String companyId,
			@RequestParam(name = "franja") String horariId,
			@RequestParam(required = false, name = "municipi") String municipiId,
			@RequestParam(required = false, name = "codiPostal") String codiPostal,
			@RequestParam(required = false, name = "distancia") String distancia)

	{
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		if(usuari != null) {
			usuari_id = usuari.getId();
		}
		
		Long distance = Long.parseLong(distancia);
		
		List<Activitat> activitats = null;
		if(distance == 1) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListByFilters(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}else {
				activitats = activitatService.activitiesListByFiltersAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}
		}else if(distance == 2) {
			if(Long.parseLong(companyId) !=4) {
				activitats = activitatService.activitiesListAprop(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}else {
				activitats = activitatService.activitiesListApropAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}
		}else if(distance == 3) {
			Long provincia = municipiService.findProvinciaId(Long.parseLong(municipiId));
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListLluny(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}else {
				activitats = activitatService.activitiesListLlunyAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}
		}else if(distance == 4 || distance == 0) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListIndeferent(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}else {
				activitats = activitatService.activitiesListIndeferentAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListDema(activitats));
			}
		}
		
		model.addAttribute("estatAnim", animService.estatAnim(Long.parseLong(animId)));
		model.addAttribute("company", acompanyamentService.getAmbQui(Long.parseLong(companyId)));
		model.addAttribute("franja", horariService.getFranja(Long.parseLong(horariId)));
		
		if(codiPostal != "" && municipiId != null && municipiId != "") {
			model.addAttribute("nomMunicipi", municipiService.getMunicipiNom(Long.parseLong(municipiId)));
			model.addAttribute("codiPostal", codiPostal);
			model.addAttribute("nomDistancia", distanciaService.getDistanciaNom(Long.parseLong(distancia)));
		}else {
			model.addAttribute("nomMunicipi", null);
			model.addAttribute("codiPostal", null);
			model.addAttribute("nomDistancia", null);
		}		
		
		model.addAttribute("llistaRespostes", activitatService.activitiesRespostesPreguntes(Long.parseLong(animId), Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal, distance));
		model.addAttribute("imatge", imatgeService);
		model.addAttribute("titol", "Llista d'activitats");
		model.addAttribute("activitatsPreferides", activitatService.findActivitatsPreferides(usuari_id));
		model.addAttribute("usuari", usuari);
		return "front/resultat";
	}
	
	@PostMapping("/activitats-setmana")
	public String activitatsSetmana(Model model, @RequestParam("estat") String animId,
			@RequestParam(name = "companys") String companyId,
			@RequestParam(name = "franja") String horariId,
			@RequestParam(required = false, name = "municipi") String municipiId,
			@RequestParam(required = false, name = "codiPostal") String codiPostal,
			@RequestParam(required = false, name = "distancia") String distancia)

	{
		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		if(usuari != null) {
			usuari_id = usuari.getId();
		}
		
		Long distance = Long.parseLong(distancia);
		
		List<Activitat> activitats = null;
		if(distance == 1) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListByFilters(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}else {
				activitats = activitatService.activitiesListByFiltersAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId), codiPostal);
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}
		}else if(distance == 2) {
			if(Long.parseLong(companyId) !=4) {
				activitats = activitatService.activitiesListAprop(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}else {
				activitats = activitatService.activitiesListApropAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(municipiId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}
		}else if(distance == 3) {
			Long provincia = municipiService.findProvinciaId(Long.parseLong(municipiId));
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListLluny(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}else {
				activitats = activitatService.activitiesListLlunyAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId), provincia);
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}
		}else if(distance == 4 || distance == 0) {
			if(Long.parseLong(companyId) != 4) {
				activitats = activitatService.activitiesListIndeferent(Long.parseLong(animId),
						Long.parseLong(horariId), Long.parseLong(companyId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}else {
				activitats = activitatService.activitiesListIndeferentAllCompanions(Long.parseLong(animId),
						Long.parseLong(horariId));
				model.addAttribute("llistaActivitats", activitatService.activitiesListSetmana(activitats));
			}
		}
		
		model.addAttribute("estatAnim", animService.estatAnim(Long.parseLong(animId)));
		model.addAttribute("company", acompanyamentService.getAmbQui(Long.parseLong(companyId)));
		model.addAttribute("franja", horariService.getFranja(Long.parseLong(horariId)));
		
		if(codiPostal != "" && municipiId != null && municipiId != "") {
			model.addAttribute("nomMunicipi", municipiService.getMunicipiNom(Long.parseLong(municipiId)));
			model.addAttribute("codiPostal", codiPostal);
			model.addAttribute("nomDistancia", distanciaService.getDistanciaNom(Long.parseLong(distancia)));
		}else {
			model.addAttribute("nomMunicipi", null);
			model.addAttribute("codiPostal", null);
			model.addAttribute("nomDistancia", null);
		}		

		model.addAttribute("llistaRespostes", activitatService.activitiesRespostesPreguntes(Long.parseLong(animId), Long.parseLong(horariId), Long.parseLong(companyId), Long.parseLong(municipiId), codiPostal, distance));
		model.addAttribute("imatge", imatgeService);
		model.addAttribute("titol", "Llista d'activitats");
		model.addAttribute("activitatsPreferides", activitatService.findActivitatsPreferides(usuari_id));
		model.addAttribute("usuari", usuari);
		return "front/resultat";
	}

	@GetMapping(value = "/activitats-preferides")
	public String activitatsPreferides(Model model) {

		Usuari usuari = usuariService.findLoggedUser();
		Long usuari_id = 0L;
		if(usuari != null) {
			usuari_id = usuari.getId();
		}

		model.addAttribute("imatge", imatgeService);
		model.addAttribute("llistaActivitats", activitatService.findActivitatsPreferides(usuari_id));
		model.addAttribute("usuari", usuari);

		return "front/activitatsPreferides";
	}
	
	
	@RequestMapping(value = "/front/compartir", method = RequestMethod.POST)
	@ResponseBody
	public String compartirActivitat(@RequestParam String nom, @RequestParam String email, @RequestParam String activitatCompartir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Activitat activitat = activitatService.getById(Long.parseLong(activitatCompartir));
		
		String message = "El teu amic/amiga " + nom + " a fet una cerca des quepucferavui.cat i vol que vagis amb ell a: "
		+ activitat.getName();
		String subject = "quepucferavui.cat : Vine a l'activitat";
		
		String[] emails = email.split(",");
		for(String correu : emails ) {
			mailService.sendMail("ioc.grup05.qpf@gmail.com",correu.trim(),subject,message);	
		}		
	    
        //correu enviat!
		return "activitat compartida!";
	}	
	
	@RequestMapping(value = "/front/like", method = RequestMethod.POST)
	@ResponseBody
	public String likeActivitat(@RequestParam String activitatId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//Obtindre dades usuari
		Usuari usuari = usuariService.findLoggedUser();
		//Obtindre activitat
		Activitat activitat = activitatService.getById(Long.valueOf(activitatId));
		//Afegeix usuari a activitat
		activitat.getUsuaris().add(usuari);
		//Afegir activitat a usuari
		usuari.getActivitats().add(activitat);

		//Guarda canvis
		activitatService.save(activitat);

	    return "added like";
	}	
	
	@RequestMapping(value = "/front/like", method = RequestMethod.DELETE)
	@ResponseBody
	public String dislikeActivitat(@RequestParam String activitatId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//Obtindre dades usuari
		Usuari usuari = usuariService.findLoggedUser();
		//Obtindre activitat
		Activitat activitat = activitatService.getById(Long.valueOf(activitatId));
		//Eliminar usuari a activitat
		activitat.getUsuaris().remove(usuari);
		//Eliminar activitat a usuari
		usuari.getActivitats().remove(activitat);

		//Guarda canvis
		activitatService.save(activitat);

	    return "deleted like";
	}
	
}

package com.qpf.service;

import com.qpf.model.Activitat;
import com.qpf.repository.ActivitatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ActivitatServiceImpl implements ActivitatService {
	
    @Autowired
    private ActivitatRepository activitatRepository;

    @Override
    public List<Activitat> findAll() {
        return (List<Activitat>) activitatRepository.findAll();
    }
    
    @Override
    public Optional<Activitat> findById(Long id) {
        return activitatRepository.findById(id);
    }   
    
    @Override
	public Activitat getById(Long id_activitat) {
		return activitatRepository.getById(id_activitat);
	}
    
	@Override
	public Activitat save (Activitat activitat) {
		return activitatRepository.save(activitat);
	}
	
	@Override
	public void deleteById (Long id) {
		activitatRepository.deleteById(id);
	}

	@Override
	public List<Activitat> findActivitatsPreferides(Long usuari_id) {
		return (List<Activitat>) activitatRepository.findActivitatsPreferides(usuari_id);
	}

	// activitats de les llistes assignades a un usuari	
	@Override
	public List<Activitat> findAllByUserId(Long usuari_id) {
		return (List<Activitat>) activitatRepository.findAllByUserId(usuari_id);
	}		
	
    /****************************************************************************************/
	/********************************
	 * OBTENIR ACTIVITATS PER FILTRE
	 *********************************/
	
	@Override
	public List<Activitat> activitiesListByFilters(Long id_estat, Long id_franja, Long id_companys, Long id_municipi,
			String codiPostal) {

		return (List<Activitat>) activitatRepository.activitiesListByFilters(id_estat, id_franja, id_companys,
				id_municipi, codiPostal);
	}

	@Override
	public List<Activitat> activitiesListAprop(Long id_estat, Long id_franja, Long id_companys, Long id_municipi) {

		return (List<Activitat>) activitatRepository.activitiesListAprop(id_estat, id_franja, id_companys, id_municipi);
	}

	@Override
	public List<Activitat> activitiesListLluny(Long id_estat, Long id_franja, Long id_companys, Long id_provincia) {

		return (List<Activitat>) activitatRepository.activitiesListLluny(id_estat, id_franja, id_companys,
				id_provincia);
	}

	@Override
	public List<Activitat> activitiesListIndeferent(Long id_estat, Long id_franja, Long id_companys) {

		return (List<Activitat>) activitatRepository.activitiesListIndeferent(id_estat, id_franja, id_companys);
	}
	
	/****************************************************************************************/
	/********************************
	 * OBTENIR ACTIVITATS PER FILTRE ENCARA NO HO SE
	 *********************************/
	@Override
	public List<Activitat> activitiesListByFiltersAllCompanions(Long id_estat, Long id_franja, Long id_municipi,
			String codiPostal) {
		
		return (List<Activitat>) activitatRepository.activitiesListByFiltersAllCompanions(id_estat, id_franja, id_municipi, codiPostal);
	}

	@Override
	public List<Activitat> activitiesListApropAllCompanions(Long id_estat, Long id_franja, Long id_municipi) {
		
		return (List<Activitat>) activitatRepository.activitiesListApropAllCompanions(id_estat, id_franja, id_municipi);
	}

	@Override
	public List<Activitat> activitiesListLlunyAllCompanions(Long id_estat, Long id_franja, Long id_provincia) {
		
		return (List<Activitat>) activitatRepository.activitiesListLlunyAllCompanions(id_estat, id_franja, id_provincia);
	}

	@Override
	public List<Activitat> activitiesListIndeferentAllCompanions(Long id_estat, Long id_franja) {
		
		return (List<Activitat>) activitatRepository.activitiesListIndeferentAllCompanions(id_estat, id_franja);
	}	
	
    /****************************************************************************************/
	/********************************
	 * OBTENIR ACTIVITATS AVUI
	 *********************************/

	@Override
	public List<Activitat> activitiesListToday(List<Activitat> activitats) {
		List<Activitat> activitatsAvui = new ArrayList<Activitat>();
		Date date = new Date();
		String avui = new SimpleDateFormat("yyyy-MM-dd").format(date);
		int diaSetmana = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
		for(Activitat a : activitats) {
			String dataInici = new SimpleDateFormat("yyyy-MM-dd").format(a.getDataInici());
			if( dataInici.equals(avui)) {
				activitatsAvui.add(a);
			}
			if(avui.compareTo(dataInici) > 0){
				if(a.getDies() != null) {
					String[] dies = a.getDies().split(",");
					for(String dia: dies) {
						if(dia.equals("dl") && diaSetmana == 2) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}							
						}
						
						if(dia.equals("dt") && diaSetmana == 3) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
						
						if(dia.equals("dc") && diaSetmana == 4) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
						
						if(dia.equals("dj") && diaSetmana == 5) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
						
						if(dia.equals("dv") && diaSetmana == 6) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
						
						if(dia.equals("ds") && diaSetmana == 7) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
						
						if(dia.equals("dg") && diaSetmana == 1) {
							if(!activitatsAvui.contains(a)) {
								activitatsAvui.add(a);
							}	
						}
					}
				}
			}
		}
		return activitatsAvui;
	}

	/****************************************************************************************/
	/***********************
	 * OBTENIR RESPOSTES PREGUNTES USUARIS
	 ******************************/

	@Override
	public HashMap<String, String> activitiesRespostesPreguntes(Long id_estat, Long id_franja, Long id_companys,
			Long id_municipi, String codiPostal, Long distancia) {
		HashMap<String, String> questions = new HashMap<String, String>();

		questions.put("estat", Long.toString(id_estat));
		questions.put("franja", Long.toString(id_franja));
		questions.put("companys", Long.toString(id_companys));
		questions.put("municipi", Long.toString(id_municipi));
		questions.put("codiPostal", codiPostal);
		questions.put("distancia", Long.toString(distancia));

		return questions;
	}

	/****************************************************************************************/
	/********************************
	 * OBTENIR ACTIVITATS DEMA
	 *********************************/

	@Override
	public List<Activitat> activitiesListDema(List<Activitat> activitats){
		List<Activitat> activitatsDema = new ArrayList<Activitat>();		
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		Date date = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String dema = new SimpleDateFormat("yyyy-MM-dd").format(date);
		int diaSetmana = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
		int diaSetmanaDema = 0;
		if(diaSetmana != 7) {
			diaSetmanaDema = diaSetmana + 1;
		}else {
			diaSetmana = 1;
		}		
		for(Activitat a : activitats) {
			String dataInici = new SimpleDateFormat("yyyy-MM-dd").format(a.getDataInici());
			
			if( dataInici.equals(dema)) {
				activitatsDema.add(a);
			}
			if(dema.compareTo(dataInici) > 0) {
				if(a.getDies() != null) {
					String[] dies = a.getDies().split(",");
					for(String dia: dies) {
						if(dia.equals("dl") && diaSetmanaDema == 2) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("dt") && diaSetmanaDema == 3) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("dc") && diaSetmanaDema == 4) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("dj") && diaSetmanaDema == 5) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("dv") && diaSetmanaDema == 6) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("ds") && diaSetmanaDema == 7) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
						
						if(dia.equals("dg") && diaSetmanaDema == 1) {
							if(!activitatsDema.contains(a)) {
								activitatsDema.add(a);
							}							
						}
					}
				}
			}
		}
		return activitatsDema;
	}

	/****************************************************************************************/
	/*****************************
	 * OBTENIR ACTIVITATS SETMANA
	 *********************************/

	@Override
	public List<Activitat> activitiesListSetmana(List<Activitat> activitats){
		List<Activitat> activitatsSetmana = new ArrayList<Activitat>();
		
		LocalDate avui = LocalDate.now();
		DayOfWeek nomDia = avui.getDayOfWeek();
		int diaSetmana = nomDia.getValue();
		int totalDiesMostrar = 7 - diaSetmana;
		LocalDate diesSetmana = avui.plusDays(totalDiesMostrar);
		
		for(Activitat a : activitats) {
			String dataInici = new SimpleDateFormat("yyyy-MM-dd").format(a.getDataInici());
			
			if( dataInici.compareTo(avui.format(DateTimeFormatter.ISO_DATE)) > 0 && dataInici.compareTo(diesSetmana.format(DateTimeFormatter.ISO_DATE)) < 0) {
				activitatsSetmana.add(a);
			}else {
				if(a.getDies() != null) {
					String[] dies = a.getDies().split(",");
					for(String dia: dies) {
						if((dia.equals("dt") || dia.equals("dc") || dia.equals("dj") || dia.equals("dv") || dia.equals("ds") || dia.equals("dg")) && totalDiesMostrar == 6) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
						
						if((dia.equals("dc") || dia.equals("dj") || dia.equals("dv") || dia.equals("ds") || dia.equals("dg")) && totalDiesMostrar == 5) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
						
						if((dia.equals("dj") || dia.equals("dv") || dia.equals("ds") || dia.equals("dg")) && totalDiesMostrar == 4) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
						
						if((dia.equals("dv") || dia.equals("ds") || dia.equals("dg")) && totalDiesMostrar == 3) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
						
						if((dia.equals("ds") || dia.equals("dg")) && totalDiesMostrar == 2) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
						
						if(dia.equals("dg") && totalDiesMostrar == 1) {
							if(!activitatsSetmana.contains(a)) {
								activitatsSetmana.add(a);
							}
						}
					}
				}
			}
		}
		
		return activitatsSetmana;
	}

}

package com.qpf.service;

import com.qpf.model.Activitat;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ActivitatService {

	List<Activitat> findAll();
	
	Optional<Activitat> findById(Long id);
	
	Activitat getById(Long id_activitat);
	
	Activitat save (Activitat activitat);	
	
	void deleteById(Long id);

	List<Activitat> findActivitatsPreferides(Long usuari_id);
	
	// activitats de les llistes assignades a un usuari	
	List<Activitat> findAllByUserId(Long usuari_id);
	
	/****************************************************************************************/
	/********************************FILTRAR ACTIVITATS*********************************/
	
	List<Activitat> activitiesListByFilters(Long id_estat, Long id_franja, Long id_companys, Long id_municipi, String codiPostal);
	
	List<Activitat> activitiesListAprop(Long id_estat, Long id_franja, Long id_companys, Long id_municipi);
	
	List<Activitat> activitiesListLluny(Long id_estat, Long id_franja, Long id_companys, Long id_provincia);
	
	List<Activitat> activitiesListIndeferent(Long id_estat, Long id_franja, Long id_companys);
	
	/****************************************************************************************/
	/************************FILTRAR ACTIVITATS ENCARA NO HO SE*********************************/
	List<Activitat> activitiesListByFiltersAllCompanions(Long id_estat, Long id_franja, Long id_municipi, String codiPostal);
	List<Activitat> activitiesListApropAllCompanions(Long id_estat, Long id_franja, Long id_municipi);
	List<Activitat> activitiesListLlunyAllCompanions(Long id_estat, Long id_franja, Long id_provincia);
	List<Activitat> activitiesListIndeferentAllCompanions(Long id_estat, Long id_franja);	
	
	/****************************************************************************************/
	/********************************OBTENIR ACTIVITATS AVUI*********************************/
	
	List<Activitat> activitiesListToday(List<Activitat> activitats);
	
	/****************************************************************************************/
	/***********************OBTENIR RESPOSTES PREGUNTES USUARIS******************************/
	
	HashMap<String, String> activitiesRespostesPreguntes(Long id_estat, Long id_franja, Long id_companys, Long id_municipi, String codiPostal, Long distancia);
	
	/****************************************************************************************/
	/********************************OBTENIR ACTIVITATS DEMA*********************************/
		
	List<Activitat> activitiesListDema(List<Activitat> activitats);
	
	/****************************************************************************************/
	/*****************************OBTENIR ACTIVITATS SETMANA*********************************/
	
	List<Activitat> activitiesListSetmana(List<Activitat> activitats);

}
package com.qpf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qpf.model.Activitat;

@Repository
public interface ActivitatRepository extends JpaRepository<Activitat, Long> {
	
	// llista totes les activitats
	List<Activitat> findAll();

	// llista activitats preferides del usuari
	@Query("SELECT a FROM Activitat a "
			+ "JOIN a.usuaris u "
			+ "WHERE u.id= :usuari_id "
			+ "AND a.visiblea= '1' "
			+ "AND a.visiblep= '1' "
			+ "AND a.dades.visible= '1'")
	List<Activitat> findActivitatsPreferides(Long usuari_id);
	
	// activitats de les llistes assignades a un usuari	
	@Query("SELECT a FROM Activitat a WHERE a.dades.usuari.id= :usuari_id")	
	List<Activitat> findAllByUserId(Long usuari_id);
	
	// mostra activtat segons id
	Optional<Activitat> findById(Long id);
	
	@Query("SELECT a FROM Activitat a WHERE a.id= :id_activitat")
	Activitat activitatPerId(Long id_activitat);
	
	// insereix/actualitza activitat	
	Activitat save(Activitat activitat);
	
	// esborra activitat
	@Transactional
	@Modifying	
	@Query(
			"DELETE FROM Activitat a "
			+ "WHERE "
			+ "a.id= :id "
	)	
	void deleteById(Long id);
	
/********FILTRAR ACTIVITATS********/
	
	// filtra activitat amb tots els filtres
	@Query(
			"SELECT a FROM Activitat a "
			+ "JOIN a.anims anim "
			+ "JOIN a.horaris horaris "
			+ "JOIN a.companys companys "
			+ "JOIN a.municipi municipi "
			+ "WHERE "
			+ "anim.id= :id_estat "
			+ "AND horaris.id= :id_franja "
			+ "AND companys.id= :id_companys "
			+ "AND municipi.id= :id_municipi "
			+ "AND a.codi.codiPostal= :codiPostal "
			+ "AND a.visiblep = '1' "
			+ "AND a.visiblea = '1' "
			+ "AND a.dades.visible = '1' "
			+ "AND a.dataFi >= CURRENT_DATE "
			+ "ORDER BY a.destacat DESC"
	)
	List<Activitat> activitiesListByFilters(Long id_estat, Long id_franja, Long id_companys, Long id_municipi, String codiPostal);
	
	// filtra a prop
	@Query(
			"SELECT a FROM Activitat a "
			+ "JOIN a.anims anim "
			+ "JOIN a.horaris horaris "
			+ "JOIN a.companys companys "
			+ "JOIN a.municipi municipi "
			+ "WHERE "
			+ "anim.id= :id_estat "
			+ "AND horaris.id= :id_franja "
			+ "AND companys.id= :id_companys "
			+ "AND municipi.id= :id_municipi "
			+ "AND a.visiblep = '1' "
			+ "AND a.visiblea = '1' "
			+ "AND a.dades.visible = '1' "
			+ "AND a.dataFi >= CURRENT_DATE "
			+ "ORDER BY a.destacat DESC"
	)
	List<Activitat> activitiesListAprop(Long id_estat, Long id_franja, Long id_companys, Long id_municipi);
	
	// filtra ben lluny
	@Query(
			"SELECT a FROM Activitat a "
			+ "JOIN a.anims anim "
			+ "JOIN a.horaris horaris "
			+ "JOIN a.companys companys "
			+ "JOIN a.municipi municipi "
			+ "WHERE "
			+ "anim.id= :id_estat "
			+ "AND horaris.id= :id_franja "
			+ "AND companys.id= :id_companys "
			+ "AND municipi.provincia.id= :id_provincia "
			+ "AND a.visiblep = '1' "
			+ "AND a.visiblea = '1' "
			+ "AND a.dades.visible = '1' "
			+ "AND a.dataFi >= CURRENT_DATE "
			+ "ORDER BY a.destacat DESC"
	)
	List<Activitat> activitiesListLluny(Long id_estat, Long id_franja, Long id_companys, Long id_provincia);
	
	//filtra indeferent
	@Query(
			"SELECT a FROM Activitat a "
			+ "JOIN a.anims anim "
			+ "JOIN a.horaris horaris "
			+ "JOIN a.companys companys "
			+ "JOIN a.municipi municipi "
			+ "WHERE "
			+ "anim.id= :id_estat "
			+ "AND horaris.id= :id_franja "
			+ "AND companys.id= :id_companys "
			+ "AND a.visiblep = '1' "
			+ "AND a.visiblea = '1' "
			+ "AND a.dades.visible = '1' "
			+ "AND a.dataFi >= CURRENT_DATE "
			+ "ORDER BY a.destacat DESC"
	)
	List<Activitat> activitiesListIndeferent(Long id_estat, Long id_franja, Long id_companys);	
	
	/********FILTRAR ACTIVITATS AMB ENCARA NO HO SE********/
	// filtra activitat amb tots els filtres totes companyies
		@Query(
				"SELECT a FROM Activitat a "
				+ "JOIN a.anims anim "
				+ "JOIN a.horaris horaris "
				+ "JOIN a.companys companys "
				+ "JOIN a.municipi municipi "
				+ "WHERE "
				+ "anim.id= :id_estat "
				+ "AND horaris.id= :id_franja "
				+ "AND companys.id!= '4' "
				+ "AND municipi.id= :id_municipi "
				+ "AND a.codi.codiPostal= :codiPostal "
				+ "AND a.visiblep = '1' "
				+ "AND a.visiblea = '1' "
				+ "AND a.dades.visible = '1' "
				+ "AND a.dataFi >= CURRENT_DATE "
				+ "ORDER BY a.destacat DESC"
		)
		List<Activitat> activitiesListByFiltersAllCompanions(Long id_estat, Long id_franja, Long id_municipi, String codiPostal);
		
		// filtra a prop totes companyies
		@Query(
				"SELECT a FROM Activitat a "
				+ "JOIN a.anims anim "
				+ "JOIN a.horaris horaris "
				+ "JOIN a.companys companys "
				+ "JOIN a.municipi municipi "
				+ "WHERE "
				+ "anim.id= :id_estat "
				+ "AND horaris.id= :id_franja "
				+ "AND companys.id!= '4' "
				+ "AND municipi.id= :id_municipi "
				+ "AND a.visiblep = '1' "
				+ "AND a.visiblea = '1' "
				+ "AND a.dades.visible = '1' "
				+ "AND a.dataFi >= CURRENT_DATE "
				+ "ORDER BY a.destacat DESC"
		)
		List<Activitat> activitiesListApropAllCompanions(Long id_estat, Long id_franja, Long id_municipi);
		
		// filtra ben lluny totes companyies
		@Query(
				"SELECT a FROM Activitat a "
				+ "JOIN a.anims anim "
				+ "JOIN a.horaris horaris "
				+ "JOIN a.companys companys "
				+ "JOIN a.municipi municipi "
				+ "WHERE "
				+ "anim.id= :id_estat "
				+ "AND horaris.id= :id_franja "
				+ "AND companys.id!= '4' "
				+ "AND municipi.provincia.id= :id_provincia "
				+ "AND a.visiblep = '1' "
				+ "AND a.visiblea = '1' "
				+ "AND a.dades.visible = '1' "
				+ "AND a.dataFi >= CURRENT_DATE "
				+ "ORDER BY a.destacat DESC"
		)
		List<Activitat> activitiesListLlunyAllCompanions(Long id_estat, Long id_franja, Long id_provincia);
		
		//filtra indeferent totes companyies
		@Query(
				"SELECT a FROM Activitat a "
				+ "JOIN a.anims anim "
				+ "JOIN a.horaris horaris "
				+ "JOIN a.companys companys "
				+ "JOIN a.municipi municipi "
				+ "WHERE "
				+ "anim.id= :id_estat "
				+ "AND horaris.id= :id_franja "
				+ "AND companys.id!= '4' "
				+ "AND a.visiblep = '1' "
				+ "AND a.visiblea = '1' "
				+ "AND a.dades.visible = '1' "
				+ "AND a.dataFi >= CURRENT_DATE "
				+ "ORDER BY a.destacat DESC"
		)
		List<Activitat> activitiesListIndeferentAllCompanions(Long id_estat, Long id_franja);
}
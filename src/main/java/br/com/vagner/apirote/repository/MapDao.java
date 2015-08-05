package br.com.vagner.apirote.repository;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.vagner.apirote.exception.DAOException;
import br.com.vagner.apirote.model.Map;

/**
 * Dao for map interactions
 * @author vagner
 *
 */
@Repository
public class MapDao extends GenericDao<Map, Long> {
	
	public Map findByName(String name) throws DAOException {
		Query query = getNamedQuery("Map.findByName").setParameter("name", name);
		return (Map) getSingleResult(query);		
	}
 
}

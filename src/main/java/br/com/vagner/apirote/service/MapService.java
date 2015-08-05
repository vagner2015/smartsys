
package br.com.vagner.apirote.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;

import br.com.vagner.apirote.exception.DAOException;
import br.com.vagner.apirote.exception.ServiceException;
import br.com.vagner.apirote.model.Map;
import br.com.vagner.apirote.repository.MapDao;
import br.com.vagner.apirote.util.Dijkstra;
import br.com.vagner.apirote.util.MapUtil;
import br.com.vagner.apirote.util.Vertex;

/**
 * Service for able map funcionalities
 * @author Vagner
 *
 */
@Service
public class MapService {
	
	private static final Logger LOGGER = Logger.getLogger(MapService.class);

	@Autowired
	private MapDao mapDao;
	
	public Map findMapByName(String name) throws ServiceException {
		try {
			LOGGER.info(String.format("Buscando map por nome: %s", name));
			return mapDao.findByName(name);
		} catch (DAOException e) {
			throw new ServiceException("Erro ao buscar map no banco de dados.");
		}
	}
	
	public String calculateShortestRoute(String mapName, String origin, String destination, Double autonomy, Double cost) throws ServiceException {
		
		LOGGER.info(String.format("Calculando menor caminho: %s, %s, %s, %s, %s", mapName, origin, destination, autonomy, cost));
		Map map = findMapByName(mapName);
		if (MapUtil.isValidMap(map)) {
			java.util.Map<String, Vertex> vertexMap = MapUtil.getVertexMap(map);
			
			Dijkstra.computePaths(vertexMap.get(origin));
			List<Vertex> path = Dijkstra.getShortestPathTo(vertexMap.get(destination));
			
			Double distance = MapUtil.getTotalDistance(path, destination);
			String routes = MapUtil.getFormattedRoute(path);
			Double totalCost =  MapUtil.getTotalCost(distance, autonomy, cost);
			
			return MapUtil.shortestRouteAsJson(routes, totalCost);
		} else {
			throw new ServiceException("Mapa nao encontrado. Confira os parametros da requisicao.");
		}
	}
	
	@Transactional
	public Map incluir(Map map) throws ServiceException{
		
		try {
			return mapDao.persist(map);
		} catch (DAOException e) {
			throw new ServiceException("Erro ao persistir mapa.");
		}
	}
		
	@Transactional
	public Map insertMap(String json) throws ServiceException {
		
		try {
			LOGGER.info(String.format("Inserindo novo mapa: %s", json));
			Map map = convertJsonToMap(json);
			if (MapUtil.isValidMap(map)) {
				Map mapPersist = this.findMapByName(map.getName());
				map.populateRouteParent();
				if (mapPersist != null) {
					map.setId(mapPersist.getId());
					return mapDao.merge(map);
				} else {
					return mapDao.persist(map);
				}
			} else {
				throw new ServiceException("Erro ao validar o mapa. Confira os parametros do JSON.");
			}
		} catch (DAOException e) {
			throw new ServiceException("Erro ao persistir mapa.");
		}
	}
	
	public Map convertJsonToMap(String json) throws ServiceException {
		try {
			LOGGER.info(String.format("Convertendo Json para objeto Map: %s", json));
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} catch (JsonMappingException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} catch (IOException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} 
	}
	
	public String convertMapToJson(Map map) throws ServiceException {
		try {
			LOGGER.info(String.format("Convertendo objeto Map para Json: %s", map.getName()));
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} catch (JsonMappingException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} catch (IOException e) {
			throw new ServiceException("Erro ao parsear JSON.");
		} 
	}
	
}

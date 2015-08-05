package br.com.vagner.apirote.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.vagner.apirote.model.Map;
import br.com.vagner.apirote.model.Route;

public class MapUtil {
	
	private static final String ROUTE_SEPARATOR = ", ";
	private static final String ROUTE_JSON_CONCAT = "{\"route\": \"";
	private static final String TOTALCOST_JSON_CONCAT = "\", \"totalCost\":";
	private static final String CLOSE_JSON_CONCAT = "}";
	
	public static String shortestRouteAsJson(String routes, Double totalCost) {
		return new StringBuffer().append(ROUTE_JSON_CONCAT)
				.append(routes)
				.append(TOTALCOST_JSON_CONCAT)
				.append(totalCost)
				.append(CLOSE_JSON_CONCAT)
				.toString();
	}
	
	public static java.util.Map<String, Vertex> getVertexMap(Map map) {
		
		if (map != null && map.getRoutes() != null) {
			
			java.util.Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
			java.util.Map<Vertex, ArrayList<Edge>> vertexEdgesMap = new HashMap<Vertex, ArrayList<Edge>>();
		
			for (Route route : map.getRoutes()) {
				if (vertexMap.get(route.getDestination()) == null) {
					vertexMap.put(route.getDestination(), new Vertex(route.getDestination()));
					vertexEdgesMap.put(vertexMap.get(route.getDestination()), new ArrayList<Edge>());
				}
				if (vertexMap.get(route.getOrigin()) == null) {
					vertexMap.put(route.getOrigin(), new Vertex(route.getOrigin()));
					vertexEdgesMap.put(vertexMap.get(route.getOrigin()), new ArrayList<Edge>());
				}
				List<Edge> edges = (ArrayList<Edge>) vertexEdgesMap.get( vertexMap.get( route.getOrigin() ) );
				edges.add(new Edge( (Vertex)vertexMap.get(route.getDestination()), route.getDistance()) );
			}
			
			setVertexAdjacencies(vertexMap, vertexEdgesMap);
			
			return vertexMap;
		}
		return null;
		
	}
	
	private static void setVertexAdjacencies(java.util.Map<String, Vertex> vertexMap, java.util.Map<Vertex, ArrayList<Edge>> vertexEdgesMap) {
		for (Entry<Vertex, ArrayList<Edge>> entry : vertexEdgesMap.entrySet()) {
			Vertex v = entry.getKey();
			List<Edge> e = entry.getValue();
			vertexMap.get(v.getName()).setAdjacencies(e);
		}
	}
	
	public static Double getTotalDistance(List<Vertex> path, String destination) {
		for (Vertex v : path) {
			if (v.getName().equals(destination)) {
				return v.getMinDistance();
			}
		}
		return null;
	}
	
	public static Double getTotalCost(Double distance, Double autonomy, Double literCost) {
		return ((distance / autonomy) * literCost);
	}
	
	public static String getFormattedRoute (List<Vertex> path) {
		StringBuffer sbf = new StringBuffer();
		for (Vertex v : path) {
			sbf.append(v.getName() + ROUTE_SEPARATOR);
		}
		return sbf.toString().substring(0, sbf.toString().length() - 2);
	}
	
	public static boolean isValidMap (Map map) {
		if (map != null && !map.getName().isEmpty()) {
			return true;
		}
		return false;
	}

}

package br.com.vagner.apirote.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
	public static void computePaths(Vertex source) {
		source.setMinDistance(0.);
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			if (u.getAdjacencies() != null) {
				for (Edge e : u.getAdjacencies()) {
					Vertex v = e.getTarget();
					double weight = e.getWeight();
					double distanceThroughU = u.getMinDistance() + weight;
					if (distanceThroughU < v.getMinDistance()) {
						vertexQueue.remove(v);
						v.setMinDistance(distanceThroughU);
						v.setPrevious(u);
						vertexQueue.add(v);
					}
				}
			}
		}
	}

	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.getPrevious())
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

}
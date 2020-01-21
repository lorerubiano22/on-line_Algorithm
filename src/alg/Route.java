package alg;

import java.util.LinkedList;
import java.util.List;

public class Route {

	private double time = 0.0; // route total travel time
	private double distance = 0.0; // route total travel distance
		private LinkedList<Edge> edges; // edges list


	// Constructor
	public Route() {
		edges = new LinkedList<Edge>();
	}

	public Route(Route r) {
		edges = new LinkedList<Edge>();
		LinkedList<Edge> edges;
		for (Edge e : r.getEdges()) {
			Edge eAux = new Edge(e);
			this.edges.add(eAux);
		}

	}

	/* SET METHODS */
	public void setTime(double c) {
		time = c;
	}

	public void setDistance(double c) {
		distance = c;
	}


	public void setEdges(LinkedList<Edge> e) {
		edges = e;
	}



	public void addEdge(int i, Edge e) {
		Edge eAux = new Edge(e);
		this.edges.add(i, e);
	}


	public double getTime() {
		return time;
	}

	public double getDistance() {
		return distance;
	}



	public List<Edge> getEdges() {
		return edges;
	}


	public void removeEdge(Edge e) {
		this.edges.remove(e);
	}

	public void substractCosts(Edge e) {
		this.time = this.time - e.getTime();
	}


	/* AUXILIARY METHODS */
	/**
	 * Reverses a route, e.g. (0 -> 2 -> 6 -> 0) becomes (0 -> 6 -> 2 -> 0)
	 */
	public void reverse() {
		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			Edge invE = e.getInverseEdge();
			edges.remove(e);
			edges.add(0, invE);
		}
	}

	@Override
	public String toString() {
		String s = "";
		s = s.concat("\nRute time: " + (this.getTime()));
		s = s.concat("\nRuta edges: " + this.getEdges());
		return s;
	}


	public void calcTime() {
		double cost = 0;
		for (Edge e : this.getEdges()) {
			cost += e.getTime();
		}
		this.setTime(cost);
	}

	public void calcDistance() {
		double cost = 0;
		for (Edge e : this.getEdges()) {
			cost += e.getDistance();
		}
		this.setDistance(cost);
	}




}

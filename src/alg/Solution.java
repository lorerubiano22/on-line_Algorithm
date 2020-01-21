package alg;

import java.util.LinkedList;

public class Solution implements Cloneable {

	/* INSTANCE FIELDS & CONSTRUCTOR */
	private double totalTime; // solution total travel time
	private double totalDistance; // solution travel distance
	private LinkedList<Route> routes; // list of routes in this solution - keep for the extension
	private double timePC; // elapsed computational time (in seconds)

	public Solution() {
		totalTime = 0.0; // solution total travel time
		totalDistance = 0.0; // solution travel distance
		routes= new LinkedList<Route>(); // list of routes in this solution - keep for the extension
		timePC = 0.0;  // elapse
	}

	public void updatingSolutionAttributes() {
		double totalCost = 0.0;
		double distance = 0.0;
		for (Route r : this.routes) {
			totalCost += r.getTime();
			distance += r.getDistance();
		}
		setDistance(distance);
		setTime(totalCost);
	}

	/* GET METHODS */
	public LinkedList<Route> getRoutes() {
		return routes;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public double getPCTime() {
		return timePC;
	}

	/* SET METHODS */

	public void setTime(double c) {
		totalTime = c;
	}

	public void setDistance(double c) {
		totalDistance = c;
	}

	public void setPCTime(double t) {
		timePC = t;
	}

	/* AUXILIARY METHODS */

	@Override
	public String toString() {
		Route aRoute; // auxiliary Route variable
		String s = "";
		s = s.concat(System.lineSeparator());
		s = s.concat("Sol total time: " + getTotalTime() + System.lineSeparator());
		s = s.concat("Sol total distance: " + getTotalDistance() + System.lineSeparator());
		s = s.concat("Sol run time: " + getPCTime() + System.lineSeparator());
		s = s.concat(System.lineSeparator());
		s = s.concat("\r\n\r\n\r\n");
		s = s.concat("List of routes (cost and nodes): \r\n\r\n");
		for (int i = 1; i <= routes.size(); i++) {
			aRoute = routes.get(i - 1);
			s = s.concat("Travel time = " + aRoute.getTime() + " || ");
			s = s.concat("Travel distance = " + aRoute.getDistance() + " || ");
			s = s.concat(System.lineSeparator());
			s = s.concat("\n");
			s = s.concat("Nodes = ");
			int last = -1;
			for (Edge e : aRoute.getEdges()) { // obtengo edges
				s = s.concat(e.getOrigin().getId() + "  - ");
				last = e.getEnd().getId();
			}
			s = s.concat(last + "\n");
		}
		return s;
	}

}

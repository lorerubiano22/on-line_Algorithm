package alg;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Solution implements Cloneable {

	/* INSTANCE FIELDS & CONSTRUCTOR */
	private Map<Integer, Node> recheablevictims = new HashMap<>();
	private double totalTime; // solution total travel time
	private double totalDistance; // solution travel distance
	private LinkedList<Route> routes; // list of routes in this solution - keep for the extension
	private double timePC; // elapsed computational time (in seconds)
private  Route explorationRoute;
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


	public Route setExplorationRoute(Route r) {
		explorationRoute= new Route();
		for(Edge e:r.getEdges()) {
			explorationRoute.getEdges().add(e);
		}
		explorationRoute.calcTime();
		explorationRoute.calcDistance();
		return explorationRoute;
	}

	public Route getExplorationRoute() {
		return explorationRoute;
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

	public void setrecheablevictims(Map<Integer, Node> victims) {
		for(Node victim:victims.values()) {
			recheablevictims.put(victim.getId(), victim);
		}
	}
	public Map<Integer, Node> getrecheablevictims() {
		return recheablevictims;
	}

	public void sorting() {
		this.getRoutes().sort(minDistance);

	}
	static final Comparator<Route> minDistance = new Comparator<Route>() {
		@Override
		public int compare(Route r1, Route r2) {
			if (r1.getDistance() >r2.getDistance())
				return 1;
			if (r1.getDistance() < r2.getDistance())
				return -1;
			return 0;
		}
	};

}

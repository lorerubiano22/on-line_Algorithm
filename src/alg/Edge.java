package alg;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;

public class Edge implements Serializable {
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private String key;
	private Node origin; // origin node
	private int typeEdge;// 1 if it belongs to the road network 0 otherwise
	private Node end; // end node
	private double timeEuclidean ; // edge costs (travel time)
	private double distanceEuclidean ; // edge costs (travel distance)
	private double timeRoad ; // edge costs (travel time)
	private double distanceRoad ; // edge costs (travel distance)
	private Edge inverseEdge = null; // edge with inverse direction
	private double nodesImportance;
	private LinkedList<Edge> roadInflexionEdge;
	private LinkedList<Node> roadInflexionNode;
	public double connectivity = 0.0;
	public double weight = 0.0;
	public int disruptionIndex = -1;
	public double maxAdjConnectivity = 0;
	public double minAdjTime = 0;
	public double maxAdjTime = 0;

	public Edge(Node originNode, Node endNode) {
		origin = originNode;
		end = endNode;
		String s1 = Integer.toString(originNode.getId());
		String s2 = Integer.toString(endNode.getId());
		String keyEdge = s1 + "," + s2;
		this.key = keyEdge;
	}

	public Edge(Edge e) {
		this.key = e.key;
		this.origin = e.origin;
		this.end = e.end;
		this.timeEuclidean = e.timeEuclidean;
		this.connectivity = e.connectivity;
		this.weight = e.weight;
		this.distanceEuclidean = e.distanceEuclidean;
		this.inverseEdge = e.inverseEdge;
		if(e.getInflexionEdge()!=null) {
		//if(e.disruptionIndex!=-1) {
		this.disruptionIndex=e.disruptionIndex;
		this.setInflextionNodes(e.getInflexionEdge());

		}
	}

	/* SET METHODS */
	public void settypeEdge(int c) {
		typeEdge = c;
	}

	public void setDistance(double c) {
		distanceEuclidean = c;
	}

	public void setTime(double c) {
		timeEuclidean = c;
	}

	public void setConnectivity(double s) {
		connectivity = s;
	}

	public void setWeight(double s) {
		weight = s;
	}

	public void setDistanceRoad(double c) {
		distanceRoad = c;
	}

	public void setTimeRoad(double c) {
		timeRoad = c;
	}

	public void setOrigin(Node origin) {
		this.origin = origin;
	}

	public void setEnd(Node end) {
		this.end = end;
	}

	public void setInverse(Edge e) {
		inverseEdge = e;
	}

	public void setDisruptionIndex(int x) {
		disruptionIndex = x;
	}

	/* GET METHODS */

	public int gettypeEdge() {
		return typeEdge;
	}

	public Node getOrigin() {
		return origin;
	}

	public Node getEnd() {
		return end;
	}

	public double getTime() {
		return timeEuclidean;
	}

	public double getDistance() {
		return distanceEuclidean;
	}

	public double getTimeRoad() {
		return timeRoad;
	}

	public double getDistanceRoad() {
		return distanceRoad;
	}

	public int getDisruptionIndex() {
		return disruptionIndex;
	}

	public double getConnectivity() {
		return connectivity;
	}

	public double getWeight() {
		return weight;
	}

	public Edge getInverseEdge() {
		return inverseEdge;
	}

	public LinkedList<Edge> getInflexionEdge() {
		return roadInflexionEdge;
	}

	public LinkedList<Node> getroadInflexionNode() {
		return roadInflexionNode;
	}

	public String getKey() {
		return key;
	}

	/* AUXILIARY METHODS */

	public double calcTime() {
		double X1 = origin.getX();
		double Y1 = origin.getY();
		double X2 = end.getX();
		double Y2 = end.getY();
		double time = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1)) / 10.8;
		return time;
	}

	public double calcDistance() {
		double X1 = origin.getX();
		double Y1 = origin.getY();
		double X2 = end.getX();
		double Y2 = end.getY();
		double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
		return d;
	}

	public static double calcTime(Node origin, Node end) {
		double X1 = origin.getX();
		double Y1 = origin.getY();
		double X2 = end.getX();
		double Y2 = end.getY();
		double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1)) / 10.8;
		return d;
	}

	public static double calcDistance(Node origin, Node end) {
		double X1 = origin.getX();
		double Y1 = origin.getY();
		double X2 = end.getX();
		double Y2 = end.getY();
		double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
		return d;
	}

	static final Comparator<Edge> minTime = new Comparator<Edge>() {
		@Override
		public int compare(Edge a1, Edge a2) {
			if (a1.timeEuclidean > a2.timeEuclidean)
				return 1;
			if (a1.timeEuclidean < a2.timeEuclidean)
				return -1;
			return 0;
		}
	};

	static final Comparator<Edge> minDistance = new Comparator<Edge>() {
		@Override
		public int compare(Edge a1, Edge a2) {
			if (a1.distanceEuclidean > a2.distanceEuclidean)
				return 1;
			if (a1.distanceEuclidean < a2.distanceEuclidean)
				return -1;
			return 0;
		}
	};

	static final Comparator<Edge> connectivityComp = new Comparator<Edge>() {
		@Override
		public int compare(Edge a1, Edge a2) {
			if (a1.connectivity < a2.connectivity)
				return 1;
			if (a1.connectivity > a2.connectivity)
				return -1;
			return 0;
		}
	};

	static final Comparator<Edge> weightComp = new Comparator<Edge>() {
		@Override
		public int compare(Edge a1, Edge a2) {
			if (a1.weight < a2.weight)
				return 1;
			if (a1.weight > a2.weight)
				return -1;

			return 0;
		}
	};

	static final Comparator<Edge> nodesImportanceComp = new Comparator<Edge>() {
		@Override
		public int compare(Edge a1, Edge a2) {
			if (a1.nodesImportance < a2.nodesImportance)
				return 1;
			if (a1.nodesImportance > a2.nodesImportance)
				return -1;

			return 0;
		}
	};

	@Override
	public String toString() {
		String s = "";
		s = s.concat("\nEdge origin: " + this.getOrigin());
		s = s.concat("\nEdge end: " + this.getEnd());
		s = s.concat("\nEdge time: " + (this.getTime()));
		s = s.concat("\nEdge ditance: " + (this.getDistance()));
		s = s.concat("\nEdge road_time: " + (this.getTimeRoad()));
		s = s.concat("\nEdge road_ditance: " + (this.getDistanceRoad()));
		s = s.concat("\nEdge connectivity: " + (this.getConnectivity()));
		s = s.concat("\nEdge weight: " + (this.getWeight()));
		return s;
	}

	public void setInflextionNodes(LinkedList<Edge> inflexionsEdge) {
		roadInflexionEdge = new LinkedList<Edge>();
		roadInflexionNode = new LinkedList<Node>();
		for (int i = 0; i < inflexionsEdge.size(); i++) {
			roadInflexionEdge.add(new Edge(inflexionsEdge.get(i)));
			roadInflexionNode.add(inflexionsEdge.get(i).end);
		}
		System.out.println("done");
	}

}
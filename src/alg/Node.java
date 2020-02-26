package alg;

import java.util.ArrayList;
import java.util.Comparator;

public class Node implements Comparable<Node> {
	static int counter = 0;
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private int id; // node ID (depotID = 0)
	private float x; // node x coordinate
	private float y; // node y coordinate
	private float expDemand = 0.0f; // node (expected) demand
	private Route inRoute = null; // route containing the node
	private boolean isInterior = false; // interior node in a route
	private boolean isOriginAdjacent = false; // adjacent to origin in a route
	private boolean isEndAdjacent = false; // adjacent to end in a route
	private Edge diEdge = null; // edge from depot to node
	private Edge idEdge = null; // edge from node to depot
	private double typeofNode = 0.0;  // 1= road crossing  1.1= DMC or victim nodes
	private float staticScore = 0.0f;  // 1= road crossing  1.1= DMC or victim nodes
	private double importance = 0.0;
	private boolean connection = false;
	private ArrayList<Edge> Adjedges = new ArrayList<>();

	public Node(int nodeId, float nodeX, float nodeY, double type) {
		id = nodeId;
		x = nodeX;
		y = nodeY;
		expDemand = 0;
		this.typeofNode = type; // Profit to visit a node
	}

	public Node(Node n) {
		this.id = n.id;
		this.x = n.x;
		this.y = n.y;
		this.expDemand = n.expDemand;
		if (this.inRoute != null) {
			this.inRoute = new Route(n.inRoute);
		}
		this.isInterior = n.isInterior;
		this.isOriginAdjacent = n.isOriginAdjacent;
		this.isEndAdjacent = n.isEndAdjacent;
		this.typeofNode = n.typeofNode;
		Adjedges = new ArrayList<>();
		for (Edge e : n.getAdjEdgesList()) {
			Edge newEdge = new Edge(e);
			newEdge.setOrigin(new Node(e.getOrigin().getId(), e.getOrigin().getX(), e.getOrigin().getY(),
					e.getOrigin().getTypeNode()));
			newEdge.setEnd(new Node(e.getEnd().getId(), e.getEnd().getX(), e.getEnd().getY(), e.getEnd().getTypeNode()));

			this.Adjedges.add(newEdge);
		}
		this.connection = n.connection;
		this.importance = n.importance;
		if (this.diEdge != null) {
			this.diEdge = new Edge(n.diEdge);
		}
		if (this.idEdge != null) {
			this.idEdge = new Edge(n.idEdge);
		}

	}

	/* SET METHODS */
	public void setImportance(Double i) {
		importance = i;
	}

	public void setStaticScore(float i) {
		staticScore = i;
	}

	public void setInRoute(Route r) {
		inRoute = r;
	}

	public void setIsInterior(boolean value) {
		isInterior = value;
	}

	public void setDiEdge(Edge e) {
		diEdge = e;
	}

	public void setIdEdge(Edge e) {
		idEdge = e;
	}

	public void setProfit(double profit) {
		this.typeofNode = profit;
	}

	public void setAdjedges(ArrayList<Edge> sList) {
		Adjedges = sList;
	}

	public void setConnection(boolean connection) {
		this.connection = connection;
	}

	/* GET METHODS */
	public int getId() {
		return id;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean getConnection() {
		return connection;
	}

	public double getTypeNode() {
		return typeofNode;
	}

	public double getStaticScore() {
		return staticScore;
	}

	// public double getPolicyCost(int index){return PolicyCost[index];}
	public float getExpDemand() {
		return expDemand;
	}

	public Route getInRoute() {
		return inRoute;
	}

	public boolean getIsInterior() {
		return isInterior;
	}

	public Edge getDiEdge() {
		return diEdge;
	}

	public Edge getIdEdge() {
		return idEdge;
	}

	public boolean getIsOriginAdjacent() {
		return isOriginAdjacent;
	}

	public boolean getIsEndAdjacent() {
		return isEndAdjacent;
	}

	public double getImportance() {
		return importance;
	}

	public ArrayList<Edge> getAdjEdgesList() {
		return Adjedges;
	}
	/* AUXILIARY METHODS */

	@Override
	public String toString() {
		String s = "";
		s = s.concat(this.getId() + " ");
		s = s.concat(this.getX() + " ");
		s = s.concat(this.getY() + " ");
		s = s.concat(this.getTypeNode() + " ");
		return s;
	}

	@Override
	public int compareTo(Node comparestu) {
		if (comparestu.typeofNode > typeofNode)
			return 1;
		if (comparestu.typeofNode < typeofNode)
			return -1;
		else
			return 0;
	}

	static final Comparator<Node> positionY = new Comparator<Node>() {
		@Override
		public int compare(Node a1, Node a2) {
			if (a1.getY() < a2.getY())
				return -1;
			if (a1.getY() > a2.getY())
				return 1;
			return 0;
		};
	};

	static final Comparator<Node> ID = new Comparator<Node>() {
		@Override
		public int compare(Node a1, Node a2) {
			if (a1.getId() < a2.getId())
				return -1;
			if (a1.getId() > a2.getId())
				return 1;
			return 0;
		};
	};

	static final Comparator<Node> positionX = new Comparator<Node>() {
		@Override
		public int compare(Node a1, Node a2) {
			if (a1.getX() < a2.getX())
				return -1;
			if (a1.getX() > a2.getX())
				return 1;
			return 0;
		};
	};

}
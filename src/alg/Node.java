package alg;

import java.util.Comparator;
import java.util.LinkedList;

public class Node implements Comparable<Node>
{
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
	private double profit = 0.0;
	private double importance=0.0;
	private boolean connection=false;
	private LinkedList<Edge> Adjedges= new LinkedList<Edge>();
	private LinkedList<Edge> closestNodes= new LinkedList<Edge>();




	public Node(int nodeId, float nodeX, float nodeY, double profit)
	{ 
		id = nodeId;
		x = nodeX;
		y = nodeY;
		expDemand = 0;
		this.profit = profit; //Profit to visit a node
	}

	public Node (Node n){
		this.id = n.id;
		this.x = n.x;
		this.y = n.y;
		this.expDemand = n.expDemand;
		if(this.inRoute != null){
			this.inRoute = new Route(n.inRoute);
		}
		this.isInterior = n.isInterior;
		this.isOriginAdjacent = n.isOriginAdjacent; 
		this.isEndAdjacent = n.isEndAdjacent;
				this.profit = n.profit;
				closestNodes=new LinkedList<Edge>();
				for(Edge e: n.getconnectionsList()){
					this.closestNodes.add(e);
				}

		Adjedges=new LinkedList<Edge>();
		for(Edge e: n.getAdjEdgesList()){
			this.Adjedges.add(e);
		}
		this.connection=n.connection;
		this.importance=n.importance;
		if(this.diEdge  != null){
			this.diEdge = new Edge (n.diEdge);
		}
		if(this.idEdge != null){
			this.idEdge = new Edge(n.idEdge);
		}




	}



	

	/* SET METHODS */
	public void setImportance(Double i){importance = i;}
	public void setInRoute(Route r){inRoute = r;}
	public void setIsInterior(boolean value){isInterior = value;}
	public void setDiEdge(Edge e){diEdge = e;}
	public void setIdEdge(Edge e){idEdge = e;}
	public void setProfit(double profit) {this.profit = profit;}
	public void setAdjedges(LinkedList<Edge> sList) {Adjedges = sList;}
	public void setConnection(boolean connection) {this.connection = connection;}
	public void  setclosestNodesList(LinkedList<Edge> sList) {closestNodes = sList;}
	/* GET METHODS */
	public int getId(){return id;}
	public float getX(){return x;}
	public float getY(){return y;}
	public boolean getConnection() {return connection;}

	public double getProfit() {return profit;}
	// public double getPolicyCost(int index){return PolicyCost[index];}
	public float getExpDemand(){return expDemand;}
	public Route getInRoute(){return inRoute;}
	public boolean getIsInterior(){return isInterior;}
	public Edge getDiEdge(){return diEdge;}
	public Edge getIdEdge(){return idEdge;}
	public boolean getIsOriginAdjacent(){return isOriginAdjacent;}
	public boolean getIsEndAdjacent(){return isEndAdjacent;}
	public double getImportance(){return importance;}
	public LinkedList<Edge> getAdjEdgesList(){return Adjedges;}
	public LinkedList<Edge>  getconnectionsList() {return closestNodes;}
	/* AUXILIARY METHODS */



	private float setCurrentLevel()
	{
		float level = 0.0f; // default (id is odd and multiple of 3)
		if( id % 2 != 0 && id % 3 != 0 ) // id is odd and not multiple of 3
			level = 0.5f * expDemand;
		else if ( id % 2 == 0 && id % 4 == 0 ) // id is even and multiple of 4
			level = expDemand;
		else if ( id % 2 == 0 && id % 4 != 0 ) // id is even and not multiple of 4
			level = 1.5f * expDemand;
		return level;
	}



	public int getIndexPolicy(float value){
		int index;
		if (value == 0.0) {index = 0;}
		else if (value == 0.25) {index = 1;}
		else if (value == 0.50) {index = 2;}
		else if (value == 0.75) {index = 3;}
		else if (value == 1.0) {index = 4;}
		else {index = 5;}
		return index;
	}

	public void setIsOriginAdjacent(boolean mIsOriginAdjacent){isOriginAdjacent=mIsOriginAdjacent;}
	public void setIsEndAdjacent(boolean mIsEndAdjacent){isEndAdjacent=mIsEndAdjacent;}



	@Override
	public String toString() 
	{   String s = "";
	s = s.concat(this.getId() + " ");
	s = s.concat(this.getX() + " ");
	s = s.concat(this.getY() + " ");
	s = s.concat(this.getProfit() + " ");
	return s;
	}


	@Override
	public int compareTo(Node comparestu) {
		if(comparestu.profit>profit) return 1;
		if(comparestu.profit<profit) return -1;
		else return 0;
	}
	static final Comparator<Node>positionY = new Comparator<Node>(){
		public int compare(Node a1, Node a2){
			if (a1.getY() < a2.getY()) return -1;
			if (a1.getY() > a2.getY()) return 1;
			return 0;
	};};
	
	static final Comparator<Node>ID = new Comparator<Node>(){
		public int compare(Node a1, Node a2){
			if (a1.getId() < a2.getId()) return -1;
			if (a1.getId() > a2.getId()) return 1;
			return 0;
	};};
	
	static final Comparator<Node>positionX = new Comparator<Node>(){
		public int compare(Node a1, Node a2){
			if (a1.getX() < a2.getX()) return -1;
			if (a1.getX() > a2.getX()) return 1;
			return 0;
	};};

	//	public static void setImpAdjdges(Inputs inputs) {
	//		LinkedList<Edge> edges= new LinkedList<Edge>();
	//		for(Edge ed:Adjedges) {
	//			int i=ed.getOrigin().getId();
	//			int j=ed.getEnd().getId();
	//			Edge e=inputs.getedge(i, j);
	//			ed.setImportance(e.getImportance());
	//			
	//		}
	//		
	//	}




}
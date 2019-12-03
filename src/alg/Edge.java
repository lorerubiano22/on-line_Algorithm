package alg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class Edge implements Serializable
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private String key;
	private Node origin; // origin node
	private int typeEdge;// 1 if it belongs to the road network 0 otherwise
	private Node end; // end node
	private double time = 0.0; // edge costs (travel time)
	private double distance = 0.0; // edge costs (travel distance)

	private Route inRoute = null; // route containing this edge (0 if no route assigned)
	private Edge inverseEdge = null; // edge with inverse direction
	private Edge thisEdge = null; // edge 
	private int state; // edge states
	private double optCriterion; // selection criterion
	private double nodesImportance;
	private LinkedList<Edge> roadInflexion;
	private LinkedList<Node> roadInflexionNode;
	public double connectivity=0.0;
	public double weight=0.0;
	public int disruptionIndex=-1;
	public double maxAdjConnectivity=0;
	public double minAdjTime=0;
	public double maxAdjTime=0;


	public Edge(Node originNode, Node endNode) 
	{   origin = originNode;
	end = endNode;
	  String s1 = Integer.toString(originNode.getId()); 
      String s2 = Integer.toString(endNode.getId()); 
	String keyEdge=s1+","+s2;
	this.key=keyEdge;
	this.state=0;

	}


	public Edge(Edge e){  
		this.key=e.key;
		this.origin = e.origin;
		this.end = e.end; 
		this.time = e.time;
		this.connectivity = e.connectivity; 
		this.weight = e.weight; 
		this.distance=e.distance;
		//this.selection=e.selection;
		this.state=e.state;
		//this.optCriterion=e.optCriterion;
		this.inverseEdge=e.inverseEdge;
		//this.nodesImportance=e.nodesImportance;
		if(e.inRoute !=null){
			this.inRoute = new Route (e.inRoute);
		}else{
			this.inRoute = null;	
		}

	}



	/* SET METHODS */
	public void settypeEdge(int c){typeEdge = c;}
	public void setState(int c){state = c;}
	public void setDistance(double c){distance = c;}
	public void setTime(double c){time = c;}
	
	public void setConnectivity(double s){connectivity = s;}
	public void setWeight(double s){weight = s;}
	
	
//	public void setImportance(double s){importance = s;
//	optCriterion();
//	setnodesImportance();}
	public void setInRoute(Route r){inRoute = r;}
	public void setInverse(Edge e){inverseEdge = e;}
	public void setEdege(Edge e) {e=thisEdge;}
	public void setoptCriterion(double e) {optCriterion=e;
	setnodesImportance();}

	/* GET METHODS */


public int gettypeEdge(){return typeEdge;}
	public int getState(){return state;}
	public Node getOrigin(){return origin;}
	public Node getEnd(){return end;}
	public double getTime(){return time;}
	public double getDistance(){return distance;}
	public double getConnectivity(){return connectivity;}
	public double getWeight(){return weight;}
	public Route getInRoute(){return inRoute;}
	public Edge getInverseEdge(){return inverseEdge;}
	public double getoptCriterion() {return optCriterion;}
	public LinkedList<Edge> getInflexionEdge() {return  roadInflexion;}
	public LinkedList<Node> getroadInflexionNode() {return  roadInflexionNode;}
	public String getKey(){return key;}
	
	public Edge getEdge(Edge e) {return thisEdge;}

	/* AUXILIARY METHODS */

	public double calcTime()
	{   double X1 = origin.getX();
	double Y1 = origin.getY();
	double X2 = end.getX();
	double Y2 = end.getY();
	double time = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1))/Test.getTravelSpeed();
	return time;
	}

	public double calcDistance()
	{   double X1 = origin.getX();
	double Y1 = origin.getY();
	double X2 = end.getX();
	double Y2 = end.getY();
	double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
	return d;
	}

	public static double calcTime(Node origin, Node end)
	{   double X1 = origin.getX();
	double Y1 = origin.getY();
	double X2 = end.getX();
	double Y2 = end.getY();
	double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1))/Test.getTravelSpeed();
	return d;
	}

	public static double calcDistance(Node origin, Node end)
	{   double X1 = origin.getX();
	double Y1 = origin.getY();
	double X2 = end.getX();
	double Y2 = end.getY();
	double d = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
	return d;
	}



	static final Comparator<Edge> minTime = new Comparator<Edge>(){
		public int compare(Edge a1, Edge a2){
			if (a1.time > a2.time) return 1;
			if (a1.time < a2.time) return -1;
			return 0;
		}
	};

	static final Comparator<Edge> minDistance = new Comparator<Edge>(){
		public int compare(Edge a1, Edge a2){
			if (a1.distance > a2.distance) return 1;
			if (a1.distance < a2.distance) return -1;
			return 0;
		}
	};

	static final Comparator<Edge>connectivityComp = new Comparator<Edge>(){
		public int compare(Edge a1, Edge a2){
			if (a1.connectivity < a2.connectivity) return 1;
			if (a1.connectivity > a2.connectivity) return -1;
			return 0;
		}};


		static final Comparator<Edge>weightComp = new Comparator<Edge>(){
			public int compare(Edge a1, Edge a2){
				if (a1.weight < a2.weight) return 1;
				if (a1.weight > a2.weight) return -1;

				return 0;
			}
		};

		static final Comparator<Edge>nodesImportanceComp = new Comparator<Edge>(){
			public int compare(Edge a1, Edge a2){
				if (a1.nodesImportance < a2.nodesImportance) return 1;
				if (a1.nodesImportance > a2.nodesImportance) return -1;

				return 0;
			}
		};

		private void setnodesImportance() {
			double value=0;
			value=this.getOrigin().getImportance()+this.getEnd().getImportance();
			nodesImportance=value;}



		@Override
		public String toString() 
		{ 
			String s = "";
			s = s.concat("\nEdge origin: " + this.getOrigin());
			s = s.concat("\nEdge end: " + this.getEnd());
			s = s.concat("\nEdge time: " + (this.getTime()));
			s = s.concat("\nEdge ditance: " + (this.getDistance()));
			s = s.concat("\nEdge connectivity: " + (this.getConnectivity()));
			s = s.concat("\nEdge weight: " + (this.getWeight()));
			s=  s.concat("\nEdge status: " + (this.getState()));
			return s;
		}


		public void setInflextionNodes(LinkedList<Edge> inflexionsEdge) {
			roadInflexion= new LinkedList<Edge> ();
			roadInflexionNode = new LinkedList<Node>();
			for(int i=0; i<inflexionsEdge.size();i++) {
			//for(Edge e:inflexionsEdge) {
				roadInflexion.add(new Edge (inflexionsEdge.get(i)));
				roadInflexionNode.add(inflexionsEdge.get(i).end);
			}
			System.out.println("done");
					}
		
		public void setDisruptionIndex(int x) {disruptionIndex=x;}
		
		









}
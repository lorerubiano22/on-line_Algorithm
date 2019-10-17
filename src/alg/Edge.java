package alg;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Edge implements Serializable
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private Node origin; // origin node
	private Node end; // end node
	private double time = 0.0; // edge costs (travel time)
	private double distance = 0.0; // edge costs (travel distance)
	private double importance = 0.0; // edge savings (Clarke & Wright)
	private double selection = 0.0;
	private Route inRoute = null; // route containing this edge (0 if no route assigned)
	private Edge inverseEdge = null; // edge with inverse direction
	private Edge thisEdge = null; // edge 
	private int state=0; // edge states
	private double optCriterion; // selection criterion
	private double nodesImportance;
	
	
	public Edge(Node originNode, Node endNode) 
	{   origin = originNode;
	end = endNode;

	}


	public Edge(Edge e){   
		this.origin = e.origin;
		this.end = e.end; 
		this.time = e.time;
		this.importance = e.importance; 
		this.distance=e.distance;
		this.selection=e.selection;
		this.state=e.state;
		this.optCriterion=e.optCriterion;
		this.inverseEdge=e.inverseEdge;
this.nodesImportance=e.nodesImportance;
		if(e.inRoute !=null){
			this.inRoute = new Route (e.inRoute);
		}else{
			this.inRoute = null;	
		}

	}



	/* SET METHODS */
	public void setState(int c){state = c;}
	public void setDistance(double c){distance = c;}
	public void setTime(double c){time = c;}
	public void setImportance(double s){importance = s;
	optCriterion();
	setnodesImportance();}
	public void setInRoute(Route r){inRoute = r;}
	public void setInverse(Edge e){inverseEdge = e;}
	public void setEdege(Edge e) {e=thisEdge;}
	public void setoptCriterion(double e) {optCriterion=e;
	optCriterion();
	setnodesImportance();}

	/* GET METHODS */

	


	public int getState(){return state;}
	public Node getOrigin(){return origin;}
	public Node getEnd(){return end;}
	public double getTime(){return time;}
	public double getDistance(){return distance;}
	public double getImportance(){return importance;}
	public Route getInRoute(){return inRoute;}
	public Edge getInverseEdge(){return inverseEdge;}
	public double getoptCriterion() {return optCriterion;}
	public double getvalueSelection() {return selection;}
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

	public void optCriterion()
	{  if(this.origin.getId()!=this.end.getId()) {
		selection=0.5*(States.importancesEdges[this.origin.getId()][this.end.getId()]/Inputs.getMaxImportance())+0.5*(Inputs.getMaxDistance()-distance)/((Inputs.getMaxDistance()+0.0001)-Inputs.getMinDistance());
	}
	else {selection=0;}
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

	static final Comparator<Edge>importanceComp = new Comparator<Edge>(){
		public int compare(Edge a1, Edge a2){
			if (a1.importance < a2.importance) return 1;
			if (a1.importance > a2.importance) return -1;
			return 0;
		}};


		static final Comparator<Edge>optCriterionComp = new Comparator<Edge>(){
			public int compare(Edge a1, Edge a2){
				if (a1.selection < a2.selection) return 1;
				if (a1.selection > a2.selection) return -1;

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
			s = s.concat("\nEdge importance: " + (this.getImportance()));
			s=  s.concat("\nEdge status: " + (this.getState()));
			
			
			return s;
		}


		






}
package alg;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Route implements Serializable,Comparable<Route>
{
	/* INSTANCE FIELDS & CONSTRUCTOR */

	private double coverage = 0.0; // route total costs
	private double time = 0.0; // route total travel time
	private double distance = 0.0; // route total travel distance
	private float demand = 0.0F; // route total demand
	private LinkedList<Edge> edges; // edges list
	private float[] center; // (x-bar, y-bar) for all (x,y) in the route
	private float getYRouteCenter = 0.0F; 
	private float getXRouteCenter = 0.0F;
	private double score = 0.0;
	private int Lastconnections = 0;
	


	//Constructor
	public Route() 
	{   edges = new LinkedList<Edge>();
	center = new float[2];
	}



	public Route(Route r) 
	{ 

		center = new float[2];	
		this.coverage=r.coverage;
		this.time = r.time;
		this.distance = r.distance;
		this.demand = r.demand;
		this.center  = r.center;
		this.getYRouteCenter = r.getYRouteCenter;
		this.getXRouteCenter = r.getXRouteCenter;
		this.score = r.score;
		this.Lastconnections=r.getLastconnections();
		edges = new LinkedList<Edge>();
		LinkedList<Edge> edges;
		for(Edge e: r.getEdges()){
			Edge eAux = new Edge(e);
			this.edges.add(e);
		}

	}






	/* SET METHODS */
	public void setTime(double c){time = c;}
	public void setDistance(double c){distance = c;}
	public void setCoverage(double c){coverage = c;}
	public void setDemand(float d){demand = d;}
	public void setCenter(float[] coord){center = coord;}
	public void setEdges(LinkedList<Edge> e){edges = e;}
	public void setXRouteCenter(float getXRouteCenter) {this.getXRouteCenter = getXRouteCenter;}
	public void setYRouteCenter(float getYRouteCenter) {this.getYRouteCenter = getYRouteCenter;}
	public void setScore(double score) {this.score = score;}
	public void addEdge(int i, Edge e){
		Edge eAux = new Edge(e);
		this.edges.add(i, e);
	}
	public void setLastconnections(int connection) {this.Lastconnections = connection;}
	

	/* LIST GET METHODS*/
	public double getcoverage(){return coverage;}
	public double getTime(){return time;}
	public double getDistance(){return distance;}
	public float getDemand(){return demand;}
	public float[] getCenter(){return center;}
	public List<Edge> getEdges(){return edges;}
	public double getScore() {return score;}
	public float getYRouteCenter() {return getYRouteCenter;}
	public float getXRouteCenter() {return getXRouteCenter;}
	public void removeEdge(Edge e){this.edges.remove(e);}
	public void substractCosts(Edge e){this.time = this.time - e.getTime();}
	public int getLastconnections() {return Lastconnections;}
	/* AUXILIARY METHODS */
	/** 
	 * Reverses a route, e.g. (0 -> 2 -> 6 -> 0) becomes (0 -> 6 -> 2 -> 0)
	 */
	public void reverse()
	{   
		for( int i = 0; i < edges.size(); i++ )
		{   Edge e = edges.get(i);
		Edge invE = e.getInverseEdge();
		edges.remove(e);
		edges.add(0, invE);
		}
	}


	@Override
	public String toString() 
	{   String s = "";
	s = s.concat("\nRute time: " + (this.getTime()));
	s = s.concat("\nRuta demand:" + this.getDemand());
	s = s.concat("\nRuta edges: " + this.getEdges());
	return s;
	}



	@Override
	public int compareTo(Route arg0) {
		// TODO Auto-generated method stub
		if(score < arg0.score) return 1;
		if (score > arg0.score) return -1;
		else return 0;
	}



	public void sortRoute() {
		int origen=0;
		int EdgeId=0;
		Edge FirstEdge=null;
		Edge toInsert=null;
		Node nOr=null;
		Node nEnd=null;
		LinkedList<Edge> edgesCopy=new LinkedList<Edge>();
		LinkedList<Edge> route=new LinkedList<Edge>();
		/*Coping edges list*/
		//for(Edge e:this.getEdges()) {
		for(int e=0;e<this.getEdges().size();e++) {
			nOr=this.getEdges().get(e).getOrigin();
			nEnd=this.getEdges().get(e).getEnd();
			toInsert=new Edge(nOr,nEnd);
			edgesCopy.add(toInsert);
			if(toInsert.getOrigin().getId()==origen) {
				FirstEdge=new Edge(nOr,nEnd);
				EdgeId=nEnd.getId();
				route.addLast(FirstEdge);
				edgesCopy.remove(e);}
		}
		while(route.size()<this.getEdges().size()) {
			EdgeId=route.getLast().getEnd().getId();
			for(int i=edgesCopy.size()-1;i>-1;i--) {
				if(edgesCopy.get(i).getOrigin().getId()==EdgeId) {
					nOr=edgesCopy.get(i).getOrigin();
					nEnd=edgesCopy.get(i).getEnd();
					toInsert=new Edge(nOr,nEnd);
					route.addLast(toInsert);
					edgesCopy.remove(i);}
			}
		}
		System.out.println("Initial order route");
		for(Edge e:this.getEdges()) {

			System.out.println(e.getOrigin().getId()+","+e.getEnd().getId());
		}

		for(int i=this.getEdges().size()-1;i>-1;i--) {
			this.removeEdge(this.getEdges().get(i));
		}
		for(int i=0;i<route.size();i++) {
			this.addEdge(i, route.get(i));

		}

		System.out.println("after sort order route");
		for(Edge e:this.getEdges()) {

			System.out.println(e.getOrigin().getId()+","+e.getEnd().getId());
		}

	}



	public void calcTime() {
		double cost=0;
		for(Edge e:this.getEdges()) {
			cost+=e.getTime();
		}
		this.setTime(cost);
	}


	public void calcDistance() {
		double cost=0;
		for(Edge e:this.getEdges()) {
			cost+=e.getDistance();
		}
		this.setDistance(cost);
	}

	public void calcCoverage() {
		int c=0;
		for(Edge e:this.edges) {
			if(e.getEnd().getId()!=0 && e.getEnd().getProfit()>1 && e.getEnd().getId()!=0 && e.getEnd().getId()!=0) {
				c++;
			}	
		}
		this.setCoverage(c);
	}





}

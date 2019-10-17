package alg;
import java.io.Serializable;
import java.util.LinkedList;


public class Inputs implements Serializable
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	public Node[] nodes; // List of all nodes in the problem/sub-problem
	private int vehicles = 0; // Vehicle capacity (homogeneous fleet
	public double DrivingRange=0;
	public double BenchmarkTime=0;
	public LinkedList<Edge> edgeList = null;
	public LinkedList<Edge> depotDistance = null; //Distance depot with customers 
	public static double Max_Distance;
	public static double Min_Distance;
	public static double Max_Importance;
	public Inputs(int n)
	{   nodes = new Node[n]; // n nodes, including the depot
	}

	

	public Inputs(Inputs i, Node[] nodes){
		this.nodes = nodes;
		vehicles = i.vehicles;
		this.DrivingRange=i.DrivingRange;
		this.BenchmarkTime=i.DrivingRange;
		
	}

	/* GET METHODS */
	public Node[] getNodes(){return nodes;}
	public Node getNode(int i) {return nodes[i];}
	public Edge getedge(int o, int e){
		Edge selectEdge=null;
		for(Edge edge:edgeList) {
			if(edge.getOrigin().getId()==o && edge.getEnd().getId()==e) {
				selectEdge=new Edge(edge);
				break;
			}
		}
		return selectEdge;}

	public LinkedList<Edge> getedgeList(){return edgeList;}
	public int getVehNumber(){return vehicles;}
	public double getDrivingRange(){return DrivingRange;}
	public double getBenchmarkTime(){return DrivingRange;}
	public LinkedList<Edge> getdistanceDepot(){return depotDistance;}

	
	
	/* SET METHODS */
	public void setVehNumber(int c){vehicles = c;}
	public void setDrivingRange(double DR){DrivingRange = DR;}
	public void setNodes(Node[] nodes){this.nodes = nodes;}
	public void setdistanceDepot(LinkedList<Edge> sList){depotDistance = sList;}
	public void setEdgeList(LinkedList<Edge> sList) {edgeList = sList;
	setMaximportance(sList);
	setMaxdistance(sList);
	setMindistance(sList);}
	public void remove(int index){
		Node[] nNode=new Node[getNodes().length-1];
		int j=0;
		for(int i=0;i<getNodes().length-1;i++){
			if(i!=index) {
				nNode[i]=new Node(nodes[j]);
				j++;
			}
			else {
				nNode[i]=new Node(nodes[i+1]);
				j++;
				j++;
			}
		}
		nodes = new Node[nNode.length];
		for(int i=0;i<nNode.length;i++){
			nodes[i]=new Node(nNode[i]);
		}
	}
	public void UpdatingEdgesList(LinkedList<Edge> sList) {
	//setEdgeList(sList);
		
	setMaximportance(sList);
	setMaxdistance(sList);
	setMindistance(sList);
	for(Edge e:sList) {
		e.optCriterion();
	}
	}

	public void setBenchmarck(double totalCosts) {BenchmarkTime=(totalCosts);}



	public static void setMaxdistance(LinkedList<Edge> edgeList2) {
		double distance=0;
		for(Edge e:edgeList2) {
			int i=e.getOrigin().getId();
			int j=e.getEnd().getId();
			if(States.DisruptedNetwork[i][j]>0) {
			if(e.getDistance()>distance) {
				distance=e.getDistance();
			}}
		}
		Max_Distance=distance;
		
	}
	
	public static void setMindistance(LinkedList<Edge> edgeList2) {
		double distance=0;
		for(Edge e:edgeList2) {
			int i=e.getOrigin().getId();
			int j=e.getEnd().getId();
			if(States.DisruptedNetwork[i][j]>0) {
			if(e.getDistance()<distance) {
				distance=e.getDistance();
			}
		}}
		Min_Distance=distance;
		
	}




	public static double getMaxDistance() {return Max_Distance;}
	public static double getMinDistance() {return Min_Distance;}
	public static double getMaxImportance() {return Max_Importance;}

	
	public static void setMaximportance(LinkedList<Edge> edgeList2) {
		double imp=0;
		for(Edge e:edgeList2) {
			int i=e.getOrigin().getId();
			int j=e.getEnd().getId();
			if(States.DisruptedNetwork[i][j]>0) {
			if(States.importancesEdges[i][j]>imp) {
				
				imp=States.importancesEdges[e.getOrigin().getId()][e.getEnd().getId()];
			}}
		}
		Max_Importance=imp;
		
	}




	

}
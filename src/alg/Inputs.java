package alg;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Inputs implements Serializable
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	public static Node[] nodes; // List of all nodes in the problem/sub-problem
	public static LinkedList<Node> victimnodes= new LinkedList<Node>(); 
	public static LinkedList<Node> intermediatenodes= new LinkedList<Node>(); 
	public static LinkedList<Edge> edgesList= new LinkedList<Edge>(); 
	private int vehicles = 0; // Vehicle capacity (homogeneous fleet
	public double DrivingRange=0;
	public double BenchmarkTime=0;
	public LinkedList<Edge> edgeList = null;
	public LinkedList<Edge> depotDistance = null; //Distance depot with customers 
	public static double Max_Distance;
	public static double Min_Distance;
	public static double Max_Importance;
	static HashMap<String, Edge> directoryEdges;
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
	
	public static void setEdgeList(LinkedList<Edge> sList) {	
		directoryEdges = new HashMap<String, Edge>();
		for(Edge e:sList) {
			directoryEdges.put(e.getKey(),e);
			edgesList.add(e);
			}
		
	}
	
	
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
	public  LinkedList<Node> getvictimnodes() {return victimnodes;}
	public  LinkedList<Node> getintermediatenodes() {return intermediatenodes;}
	public static Edge getdirectoryEdges(String a) {return directoryEdges.get(a);}
	
	
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



	public static void setTypeofNodes() {
		for(int i=1;i<nodes.length;i++) {
if(nodes[i].getProfit()>1) {
	victimnodes.add(nodes[i]);
}
else {intermediatenodes.add(nodes[i]);}
		}
		
	}



	public static void setAdjEdges() {
			for(Node n:nodes) {
				LinkedList<Edge> adjEdges= new LinkedList<Edge>();
				for(int j=0;j<=nodes.length;j++) {
					String s1 = Integer.toString(n.getId()); 
				      String s2 = Integer.toString(j); 
					String key=s1+","+s2;
					
						Edge e= directoryEdges.get(key);
						
					if(e!=null) {
						adjEdges.add(e);}
					
				}
				n.setAdjedges(adjEdges);
		
			}
			
			System.out.println("Done");
			
		
	}




	

}
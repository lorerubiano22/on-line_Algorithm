package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;





public class Validation {

	// Solution data
	private Solution sol;
	private final Test aTest;
	private final Inputs inputs;
	private final Disaster Event;
	private final Network informationRoadNetwork;
	private Outputs outputValidation;
	// Input data: network information
	private Node[] nodes; // List of all nodes in the problem/sub-problem
	private final ArrayList<Edge> edgeRoadConnections = new ArrayList<>();
	private ArrayList<Edge> edgeRoadNetwork = new ArrayList<>();
	private final ArrayList<Edge> edgeAerialNetwork = new ArrayList<>();
	private final Map<String, Edge> directoryRoadEdgesNetwork = new HashMap<String, Edge>();
	private final Map<String, Edge> directoryAerialEdgesNetwork = new HashMap<String, Edge>();
	private final Map<Integer, Node> directoryNode=new HashMap<>();
	private final Map<Integer, Node> VictimList=new HashMap<>(); // list of all victim nodes

	/// comparison Criteria
	private final Map<Integer, Node> recheablevictims= new HashMap<>();
	private boolean victimStateValidation=false; // the correct solution have to provide a true
	private boolean RepeatedEdgesValidation=false; // the correct solution have to provide a true
	private boolean[] criteria= new boolean[2]; // List of criteria to evaluate - victim accessibility and repeated edges


	public Validation(Test t, Inputs inp, InsertionProcedure insertion, Network network, Disaster event2, Assessment labelledInformation) {
		// Solution data
		sol=insertion.getSol_exploration();
		// Input data: network information
		aTest=t;
		Event=event2;
		inputs=inp;
		informationRoadNetwork=network;

		// 1. validation victim accessibility
		VictimStateValidation();
		criteria[0]=victimStateValidation;
		//2. Validation if there is road connections repeated
		repeatedEdges();
		criteria[1]=RepeatedEdgesValidation;
		outputValidation=new Outputs(Event, inputs, aTest,criteria);
		//outputValidation.setCriteria(criteria);

	}





	// Accessibility to data

	private void repeatedEdges() {
		ArrayList<Edge> listEdge= new ArrayList<>();
		for(Edge e:sol.getExplorationRoute().getEdges()) {

			if(listEdge.contains(e)) {
				RepeatedEdgesValidation=false;
				break;
			}
			else {
				listEdge.add(e);
			}
		}
		if(sol.getExplorationRoute().getEdges().equals(listEdge)) {
			RepeatedEdgesValidation=true;
		}

	}





	private void VictimStateValidation() {
		FindingPath tree = new FindingPath();



		Map<String, Edge> disruptedNetwork= new HashMap<>(); // to pass the information to the method "findingallPathsToVictimNode"
		for(Edge e:Event.getDisruptedRoadConnections()) {
			disruptedNetwork.put(e.getKey(), e);
		}
		ArrayList<Node> nodeList= new ArrayList<>();
		for(Node n:informationRoadNetwork.getdirectoryNode().values()) { // to pass the information to the method "findingallPathsToVictimNode"
			nodeList.add(n);
		}
//

		ArrayList<Edge> victimsansintermediateNetwork = new ArrayList<>();
		for (Edge e : Event.getDisruptedRoadConnections()) {
			Edge newEdge = new Edge(e);
			victimsansintermediateNetwork.add(newEdge);
		}
///
		for(Node victim:informationRoadNetwork.getVictimList().values()) {
			// compute all the path
			boolean paths=false;

			Map<Integer, Node> paths1= tree.spanningTreePath(aTest,victimsansintermediateNetwork, victim.getId());
			boolean additionalTest =tree.spanningTree(Event.getDisruptedRoadConnections(),0,victim.getId());
			if(paths1.containsKey(0) || additionalTest) {
				paths= true;
			}

			if(paths) { // there is one or more than one path to reach the victim
				recheablevictims.put(victim.getId(), victim);
			}
		}
		if(recheablevictims.equals(sol.getrecheablevictims())) {
			victimStateValidation=true;
		}

	}





	public Solution getsolution() {
		return sol;
	}
	public boolean[] getCriteria() {
		return criteria;
	}


	public Test getaTest() {
		return aTest;
	}
	public Disaster getEvent() {
		return Event;
	}
	public Node[] getNodes() {
		return nodes;
	}
	public ArrayList<Edge> getEdgeRoadConnections() {
		return edgeRoadConnections;
	}
	public ArrayList<Edge> getEdgeRoadNetwork() {
		return edgeRoadNetwork;
	}
	public ArrayList<Edge> getEdgeAerialNetwork() {
		return edgeAerialNetwork;
	}
	public Map<String, Edge> getDirectoryRoadEdgesNetwork() {
		return directoryRoadEdgesNetwork;
	}
	public Map<String, Edge> getDirectoryAerialEdgesNetwork() {
		return directoryAerialEdgesNetwork;
	}
	public Map<Integer, Node> getDirectoryNode() {
		return directoryNode;
	}
	public Map<Integer, Node> getVictimList() {
		return VictimList;
	}

	public boolean isCorrectVictimStateinSolution() {
		return victimStateValidation;
	}

}

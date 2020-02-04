package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;





public class Validation {

	// Solution data
	private Solution jump_Sol;
	private final Test aTest;
	private final Inputs inputs;
	private final Disaster Event;
	private final Network informationRoadNetwork;
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
	private boolean victimStateValidation=false;


	public Validation(Test t, Inputs inp, InsertionProcedure insertion, Network network, Disaster event2, Assessment labelledInformation) {
		// Solution data
		jump_Sol=insertion.getSol_exploration();
		// Input data: network information
		aTest=t;
		Event=event2;
		inputs=inp;
		informationRoadNetwork=network;

		// 1. validation victim accessibility
		VictimStateValidation();

		//2. Validation if there is road connections repeated


	}





	// Accessibility to data

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

		for(Node victim:informationRoadNetwork.getVictimList().values()) {
			// compute all the path

			boolean paths= tree.spanningTree(Event.getDisruptedRoadConnections(), 0, victim.getId());
			if(paths) { // there is one or more than one path to reach the victim
				recheablevictims.put(victim.getId(), victim);
			}
		}
		if(recheablevictims.equals(jump_Sol.getrecheablevictims())) {
			victimStateValidation=true;
		}

	}





	public Solution getJump_Sol() {
		return jump_Sol;
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

package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateRoadInformation {
	private final  Map<String, Edge> originalEdgeRoadConnection; // all road connections
	private final ArrayList<Edge> edgeRoadConnection; // all road connections
	private final Map<String, Edge> revealedDisruptedRoadConnections; // revealed disrupted
	private Map<String, Edge> revealedDisruptedEdges= new HashMap<>(); // list of revealed disrupted edges
	private final ArrayList<Node> nodeList; // list of nodes
	private final Map<Integer, Node> directoryNodes;// It contains all nodes of the network
	private final ArrayList<Edge> edgeAerialConnection = new ArrayList<>();
	private final Map<Integer, Node> VictimList = new HashMap<>(); // list of nodes
	private final Map<String, Edge> directoryroadConnections; // It contains all edges of the network
	private final Map<String, Edge> directoryAerialConnections; // It contains all edges of the network
	private final HashMap<Integer, Node> checkedAccesibiliyVictims = new HashMap<>();
	private final Map<String, Edge>visitedRoadConnections= new HashMap<>();
	private final HashMap<String, Edge> connectedEdgestoRevealedRoadNetwork= new HashMap<>(); // storage the edges that belong to a route to
	private final Map<Integer, Node> connectedNodestoRevealedRoadNetwork= new HashMap<>();

	public UpdateRoadInformation(Network network) {
		revealedDisruptedRoadConnections = new HashMap<>();
		directoryroadConnections = new HashMap<>();
		originalEdgeRoadConnection= new HashMap<>();
		edgeRoadConnection = new ArrayList<>();
		directoryAerialConnections = new HashMap<>();
		for (Edge e : network.getEdgeAerialNetwork()) {
			directoryAerialConnections.put(e.getKey(), e);
		}
		// TO DO
		for (Edge e : network.getEdgeConnections()) {// the road connections are created
			edgeRoadConnection.add(e);
			originalEdgeRoadConnection.put(e.getKey(), e);
			if(e.getOrigin().getId()==18 && e.getEnd().getId()==19){
				System.out.print("stopr");
			}
		}
		for (Edge e : edgeRoadConnection) {// the road connections are created
			revealedDisruptedRoadConnections.put(e.getKey(), e);
			directoryroadConnections.put(e.getKey(), e);
			if(e.getOrigin().getId()==18 && e.getEnd().getId()==19){
				System.out.print("stopr");
			}
		}
		// TO DO
		nodeList = new ArrayList<>();
		directoryNodes = new HashMap<>();
		for (Node n : network.getNodes()) {// list of nodes and victim list
			nodeList.add(n);
			directoryNodes.put(n.getId(), n);
			if (n.getProfit() > 1 && n.getId() != 0) {
				VictimList.put(n.getId(), n);
			}
		}
		for (Node n : nodeList) {// list of nodes
			directoryNodes.put(n.getId(), n);
		}

		for (Edge e : network.getEdgeAerialNetwork()) {// the aerial connectios are created
			edgeAerialConnection.add(e);
		}

	}

	// GETTERS

	public ArrayList<Edge> getedgeAerialConnection() {
		return edgeAerialConnection;
	}

	public Map<String, Edge> getRevealedDisruptedRoadConnections() {
		return revealedDisruptedRoadConnections;
	}

	public Map<String, Edge> getdirectoryAerialConnections() {
		return directoryAerialConnections;
	}

	public Map<String, Edge> getdirectoryroadConnections() {
		return directoryroadConnections;
	}

	public ArrayList<Edge> getedgeRoadConnection() {
		return edgeRoadConnection;
	}

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	public Map<Integer, Node> getVictimList() {
		return VictimList;
	}

	public Map<Integer, Node> getdirectoryNodes() {
		return directoryNodes;
	}

	public Map<Integer, Node> getcheckedAccesibiliyVictims() {
		return checkedAccesibiliyVictims;
	}


	public Map<String, Edge> getrevealedDisruptedEdges() {
		return revealedDisruptedEdges;
	}

	public Map<String, Edge> getvisitedRoadConnections() {
		return visitedRoadConnections;
	}

	public Map<String, Edge> getoriginalEdgeRoadConnection() {
		return originalEdgeRoadConnection;
	}

	public HashMap<String, Edge> getconnectedEdgestoRevealedRoadNetwork() {
		return connectedEdgestoRevealedRoadNetwork;
	}

	public  Map<Integer, Node> getconnectedNodestoRevealedRoadNetwork() {
		return connectedNodestoRevealedRoadNetwork;
	}



}

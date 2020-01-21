package alg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class Inputs implements Serializable {
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private Node[] nodes; // List of all nodes in the problem/sub-problem
	private LinkedList<Node> victimnodes = new LinkedList<Node>();
	private LinkedList<Node> intermediatenodes = new LinkedList<Node>();
	private LinkedList<Edge> edgesList = new LinkedList<Edge>();
	private double DrivingRange = 0;

	private LinkedList<Edge> edgeList = null;
	private HashMap<String, Edge> directoryEdges;
	private HashMap<Integer, Node> directoryNodes;

	public Inputs(int n) {
		nodes = new Node[n]; // n nodes, including the depot
	}

	/* GET METHODS */
	public Node[] getNodes() {
		return nodes;
	}

	public Node getNode(int i) {
		return nodes[i];
	}

	public Edge getedge(int o, int e) {
		Edge selectEdge = null;
		for (Edge edge : edgeList) {
			if (edge.getOrigin().getId() == o && edge.getEnd().getId() == e) {
				selectEdge = new Edge(edge);
				break;
			}
		}
		return selectEdge;
	}

	public double getDrivingRange() {
		return DrivingRange;
	}

	/* SET METHODS */

	public void setDrivingRange(double DR) {
		DrivingRange = DR;
	}

	public void setEdgeList(LinkedList<Edge> sList) {
		setNodes();
		directoryEdges = new HashMap<String, Edge>();
		for (Edge e : sList) {
			directoryEdges.put(e.getKey(), e);
			edgesList.add(e);
		}

	}

	private void setNodes() {
		directoryNodes = new HashMap<Integer, Node>();
		for (Node n : nodes) {
			directoryNodes.put(n.getId(), nodes[n.getId()]);
		}

	}

	public void remove(int index) {
		Node[] nNode = new Node[getNodes().length - 1];
		int j = 0;
		for (int i = 0; i < getNodes().length - 1; i++) {
			if (i != index) {
				nNode[i] = new Node(nodes[j]);
				j++;
			} else {
				nNode[i] = new Node(nodes[i + 1]);
				j++;
				j++;
			}
		}
		nodes = new Node[nNode.length];
		for (int i = 0; i < nNode.length; i++) {
			nodes[i] = new Node(nNode[i]);
		}
	}

	public LinkedList<Node> getvictimnodes() {
		return victimnodes;
	}

	public LinkedList<Node> getintermediatenodes() {
		return intermediatenodes;
	}

	public void setTypeofNodes() {
		for (int i = 1; i < nodes.length; i++) {
			if (nodes[i].getProfit() > 1) {
				victimnodes.add(nodes[i]);
			} else {
				intermediatenodes.add(nodes[i]);
			}
		}

	}

}
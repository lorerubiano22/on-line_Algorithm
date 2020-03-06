package alg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Network {
	private Node[] nodes; // List of all nodes in the problem/sub-problem
	private final ArrayList<Edge> edgeRoadConnections = new ArrayList<>();
	private ArrayList<Edge> edgeRoadNetwork = new ArrayList<>();
	private final ArrayList<Edge> edgeAerialNetwork = new ArrayList<>();
	private final Map<String, Edge> spanningTree = new HashMap<String, Edge>();
	private final Map<String, Edge> directoryRoadEdgesNetwork = new HashMap<String, Edge>();
	private final Map<String, Edge> directoryAerialEdgesNetwork = new HashMap<String, Edge>();
	private final Map<Integer, Node> directoryNode=new HashMap<>();
	private final Map<Integer, Node> VictimList=new HashMap<>(); // list of all victim nodes

	// information instance
	private double maxDistanceFromDepot=Double.MIN_VALUE;
	private double minDistanceFromDepot=Double.MAX_VALUE;
	private double maxDistance=Double.MIN_VALUE;
	private double minDistance=Double.MAX_VALUE;
	private double averageDistance=0;

	private double averageAdjEdges=0;
	private double cycles=0;




	public Network(Inputs inp) {
		nodes = new Node[inp.getNodes().length];
		for (int i = 0; i < inp.getNodes().length; i++) {
			this.nodes[i] = new Node (inp.getNodes()[i]);
		}
	}

	public Network generateroadNetwork(Test t, Inputs inp) {
		inp.setTypeofNodes();
		generationRoadAerialNetwork(); // creating the aerial network
		generatesRoadConnections(t);// creating the road connection networks
		//setAdjEdges(); // it set the adjacent edges for each node
		genarateRoads(t); // generating break points
		//new DrawingNetwork(edgeRoadNetwork,t);
		setAdjEdges(); // it set the adjacent edges for each node
		//new DrawingNetwork(edgeRoadNetwork,t);
		generatingVictimNodes(t, inp); // set the victim nodes
		//setAdjEdges(); // it set the adjacent edges for each node
		statisticsInstance(t, inp); // set the victim nodes
		return this;
	}




	private void statisticsInstance(Test t, Inputs inp) {
		String file = new String(t.getInstanceName() + "_inf_Instance.txt");
		maxDistance();
		averageAdjEdges();
		totalCycles();

		printingInformation(file);

	}

	private void printingInformation(String file) {

		try {
			PrintWriter bw = new PrintWriter(file);

			bw.println("_Total_nodes_"+this.nodes.length);
			bw.println("_Total_edges_"+this.edgeRoadConnections.size());
			bw.println("_Total_victims_"+this.VictimList.size());
			bw.println("_max_Distance_From_Depot_"+maxDistanceFromDepot);
			bw.println("_min_Distance_From_Depot_"+minDistanceFromDepot);
			bw.println("_max_Distance_"+maxDistance);
			bw.println("_minDistance_"+minDistance);
			bw.println("_averageDistance_"+averageDistance);
			bw.println("_average_Adj_Edges_"+averageAdjEdges);
			bw.println("_edges_added_to_the_spanning_tree_"+cycles);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// why does the catch need its own curly?
		}



	}

	private void totalCycles() {
		cycles=edgeRoadConnections.size()-spanningTree.size();

	}

	private void averageAdjEdges() {
		int[] adjEdges= new int[this.nodes.length];
		double adjAmount=0;
		for(Node n:this.nodes) {
			adjEdges[n.getId()]=n.getAdjEdgesList().size();
			adjAmount+=n.getAdjEdgesList().size();
		}
		averageAdjEdges=adjAmount/this.nodes.length;


	}

	private void maxDistance() {
		double dist=0;
		for(Edge e:this.edgeRoadConnections) {
			if(e.getEnd().getId()==0 || e.getOrigin().getId()==0 ) {
				if(e.getDistance()>maxDistanceFromDepot) {
					maxDistanceFromDepot=e.getDistance();
				}
				if(e.getDistance()<minDistanceFromDepot) {
					maxDistanceFromDepot=e.getDistance();
				}
			}
			// max and min distance
			if(e.getDistance()>maxDistance) {
				maxDistance=e.getDistance();
			}
			if(e.getDistance()<minDistance) {
				minDistance=e.getDistance();
			}
			dist+=e.getDistance();
		}
		averageDistance=dist/edgeRoadConnections.size();
	}

	private void generatingVictimNodes(Test t, Inputs inp) {
		int victimNodeSize = Math.round(((this.nodes.length - 2) * t.getVictimNodesPercentage()));
		int totalVictim = 0;
		for (int i = 1; i < this.nodes.length; i++) {
			nodes[i].setProfit(0);// resetting Node
		}

		for (int i = 1; i < this.nodes.length; i++) {
			if(i==25) {
				System.out.println(nodes[i].getAdjEdgesList().size());
			}
			if (nodes[i].getAdjEdgesList().size() <= 1) {
				nodes[i].setProfit(1.1);
				totalVictim++;
				if (totalVictim >= victimNodeSize) {
					break;
				}
			} else {
				nodes[i].setProfit(1);
			}
		}

		for (int i = 1; i < this.nodes.length; i++) { // setting the intermediate nodes
			if (nodes[i].getTypeNode() == 0) {
				nodes[i].setProfit(1);
			}
		}
		Random rn1 = new Random(93805); // this seed is set for fixing victim locations
		while (totalVictim < victimNodeSize) { // checking if the total of victim nodes meet the characteristic of the
			int IndexdisruptedNonodes = rn1.nextInt(nodes.length - 1);
			if (nodes[IndexdisruptedNonodes].getTypeNode() == 1) {
				nodes[IndexdisruptedNonodes].setProfit(1.1);
				totalVictim++;
			}
		}

		for (int i = 0; i < inp.getNodes().length; i++) { // changing intial information
			inp.getNodes()[i].setProfit(nodes[i].getTypeNode());
		}

		for(Node n:nodes) {
			if(n.getTypeNode()>1 && n.getId()!=0) {
				this.VictimList.put(n.getId(), n);
			}
		}
	}



	private void setNodes() {
		for (Node n : nodes) {
			directoryNode.put(n.getId(), nodes[n.getId()]);
		}
	}

	public void setAdjEdges() {
		for (Node n : nodes) {
			if(n.getId()==25) {
				System.out.println(n.getAdjEdgesList().size());
			}
			ArrayList<Edge> adjEdges = new ArrayList<>();
			for (int j = 0; j <= nodes.length; j++) {
				String s1 = Integer.toString(n.getId());
				String s2 = Integer.toString(j);
				String key = s1 + "," + s2;
				Edge e = directoryRoadEdgesNetwork.get(key);
				if (e != null) {
					adjEdges.add(e);
				}
			}
			n.setAdjedges(adjEdges);
			if(n.getId()==25) {
				System.out.println(n.getAdjEdgesList().size());
			}
		}
	}

	/* Road connections */ // -- Kruskal Algorithm

	public void generatesRoadConnections(Test inp) {
		LinkedList<Edge> copyAerialNetwork = new LinkedList<Edge>();
		for (Edge e : this.edgeAerialNetwork) {
			copyAerialNetwork.add(new Edge(e));
		}

		Edge[] result = spanningTree(copyAerialNetwork); // creating the road network based on the spannig tree
		setNodes();
		for (int i = 0; i < result.length - 1; i++) { // setting the directory of the road network
			directoryRoadEdgesNetwork.put(result[i].getKey(), result[i]);
			directoryRoadEdgesNetwork.put(result[i].getInverseEdge().getKey(), result[i].getInverseEdge());

		}


		for (Edge e : directoryRoadEdgesNetwork.values()) { // setting the road network connections
			this.edgeRoadConnections.add(e);
			spanningTree.put(e.getKey(), e);
		}

		//new DrawingNetwork(edgeRoadConnections,inp);

		for (int i = 0; i < this.nodes.length - 1; i++) { // setting the road network
			for (int j = i + 1; j < this.nodes.length - 1; j++) {
				boolean merge = false;
				String key = i + "," + j;
				String key1 = j + "," + i;
				if (!directoryRoadEdgesNetwork.containsKey(key) || !directoryRoadEdgesNetwork.containsKey(key1)) {
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					// merge = false;
					if (!inp.getInstanceName().equals("p2.2.a") && !inp.getInstanceName().equals("p3.2.a")
							&& !inp.getInstanceName().equals("p4.2.a") && !inp.getInstanceName().equals("p5.2.a")
							&& !inp.getInstanceName().equals("p6.2.a") && !inp.getInstanceName().equals("p7.2.a")) {
						merge = setAdditionalConnections(e3, inp);
						if (merge) {
							directoryRoadEdgesNetwork.put(e3.getKey(), e3);
							directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
						}
					}
				}


			}// end setting the road network
		}
		// Adding connections
		if (inp.getInstanceName().equals("p1.2.b")) {
			String key = 15 + "," + 13;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 13 + "," + 3;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 12 + "," + 17;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 8 + "," + 12;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 23 + "," + 10;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


			/// sensitivity
			//
			key = 8 + "," + 0;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 17 + "," + 0;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			//
			//
			/// sensitivity
			key = 14 + "," + 3;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			key = 14 + "," + 4;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			key = 14 + "," + 17;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 14 + "," + 27;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			key = 25 + "," + 24;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			key = 8 + "," + 11;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			//
			key = 17 + "," + 5;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
			//
			key = 8 + "," + 18;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


		}

		if (inp.getInstanceName().equals("p2.2.a")) {
			String key = 19 + "," + 18;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 18 + "," + 15;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 14 + "," + 8;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 0 + "," + 6;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 4 + "," + 3;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
		}

		if (inp.getInstanceName().equals("p3.2.a")) {
			String key = 20 + "," + 16;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 16 + "," + 15;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 11 + "," + 10;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 10 + "," + 28;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 10 + "," + 18;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 16 + "," + 4;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
		}

		if (inp.getInstanceName().equals("p4.2.a")) {
			String key = 33 + "," + 35;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 36 + "," + 24;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 76 + "," + 97;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 11 + "," + 92;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 58 + "," + 54;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 57 + "," + 72;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 77 + "," + 27;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 83 + "," + 56;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 56 + "," + 5;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 5 + "," + 46;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
		}

		if (inp.getInstanceName().equals("p5.2.a")) {
			int range1 = 8;
			int range2 = 16;
			int range3 = 24;
			int range4 = 32;
			int range5 = 40;
			int range6 = 48;
			int range7 = 56;
			int range8 = 64;
			for (int ii = 1; ii <= range1; ii++) {
				for (int ij = ii + 1; ij <= range1; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range1 + 1; ii <= range2; ii++) {
				for (int ij = ii + 1; ij <= range2; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range2 + 1; ii <= range3; ii++) {
				for (int ij = ii + 1; ij <= range3; ij++) {

					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range3 + 1; ii <= range4; ii++) {
				for (int ij = ii + 1; ij <= range4; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range4 + 1; ii <= range5; ii++) {
				for (int ij = ii + 1; ij <= range5; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range5 + 1; ii <= range6; ii++) {
				for (int ij = ii + 1; ij <= range6; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range6 + 1; ii <= range7; ii++) {
				for (int ij = ii + 1; ij <= range7; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

			for (int ii = range7 + 1; ii <= range8; ii++) {
				for (int ij = ii + 1; ij <= range8; ij++) {
					String key = ii + "," + ij;
					Edge e3 = this.directoryAerialEdgesNetwork.get(key);
					directoryRoadEdgesNetwork.put(e3.getKey(), e3);
					directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
				}
			}

		}

		if (inp.getInstanceName().equals("p6.2.a")) {
			String key = 5 + "," + 8;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 8 + "," + 12;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 12 + "," + 17;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 17 + "," + 23;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 23 + "," + 30;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 30 + "," + 37;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 37 + "," + 43;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 27 + "," + 34;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 34 + "," + 41;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 41 + "," + 47;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 47 + "," + 52;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 52 + "," + 56;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 56 + "," + 59;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 59 + "," + 61;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
		}

		if (inp.getInstanceName().equals("p7.2.a")) {
			String key = 35 + "," + 78;
			Edge e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 35 + "," + 12;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 45 + "," + 44;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 32 + "," + 45;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 87 + "," + 86;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 67 + "," + 37;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 78 + "," + 77;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


			//

			key = 76 + "," + 2;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 46 + "," + 2;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 70 + "," + 46;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 72 + "," + 34;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 87 + "," + 44;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 73 + "," + 79;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 67 + "," + 12;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 78 + "," + 22;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());

			key = 81 + "," + 29;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


			key = 30 + "," + 29;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


			key = 88 + "," + 3;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());


			key = 80 + "," + 31;
			e3 = this.directoryAerialEdgesNetwork.get(key);
			directoryRoadEdgesNetwork.put(e3.getKey(), e3);
			directoryRoadEdgesNetwork.put(e3.getInverseEdge().getKey(), e3.getInverseEdge());
		}
				//
		edgeRoadConnections.clear(); // creating road connections
		for (Edge e : directoryRoadEdgesNetwork.values()) {
			this.edgeRoadConnections.add(e);
		}

		//new DrawingNetwork(edgeRoadConnections,inp);
		System.out.println(edgeRoadConnections.size());
	}

	private boolean setAdditionalConnections(Edge e2, Test inp) {
		boolean merge = true;
		if (e2.getOrigin().getId() != e2.getEnd().getId()) {
			double maxX = Math.max(e2.getOrigin().getX(), e2.getEnd().getX());
			double minX = Math.min(e2.getOrigin().getX(), e2.getEnd().getX());
			double maxY = Math.max(e2.getOrigin().getY(), e2.getEnd().getY());
			double minY = Math.min(e2.getOrigin().getY(), e2.getEnd().getY());
			//
			for (Edge e : directoryRoadEdgesNetwork.values()) {
				Node i = e.getOrigin();
				Node j = e.getEnd();
				LinkedList<Node> nodesSortX = new LinkedList<Node>();
				LinkedList<Node> nodesSortY = new LinkedList<Node>();
				nodesSortX.add(i);
				nodesSortX.add(j);
				nodesSortX.sort(Node.positionX);
				nodesSortY.add(i);
				nodesSortY.add(j);
				nodesSortY.sort(Node.positionY);
				if (inp.getInstanceName().equals("p1.2.b")) {

					if (minX < nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (minY > nodesSortY.getFirst().getX() && maxY < nodesSortY.getLast().getX()) {
							merge = false;
						}
					}

					if (minX > nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (minY < nodesSortY.getFirst().getX() && maxY > nodesSortY.getLast().getX()) {
							merge = false;
						}
					}

					if (minX < nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (minY < nodesSortY.getFirst().getX() && maxY > nodesSortY.getLast().getX()) {
							merge = false;
						}
					}

					if (minX > nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (minY > nodesSortY.getFirst().getX() && maxY < nodesSortY.getLast().getX()) {
							merge = false;
						}
					}

					if (minX < nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (maxY > nodesSortY.getLast().getY() && minY < nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}

					if (minX > nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (maxY > nodesSortY.getLast().getY() && minY < nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}
					if (minX < nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (maxY > nodesSortY.getLast().getY() && minY > nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}
					if (minX > nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (maxY < nodesSortY.getLast().getY() && minY < nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}
					if (minX > nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (maxY > nodesSortY.getLast().getY() && minY < nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}
					if (minX < nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (minY < nodesSortY.getFirst().getY() && maxY < nodesSortY.getLast().getY()) {
							merge = false;
						}
					}

					if (minX < nodesSortX.getFirst().getX() && minX < nodesSortX.getLast().getX()
							&& maxX > nodesSortX.getFirst().getX() & maxX < nodesSortX.getLast().getX()) {
						if (minY < nodesSortX.getLast().getY() && minY < nodesSortY.getLast().getY()
								&& minY < nodesSortY.getFirst().getY() && maxY > nodesSortX.getLast().getY()
								&& maxY > nodesSortY.getLast().getY() && maxY > nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}

					if (minX > nodesSortX.getFirst().getX() && minX < nodesSortX.getLast().getX()
							&& maxX > nodesSortX.getFirst().getX() & maxX < nodesSortX.getLast().getX()) {
						if (minY < nodesSortX.getFirst().getY() && minY < nodesSortY.getLast().getY()
								&& maxY > nodesSortY.getFirst().getY() && maxY < nodesSortX.getLast().getY()) {
							merge = false;
						}
					}

					if (minX > nodesSortX.getFirst().getX() && maxX > nodesSortX.getLast().getX()) {
						if (minY > nodesSortY.getFirst().getY() && maxY > nodesSortY.getLast().getY()) {
							merge = false;
						}
					}
					if (minX > nodesSortX.getFirst().getX() && minX < nodesSortX.getLast().getX()
							&& maxX > nodesSortX.getFirst().getX() & maxX < nodesSortX.getLast().getX()) {
						if (minY <= nodesSortX.getFirst().getY() && minY < nodesSortY.getLast().getY()
								&& maxY > nodesSortY.getFirst().getY() && maxY < nodesSortX.getLast().getY()) {
							merge = false;
						}
					}
					if (minX >= nodesSortX.getFirst().getX() && maxX < nodesSortX.getLast().getX()) {
						if (maxY > nodesSortY.getLast().getY() && minY < nodesSortY.getFirst().getY()) {
							merge = false;
						}
					}

					if (minX == nodesSortX.getFirst().getX() && maxX == nodesSortX.getLast().getX()) {
						if (maxY == nodesSortX.getLast().getY() && minY == nodesSortX.getFirst().getY()) {
							merge = false;
						}
					}

					if (minX == nodesSortX.getFirst().getX() && maxX == nodesSortX.getLast().getX()) {
						if (maxY == nodesSortX.getFirst().getY() && minY == nodesSortX.getLast().getY()) {
							merge = false;
						}
					}

				}

				if (!merge) {
					break;
				}
			}
		}
		return merge;
	}

	private Edge[] spanningTree(LinkedList<Edge> copyAerialNetwork) {
		copyAerialNetwork.sort(Edge.minDistance);

		Edge result[] = new Edge[this.nodes.length]; // Tnis will store the resultant MST

		Subset subsets[] = new Subset[this.nodes.length];
		for (int i = 0; i < this.nodes.length; ++i) {
			subsets[i] = new Subset();
			subsets[i].parent = i;
			subsets[i].rank = 0;
		}

		int e = 0; // An index variable, used for result[]
		int i = 0; // An index variable, used for nodes[]

		while (e < this.nodes.length - 1) {
			// Step 2: Pick the smallest edge. And increment
			// the index for next iteration
			Edge next_edge = copyAerialNetwork.get(i++);

			int x = find(subsets, next_edge.getOrigin().getId());
			int y = find(subsets, next_edge.getEnd().getId());

			// If including this edge does't cause cycle,
			// include it in result and increment the index
			// of result for next edge
			if (x != y) {
				result[e++] = next_edge;
				Union(subsets, x, y);
			}
			// Else discard the next_edge
		}

		for (i = 0; i < e; ++i) {
			System.out.println(result[i].getOrigin().getId() + " -- " + result[i].getEnd().getId() + " == "
					+ result[i].getDistance());
		}

		return result;
	}

	public int find(Subset subsets[], int i) {
		// find root and make root as parent of i (path compression)
		if (subsets[i].parent != i)
			subsets[i].parent = find(subsets, subsets[i].parent);

		return subsets[i].parent;
	}

	void Union(Subset subsets[], int x, int y) {
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		// Attach smaller rank tree under root of high rank tree
		// (Union by Rank)
		if (subsets[xroot].rank < subsets[yroot].rank)
			subsets[xroot].parent = yroot;
		else if (subsets[xroot].rank > subsets[yroot].rank)
			subsets[yroot].parent = xroot;

		// If ranks are same, then make one as root and increment
		// its rank by one
		else {
			subsets[yroot].parent = xroot;
			subsets[xroot].rank++;
		}
	}

	private void generationRoadAerialNetwork() {
		// para cada road connection existe un edge en la red aerea y una
		int nNodes = nodes.length;
		for (int i = 0; i < nNodes; i++) // node 0 is the depot
		{
			for (int j = i + 1; j < nNodes; j++) {
				Node iNode = nodes[i];
				Node jNode = nodes[j];
				Edge ijEdge = new Edge(iNode, jNode);
				ijEdge.setTime(ijEdge.calcTime());
				ijEdge.setDistance(ijEdge.calcDistance());
				Edge jiEdge = new Edge(jNode, iNode);
				jiEdge.setTime(jiEdge.calcTime());
				jiEdge.setDistance(jiEdge.calcDistance());
				ijEdge.setInverse(jiEdge);// Set inverse edges
				jiEdge.setInverse(ijEdge);
				edgeAerialNetwork.add(ijEdge);
				edgeAerialNetwork.add(jiEdge);
			}
		}

		for (Edge e : edgeAerialNetwork) {
			directoryAerialEdgesNetwork.put(e.getKey(), e);
			directoryAerialEdgesNetwork.put(e.getInverseEdge().getKey(), e.getInverseEdge());
		}
	}

	private void genarateRoads(Test aTest) {
		Map<String, Edge> generatedRoadInflectoin = new HashMap<String, Edge>();
		for (Edge e : edgeRoadConnections) {
			if (e.getOrigin().getX() <= e.getEnd().getX()) { // taking just the diagonal matrix
				if(!generatedRoadInflectoin.containsKey(e.getKey())) {
					generatedRoadInflectoin.put(e.getKey(), e);// storing edges evaluated
					generatedRoadInflectoin.put(e.getInverseEdge().getKey(), e.getInverseEdge());
					LinkedList<Node> nodesEdge = new LinkedList<Node>();
					LinkedList<Node> x = new LinkedList<Node>();
					LinkedList<Edge> inflexionsEdge = new LinkedList<Edge>();
					LinkedList<Edge> inflexionsEdgeReverse = new LinkedList<Edge>();
					if (aTest.getInstanceName() == "manati") {
						nodesEdge.add(e.getOrigin());
						x = Manti_Instance.getIntermedianNodes(e.getOrigin(), e.getEnd());
						for (Node n : x) {
							nodesEdge.add(n);
						}
						nodesEdge.add(e.getEnd());
						for (int i = 0; i < nodesEdge.size() - 1; i++) {
							inflexionsEdge.add(new Edge(nodesEdge.get(i), nodesEdge.get(i + 1)));
						}
					} else {
						nodesEdge.add(e.getOrigin());
						nodesEdge.add(e.getEnd());
						nodesEdge.sort(Node.positionY);
						float maxX = Math.max(nodesEdge.getFirst().getX(), nodesEdge.getLast().getX());// se calcula el
						// square
						float minX = Math.min(nodesEdge.getFirst().getX(), nodesEdge.getLast().getX());
						float maxY = Math.max(nodesEdge.getFirst().getY(), nodesEdge.getLast().getY());
						float minY = Math.min(nodesEdge.getFirst().getY(), nodesEdge.getLast().getY());// dividido entre 3
						//
						// break points (3 additional nodes)
						float deltaX = (maxX - minX) / 3;
						float deltaY = (maxY - minY) / 3;
						float totalDeltaX=(maxX - minX);
						float totalDeltaY=(maxY - minY);
						float m = (nodesEdge.getLast().getY() - nodesEdge.getFirst().getY())
								/ (nodesEdge.getLast().getX() - nodesEdge.getFirst().getX());// se identifica si la
						// negative slope
						if(m!=Float.POSITIVE_INFINITY && m!=Float.NEGATIVE_INFINITY) { // the edge has a slope
							computeCoordinatesInflection(inflexionsEdge,m,deltaX,deltaY,e);
						}
						//
						else { // if m is different to 0
							float randomX1 = e.getOrigin().getX();
							float randomX2 = e.getEnd().getX();
							float randomY1 = e.getOrigin().getY();
							float randomY2 = e.getEnd().getY();
							Node randomNode1 = new Node(0, randomX1, randomY1, 0);
							Node randomNode2 = new Node(1, randomX2, randomY2, 0);
							if (e.getOrigin().getX() == e.getEnd().getX()) {
								nodesEdge.sort(Node.positionX); // decreasing order
								randomX1 = nodesEdge.getFirst().getX()+ totalDeltaY*0.05f;;
								randomX2 = nodesEdge.getLast().getX()- totalDeltaY*0.05f;
								nodesEdge.sort(Node.positionY); // decreasing order
								randomY1 = nodesEdge.getFirst().getY()+ deltaY;
								randomY2 = nodesEdge.getLast().getY()- deltaY;
								//							randomY1 = nodesEdge.getFirst().getY()+ totalDeltaY*0.15f;
								//							randomY2 = nodesEdge.getLast().getY()- totalDeltaY*0.15f;
							}
							if (e.getOrigin().getY() == e.getEnd().getY()) {
								nodesEdge.sort(Node.positionY); // decreasing order
								randomY1 = nodesEdge.getFirst().getY()+ totalDeltaX*0.1f;
								randomY2 = nodesEdge.getLast().getY()- totalDeltaX*0.1f;
								nodesEdge.sort(Node.positionX); // decreasing order
								randomX1 = nodesEdge.getFirst().getX()+ deltaX;
								randomX2 = nodesEdge.getLast().getX()- deltaX;
							}
							randomNode1 = new Node(0, randomX1, randomY1, 0);
							randomNode2 = new Node(1, randomX2, randomY2, 0);
							inflexionsEdge.add(new Edge(e.getOrigin(), randomNode1));
							inflexionsEdge.add(new Edge(randomNode1, randomNode2));
							inflexionsEdge.add(new Edge(randomNode2, e.getEnd()));
						}
					}

					double roadDistance = 0; // Computing the distance for each road connection
					double roadTime = 0;
					for (Edge ex : inflexionsEdge) {
						ex.settypeEdge(1);
						ex.setTime(ex.calcTime());
						ex.setDistance(ex.calcDistance());
						Edge jiEdge = new Edge(ex.getEnd(), ex.getOrigin());
						jiEdge.setTime(jiEdge.calcTime());
						jiEdge.setDistance(jiEdge.calcDistance());
						jiEdge.settypeEdge(1);
						roadDistance += ex.getDistance();
						roadTime += ex.getTime();
						// Set inverse edges
						ex.setInverse(jiEdge);
						jiEdge.setInverse(ex);
					}
					if (Math.round(roadDistance) == Math.round(e.getDistance())) { // Eclipse round the values
						roadDistance = 1.1 * e.getDistance();
						roadTime = 1.1 * e.getTime();
					}
					e.setInflextionNodes(inflexionsEdge);
					e.setDistanceRoad(roadDistance);
					e.setTimeRoad(roadTime);

					for (int i = inflexionsEdge.size(); i > 0; i--) {
						inflexionsEdgeReverse.add(inflexionsEdge.get(i-1).getInverseEdge());
					}
					e.getInverseEdge().setInflextionNodes(inflexionsEdgeReverse);
					e.getInverseEdge().setDistanceRoad(roadDistance); // setting distance and time
					e.getInverseEdge().setTimeRoad(roadTime);
					for (Edge ex : e.getInflexionEdge()) {
						edgeRoadNetwork.add(ex);
					}
					for (Edge ex : e.getInverseEdge().getInflexionEdge()) {
						edgeRoadNetwork.add(ex);

					}
				}
			}
		}

		for (Edge e : edgeRoadConnections) {
			if(e.getInflexionEdge()==null) {
				System.out.println("sotp");
			}
			if (e.getInflexionEdge()==null) {
				e.setInflextionNodes(e.getInverseEdge().getInflexionEdge());
				e.setDistanceRoad(e.getInverseEdge().getDistanceRoad());
				e.setTimeRoad(e.getInverseEdge().getTimeRoad());

			}}


		directoryRoadEdgesNetwork.clear();
		for (Edge e : edgeRoadConnections) { // creating the directory of edges (individual edges)
			directoryRoadEdgesNetwork.put(e.getKey(), e);
		}

		for (Edge e : edgeRoadConnections) { // setting the information of inverse edges in the network
			e.setInverse(directoryRoadEdgesNetwork.get(e.getInverseEdge().getKey()));
		}
		for (Edge e : directoryRoadEdgesNetwork.values()) { // setting the information of inverse edges in the network
			e.setInverse(directoryRoadEdgesNetwork.get(e.getInverseEdge().getKey()));
		}

	}

	private void computeCoordinatesInflection(LinkedList<Edge> inflexionsEdge, float m, float deltaX, float deltaY, Edge e) {
		float totalDeltaX=deltaX*3; // the edge is split in 3 subsections
		float totalDeltaY=deltaY*3; // the edge is spit in 3 subsections
		float small = 0;
		float firstCoordinateX = 0f;
		float secondCoordinateX = 0f;
		float firstCoordinateY = 0f;
		float secondCoordinateY = 0f;
		// coordinates of new nodes
		float randomX1 = e.getOrigin().getX();
		float randomX2 = e.getEnd().getX();
		float randomY1 = e.getOrigin().getY();
		float randomY2 = e.getEnd().getY();
		small = -m * e.getOrigin().getX() + e.getOrigin().getY();
		firstCoordinateX = e.getOrigin().getX() + deltaX;
		secondCoordinateX = firstCoordinateX + deltaX;
		firstCoordinateY = firstCoordinateX * m + small;
		secondCoordinateY = secondCoordinateX * m + small;
		randomX1 = firstCoordinateX + totalDeltaX*0.15f;
		randomX2 = secondCoordinateX - totalDeltaX*0.15f;
		randomY1 = firstCoordinateY + totalDeltaY*0.15f;
		randomY2 = secondCoordinateY - totalDeltaY*0.15f;
		Node randomNode1 = new Node(0, randomX1, randomY1, 0);
		Node randomNode2 = new Node(1, randomX2, randomY2, 0);
		inflexionsEdge.add(new Edge(e.getOrigin(), randomNode1));
		inflexionsEdge.add(new Edge(randomNode1, randomNode2));
		inflexionsEdge.add(new Edge(randomNode2, e.getEnd()));

	}

	/*** GETTERS ***/
	public Node[] getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdgeConnections() {
		return edgeRoadConnections;
	}

	public ArrayList<Edge> getedgeRoadNetwork() {
		return edgeRoadNetwork;
	}

	public ArrayList<Edge> getEdgeAerialNetwork() {
		return edgeAerialNetwork;
	}

	public Map<Integer, Node> getdirectoryNode() {
		return directoryNode;
	}

	public Map<Integer, Node> getVictimList() {
		return VictimList;
	}

}

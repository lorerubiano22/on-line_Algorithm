
package alg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BackandForwardMovement {
	// solution
	private final Route auxRoute = new Route();
	private final Solution back_Sol= new Solution();
	private final Test aTest;
	private final Disaster Event;
	private final Map<Integer, Node> recheablevictims= new HashMap<>();
	// update information
	private final Map<String, Edge> revealedDisruptedRoadConnections; // revealed disrupted connections
	private final ArrayList<Edge> edgeRoadConnection;
	private final Map<String, Edge> directoryAerialEdges; // It contains all edges of the network
	private final Map<String, Edge> directoryRoadEdges; // It contains all edges of the network
	private final Map<Integer, Node> directoryNodes;// It contains all nodes of the network
	private final ArrayList<Node> nodeList; // list of nodes
	private final Map<Integer, Node> VictimList; // list of all victim nodes


	// local information
	private HashMap<String, Edge> revealedDisruptedEdges = new HashMap<>(); ; // list of revealed disrupted edges
	private final HashMap<String, Edge> connectedEdgestoRevealedRoadNetwork= new HashMap<>(); // storage the edges that belong to a route to
	private final Map<Integer, Node> connectedNodestoRevealedRoadNetwork= new HashMap<>();
	private final HashMap<Integer, Node> visitedVictims = new HashMap<>(); // list of visited victims
	private final HashMap<Integer, Node> checkedAccesibiliyVictims = new HashMap<>();
	private final HashMap<Integer, Node> connectedNodesNotoVisit = new HashMap<>(); // consider the nodes that are connected
	private final HashMap<Integer, Node> NodesNotoVisit = new HashMap<>(); // Nodes without adjacent edges to be explored
	private final HashMap<String, Edge> EdgesNotoVisit = new HashMap<>(); // consider the nodes that are connected and their
	private final HashMap<String, Edge> visitedRoadConnections = new HashMap<>(); // it considers the ground route
	private final HashMap<Integer, Node> visitedNodeConnections = new HashMap<>(); // it considers the ground route

	public BackandForwardMovement(Test aTest, Disaster Event, UpdateRoadInformation reveledNetwork, Inputs inputs) {
		// Inputs
		this.aTest = aTest;
		this.Event = Event;
		// Road information - online
		revealedDisruptedRoadConnections=reveledNetwork.getRevealedDisruptedRoadConnections();
		edgeRoadConnection = reveledNetwork.getedgeRoadConnection();
		directoryAerialEdges=reveledNetwork.getdirectoryAerialConnections();
		directoryRoadEdges=reveledNetwork.getdirectoryroadConnections();
		directoryNodes = reveledNetwork.getdirectoryNodes();// It contains all nodes of the network
		nodeList = reveledNetwork.getNodeList(); // list of nodes
		VictimList = reveledNetwork.getVictimList(); // list of nodes

		backforwardMovement(reveledNetwork, inputs);
		back_Sol.updatingSolutionAttributes();
		udateStateVictims(back_Sol);
	}

	private void udateStateVictims(Solution jump_Sol) {
		for (Node v : this.VictimList.values()) {
			if (this.connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
				recheablevictims.put(v.getId(), v);
			}}
		jump_Sol.setrecheablevictims(recheablevictims);
	}

	private void  backforwardMovement(UpdateRoadInformation reveledNetwork, Inputs inputs) {

		Node currentPostion = nodeList.get(0);
		while (checkedAccesibiliyVictims.size() < VictimList.size()
				&& !this.revealedDisruptedRoadConnections.isEmpty()) {
			Edge edgeToinsert = selectBestEdge(currentPostion, aTest.getOptcriterion(), auxRoute, reveledNetwork,
					inputs);
			if(edgeToinsert.getOrigin().getId()==10 && edgeToinsert.getEnd().getId()==11) {
				System.out.println("Stop");
			}
			if (edgeToinsert != null) {
				auxRoute.getEdges().add(edgeToinsert);
				// check if it is a disrupted
				if (disruptedEdge2(edgeToinsert)) { // si esta disrupto -> deberia regresar al nodo conectado más
					currentPostion = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd();
					updateDisruption(edgeToinsert, auxRoute, reveledNetwork, inputs);

					if (!checkedAccesibiliyVictims.equals(VictimList)) {
						redirectRoute(edgeToinsert, auxRoute, reveledNetwork, inputs);
					} else {
						String key = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() + "," + 0;
						Edge connectionToOrigin = directoryAerialEdges.get(key);
						auxRoute.getEdges().add(connectionToOrigin);
					}
				}
			}
			updatingReveleadedNetwork(edgeToinsert, auxRoute);
			currentPostion = this.directoryNodes
					.get(auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId());
			if (checkedAccesibiliyVictims.size() == VictimList.size()
					&& auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() != 0) {
				String key = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() + "," + 0;
				Edge connectionToOrigin = directoryAerialEdges.get(key);
				auxRoute.getEdges().add(connectionToOrigin);
			}
		}
		auxRoute.calcTime();
		auxRoute.calcDistance();
		back_Sol.getRoutes().add(auxRoute);

	}

	private void updateDisruption(Edge edgeToinsert, Route auxRoute, UpdateRoadInformation reveledNetwork,
			Inputs inputs) {
		updatingReveleadedNetwork(edgeToinsert, auxRoute); // it sends the reveledNetwork to UpdateRoadInformation
		ArrayList<Edge> aux= new ArrayList<>();
		for(Edge e:reveledNetwork.getRevealedDisruptedRoadConnections().values()) {
			aux.add(e);
		}

		if(!aux.isEmpty()) {
			new DrawingNetwork(aux,aTest);}
		new Assessment(reveledNetwork, inputs, this.aTest);

		String Disrup_file= new String(aTest.getInstanceName()+"AfterDisrution_"+edgeToinsert.getKey()+"_Road_Network_Distances"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
		writeLinkedList2(Disrup_file, this.revealedDisruptedRoadConnections , false);
		aux.clear();
		for(Edge e:reveledNetwork.getRevealedDisruptedRoadConnections().values()) {
			aux.add(e);
		}
		if(!aux.isEmpty()) {
			new DrawingNetwork(aux,aTest);}
	}

	private static void writeLinkedList2(String tV_file, Map<String, Edge> revealedDisruptedRoadConnections2, boolean b) {

		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();// It contains all nodes of the network
			bw.println("Road_Connections");
			for(Edge e:revealedDisruptedRoadConnections2.values()) {
				//for(Edge e:edgeRoadConnection ) {
				nodes.put(e.getOrigin().getId(), e.getOrigin());
				nodes.put(e.getEnd().getId(), e.getEnd());
				int i=0;
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
					bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad()+ "_Connectivity_"+e.getConnectivity()+"_weight_"+e.getWeight());
					//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Distance_Euclidean_"+e.getDistance());
				}}

			bw.flush();
		}
		catch (IOException e) {
			//why does the catch need its own curly?
		}
		//writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,false);

	}

	private void updatingReveleadedNetwork(Edge disruptedEdge, Route r) {
		// (1) revealedDisruptedEdges.add(disruptedEdge); /// La lista de ejes disruptos
		// se la para al update information
System.out.println(this.auxRoute.toString());
		for (Edge e : r.getEdges()) { // (2) Actualiza la red revelada
			if (visitedRoadConnections.containsKey(e.getKey())) {

				if (this.disruptedEdge2(e)) {
					revealedDisruptedRoadConnections.remove(e.getKey());
					revealedDisruptedRoadConnections.remove(e.getInverseEdge().getKey());
				}
			}
		}

		for (Edge e : r.getEdges()) { // Los elementos conectados en la red
			if (!revealedDisruptedEdges.containsKey(e.getKey())) {// No esta dentro de la lista de los elementos
				// disruptos
				if (revealedDisruptedRoadConnections.containsKey(e.getKey())) {// Si pertenece a la red de carreteras
					if (e.getOrigin().getId() == 0 || e.getEnd().getId() == 0) {
						this.connectedEdgestoRevealedRoadNetwork.put(e.getKey(), e); // edges
						this.connectedNodestoRevealedRoadNetwork.put(e.getOrigin().getId(), e.getOrigin()); // nodes
						this.connectedNodestoRevealedRoadNetwork.put(e.getEnd().getId(), e.getEnd()); // nodes
					} else {
						if (connectedNodestoRevealedRoadNetwork.containsKey(e.getOrigin().getId())
								|| connectedNodestoRevealedRoadNetwork.containsKey(e.getEnd().getId())) {
							this.connectedEdgestoRevealedRoadNetwork.put(e.getKey(), e); // edges
							this.connectedNodestoRevealedRoadNetwork.put(e.getOrigin().getId(), e.getOrigin()); // nodes
							this.connectedNodestoRevealedRoadNetwork.put(e.getEnd().getId(), e.getEnd()); // nodes
						}

					}
				}

			}
		}

		// inverseroute
		for (int i = r.getEdges().size() - 1; i >= 0; i--) {
			Edge e = r.getEdges().get(i);
			if (!revealedDisruptedEdges.containsKey(e.getKey())) {// No esta dentro de la lista de los elementos
				// disruptos
				if (revealedDisruptedRoadConnections.containsKey(e.getKey())) {// Si pertenece a la red de carreteras
					if (e.getOrigin().getId() == 0 || e.getEnd().getId() == 0) {
						this.connectedEdgestoRevealedRoadNetwork.put(e.getKey(), e); // edges
						this.connectedNodestoRevealedRoadNetwork.put(e.getOrigin().getId(), e.getOrigin()); // nodes
						this.connectedNodestoRevealedRoadNetwork.put(e.getEnd().getId(), e.getEnd()); // nodes
					} else {
						if (connectedNodestoRevealedRoadNetwork.containsKey(e.getOrigin().getId())
								|| connectedNodestoRevealedRoadNetwork.containsKey(e.getEnd().getId())) {
							this.connectedEdgestoRevealedRoadNetwork.put(e.getKey(), e); // edges
							this.connectedNodestoRevealedRoadNetwork.put(e.getOrigin().getId(), e.getOrigin()); // nodes
							this.connectedNodestoRevealedRoadNetwork.put(e.getEnd().getId(), e.getEnd()); // nodes
						}

					}
				}

			}
		}
		HashMap<Integer, Node> copyRoadConnections = new HashMap<Integer, Node>();
		for (Edge e : revealedDisruptedRoadConnections.values()) {
			copyRoadConnections.put(e.getOrigin().getId(), e.getOrigin());
			copyRoadConnections.put(e.getEnd().getId(), e.getEnd());
		}
		for (Node n : connectedNodestoRevealedRoadNetwork.values()) {
			HashMap<String, Edge> check = new HashMap<String, Edge>();
			HashMap<String, Edge> copyAdj = new HashMap<String, Edge>();
			for (Edge e : n.getAdjEdgesList()) {
				if (this.visitedRoadConnections.containsKey(e.getKey())) { // exploring
					if (e.getConnectivity() != 0) {
						check.put(e.getKey(), e);
					}
				}
				if (e.getConnectivity() != 0) {
					copyAdj.put(e.getKey(), e);
				}
			}
			if (check.size() == copyAdj.size()) {
				this.connectedNodesNotoVisit.put(n.getId(), n);
			}
		}
		elementsNotToVisit(r); // Nodes not to visit
		visitedVictims(r);
	}

	private void elementsNotToVisit(Route r) {
		for (Edge e : revealedDisruptedRoadConnections.values()) {
			HashMap<String, Edge> check = new HashMap<String, Edge>();
			HashMap<String, Edge> adjCopy = new HashMap<String, Edge>(); // adjacent edges with priority
			// Origin Node
			for (Edge n : e.getOrigin().getAdjEdgesList()) {

				if (this.visitedRoadConnections.containsKey(n.getKey())) { // exploring
					if (n.getConnectivity() != 0) {
						check.put(n.getKey(), n);
					}
					// visitedVictims.put(n.getId(),n);
				}
				if (n.getConnectivity() != 0) {
					adjCopy.put(n.getKey(), n);
				}
			}
			if (check.size() == adjCopy.size()) {
				if (e.getOrigin().getId() != 0) {
					this.NodesNotoVisit.put(e.getOrigin().getId(), e.getOrigin());
				}
			}

			check = new HashMap<String, Edge>();

			adjCopy = new HashMap<String, Edge>(); // adjacent edges with priority

			// End Node
			for (Edge n : e.getEnd().getAdjEdgesList()) {
				if (this.visitedRoadConnections.containsKey(n.getKey())) { // exploring
					if (n.getConnectivity() != 0) {
						check.put(n.getKey(), n);
					}
				}
				if (n.getConnectivity() != 0) {
					adjCopy.put(n.getKey(), n);
				}
			}
			if (check.size() == adjCopy.size()) {
				this.NodesNotoVisit.put(e.getEnd().getId(), e.getEnd());
			}

		}

		HashMap<String, Edge> check = new HashMap<String, Edge>();
		for (Edge e : r.getEdges()) {
			check.put(e.getKey(), e);
			if (check.containsKey(e.getInverseEdge().getKey())) {
				this.EdgesNotoVisit.put(e.getKey(), e);
				this.EdgesNotoVisit.put(e.getInverseEdge().getKey(), e.getInverseEdge());
			}
		}
	}

	private void visitedVictims(Route r) {
		for (Node n : VictimList.values()) {
			HashMap<String, Edge> check = new HashMap<String, Edge>();
			HashMap<String, Edge> copyAdjEdges = new HashMap<String, Edge>();
			copyAdjEdges = new HashMap<String, Edge>();
			for (Edge e : n.getAdjEdgesList()) {
				if (this.visitedRoadConnections.containsKey(e.getKey())) { // exploring
					check.put(e.getKey(), e);
				}
				if (e.connectivity != 0) {
					copyAdjEdges.put(e.getKey(), e);
				}
			}
			if (check.size() >= copyAdjEdges.size()) { // it is greater because the network is getting smaller afte each
				// detected disruption
				visitedVictims.put(n.getId(), n);
			}
		}
		for (Edge e : r.getEdges()) {
			if (this.VictimList.containsKey(e.getEnd().getId())
					&& this.connectedEdgestoRevealedRoadNetwork.containsKey(e.getKey())) {
				visitedVictims.put(e.getEnd().getId(), e.getEnd());
			}
		}

		for (Node victim : VictimList.values()) {
			if (this.connectedNodestoRevealedRoadNetwork.containsKey(victim.getId())
					&& this.directoryNodes.containsKey(victim.getId())) {
				checkedAccesibiliyVictims.put(victim.getId(), victim);
			}
			if (!this.connectedNodestoRevealedRoadNetwork.containsKey(victim.getId())
					&& !this.directoryNodes.containsKey(victim.getId())) {
				checkedAccesibiliyVictims.put(victim.getId(), victim);
			}

		}

		// para las victimas que no han sido visitadas

		ArrayList<Edge> edgeList = new ArrayList<>();
		for (Edge e : revealedDisruptedRoadConnections.values()) {
			Edge newEdge = new Edge(e);
			edgeList.add(newEdge);
		}

		for (Node victim : VictimList.values()) {
			if (!this.checkedAccesibiliyVictims.containsKey(victim.getId())) {
				FindingPath tree = new FindingPath();
				boolean x = tree.spanningTree(edgeList, 0, victim.getId());

				if (!x) {
					this.checkedAccesibiliyVictims.put(victim.getId(), victim);
				}

			}

		}
	}

	private void redirectRoute(Edge disruptedEdge, Route r, UpdateRoadInformation reveledNetwork, Inputs inputs) {
		Edge EdgetoRedirectRoute;
		if (disruptedEdge2(disruptedEdge)) {
			EdgetoRedirectRoute = redirectRoute(r);
			r.getEdges().add(EdgetoRedirectRoute);
			if (disruptedEdge2(EdgetoRedirectRoute) && !this.connectedNodestoRevealedRoadNetwork.containsKey(0)) { // si
				exploreAdjacentEdgesToDepot(r);
				updateDisruption(EdgetoRedirectRoute, r, reveledNetwork, inputs);

			}
			else {
				while(disruptedEdge2(EdgetoRedirectRoute) && !checkedAccesibiliyVictims.equals(VictimList)) {
					updateDisruption(EdgetoRedirectRoute, r, reveledNetwork, inputs);
					if(!checkedAccesibiliyVictims.equals(VictimList)) {
						EdgetoRedirectRoute = redirectRoute(r);
						r.getEdges().add(EdgetoRedirectRoute);}
					updatingReveleadedNetwork(EdgetoRedirectRoute, auxRoute); // it sends the reveledNetwork to UpdateRoadInformation
				}
			}
			this.updatingReveleadedNetwork(disruptedEdge, r);
			updateVisitedNetwork(r);
		}

	}

	private void exploreAdjacentEdgesToDepot(Route r) { // Explore all adjacent edges
		LinkedList<Edge> adjEdges = new LinkedList<Edge>();
		if(r.getEdges().get(r.getEdges().size() - 1).getEnd().getId()!=0) {
			String key=r.getEdges().get(r.getEdges().size()-1).getEnd().getId()+","+0;
			Edge aux= this.directoryAerialEdges.get(key);
			r.getEdges().add(aux);
		}
		// If current position is the depot node
		if(r.getEdges().get(r.getEdges().size() - 1).getEnd().getId()==0) {
			Node currentPostion = this.directoryNodes.get(r.getEdges().get(r.getEdges().size() - 1).getEnd().getId());
			sortEdges(currentPostion.getAdjEdgesList(), this.aTest.getOptcriterion());
			for (Edge adj : currentPostion.getAdjEdgesList()) { // selection of no visited adjacent edges
				if (!visitedRoadConnections.containsKey(adj.getKey())
						&& !visitedRoadConnections.containsKey(adj.getInverseEdge().getKey())) {
					adjEdges.add(adj);
				}
			}
			LinkedList<Edge> edgesToInsert = new LinkedList<Edge>();
			if (!adjEdges.isEmpty()) {
				for (Edge adj : adjEdges) { // selection of edges to insert
					if (this.disruptedEdge(adj)) {
						edgesToInsert.add(adj);
						edgesToInsert.add(adj.getInverseEdge());
					} else {
						edgesToInsert.add(adj);
						break;
					}
				}
			}

			if(!edgesToInsert.isEmpty()) {
				for (Edge adj : edgesToInsert) {
					r.getEdges().add(adj);
				}
			}
		}}


	private boolean disruptedEdge(Edge edgeToinsert) {
		boolean disruption = false;
		if (!Event.getDisruptedRoadConnections().contains(edgeToinsert)) {
			if (Event.getDisruptedEdges().containsKey(edgeToinsert.getKey())) {
				revealedDisruptedEdges.put(edgeToinsert.getInverseEdge().getKey(), edgeToinsert.getInverseEdge()); /// La
				revealedDisruptedEdges.put(edgeToinsert.getKey(), edgeToinsert);
			}
			Event.getDisruptedRoadConnections().indexOf(edgeToinsert);
			disruption = true;
		}
		return disruption;
	}

	private Edge redirectRoute(Route r) {
		String key;
		Edge toInsert = null;
		this.updateVisitedNetwork(r);
		if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() == 0) { // if the current position is the DMC
			toInsert =	jumpingToVictimNodesORnodesToVisit(toInsert);
		}
		else {
			if (this.connectedNodestoRevealedRoadNetwork.size() != 0) { // if the disaster management centre is connected
				toInsert=jumpingselectingTheBestConnectedNode(toInsert); // 1. Fly to connected nodes which have adjacent edges non-explored
				//2. Flying to edges with high priority
				if(toInsert==null) { // if there is not a connected node with adj edges to explore
					toInsert =	jumpingselectingNotExploredEdgesWithHighPriority(toInsert);
				}
			}
			else{// 4. Flying to disaster management. if the disaster management centre is not connected
				toInsert=jumpingtoDepotConnectition(toInsert);
			}
		}

		if (toInsert != null) {
			this.updateVisitedNetwork(r);
		}
		return toInsert;
	}

	private Edge jumpingtoDepotConnectition(Edge toInsert) {
		Node currentPostion = new Node(directoryNodes.get(0)); // initial position depot

		ArrayList<Edge> adjEdges = new ArrayList<>();
		if (!currentPostion.getAdjEdgesList().isEmpty()) {
			sortEdges(currentPostion.getAdjEdgesList(), this.aTest.getOptcriterion());
			for (Edge adj : currentPostion.getAdjEdgesList()) {
				if(!this.visitedRoadConnections.containsKey(adj.getKey())) {
				if (adj.getConnectivity() != 0) {
					adjEdges.add(adj);
				}
			}}
		}
		if (!adjEdges.isEmpty()) {// case 2: if ArrayList adjEdges is not empty

			if (auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() != currentPostion.getId()) {
				String key = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() + "," + currentPostion.getId();
				toInsert = this.directoryAerialEdges.get(key);
				auxRoute.getEdges().add(toInsert);
			}
			ArrayList<Edge> VictimAdjEdges = new ArrayList<>();
			for (Edge e : adjEdges) { // case 2.1: if LinkedList adjEdges contains victim nodes
				if (!visitedRoadConnections.containsKey(e.getKey())) {

					if (!this.checkedAccesibiliyVictims.containsKey(e.getOrigin().getId())
							&& this.VictimList.containsKey(e.getEnd().getId())) {
						VictimAdjEdges.add(e);

					}
				}
			}
			if (!VictimAdjEdges.isEmpty()) {
				toInsert = VictimAdjEdges.get(0);

				if (this.VictimList.containsKey(toInsert.getOrigin().getId())
						&& !this.VictimList.containsKey(toInsert.getEnd().getId())) {
					if (!this.NodesNotoVisit.containsKey(toInsert.getOrigin().getId())) {
						toInsert = VictimAdjEdges.get(0).getInverseEdge();
					}
				}

			}
			else {
				toInsert = adjEdges.get(0).getInverseEdge();
			}
		}
		return toInsert;
	}

	private Edge jumpingselectingNotExploredEdgesWithHighPriority(Edge toInsert) {
		ArrayList<Edge> auxListEdges = new ArrayList<>();
		for (Edge e : revealedDisruptedRoadConnections.values()) {// exploring the edges in the network
			if (!visitedRoadConnections.containsKey(e.getKey()) && !visitedRoadConnections.containsKey(e.getInverseEdge().getKey())) { // selecting which are not visited
				if (e.getConnectivity() != 0) { // selecting the one which has a connectivity value
					auxListEdges.add(e);
				}
			}
		}

		if (!auxListEdges.isEmpty()) {
			sortEdges(auxListEdges, aTest.getOptcriterion());
			toInsert=searchingVictimNodes(auxListEdges); // selecting victim node
			if(toInsert==null) {
				toInsert = auxListEdges.get(0);
				if (this.VictimList.containsKey(toInsert.getOrigin().getId()) || this.VictimList.containsKey(toInsert.getEnd().getId()) ) {// se deja que el ultimo nodo del
					LinkedList<Edge> adjOrigin = new LinkedList<Edge>();// exploring the adjacent edge of the origin node
					for (Edge adjOriginNode : toInsert.getOrigin().getAdjEdgesList()) {
						if (!this.visitedRoadConnections.containsKey(adjOriginNode.getKey())
								&& this.revealedDisruptedRoadConnections.containsKey(adjOriginNode.getKey())) {
							adjOrigin.add(adjOriginNode);
						}
					}
					LinkedList<Edge> adjEnd = new LinkedList<Edge>(); // exploring the adjacent edge of the end node
					for (Edge adjEndNode : toInsert.getEnd().getAdjEdgesList()) {
						if (!this.visitedRoadConnections.containsKey(adjEndNode.getKey())
								&& this.revealedDisruptedRoadConnections.containsKey(adjEndNode.getKey())) {
							adjEnd.add(adjEndNode);
						}
					}
					if (adjOrigin.size() == adjEnd.size()) {// comparison of total adjacent edges have the origin - and
						// end node
						if (this.VictimList.containsKey(toInsert.getOrigin().getId())) {
							toInsert = auxListEdges.get(0).getInverseEdge();
						}
						if (this.VictimList.containsKey(toInsert.getEnd().getId())) {
							toInsert = auxListEdges.get(0);
						}
					}
					if (adjOrigin.size() > adjEnd.size()) {
						toInsert = auxListEdges.get(0).getInverseEdge();
					} else {
						toInsert = auxListEdges.get(0);
					}
				}
			}
		}
		return toInsert;
	}

	private Edge jumpingselectingTheBestConnectedNode(Edge toInsert) {
		if (this.connectedEdgestoRevealedRoadNetwork .containsKey(this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getKey())) {// if current position is a
			if (!this.NodesNotoVisit.containsKey(this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd().getId())) { // if
				toInsert = selectBestEdgeNotDistuption(this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd(),
						aTest.getOptcriterion(), this.auxRoute);
			}
			else { // if the current position belongs to the list of NodesNotoVisit
				toInsert=searchingConnectedNodeToVisit(toInsert);
			}
		}
		else { // if the current position belongs to the list of NodesNotoVisit
			toInsert=searchingConnectedNodeToVisit(toInsert);
		}
		return toInsert;
	}

	private Edge jumpingToVictimNodesORnodesToVisit(Edge toInsert) {
		ArrayList<Edge> auxListVictims = new ArrayList<>();
		for (Node e : this.VictimList.values()) {
			if (!this.checkedAccesibiliyVictims.containsKey(e.getId()) && !this.NodesNotoVisit.containsKey(e.getId()) && this.directoryNodes.containsKey(e.getId())) {
				if (auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() != e.getId()) {
					String key = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() + "," + e.getId();
					if(this.directoryRoadEdges.containsKey(key)) {
						toInsert = this.directoryAerialEdges.get(key);
					}
					else {
						toInsert = this.directoryAerialEdges.get(key);}
					auxListVictims.add(toInsert);
				}
			}
		}
		if (!auxListVictims.isEmpty()) {
			sortEdges(auxListVictims, aTest.getOptcriterion());
			toInsert = auxListVictims.get(0);
		} else { // jumping to nodes which have adjacent edges to explore
			ArrayList<Edge> auxListNodesToVisit = new ArrayList<>();
			for (Node n : this.directoryNodes.values()) {
				if (!this.NodesNotoVisit.containsKey(n.getId())) {
					if (auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() != n.getId()) {
						String key = auxRoute.getEdges().get(auxRoute.getEdges().size() - 1).getEnd().getId() + "," + n.getId();
						toInsert = this.directoryAerialEdges.get(key);
						auxListNodesToVisit.add(toInsert);

					}
				}
			}
			if (!auxListNodesToVisit.isEmpty()) {
				sortEdges(auxListNodesToVisit, aTest.getOptcriterion());
				toInsert = auxListNodesToVisit.get(0);
			}
		}
		return toInsert;
	}


	private Edge selectBestEdgeNotDistuption(Node currentPostion, double criterion, Route r) {
		Edge edgeToinsert = null;
		LinkedList<Edge> adjEdges = new LinkedList<Edge>();
		if (!currentPostion.getAdjEdgesList().isEmpty()) {
			sortEdges(currentPostion.getAdjEdgesList(), criterion);
			for (Edge adj : currentPostion.getAdjEdgesList()) {
				if (!this.EdgesNotoVisit.containsKey(adj.getKey())) {
					if (!this.NodesNotoVisit.containsKey(adj.getEnd().getId())) {
						adjEdges.add(adj);
					}
				}
			}
			ArrayList<Edge> victimList = new ArrayList<>();
			if (adjEdges.size() != 0) {
				for (Edge e : adjEdges) {
					if (this.VictimList.containsKey(e.getEnd().getId()) && !this.checkedAccesibiliyVictims.containsKey(e.getEnd().getId())) {
						victimList.add(e);
					}
				}
				if (!victimList.isEmpty()) {
					sortEdges(victimList, criterion);
					edgeToinsert = victimList.get(0);
				} else {
					edgeToinsert = adjEdges.getFirst();
				}
			}
			else {


			}
		} else { // 2. Si no hay ejes adyacentes se busca saltar a un nodo conectado
			ArrayList<Edge> nodesToVisit = new ArrayList<>();
			if (!this.connectedNodestoRevealedRoadNetwork.isEmpty()) { // se seleccionan los nodos connectados de la red
				for (Node n : connectedNodestoRevealedRoadNetwork.values()) {
					if (!this.connectedNodesNotoVisit.containsKey(n.getId())) { // se seleccionan los nodos connectados
						// que se pueden visitar
						if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != n.getId()) {
							String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + n.getId();
							Edge aerialEdge = this.directoryAerialEdges.get(key);
							nodesToVisit.add(aerialEdge);
						}
					}
				}
			}
			if (!nodesToVisit.isEmpty()) { // se revisa si hay nodos conectados que se pueden visitar
				sortEdges(nodesToVisit, this.aTest.getOptcriterion());
				edgeToinsert = nodesToVisit.get(0);
			} else { // if empty, a victim node to visit is selected
				nodesToVisit = new ArrayList<>();
				for (Node n : VictimList.values()) {
					if (!this.checkedAccesibiliyVictims.containsKey(n.getId())) { // se seleccionan los nodos
						// connectados que se pueden visitar
						String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + n.getId();
						Edge aerialEdge = this.directoryAerialEdges.get(key);
						nodesToVisit.add(aerialEdge);
					}
				}
				if (!nodesToVisit.isEmpty()) { // se revisa si hay nodos conectados que se pueden visitar
					sortEdges(nodesToVisit, 1010);
					edgeToinsert = nodesToVisit.get(0);
				} else {
					LinkedList<Edge> auxListEdges = new LinkedList<Edge>();
					ArrayList<Edge> victimList = new ArrayList<>();
					for (Edge e : this.edgeRoadConnection) {
						if (!EdgesNotoVisit.containsKey(e.getKey())) {
							auxListEdges.add(e);
							if (!this.checkedAccesibiliyVictims.containsKey(e.getEnd().getId())
									&& this.VictimList.containsKey(e.getEnd().getId())) {
								victimList.add(e);
							}
						}
					}

					if (!victimList.isEmpty()) { // se revisa si hay nodos conectados que se pueden visitar
						sortEdges(victimList, 1010);
						edgeToinsert = victimList.get(0);
					} else {
						// se revisa si hay nodos conectados que se pueden visitar
						sortEdges(nodesToVisit, 1010);
						edgeToinsert = nodesToVisit.get(0);

					}
					if (edgeToinsert.getOrigin().getId() != r.getEdges().get(r.getEdges().size() - 1).getEnd().getId()
							&& edgeToinsert.getEnd().getId() != r.getEdges().get(r.getEdges().size() - 1).getEnd()
							.getId()) {
						String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + ","
								+ edgeToinsert.getOrigin().getId();
						Edge aerialEdge = this.directoryAerialEdges.get(key);
						r.getEdges().add(aerialEdge);
						r.getEdges().add(edgeToinsert);
					} else {
						if (edgeToinsert.getEnd().getId() == r.getEdges().get(r.getEdges().size() - 1).getEnd()
								.getId()) {
							r.getEdges().add(edgeToinsert.getInverseEdge());
						} else {
							r.getEdges().add(edgeToinsert);
						}
					}
				}
			}
		}
		if (edgeToinsert != null) {
			this.updateVisitedNetwork(edgeToinsert);
		}
		updateVisitedNetwork(r);
		return edgeToinsert;
	}

	private Edge searchingConnectedNodeToVisit(Edge edgeToinsert) {
		ArrayList<Edge> nodesToVisit = new ArrayList<>();
		if (!this.connectedNodestoRevealedRoadNetwork.isEmpty()) {
			for (Node n : connectedNodestoRevealedRoadNetwork.values()) {
				if (!this.connectedNodesNotoVisit.containsKey(n.getId())) {
					if (this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd().getId() != n.getId()) {
						String key = this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd().getId() + ","
								+ n.getId();
						if (!this.visitedRoadConnections.containsKey(key)) {
							if (!this.NodesNotoVisit.containsKey(n.getId())) {
								Edge aerialEdge = this.directoryAerialEdges.get(key);
								nodesToVisit.add(aerialEdge);
							}
						}
					}
				}
			}
		}
		if (!nodesToVisit.isEmpty()) {
			sortEdges(nodesToVisit, 1010); // sorting by distance 1010
			edgeToinsert = nodesToVisit.get(0);
		}
		else { // si es empty se selecciona un nodo victima a visitar

			nodesToVisit = new ArrayList<Edge>();
			for (Node n : VictimList.values()) {
				if(n.getId()!=this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd().getId()) {
				if (!this.checkedAccesibiliyVictims.containsKey(n.getId())) { // se seleccionan los nodos
					// connectados que se pueden
					// visitar
					String key = this.auxRoute.getEdges().get(this.auxRoute.getEdges().size() - 1).getEnd().getId() + "," + n.getId();
					if (!this.EdgesNotoVisit.containsKey(key)) {
						if (!this.NodesNotoVisit.containsKey(n.getId())) {
							Edge aerialEdge = this.directoryAerialEdges.get(key);
							nodesToVisit.add(aerialEdge);
						}
					}
				}
			}}
			if (!nodesToVisit.isEmpty()) { // se revisa si hay nodos conectados que se pueden visitar
				sortEdges(nodesToVisit, 1010);
				edgeToinsert = nodesToVisit.get(0);
			}
		}
		return edgeToinsert;
	}

	private Edge backforwardMovement(Route r, Edge e) {
		if (e != null) {
			if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != e.getOrigin().getId()
					&& r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != e.getEnd().getId()) {
				String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + e.getOrigin().getId();
				Edge connectionToOrigin = directoryAerialEdges.get(key);
				key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + e.getEnd().getId();
				Edge connectionToEnd = directoryAerialEdges.get(key);
				if (this.visitedRoadConnections.containsKey(connectionToEnd.getKey())
						&& this.visitedRoadConnections.containsKey(connectionToOrigin.getKey())
						|| !this.visitedRoadConnections.containsKey(connectionToEnd.getKey())
						&& !this.visitedRoadConnections.containsKey(connectionToOrigin.getKey())) {
					if (connectionToOrigin.getDistance() > connectionToEnd.getDistance()) {
						r.getEdges().add(connectionToEnd);
					} else {
						r.getEdges().add(connectionToOrigin);
					}
				} else {
					if (this.visitedRoadConnections.containsKey(connectionToEnd.getKey())
							|| this.visitedRoadConnections.containsKey(connectionToOrigin.getKey())) {
						if (!this.visitedRoadConnections.containsKey(connectionToEnd.getKey())) {
							r.getEdges().add(connectionToEnd);
						} else {
							r.getEdges().add(connectionToOrigin);
						}
					}
				}
			}
			for (Edge edge : r.getEdges()) {
				updateVisitedNetwork(edge);
			}

			updateVisitedNetwork(r);

			if (r.getEdges().get(r.getEdges().size() - 1).getEnd() == e.getOrigin()) {
				updateVisitedNetwork(r);
				return e;

			} else {
				updateVisitedNetwork(r);
				return e.getInverseEdge();

			}
		} else {
			return null;
		}
	}

	private boolean disruptedEdge2(Edge edgeToinsert) {
		boolean disruption = false;

		if (Event.getDisruptedEdges().containsKey(edgeToinsert.getKey())) {
			revealedDisruptedEdges.put(edgeToinsert.getInverseEdge().getKey(), edgeToinsert.getInverseEdge()); /// La
			revealedDisruptedEdges.put(edgeToinsert.getKey(), edgeToinsert);
			disruption = true;
		}
		return disruption;
	}

	private Edge selectBestEdge(Node currentPostion, double criterion, Route r, UpdateRoadInformation reveledNetwork,
			Inputs inputs) {
		// case 1: if LinkedList adjEdges not empty
		Edge edgeToinsert = exploringAdjEdges(currentPostion); // looking for the best adjacent edge to explore
		// case 2: there is not adjacent edge to visit
		if (edgeToinsert==null) { // there is not promising adjacent edges to explore
			edgeToinsert = redirectRouteNoDisruption(r, reveledNetwork, inputs); // jumping to a promising edge
		}

		this.updateVisitedNetwork(edgeToinsert);
		updateVisitedNetwork(r);
		return edgeToinsert;
	}

	private Edge redirectRouteNoDisruption(Route r, UpdateRoadInformation reveledNetwork, Inputs inputs) {

		Edge EdgetoRedirectRoute = redirectRoute(r);
		EdgetoRedirectRoute = jumpingEdge(r, EdgetoRedirectRoute);

		return EdgetoRedirectRoute;
	}

	private Edge jumpingEdge(Route r, Edge e) {

		if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != e.getOrigin().getId()) {
			String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + e.getOrigin().getId();
			Edge connectionToOrigin = directoryAerialEdges.get(key);
			if (connectionToOrigin == null) {
				connectionToOrigin = new Edge(r.getEdges().get(r.getEdges().size() - 1).getEnd(), e.getOrigin());
				connectionToOrigin.setTime(connectionToOrigin.calcTime());
				connectionToOrigin.setDistance(connectionToOrigin.calcDistance());
			}

			r.getEdges().add(connectionToOrigin);
		}
		updateVisitedNetwork(r);
		return e;

	}

	private Edge exploringAdjEdges(Node currentPostion) {
		Edge edgeToinsert=null;
		ArrayList<Edge> adjEdges = new ArrayList<>();
		sortEdges(currentPostion.getAdjEdgesList(), this.aTest.getOptcriterion());
		for (Edge adj : currentPostion.getAdjEdgesList()) {
			if (!this.visitedRoadConnections.containsKey(adj.getKey())) {
				if (adj.getConnectivity() != 0) {
					adjEdges.add(adj);
				}
			}
		}
		if (!adjEdges.isEmpty()) { // if there is adjacent edges. Now it is looking for victim nodes
			edgeToinsert=this.searchingVictimNodes(adjEdges);
			if (edgeToinsert==null) { // if there is victim nodes in the list. Then the closest one is selected
				edgeToinsert = adjEdges.get(0);}// // if there is not victim nodes in the list. Then the closest adjacent edge is selected
		}
		return edgeToinsert;
	}

	private Edge searchingVictimNodes(ArrayList<Edge> auxListEdges) {
		Edge edgeToinsert=null;
		ArrayList<Edge> VictimAdjEdges = new ArrayList<>();
		for (Edge e : auxListEdges) { // case 2.1: if ArrayList adjEdges contains victim nodes
			if (!visitedRoadConnections.containsKey(e.getKey())) {
				if (!this.checkedAccesibiliyVictims.containsKey(e.getOrigin().getId())
						&& this.VictimList.containsKey(e.getOrigin().getId())
						|| !this.checkedAccesibiliyVictims.containsKey(e.getEnd().getId())
						&& this.VictimList.containsKey(e.getEnd().getId())) {
					VictimAdjEdges.add(e);

				}
			}
		}
		if (!VictimAdjEdges.isEmpty()) { // if there is victim nodes in the list. Then the closest one is selected
			edgeToinsert = VictimAdjEdges.get(0);

		}
		return edgeToinsert;
	}

	private Edge redirectRouteNoDisruption(Route r) {
		Edge EdgetoRedirectRoute = redirectRouteJumping(r);
		EdgetoRedirectRoute = backforwardMovement(r, EdgetoRedirectRoute);
		return EdgetoRedirectRoute;
	}

	private Edge redirectRouteJumping(Route r) {
		String key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + 0;
		Edge toInsert = this.directoryAerialEdges.get(key);

		// edge default
		if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() == 0) {
			ArrayList<Edge> auxListVictims = new ArrayList<>();

			for (Node e : this.VictimList.values()) {
				if (!this.checkedAccesibiliyVictims.containsKey(e.getId())
						&& !this.NodesNotoVisit.containsKey(e.getId())) {
					if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != e.getId()) {
						key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + e.getId();
						toInsert = this.directoryAerialEdges.get(key);
						if (toInsert == null) {
							toInsert = new Edge(r.getEdges().get(r.getEdges().size() - 1).getEnd(), e);
							toInsert.setTime(toInsert.calcTime());
							toInsert.setDistance(toInsert.calcDistance());
						}
						auxListVictims.add(toInsert);
					}
				}
			}
			if (!auxListVictims.isEmpty()) {
				sortEdges(auxListVictims, aTest.getOptcriterion());
				toInsert = auxListVictims.get(0);
			} else {
				ArrayList<Edge> auxListNodesToVisit = new ArrayList<>();
				for (Node n : this.directoryNodes.values()) {
					if (!this.NodesNotoVisit.containsKey(n.getId())) {
						if (r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() != n.getId()) {
							key = r.getEdges().get(r.getEdges().size() - 1).getEnd().getId() + "," + n.getId();
							toInsert = this.directoryAerialEdges.get(key);
							if (toInsert == null) {
								toInsert = new Edge(r.getEdges().get(r.getEdges().size() - 1).getEnd(), n);
								toInsert.setTime(toInsert.calcTime());
								toInsert.setDistance(toInsert.calcDistance());
							}
							auxListNodesToVisit.add(toInsert);
						}
					}
				}
				if (!auxListNodesToVisit.isEmpty()) {
					sortEdges(auxListNodesToVisit, aTest.getOptcriterion());
					toInsert = auxListNodesToVisit.get(0);
				}
			}
		}
		ArrayList<Edge> auxListEdges = new ArrayList<>();
		if (this.connectedNodestoRevealedRoadNetwork.size() != 0) {
			for (Edge e : revealedDisruptedRoadConnections.values()) {
				if (!visitedRoadConnections.containsKey(e.getKey())
						&& !visitedRoadConnections.containsKey(e.getInverseEdge().getKey())) {
					if (e.getConnectivity() != 0) {
						auxListEdges.add(e);
					}
				}
			}
			if (!auxListEdges.isEmpty()) {
				sortEdges(auxListEdges, aTest.getOptcriterion());
				toInsert = auxListEdges.get(0);
				if (this.VictimList.containsKey(toInsert.getOrigin().getId())
						|| this.VictimList.containsKey(toInsert.getEnd().getId())) {// se deja que el ultimo nodo del
					// arco sea una victima
					LinkedList<Edge> adjOrigin = new LinkedList<Edge>();
					for (Edge adjOriginNode : toInsert.getOrigin().getAdjEdgesList()) {
						if (!this.visitedRoadConnections.containsKey(adjOriginNode.getKey())
								&& this.revealedDisruptedRoadConnections.containsKey(adjOriginNode.getKey())) {
							adjOrigin.add(adjOriginNode);
						}

					}

					// end node
					LinkedList<Edge> adjEnd = new LinkedList<Edge>();
					for (Edge adjEndNode : toInsert.getEnd().getAdjEdgesList()) {
						if (!this.visitedRoadConnections.containsKey(adjEndNode.getKey())
								&& this.revealedDisruptedRoadConnections.containsKey(adjEndNode.getKey())) {
							adjEnd.add(adjEndNode);
						}
					}
					if (adjOrigin.size() == adjEnd.size()) {
						if (this.VictimList.containsKey(toInsert.getOrigin().getId())) {
							toInsert = auxListEdges.get(0).getInverseEdge();
						}
						if (this.VictimList.containsKey(toInsert.getEnd().getId())) {
							toInsert = auxListEdges.get(0);
						}

					}
					if (adjOrigin.size() > adjEnd.size()) { // se vuela al nodo con menos oportunidades
						toInsert = auxListEdges.get(0).getInverseEdge();
					} else {
						toInsert = auxListEdges.get(0);
					}

				}
			} else { // all road network has been visited
				this.updateVisitedNetwork(r);
			}
		} else {
			// si no hay ningun nodo conectado hasta el momento
			// key=r.getEdges().get(r.getEdges().size()-1).getEnd().getId()+","+0; // si el
			// primer todos los arcos explorados no demuestran que el depot esta conectado
			// el UAV regresa al depot
			// toInsert=this.directoryAerialEdges.get(key);
			Node currentPostion = r.getEdges().get(0).getOrigin(); // toma el depot

			// do {// explora las connecciones del depot
			LinkedList<Edge> adjEdges = new LinkedList<Edge>();
			if (!currentPostion.getAdjEdgesList().isEmpty()) {
				sortEdges(currentPostion.getAdjEdgesList(), this.aTest.getOptcriterion());
				for (Edge adj : currentPostion.getAdjEdgesList()) {
					if (adj.getConnectivity() != 0) {
						adjEdges.add(adj);
					}
				}
				// case 2: if LinkedList adjEdges is not empty

				r.getEdges().add(toInsert);
				LinkedList<Edge> VictimAdjEdges = new LinkedList<Edge>();
				for (Edge e : adjEdges) { // case 2.1: if LinkedList adjEdges contains victim nodes
					if (!visitedRoadConnections.containsKey(e.getKey())) {
						// if (this.VictimList.containsKey(e.getOrigin().getId())
						// ||this.VictimList.containsKey(e.getEnd().getId()) ) {
						if (!this.visitedVictims.containsKey(e.getOrigin().getId())
								|| !this.visitedVictims.containsKey(e.getEnd().getId())) {
							VictimAdjEdges.add(e);
							// }
						}
					}
				}
				if (!VictimAdjEdges.isEmpty()) {
					toInsert = VictimAdjEdges.getFirst();
					if (this.VictimList.containsKey(toInsert.getOrigin().getId())
							&& !this.VictimList.containsKey(toInsert.getEnd().getId())) {// se deja que el ultimo nodo
						// del arco sea una victima
						if (!this.NodesNotoVisit.containsKey(toInsert.getOrigin().getId())) {
							toInsert = VictimAdjEdges.getFirst().getInverseEdge();
						}
					}

				} else {
					for (Edge e : adjEdges) { // case 2.2: if LinkedList adjEdges contains only road crossings

						if (!visitedRoadConnections.containsKey(e.getKey())) {
							if (!this.visitedNodeConnections.isEmpty()) {
								if (!this.visitedNodeConnections.containsKey(e.getOrigin().getId())
										&& !this.visitedNodeConnections.containsKey(e.getEnd().getId())) {
									toInsert = e;
									if (this.VictimList.containsKey(toInsert.getOrigin().getId())
											&& !this.VictimList.containsKey(toInsert.getEnd().getId())) {// se deja que
										// el ultimo
										// nodo del
										// arco sea
										// una
										// victima
										if (!this.NodesNotoVisit.containsKey(toInsert.getOrigin().getId())) {
											toInsert = e.getInverseEdge();
										}
									}
									break;
								}
							}
						}
					}

				}
			}

			this.updateVisitedNetwork(r); // se actualizan los arcos visitado

		}
		return toInsert;
	}

	private void updateVisitedNetwork(Edge edgeToinsert) {
		visitedRoadConnections.put(edgeToinsert.getKey(), edgeToinsert);
		visitedRoadConnections.put(edgeToinsert.getInverseEdge().getKey(), edgeToinsert.getInverseEdge());
		this.visitedNodeConnections.put(edgeToinsert.getOrigin().getId(), edgeToinsert.getOrigin());
		this.visitedNodeConnections.put(edgeToinsert.getEnd().getId(), edgeToinsert.getEnd());

	}

	private void updateVisitedNetwork(Route r) {
		for (Edge edgeToinsert : r.getEdges()) {
			visitedRoadConnections.put(edgeToinsert.getKey(), edgeToinsert);
			visitedRoadConnections.put(edgeToinsert.getInverseEdge().getKey(), edgeToinsert.getInverseEdge());
			this.visitedNodeConnections.put(edgeToinsert.getOrigin().getId(), edgeToinsert.getOrigin());
			this.visitedNodeConnections.put(edgeToinsert.getEnd().getId(), edgeToinsert.getEnd());
		}

		for (Node n : this.nodeList) {
			if (visitedNodeConnections.containsKey(n.getId()) && this.connectedNodesNotoVisit.containsKey(n.getId())) {// si
				HashMap<String, Edge> check = new HashMap<String, Edge>();
				for (Edge e : n.getAdjEdgesList()) {
					if (this.visitedRoadConnections.containsKey(e.getKey())) { // exploring

						check.put(e.getKey(), e);
						// visitedVictims.put(n.getId(),n);
					}
				}
				if (check.size() == n.getAdjEdgesList().size()) {
					this.connectedNodesNotoVisit.put(n.getId(), n);
				}

				// 2. Si todos sus arcos adjacentes ya han sido visitados
				if (n.getAdjEdgesList().isEmpty()) {
					this.connectedNodesNotoVisit.put(n.getId(), n);
				}

			}
		}
		this.visitedVictims(r);
	}

	private void sortEdges(ArrayList<Edge> adjEdgesList, double criterion) {
		if (!adjEdgesList.isEmpty()) {
			if (criterion == 1100) {
				adjEdgesList.sort(Edge.connectivityComp);
			}
			if (criterion == 1010) {
				adjEdgesList.sort(Edge.minDistance);
			}
			if (criterion == 1001) {
				adjEdgesList.sort(Edge.weightComp);
			}
		}
	}

	// Getters

	public Route getAuxRoute() {
		return auxRoute;
	}

	public Map<Integer, Node> getConnectedNodestoRevealedRoadNetwork() {
		return connectedNodestoRevealedRoadNetwork;
	}

	public Map<Integer, Node> getrecheablevictims() {
		return recheablevictims;
	}


	public Map<Integer, Node> getVictimList() {
		return VictimList;
	}

	public Solution getBack_Sol() {
		return back_Sol;
	}

	public Map<Integer, Node> getDirectoryNodes() {
		return directoryNodes;
	}

	public Test getaTest() {
		return aTest;
	}

	public Disaster getEvent() {
		return Event;
	}

}

package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Assessment {

	private final ArrayList<Edge> edgeRoadConnection;
	private double averageConnectivity = 0;
	private double maxConnectivityValue = 0;
	private double maxDistance = Double.MIN_VALUE;
	private double minDistance = Double.MAX_VALUE;
	private final ArrayList<Node> nodeList;
	private HashMap<Integer, Node> removedNode = new HashMap<>();
	private HashMap<String, Edge> removedEdge = new HashMap<>();
	private final Map<String, List<Integer>> directoryVictimsCycles= new HashMap<>();
	private final Map<String, List<Integer>> directoryIntermediateCycles= new HashMap<>();
	private final Map<Integer, Node> directoryNodes;
	private final Map<String, Edge> directoryEdges;
	private final Map<Integer, Node> checkedAccesibiliyVictims;
	private final Map<Integer, Node> victimList;
	// these information is continuously updated
	private final Map<String, Edge>visitedRoadConnections;
	private final Map<String, Edge> revealedDisruptedRoadConnections; // revealed disrupted connections
	private final Map<String, Edge>revealedDisruptedEdges; // revealed disrupted edges
	private final ArrayList<Node> revealednodesDisruption = null; // revealed disrupted node
	private FindingPath tree = new FindingPath();
	private Test aTest;
	private Inputs inputs;


	public Assessment(UpdateRoadInformation RoadInformations, Inputs inp, Test test) {
		aTest=test;
		inputs=inp;
		victimList = RoadInformations.getVictimList();
		checkedAccesibiliyVictims = RoadInformations.getcheckedAccesibiliyVictims();
		nodeList = RoadInformations.getNodeList(); // the list of nodes is copied
		directoryNodes = RoadInformations.getdirectoryNodes();
		directoryEdges = RoadInformations.getdirectoryroadConnections();
		edgeRoadConnection = RoadInformations.getedgeRoadConnection();
		visitedRoadConnections=RoadInformations.getvisitedRoadConnections();
		revealedDisruptedRoadConnections = RoadInformations.getRevealedDisruptedRoadConnections();
		revealedDisruptedEdges=RoadInformations.getrevealedDisruptedEdges();
		setAdjEdges(); // in case that the edges in the network change because a disruption
		cleanningNetwork(RoadInformations.getRevealedDisruptedRoadConnections(), inputs);
		computingCriteria(aTest.getpercentageDistance());
		setAdjEdges();

	}

	private void computingCriteria(float a) {
		removingPreviousConnectivityValues();
		importanceNodes();
		/* Testing if all nodes are connected with the network */
		generatingNodeAverageConnectivity();
		connectivityEdges();
		weightEdges(a);

	}

	private void removingPreviousConnectivityValues() {
		for (Edge e : revealedDisruptedRoadConnections.values()) {
			e.setWeight(0);
			e.setConnectivity(0);
		}
	}

	private void weightEdges(float a) {
		for (Edge e : this.revealedDisruptedRoadConnections.values()) { // set the importance for each edge
			double maxAdjConnectivity = e.getConnectivity();
			double minAdjTime = e.getTime();
			double maxAdjTime = e.getTime();
			for (Edge adjEdge : e.getOrigin().getAdjEdgesList()) {
				if (this.directoryEdges.containsKey(adjEdge.getKey())) {
					if (adjEdge.getConnectivity() > maxAdjConnectivity) {
						maxAdjConnectivity = adjEdge.getConnectivity();
					}
					if (adjEdge.getTime() < minAdjTime) {
						minAdjTime = adjEdge.getTime();
					}
					if (adjEdge.getTime() > maxAdjTime) {
						maxAdjTime = adjEdge.getTime();
					}
					e.maxAdjConnectivity = maxAdjConnectivity;
					e.maxAdjTime = maxAdjTime;
					e.minAdjTime = minAdjTime;
				}
			}
			double firstPartweight = (a) * (1 - (e.getTime() / e.maxAdjTime));
			double secondPartweight = (1 - a) * (e.getConnectivity() / maxAdjConnectivity);
			e.setWeight(firstPartweight + secondPartweight);
		}
	}

	private void connectivityEdges() {
		for (Edge e : this.revealedDisruptedRoadConnections.values()) { // set the importance for each edge
			double firstPart = Math.max(e.getOrigin().getProfit(), e.getEnd().getProfit())
					* (e.getOrigin().getImportance() + e.getEnd().getImportance());
			double conectivityEdge = firstPart / (this.averageConnectivity * e.getTime());
			System.out.println("conectivityEdge_"+e.getOrigin().getId()+","+e.getEnd().getId()+"__"+e.getConnectivity());
			e.setConnectivity(conectivityEdge);
			System.out.println("conectivityEdge_"+e.getOrigin().getId()+","+e.getEnd().getId()+"__"+e.getConnectivity());

			if (e.getConnectivity() > this.maxConnectivityValue) {
				this.maxConnectivityValue = e.getConnectivity();
				if (e.getDistance() > this.maxDistance) {
					this.maxDistance = e.getTime();
				}
				if (e.getDistance() < this.minDistance) {
					this.minDistance = e.getTime();
				}
			}
		}
	}

	private void generatingNodeAverageConnectivity() {
		double sumAllTime = 0;
		double sumAllImportances = 0;
		for (Edge e : this.revealedDisruptedRoadConnections.values()) { // computing the average distance for all active
			// edges
			sumAllTime += e.getTime();
		}
		for (Node n : this.nodeList) {
			sumAllImportances += n.getImportance();
		}
		double averageTime = sumAllTime / this.revealedDisruptedRoadConnections.size();
		double averageImportances = sumAllImportances / nodeList.size();
		averageConnectivity = averageImportances / averageTime;
		System.out.println("Average_Time_"+ averageTime );
		System.out.println("Average_averageImportances_"+ averageImportances );
	}

	private void importanceNodes() {
		for (Node e : this.nodeList) {
			if (e.getProfit() > 1) {
				e.setImportance(e.getProfit() / e.getAdjEdgesList().size());
			} else {
				e.setImportance(0.0);
			}
		}
		nodeList.clear();
		for (Node n : directoryNodes.values()) {
			nodeList.add(n);
		}

		int testing = 10;
		for (int t = 0; t <= testing; t++) { // test security
			for (Node n : nodeList) {
				double counterDynamicScore = 0;
				if (n.getImportance() == 0) {
					for (Edge edgeAdj : n.getAdjEdgesList()) {
						if (this.directoryEdges.containsKey(edgeAdj.getKey())) {
							counterDynamicScore += edgeAdj.getEnd().getImportance();
						}
					}
					if (n.getAdjEdgesList().size() != 0) {
						n.setImportance(counterDynamicScore / n.getAdjEdgesList().size());
					} else {
						n.setImportance(counterDynamicScore);
					}
				}
			}
		}
		for(Node n : nodeList) {
			System.out.println("importance_"+n.getExpDemand()+"__"+ n.getImportance() );
		}
		System.out.println("importance_");
	}

	private void cleanningNetwork(Map<String, Edge> map, Inputs inputs) {
		Boolean depotConnected = checkingConnectionsToVictims(map, inputs); // 1. Check if each road crossing is connected with the depot and victim node
		if (depotConnected) {// 2. Check if each intermediate node is connected with a victim node after removing the connections of the depot
			checkingIntermediateNodes(map, inputs, depotConnected);
		}
		// 4. Updating the list of nodes
		setCleanedRoadConnection(depotConnected); // leave all functional edges
		// 5. Set the list of adjacent edges
		setAdjEdges();
	}


	private void setAdjEdges() { /// Setting adjacent edges to each node in the node list
		directoryNodes.clear();
		for(Edge e:this.directoryEdges.values()) {
			directoryNodes.put(e.getOrigin().getId(), e.getOrigin());
			directoryNodes.put(e.getEnd().getId(), e.getEnd());
			if(e.getOrigin().getId()==18 && e.getEnd().getId()==19){
				System.out.print("stopr");
			}
		}

		for (Node i : this.directoryNodes.values()) {
			i.getAdjEdgesList().clear();
		}

		for (Node i : this.directoryNodes.values()) {
			if(i.getId()==18 ){
				System.out.print("stopr");
			}
			ArrayList<Edge> adj = new ArrayList<>();
			for (Node j : this.directoryNodes.values()) {
				String key = i.getId() + "," + j.getId();
				if (this.directoryEdges.containsKey(key)) {
					adj.add(directoryEdges.get(key));
				}

			}
			i.setAdjedges(adj);
		}
		nodeList.clear();
		for (Node i : this.directoryNodes.values()) {
			nodeList.add(i);
		}
	}

	private void checkingIntermediateNodes(Map<String, Edge> map, Inputs inputs, Boolean depotConnected) {
		checkingConnectionsIntermediateNodesVictimNodes(map); // 1. Checking if removing the depot the intermediate nodes have still a connection with victim nodes
		checkingNodesWithOneAdjacentEdge(map,depotConnected); // removing edges with just one adjacent edge
		ArrayList<Edge> x = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			x.add(newEdge);
		}
		// selecting all connections which are not contained in the spanning tree
		selectingCycles(map, inputs);
		removingSubNetworksWithIntermediateNodes();
		//new DrawingNetwork(x,aTest);
		//removingSubNetworksWithIntermediateNodes(map, inputs);// 2. Looking for non necessaries subnetworks
		x = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			x.add(newEdge);
		}
		new DrawingNetwork(x,aTest);
		checkingNodesWithOneAdjacentEdge(map,depotConnected); // removing edges with just one adjacent edge
		x = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			x.add(newEdge);
		}
		//new DrawingNetwork(x,aTest);
		removingSubNetworksWithVictimNodes(map, inputs);// 2. Looking for non necessaries subnetworks
		x = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			x.add(newEdge);
		}
		new DrawingNetwork(x,aTest);
		checkingNodesWithOneAdjacentEdge(map,depotConnected); // removing edges with just one adjacent edge
	}

	private void selectingCycles(Map<String, Edge> map, Inputs inp) {
		ArrayList<Edge> victimsansintermediateNetwork = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			if(!removedNode.containsKey(e.getOrigin().getId()) &&  !removedNode.containsKey(e.getEnd().getId())) {
				victimsansintermediateNetwork.add(newEdge);}
		}
		//new DrawingNetwork(victimsansintermediateNetwork,aTest);
		Map<String, Edge> edgesGenerateCycles = new HashMap<>();
		ArrayList<Edge> xx = new ArrayList<>();
		for (Edge e : tree.getTree().values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			xx.add(newEdge);
		}
		// original spanning tree from depot
		new DrawingNetwork(xx,aTest);
		for (Edge e : victimsansintermediateNetwork) { // 2. Select the edges which are not in the spanning tree from the depot (main spanning tree)
			if(!tree.getTree().containsKey(e.getKey())) {
				Edge newEdge = new Edge(e);
				edgesGenerateCycles.put(newEdge.getKey(),newEdge);}
		}
		Map<Integer, Node> nodeListToSpannigtree= new HashMap<>();
		for(Edge e:edgesGenerateCycles.values()) { // generates the list of nodes to create the spanning tree
			nodeListToSpannigtree.put(e.getOrigin().getId(), e.getOrigin());
			nodeListToSpannigtree.put(e.getEnd().getId(), e.getEnd());
		}

		// selecting the cycles from the main spanning tree from the depot
		findingSubNetworks(edgesGenerateCycles, tree);

		nodesToCtoCreateSpanningTree(map, inputs,nodeListToSpannigtree); // Searches the spanning tree for each node and detects possible cycles

	}

	private void findingSubNetworks(Map<String, Edge> edgesGenerateCycles, FindingPath spanningTree) {
		Map<String, Edge> checkedCycles = new HashMap<>();// list to save connections which represents a cycle
		for(Edge e: edgesGenerateCycles.values() ) {
			Paths allposiblePaths= new Paths();
			//		List<Integer> subnetwork=new ArrayList<>();
			if(!checkedCycles.containsKey(e.getKey())) {
				checkedCycles.put(e.getKey(),e);
				checkedCycles.put(e.getInverseEdge().getKey(),e.getInverseEdge());
				//subnetwork= allposiblePaths.findingallPathsToVictimNode(tree.getTree(),inputs,nodeList, e.getOrigin().getId(),e.getEnd().getId());
				allposiblePaths.findingallPathsToVictimNode(spanningTree.getTree(),inputs,nodeList, e.getOrigin().getId(),e.getEnd().getId());

				for(List<Integer> subnetwork :allposiblePaths.listOfpaths) {
					if(subnetwork.size()>2) {
						if(!hasVictimNode(subnetwork)) {
							directoryIntermediateCycles.put(e.getKey(),subnetwork);
						}
						else {
							directoryVictimsCycles.put(e.getKey(),subnetwork);
						}
					}
				}
			}
		}
	}

	private void nodesToCtoCreateSpanningTree(Map<String, Edge> map, Inputs inputs2, Map<Integer, Node> nodeListToSpannigtree) {
		ArrayList<Edge> copytree= new ArrayList<>();
		for(Edge e:map.values()) { // copies the current network without considering the items to be deleted
			if(!removedNode.containsKey(e.getOrigin().getId()) &&  !removedNode.containsKey(e.getEnd().getId())) {
				copytree.add(new Edge(e));
			}
		}

		for(Node node:nodeListToSpannigtree.values()) {
			// 1. create the spanning tree
			FindingPath treeConncetions = new FindingPath();
			treeConncetions.spanningTreePath(copytree, node.getId()); // compute the spanning tree for each node
			// 2. Select all edges that are not contained in the spanning tree
			Map<String, Edge> edgesGenerateCycles = new HashMap<>();
			for (Edge e : copytree) { // 2. Select the edges which are not in the spanning tree
				if(!treeConncetions.getTree().containsKey(e.getKey())) { // it excludes the edges list in the spanning tree
					Edge newEdge = new Edge(e);
					edgesGenerateCycles.put(newEdge.getKey(),newEdge);}
			}

			// to remove
			ArrayList<Edge> xx= new ArrayList<>();
			for (Edge e : map.values()) { // 1. network
				Edge newEdge = new Edge(e);
				xx.add(newEdge);
			}

			//new DrawingNetwork(xx,aTest);
			xx= new ArrayList<>();

			for (Edge e : treeConncetions.getTree().values()) { // 1. Compute the Spanning tree
				Edge newEdge = new Edge(e);
				xx.add(newEdge);
			}
			//new DrawingNetwork(xx,aTest);
			// 3. Find the cycles
			findingSubNetworks(edgesGenerateCycles, treeConncetions);
		}

	}



	private void removingSubNetworksWithVictimNodes(Map<String, Edge> map, Inputs inputs2) {
		ArrayList<Edge> victimsansintermediateNetwork = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			if(!removedNode.containsKey(e.getOrigin().getId()) &&  !removedNode.containsKey(e.getEnd().getId())) {
				victimsansintermediateNetwork.add(newEdge);}
		}
		//new DrawingNetwork(victimsansintermediateNetwork,aTest);
		Map<String, Edge> edgesGenerateCycles = new HashMap<>();
		ArrayList<Edge> xx = new ArrayList<>();
		for (Edge e : map.values()) { // 1. Compute the Spanning tree
			Edge newEdge = new Edge(e);
			xx.add(newEdge);
		}
		//new DrawingNetwork(xx,aTest);
		for (Edge e : victimsansintermediateNetwork) { // 2. Select the edges which are not in the spanning tree
			if(!tree.getTree().containsKey(e.getKey())) {
				Edge newEdge = new Edge(e);
				edgesGenerateCycles.put(newEdge.getKey(),newEdge);}
		}
		Map<String, Edge> net= new HashMap<>();
		for(Edge e:victimsansintermediateNetwork) {
			net.put(e.getKey(),e);
			net.put(e.getInverseEdge().getKey(), e.getInverseEdge());
		}
		Solution routesList= new Solution();
		// 3. For each edge which are not in the spanning tree
		// find a path in the spanning tree

		storingAllpaths(directoryVictimsCycles,routesList); // store all
		for(Route  r: routesList.getRoutes()) {
			String key=r.getEdges().get(0).getOrigin().getId()+","+r.getEdges().get(r.getEdges().size()-1).getEnd().getId();
			findingunnecesaryNodesandEdges(routesList,directoryEdges.get(key));
		}
		setCleanedRoadConnection(true); // leave all functional edges
		// 5. Set the list of adjacent edges
		setAdjEdges();
	}

	private void storingAllpaths(Map<String, List<Integer>> directoryVictimsCycles, Solution routesList) {
		// searching for sub-networks with only victim nodes

		for(List<Integer> list: directoryVictimsCycles.values()) {
			// here is the list of paths with victim  nodes
			Route aux= new Route(); // 1. creating a new route per path
			boolean areThereEdges=checkEdgesRemoved(list);
			if(areThereEdges) {
			for(int i=0;i< list.size()-1;i++) {
				if(list.size()>2) {// list of elements in the path
					String keyEdge=list.get(i)+","+list.get(i+1);
					Edge e= this.directoryEdges.get(keyEdge);
					aux.getEdges().add(e);}
			}
			if(!aux.getEdges().isEmpty()) {
				aux.calcDistance();
				aux.calcTime();
				routesList.getRoutes().add(aux);}}// 2. inserting the route in the solution
		}
		routesList.sorting(); // 3. sorting the routes into the solutions

	}





	private boolean checkEdgesRemoved(List<Integer> list) {
		boolean thereAreEdges= true;
		for(int i=0;i< list.size()-1;i++) {
			if(list.size()>2) {// list of elements in the path
				String keyEdge=list.get(i)+","+list.get(i+1);
				if(!directoryEdges.containsKey(keyEdge)) {
					thereAreEdges= false;
				}
				if(!thereAreEdges) {
					break;
				}
				}
		}
		return thereAreEdges;
	}

	private void findingunnecesaryNodesandEdges(Solution routesList, Edge conection) {
		//  1. Identify the visited and functional networks
		if(!routesList.getRoutes().isEmpty()) {
			Route auxR= visitedandFunctionalNetwork(routesList);
			if(!auxR.getEdges().isEmpty()) { // auxR is empty when a route has no been visited
				Map<String, Edge> listEdgesToKeep= new HashMap<String, Edge>();
				for(Edge e:auxR.getEdges()) {
					listEdgesToKeep.put(e.getKey(),e);
					listEdgesToKeep.put(e.getInverseEdge().getKey(),e.getInverseEdge());
				}


				Map<String, Edge> edgesToRemove= new HashMap<>();
				for(Edge e:auxR.getEdges()) {
					if(!listEdgesToKeep.containsKey(e.getKey())) {
						edgesToRemove.put(e.getKey(), e);
						edgesToRemove.put(e.getInverseEdge().getKey(), e.getInverseEdge());
					}
				}

				computingTheAccesiblitytoVictims(edgesToRemove);
			}
			else {
				for(int i=0;i<routesList.getRoutes().size();i++) {
				auxR= new Route(routesList.getRoutes().get(i)); // Select the smallest sub-network (with the shortest distance)
				// Identify the boarding points of the sub-networks
				ArrayList<Node> boardingNodes= new ArrayList<>();// Create the pathBoardingPoints list
				searchingBoardingNodes(auxR,boardingNodes);
				findingFunctionalEdges(boardingNodes,auxR,conection);}
			}
		}

	}

	private void computingTheAccesiblitytoVictims(Map<String, Edge> listEdgesToKeep) {
		ArrayList<Edge> copytree= new ArrayList<>();
		for(Edge e:revealedDisruptedRoadConnections.values()) {
			if(!listEdgesToKeep.containsKey(e.getKey()) && !listEdgesToKeep.containsKey(e.getInverseEdge().getKey()) ) {
				copytree.add(new Edge(e));
			}
		}

		// checking if the victim accesibility changes

		FindingPath treeintermediate = new FindingPath();

		Map<Integer, Node> treeRoadConnection = treeintermediate.spanningTreePath(copytree, 0);

		Map<Integer, Node> checkingVictims= new HashMap<>();
		for(Node n:this.victimList.values()) {
			if(treeRoadConnection.containsKey(n.getId())) {
				checkingVictims.put(n.getId(), n);
			}
		}
		Map<Integer, Node> victiminThestree= CheckingCurrentConnectionToVictims();
		if(victiminThestree.equals(checkingVictims)) { // if the accessibility does not change
			for(Edge e:listEdgesToKeep.values()) {
				this.removedEdge.put(e.getKey(), e);
				this.removedEdge.put(e.getInverseEdge().getKey(), e.getInverseEdge());
			}
		}

	}

	private void findingFunctionalEdges(ArrayList<Node> boardingNodes, Route auxR, Edge conection) {
		if(!boardingNodes.isEmpty() && boardingNodes.size()>1) {
			// if there is more than 1 boarding point
			findingPathamongBoardingPoints(boardingNodes,auxR,conection);}

	}

	private void findingPathamongBoardingPoints(ArrayList<Node> boardingNodes, Route auxR, Edge conection) {
		Node[] extremNodes= new Node[2] ;
		Solution visitedRoutesStore = new Solution();
		findingextremeNodes(extremNodes,boardingNodes);// finding the extreme nodes
		// Considering the subnetwork. Find all possible paths in the subnetwork which contain the boarding points
		Paths allposiblePaths= new Paths();
		HashMap<String, Edge> edgesInRoute= new HashMap<>();
		for(Edge e:auxR.getEdges()) {
			edgesInRoute.put(e.getKey(), e);
			edgesInRoute.put(e.getInverseEdge().getKey(), e.getInverseEdge());
		}
		// adding the connection which create the cycle
		edgesInRoute.put(conection.getKey(), conection);
		edgesInRoute.put(conection.getInverseEdge().getKey(), conection.getInverseEdge());

		// finding path between boarding nodes
		allposiblePaths.findingallPathsToVictimNode(edgesInRoute,inputs,nodeList, extremNodes[0].getId(),extremNodes[1].getId());
		boolean visitedPath= true; // determine if a path between boarding points is visited
		for(ArrayList<Integer> list: allposiblePaths.listOfpaths) {// here is the list of paths with victim  nodes
			Route r= new Route(); // 1. creating a new route per path
			visitedPath= true;
			for(int i=0;i< list.size()-1;i++) { // list of elements in the path
				String key= list.get(i)+","+list.get(i+1);
				if(!this.visitedRoadConnections.containsKey(key)) {
					visitedPath=false;
				}
				if(!visitedPath) {
					break;
				}
			}
			if(visitedPath) {
				for(int i=0;i< list.size()-1;i++) { // list of elements in the path
					String key= list.get(i)+","+list.get(i+1);
					Edge e= new Edge(this.directoryEdges.get(key));
					r.getEdges().add(e);
				}
				visitedRoutesStore.getRoutes().add(r);
			}
		}

		if(!visitedRoutesStore.getRoutes().isEmpty()) {//check if all the edges are functional
			boolean functionPath=true;
			Route funtionalEdgeswithBoardingPoints= new Route();
			for(Route r: visitedRoutesStore.getRoutes()) {
				for(Edge e:r.getEdges()) {
					if(!this.revealedDisruptedRoadConnections.containsKey(e.getKey())) {
						functionPath=false;
					}
					if(!functionPath) { // el path contiene un eje disrupto ya no mi interesa la ruta
						break;
					}
				}
				if(functionPath) { // if yes, store the path
					for(Edge e:r.getEdges()) {
						funtionalEdgeswithBoardingPoints.getEdges().add(e);
					}
				}
			}

			Map<Integer, Node> functionalEdges= new HashMap<>(); // create a list of nodes connected to functional edges (functionalNodes)
			for(Edge e:funtionalEdgeswithBoardingPoints.getEdges()) {
				functionalEdges.put(e.getOrigin().getId(), e.getOrigin());
				functionalEdges.put(e.getEnd().getId(), e.getEnd());
			}
			// sort the list of visited paths (Paths With victim nodes)
			Map<Integer, Node> victimsInSubNetwork= new HashMap<>();
			for(Edge e:auxR.getEdges() ) {
				if(this.victimList.containsKey(e.getOrigin().getId())) {
					victimsInSubNetwork.put(e.getOrigin().getId(), e.getOrigin());
				}
				if(this.victimList.containsKey(e.getEnd().getId())) {
					victimsInSubNetwork.put(e.getEnd().getId(), e.getEnd());
				}
			}
			Map<String, Edge> EdgestoKeep= new HashMap<>();
			ArrayList<Node> connectedVictims= new ArrayList<>();
			// If no, check if there is a direct connection between the victim node and any node in the list of (functionalNodes)
			for(Node victim: victimsInSubNetwork.values()) {
				for(Node functional:functionalEdges.values()) {
					if(victim.getId()!=functional.getId()) {
						String key= victim.getId()+","+functional.getId();
						if(this.directoryEdges.containsKey(key)) {
							Edge e= new Edge(this.directoryEdges.get(key));
							if(this.visitedRoadConnections.containsKey(e.getKey())) {
								if(this.revealedDisruptedRoadConnections.containsKey(e.getKey())) {
									connectedVictims.add(victim);
									EdgestoKeep.put(e.getKey(), e);
									EdgestoKeep.put(e.getInverseEdge().getKey(), e.getInverseEdge());
								}
							}
						}
					}
				}
			}

			if(!connectedVictims.isEmpty()) {
				for(Node n:connectedVictims) {
					functionalEdges.put(n.getId(), n);}
			}
			// if yes, store the victim node to the list of functionalNodes
			Map<Integer, Node> victimsPath= new HashMap<>();
			for(Edge e:auxR.getEdges() ) {
				if(this.victimList.containsKey(e.getOrigin().getId())) {
					victimsPath.put(e.getOrigin().getId(), e.getOrigin());
				}
				if(this.victimList.containsKey(e.getEnd().getId())) {
					victimsPath.put(e.getEnd().getId(), e.getEnd());
				}
			}

			if(victimsPath.equals(victimsInSubNetwork)) { // if the path contains all victims from subnetwork
				for(Edge e: funtionalEdgeswithBoardingPoints.getEdges()) {
					EdgestoKeep.put(e.getKey(), e);
					EdgestoKeep.put(e.getInverseEdge().getKey(), e.getInverseEdge());
				}
			}

			Map<String, Edge> edgesToRemove= new HashMap<>();
			for(Edge e:auxR.getEdges()) {
				if(!EdgestoKeep.containsKey(e.getKey())) {
					edgesToRemove.put(e.getKey(), e);
					edgesToRemove.put(e.getInverseEdge().getKey(), e.getInverseEdge());
				}
			}
			this.computingTheAccesiblitytoVictims(edgesToRemove);
		}
	}

	private Route visitedandFunctionalNetwork(Solution routesList) {
		boolean visitedPath= true;
		Solution visitedRoutesStore= new Solution();
		for(Route r:routesList.getRoutes() ) { // looking in all the routes
			for(Edge e: r.getEdges()) {
				if(!this.visitedRoadConnections.containsKey(e.getKey())) {
					visitedPath=false;
				}
				if(!visitedPath) {
					break;
				}
			}
			if(visitedPath) {
				Route auxR= new Route();
				for(Edge e: r.getEdges()) {
					auxR.getEdges().add(e);
				}
				visitedRoutesStore.getRoutes().add(auxR);
				visitedPath= true;
			}

		}
		// funtional edges
		Route smallestNetwork= new Route();
		if(!visitedRoutesStore.getRoutes().isEmpty()) {
			boolean disruptedEdge= false;
			Solution visitedFunctionalRoutesStore= new Solution();
			for(Route r:visitedRoutesStore.getRoutes() ) { // looking in all the routes
				for(Edge e: r.getEdges()) {
					if(!this.revealedDisruptedEdges.containsKey(e.getKey())) {
						disruptedEdge=true;
					}
					if(disruptedEdge) {
						break;
					}
				}
				if(!disruptedEdge) {
					Route auxR= new Route();
					for(Edge e: r.getEdges()) {
						auxR.getEdges().add(e);
					}
					visitedFunctionalRoutesStore.getRoutes().add(auxR);
				}
			}
			if(!visitedFunctionalRoutesStore.getRoutes().isEmpty()) {
				smallestNetwork=new Route(visitedFunctionalRoutesStore.getRoutes().get(0));
			}


		}
		//		if(smallestNetwork.getEdges().isEmpty()) {
		//			smallestNetwork=new Route(routesList.getRoutes().get(0));
		//		}
		return smallestNetwork;
	}

	private void findingextremeNodes(Node[] extremNodes, ArrayList<Node> boardingNodes) {
		boardingNodes.sort(Node.positionY);
		if(boardingNodes.get(0).getY()-boardingNodes.get(boardingNodes.size()-1).getY()==0) {
			boardingNodes.sort(Node.positionX);
		}
		extremNodes[0]=boardingNodes.get(0);
		extremNodes[1]=boardingNodes.get(boardingNodes.size()-1);
	}

	private void searchingBoardingNodes(Route smallestNetwork, ArrayList<Node> boardingNodes) {
		Map<Integer, Node> subnetworkElements = new HashMap<Integer, Node>();
		for(Edge e: smallestNetwork.getEdges()) {
			subnetworkElements.put(e.getOrigin().getId(), e.getOrigin());
			subnetworkElements.put(e.getEnd().getId(), e.getEnd());
		}
		Boolean boardingNode= false;
		for(Node n:subnetworkElements.values()) {
			for(Edge e:n.getAdjEdgesList()) { // exploring the adjacent edge of the elements in the subnetwork
				if(!subnetworkElements.containsKey(e.getEnd().getId())) {
					boardingNode=true;
				}
				if(boardingNode) {
					boardingNodes.add(n);
					break;
				}
			}
			if(boardingNode) {
				boardingNode= false;
			}
		}
	}

	private void checkingNodesWithOneAdjacentEdge(Map<String, Edge> map, Boolean depotConnected) {
		int iteration=1;// 4. Check the remaining nodes, if the node has just an adjacent edge remove from the network
		int removedNodes=0;
		int edgesOneconnection=0;  // total of iterations
		while(edgesOneconnection<iteration) {
			edgesOneconnection++;
			for (Edge e : map.values()) { //Check the remaining nodes, if the node has just an adjacent edge remove from the network
				if (e.getOrigin().getId() != 0) { // it looks for road crossings connected with a victim node
					if (e.getOrigin().getAdjEdgesList().size() < 2 && !removedNode.containsKey(e.getOrigin().getId()) ) { // if the origin node has only an edge
						if (e.getOrigin().getProfit() == 1) {
							removedNodes++; // counter of removed nodes
							removedNode.put(e.getOrigin().getId(), e.getOrigin());
						}
					}
				}
			}
			// 4. Updating the list of nodes
			setCleanedRoadConnection(depotConnected); // leave all functional edges
			// 5. Set the list of adjacent edges
			setAdjEdges();
			if(removedNodes!=0) {
				edgesOneconnection=0;
				iteration=removedNodes+1; // iteration to test
				removedNodes=0; // re-start the counter
			}
		}
	}

	private void removingSubNetworksWithIntermediateNodes() {

		//List<Integer> subnetwork=new ArrayList<>();
		for(List<Integer> subnetwork: directoryIntermediateCycles.values()) {
			findingAdditionalConnections(subnetwork);
		}
		setCleanedRoadConnection(true); // leave all functional edges
		// 5. Set the list of adjacent edges
		setAdjEdges();

	}

	private boolean hasVictimNode(List<Integer> subnetwork) {
		boolean hasVictimNode=false;
		for(int i:subnetwork) {
			if(this.victimList.containsKey(i)) {
				hasVictimNode=true;
				break;
			}
		}
		return hasVictimNode;
	}

	private void findingAdditionalConnections(List<Integer> subnetwork) {
		// 4. Select the path and the additional connection which contains the initial and end node
		boolean isAsubnetworkToRemove=false;
		boolean additional=false;
		int additionalConnection=-1;
		int[] connections= new int[2];
		for(int i: subnetwork) {
			for(Edge e: this.directoryNodes.get(i).getAdjEdgesList()) {
				additional=false;
				if(!subnetwork.contains(e.getEnd().getId())) { // it find the node is connected with other node in the network
					// counting the connection that have the
					additional=true;
				}
				if(additional) {
					additionalConnection++;
					break;
				}
			}

			if(additional && additionalConnection>=0 && additionalConnection<=1) {
				connections[additionalConnection]=i;
			}
			else {
				if(additionalConnection>1) {
					break;
				}
			}
		}
		if(additionalConnection==-1) {
			isAsubnetworkToRemove=true;
		}
		if(additionalConnection==1 || additionalConnection==0 ) {
			checkingtheRelevanceOfcycles(connections,additionalConnection,isAsubnetworkToRemove,subnetwork);
		}
	}
	private void checkingtheRelevanceOfcycles(int[] connections, int additionalConnection,
			boolean isAsubnetworkToRemove, List<Integer> subnetwork) {
		Map<Integer, Node> victiminThestree= CheckingCurrentConnectionToVictims();
		if(isAsubnetworkToRemove) {//  If no nodes contain different nodes. It means that subnetwork is disconnected
			// remove all nodes
			for(int i: subnetwork) {
				this.removedNode.put(i, this.nodeList.get(i));
			}
		}
		if(additionalConnection==0) { // If the subnetwork is connected to big network just by one node
			// check if inside the subnetwork there is a path to a victim node
			theresIsaVictimconnectedtoSubnetwork(subnetwork,victiminThestree);

		}

		if(additionalConnection>=1 ) {

			evaluateRelevanceSubnetwork(connections,subnetwork,victiminThestree);


		}

	}

	private void evaluateRelevanceSubnetwork(int[] connections, List<Integer> subnetwork,
			Map<Integer, Node> victiminThestree) {
		// selection the nodes which connect the subnetwork with the network

		Map<String,Edge> subNet= new HashMap<>();

		for(int i:subnetwork ) {
			for(int j:subnetwork) {
				String key=i+","+j;
				if(i!=j) {
					if(directoryEdges.containsKey(key)) {
						Edge e= new Edge(this.directoryEdges.get(key));
						subNet.put(key, e);
					}
				}
			}
		}

		// se calcula todos los posibles caminos entre ellos
		Paths allposiblePaths= new Paths();
		allposiblePaths.findingallPathsToVictimNode(subNet,this.inputs,this.nodeList,connections[0],connections[1]);

		// se verifica si existe una connección directa entre los nodos
		String key =connections[0] + ","+connections[1];
		String key1 =connections[1] + ","+connections[0];
		boolean thereIsDirectConnection=false;
		if(directoryEdges.containsKey(key)) {
			Edge e= new Edge(this.directoryEdges.get(key));
			thereIsDirectConnection=true;
			if(thereIsDirectConnection) {
				if(this.visitedRoadConnections.containsKey(e.getKey())) {
					if(!this.revealedDisruptedEdges.containsKey(e.getKey())) { // if the condition is meet. We can evaluate remove all remaing connections
						evaluatingPaths(allposiblePaths,victiminThestree,key,key1);
					}
				}
			}}
		else {// sino existe una connección directa. Identificar si algún camino de los encontrados,
			// pertenece a los arcos visitados y a los arcos funcionales

			checkingtheVisitedPaths(allposiblePaths,victiminThestree,key,key1);
		}



	}

	private void checkingtheVisitedPaths(Paths allposiblePaths, Map<Integer, Node> victiminThestree, String key2, String key1) {
		ArrayList<ArrayList<String>> VisitedandPaths=new ArrayList<>();
		ArrayList<ArrayList<String>> nonVisitedandPaths=new ArrayList<>();
		boolean workingPath=false;

		for(int i=0;i<allposiblePaths.listOfpaths.size();i++) {
			ArrayList<Integer> list=allposiblePaths.listOfpaths.get(i);
			ArrayList<String> path=new ArrayList<>();
			for(int j=0;j<list.size()-1;j++) { // checking if the edges have been already visited
				String key=list.get(j)+","+list.get(j+1);
				// se asume que todos los caminos no son visitados
				path.add(key);
			}
			nonVisitedandPaths.add(path);
		}

		for(int i=0;i<nonVisitedandPaths.size();i++) { // searching for visited paths
			ArrayList<String> list=nonVisitedandPaths.get(i);
			// searching for visited edges and working
			ArrayList<String> path=new ArrayList<>();
			boolean visited=false;
			for(int j=0;j<list.size();j++) {
				System.out.println(this.visitedRoadConnections.containsKey(list.get(j)));
				if(this.visitedRoadConnections.containsKey(list.get(j))) {
					if(this.revealedDisruptedEdges.containsKey(list.get(j))) {

						visited=false;
					}
					else {
						visited=true;
					}
				}
				if(!visited) {

					break;}

			}
			if(visited) {
				for(int j=0;j<list.size();j++) {
					path.add(list.get(j));
				}
				VisitedandPaths.add(path);
				visited=true;
			}

		}

		// all visited edges
		Map<String,Edge> visitedEdges=new HashMap<>();
		for(int i=0;i<VisitedandPaths.size();i++) { // searching for visited paths
			ArrayList<String> list=VisitedandPaths.get(i);
			for(int j=0;j<list.size();j++) {
				Edge e=new Edge(this.directoryEdges.get(list.get(j)));
				visitedEdges.put(e.getKey(), e);
				visitedEdges.put(e.getInverseEdge().getKey(), e.getInverseEdge());
			}
		}
		if(!visitedEdges.isEmpty()) {
			// all non visited edges
			Map<String,Edge> nonvisitedEdges=new HashMap<>();
			for(int i=0;i<nonVisitedandPaths.size();i++) { // searching for visited paths
				ArrayList<String> list=nonVisitedandPaths.get(i);
				for(int j=0;j<list.size();j++) {
					if(!visitedEdges.containsKey(list.get(j))) {
						Edge e=new Edge(this.directoryEdges.get(list.get(j)));
						nonvisitedEdges.put(e.getKey(), e);
						nonvisitedEdges.put(e.getInverseEdge().getKey(), e.getInverseEdge());
					}
				}
			}

			ArrayList<Edge> copytree= new ArrayList<>();
			for(Edge e:revealedDisruptedRoadConnections.values()) {
				if(!nonvisitedEdges.containsKey(e.getKey()) && !nonvisitedEdges.containsKey(e.getInverseEdge().getKey()) ) {
					copytree.add( e);
				}
			}

			// checking if the victim accesibility changes

			FindingPath treeintermediate = new FindingPath();

			Map<Integer, Node> treeRoadConnection = treeintermediate.spanningTreePath(copytree, 0);

			//removing all nonecesaries edges

			Map<Integer, Node> checkingVictims= new HashMap<>();
			for(Node n:this.victimList.values()) {
				if(treeRoadConnection.containsKey(n.getId())) {
					checkingVictims.put(n.getId(), n);
				}
			}

			if(victiminThestree.equals(checkingVictims)) { // if the accessibility does not change
				for(Edge e:nonvisitedEdges.values()) {
					this.removedEdge.put(e.getKey(), e);
					this.removedEdge.put(e.getInverseEdge().getKey(), e.getInverseEdge());

				}
			}}
	}


	private void evaluatingPaths(Paths allposiblePaths, Map<Integer, Node> victiminThestree, String key2, String key3) {
		Map<String, Edge> edgesToRemove=new HashMap<>();
		for(int i=0;i<allposiblePaths.listOfpaths.size();i++) {
			ArrayList<Integer> list=allposiblePaths.listOfpaths.get(i);
			for(int j=0;j<list.size()-2;j++) {
				String key=list.get(j)+","+list.get(j+1);
				if(this.directoryEdges.containsKey(key)) {
					Edge e= new Edge(this.directoryEdges.get(key));
					edgesToRemove.put(e.getKey(),e);
				}
			}
		}

		// si existe una connección directa y esta ya ha sido visitada y esta funcionando
		// se eliminan los arcos de los otros caminos

		Map<String, Edge> copytree= new HashMap<>(); // copy of tree map without subnetwork
		for(Edge e:tree.getTree().values()) {
			if(!edgesToRemove.containsKey(e.getKey()) && !edgesToRemove.containsKey(e.getInverseEdge().getKey())) {
				copytree.put(e.getKey(), e);
			}
		}


		Map<Integer, Node> copyNode= new HashMap<>();
		for(Edge e:copytree.values()) {
			copyNode.put(e.getOrigin().getId(), e.getOrigin());
			copyNode.put(e.getEnd().getId(), e.getEnd());
		}

		Map<Integer, Node> checkingVictims= new HashMap<>();
		for(Node n:this.victimList.values()) {
			if(copyNode.containsKey(n.getId())) {
				checkingVictims.put(n.getId(), n);
			}
		}
		Edge ed;
		if(this.directoryEdges.containsKey(key2)) {
			ed= new Edge(this.directoryEdges.get(key2));



			if(victiminThestree.equals(checkingVictims)) { // if the accessibility does not change
				// remove the subnetwork
				for(Edge e:edgesToRemove.values()) {
					if(e.getKey()!=key2 && e.getKey()!=key3  ) {
						if(e.getOrigin().getId()!=ed.getOrigin().getId() && e.getOrigin().getId()!=ed.getEnd().getId()) {
							this.removedNode.put(e.getOrigin().getId(), e.getOrigin());}
						if(e.getEnd().getId()!=ed.getOrigin().getId() && e.getEnd().getId()!=ed.getEnd().getId()) {
							this.removedNode.put(e.getEnd().getId(), e.getEnd());}
					}
				}
			}}
	}

	private void theresIsaVictimconnectedtoSubnetwork(List<Integer> subnetwork, Map<Integer, Node> victiminThestree) {
		ArrayList<Edge> copytree= new ArrayList<>(); // copy of tree map without subnetwork
		for(Edge e:tree.getTree().values()) {
			if(!subnetwork.contains(e.getOrigin().getId()) && !subnetwork.contains(e.getEnd().getId()) ) {
				copytree.add(e);
			}
		}

		// computing the spanning tree with the possible network
		Map<Integer, Node> treeRoadConnection = new HashMap<>() ;
		for(Edge e:copytree) {
			treeRoadConnection.put(e.getOrigin().getId(), e.getOrigin());
			treeRoadConnection.put(e.getEnd().getId(), e.getEnd());
		}
		Map<Integer, Node> checkingVictims= new HashMap<>();
		for(Node n:this.victimList.values()) {
			if(treeRoadConnection.containsKey(n.getId())) {
				checkingVictims.put(n.getId(), n);
			}
		}
		if(victiminThestree.equals(checkingVictims)) { // if the accessibility does not change
			// remove the subnetwork
			for(int i:subnetwork) {
				this.removedNode.put(i, this.directoryNodes.get(i));
				//this.removedNode.put(i, this.nodeList.get(i));
			}
		}

	}

	private Map<Integer, Node> CheckingCurrentConnectionToVictims() {
		Map<Integer, Node> victiminThestree= new HashMap<>();
		for(Node n:this.victimList.values()) {
			if(tree.getTreeNodes().containsKey(n.getId())) {
				victiminThestree.put(n.getId(), n);
			}
		}
		return victiminThestree;
	}

	private void checkingConnectionsIntermediateNodesVictimNodes(Map<String, Edge> map) {
		LinkedList<Edge> victimsansintermediateNetwork = new LinkedList<Edge>();
		for (Edge e : map.values()) {
			Edge newEdge = new Edge(e);
			victimsansintermediateNetwork.add(newEdge);
		}
		ArrayList<Edge> intermediateNetwork = new ArrayList<Edge>();
		for (Edge e : victimsansintermediateNetwork) {
			if (e.getOrigin().getId() != 0) {
				if (e.getEnd().getId() != 0) {
					if (!removedNode.containsKey(e.getOrigin().getId())) {
						if (!removedNode.containsKey(e.getEnd().getId())) {
							Edge newEdge = new Edge(e);
							intermediateNetwork.add(newEdge);
						}
					}
				}
			}
		}

		for (Node n : this.nodeList) { // intermediate node
			Boolean intermediateNodeConnected = true;
			if (n.getId() != 0 && n.getProfit() == 1 && !removedNode.containsKey(n.getId())) { // searching road crossing to remove
				intermediateNodeConnected = false;
				FindingPath treeintermediate = new FindingPath();
				// Spanning tree for each intermediate node
				Map<Integer, Node> treeRoadConnection = treeintermediate.spanningTreePath(intermediateNetwork, n.getId());
				for (Node nn : this.victimList.values()) {// evaluating if the spanning tree has at least one victim node in the
					if (!this.checkedAccesibiliyVictims.containsKey(nn.getId())) {
						if (treeRoadConnection.containsKey(nn.getId())) {
							intermediateNodeConnected = true;
						}
					}
					if (intermediateNodeConnected) {
						break;
					}
				}
			}
			if (!intermediateNodeConnected) {
				removedNode.put(n.getId(), n);
			}

		}

	}

	private void setCleanedRoadConnection(boolean depotConnecterd) {
		if (!depotConnecterd) { // if the depot is not connected all nodes are removed from the network
			for (Node nn : this.nodeList) {
				removedNode.put(nn.getId(), nn);
			}
		}
		for (Edge e : this.revealedDisruptedRoadConnections.values()) {
			if (removedNode.containsKey(e.getOrigin().getId())) {
				this.removedEdge.put(e.getKey(), e);
			}
			if (removedNode.containsKey(e.getEnd().getId())) {
				this.removedEdge.put(e.getKey(), e);
			}
		}

		for (Node n : removedNode.values()) {
			System.out.println(n.getId());
			if (directoryNodes.containsKey(n.getId())) {
				System.out.println("OtherPart"+directoryNodes.get(n.getId()));
				directoryNodes.remove(n.getId());
			}
		}

		nodeList.clear(); // updating the list of nodes in the network
		for (Node n : directoryNodes.values()) {
			nodeList.add(n);
		}

		// updating the list of victim nodes in the network and the list of  checkedAccesibiliyVictims
		for (Node n : this.victimList.values()) {
			if(!directoryNodes.containsKey(n.getId())) {
				//victimList.remove(n.getId());
				this.checkedAccesibiliyVictims.put(n.getId(), n);
			}
		}

		for (Edge e : removedEdge.values()) {
			if (revealedDisruptedRoadConnections.containsKey(e.getKey())) {
				revealedDisruptedRoadConnections.remove(e.getKey()); // removing from the revelead network
				directoryEdges.remove(e.getKey()); // removing from directory
			}
		}

		this.edgeRoadConnection.clear(); // updating the list of edges in the network
		for (Edge e : revealedDisruptedRoadConnections.values()) {
			this.edgeRoadConnection.add(e);
		}
	}

	private boolean checkingConnectionsToVictims(Map<String, Edge> map, Inputs inputs) {
		boolean path = false; // there is a path from the depot to at least one victim?
		ArrayList<Edge> victimsansintermediateNetwork = new ArrayList<>();
		for (Edge e : map.values()) {
			Edge newEdge = new Edge(e);
			victimsansintermediateNetwork.add(newEdge);
		}

		Map<Integer, Node> treePath = tree.spanningTreePath(victimsansintermediateNetwork, 0);
		// checking all intermediate nodes
		for(Node n:this.nodeList) { // here victim nodes can be removed
			if (!treePath.containsKey(n.getId())) {
				this.removedNode.put(n.getId(), n);
			}
		}
		// if the depot is connected to at least one victim node which has not be evaluated, then continue the exploration
		for(Node victim:this.victimList.values()) {
			if(!this.checkedAccesibiliyVictims.containsKey(victim.getId())) {
				if(treePath.containsKey(victim.getId())) {
					path=true;
				}
			}
			if(path) { // go out of the loop because at least a victim could be evaluated
				break;
			}
		}
		return path;
	}

	// GETTERS
	public Map<String, Edge> getRevealedDisruptedRoadConnections() {
		return revealedDisruptedRoadConnections;
	}

	public Map<String, Edge> getRevealedDisruptedEdges() {
		return revealedDisruptedEdges;
	}

	public ArrayList<Node> getRevealednodesDisruption() {
		return revealednodesDisruption;
	}

	public Map<Integer, Node> getcheckedAccesibiliyVictims() {
		return checkedAccesibiliyVictims;
	}
}
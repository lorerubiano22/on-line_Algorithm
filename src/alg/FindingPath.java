package alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class FindingPath {
	// solution
	private HashMap<String, Edge> tree = new HashMap<String, Edge>();
	private Map<Integer, Node> treeNodes = new HashMap<Integer, Node>();

		public boolean spanningTree(ArrayList<Edge> Network, int origin, int victima) {
		boolean path = false;

		Map<Integer, Node> listNodes= new HashMap<>();
		for(Edge e:Network) {
			listNodes.put(e.getOrigin().getId(), new Node(e.getOrigin()));
			listNodes.put(e.getEnd().getId(), new Node(e.getEnd()));
		}
		HashMap<String,Edge> copyEdgesNetwork = new HashMap<>();
		LinkedList<Edge> copyAerialNetwork = new LinkedList<Edge>();
		HashMap<Integer, Node> edgesList = new HashMap<Integer, Node>();
		int max = 0;
		for (Edge e : Network) {
			if (max < Math.max(e.getOrigin().getId(), e.getEnd().getId())) {
				max = Math.max(e.getOrigin().getId(), e.getEnd().getId());
			}
			copyEdgesNetwork.put(e.getKey(), e);
		}

		// setting adjacent edges

				Map<Integer,ArrayList<Edge>> adj= new HashMap<>();

				for(Node n:listNodes.values()) {
					ArrayList<Edge> ed= new ArrayList<>();
					for(Node nd:listNodes.values()) {
						String key=n.getId()+","+nd.getId();
						if(copyEdgesNetwork.containsKey(key)) {
						Edge e=copyEdgesNetwork.get(key);
						ed.add(e);}
					}
					adj.put(n.getId(), ed);
				}



		for (Edge e : copyEdgesNetwork.values()) {
			Edge ed= new Edge(listNodes.get(e.getOrigin().getId()),listNodes.get(e.getEnd().getId()));
			copyAerialNetwork.add(ed);
		}
		int maxIndex = max + 1;
		copyAerialNetwork.sort(Edge.minDistance);

		Edge result[] = new Edge[copyAerialNetwork.size()]; // Tnis will store the resultant MST
		int e = 0; // An index variable, used for result[]
		int i = 0;

		Subset subsets[] = new Subset[maxIndex];
		for (i = 0; i <= max; ++i) {
			subsets[i] = new Subset();
		}

		for (i = 0; i <= max; ++i) {
			subsets[i].parent = i;
			subsets[i].rank = 0;
		}

		i = 0;

		for (e = 0; e < copyAerialNetwork.size(); e++)
		// while ( e< this.nodes.length - 1)
		{
			// Step 2: Pick the smallest edge. And increment
			// the index for next iteration
			Edge next_edge = copyAerialNetwork.get(e);

			int x = -1;
			int y = -1;
			x = find(subsets, next_edge.getOrigin().getId());
			y = find(subsets, next_edge.getEnd().getId());
			// If including this edge does't cause cycle,
			// include it in result and increment the index
			// of result for next edge
			if (x != y && x != -1 && y != -1) {
				result[e] = next_edge;
				Union(subsets, x, y);
			}
			// Else discard the next_edge
		}

		HashMap<String, Edge> connectedEdges = new HashMap<String, Edge>();
		HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
		for (i = 0; i < result.length; i++) {
			if (result[i] != null) {
				connectedEdges.put(result[i].getKey(), result[i]);
				String key=result[i].getEnd().getId()+","+result[i].getOrigin().getId();
				Edge ed= new Edge(result[i].getEnd(),result[i].getOrigin());
				connectedEdges.put(key, ed);
				Nodes.put(result[i].getOrigin().getId(), result[i].getOrigin());
				Nodes.put(result[i].getEnd().getId(), result[i].getEnd());
				// if(this.connectedNodestoRevealedRoadNetwork.containsKey(result[i].getOrigin().getId())
				// ||
				// this.connectedNodestoRevealedRoadNetwork.containsKey(result[i].getEnd().getId())
				// ) {
				edgesList.put(result[i].getOrigin().getId(), result[i].getOrigin());
				edgesList.put(result[i].getEnd().getId(), result[i].getEnd());
				System.out.println(result[i].getOrigin().getId() + " -- " + result[i].getEnd().getId() + " == "
						+ result[i].getDistance());
				// }

			}

		}

		LinkedList<Node> nodescon = new LinkedList<Node>();
		HashMap<String, Edge> tree = new HashMap<String, Edge>();
		HashMap<Integer, Node> treeNodes = new HashMap<Integer, Node>();
		nodescon.add(Nodes.get(origin));

		int iter = 0;
		int nodessInserted = 1;
		while (iter < nodessInserted) {

			for (Node n : nodescon) {
				iter++;

				if (n != null) {
					for (Edge ed : adj.get(n.getId())) {
						if (connectedEdges.containsKey(ed.getKey()) && !tree.containsKey(ed.getKey())) {
							tree.put(ed.getKey(), ed);
							treeNodes.put(ed.getOrigin().getId(), ed.getOrigin());
							treeNodes.put(ed.getEnd().getId(), ed.getEnd());
							iter = 0;
						}
					}
				}
			}
			nodescon.clear();

			for (Node nn : treeNodes.values()) {
				nodescon.add(nn);
			}
			if (!tree.isEmpty()) {
				nodessInserted = nodescon.size();
			}

		}

		for (int l = 0; l < maxIndex; l++) {
			for (int m = 0; m < maxIndex; m++) {
				if (l != m) {
					String key = l + "," + m;
					if (connectedEdges.containsKey(key)) {

					}
				}
			}
		}

		if (treeNodes.containsKey(victima)) {
			path = true;
		}

		return path;
	}



	public Map<Integer, Node> spanningTreePath(Test test, ArrayList<Edge> nt, int origin) {

		LinkedList<Edge> copyAerialNetwork = new LinkedList<Edge>();
		HashMap<Integer, Node> edgesList = new HashMap<Integer, Node>();
		Map<Integer, Node> listNodes= new HashMap<>();
		for(Edge e:nt) {
			listNodes.put(e.getOrigin().getId(), new Node(e.getOrigin()));
			listNodes.put(e.getEnd().getId(), new Node(e.getEnd()));
		}
		HashMap<String,Edge> copyEdgesNetwork = new HashMap<>();
		int max = 0;
		for (Edge e : nt) {
			if (max < Math.max(e.getOrigin().getId(), e.getEnd().getId())) {
				max = Math.max(e.getOrigin().getId(), e.getEnd().getId());
			}
			copyEdgesNetwork.put(e.getKey(), e);
		}
// setting adjacent edges

		Map<Integer,ArrayList<Edge>> adj= new HashMap<>();

		for(Node n:listNodes.values()) {
			ArrayList<Edge> ed= new ArrayList<>();
			for(Node nd:listNodes.values()) {
				String key=n.getId()+","+nd.getId();
				if(copyEdgesNetwork.containsKey(key)) {
				Edge e=copyEdgesNetwork.get(key);
				ed.add(e);}
			}
			adj.put(n.getId(), ed);
		}





		for (Edge e : copyEdgesNetwork.values()) {
			Edge ed= new Edge(listNodes.get(e.getOrigin().getId()),listNodes.get(e.getEnd().getId()));
			copyAerialNetwork.add(ed);
		}
		int maxIndex = max + 1;
		//copyAerialNetwork.sort(Edge.minDistance);
		Collections.shuffle(copyAerialNetwork,new Random(123));

		Edge result[] = new Edge[copyAerialNetwork.size()]; // Tnis will store the resultant MST
		int e = 0; // An index variable, used for result[]
		int i = 0;

		Subset subsets[] = new Subset[maxIndex];
		for (i = 0; i <= max; ++i) {
			subsets[i] = new Subset();
		}

		for (i = 0; i <= max; ++i) {
			subsets[i].parent = i;
			subsets[i].rank = 0;
		}

		i = 0;

		ArrayList<Edge> xx= new ArrayList<>();
		for (Edge ex : nt) { // 1. network
			Edge newEdge = new Edge(ex);
			xx.add(newEdge);
		}

		//new DrawingNetwork(xx,test);


		for (e = 0; e < copyAerialNetwork.size(); e++)
		// while ( e< this.nodes.length - 1)
		{
			// Step 2: Pick the smallest edge. And increment
			// the index for next iteration
			Edge next_edge = copyAerialNetwork.get(e);

			int x = -1;
			int y = -1;
			x = find(subsets, next_edge.getOrigin().getId());
			y = find(subsets, next_edge.getEnd().getId());
			// If including this edge does't cause cycle,
			// include it in result and increment the index
			// of result for next edge
			if (x != y && x != -1 && y != -1) {
				result[e] = next_edge;
				Union(subsets, x, y);
			}
			// Else discard the next_edge
		}

		xx= new ArrayList<>();
		for (Edge ex : nt) { // 1. network
			Edge newEdge = new Edge(ex);
			xx.add(newEdge);
		}

		//new DrawingNetwork(xx,test);



		HashMap<String, Edge> connectedEdges = new HashMap<String, Edge>();
		HashMap<Integer, Node> nodeInNetwork = new HashMap<Integer, Node>();
		for (i = 0; i < result.length; i++) {
			if (result[i] != null) {
				connectedEdges.put(result[i].getKey(), result[i]);
				String key=result[i].getEnd().getId()+","+result[i].getOrigin().getId();
				Edge ed= new Edge(result[i].getEnd(),result[i].getOrigin());
				connectedEdges.put(key, ed);
				nodeInNetwork.put(result[i].getOrigin().getId(), result[i].getOrigin());
				nodeInNetwork.put(result[i].getEnd().getId(), result[i].getEnd());
				// if(this.connectedNodestoRevealedRoadNetwork.containsKey(result[i].getOrigin().getId())
				// ||
				// this.connectedNodestoRevealedRoadNetwork.containsKey(result[i].getEnd().getId())
				// ) {
				edgesList.put(result[i].getOrigin().getId(), result[i].getOrigin());
				edgesList.put(result[i].getEnd().getId(), result[i].getEnd());
				System.out.println(result[i].getOrigin().getId() + " -- " + result[i].getEnd().getId() + " == "
						+ result[i].getDistance());
				// }

			}

		}

		LinkedList<Node> nodescon = new LinkedList<Node>();
		tree = new HashMap<String, Edge>();
	   treeNodes = new HashMap<Integer, Node>();
		if(nodeInNetwork.containsKey(origin)) {
		nodescon.add(new Node(nodeInNetwork.get(origin)));


		//new Node(nodeInNetwork.get(ed.getOrigin().getId()))
		int iter = 0;
		int nodessInserted = 1;
		while (iter < nodessInserted) {
			for (Node n : nodescon) { // the list nodescon is growing in each iteration
				iter++;
				if (n != null) {
					for (Edge ed : adj.get(n.getId())) { // checking the adjacent edges which are contained in the list of connectedEdges
						if (connectedEdges.containsKey(ed.getKey()) && !tree.containsKey(ed.getKey())) {
							tree.put(ed.getKey(), ed);
							treeNodes.put(new Node(nodeInNetwork.get(ed.getOrigin().getId())).getId(), new Node(nodeInNetwork.get(ed.getOrigin().getId())));
							treeNodes.put(new Node(nodeInNetwork.get(ed.getEnd().getId())).getId(), new Node(nodeInNetwork.get(ed.getEnd().getId())));
						//	treeNodes.put(ed.getEnd().getId(), ed.getEnd());
							iter = 0; // the variable iter becomes zero because a new element was inserted
						}
					}
				}
			}
			nodescon.clear();

			for (Node nn : treeNodes.values()) {
				nodescon.add(nn);
			}
			if (!tree.isEmpty()) {
				nodessInserted = nodescon.size();
			}

		}
	}
		for (int l = 0; l < maxIndex; l++) {
			for (int m = 0; m < maxIndex; m++) {
				if (l != m) {
					String key = l + "," + m;
					if (connectedEdges.containsKey(key)) {

					}
				}
			}
		}

		for(Edge es:tree.values()) {
			System.out.println(es.getOrigin().getId()+","+es.getEnd().getId());
		}
		return treeNodes;
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

	public int find(Subset subsets[], int i) {
		// find root and make root as parent of i (path compression)
		if (subsets[i].parent != i)
			subsets[i].parent = find(subsets, subsets[i].parent);

		return subsets[i].parent;
	}

	public HashMap<String, Edge> getTree() {
		return tree;
	}



	public Map<Integer, Node> getTreeNodes() {
		return treeNodes;
	}

}

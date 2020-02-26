package alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GFG {
	// par and rank will store the parent
	// and rank of particular node
	// in the Union Find Algorithm
	static int par[], rank[];
	private int v; // total of nodes
	int m;  // total edges
	ArrayList<Integer> g[];
	double deg[];

	public List<Integer> findingallPathsToVictimNode(Map<String, Edge> map, Inputs inputs, ArrayList<Node> nodeList,
			Map<Integer, Node> victimList) {
		m=map.size();
		v=inputs.getNodes().length;

		initAdjList();
		for(Edge e:map.values()) {
			addEdge(e.getOrigin().getId(),e.getEnd().getId(),nodeList);
		}
		  findSpanningTree();

		return null;
	}

	private void findSpanningTree() {
		par = new int[v ];
        rank = new int[v ];

        // Initialising parent of a node
        // by itself
        for (int i = 0; i < v; i++)
            par[i] = i;

        // Variable to store the node
        // with maximum degree
        int max = 0;

        // Finding the node with maximum degree
        for (int i = 0; i < v; i++)
            if (deg[i] > deg[max])
                max = i;

        // Union of all edges incident
        // on vertex with maximum degree
        for (int v : g[max]) {
            System.out.println(max + " " + v);
            union(max, v);
        }

        // Carrying out normal Kruskal Algorithm
        for (int u =0; u < v; u++) {
            for (int v : g[u]) {
                int x = find(u);
                int y = find(v);
                if (x == y)
                    continue;
                union(x, y);
                System.out.println(u + " " + v);
            }
        }

	}

	static void union(int u, int v)
    {
        int x = find(u);
        int y = find(v);
        if (x == y)
            return;
        if (rank[x] > rank[y])
            par[y] = x;
        else if (rank[x] < rank[y])
            par[x] = y;
        else {
            par[x] = y;
            rank[y]++;
        }
    }

	 static int find(int x)
	    {
	        if (par[x] != x)
	            par[x] = find(par[x]);
	        return par[x];
	    }

	private void addEdge(int id, int id2, ArrayList<Node> nodeList ) {
		g[id].add(id2);
		deg[id]=nodeList.get(id).getTypeNode();
		deg[id2]=nodeList.get(id2).getTypeNode();
	}

	private void initAdjList() {
		g = new ArrayList[v];
		for (int i = 0; i < v; i++) {
			g[i] = new ArrayList<>(); }

		deg = new double[v];

	}




}

package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UndirectedGraphCycle {
	private int v; // total of nodes
	private LinkedList<Integer> [] adjList;  // List of adj edges

	public boolean cylces(ArrayList<Edge> Network) {
		LinkedList<Edge> copyAerialNetwork = new LinkedList<Edge>();
		HashMap<Integer, Node> edgesList = new HashMap<Integer, Node>();
		int max = 0;
		for (Edge e : Network) {
			if (max < Math.max(e.getOrigin().getId(), e.getEnd().getId())) {
				max = Math.max(e.getOrigin().getId(), e.getEnd().getId());
			}
			copyAerialNetwork.add(e);

		}
		v = max + 1;
		adjList = new LinkedList[v];

		 for (int i = 0; i <v ; i++) {
             adjList[i] = new LinkedList<>();
         }

		 for (Edge e : copyAerialNetwork) {
			 adjList[e.getOrigin().getId()].addFirst(e.getEnd().getId());
			 adjList[e.getEnd().getId()].addFirst(e.getOrigin().getId());
			}

		 boolean result = isCycle();



	return result;

	}

	public boolean isCycle() {
        boolean result = false;

        //visited array
        boolean[] visited = new boolean[v];
        //do DFS, from each vertex
        for (int i = 0; i <v ; i++) {
            if(visited[i]==false){
                if(isCycleUtil(i, visited, -1)){
                    return true;
                }
            }
        }
        return result;
    }

	boolean isCycleUtil(int currVertex, boolean [] visited, int parent){

        //add this vertex to visited vertex
        visited[currVertex] = true;

        //visit neighbors except its direct parent
        for (int i = 0; i <adjList[currVertex].size() ; i++) {
            int vertex = adjList[currVertex].get(i);
            //check the adjacent vertex from current vertex
            if(vertex!=parent) {
                //if destination vertex is not its direct parent then
                if(visited[vertex]){
                    //if here means this destination vertex is already visited
                    //means cycle has been detected
                    return true;
                }
                else{
                    //recursion from destination node
                    if (isCycleUtil(vertex, visited, currVertex)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

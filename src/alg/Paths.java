package alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Paths {
	private int v; // total of nodes
	private ArrayList<Integer>[] adjList;  // List of adj edges
	List<ArrayList<Integer>> listOfpaths;


	public List<Integer> findingallPathsToVictimNode(Map<String, Edge> map, Inputs inputs,
			ArrayList<Node> nodeList, int origin, int victim) {
		v=inputs.getNodes().length;
//		for(Edge e:map.values()) {
//			if(v<Math.max(e.getOrigin().getId(), e.getEnd().getId())) {
//				v=Math.max(e.getOrigin().getId(), e.getEnd().getId());
//			}
//		}
				initAdjList();

		for(Edge e:map.values()) {
			Edge newEdge=new Edge(e);
			newEdge.setOrigin(new Node(e.getOrigin().getId(), e.getOrigin().getX(), e.getOrigin().getY(),e.getOrigin().getTypeNode()));
			newEdge.setEnd(new Node(e.getEnd().getId(), e.getEnd().getX(), e.getEnd().getY(), e.getEnd().getTypeNode()));

			addEdge(newEdge.getOrigin().getId(),newEdge.getEnd().getId());
		}

		// para cada nodo victima se busca un camino
		listOfpaths = new ArrayList<>();
		List<Integer> listOfLists = new ArrayList<>();
		// selecting the destination point
		listOfLists=printAllPaths(origin, victim);

		System.out.println("Following are all different paths from "+origin+" to "+victim);
		return listOfLists;
	}

	public List<Integer> printAllPaths(int s, int d)
	{
		boolean[] isVisited = new boolean[v];
		ArrayList<Integer> pathList = new ArrayList<>();

		//add source to path[]
		pathList.add(s);

		//Call recursive utility
		List<Integer> listOfLists = new ArrayList<>();
		printAllPathsUtil(s, d, isVisited, pathList,listOfLists);
		return listOfLists;
	}

	// A recursive function to print
	// all paths from 'u' to 'd'.
	// isVisited[] keeps track of
	// vertices in current path.
	// localPathList<> stores actual
	// vertices in the current path
	private void printAllPathsUtil(Integer u, Integer d,boolean[] isVisited, ArrayList<Integer> localPathList,List<Integer> listOfLists) {

		// Mark the current node
		isVisited[u] = true;

		if (u.equals(d))
		{
			ArrayList<Integer> path = new ArrayList<>();

			for(int i:localPathList) {
				listOfLists.add(i);
				path.add(i);
			}
			listOfpaths.add(path);

			System.out.println(localPathList);

			// if match found then no need to traverse more till depth
			isVisited[u]= false;
			return ;
		}

		// Recur for all the vertices
		// adjacent to current vertex
		for (Integer i : adjList[u])
		{
			if (!isVisited[i])
			{
				// store current node
				// in path[]
				localPathList.add(i);
				printAllPathsUtil(i, d, isVisited, localPathList,listOfLists);

				// remove current node
				// in path[]
				localPathList.remove(i);
			}
		}

		isVisited[u] = false;
	}


	private void initAdjList()
	{
		adjList = new ArrayList[v];

		for(int i = 0; i < v; i++)
		{
			adjList[i] = new ArrayList<>();
		}
	}

	public void addEdge(int u, int v)
	{
		// Add v to u's list.
		adjList[u].add(v);
	}




}

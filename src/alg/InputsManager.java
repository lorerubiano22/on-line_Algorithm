package alg;

import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

import java.util.Scanner;


public class InputsManager
{

	/**
	 * Function to read the inputs files (Instances)
	 */
	public static Inputs readInputs(String nodesFilePath)
	{
		Inputs inputs = null;
		try
		{   
			// 1. CREATE ALL NODES AND FILL THE NODES LIST
			FileReader reader = new FileReader(nodesFilePath);
			Scanner in = new Scanner(reader);
			String s = null;
			int k = 0;
			int nLine = 0;
			int nNodes = 0;
			while( in.hasNextLine() )
			{  
				if(!in.hasNext()) {
					in.nextLine();
					continue;
				}
				s = in.next(); 
				if( s.charAt(0) == '#' ) // this is a comment line
					in.nextLine(); // skip comment lines
				else
				{   
					String tokens[] = s.split(";"); //n;numberCustomers
					if(nLine == 0){ // Number of depots + customers
						nNodes = Integer.parseInt(tokens[1]);
						inputs = new Inputs(nNodes);
					}else if (nLine == 1){ // Number of vehicles

						int numVehiches = Integer.parseInt(tokens[1]);
						inputs.setVehNumber(numVehiches);
					}else if (nLine == 2){ // Tmax of travel
						float tMax = Float.parseFloat(tokens[1]);	
						inputs.setDrivingRange(tMax);
					}else{//depots + customers (0->Depot, 1...N-1 -> customers, N-> FinalDepot)

						float x = Float.parseFloat(tokens[0]); 
						float y = Float.parseFloat(tokens[1]);
						double profit = Double.parseDouble(tokens[2]);
						Node node = new Node(k, x, y, profit);
						inputs.getNodes()[k] = node;
						k++;
					}
				}
				nLine++; 
			}
			in.close();

		}
		catch (IOException exception)
		{   System.out.println("Error processing inputs files: " + exception);
		}
		return inputs;
	}


	
	

	/**
	 * Creates the edges
	 */


	public static void generatesetEdgeList(Inputs inputs)
	{ /*all edges are created for considering the aerial distance*/
		int nNodes = inputs.getNodes().length;
		LinkedList<Edge>  edgeList = new LinkedList<Edge>();
		for( int i = 0; i < nNodes - 1; i++ ) // node 0 is the depot
		{
			Node iNode = inputs.getNodes()[i];
			for( int j = 0; j < nNodes; j++ )
			{
				Node jNode = inputs.getNodes()[j];

				Edge ijEdge = new Edge(iNode, jNode);
				ijEdge.setTime(ijEdge.calcTime());
				ijEdge.setDistance(ijEdge.calcDistance());
				Edge jiEdge = new Edge(jNode, iNode);
				jiEdge.setTime(jiEdge.calcTime());
				jiEdge.setDistance(jiEdge.calcDistance());
				// Set inverse edges
				ijEdge.setInverse(jiEdge);
				jiEdge.setInverse(ijEdge);
				edgeList.add(ijEdge);
				edgeList.add(jiEdge);
			}
		}
		inputs.setEdgeList(edgeList);
		

	}





	/*
	 * Creates the list of paired edges connecting node i with the depot,
	 *  i.e., it creates the edges (0,i) and (i,0) for all i > 0.
	 */
	public static void generateDepotEdges(Inputs inputs)
	{   Node[] nodes = inputs.getNodes();
	Node depot = nodes[0]; // depot is always node 0
	// Create diEdge and idEdge, and set the corresponding costs
	int nNodes = inputs.getNodes().length;
	LinkedList<Edge> distanceList = new LinkedList<Edge>();
	for( int i = 1; i < nodes.length - 1; i++ ) // node 0 is depot
	{   Node iNode = nodes[i];
	Edge diEdge = new Edge(depot, iNode);
	iNode.setDiEdge(diEdge);
	diEdge.setTime(Edge.calcTime(depot, iNode));
	diEdge.setDistance(Edge.calcDistance(depot, iNode));
	Edge idEdge = new Edge(iNode, depot);
	iNode.setIdEdge(idEdge);
	idEdge.setTime(Edge.calcTime(iNode, depot));
	idEdge.setDistance(Edge.calcDistance(iNode, depot));
	// Set inverse edges
	distanceList.add(diEdge);
	}
	distanceList.sort(Edge.minTime);
	inputs.setdistanceDepot(distanceList);
	}

	/**
	 * @return geometric center for a set of nodes
	 */
	public static float[] calcGeometricCenter(List<Node> nodesList)
	{
		Node[] nodesArray = new Node[nodesList.size()];
		nodesArray = nodesList.toArray(nodesArray);
		return calcGeometricCenter(nodesArray);
	}

	public static float[] calcGeometricCenter(Node[] nodes)
	{
		// 1. Declare and initialize variables
		float sumX = 0.0F; // sum of x[i]
		float sumY = 0.0F; // sum of y[i]
		float[] center = new float[2]; // center as (x, y) coordinates
		// 2. Calculate sums of x[i] and y[i] for all iNodes in nodes
		Node iNode; // iNode = ( x[i], y[i] )
		for( int i = 0; i < nodes.length; i++ )
		{   iNode = nodes[i];
		sumX = sumX + iNode.getX();
		sumY = sumY + iNode.getY();
		}
		// 3. Calculate means for x[i] and y[i]
		center[0] = sumX / nodes.length; // mean for x[i]
		center[1] = sumY / nodes.length; // mean for y[i]
		// 4. Return center as (x-bar, y-bar)
		return center;
	}








}
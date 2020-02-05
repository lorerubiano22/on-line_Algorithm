package alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Disaster {
	private Random rn;



	private ArrayList<Edge> edgeRoadConnection = null;
	private ArrayList<Edge> edgeRoadNetwork = null;
	private ArrayList<Edge> DisruptedRoadNetwork = null;
	private ArrayList<Edge> DisruptedRoadConnections = null; // disrupted connections
	private HashMap<String, Edge>  DisruptedEdges = null; // disrupted edges
	private ArrayList<Node> nodesDisruption = null; // disrupted nodes


	public Disaster(Network network, Test aTest) {
		System.out.println("size_network:_"+network.getedgeRoadNetwork().size());
		rn = new Random(aTest.getseed());
		edgeRoadConnection= new ArrayList<Edge>();
		edgeRoadNetwork= new ArrayList<Edge>();
		DisruptedEdges= new HashMap<String, Edge>();
		DisruptedRoadConnections= new ArrayList<Edge>();
		for(Edge e:network.getEdgeConnections()) {
			edgeRoadConnection.add(e);
					}
				// Operations in the class
		generationDisruptedEdges(aTest);
		generationDisruptedNetwork(network); // it contains also the procedure to locate the disruptions on the edge
	}


	private void generationDisruptedNetwork( Network network) {
		int [] disruptedIndex= new int[DisruptedEdges.size()];
		DisruptedRoadNetwork= new ArrayList<Edge>();
		nodesDisruption = new ArrayList<Node>();
		int i =0;
		for(Edge e:DisruptedEdges.values()) {
			disruptedIndex[i]= edgeRoadConnection.indexOf(e);
			i++;
		}
		for (Edge e: edgeRoadConnection) {
			if(e.getOrigin().getId()>e.getEnd().getId()) { // checking the just the diagonal
			if(!DisruptedEdges.containsKey(e.getKey())) {
				if(!DisruptedEdges.containsKey(e.getInverseEdge().getKey())) {
					DisruptedRoadConnections.add(e);
					DisruptedRoadConnections.add(e.getInverseEdge());
				}
			}
			else {
				int IndexdisruptedNonodes=rn.nextInt(e.getroadInflexionNode().size()-1);
				e.setDisruptionIndex(IndexdisruptedNonodes); // setting the disrupted node in direction 1
				e.getInverseEdge().setDisruptionIndex(IndexdisruptedNonodes); // setting the disrupted node in direction 2
			nodesDisruption.add(e.getroadInflexionNode().get(IndexdisruptedNonodes));
			}
				for(Edge radomE:e.getInflexionEdge()) {		// setting the road network
				DisruptedRoadNetwork.add(radomE);
			}
		}}
	}


	private void generationDisruptedEdges(Test aTest) {
		float percentageDisruption= aTest.getpercentangeDisruption();
		LinkedList<Edge> copy= new LinkedList<Edge>();
		for(Edge e:edgeRoadConnection) {// copy diagonal matrix of the network
			if(e.getOrigin().getId()>e.getEnd().getId()) {
				copy.add(e);
			}

			/// start to remove  testing the cutting
//			if(e.getOrigin().getId()==7 && e.getEnd().getId()==1) {
//			DisruptedEdges.put(e.getKey(),e);
//			DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==8 && e.getEnd().getId()==7) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==9 && e.getEnd().getId()==7) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==30 && e.getEnd().getId()==24) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==29 && e.getEnd().getId()==24) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==20 && e.getEnd().getId()==19) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
//			if(e.getOrigin().getId()==19 && e.getEnd().getId()==18) {
//				DisruptedEdges.put(e.getKey(),e);
//				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());}
			////end to remove
		}
		int totalDisruptedEdges= (int) Math.floor((copy.size())*percentageDisruption);


		Random rn = new Random(aTest.getseed());
		int IDdisruptedEdges=0;
		/***Reading the network***/
		/**** Manati  ****/

		if(aTest.getInstanceName()=="manati") {
			int[][] Disruptions=new int[22][22];
			for(int i=0;i<22;i++) {
				for(int j=0;j<22;j++) {
					Disruptions[i][j]=0;
				}
			}

			Disruptions[2][7]=1;
			//Disruptions[7][2]=1;
			Disruptions[18][8]=1;
			//Disruptions[8][18]=1;
			Disruptions[18][5]=1;
			//Disruptions[5][18]=1;
			Disruptions[9][8]=1;
			//Disruptions[8][9]=1;
			Disruptions[9][10]=1;
			//Disruptions[10][9]=1;
			Disruptions[9][11]=1;
			//Disruptions[11][9]=1;
			Disruptions[11][12]=1;
			//Disruptions[12][11]=1;
			Disruptions[21][12]=1;
			//Disruptions[12][21]=1;
			Disruptions[17][7]=1;
			//Disruptions[7][17]=1;
		}

		do {
			/*desactivado por el momento*/
			IDdisruptedEdges=rn.nextInt(copy.size()-1);
			Edge e=copy.get(IDdisruptedEdges);
			if(!DisruptedEdges.isEmpty()) {
				if(!DisruptedEdges.containsKey(e.getKey())) {
					DisruptedEdges.put(e.getKey(),e);
					DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());
				}
			}
			else {
				DisruptedEdges.put(e.getKey(),e);
				DisruptedEdges.put(e.getInverseEdge().getKey(),e.getInverseEdge());
			}


		}
		while(DisruptedEdges.size()/2<totalDisruptedEdges);

	}

	/// Getters
	public ArrayList<Edge> getDisruptedRoadNetwork() {
		return DisruptedRoadNetwork;
	}


	public ArrayList<Edge> getDisruptedRoadConnections() {
		return DisruptedRoadConnections;
	}
	public ArrayList<Edge> getedgeRoadConnection() {
		return edgeRoadConnection;
	}

	public HashMap<String, Edge> getDisruptedEdges() {
		return DisruptedEdges;
	}


	public ArrayList<Node> getNodesDisruption() {
		return nodesDisruption;
	}




}

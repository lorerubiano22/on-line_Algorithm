package alg;

import java.io.File;
import java.util.ArrayList;

public class StartTester {
	final static boolean DEBUG=false;
	final static String inputFolder = "inputs";
	final static String outputFolder = "outputs";
	final static String testFolder = "tests";
	final static String fileNameTest = "test2Run.txt";
	//static String fileNameTest ;
	final static String sufixFileNodes = ".txt";
	final static String sufixFileVehicules = "_input_vehicles.txt";
	final static String sufixFileOutput = "_outputs.txt";
	//static DrawingNetwork dr;

	// generating of files
	static String file= "";



	public static void main(String[] args)  {
		//	fileNameTest =args[0] ;

		System.out.println("****  WELCOME TO THIS PROGRAM  ****");
		long programStart = ElapsedTime.systemTime();

		/* 1. GET THE LIST OF TESTS TO RUN FORM "test2run.txt" */

		String testsFilePath = testFolder + File.separator + fileNameTest;
		ArrayList<Test> testsList = TestsManager.getTestsList(testsFilePath);

		/* 2. FOR EACH TEST (instanceName + testParameters) IN THE LIST... */
		int nTests = testsList.size();
		System.out.println("Number of tests to run: " + nTests);
		Test aTest = null;
		for (int k = 0; k < nTests; k++) {
			aTest = testsList.get(k);
			// System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);
			System.out.print("Start test for Network: " + aTest.getInstanceName());
			System.out.println();

			// 2.1 GET THE INSTANCE INPUTS (DATA ON NODES AND VEHICLES)
			// "instanceName.txt" contains data on nodes
			String inputNodesPath = inputFolder + File.separator + aTest.getInstanceName() + sufixFileNodes;

			// Read inputs files (nodes) and construct the inputs object
			Inputs inputs = InputsManager.readInputs(inputNodesPath);

			Network Network = new Network(inputs);
			Network = Network.generateroadNetwork(aTest, inputs);// The information of the network have to be full. This
			// knowledge is a static one
			//validationInformationRoad(Network, aTest);

			file = new String(aTest.getInstanceName() + "_Road_Network_Distances" + "_Seed"
					+ aTest.getseed() + "_P(disruption)_" + aTest.getpercentangeDisruption() + "_" + "Disruptions.txt");
			PrintingFiles.printingNetworkElements(file, Network.getEdgeConnections(), false);

			//DrawingNetwork
			//DrawingNetwork networkPicture= new DrawingNetwork(Network.getEdgeConnections(),aTest);
			DrawingNetwork2.drawingNetwork(Network.getEdgeConnections(),aTest);
			//	networkPicture.depictingNetwork(Network.getEdgeConnections());
			Disaster Event = new Disaster(Network, aTest); // set the disaster conditions and the disrupted road
			UpdateRoadInformation revealedRoadInformation = new UpdateRoadInformation(Network);
			/// validation if the nodes contain all information of the network
			new DrawingNetwork(revealedRoadInformation.getedgeRoadConnection(),aTest);
			//dr=new DrawingNetwork(aTest);
			//dr.drawingNetwork(revealedRoadInformation.getedgeRoadConnection());
			Assessment labelledInformation =new Assessment(revealedRoadInformation, inputs, aTest);// computing labelled network for the initial network
			// validation of the information
			//validationInformationRoad(labelledInformation, aTest);
			file =aTest.getInstanceName()+"_Initial_"+"_Seed_"+aTest.getseed()+"_alpha_"+aTest.getpercentageDistance()+"_"+"ValidateConnectivityWeight.txt";
			PrintingFiles.printingRoadInformation(file,labelledInformation.getRevealedDisruptedRoadConnections());
			PrintingFiles.printingDisruptedRoadNetworkSimulation(Event, inputs, aTest);
			InsertionProcedure insertion=new InsertionProcedure(aTest, Event, revealedRoadInformation, inputs);
			//new Validation(aTest,inputs,insertion,Network,Event,labelledInformation);

		}

		/* 3. END OF PROGRAM */

		System.out.println("****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();

		System.out.println("Total elapsed time = " + ElapsedTime.calcElapsedHMS(programStart, programEnd));
	}


	// To Remove

//	private static void validationInformationRoad(Network network, Test aTest) {
//		// 1. Select the list of nodes
//		ArrayList<Edge> inflexionEdges= new ArrayList<>();
//		for(Node n:network.getNodes()) { //printing the list of adj edges
//			// 1. imprimir los elementos contenidos en los nodos
//			System.out.println("_total_Adj_"+n.getAdjEdgesList().size());
//			for(Edge e:n.getAdjEdgesList()) { // selecting the connection
//
//				for(Edge ed:e.getInflexionEdge()) {// selecting the road network
//					inflexionEdges.add(ed);
//				}
//				new DrawingNetwork(inflexionEdges,aTest);
//			}
//
//			System.out.println(n.getId());
//		}
//
//
//		// // information of the road network
//		inflexionEdges.clear();
//		for(Edge e: network.getEdgeConnections()) {
//			if(e.getOrigin().getId()<e.getEnd().getId()) {
//
//				for(Edge ed:e.getInflexionEdge()) {// selecting the road network
//					inflexionEdges.add(ed);
//				}
//			}
//		}
//		new DrawingNetwork(inflexionEdges,aTest);
//
//
//	}
//
//
//	// To Remove
//	private static void validationInformationRoad(Assessment labelledInformation, Test aTest) {
//		// 1. Select the list of nodes
//		//ArrayList<Edge> inflexionEdges= new ArrayList<>();
//		ArrayList<Edge> inflexionEdges= new ArrayList<>();
//		for(Node n:labelledInformation.getNodeList()) { //printing the list of adj edges
//			// 1. imprimir los elementos contenidos en los nodos
//			if(n.getId()==14) {
//				System.out.println("Stop");
//			}
//			inflexionEdges.clear();
//			System.out.println("_labelled_total_Adj_"+n.getAdjEdgesList().size());
//			for(Edge e:n.getAdjEdgesList()) { // selecting the connection
//				for(Edge ed:e.getInflexionEdge()) {// selecting the road network
//					inflexionEdges.add(ed);
//				}
//			}
//
//			System.out.println(n.getId());
//			new DrawingNetwork(inflexionEdges,aTest);
//		}
//		//new DrawingNetwork(inflexionEdges,aTest);
//
//		// information of the road network
//		inflexionEdges.clear();
//		for(Edge e: labelledInformation.getRevealedDisruptedRoadConnections().values()) {
//			if(e.getOrigin().getId()>e.getEnd().getId()) {
//
//				for(Edge ed:e.getInflexionEdge()) {// selecting the road network
//					inflexionEdges.add(ed);
//				}
//			}
//		}
//		new DrawingNetwork(inflexionEdges,aTest);
//
//		// // information of the road network
//		inflexionEdges.clear();
//		for(Edge e: labelledInformation.getEdgeRoadConnection()) {
//			if(e.getOrigin().getId()<e.getEnd().getId()) {
//
//				for(Edge ed:e.getInflexionEdge()) {// selecting the road network
//					inflexionEdges.add(ed);
//				}
//			}
//		}
//		new DrawingNetwork(inflexionEdges,aTest);
//
//	}

}

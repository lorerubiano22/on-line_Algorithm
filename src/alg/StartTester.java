package alg;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.io.IOException;

import umontreal.iro.lecuyer.rng.LFSR113;
import umontreal.iro.lecuyer.rng.RandomStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class StartTester
{
	final static String inputFolder = "inputs";
	final static String outputFolder = "outputs";
	final static String testFolder = "tests";
	final static String fileNameTest = "test2Run.txt"; 
	final static String sufixFileNodes = ".txt";
	final static String sufixFileVehicules = "_input_vehicles.txt";
	final static String sufixFileOutput = "_outputs.txt";

	public static void main( String[] args )
	{
		ArrayList<Outputs> outList = new ArrayList<Outputs>();
		System.out.println("****  WELCOME TO THIS PROGRAM  ****");
		long programStart = ElapsedTime.systemTime();

		/* 1. GET THE LIST OF TESTS TO RUN FORM "test2run.txt"*/

		String testsFilePath = testFolder + File.separator + fileNameTest;
		ArrayList<Test> testsList = TestsManager.getTestsList(testsFilePath);

		/* 2. FOR EACH TEST (instanceName + testParameters) IN THE LIST... */
		int nTests = testsList.size();
		System.out.println("Number of tests to run: " + nTests);
		Test aTest = null;
		for( int k = 0; k < nTests; k++ )
		{   
			aTest = testsList.get(k);
			//System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);
			System.out.print("Start test for Network: " + aTest.getInstanceName());
			System.out.println();

			// 2.1 GET THE INSTANCE INPUTS (DATA ON NODES AND VEHICLES)
			// "instanceName.txt" contains data on nodes
			String inputNodesPath = inputFolder + File.separator + aTest.getInstanceName() + sufixFileNodes;

			// Read inputs files (nodes) and construct the inputs object
			Inputs inputs = InputsManager.readInputs(inputNodesPath);
			
			Network Network = new Network();
			Network = Network.generateroadNetwork(aTest);// The information of the network have to be full. This knowledge is a static one
			

			
			String Disrup_file= new String(aTest.getInstanceName()+"_Road_Network_Distances"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
			writeLinkedList2(Disrup_file, Network.getEdgeConnections(),false);
			//new DrawingNetwork(Network.getedgeRoadNetwork());
			// set the disaster conditions and the disrupted road network: this information is static does not change over the time
			Disaster Event = new Disaster(Network,aTest);
			
			UpdateRoadInformation revealedRoadInformation= new UpdateRoadInformation(Network);
			//Assessment LabeledNetwork= new Assessment(revealedRoadInformation,inputs); //  Aquí se va a evaluar la conectividad sobre la red conocida hasta el momento. es la evaluadión inicial
			HashMap<Integer, Node> victims= new HashMap<Integer, Node>();
			for(Node n:Network.getNodes()) {
				if(n.getProfit()>1 && n.getId()!=0) {
					victims.put(n.getId(),n);
				}
			}
		new Assessment(revealedRoadInformation,inputs, aTest);
			
			
			
     	new Outputs(Event,inputs, aTest);


			
			 //Disrup_file= new String(aTest.getInstanceName()+"Edges_Disruptions"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
			//writeLinkedList2(Disrup_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadConnections,false);

			//new Assessment(revealedRoadInformation,inputs, aTest);
			
			
		
			//String TV_file= new String(aTest.getInstanceName()+"Network"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
			//writeLinkedList(TV_file, revealedRoadInformation.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadConnections,false);


	InsertionProcedure MS = new InsertionProcedure(aTest,Event,revealedRoadInformation,inputs);



	
			//LabeledNetwork.computingPriorities();















			////////////////////////////////////

			//			
			//			new Optimization(aTest,inputs);
			//			new States();
			//			InputsManager.generateDepotEdges(inputs);
			//			InputsManager.generatesetEdgeList(inputs);
			//			// It is generated an initial condition of the network
			//			States.setNonDirectConnections();
			//			States.generatingStatesNetwork(inputs,aTest);
			//			CPLEXDist MinDist = new CPLEXDist();
			//			CPLEXBenefit MaxBenefit = new CPLEXBenefit();
			//			InsertionAlgorithm MS = new InsertionAlgorithm(aTest,inputs);
			//			States.generatingEvents();
			//			Importances.Conditions();
			//			States.importancesRouting();
			//			
			//			States.UpdatingStatesEdges();
			//			Optimization.inputs.UpdatingEdgesList(Optimization.inputs.edgeList);
			//			
			//
			//
			//
			//			/*Printing inputs*/
			//			String importance_file= new String(Optimization.aTest.getInstanceName()+"_Initial_importances_"+"_P(disruption)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_importance.txt");
			//			Optimization.writeList(importance_file, States.importancesEdges,States.DisruptedNetwork,true);
			//			String TV_file= new String(Optimization.aTest.getInstanceName()+"Times"+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_P(disruption)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_"+"TravelTime.txt");
			//			Optimization.writeList(TV_file, States.TravelTime,States.DisruptedNetwork,false);
			//			
			//			String mix_criterion_file= new String(Optimization.aTest.getInstanceName()+"_Initial_Mix_criterion_"+"_P(disruption)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"Mix_criterion.txt");
			//			Optimization.writeWeigthedCriterion(mix_criterion_file);
			//
			//
			//			/**/
			//			RandomStream stream = new LFSR113(); // L'Ecuyer stream
			//			aTest.setRandomStream(stream);
			//
			//			/********* online System********/
			//
			//			OnlineSistem onlineSol= new OnlineSistem(MinDist,MaxBenefit,inputs,aTest); 
			//			onlineSol.solveMe();
			//			outList.addAll(onlineSol.outList);
			//			new Outputs(inputs,aTest);
			//			Outputs.printSolST(outList);

		}


		/* 3. END OF PROGRAM */
		
		System.out.println("****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();
		
		System.out.println("Total elapsed time = "
				+ ElapsedTime.calcElapsedHMS(programStart, programEnd));
	}



	//	private static void writeLinkedList(String tV_file, LinkedList<Edge> edgeRoadConnection,
	//			 HashMap<String, Edge> disruptedEdges, LinkedList<Edge> disruptedRoadNetwork, boolean b) {
	//
	//		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
	//		try {
	//			PrintWriter bw = new PrintWriter(tV_file);
	//			bw.println("Road_Connections");
	//			for(Edge e:edgeRoadConnection ) {
	//				if(e.getOrigin().getId()>e.getEnd().getId()) {
	//				//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
	//				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getDistance()+"_Distance_Euclidean_"+e.getDistance());
	//			}}
	//
	//			bw.println("Disrupted_Edge");
	//			if(disruptedEdges!=null) {
	//
	//				Iterator hmIterator = disruptedEdges.entrySet().iterator(); 
	//				while (hmIterator.hasNext()) { 
	//					Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
	//					Edge e= disruptedEdges.get(mapElement.getKey());
	//					if(e.getOrigin().getId()>e.getEnd().getId()) {
	//					bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getDistance()+"_Distance_Euclidean_"+e.getDistance());
	//					}
	//				}				}
	//			bw.println("Disrupted_Network");
	//			for(Edge e:disruptedRoadNetwork) {
	//				if(e.getOrigin().getId()>e.getEnd().getId()) {
	//				//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
	//				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getDistance()+"_Distance_Euclidean_"+e.getDistance());
	//				}
	//			}
	//
	//
	//			for(Edge e:disruptedRoadNetwork) {
	//				if(e.getOrigin().getId()>e.getEnd().getId()) {
	//				//bw.println("("+e.getOrigin().getId()+")_Connectivity_"+e.getOrigin().getImportance());
	//				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getDistance()+"_Distance_Euclidean_"+e.getDistance());
	//				}
	//			}
	//			bw.println("("+disruptedRoadNetwork.getLast().getEnd().getId()+")_Connectivity_"+disruptedRoadNetwork.getLast().getEnd().getImportance());
	//			bw.flush();
	//		} 
	//		catch (IOException e) {
	//			//why does the catch need its own curly?
	//		}

	private static void writeLinkedList(String tV_file, LinkedList<Edge> edgeRoadConnection,
			HashMap<String, Edge> disruptedEdges, LinkedList<Edge> disruptedRoadNetwork, boolean b) {

		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			bw.println("Road_Connections");
			for(Edge e:edgeRoadConnection ) {
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
					bw.println(e.getOrigin().toString());
					bw.println(e.getEnd().toString());
				}}

			bw.println("Disrupted_Edge");
			if(disruptedEdges!=null) {

				Iterator hmIterator = disruptedEdges.entrySet().iterator(); 
				while (hmIterator.hasNext()) { 
					Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
					Edge e= disruptedEdges.get(mapElement.getKey());
					if(e.getOrigin().getId()>e.getEnd().getId()) {	
						bw.println(e.getOrigin().toString());
						bw.println(e.getEnd().toString());
						}
				}				}
			bw.println("Disrupted_Network");
			for(Edge e:disruptedRoadNetwork) {
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
					bw.println(e.getOrigin().toString());
					bw.println(e.getEnd().toString());
					}
			}


			for(Edge e:disruptedRoadNetwork) {
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					bw.println(e.getOrigin().toString());
					bw.println(e.getEnd().toString());
					}
			}
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}
		//writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,false);

	}

// main
	
	private static void writeLinkedList2(String tV_file, LinkedList<Edge> edgeRoadConnection, boolean b) {

		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			 HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();// It contains all nodes of the network
			bw.println("Road_Connections");
			for(Edge e:edgeRoadConnection ) {
				nodes.put(e.getOrigin().getId(), e.getOrigin());
				nodes.put(e.getEnd().getId(), e.getEnd());
				int i=0;
				if(e.getOrigin().getId()>e.getEnd().getId()) {
										//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
					bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad());
					//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Distance_Euclidean_"+e.getDistance());
				}}
			bw.println("Node");
			Iterator hmIterator = nodes.entrySet().iterator(); 
			while (hmIterator.hasNext()) { 
				Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
				Node nn= nodes.get(mapElement.getKey());
				if(nn.getProfit()>1 && nn.getId()!=0) {
					bw.println(+nn.getId()+"  "+"_Victim_");
				}
				if(nn.getProfit()>1 && nn.getId()==0) {
					bw.println(+nn.getId()+"  "+"_Depot_");
				}
				if(nn.getProfit()==1 ) {
					bw.println(+nn.getId()+"  "+"_road_Crossing_");
				}
			}

			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}
		//writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,false);

	}





}


package alg;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;

import umontreal.iro.lecuyer.rng.LFSR113;
import umontreal.iro.lecuyer.rng.RandomStream;



public class StartTester
{
	final static String inputFolder = "inputs";
	final static String outputFolder = "outputs";
	final static String testFolder = "tests";
	final static String fileNameTest = "TTT.txt"; 
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
			Network = Network.generateroadNetwork();// The information of the network have to be full. This knowledge is a static one


			// set the disaster conditions and the disrupted road network: this information is static does not change over the time
			Disaster Event = new Disaster(Network,aTest);
			UpdateRoadInformation revealedRoadInformation= new UpdateRoadInformation(Network);
			Assessment LabeledNetwork= new Assessment(revealedRoadInformation,inputs); //  Aquí se va a evaluar la conectividad sobre la red conocida hasta el momento. es la evaluadión inicial

			//InsertionAlgorithm MS = new InsertionAlgorithm(aTest,inputs);



			String Disrup_file= new String(aTest.getInstanceName()+"Disruptions"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
			writeLinkedList(Disrup_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadConnections,false);

			String TV_file= new String(aTest.getInstanceName()+"Network"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
			writeLinkedList(TV_file, revealedRoadInformation.edgeRoadConnection,revealedRoadInformation.revealedDisruptedRoadNetwork,Event.DisruptedRoadConnections,false);

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
			//			Outputs.printSol(outList);
		}


		/* 3. END OF PROGRAM */
		System.out.println("****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();
		System.out.println("Total elapsed time = "
				+ ElapsedTime.calcElapsedHMS(programStart, programEnd));
	}



	private static void writeLinkedList(String tV_file, LinkedList<Edge> edgeRoadConnection,
			LinkedList<Edge> disruptedEdges, LinkedList<Edge> disruptedRoadNetwork, boolean b) {

		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			bw.println("Road_Connections");
			for(Edge e:edgeRoadConnection ) {
				//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity());
			}

			bw.println("Disrupted_Edge");
			for(Edge e:disruptedEdges ) {
				//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity());
			}
			bw.println("Disrupted_Network");
			for(Edge e:disruptedRoadNetwork) {
				//bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Time_"+e.getTime());
				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity());

			}
			
			
			for(Edge e:disruptedRoadNetwork) {
				//bw.println("("+e.getOrigin().getId()+")_Connectivity_"+e.getOrigin().getImportance());
				bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_Connectivity_"+e.getConnectivity());

			}
			bw.println("("+disruptedRoadNetwork.getLast().getEnd().getId()+")_Connectivity_"+disruptedRoadNetwork.getLast().getEnd().getImportance());
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}


		//writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,false);

	}







}


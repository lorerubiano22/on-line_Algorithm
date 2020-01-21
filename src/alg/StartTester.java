package alg;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class StartTester {
	final static String inputFolder = "inputs";
	final static String outputFolder = "outputs";
	final static String testFolder = "tests";
	final static String fileNameTest = "test2Run.txt";
	final static String sufixFileNodes = ".txt";
	final static String sufixFileVehicules = "_input_vehicles.txt";
	final static String sufixFileOutput = "_outputs.txt";

	public static void main(String[] args) {

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

			String Disrup_file = new String(aTest.getInstanceName() + "_Road_Network_Distances" + "_Seed"
					+ aTest.getseed() + "_P(disruption)_" + aTest.getpercentangeDisruption() + "_" + "Disruptions.txt");
			writeLinkedList2(Disrup_file, Network.getEdgeConnections(), false);
			new DrawingNetwork(Network.getedgeRoadNetwork());

			Disaster Event = new Disaster(Network, aTest); // set the disaster conditions and the disrupted road
			UpdateRoadInformation revealedRoadInformation = new UpdateRoadInformation(Network);
			new Assessment(revealedRoadInformation, inputs, aTest);// computing labelled network for the initial network
			new Outputs(Event, inputs, aTest); // printing the initial conditions
			new InsertionProcedure(aTest, Event, revealedRoadInformation, inputs);

		}

		/* 3. END OF PROGRAM */

		System.out.println("****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();

		System.out.println("Total elapsed time = " + ElapsedTime.calcElapsedHMS(programStart, programEnd));
	}

	private static void writeLinkedList2(String tV_file, LinkedList<Edge> edgeRoadConnection, boolean b) {
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();// It contains all nodes of the network
			bw.println("Road_Connections");
			for (Edge e : edgeRoadConnection) {
				nodes.put(e.getOrigin().getId(), e.getOrigin());
				nodes.put(e.getEnd().getId(), e.getEnd());
				if (e.getOrigin().getId() > e.getEnd().getId()) {
					bw.println(+e.getOrigin().getId() + "  " + e.getEnd().getId() + "_Distance_" + e.getDistance()
							+ "_road_Distance_" + e.getDistanceRoad());
				}
			}
			bw.println("Node");
			for (Node nn : nodes.values()) {
				if (nn.getProfit() > 1 && nn.getId() != 0) {
					bw.println(+nn.getId() + "  " + "_Victim_");
				}
				if (nn.getProfit() > 1 && nn.getId() == 0) {
					bw.println(+nn.getId() + "  " + "_Depot_");
				}
				if (nn.getProfit() == 1) {
					bw.println(+nn.getId() + "  " + "_road_Crossing_");
				}
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// why does the catch need its own curly?
		}

	}

}

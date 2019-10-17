package alg;
import java.io.File;
import java.util.ArrayList;
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
			System.out.print("Start test for Instance: " + aTest.getInstanceName());
			System.out.println();

			// 2.1 GET THE INSTANCE INPUTS (DATA ON NODES AND VEHICLES)
			// "instanceName.txt" contains data on nodes
			String inputNodesPath = inputFolder + File.separator + aTest.getInstanceName() + sufixFileNodes;

			// Read inputs files (nodes) and construct the inputs object
			Inputs inputs = InputsManager.readInputs(inputNodesPath);
			new Optimization(aTest,inputs);
			new States();
			InputsManager.generateDepotEdges(inputs);
			InputsManager.generatesetEdgeList(inputs);
			// It is generated an initial condition of the network
			States.setNonDirectConnections();
			States.generatingStatesNetwork(inputs,aTest);
			CPLEXDist MinDist = new CPLEXDist();
			CPLEXBenefit MaxBenefit = new CPLEXBenefit();
			InsertionAlgorithm MS = new InsertionAlgorithm(aTest,inputs);
			States.generatingEvents();
			Importances.Conditions();
			States.importancesRouting();
			
			States.UpdatingStatesEdges();
			Optimization.inputs.UpdatingEdgesList(Optimization.inputs.edgeList);
			



			/*Printing inputs*/
			String importance_file= new String(Optimization.aTest.getInstanceName()+"_Initial_importances_"+"_P(disruption)_"+Optimization.aTest.getprobDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_importance.txt");
			Optimization.writeList(importance_file, States.importancesEdges,States.DisruptedNetwork,true);
			String TV_file= new String(Optimization.aTest.getInstanceName()+"Times"+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_P(disruption)_"+Optimization.aTest.getprobDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_"+"TravelTime.txt");
			Optimization.writeList(TV_file, States.TravelTime,States.DisruptedNetwork,false);
			
			String mix_criterion_file= new String(Optimization.aTest.getInstanceName()+"_Initial_Mix_criterion_"+"_P(disruption)_"+Optimization.aTest.getprobDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"Mix_criterion.txt");
			Optimization.writeWeigthedCriterion(mix_criterion_file);


			/**/
			RandomStream stream = new LFSR113(); // L'Ecuyer stream
			aTest.setRandomStream(stream);

			/********* online System********/

			OnlineSistem onlineSol= new OnlineSistem(MinDist,MaxBenefit,inputs,aTest); 
			onlineSol.solveMe();
			outList.addAll(onlineSol.outList);
			new Outputs(inputs,aTest);
			Outputs.printSolST(outList);
			Outputs.printSol(outList);
		}


		/* 3. END OF PROGRAM */
		System.out.println("****  END OF PROGRAM, CHECK OUTPUTS FILES  ****");
		long programEnd = ElapsedTime.systemTime();
		System.out.println("Total elapsed time = "
				+ ElapsedTime.calcElapsedHMS(programStart, programEnd));
	}







}


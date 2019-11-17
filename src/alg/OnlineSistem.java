package alg;

import java.util.ArrayList;
import java.util.LinkedList;


public class OnlineSistem {
	Inputs initialInputs;
	Test test;
	ArrayList<Outputs> outList = new ArrayList<Outputs>();
	Outputs outputs;
	Optimization OptParameters;
	CPLEXDist MinDist;
	CPLEXBenefit MaxBenefit;
	//InsertionAlgorithm MS ;
	//InsertionAlgorithm MS ;
	Solution Max_CPLEX;
	Solution Min_CPLEX;
	Solution Max_MS;
	InsertionAlgorithm_go_ahead MS_InsertionAlgorithm_go_ahead=null;
	InsertionAlgorithm_go_back MS_InsertionAlgorithm_go_back=null;
	InsertionAlgorithm_Jumping MS_InsertionAlgorithm_Jumping= null;
	static int maxIter;
	private ArrayList<Solution> CPLEX_MinDistsol = null;
	private ArrayList<Solution> CPLEX_MaxBenefit = null;
	private ArrayList<Solution> MS_MaxBenefit = null;


	public OnlineSistem(CPLEXDist minDist, CPLEXBenefit maxBenefit,Inputs inputs,Test aTest) {
		initialInputs=inputs;
		test=aTest;
		CPLEX_MinDistsol = new ArrayList<Solution>();
		CPLEX_MaxBenefit = new ArrayList<Solution>();
		MS_MaxBenefit = new ArrayList<Solution>();
		MinDist=minDist;
		MaxBenefit=maxBenefit;
		outputs = new Outputs(Optimization.inputs,Optimization.aTest);
		new Optimization(Optimization.aTest,Optimization.inputs);	
	}

	public void solveMe() {
		//MS=new InsertionAlgorithm(Optimization.aTest,Optimization.inputs);

		long start; //starting point: operation time
		double elapsed = 0.0;  //ending point: operation time
		/*** Creating solutions object ***/
		Solution temporal_Sol_MS=new Solution(); // temporal solution obtained by the insertion procedure in each iteration
		Solution temporal_Sol_Max=new Solution(); // solution obtained by CPLEX maximizing the importance edges 
		Solution temporal_Sol_Min=new Solution(); // solution obtained by CPLEX minimizing the importance edges 
		Solution byInsertion_Sol=new Solution(); // final solution by the insertion procedure
		Solution Jumping_Sol=new Solution();
		Solution temporal_Sol_Jumping=new Solution(); // temporal solution obtained by the insertion procedure in each iteration
		
		Solution Back_Sol=new Solution();
		Solution temporal_Sol_Back=new Solution(); // temporal solution obtained by the insertion procedure in each iteration
		
		Solution Risky_Sol=new Solution();
		Solution temporal_Sol_Risk=new Solution(); // temporal solution obtained by the insertion procedure in each iteration

		/************************************************************/		


		/*** Printing disruptions***/
		String disruptionList_file= new String(Optimization.aTest.getInstanceName()+"_Disruptions"+"_Percentage(disruptions)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_importance.txt");
		double [][] Disruptions= removedTimes(States.Disruptions);
		int [][] CoveDisruptions=removedEdges(States.Disruptions);
		Optimization.writeDisruptions(disruptionList_file);
		/************************************************************/	
		/**Jumping strategy**/
		String Strategy= "Jumping";
		solvingsStrategies( Strategy,Jumping_Sol,temporal_Sol_Jumping);
		
		/**Conservative strategy**/
		Strategy= "Conservative";
		solvingsStrategies(Strategy,Back_Sol,temporal_Sol_Back);
		
		/**Risky strategy**/
		Strategy= "Risky";
		solvingsStrategies(Strategy,Risky_Sol,temporal_Sol_Risk);


		//MS_MaxBenefit.add(interations,byInsertion_Sol);
		outputs.addSoltoArray(Jumping_Sol);
		outputs.addSoltoArray(Back_Sol);
		outputs.addSoltoArray(Risky_Sol);
		/*The solution per iteration is stored*/
		outputs.setJumping(Jumping_Sol);
		outputs.setBack(Back_Sol);
		outputs.setAhead(Risky_Sol);
		outputs.setMin_CPLEXSol(temporal_Sol_Min);
		//outputs.setMax_CPLEX_SolSol(CPLEX_MaxBenefit.get(interations));
		outList.add(outputs);
	}




	private void solvingsStrategies(String Strategy, Solution Jumping_Sol, Solution temporal_Sol_MS) {
		new States();
		States.setNonDirectConnections();
					States.generatingStatesNetwork(Optimization.inputs,Optimization.aTest);
					States.generatingEvents();
					Importances.Conditions();
					States.importancesRouting();
					
					States.UpdatingStatesEdges();
					Optimization.inputs.UpdatingEdgesList(Optimization.inputs.edgeList);
		long start; //starting point: operation time
		double elapsed = 0.0;  //ending point: operation time

		int iterations=1;
		int insertions=1;
		maxIter=1;

		while((iterations)<=maxIter) {
			/*The computational time is limited per iteraton*/
			System.out.println("START	/******INSERTION PROCESS*******/   " +iterations);
			/******Setting the list of solution per iteration*******/


			/************************************************************/

			/******Routing process*******/
			//InsertionAlgorithm_go_ahead MS=new InsertionAlgorithm_go_ahead(Optimization.aTest,Optimization.inputs,temporal_Sol_MS);
			//InsertionAlgorithm_go_back MS=new InsertionAlgorithm_go_back(Optimization.aTest,Optimization.inputs,temporal_Sol_MS);
			//InsertionAlgorithm_Jumping
			start=ElapsedTime.systemTime(); //starting point: operation time
			//temporal_Sol_MS=MS.solveMe();
			if(Strategy== "Jumping") {
				MS_InsertionAlgorithm_Jumping=new InsertionAlgorithm_Jumping(Optimization.aTest,Optimization.inputs,temporal_Sol_MS);
				temporal_Sol_MS=MS_InsertionAlgorithm_Jumping.solveMe();
				elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
				MS_InsertionAlgorithm_Jumping.setTimesPC(elapsed);}

			if(Strategy== "Conservative") {
				MS_InsertionAlgorithm_go_back= new InsertionAlgorithm_go_back(Optimization.aTest,Optimization.inputs,temporal_Sol_MS);
				temporal_Sol_MS=MS_InsertionAlgorithm_go_back.solveMe();
				elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
				MS_InsertionAlgorithm_go_back.setTimesPC(elapsed);}


			if(Strategy== "Risky") {
				MS_InsertionAlgorithm_go_ahead= new InsertionAlgorithm_go_ahead(Optimization.aTest,Optimization.inputs,temporal_Sol_MS);
				temporal_Sol_MS=MS_InsertionAlgorithm_go_ahead.solveMe();
				elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
				MS_InsertionAlgorithm_go_ahead.setTimesPC(elapsed);}

			elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
			//MS.setTimesPC(elapsed);
			/************************************************************/

			/******Updating information*******/
			/*The missing edges are updated according to the visit of the customer*/
			States.uptadatingStatesNetwork(temporal_Sol_MS,States.Disruptions);
			Importances.Conditions();
			States.uptadatingSImportancesNetwork(temporal_Sol_MS);
			States.UpdatingStatesEdges();
			Optimization.inputs.UpdatingEdgesList(Optimization.inputs.edgeList);

			/************************************************************/

			String importance_file= new String(Optimization.aTest.getInstanceName()+"_"+Strategy+"_Weights_after_insertion_"+insertions+"_P(disruption)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_Weight_criterion.txt");
			Optimization.writeList(importance_file, States.importancesEdges,States.DisruptedNetwork,true);

			/*Mix criterion*/

			String mix_criterion_file= new String(Optimization.aTest.getInstanceName()+"_"+Strategy+"_Score_after_insertion_"+insertions+"_P(disruption)_"+Optimization.aTest.getpercentangeDisruption()+"_Seed_"+Optimization.aTest.getseed()+"_OptCriterion"+Optimization.aTest.getOptcriterion()+"_Score_criterion.txt");
			Optimization.writeWeigthedCriterion(mix_criterion_file);
			iterations++;
			insertions++;
			System.out.println("end	/******insertion*******/");

			if(maxIter==0) {
				int n=Optimization.inputs.getNodes().length;
				Node depot= Optimization.inputs.getNode(0);
				Edge lastEdge;
				if(temporal_Sol_MS.getRoutes().size()!=0){
					Edge prevlast=temporal_Sol_MS.getRoutes().get(0).getEdges().get(temporal_Sol_MS.getRoutes().get(0).getEdges().size()-1);
					lastEdge=Optimization.inputs.getedge(prevlast.getEnd().getId(),depot.getId());
					prevlast=temporal_Sol_MS.getRoutes().get(0).getEdges().get(temporal_Sol_MS.getRoutes().get(0).getEdges().size()-1);
					lastEdge=Optimization.inputs.getedge(prevlast.getEnd().getId(),depot.getId());

					temporal_Sol_MS.getRoutes().get(0).getEdges().add(lastEdge);}
				else {
					lastEdge=Optimization.inputs.getedge(depot.getId(),depot.getId());
					Route fake=new Route();
					fake.getEdges().add(lastEdge);
					fake.calcTime();
					fake.calcDistance();
					//auxRoute.calcCoverage(Optimization.inputs);
					fake.setCoverage(0);
					temporal_Sol_MS.addRoute(fake);
					temporal_Sol_MS.updatingSolutionAttributes(Optimization.inputs);
					temporal_Sol_MS.getRoutes().get(0).getEdges().add(lastEdge);
				}
			}
			else {iterations=1;}

			/*Setting the last solutions*/
			double runtime=0;
			
			if(Strategy== "Jumping") {runtime=MS_InsertionAlgorithm_Jumping.getTimesPC();}
			if(Strategy== "Conservative") {runtime=MS_InsertionAlgorithm_go_back.getTimesPC();}
			if(Strategy== "Risky") {runtime=MS_InsertionAlgorithm_go_ahead.getTimesPC();}	
			
			SettingSolution(Jumping_Sol,temporal_Sol_MS,runtime,Strategy);


		}

	}

	private void SettingSolution(Solution final_Sol,Solution temporal_Sol_MS, double timesPC, String a) {
		final_Sol.getRoutes().add(temporal_Sol_MS.getRoutes().get(0));
		final_Sol.updatingSolutionAttributes(Optimization.inputs);
		final_Sol.settypeSol(a);
		final_Sol.setSolveconditions(Optimization.TravelTime, Optimization.imp,Optimization.cover);
		final_Sol.setprobDisruption(Optimization.aTest.getpercentangeDisruption());
		final_Sol.setoptCriterion(Optimization.aTest.getOptcriterion());
		final_Sol.setsol_typeNetwork(Optimization.aTest.gettypeNetwork());
		final_Sol.solCondition(Optimization.inputs, Optimization.aTest);
		final_Sol.setPCTime(timesPC);

	}

	private int[][] removedEdges(int[][] cover) {
		int [][] CoveDisruptions= new int[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length];
		for(int i=0;i<cover.length;i++) {
			for(int j=0;j<cover.length;j++) {
				if(i!=j ) {
					if(cover[i][j]!=1) {
						System.out.println(Optimization.inputs.getNode(j).getProfit());
						CoveDisruptions[i][j]=1;
						if(i==0 && Optimization.inputs.getNode(j).getProfit()>1 ||  Optimization.inputs.getNode(j).getId()==0 && Optimization.inputs.getNode(i).getProfit()>1) {
							CoveDisruptions[i][j]=0;
						}
						else{CoveDisruptions[i][j]=1;}
					}
				}
			}
		}
		return CoveDisruptions;
	}

	private double[][] removedTimes(int[][] disruptions2) {
		double [][] Disruptions=new double[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length];
		/*Detecting ONLY disruptions*/
		for(int i=0;i<disruptions2.length;i++) {
			for(int j=0;j<disruptions2.length;j++) {
				if(disruptions2[i][j]!=1) {
					if(i!=j) {
						Disruptions[i][j]=Optimization.inputs.getedge(i, j).getTime();
					}
				}
			}
		}


		return Disruptions;
	}

	public static void setmaxIter(int n) {
		maxIter=n;
	}


}

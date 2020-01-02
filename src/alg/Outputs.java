package alg;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;



public class Outputs
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private String instanceName;
	private Solution bestInitSol; // saving the sol with routes per iteration
	private Solution Min_CPLEX_Sol;
	private Solution Max_CPLEX_Sol;
	private static JumpingMovement Jumping_Strategy;
	private static BackandForwardMovement Back_Strategy;
	private Solution Back_Sol;
	private Solution Ahead_Sol;
	private float runningTime;
	private ArrayList<Outputs> list = null;
	private ArrayList<Solution> finalsol = new ArrayList<Solution>(); // saving all solution from the iterations
	private static Inputs inputs;
	private static Test test;


	public void setList(){
		list.add(this);
	}


	public Outputs()
	{   
		String instanceName;
		JumpingMovement Jumping_Strategy;
		BackandForwardMovement Back_Strategy;
		Solution Back_Sol;
		Solution Ahead_Sol;
		float runningTime;
		ArrayList<Outputs> list = null;
		ArrayList<Solution> finalsol = new ArrayList<Solution>(); // saving all solution from the iterations
		Inputs inputs;
		Test test;
	}

	/* SET METHODS */

	public Outputs(Inputs inp, Test t) {
		test=t;
		instanceName=t.getInstanceName();
		inp=inputs;
		new Outputs();
	}


	// Max_CPLEX_Sol
	public void setBestInitSol(Solution aSol){bestInitSol = aSol;}
	public void setMin_CPLEXSol(Solution obSol){Min_CPLEX_Sol = obSol;}
	public void setMax_CPLEX_SolSol(Solution obSol){Max_CPLEX_Sol = obSol;}
	public void setJumping(JumpingMovement obSol){Jumping_Strategy = obSol;
	printJumpSol();}
	public void setBack(BackandForwardMovement obSol){Back_Strategy = obSol;
	 printBackSol();}
	public void setAhead(Solution obSol){Ahead_Sol = obSol;}
	public void setRunningT(float t){runningTime = t;}
	public void addSoltoArray (Solution solList){finalsol.add(solList); }


	/* GET METHODS */

	public Solution getBestInitSol(){return bestInitSol;}
	public Solution getMin_CPLEX_SolSol(){return Min_CPLEX_Sol;}
	public Solution getMax_CPLEX_Sol(){return  Max_CPLEX_Sol;}
	public JumpingMovement getJumping(){return Jumping_Strategy;}
	public BackandForwardMovement getBack(){return Back_Strategy;}
	public Solution getAhead(){return Ahead_Sol;}
	public float getRunningT(){return runningTime;}
	public ArrayList<Solution> getFinalsol() {return finalsol;}
	
	
	
	public static void printJumpSol(){
		/*
		 * Script with routes per instance
		 */
		try 
		{   
			//for(Outputs o : list){
				//for(Solution sol1 : o.finalsol){
				//{
					//Solution sol = o.getMin_CPLEX_SolSol();
					//Solution sol1 = o.getBestInitSol();
					if(Jumping_Strategy.jump_Sol!=null) {
						String file_name= new String(Jumping_Strategy.aTest.getInstanceName()+"_SOLUTION_"+"_p(disruption)_"+Jumping_Strategy.aTest.getpercentangeDisruption()+"_seed_"+Jumping_Strategy.aTest.getseed()+"_Strategy_Jumping_OptCriterion_"+Jumping_Strategy.aTest.getOptcriterion()+"_Output.txt");
						PrintWriter out = new PrintWriter(file_name);
						out.printf("*********************************\n");
						//out.printf("******solution Min_Distance****");
						out.printf("*********************************\n");

						//out.print(sol.toString());
						//out.printf("runTime	%f\n",sol.getTime());
						out.printf("*********************************\n");
						out.printf("      SOLUTION: MAXIMIZING CRITERION     \n");
						out.printf("*********************************\n");
						out.printf("*********************************\n");
						out.println("\n");
						out.print(Jumping_Strategy.jump_Sol.toString());
						out.println("\n");
						out.printf("      unreachable victims     \n");
						Iterator hmIterator = Jumping_Strategy.VictimList.entrySet().iterator(); 
						while (hmIterator.hasNext()) { 
							Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
							Node v= Jumping_Strategy.VictimList.get(mapElement.getKey());
							if(!Jumping_Strategy.connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
								out.print(v.toString());
								out.println("\n");
							}
							
						}
						out.close();	
					//}}
				}
			

		}
		catch(IOException exception){
			System.out.println("Error processing output file: " + exception);
		}
	}//end method
	
	
	
	public static void printBackSol(){
		/*
		 * Script with routes per instance
		 */
		try 
		{   
			//for(Outputs o : list){
				//for(Solution sol1 : o.finalsol){
				//{
					//Solution sol = o.getMin_CPLEX_SolSol();
					//Solution sol1 = o.getBestInitSol();
					if(Back_Strategy.back_Sol!=null) {
						String file_name= new String(Back_Strategy.aTest.getInstanceName()+"_SOLUTION_"+"_p(disruption)_"+Back_Strategy.aTest.getpercentangeDisruption()+"_seed_"+Back_Strategy.aTest.getseed()+"_Strategy_BackandForward_OptCriterion_"+Back_Strategy.aTest.getOptcriterion()+"_Output.txt");
						PrintWriter out = new PrintWriter(file_name);
						out.printf("*********************************\n");
						//out.printf("******solution Min_Distance****");
						out.printf("*********************************\n");

						//out.print(sol.toString());
						//out.printf("runTime	%f\n",sol.getTime());
						out.printf("*********************************\n");
						out.printf("      SOLUTION: MAXIMIZING CRITERION     \n");
						out.printf("*********************************\n");
						out.printf("*********************************\n");
						out.println("\n");
						out.print(Back_Strategy.back_Sol.toString());
						out.println("\n");
						out.printf("      unreachable victims     \n");
						Iterator hmIterator = Back_Strategy.VictimList.entrySet().iterator(); 
						while (hmIterator.hasNext()) { 
							Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
							Node v= Back_Strategy.VictimList.get(mapElement.getKey());
							if(!Back_Strategy.connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
								out.print(v.toString());
								out.println("\n");
							}
						}
						//out.printf("runTime	%f",sol1.getPCTime());
						//out.println();
						out.close();	
					//}}
				}
			

		}
		catch(IOException exception){
			System.out.println("Error processing output file: " + exception);
		}
	}//end method




}
package alg;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Outputs
{
	/* INSTANCE FIELDS & CONSTRUCTOR */
	private String instanceName;
	private Solution bestInitSol; // saving the sol with routes per iteration
	private Solution Min_CPLEX_Sol;
	private Solution Max_CPLEX_Sol;
	private Solution Jumping_Sol;
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
	{   bestInitSol = null;
	Min_CPLEX_Sol = null;
	Max_CPLEX_Sol = null;
	Jumping_Sol= null;
	Back_Sol= null;
	Ahead_Sol= null;
	runningTime = 0;
	finalsol = new ArrayList<Solution>();
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
	public void setJumping(Solution obSol){Jumping_Sol = obSol;}
	public void setBack(Solution obSol){Back_Sol = obSol;}
	public void setAhead(Solution obSol){Ahead_Sol = obSol;}
	public void setRunningT(float t){runningTime = t;}
	public void addSoltoArray (Solution solList){finalsol.add(solList); }


	/* GET METHODS */

	public Solution getBestInitSol(){return bestInitSol;}
	public Solution getMin_CPLEX_SolSol(){return Min_CPLEX_Sol;}
	public Solution getMax_CPLEX_Sol(){return  Max_CPLEX_Sol;}
	public Solution getJumping(){return Jumping_Sol;}
	public Solution getBack(){return Back_Sol;}
	public Solution getAhead(){return Ahead_Sol;}
	public float getRunningT(){return runningTime;}
	public ArrayList<Solution> getFinalsol() {return finalsol;}
	
	
	
	public static void printSol(ArrayList<Outputs> list){
		/*
		 * Script with routes per instance
		 */
		try 
		{   
			for(Outputs o : list){
				for(Solution sol1 : o.finalsol){
				{
					//Solution sol = o.getMin_CPLEX_SolSol();
					//Solution sol1 = o.getBestInitSol();
					if(sol1!=null) {
						String file_name= new String(sol1.getTestCondition().getInstanceName()+"_SOLUTION_"+"_p(disruption)_"+sol1.getprobDisruption()+"_seed_"+sol1.getTestCondition().getseed()+"_Strategy_"+sol1.typeSol+"_OptCriterion_"+sol1.getoptCriterion()+"_Output.txt");
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
						out.print(sol1.toString());
						//out.println("\n");
						//out.printf("runTime	%f",sol1.getPCTime());
						//out.println();
						out.close();	
					}}
				}
			}

		}
		catch(IOException exception){
			System.out.println("Error processing output file: " + exception);
		}
	}//end method


	public static void printSolST(ArrayList<Outputs> list){
		try 
		{   
			PrintWriter out = new PrintWriter("ResumeSols.txt");
			out.printf("Instance   strategy     optCriteria   p(disruption)  seed    TravelTime      TravelDistance     visitedVictims");
			for(Outputs o : list){
				//Solution sol1 = o.getMin_CPLEX_SolSol();
//				getAhead()
//				getBack
//				getJumping
				for(Solution sol2 : o.finalsol){
					if(sol2!=null) {
						//Solution sol2 = o.getMax_CPLEX_Sol();
						out.println();
						String a= new String();
//						if(o.getMin_CPLEX_SolSol()!=null || o.getMax_CPLEX_Sol()!=null) {
//							a= new String(sol2.gettypeSol());
//						}
						//out.printf("%s  %s   %s	 %2f  %2f	%2f	%2f	%2f", o.instanceName,a, sol1.getsol_typeNetwork(),sol1.getprobDisruption(),(float)sol1.getTestCondition().getseed(),sol1.getTotalCosts(),sol1.getTotalCoverage(),sol2.getTotalCosts(),sol2.getTotalCoverage());
						out.printf("%s   %s ", sol2.getTestCondition().getInstanceName(),sol2.gettypeSol());
						Locale.setDefault(Locale.US);
						sol2.getTestCondition();
						out.printf(" %.0f     %.2f           %.2f           %.2f          %.2f           %.2f     ", sol2.getoptCriterion(),sol2.getprobDisruption(),(float)sol2.getTestCondition().getseed(),sol2.getTotalTime(),sol2.getTotalDistance(),sol2.Coverage);
						String s="";
						int last=-1;
						//sol2.getRoutes().getLast();
						for(Edge e: sol2.getRoutes().getLast().getEdges()){ //obtengo edges
							s = s.concat(e.getOrigin().getId() + "  - ");	
							last = e.getEnd().getId();
						}
						s = s.concat(last + "");
						
						out.printf("%s ", s);
						
					
					
					}
				}
			}
			out.close();
		} 
		catch (IOException exception) 
		{   System.out.println("Error processing output file: " + exception);
		}
	}//end method

}
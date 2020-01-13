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
	private JumpingMovement Jumping_Strategy;
	private BackandForwardMovement Back_Strategy;
	private Solution Back_Sol;
	private Solution Ahead_Sol;
	private float runningTime;
	private ArrayList<Outputs> list = null;
	private ArrayList<Solution> finalsol = new ArrayList<Solution>(); // saving all solution from the iterations
	private Inputs inputs;
	private Test test;


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
		
	}

	/* SET METHODS */

	public Outputs(Inputs inp, Test t, BackandForwardMovement obSol) {
		new Outputs();
		test=t;
		inp=inputs;
		this.setBack(obSol);
		
		
	}
	
	public Outputs(Inputs inp, Test t, JumpingMovement obSol) {
		new Outputs();
		test=t;
		instanceName=t.getInstanceName();
		this.setJumping(obSol);
		inp=inputs;
		
	}



	public Outputs(Disaster Event, Inputs inp, Test aTest) {
		String Disrup_file= new String(aTest.getInstanceName()+"_Edges_Disruptions"+"_Seed"+aTest.getseed()+"_P(disruption)_"+aTest.getpercentangeDisruption()+"_"+"Disruptions.txt");
		writeLinkedList2(Disrup_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadConnections,false);

	}


	// Max_CPLEX_Sol
	public void setBestInitSol(Solution aSol){bestInitSol = aSol;}
	public void setMin_CPLEXSol(Solution obSol){Min_CPLEX_Sol = obSol;}
	public void setMax_CPLEX_SolSol(Solution obSol){Max_CPLEX_Sol = obSol;}
	public void setJumping(JumpingMovement obSol){
		Jumping_Strategy = obSol;
		this.Ahead_Sol=obSol.jump_Sol;
	printJumpSol();}
	public void setBack(BackandForwardMovement obSol){Back_Strategy = obSol;
	this.Back_Sol=obSol.back_Sol;
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
	
	
	
	public void writeLinkedList2(String tV_file, LinkedList<Edge> edgeRoadConnection,
			HashMap<String, Edge> disruptedEdges, LinkedList<Edge> disruptedRoadNetwork, boolean b) {

		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		try {
			PrintWriter bw = new PrintWriter(tV_file);
			bw.println("Road_Connections");
			for(Edge e:edgeRoadConnection ) {
				int i=0;
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					//bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_aerial_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad());
					
					bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_"+"_Distance_"+e.getDistance()+"_Road_Distance_"+e.getDistanceRoad()+"_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getWeight());
						}}

			bw.println("Disrupted_Edge");
			if(disruptedEdges!=null) {

				Iterator hmIterator = disruptedEdges.entrySet().iterator(); 
				while (hmIterator.hasNext()) { 
					Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
					Edge e= disruptedEdges.get(mapElement.getKey());
					if(e.getOrigin().getId()>e.getEnd().getId()) {	
							//bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_aerial_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad());
						bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_"+"_Distance_"+e.getDistance()+"_Road_Distance_"+e.getDistanceRoad()+"_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getWeight());
							
					}
				}				}
			bw.println("Disrupted_Network");
			for(Edge e:disruptedRoadNetwork) {
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					//bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_aerial_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad());
					bw.println("("+e.getOrigin().getId()+","+e.getEnd().getId()+")_"+"_Distance_"+e.getDistance()+"_Road_Distance_"+e.getDistanceRoad()+"_Connectivity_"+e.getConnectivity()+"_Weight_"+e.getWeight());
						
					}
			}
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}
		//writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,false);

	}

	
	public void printJumpSol(){
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
					if(this.Jumping_Strategy.jump_Sol!=null) {
						String file_name= new String(this.Jumping_Strategy.aTest.getInstanceName()+"_SOLUTION_"+"_p(disruption)_"+this.Jumping_Strategy.aTest.getpercentangeDisruption()+"_seed_"+this.Jumping_Strategy.aTest.getseed()+"_Strategy_Jumping_OptCriterion_"+this.Jumping_Strategy.aTest.getOptcriterion()+"_Output.txt");
						PrintWriter out = new PrintWriter(file_name);
						out.printf("*********************************\n");
						//out.printf("******solution Min_Distance****");
						out.printf("*********************************\n");

						//out.print(sol.toString());
						//out.printf("runTime	%f\n",sol.getTime());
						out.printf("*********************************\n");
						out.printf("      Jumping___________"+this.Jumping_Strategy.aTest.getpercentangeDisruption());
						out.println("\n");
						out.printf("*********************************\n");
						out.println("\n");
						out.printf("*********************************\n");
						out.printf("      Orientation Route    \n");
						out.println("\n");
						out.print(Jumping_Strategy.jump_Sol.toString());
						out.println("\n");
						out.printf("*********************************\n");
						out.printf("      Exploration Route    \n");
						out.println("\n");
						out.print(Jumping_Strategy.exploration_Sol.toString());
						out.println("\n");
						out.printf("      Victims     \n");
						out.println("\n");
						out.println("ID      State");
						Iterator hmIterator = Jumping_Strategy.VictimList.entrySet().iterator(); 
						while (hmIterator.hasNext()) { 
							Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
							Node v= Jumping_Strategy.VictimList.get(mapElement.getKey());
							if(!Jumping_Strategy.connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
								out.print(v.getId() + "      Unreachable");
								out.println("\n");
							}
							else {out.print(v.getId() + "      reachable");
							out.println("\n");}
							
						}
						out.close();	
					//}}
				}
			

		}
		catch(IOException exception){
			System.out.println("Error processing output file: " + exception);
		}
	}//end method
	
	
	
	public void printBackSol(){
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
					if(this.Back_Strategy.back_Sol!=null) {
						String file_name= new String(this.Back_Strategy.aTest.getInstanceName()+"_SOLUTION_"+"_p(disruption)_"+this.Back_Strategy.aTest.getpercentangeDisruption()+"_seed_"+this.Back_Strategy.aTest.getseed()+"_Strategy_BackandForward_OptCriterion_"+this.Back_Strategy.aTest.getOptcriterion()+"_Output.txt");
						PrintWriter out = new PrintWriter(file_name);
						out.printf("*********************************\n");
						//out.printf("******solution Min_Distance****");
						out.printf("*********************************\n");

						//out.print(sol.toString());
						//out.printf("runTime	%f\n",sol.getTime());
						out.printf("*********************************\n");
						out.printf("      Back and Forward    \n");
						out.printf("*********************************\n");
						out.println("\n");
						out.printf("*********************************\n");
						out.printf("      Orientation Route    \n");
						out.println("\n");
						out.print(Back_Strategy.back_Sol.toString());
						out.println("\n");
						out.printf("*********************************\n");
						out.printf("      Exploration Route    \n");
						out.println("\n");
						out.print(Back_Strategy.exploration_Sol.toString());
						out.println("\n");
						out.printf("      Victims     \n");
						out.println("\n");
						out.println("ID      State");
						Iterator hmIterator = Back_Strategy.VictimList.entrySet().iterator(); 
						while (hmIterator.hasNext()) { 
							Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
							Node v= Back_Strategy.VictimList.get(mapElement.getKey());
							if(!Back_Strategy.connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
								out.print(v.getId() + "      Unreachable");
								out.println("\n");
							}
							else {out.print(v.getId() + "      reachable");
							out.println("\n");}
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
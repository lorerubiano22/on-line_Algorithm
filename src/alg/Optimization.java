package alg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Optimization {
	public static Test aTest;
	public static Inputs inputs;
	public static double [][]TravelTime;
	public static int [][]cover;
	public static double [][]imp;
	public static double [][]importances;
	public static double []U;
	public static double []victims;
	public static Solution solution=null;


	/*Getters*/
	public double[][] getTV() {return TravelTime;}
	public static int[][] getC() {return cover;}
	public static double[][] getImp() {return imp;}
	public static double[][] getimportancesDecisionMaker() {return importances;}
	public static double[] getU() {return U;}
	public static double[] getVictims() {return victims;}
	public static Solution getSolution() {return solution;}
	/*Setters*/


	public Optimization(Test t, Inputs inp) {
		aTest=t;
		inputs=inp;
	}   

	public Optimization(double[][] tV, int[][] c, double[][] im, double[][] decisionMaker, double[] u, double[] vict) {
		TravelTime = tV;
		cover = c;
		imp = im;
		importances = decisionMaker;
		U = u;
		victims = vict;

	}
	public static void writeFile(String filename, IloCplex cplex, IloNumVar[][][] x, IloNumVar[] length,
			IloNumVar[] coverage, int v, int n, int K) {
		try {
			PrintWriter bw = new PrintWriter(filename);
			try {
				bw.printf(Locale.US, "Solution status = " + cplex.getStatus());
				bw.println();
				bw.printf(Locale.US, "Solution value  = " + cplex.getObjValue());
				bw.println();
				for (int k=0; k<K; k++){
					for (int i=0; i<n; i++){
						for (int j=0; j<n; j++){
							if (i!=j && cplex.getValue(x[i][j][k])>0.9999) {
								bw.println(i+" - "+j);
							}
						}
					}
					bw.println();
					bw.printf("total travel time E(Length)  = "+cplex.getValue(length[k]));
					bw.println();
					bw.printf("attended victims E(Coverage)  = " + cplex.getValue(coverage[k]));
					bw.println();
				}

				bw.print(cplex.getCplexTime());	
			}
			catch(IloException e) {
				e.printStackTrace();
			}
			bw.println();
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}

	}



	static void writeList(String filename, double [][]TV, int [][]binary , boolean importancefile) {
		try {
			PrintWriter bw = new PrintWriter(filename);
			int n=TV.length;
			bw.println();
			if(importancefile) {
				bw.println("****Importance edges******\n");}
			else {
				bw.println("****Times edges******\n");
			}
			for (int i = 0; i < n; i++) {
				for (int j = i+1; j < n ;j++) {
					Node Inode=inputs.getNode(i);
					Node Jnode=inputs.getNode(j);
					if(i!=j) {
						if(binary[i][j]!=0){
							bw.printf(Locale.US,"("+ i+"."+j+")"+";" +TV[i][j]);
							bw.println();}}
					else {
						if(i!=j) {

							if(importancefile) {

								if(Inode.getId()==0 && Jnode.getProfit()>1  ||  Jnode.getId()==0 && Inode.getProfit()>1 ) {
									bw.printf(Locale.US, "("+ i+"."+j+") "+"; " +" NO CONNECTION ");}
								else{bw.printf(Locale.US, "("+ i+"."+j+") "+"; " +" DISRUPTED ");}}
							else {bw.printf(Locale.US, " ("+ i+"."+j+") "+"; " +999999999 );}
							bw.println();
						}}
				}
			}

			if(importancefile){
				bw.println();
				bw.println("****Importance nodes******\n");
				for(int i=0;i<n;i++) {
					if(inputs.getNode(i).getImportance()==0) {
						bw.printf(Locale.US, i+"; DISCONNECTED" );
						bw.println();
					}
				else{bw.printf(Locale.US, i+"; " +inputs.getNode(i).getImportance());
					bw.println();}
					
				}}
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}
	}

	
	static void writeWeigthedCriterion(String filename) {
		try {
			PrintWriter bw = new PrintWriter(filename);
			int n=Optimization.inputs.getNodes().length;
			bw.println();
			bw.println("****Mix criterion 0.5*C(weight)+0.5*C(distance) ******\n");
			for (int i = 0; i < n; i++) {
				for (int j = i+1; j < n ;j++) {
						if(States.DisruptedNetwork[i][j]==1) {
							bw.printf(Locale.US,"("+ i+"."+j+")"+";" +Optimization.inputs.getedge(i, j).getvalueSelection());
							bw.println();
						}
						
						

						}
					
							}

			bw.flush();
		}
		catch (IOException e) {
			//why does the catch need its own curly?
		}
	}
	
	
	static void writeDisruptions(String filename) {
		try {
			PrintWriter bw = new PrintWriter(filename);
			int n=Optimization.inputs.getNodes().length;
			bw.println();
			bw.println("****Network******\n");
			for (int i = 0; i < n; i++) {
				for (int j = i+1; j < n ;j++) {
						if(States.blocks[i][j]==-1) {
							bw.printf(Locale.US,"("+ i+"."+j+")"+";" +States.importancesEdges[i][j]+" (Disrupted_edge) ");
							bw.println();
						}
						else {
							if(States.blocks[i][j]==1) {
								bw.printf(Locale.US,"("+ i+"."+j+")"+";" +States.importancesEdges[i][j]+" (Active_edge) ");
								bw.println();
							}
						else{bw.printf(Locale.US,"("+ i+"."+j+")"+";  NOT_CONNECTION");
							bw.println();}

						}
					
				}
			}

			bw.flush();
		}
		catch (IOException e) {
			//why does the catch need its own curly?
		}
	}
	public static void estimatePerUSESImportance(IloCplex cplex, IloNumVar[][][] x, int v, int n, int K,String obj) {
		String importance_file= new String("arc_MaxCoverage_importance_per_Edge_use_"+obj+"Euclidean network.txt");
		//How many times is used each arc
		int nArcs=((n - 1) * (n - 2) / 2);
		double[][] importance  = new double[nArcs][nArcs];
		double[][] uses  = new double[n][n];
		try {
			PrintWriter bw = new PrintWriter(importance_file);
			try {
				bw.printf(Locale.US, "obj = "+ cplex.getObjValue());
				bw.println();
				for (int i=0; i<n; i++){
					for (int j=0; j<n; j++){
						int value=0;
						for (int k=0; k<K; k++){
							if (i!=j && cplex.getValue(x[i][j][k])>0.9999) {
								value++;
							}
						}
						uses[i][j]=value;
					}
				}
				for(int i=0;i<n;i++) {
					for(int j=0;j<n;j++) {
						if(uses[i][j]!=0 || uses[j][i]!=0 || i!=j) {
							importance[i][j]=uses[i][j]+uses[j][i];}
					}
				}
			}
			catch(IloException e) {
				e.printStackTrace();
			}
			bw.println();
			bw.flush();
		} 
		catch (IOException e) {
			//why does the catch need its own curly?
		}
	}


	private static void swap(double c1, double c2) {
		double temp = c1; 
		c1 = c2; 
		c2 = temp; 
	}
	public static void SetSolution(String cPLEX_file, IloCplex cplex, IloNumVar[][][] x, IloNumVar[] length,IloNumVar[] coverage, int mv, int n, int K, String Method, String Obj) {
		Solution sol= new Solution();
		try {
			for (int k=0; k<K; k++){
				Route rAux = new Route();
				for (int i=0; i<n; i++){
					for (int j=0; j<n; j++){
						if (i!=j && cplex.getValue(x[i][j][k])>0.9999) {
							Edge ed=inputs.getedge(i, j);
							rAux.addEdge(0, ed);
						}
					}
				}
				if(rAux!=null) {
					for(Edge e:rAux.getEdges()) {
						e.setInRoute(rAux);
					}
				}
				rAux.setTime(cplex.getValue(length[k]));
				rAux.setDistance(cplex.getValue(length[k])*Test.getTravelSpeed());
				rAux.setCoverage(cplex.getValue(coverage[k]));
				rAux.sortRoute();
				sol.addRoute(rAux);
				sol.updatingSolutionAttributes(inputs);
				sol.settypeSol(Method);
				sol.setObjective(Obj);
				sol.setPCTime(cplex.getCplexTime());
				sol.setprobDisruption(aTest.getprobDisruption());
				sol.setoptCriterion(aTest.getOptcriterion());
				sol.setsol_typeNetwork(aTest.gettypeNetwork());
				sol.solCondition(inputs, aTest);
				solution=new Solution(sol);
			}
		}
		catch(IloException e) {
			e.printStackTrace();
		}
	}
}

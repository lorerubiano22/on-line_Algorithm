package alg;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;


public class CPLEXDist {


	public static Solution solveMe(Inputs inp, Test t,double [][]TV,int [][]c,double []U,double []victims, boolean euclidean) {
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		/*SETS*/

		int V=inp.getVehNumber(); // set of paths
		int K=1; // set of paths
		int n=victims.length; //set of node
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		/*PARAMETERS*/

		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		/*MODEL*/
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		try {
			IloCplex cplex= new IloCplex();
			/*DECISION VARIABLES*/

			//Binary variable
			IloNumVar[][][] x= new IloNumVar[n][n][K];
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					x[i][j]=cplex.boolVarArray(K);
				}
			}


			//subtours Variable
			IloNumVar[][] w=new IloNumVar[n][K];
			for(int i=0;i<n;i++){
				w[i]=cplex.numVarArray(K, 0,Double.MAX_VALUE);
			}
			IloNumVar[] length=new IloNumVar[K];
			for(int d=0;d<K;d++){
				length[d]=cplex.numVar(0, Double.MAX_VALUE);
			}

			IloNumVar[] coverage=new IloNumVar[K];
			for(int d=0;d<K;d++){
				coverage[d]=cplex.numVar(0, Double.MAX_VALUE);
			}


			IloLinearNumExpr obj = cplex.linearNumExpr();
			for(int i=0; i<n;i++) {
				for(int j=0;j<n;j++ ) {
					if(i!=j && TV[i][j]>0 ) {
						for(int k=0;k<K;k++ ) {
							obj.addTerm(TV[i][j]/K, x[i][j][k]);

						}
					}
				}
			}
			cplex.addMinimize(obj);


			/***CONSTRAINTS**/


			//origen_cust(k)..sum((j)$(ord(j)>1 and ord(j)<end),X('1',j,k))=e=1;  
			for(int k=0;k<K;k++) {
				IloLinearNumExpr expr2 = cplex.linearNumExpr();
				for(int j= 1; j<n;j++) {
					expr2.addTerm(1.0, x[0][j][k]);
				}
				cplex.addEq(expr2,1.0);
			}


			//end_cust(k)..sum((j)$(ord(j)>1 and ord(j)<end),X('1',j,k))=l=1;  
			for(int k=0;k<K;k++) {
				IloLinearNumExpr expr2 = cplex.linearNumExpr();
				for(int i= 1; i<n;i++) {
					expr2.addTerm(1.0, x[i][0][k]);
				}
				cplex.addEq(expr2,1);
			}

			//visit_cust(j,k)$(ord(j)<end and ord(j)>1)..sum((i),X(i,j,k))=l=1; 
			for(int j=1;j<n;j++) {
				for(int k=0;k<K;k++) {
					IloLinearNumExpr expr2 = cplex.linearNumExpr();
					for(int i=0;i<n;i++) {
						expr2.addTerm(1.0, x[i][j][k]);}
					cplex.addLe(expr2,1);}
			}

			//flow(o,k)$(ord(o)<>1 and ord(o)<>end)..sum(i,X(i,o,k))=e=sum(j,X(o,j,k)); 
			for(int k=0;k<K;k++) {
				for(int o=1; o<n;o++) {
					IloLinearNumExpr expr3 = cplex.linearNumExpr();
					for(int i= 0; i<n;i++) {
						for(int j= 0; j<n;j++) {
							expr3.addTerm(1.0, x[i][o][k]);
							expr3.addTerm(-1.0, x[o][j][k]);
						}
					}
					cplex.addEq(expr3, 0); 
				}
			}

		
			//segment_origen(k,i)$(ord(i)<>end)..sum(j$(ord(j)<>1),X(i,j,k))=l=1;        
			for(int k=0;k<K;k++) {
				for(int i= 1; i<n;i++) {
					IloLinearNumExpr expr6 = cplex.linearNumExpr();
					for(int j= 1; j<n;j++) {
						expr6.addTerm(1.0, x[i][j][k]);
					}
					cplex.addLe(expr6, 1);
				}
			}

			//segment_end(k,j)$(ord(j)<>1)..sum(i$(ord(i)<>end),X(i,j,k))=l=1; 
			for(int k=0;k<K;k++) {
				for(int j= 1; j<n;j++) {
					IloLinearNumExpr expr7 = cplex.linearNumExpr();
					for(int i= 1; i<n;i++) {
						expr7.addTerm(1.0, x[i][j][k]);
					}
					cplex.addLe(expr7, 1);
				}
			}

			//Subtour(i,j,d)$(ord(i)>1 and ord(i)<end and ord(j)>1 and ord(j)<end).. U(i,d)-U(j,d)+(card(i)*X(i,j,d))=l=card(i)-1; 
			for(int k=0;k<K;k++) {
				for(int i=1;i<n;i++) {
					for(int j= 1; j<n;j++) {
						IloLinearNumExpr expr8 = cplex.linearNumExpr();
						expr8.addTerm(1.0, w[i][k]);
						expr8.addTerm(-1.0, w[j][k]);
						expr8.addTerm(n, x[i][j][k]);
						cplex.addLe(expr8, n);
					}
				}
			}

			//DrivingRange(k)..DR(d,k)=L=sum((i,j),X(i,j,d)*(TV(i,j)));
			for(int k=0;k<K;k++) {
				IloLinearNumExpr expr10 = cplex.linearNumExpr();
				for(int i= 0; i<n;i++) {
					for(int j= 0; j<n;j++) {
						expr10.addTerm(TV[i][j], x[i][j][k]);
					}
				}
				cplex.addLe(expr10,inp.getDrivingRange());
			}			

			//forcing potential nodes
			for(int j= 1; j<n;j++) {
				for(int k=0;k<K;k++) {
					IloLinearNumExpr expr10 = cplex.linearNumExpr();
					for(int i= 0; i<n;i++) {
						expr10.addTerm(1, x[i][j][k]);
					}
					cplex.addGe(expr10,victims[j]);
				}
			}

			//only the arcs in without disruption can be visited
			for(int i= 0; i<n;i++) {
				for(int j= 0; j<n;j++) {
					for(int k=0;k<K;k++) {
						IloLinearNumExpr expr10 = cplex.linearNumExpr();
						expr10.addTerm(1, x[i][j][k]);
						cplex.addLe(expr10,c[i][j]);
					}

				}
			}

			//LenghtRoute(k)..Lenght(k)=e=sum((i,j),X(i,j,d)*(TV(i,j)));
			for(int k=0;k<K;k++) {
				IloLinearNumExpr expr10 = cplex.linearNumExpr();
				for(int i= 0; i<n;i++) {
					for(int j= 0; j<n;j++) {
						expr10.addTerm(TV[i][j]/K, x[i][j][k]);
					}
				}
				expr10.addTerm(-1, length[k]);
				cplex.addEq(expr10,0);
			}

			// computing coverage
			for(int k=0;k<K;k++) {
				IloLinearNumExpr expr10 = cplex.linearNumExpr();
				for(int i= 0; i<n;i++) {
					for(int j= 0; j<n;j++) {
						if(U[j]>0) {expr10.addTerm(victims[j]/K, x[i][j][k]);}
						else{expr10.addTerm(0, x[i][j][k]);}
					}
				}
				expr10.addTerm(-1, coverage[k]);
				cplex.addEq(expr10,0);
			}


			/*SOLVING*/
			//cplex.setParam(IloCplex.Param.TimeLimit, t.getMaxTime());
			cplex.setParam(IloCplex.Param.TimeLimit, 1000);
			// write model to file
			//cplex.exportModel("Miniming_distance.lp"); 

			if (cplex.solve()) {
				//if (cplex.solveFixed()) {
				//String CPLEX_file= new String(t.getInstanceName()+"_"+"_Seed_"+t.getseed()+"_DProb_"+t.getprobDisruption()+"_network_"+euclidean +"_DR_"+inp.getDrivingRange() +"_MinDistance_CPLEX.txt");
				String CPLEX_file= new String(t.getInstanceName()+"_"+"_DProb_"+t.getprobDisruption()+"_network_"+euclidean +"_DR_"+inp.getDrivingRange() +"_MinDistance_CPLEX.txt");
				//Optimization.writeFile(CPLEX_file,cplex,x,length,coverage,V,n,K);
				Optimization.SetSolution(CPLEX_file,cplex,x,length,coverage,V,n,K,new String ("CPLEX"),new String ("MinDist"));
				
				System.out.println("obj = " + cplex.getObjValue());
				cplex.output().println("Solution value  = " + cplex.getObjValue());
				System.out.println("\n");
				cplex.output().println("Solution status = " + cplex.getStatus());
			}
			else {
				System.out.println("Model not solved");
			}
			cplex.end(); 

		}
		catch(IloException e) {
			e.printStackTrace();
		}
		return Optimization.getSolution();
		}






}
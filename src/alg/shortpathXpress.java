package alg;
import java.util.LinkedList;
import com.dashoptimization.*;




public class shortpathXpress {


	public static double [][]TravelTime;
	public static int [][]cover;
	public static double [][]imp;
	public static double [][]importances;
	public static double []U;
	public static double []victims;
	public static Solution solution=null;


	public static boolean solveMe(Inputs inputs, LinkedList<Edge> victimsansintermediateNetwork,int origin, int end) {
		boolean path= false;

		/* Setting problem parameters*/

		int n=inputs.getNodes().length; //set of node


		// Distance Matrix 	
		double [][]TravelDist= new double [n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				TravelDist[i][j]=99999;	
			}	
		}

		for(Edge e:victimsansintermediateNetwork) {
			TravelDist[e.getOrigin().getId()][e.getEnd().getId()]=e.getDistance();
		}

		//////

		// (Coverage Matrix)
		System.out.println("size"+n);
		int [][]coverage= new int [n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(TravelDist[i][j]!=99999) {
					coverage[i][j]=1;
				}
				else{coverage[i][j]=0;};	
			}	
		}


		double []profit=new double[inputs.getNodes().length];
		for(Node no:inputs.getNodes()) {
			profit[no.getId()]=no.getProfit();
		}
		double []coverageExpected= new double[inputs.getNodes().length];
		coverageExpected[0]=0; // Depot
		for(int i=1;i<inputs.getNodes().length;i++) {
			if(inputs.getNode(i).getProfit()>1) {
				coverageExpected[i]=1;
			}
			else {coverageExpected[i]=0;}
		}

		for(int i=1;i<inputs.getNodes().length;i++) {
			System.out.println("node "+i+" "+coverageExpected[i] );
		}

		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				System.out.println("connection "+i+","+j+" "+coverage[i][j]);
			}	
		}


		/* Set of items */

		//try {
		// MODEL
		XPRB bcl = new XPRB();
		XPRBprob p=bcl.newProb("ShortPath");  // inicialización del problema

		// VARIABLE

		// X(ij)= Binary. 1 if UAV travels on edge (ij). 0 otherwise
		XPRBvar[][] x=new XPRBvar[n][n];// Binary. 1 if UAV travels on edge (ij)
		XPRBvar[] w = new XPRBvar[n]; // auxiliar variable to subtour eliminate

		for(int i=0;i<n;i++) {
			w[i] = p.newVar("w_("+i+")", XPRB.SC, 0, n);    
		}

		for(int i=0; i<n;i++ ) {
			for(int j=0; j<n;j++ ) {
				x[i][j]=p.newVar("x_("+i+","+j+")", XPRB.BV);  
			}
		}

		// OBJECTIVE: short path -> to minimize travel distance
		XPRBexpr lobj = new XPRBexpr();

		// LIST OF CONTRAINTS
		XPRBexpr safeRoute = new XPRBexpr();
		//XPRBexpr startRoutecrt = new XPRBexpr();
		XPRBexpr endSafecrt = new XPRBexpr();


		// Objective function
		for(int i=0;i<n-1;i++) {
			for(int j=1;j<n;j++) {
				lobj.add(x[i][j].mul(TravelDist[i][j])); 
				p.setObj(lobj); 
			}
		}

		// CONSTRAINTS

		//origen_cust..sum((j)$(ord(j)>1),X('1',j))=e=1;  
		XPRBexpr startRoutecrt = new XPRBexpr();
		for(int j=1;j<n;j++) {
			//startRoutecrt.add(x[origin][j].mul(coverage[origin][j]));  
			startRoutecrt.add(x[origin][j].mul(1));  
		}
		p.newCtr("Origen", startRoutecrt.eql(1));    

		//end_cust..sum((j)$(ord(j)>1),X('end',j))=e=0;  
		for(int i=1;i<n;i++) {
			safeRoute.add(x[i][origin].mul(1));  
			p.newCtr("EndEnd", safeRoute.eql(0));  
		}
		//end_cust..sum((j)$(ord(j)>1),X('end',j))=e=0;  
		for(int j=1;j<n;j++) {
			endSafecrt.add(x[end][j].mul(1));  
			p.newCtr("OrigenEnd", endSafecrt.eql(0));  
		}


		//visit_cust(j)$(ord(j)<end and ord(j)>1)..sum((i),X(i,j))=l=1; 
		for(int j=1;j<n;j++) {
			XPRBexpr visit = new XPRBexpr();
			for(int i=0;i<n;i++) {
				//visit.add(x[i][j].mul(coverage[i][j]));
				visit.add(x[i][j].mul(1));
			}
			p.newCtr("VisitNode", visit.gEql(coverageExpected[j]));  
		}

		//visit_cust(j)$(ord(j)<end and ord(j)>1)..sum((i),X(i,j))=l=1; 
		for(int j=1;j<n;j++) {
			for(int i=0;i<n;i++) {
				XPRBexpr converage = new XPRBexpr();
				//visit.add(x[i][j].mul(coverage[i][j]));
				converage.add(x[i][j].mul(1));
				p.newCtr("VisitNode", converage.lEql(coverage[i][j]));  
			}

		}


		// flow
		for(int o=0;o<n;o++) {
			if(o!=origin && o!=end ) {
				XPRBexpr flow = new XPRBexpr();
				for(int i=0;i<n;i++) {
					for(int j=0;j<n;j++) {  
						flow.add(x[i][o]);
						flow.add(x[o][j].mul(-1));
					}
				}
				p.newCtr("Flow", flow.eql(0)); 
			}
		}






		// subtour constraint
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				XPRBexpr subtour = new XPRBexpr();
				subtour.add(w[i]);
				subtour.add(w[j].mul(-1));
				subtour.add(x[i][j].mul(n));
				p.newCtr("subtour", subtour.lEql(n-1));
			}
		}







		/****SOLVING + OUTPUT****/
		p.setSense(XPRB.MINIM);            /* Choose the sense of the optimization */
		p.mipOptimize("");                 /* Solve the MIP-problem */

		System.out.println("Objective: " + p.getObjVal());  /* Get objective value */

		if((p.getMIPStat()==XPRB.MIP_SOLUTION) || (p.getMIPStat()==XPRB.MIP_OPTIMAL)) {
			path=true;
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++){
					if(x[i][j].getSol()>0){
						System.out.println("x"+x[i][j].getName()+": "+x[i][j].getSol());
					}
				}
			}
		}

		return path;
	}

}

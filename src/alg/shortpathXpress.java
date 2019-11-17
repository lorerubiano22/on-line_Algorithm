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

	public static boolean solveMe(Inputs inp,double [][]TV,int [][]c,double []U,double []victims, int origin, int end) {
		boolean path= false;
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		origin=0;
		end=5;

		/*SETS*/

		int V=inp.getVehNumber(); // set of paths
		int K=1; // set of paths
		int n=victims.length; //set of node
		XPRBindexSet ITEMS;                /* Set of items */

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
		XPRBexpr startRoutecrt = new XPRBexpr();
		XPRBexpr endSafecrt = new XPRBexpr();


		// Objective function
		for(int i=0;i<n-1;i++) {
			for(int j=1;j<n;j++) {
				lobj.add(x[i][j].mul(TV[i][j])); 
				p.setObj(lobj); 
			}
		}

		// CONSTRAINTS

		// safety: the node "origin" is the start of the route and the nodo "end" is the end of the route
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(i==end || j==origin) {
					safeRoute.add(x[i][j].mul(1));  
					p.newCtr("Origen", safeRoute.eql(0) );    
				}
			}
		}

		//origen_cust..sum((j)$(ord(j)>1),X('1',j))=e=1;  
		for(int j=1;j<n;j++) {
			startRoutecrt.add(x[origin][j].mul(1));  
			p.newCtr("Origen", startRoutecrt.eql(1) );    
		}


		//end_cust..sum((j)$(ord(j)>1),X('end',j))=l=1;  
		for(int j=1;j<n;j++) {
			endSafecrt.add(x[end][j].mul(1));  
			p.newCtr("OrigenEnd", endSafecrt.eql(0) );  
		}


		//visit_cust(j)$(ord(j)<end and ord(j)>1)..sum((i),X(i,j))=l=1; 
		for(int j=1;j<n-1;j++) {
			XPRBexpr visit = new XPRBexpr();
			for(int i=0;i<n-1;i++) {
				visit.add(x[i][j]);
			}
			p.newCtr("VisitNode", visit.eql(1) );  
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

		//segment_origen(i)$(ord(i)<>end)..sum(j$(ord(j)<>1),X(i,j))=l=1;
		for(int i=1;i<n;i++) {
			XPRBexpr segment = new XPRBexpr();
			for(int j=1;j<n-1;j++) {
				segment.add(x[i][j]);
			}
			p.newCtr("Segment",segment.lEql(1));
		}


		//segment_end(k,j)$(ord(j)<>1)..sum(i$(ord(i)<>end),X(i,j,k))=l=1; 
		for(int j=1;j<n;j++) {
			XPRBexpr segmentEnd = new XPRBexpr();
			for(int i=1;i<n-1;i++) {
				segmentEnd.add(x[i][j]);
			}
			p.newCtr("SegmentEnd",segmentEnd.lEql(1));
		}


		////////////////////////////////////////




		//route ends from the depot. for all i {depot}
		//			for(int i=0;i<n;i++) {
		//				endRoutecrt.add(x[i][end].mul(1));  
		//				p.newCtr("OrigenEnd", endRoutecrt.eql(1) );  
		//			}


		//			// safety: no origin to the origin node
		//			for(int i=1;i<n-1;i++) {
		//				originSafecrt.add(x[i][origin].mul(1));  
		//				p.newCtr("OrigenEnd", originSafecrt.eql(0) );  
		//			}






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


		// for each node different than "origin" node COULD have one destination
		for(int j=1;j<n;j++) {
			XPRBexpr oneDestination= new XPRBexpr();
			for(int i=0; i<n-1;i++) {
				oneDestination.add(x[i][j]);
				p.newCtr("oneDestination", oneDestination.lEql(1));
			}
		}

		// for each node different than "end" node COULD have one origin node (precedent node)
		for(int i=0;i<n-1;i++) {
			XPRBexpr onePrecendence= new XPRBexpr();
			for(int j=1; j<n;j++) {
				onePrecendence.add(x[i][j]);
				p.newCtr("subtour", onePrecendence.lEql(1));
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
		//}

		//		finally {
		//			XPRS.free();
		//		}

		////
		return path;
	}

	public static boolean solveMe(Inputs inputs, LinkedList<Edge> victimsansintermediateNetwork,int origin, int end) {
		boolean path= false;

		/* Setting problem parameters*/

		origin=0;
		end=5;
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
				if(TravelDist[i][j]!=0) {
					coverage[i][j]=1;
				}
				else{coverage[i][j]=(int)TravelDist[i][j];};	
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
		XPRBexpr startRoutecrt = new XPRBexpr();
		XPRBexpr endSafecrt = new XPRBexpr();


		// Objective function
		for(int i=0;i<n-1;i++) {
			for(int j=1;j<n;j++) {
				lobj.add(x[i][j].mul(TravelDist[i][j])); 
				p.setObj(lobj); 
			}
		}

		// CONSTRAINTS

		// safety: the node "origin" is the start of the route and the nodo "end" is the end of the route
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(i==end || j==origin) {
					safeRoute.add(x[i][j].mul(1));  
					p.newCtr("Origen", safeRoute.eql(0) );    
				}
			}
		}

		//origen_cust..sum((j)$(ord(j)>1),X('1',j))=e=1;  
		for(int j=1;j<n;j++) {
			startRoutecrt.add(x[origin][j].mul(1));  
			p.newCtr("Origen", startRoutecrt.eql(1) );    
		}


		//end_cust..sum((j)$(ord(j)>1),X('end',j))=l=1;  
		for(int j=1;j<n;j++) {
			endSafecrt.add(x[end][j].mul(1));  
			p.newCtr("OrigenEnd", endSafecrt.eql(0) );  
		}


		//visit_cust(j)$(ord(j)<end and ord(j)>1)..sum((i),X(i,j))=l=1; 
		for(int j=1;j<n;j++) {
			XPRBexpr visit = new XPRBexpr();
			for(int i=1;i<n;i++) {
				visit.add(x[i][j].mul(coverage[i][j]));
			}
			p.newCtr("VisitNode", visit.gEql(coverageExpected[j]));  
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

		//segment_origen(i)$(ord(i)<>end)..sum(j$(ord(j)<>1),X(i,j))=l=1;
//		for(int i=1;i<n;i++) {
//			XPRBexpr segment = new XPRBexpr();
//			for(int j=1;j<n-1;j++) {
//				segment.add(x[i][j]);
//			}
//			p.newCtr("Segment",segment.lEql(1));
//		}


		//segment_end(k,j)$(ord(j)<>1)..sum(i$(ord(i)<>end),X(i,j,k))=l=1; 
		for(int j=1;j<n;j++) {
			XPRBexpr segmentEnd = new XPRBexpr();
			for(int i=1;i<n-1;i++) {
				segmentEnd.add(x[i][j]);
			}
			p.newCtr("SegmentEnd",segmentEnd.lEql(1));
		}


		////////////////////////////////////////




		//route ends from the depot. for all i {depot}
		//			for(int i=0;i<n;i++) {
		//				endRoutecrt.add(x[i][end].mul(1));  
		//				p.newCtr("OrigenEnd", endRoutecrt.eql(1) );  
		//			}


		//			// safety: no origin to the origin node
		//			for(int i=1;i<n-1;i++) {
		//				originSafecrt.add(x[i][origin].mul(1));  
		//				p.newCtr("OrigenEnd", originSafecrt.eql(0) );  
		//			}






		// subtour constraint
//		for(int i=0;i<n;i++) {
//			for(int j=0;j<n;j++) {
//				XPRBexpr subtour = new XPRBexpr();
//				subtour.add(w[i]);
//				subtour.add(w[j].mul(-1));
//				subtour.add(x[i][j].mul(n));
//				p.newCtr("subtour", subtour.lEql(n-1));
//			}
//		}
//

		// for each node different than "origin" node COULD have one destination
		for(int j=1;j<n;j++) {
			XPRBexpr oneDestination= new XPRBexpr();
			for(int i=0; i<n-1;i++) {
				oneDestination.add(x[i][j]);
				p.newCtr("oneDestination", oneDestination.lEql(1));
			}
		}

		// for each node different than "end" node COULD have one origin node (precedent node)
		for(int i=0;i<n-1;i++) {
			XPRBexpr onePrecendence= new XPRBexpr();
			for(int j=1; j<n;j++) {
				onePrecendence.add(x[i][j]);
				p.newCtr("subtour", onePrecendence.lEql(1));
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
		//}

		//		finally {
		//			XPRS.free();
		//		}

		////
		return path;
	}

}

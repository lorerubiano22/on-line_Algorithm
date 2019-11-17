package alg;
import java.util.Random;


public class States {
	/*Completed network distribution*/
	static int  [][] Network; // it shows the state of the normal network forbidden paths
	static int[][]  DisruptedNetwork; // 0= disrupted or disconnected edge. -1= visited and active edge. 1= Edge to visit. The network which is discovered by the routing.
	static int[][]  Disruptions;  // it shows the state of the current network forbidden paths+ disrupted paths 
	static int[][]  RoutingNetwork;  // it shows the state of the current network forbidden paths+ disrupted paths + visited edges
	static int[][]  blocks;
	static double [] victims;
	static double []victimsReward;
	/*Exploration process(road) network distribution*/
	static double  [][] TravelTime; 
	static double[][] impEdges; 
	static double[][] importancesEdges; 
	static double[] impNodes;
	public static double impAverage;



	public States(int[][] disruptedNetwork2, int[][] disruptions, int[][] network3, int disruptedeEdges[][]) {
		DisruptedNetwork=disruptedNetwork2;
		Disruptions=disruptions;
		RoutingNetwork=network3;
		blocks=disruptedeEdges;


	}
	public States(int[][] disruptedNetwork2) {
		for(int i=0;i<disruptedNetwork2.length;i++) {
			for(int j=0;j<disruptedNetwork2.length;j++) {
				DisruptedNetwork[i][j]=disruptedNetwork2[i][j];
			}
		}

	}


	public States() {
		/*Completed network distribution*/
		Network=new int[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length]; // it shows the state of the normal network forbidden paths
		DisruptedNetwork=new int[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length] ; // 0= disrupted or disconnected edge. -1= visited and active edge. 1= Edge to visit. The network which is discovered by the routing.
		Disruptions=new int[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length];  // it shows the state of the current network forbidden paths+ disrupted paths 
		RoutingNetwork=new int[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length];  // it shows the state of the current network forbidden paths+ disrupted paths + visited edges
		victims=new double[Optimization.inputs.getNodes().length];
		victimsReward=new double[Optimization.inputs.getNodes().length];
		TravelTime=new double[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length]; 
		impEdges=new double[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length]; 
		importancesEdges=new double[Optimization.inputs.getNodes().length][Optimization.inputs.getNodes().length]; 
		impNodes=new double[Optimization.inputs.getNodes().length];
		impAverage=0;
	}
	
	
	static void importancesRouting() {
		int n=Optimization.inputs.getNodes().length;
		for(int i=0;i<n;i++) {
			for(int j=i;j<n;j++) {
				if(RoutingNetwork[i][j]==0 || RoutingNetwork[i][j]==-1) {
					impEdges[i][j]=0;
					impEdges[j][i]=0;
				}	
			}	
		}

		for(Edge e:Optimization.inputs.getedgeList()) {
			boolean Or=e.getOrigin().getConnection();
			boolean En=e.getEnd().getConnection();
			e.setImportance(impEdges[e.getOrigin().getId()][e.getEnd().getId()]);
			e.setState(States.RoutingNetwork[e.getOrigin().getId()][e.getEnd().getId()]);
			if(Or==false && En==false ) {
				RoutingNetwork[e.getOrigin().getId()][e.getEnd().getId()]=0;
				RoutingNetwork[e.getEnd().getId()][e.getOrigin().getId()]=0;
				impEdges[e.getOrigin().getId()][e.getEnd().getId()]=0.0;
				impEdges[e.getEnd().getId()][e.getOrigin().getId()]=0.0;
				e.setState(States.RoutingNetwork[e.getOrigin().getId()][e.getEnd().getId()]);
				e.setImportance(impEdges[e.getOrigin().getId()][e.getEnd().getId()]);
			}
		}

		for(Edge e:Optimization.inputs.getedgeList()) {
			int i=e.getOrigin().getId();
			int j=e.getEnd().getId();
			e.setImportance(impEdges[i][j]);
			e.getInverseEdge().setImportance(impEdges[j][i]);
			e.setState(RoutingNetwork[i][j]);
			e.getInverseEdge().setState(RoutingNetwork[i][j]);
			//UpdatingStatesEdges(e.getOrigin());
		}
}





	public static double[] getInitialReward() {
		int n=victimsReward.length;
		double []R= new double [n];
		for(int j=0;j<n;j++) {
			R[j]=victimsReward[j];
		}	
		return R;
	}

	public static double[][] getInitialTT() {
		//
		int n=TravelTime.length;
		double [][]Travel= new double [n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				Travel[i][j]=TravelTime[i][j];
			}	
		}
		return Travel;
	}

	private static void setinitialconditions(Inputs inputs) {
		int n= Optimization.inputs.getNodes().length;
		TravelTime= new double [n][n];
		for(Edge e:inputs.getedgeList()) {
			int indexI=e.getOrigin().getId();
			int indexJ=e.getEnd().getId();
			double tv=0;
			if(indexI==indexJ) {tv=0;}
			else {tv=e.getTime();}
			TravelTime[indexI][indexJ]=tv;}

		victimsReward= new double [n];
		for(int i=0; i<n;i++) {
			Node IDnode=Optimization.inputs.getNodes()[i];
			victimsReward[i]=IDnode.getProfit();
		}
		victims= new double [n];
		for(int i=0;i<n;i++) {
			if(victimsReward[i]!=0 && i!=0) {
				victims[i]=1;
			}
			else {victims[i]=0;}
		}

	}



	public static void generatingStatesNetwork(Inputs inputs, Test t) {
		int n=Optimization.inputs.getNodes().length;
		int[][] RoutingNetwork= new int [n][n];
		int[][] Disruptions= new int [n][n];
		int[][] DisruptedNetwork= new int [n][n];
		int[][] disruptedeEdges= new int [n][n];
		//Traveling time 
		double [][]TV= new double [n][n];
		for(Edge e:Optimization.inputs.getedgeList()) {
			int indexI=e.getOrigin().getId();
			int indexJ=e.getEnd().getId();
			TV[indexI][indexJ]=e.getTime();;
			TV[indexJ][indexI]=e.getTime();;
		}

		for(int i=0;i<n;i++) {
			for(int j=i;j<n;j++) {
				DisruptedNetwork[i][j]=Network[i][j];
				Disruptions[i][j]=Network[i][j];
				RoutingNetwork[i][j]=Network[i][j];
				disruptedeEdges[i][j]=Network[i][j];
				DisruptedNetwork[j][i]=Network[j][i];
				Disruptions[j][i]=Network[j][i];
				RoutingNetwork[j][i]=Network[j][i];
				disruptedeEdges[j][i]=Network[j][i];
			}
		}
		setinitialconditions(Optimization.inputs);
		new States(DisruptedNetwork,Disruptions,RoutingNetwork,disruptedeEdges);
	}

	/*Setters*/
	public static void setImpAverage(double aver) {impAverage = aver;}

	public static void setImpEdges(double[][] imp) {
		int n=imp.length;
		impEdges=new double[n][n];
		importancesEdges=new double[n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				impEdges[i][j]=imp[i][j];
				importancesEdges[i][j]=imp[i][j];
			}
		}
		//Optimization.inputs.setEdgeList(copy);
		System.out.println("LISTO");
	}


	public static void setImpNodes(double[] imp) {
		int n=imp.length;
		impNodes= new double[n];
		for(int i=0;i<n;i++) {
			impNodes[i]=imp[i];}}

	/*Getters*/
	public double getImpAverage() { return impAverage;}
	public static double[][] getImpEdges() {return impEdges;}
	public static double[] getImpNodes() {return impNodes;}




	/*f(x) computing the mean*/
	public static double mean(double[] m) {
		double sum = 0;
		int numActivedelements=0;
		for (int i = 0; i < m.length; i++) {
			sum += m[i];
			if(m[i]>0) {numActivedelements++;}
		}
		double value=0;
		if(numActivedelements!=0) {
			value=sum / numActivedelements;}
		return value;
	}


	public static void generatingEvents() {
		//Random rn = new Random();
		int n=Optimization.inputs.getNodes().length;
		Random rn = new Random(Optimization.aTest.getseed());
		int totaldisrup = 0;
		int totalEdges=0;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++) {
				if(Network[i][j]==1) {totalEdges++;}
			}
		}
		totaldisrup=Math.round(totalEdges*Optimization.aTest.getpercentangeDisruption()/2);
		int disruption=0;
		while(disruption<totaldisrup) {
			int randomEdge=rn.nextInt(Optimization.inputs.getedgeList().size()-1);
			//for(Edge edge:Optimization.inputs.getedgeList()) {
			Edge edge=Optimization.inputs.getedgeList().get(randomEdge);
			int i=edge.getOrigin().getId();
			int j= edge.getEnd().getId();
			if(i!=j) {
				//scenario base
				if(Network[i][j]!=0.0) {
				//Dummy network 2 
					//if(Network[i][j]!=0.0 && i!=0 && j!=2 && i!=2 && j!=0) {
					{	
						{
							disruption++;
							Disruptions[i][j]=0;
							Disruptions[j][i]=0;
							blocks[i][j]=-1;
							blocks[j][i]=-1;
						}
					}
				}
			}
			/***************************/
			else{	
				Disruptions[i][j]=0;
				blocks[j][i]=0;
			}
			if(disruption>=totaldisrup) {
				break;
			}
		}
		
		for(int i=0;i<n;i++) {
			for(int j=i+1;j<n;j++) {
				Disruptions[j][i]=Disruptions[i][j];
			}
		}
	}


	public static void uptadatingStatesNetwork(Solution temporal_Sol_MS, int[][] disruptions2) {
		/*Despues del routing se actualiza la infomación de la red*/
		int n=Optimization.inputs.getNodes().length;
		/*Updating visit edges*/
		for(Route r:temporal_Sol_MS.getRoutes()) {
			for(Edge e:r.getEdges()) {
				int i= e.getOrigin().getId();
				int j= e.getEnd().getId();
				DisruptedNetwork[i][j]=Disruptions[i][j];
				DisruptedNetwork[j][i]=Disruptions[j][i];
				if(States.Disruptions[i][j]==0 || States.Disruptions[i][j]==0) {
					RoutingNetwork[i][j]=0;
					RoutingNetwork[j][i]=0;
				}
				if(States.Disruptions[i][j]==1 || States.Disruptions[i][j]==1) {
					RoutingNetwork[i][j]=-1;
					RoutingNetwork[j][i]=-1;
				}
				if(DisruptedNetwork[i][j]>States.Disruptions[i][j] || DisruptedNetwork[j][i]>Disruptions[j][i]) {
					DisruptedNetwork[i][j]=0;
					DisruptedNetwork[j][i]=0;
				}

			}
		}
		/*Updating adj edges of node visited*/
		//		for(Route r:temporal_Sol_MS.getRoutes()) {
		//			for(Edge e:r.getEdges()) {
		//				for(Edge adje:e.getEnd().getconnectionsList()) {
		//					DisruptedNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]=Disruptions[adje.getOrigin().getId()][adje.getEnd().getId()];
		//					DisruptedNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]=Disruptions[adje.getEnd().getId()][adje.getOrigin().getId()];
		//					/*List for routing*/
		//					RoutingNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]=Disruptions[adje.getOrigin().getId()][adje.getEnd().getId()];
		//					RoutingNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]=Disruptions[adje.getEnd().getId()][adje.getOrigin().getId()];
		//					//					RoutingNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]=0;
		//					//					RoutingNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]=0;
		//					if(RoutingNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]>States.Disruptions[adje.getOrigin().getId()][adje.getEnd().getId()] || RoutingNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]>States.Disruptions[adje.getEnd().getId()][adje.getOrigin().getId()]) {
		//						RoutingNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]=0;
		//						RoutingNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]=0;
		//					}
		//					if(DisruptedNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]>States.Disruptions[adje.getOrigin().getId()][adje.getEnd().getId()] || DisruptedNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]>States.Disruptions[adje.getEnd().getId()][adje.getOrigin().getId()]) {
		//						DisruptedNetwork[adje.getOrigin().getId()][adje.getEnd().getId()]=0;
		//						DisruptedNetwork[adje.getEnd().getId()][adje.getOrigin().getId()]=0;
		//					}
		//				}
		//				UpdatingStatesEdges(e.getOrigin());
		//			}
		//		}
	}


	static void UpdatingStatesEdges() {
		for(Node origin:Optimization.inputs.getNodes()) {
			for(Edge e:origin.getAdjEdgesList()) {
				e.setState(RoutingNetwork[e.getOrigin().getId()][e.getEnd().getId()]);
				e.setImportance(impEdges[e.getOrigin().getId()][e.getEnd().getId()]);
			}
			}
	}


	public static void uptadatingSImportancesNetwork(Solution temporal_Sol_MS) {
		for(Route r:temporal_Sol_MS.getRoutes()) {
			for(Edge e:r.getEdges()) {
				int i=e.getOrigin().getId();
				int j=e.getEnd().getId();
				/* Elimina las importancias de los arcos visitados y la de los arcos disruptos*/
				if(RoutingNetwork[i][j]==-1 || RoutingNetwork[i][j]==0) {
					impEdges[i][j]=0;
				}

				/*Se actualiza el estado y las importancias de cada edge*/
				importancesRouting();
				e.setState(RoutingNetwork[i][j]);
				e.setImportance(impEdges[i][j]);
				UpdatingStatesEdges();
			}
		}



		/******/
		//		LinkedList<Edge> copy =new LinkedList<Edge>();
		//		for(int i=0;i<Optimization.inputs.getNodes().length;i++) {
		//			for(int j=0;j<Optimization.inputs.getNodes().length;j++) {
		//				Edge e=Optimization.inputs.getedge(i, j);
		//				e.setImportance(impEdges[i][j]);
		//				e.setState(RoutingNetwork[i][j]);
		//				//e.setInverse(Optimization.inputs.getedge(j,i));
		//				e.getInverseEdge().setImportance(impEdges[j][i]);
		//				e.getInverseEdge().setState(RoutingNetwork[j][i]);
		//				copy.add(e);
		//			}	
		//		}
		//Optimization.inputs.setEdgeList(copy);
		System.out.println("LISTO");
		/***/

	}
	public static void setNonDirectConnections() {
		int n= Optimization.inputs.getNodes().length;
		/***Reading the network***/
		for(int i=0;i<n;i++) {
			for(int j=i+1;j<n;j++) {
				if(i!=j) {
					//Network base: NOT DIRECT CONNECTION DEPOT VICTIMS.   
					if(i==0 && Optimization.inputs.getNode(j).getProfit()>1) {
					//Network 2: Scenario_1: NOT DIRECT CONNECTION DEPOT VICTIMS, A NODE WITH JUST ONE CONNECTION AND NOT DIRECT CONNECTION BETWEEN VICTIMS.
					//
					//if(i==0 && Optimization.inputs.getNode(j).getProfit()>1|| i==1 && j==5|| i==1 && j==2 || i==2 && j==4   || i==2 && j==3  || i==2 && j==5) {
					// ONE DIRECT CONNECTION DEPOT VICTIMS, A NODE WITH JUST ONE CONNECTION AND NOT DIRECT CONNECTION BETWEEN VICTIMS. if(i==0 &&  j==1|| i==1 && j==5|| i==1 && j==4 || i==2 && j==4   || i==2 && j==3  || i==2 && j==5) {
						
					Network[i][j]=0;
						Network[j][i]=0;
					}
					else {
						Network[i][j]=1;
						Network[j][i]=1;
					}
				}
				else {
					Network[i][j]=0;
					Network[j][i]=0;
				}

			}
		}
		/***setting stated of edges***/
		setEdgeState();
		/*****************************/

		/***setting adj edges for each node***/
		setAdjEdgesNode();
		/*****************************/

	}
	private static void setEdgeState() {
		for(Edge e:Optimization.inputs.getedgeList()) {
			int indexI=e.getOrigin().getId();
			int indexJ=e.getEnd().getId();
			Optimization.inputs.getedge(indexI, indexJ).setState(Network[indexI][indexJ]);
		}

	}

	public static void setAdjEdgesNode() {
		int n= Optimization.inputs.getNodes().length;
		for(Node node:Optimization.inputs.getNodes()) {
			for(int j=0;j<n;j++) {
				if(States.Network[node.getId()][j]>0) {
					Edge e=Optimization.inputs.getedge(node.getId(), j);
					node.getAdjEdgesList().add(e);
				}
			}
		}}



}
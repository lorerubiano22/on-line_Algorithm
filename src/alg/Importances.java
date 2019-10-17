package alg;

public class Importances {

	/*** Assessment process***/

	public static Optimization Conditions() {
		int n= Optimization.inputs.getNodes().length;
		checkingConnections();
		computingImportance(); 
		/**Setting imputs CPLEX **/
		Optimization Opt=new Optimization (States.TravelTime,States.DisruptedNetwork,States.impEdges,States.importancesEdges ,States.victimsReward,States.victims);
		return Opt;
	}
	/************************************************************************************/



	/*** Checking connections by CPLEX ***/			
	static void checkingConnections() {
		new shortpathXpress();
		new ShortPath();
		/*************There is a feasible solution? path*******************/
		/*the connection of each node is assessed*/
		boolean path=false;
		boolean origin=false;
		for(Node nn:Optimization.inputs.getNodes()) {
			if(States.DisruptedNetwork[0][nn.getId()]>0 || States.DisruptedNetwork[nn.getId()][0]>0) {origin=true;}
			else{
				origin=shortpathXpress.solveMe(Optimization.inputs,States.TravelTime, States.DisruptedNetwork, States.victimsReward, States.victims,0,nn.getId());
				
				//origin=ShortPath.solveMe(Optimization.inputs,States.TravelTime, States.DisruptedNetwork, States.victimsReward, States.victims,0,nn.getId());
			}
			path=(origin);
			nn.setConnection(path);
			if(!nn.getConnection() && nn.getId()!=0 ) {nn.setImportance(0.0);}
		}
		Optimization.inputs.getNodes()[0].setConnection(true);
		/*After check the connectivity the state of the Nodes is updated */
		for(Node node:Optimization.inputs.getNodes()) {
			if(!node.getConnection()) {
				node.setImportance(0.0);
			}
		}
	}
	/************************************************************************************/

	/*** Computing importance***/
	public static void computingImportance() {
		importanceNodes();
		/*Testing if all nodes are connected with the network*/
		generatingAverage();
		importancesEdges();
	}
	/************************************************************************************/

	/*** Computing edges importance***/
	private static void importancesEdges() {
		int n=Optimization.inputs.getNodes().length;
		double[][] updatedImp= new double [n][n];
		double [][]impArc= new double [n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(i!=j && States.DisruptedNetwork[i][j]>0 && States.impAverage!=0) {
					double impOrigen=Optimization.inputs.getNode(i).getImportance();
					double impEnd=Optimization.inputs.getNode(j).getImportance();
					impArc[i][j]=(impOrigen+impEnd)*(0.5*States.victimsReward[i]+0.5*States.victimsReward[j])/(States.impAverage*States.TravelTime[i][j]);
					updatedImp[i][j]=impArc[i][j];}
				else {impArc[i][j]=0;updatedImp[i][j]=0.0;}
			}
		}
		//Updating importance on the system and the importance used for the routing
		States.setImpEdges(impArc);
	}
	/************************************************************************************/

	/****Computing importance Nodes***/
	private static void importanceNodes() {
		int n=Optimization.inputs.getNodes().length;
		double value=0;
		double[] adj= new double[n];
		for(int i=0;i<n;i++) {
			value=0;
			for(int j=0; j<n;j++) {
				if(States.DisruptedNetwork[i][j]!=0) {
					value++;
				}
			}
			adj[i]=value;
		}

		//importance: potential node
		for(Node nn:Optimization.inputs.getNodes()) {
			if(nn.getProfit()>1 && adj[nn.getId()]!=0 && nn.getConnection()) {
				nn.setImportance(nn.getProfit()/adj[nn.getId()]);
			}
			else {nn.setImportance(0.0);}
		}

		//importance: intermediate node nearby potential node
		for(Node nn:Optimization.inputs.getNodes()) {
			int i=nn.getId();
			double impInterNode=0;
			if(nn.getImportance()<=0 && nn.getConnection()) {
				for(int j=0;j<Optimization.inputs.getNodes().length;j++) {
					if(States.DisruptedNetwork[i][j]>0 && i!=j) {
						double impJ=0;
						if(Optimization.inputs.getNode(j).getImportance()!=0) {impJ=Optimization.inputs.getNode(j).getImportance();}
						impInterNode+=impJ;
					}
				}
				if(adj[i]!=0) {nn.setImportance(impInterNode/adj[i]);}
				else {nn.setImportance(0.0);}
			}
		}

		//importance: intermediate node nearby intermediate node 	
		for(Node nn:Optimization.inputs.getNodes()) {
			int i=nn.getId();
			double impInterNode=0;
			if(nn.getImportance()<=0 && nn.getConnection()) {
				for(int j=0;j<Optimization.inputs.getNodes().length;j++) {
					if(States.DisruptedNetwork[i][j]>0 && i!=j) {
						double impJ=0;
						if(Optimization.inputs.getNode(j).getImportance()!=0) {impJ=Optimization.inputs.getNode(j).getImportance();}
						impInterNode+=impJ;
					}
				}
				if(adj[i]!=0) {nn.setImportance(impInterNode/adj[i]);}
				else {nn.setImportance(0.0);}
			}
		}

		double[] ImpNodes= new double [n];
		for(Node node:Optimization.inputs.getNodes()) {
			if(!node.getConnection()) {node.setImportance(0.0);}
			ImpNodes[node.getId()]=node.getImportance();
		}

		States.setImpNodes(ImpNodes);
	}
	/************************************************************************************/


	/****Computing average importance***/

	private static void generatingAverage() {
		int n=Optimization.inputs.getNodes().length;
		/**List of importance nodes**/
		double[] sortImp= new double [n];
		for(Node node:Optimization.inputs.getNodes()) {
			if(node.getConnection()) {
				sortImp[node.getId()]=node.getImportance();}
			else {sortImp[node.getId()]=0;}
		} 
		/**List of distances**/
		double[] ListDist= new double [Optimization.inputs.getedgeList().size()];	
		int position = -1;
		for(Edge ed:Optimization.inputs.getedgeList()) {
			position++;
			if(States.DisruptedNetwork[ed.getOrigin().getId()][ed.getEnd().getId()]!=0) {
				//if(ed.getState()!=0) {
				ListDist[position]=ed.getTime();}
			else{ListDist[position]=0;}
		}

		/*********averages********/
		double Average=0;
		double AvergTV=mean(ListDist);
		double AvergU=mean(sortImp);
		if(AvergTV>0){Average=AvergU/AvergTV;}			
		States.setImpAverage(Average);
	}

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


	/************************************************************************************/

}

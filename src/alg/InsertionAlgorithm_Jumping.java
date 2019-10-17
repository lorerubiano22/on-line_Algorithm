package alg;


import java.util.LinkedList;

public class InsertionAlgorithm_Jumping {
	private Test aTest;
	private Inputs inputs;
	private double timesPC;
	private Solution sol;
	double temporing;
	/*Road network*/
	private LinkedList<Edge> blocked_EdgesList; // list of edges blocked
	public LinkedList<Node> desconected_nodesList; // list of unreachable nodes
	public LinkedList<Edge> visted_EdgesList; // list of blocked edges
	public LinkedList<Node> visted_nodesList; // list of blocked nodes
	private LinkedList<Node> notUsedNodes; // list of reachable nodes but no visited nodes
	private LinkedList<Node> notVisitedVictims; // list of reachable nodes but no visited nodes
	private LinkedList<Node> activeNodes;
	private LinkedList<Edge> activeEdges;
	private LinkedList<Edge> promisingEdges;
	private LinkedList<Edge> notUsedEdges;




	public InsertionAlgorithm_Jumping(Test myTest, Inputs myInputs) {
		aTest = myTest;
		inputs = myInputs;
		blocked_EdgesList=new LinkedList<Edge>(); // list of edges blocked
		desconected_nodesList= new LinkedList<Node>(); // list of unreachable nodes
		visted_EdgesList=new LinkedList<Edge>(); // list of blocked edges
		visted_nodesList= new LinkedList<Node>(); // list of blocked nodes
		notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
		notVisitedVictims= new LinkedList<Node>();
		activeNodes= new LinkedList<Node>();
		activeEdges=new LinkedList<Edge>(); 
		promisingEdges=new LinkedList<Edge>();
		notUsedEdges=new LinkedList<Edge>(); ;
	}

	public InsertionAlgorithm_Jumping(Test myTest, Inputs myInputs, Solution solution) {
		aTest = myTest;
		inputs = myInputs;
		sol=solution;
		temporing=0;
		blocked_EdgesList=new LinkedList<Edge>(); // list of edges blocked
		desconected_nodesList= new LinkedList<Node>(); // list of unreachable nodes
		visted_EdgesList=new LinkedList<Edge>(); // list of blocked edges
		visted_nodesList= new LinkedList<Node>(); // list of blocked nodes
		notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
		notVisitedVictims= new LinkedList<Node>();
		activeNodes= new LinkedList<Node>();
		activeEdges=new LinkedList<Edge>(); 
		promisingEdges=new LinkedList<Edge>();
		notUsedEdges=new LinkedList<Edge>(); 
	}

	public Solution solveMe() {
		callingNetwork();// the list of visited, non visited, blocked elements in the network are updating
		Node depotStart= Optimization.inputs.getNodes()[0];
		Node depotEnd=Optimization.inputs.getNodes()[0];
		if(notVisitedVictims.size()!=0) {
		//if(exploring()) {
			//if(promisingEdges.size()!=0) {
			//if(notUsedNodes.size()!=0) {
			visted_nodesList.add(new Node (depotStart));
			double operationTime=0;
			if(visted_EdgesList.size()!=0) {
				depotStart=visted_EdgesList.get((visted_EdgesList.size()-1)).getEnd();
			}
			Node prevEnd= Optimization.inputs.getNodes()[0];
			Edge finalEdge= Optimization.inputs.getedge(prevEnd.getId(), depotEnd.getId());
			/* Assesing the last visited edge was a disrupted one*/
			if(!visted_EdgesList.isEmpty()) {
				depotStart= new Node(visted_EdgesList.get((visted_EdgesList.size()-1)).getEnd());
			}
			if(!visted_EdgesList.isEmpty()) {
				for(Edge e:visted_EdgesList) {
					operationTime+=e.getTime();
				}
			}
			else {operationTime=0;}
			if(operationTime<Optimization.inputs.DrivingRange){
				doInsertion(depotStart,depotEnd,operationTime);
			}
			//visted_EdgesList.add(finalEdge);
			sol= new Solution();
			Route auxRoute= new Route();
			LinkedList<Edge> Edges= new LinkedList<Edge>();
			for(Edge e:visted_EdgesList) {
				if(e.getOrigin().getId()!=e.getEnd().getId()) {
					Edges.add(new Edge(e));
				}
			}


			auxRoute.setEdges(Edges);
			auxRoute.calcTime();
			auxRoute.calcDistance();
			
			//auxRoute.setCoverage(visted_nodesList.size());
			auxRoute.calcCoverage(); 
			sol.addRoute(auxRoute);
			sol.updatingSolutionAttributes(Optimization.inputs);
			operationTime=sol.getTotalTime();
		}
		if(notVisitedVictims.size()==0) {
		//if(promisingEdges.size()==0 || notVisitedVictims.size()==0) {
			OnlineSistem.setmaxIter(0);}
		else {OnlineSistem.setmaxIter(promisingEdges.size());}

		return sol;
	}

	private boolean exploring() {
		boolean explorationProcess=false;
		if(importanceMainCriterion()) {
			if(promisingEdges.size()!=0) {
				explorationProcess=true;
			}
		}
		else{if(notUsedNodes.size()!=0) {
			explorationProcess=true;
		}}

		return explorationProcess;
	}

	private boolean importanceMainCriterion() {
		boolean critetion=false;
		if(Optimization.aTest.getOptcriterion()==1100 )
				//|| 
				//Optimization.aTest.getOptcriterion()==1210 || 
				//Optimization.aTest.getOptcriterion()==1012 || 
				//Optimization.aTest.getOptcriterion()==1120 ||
				//Optimization.aTest.getOptcriterion()==1102 ||
				//Optimization.aTest.getOptcriterion()==1021 ||
				//Optimization.aTest.getOptcriterion()==1201) 
				{critetion=true;}
		return critetion;
	}

	public void doInsertion(Node startRoute, Node endRoute, double operationTime) {
		/*Temporing t*/
		double distance=operationTime;
		boolean disruption=false;
		/*Promising stores the active edges with nodes not visited*/
		if(promisingEdges.size()!=0 || notVisitedVictims.size()!=0) {
			//if(notUsedEdges.size()!=0) {
			//if(notUsedNodes.size()!=0 ) {
			/*The list of adjacent edges is copy*/
			LinkedList<Edge> AdjEdges= new LinkedList<Edge>();
				AdjEdges=recoverRoute(startRoute,endRoute,disruption);
				if(AdjEdges.size()!=0) {
					/*Sorting the list of adjacent list*/
					sortingAdj(AdjEdges,disruption);
					Edge best= new Edge (AdjEdges.get(0));
					
					if(best.getEnd().getId()==startRoute.getId() || best.getOrigin().getId()==startRoute.getId()) {
						if(best.getEnd().getId()==startRoute.getId()) {
							best=inputs.getedge(best.getEnd().getId(), best.getOrigin().getId());}
						visted_EdgesList.add(new Edge(best));
						visted_nodesList.add(new Node (best.getEnd()));
						removeNode(best.getEnd());
						removeEdge(best);
						
					}
					/*Selecting the shortest path to go from the node startRoute to the edge best*/
					else {
					Edge ibest= inputs.getedge(startRoute.getId(), best.getOrigin().getId());
					Edge jbest= inputs.getedge(startRoute.getId(), best.getEnd().getId());
					//Edge toRecourse;
					Edge toRecourse=connectionTObest(ibest,jbest);

					if(toRecourse.getEnd().getId()!=best.getOrigin().getId()) {
						if(toRecourse.getEnd().getId()==best.getEnd().getId()) {
							best=inputs.getedge(best.getEnd().getId(), best.getOrigin().getId());}
					}
					if(distance+toRecourse.getTime()+best.getTime()<inputs.getDrivingRange()) {
						visted_EdgesList.add(new Edge(toRecourse));
						visted_nodesList.add(new Node (toRecourse.getEnd()));
						visted_EdgesList.add(new Edge(best));
						visted_nodesList.add(new Node (best.getEnd()));
						removeNode(toRecourse.getEnd());
						removeEdge(toRecourse);
						removeNode(best.getEnd());
						removeEdge(best);
					}
				}
//					if(bestDisrupted(best)) {sol.getRoutes().getFirst().setLastconnections(-1);}
//					else {sol.getRoutes().getFirst().setLastconnections(1);}	
				}

				else {
					if(notUsedNodes.size()==1) {
						//if(!importanceMainCriterion()) {
						/*Selecting the shortest path to go from the node startRoute to the edge best*/
						Edge ibest= inputs.getedge(startRoute.getId(), notUsedNodes.get(0).getId());
						Edge jbest= inputs.getedge(startRoute.getId(), notUsedNodes.get(0).getId());
						Edge toRecourse;
						if(ibest.getDistance()<jbest.getDistance()) {
							toRecourse= new Edge(ibest);

						}
						else {toRecourse= new Edge(jbest);
						if(distance+toRecourse.getTime()<inputs.getDrivingRange()) {
							visted_EdgesList.add(new Edge(toRecourse));
							visted_nodesList.add(new Node (toRecourse.getEnd()));
							removeNode(toRecourse.getEnd());
							removeEdge(toRecourse);
						}
						}
						//}
					}

				}
			
		}		
	}


		private boolean bestDisrupted(Edge best) {
		boolean states= false;
		int i= best.getOrigin().getId();
		int j=best.getEnd().getId();
		if(States.Disruptions[i][j]==0) {
			states=true;
		}
		return states;
	}

	private Edge connectionTObest(Edge ibest, Edge jbest) {
		Edge toRecourse= new Edge(ibest);
		/*is visited*/
		if(!isVisited(ibest) && !isVisited(jbest)) {
			/*Distance*/
			if(Optimization.aTest.getOptcriterion()==1010 || Optimization.aTest.getOptcriterion()==1210 || Optimization.aTest.getOptcriterion()==1012) {
				if(ibest.getDistance()<jbest.getDistance()) {
					toRecourse= new Edge(ibest);}
				else {toRecourse= new Edge(jbest);}}

			/*Importance*/
			if(Optimization.aTest.getOptcriterion()==1120 || Optimization.aTest.getOptcriterion()==1100 || Optimization.aTest.getOptcriterion()==1102) {
				if(ibest.getImportance()>jbest.getImportance()) {
					toRecourse= new Edge(ibest);}
				else {toRecourse= new Edge(jbest);}}

			/*Importance*/
			if(Optimization.aTest.getOptcriterion()==1021 || Optimization.aTest.getOptcriterion()==1201 || Optimization.aTest.getOptcriterion()==1001) {
				if(ibest.getDistance()<jbest.getDistance()) {
					toRecourse= new Edge(ibest);}
				else {toRecourse= new Edge(jbest);}}
		}

		/*Any edge is no visited*/
		else {

			if(!isVisited(ibest)) {
				toRecourse= new Edge(ibest);
			}
			else {toRecourse= new Edge(jbest);}
		}

		return toRecourse;
	}

	private LinkedList<Edge> recoverRoute(Node startRoute, Node endRoute, boolean disruption) {
		int nNodes=Optimization.inputs.getNodes().length;
		LinkedList<Edge> newadjList= new LinkedList<Edge>();
		LinkedList<Edge> edgesList= new LinkedList<Edge>();
		//
		//for(Edge e:Optimization.inputs.getedgeList()) {
		for(Edge e:promisingEdges) {
			//for(Edge e:activeEdges) {
			if(e.getOrigin().getId()!=e.getEnd().getId()) {
				if(e.getEnd().getId()!=0) {
					if(!isVisited(e.getEnd()) || !isVisited(e.getOrigin())) {
						if(!isVisited(Optimization.inputs.getedge(startRoute.getId(), e.getEnd().getId())) || !isVisited(Optimization.inputs.getedge(startRoute.getId(), e.getOrigin().getId()))) {
							edgesList.add(e);
						}
					}
				}}
		}

		sortingAdj(edgesList,disruption);
		LinkedList<Edge> promisingList= new LinkedList<Edge>();
		for(Edge e:edgesList) {
			if(e.getOrigin().getId()!=e.getEnd().getId()) {
				//if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
				if(!isVisited(e.getOrigin()) && !isVisited(e.getEnd())) {
					//if(startRoute.getId()!= e.getOrigin().getId()) {
					//if(startRoute.getId()!=e.getEnd().getId()) {
					promisingList.add(e);
				}
			}

		}



		if(promisingList.size()==0) {
			if(edgesList.size()!=0) {
				Edge best=edgesList.get(0);
				if(best.getEnd().getId()!=best.getOrigin().getId()) {
					if(startRoute.getId()!= best.getOrigin().getId()) {newadjList.add(Optimization.inputs.getedge(startRoute.getId(), best.getOrigin().getId()));}

					if(startRoute.getId()!=best.getEnd().getId()) {newadjList.add(Optimization.inputs.getedge(startRoute.getId(), best.getEnd().getId()));}
				}}}
		else { return promisingList; }

		return newadjList;
	}
	/*** Selecting edges directly connected with the depot ***/
	private LinkedList<Edge> getEdges(Node startRoute, Node endRoute, boolean disruption) {
		int n=Optimization.inputs.getNodes().length;
		LinkedList<Edge> AdjEdges= new LinkedList<Edge>();
		if(!disruption) {
			for(Edge e:startRoute.getAdjEdgesList()) {
				if(visted_EdgesList.size()==0) {AdjEdges.add(e);}
				else{if(e.getEnd().getId()!=e.getOrigin().getId()) {
					if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
						if(!isVisited(e.getEnd())) {
							if(e.getEnd().getId()!=0) {
								AdjEdges.add(e);
							}
						}}
				}}
			}
		}
		return AdjEdges;
	}
	/**************************************************************************************/
	/**************************************************************************************/

	/*** Assessing if the selected edge is already visited ***/
	private boolean isVisited(Edge adj) {
		boolean toVisit=false;
		for(Edge e:visted_EdgesList) {
			if(e.getOrigin().getId()==adj.getOrigin().getId() && e.getEnd().getId()==adj.getEnd().getId()) {
				toVisit=true;
				break;
			}
			else {toVisit=false;}
		}
		return toVisit;
	}

	private boolean isVisited(Node n) {
		boolean isvisited=false;
		for(Node node:visted_nodesList) {
			if(node.getId()==n.getId()) {
				isvisited=true;
				break;}
			else {isvisited=false;}
		}
		return isvisited;
	}



	private boolean promisingVisit(Edge ed, boolean disruption) {
		boolean tovisit=false;
		int nvisits=0;
		for(Edge e:visted_EdgesList) {
			if(ed.getEnd().getId()==e.getEnd().getId()) {nvisits++;}
		}
		/*Si el nodo no ha sido visitado este se incluye en la lista de ser visitados para conocer su condición*/
		if(nvisits<1) {
			tovisit=true;
		}
		else{
			/********CASES*******/
			/*1. El nodo ya ha sido visitado pero pertenece al unico edge adjacente en ese caso el arco se inserta*/
			if(ed.getOrigin().getAdjEdgesList().size()==1) {
				tovisit=true;
			}
			/*2. No es el unico arco adjacente pero todos los nodos adj ya han sido visitados solo se escoge el mejor*/
			else {
				LinkedList<Edge> missingEdges=new LinkedList<Edge>();
				for(Edge e:ed.getOrigin().getAdjEdgesList()) {
					if(!isVisited(e)) {
						missingEdges.add(e);
					}
				}
				/*si todos los arcos adj al nodo ya han sido visitados entonces esta lista estará vacia y el edge ed será el más prometedor. */
				if(missingEdges.size()==0) {tovisit=true;}
				/*si la lista no esta vacia es porque existen edges adjacentes que no han sido visitado*/
				else {tovisit=false;}
				/*3. No es el unico arco adjacente y alguno no ha ido visitado, en este caso los no visitados tiene prioridad*/

			}}
		//visted_EdgesList
		return tovisit;
	}



	private boolean promissingToInset(Edge ed, Edge e) {
		boolean promissingToInset= false;
		//evalue si esos adj son el nodo de fine
		if(e.getEnd().getId()!=0 ) {
			if(ed.getEnd().getId()!=e.getEnd().getId()) {
				if(ed.getOrigin().getId()!=e.getEnd().getId()) 	
				{
					int nvisits=0;
					for(Edge visted:visted_EdgesList) {
						if(e.getEnd().getId()==visted.getEnd().getId()) {nvisits++;}
						if(nvisits>1) {promissingToInset= false;
						break;}
						else {promissingToInset=true;}
					}
				}
			}}
		return promissingToInset;
	}

	private void sortingAdj(LinkedList<Edge> adjorigen, boolean disruption) {

		/*Importance*/
		if(Optimization.aTest.getOptcriterion()==1100) {
			adjorigen.sort(Edge.importanceComp);
		}

		/*Distance*/
		if(Optimization.aTest.getOptcriterion()==1010) {
			adjorigen.sort(Edge.minDistance);
		}

		/*Weighted criterio*/
		if(Optimization.aTest.getOptcriterion()==1001) {
			adjorigen.sort(Edge.optCriterionComp);
		}

		/***LEXICOGRAPHIC STRATEGY***/		
		/*IMPORTANCE(1)*/
		if(disruption==false && Optimization.aTest.getOptcriterion()==1120) {
			adjorigen.sort(Edge.importanceComp);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1120) {
			adjorigen.sort(Edge.minDistance);
		}

		/*IMPORTANCE(1)- DISTANCE(2)*/
		if(disruption==false && Optimization.aTest.getOptcriterion()==1120) {
			adjorigen.sort(Edge.importanceComp);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1120) {
			adjorigen.sort(Edge.minDistance);
		}

		/*IMPORTANCE(1)- WEIGHT(2)*/
		if(disruption==false && Optimization.aTest.getOptcriterion()==1102) {
			adjorigen.sort(Edge.importanceComp);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1102) {
			adjorigen.sort(Edge.optCriterionComp);
		}

		/*DISTANCE(1) - IMPORTANCE(2)*/
		if(disruption==false && Optimization.aTest.getOptcriterion()==1210) {
			adjorigen.sort(Edge.minDistance);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1210) {
			adjorigen.sort(Edge.importanceComp);
		}

		/*DISTANCE(1)- WEIGHT(2)*/ 
		if(disruption==false && Optimization.aTest.getOptcriterion()==1012) {
			adjorigen.sort(Edge.minDistance);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1012) {
			adjorigen.sort(Edge.optCriterionComp);
		}	

		/*WEIGHT(1) - IMPORTANCE(2)*/
		if(disruption==false && Optimization.aTest.getOptcriterion()==1201) {
			adjorigen.sort(Edge.optCriterionComp);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1201) {
			adjorigen.sort(Edge.importanceComp);
		}

		/*WEIGHT(1)-DISTANCE(2)*/ 
		if(disruption==false && Optimization.aTest.getOptcriterion()==1021) {
			adjorigen.sort(Edge.optCriterionComp);
		}
		/*Disruption*/
		if(disruption && Optimization.aTest.getOptcriterion()==1021) {
			adjorigen.sort(Edge.minDistance);
		}					

	}





	private int visitCounter(LinkedList<Edge> visted_EdgesList2, Edge ed) {
		int visits=0;
		for(Edge edge:visted_EdgesList) {
			if(ed.getOrigin().getId()==edge.getOrigin().getId() &&  ed.getEnd().getId()== edge.getEnd().getId() || ed.getEnd().getId()==edge.getOrigin().getId() &&  ed.getOrigin().getId()== edge.getEnd().getId()) {
				visits++;
			}
		}
		return visits;
	}


	private void removeNode(Node startRoute) {
		for(int i=0;i<notUsedNodes.size();i++) {
			Node nn=notUsedNodes.get(i);
			if(nn.getId()==startRoute.getId()) {
				notUsedNodes.remove(i);
				int victim=-1;
				for(Node n:notVisitedVictims) {
					if(n.getId()==nn.getId()) {
						victim++;
						notVisitedVictims.remove(victim);
						break;
					}
				}
			}
		}
	}


	private void removeEdge(Edge insertedEdge) {
		for(int i=0;i<notUsedEdges.size();i++) {
			Edge en=notUsedEdges.get(i);
			if(en.getOrigin().getId()==insertedEdge.getOrigin().getId() && en.getEnd().getId()==insertedEdge.getEnd().getId()) {
				notUsedEdges.remove(i);
			}
		}
		for(int i=0;i<notUsedEdges.size();i++) {
			Edge en=notUsedEdges.get(i);
			if(en.getEnd().getId()==insertedEdge.getOrigin().getId() && en.getOrigin().getId()==insertedEdge.getEnd().getId()) {
				notUsedEdges.remove(i);
			}
		}
	}

	private void updatingVisitedElements() {
		//if(sol.getRoutes().getFirst().getEdges().size()==0) {}
		if(sol.getRoutes().size()==0) {
			visted_EdgesList= new LinkedList<Edge>();
			visted_nodesList= new LinkedList<Node>();
			notUsedNodes= new LinkedList<Node>();
			notUsedEdges=new LinkedList<Edge>(); }
		else {
			/*Visited edges*/
			LinkedList<Edge> EdgesSol= new LinkedList<Edge>();
			for(Route r:sol.getRoutes()) {
				for(Edge e:r.getEdges()) {
					Edge d = new Edge(e);
					EdgesSol.add(d);
				}
			}
			/* Visited nodes*/
			LinkedList<Node> NodesSol= new LinkedList<Node>();
			for(Route r:sol.getRoutes()) {
				for(Edge e:r.getEdges()) {
					Node nn= new Node (e.getEnd());
					NodesSol.add(nn);
				}
			}
			/*Copying visited edges*/
			visted_EdgesList= new LinkedList<Edge>();
			visted_nodesList= new LinkedList<Node>();
			for(Edge e:EdgesSol ) {

				visted_EdgesList.add(e);}
			/*Copying nodes*/

			for(Node node:NodesSol) {
				if(node.getId()!=0 ) {
					visted_nodesList.add(node);}}

		}

	}

	private void callingNetwork() {
		updatingVisitedElements();
		updatingNONvisitedElements();
		updatingblockedElements();
	}

	private void updatingblockedElements() {
		blocked_EdgesList=new LinkedList<Edge>(); // list of edges blocked
		desconected_nodesList= new LinkedList<Node>(); // list of unreachable nodes
		/*Blocked edges*/
		LinkedList<Edge> blockedEdges= new LinkedList<Edge>();
		for (Edge e:Optimization.inputs.getedgeList()) {
			if(e.getState()==0) {blockedEdges.add(new Edge(e));}
		}
		for(Edge e:blockedEdges) {
			blocked_EdgesList.add(new Edge (e));
		}
		/*Blocked nodes*/
		LinkedList<Node> blockedNodes= new LinkedList<Node>();
		for(Node node:Optimization.inputs.getNodes()) {
			if(!node.getConnection()) {blockedNodes.add(node);}
		}
		for(Node node:blockedNodes) {
			desconected_nodesList.add(node);
		}
	}


//	private void updatingNONvisitedElements() {
//		if(sol.getRoutes().size()==0) {
//			notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
//			notVisitedVictims= new LinkedList<Node>();
//			activeNodes= new LinkedList<Node>();
//			activeEdges=new LinkedList<Edge>(); 
//			promisingEdges=new LinkedList<Edge>();
//			notUsedEdges=new LinkedList<Edge>(); 
//		}
//		/*active edges*/
//		activeNodes= new LinkedList<Node>();
//		activeEdges=new LinkedList<Edge>(); 
//		promisingEdges=new LinkedList<Edge>();
//		LinkedList<Edge> edgesList= new LinkedList<Edge>();
//		
//		/*Selecting the promising edges*/
//		for (Edge e:Optimization.inputs.getedgeList()) {
//			if(importanceMainCriterion()) {
//				if(e.getState()>0) {
//					if(e.getOrigin().getId()!=e.getEnd().getId()) {
//						if(e.getOrigin().getId()!=0 && e.getEnd().getId()!=0) {
//							edgesList.add(new Edge(e));}}}
//			}
//			else {
//				if(e.getOrigin().getId()!=e.getEnd().getId()) {
//					if(e.getOrigin().getId()!=0 && e.getEnd().getId()!=0) {
//						edgesList.add(new Edge(e));}}}
//		}
//		for(Edge e:edgesList) {
//			activeEdges.add(new Edge (e));
//		}
//
//		/*active nodes*/
//		LinkedList<Node> nodesList= new LinkedList<Node>();
//		for(Node node:Optimization.inputs.getNodes()) {
//			if(importanceMainCriterion()) {
//				if(node.getConnection()) {nodesList.add(node);}
//			}
//			else {nodesList.add(node);}
//		}
//		for(Node node:nodesList) {
//			activeNodes.add(node);
//		}
//
//		LinkedList<Edge> nonvisitededgesList= new LinkedList<Edge>();
//		LinkedList<Node> nonvisitedList= new LinkedList<Node>();
//
//		/*Edges*/
//		//for (Edge e:Optimization.inputs.getedgeList()) {
//		for (Edge e:activeEdges) {
//			//if(isVisited(e)) {}
//			if(!isVisited(e)) {nonvisitededgesList.add(new Edge(e));}
//		}
//		/*Nodes*/
//		for (Node e:Optimization.inputs.getNodes()) {
//			if(e.getId()!=0) 
//			{if(!isVisited(e)) {nonvisitedList.add(e);}}
//		}
//		notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
//		notVisitedVictims= new LinkedList<Node>();
//		notUsedEdges=new LinkedList<Edge>();
//		promisingEdges=new LinkedList<Edge>();
//		if(nonvisitedList.size()!=0 && nonvisitededgesList.size()!=0) {
//			for(Edge e:nonvisitededgesList) {
//				if(!isVisited(e)) {
//					notUsedEdges.add(e);
//				}
//			}
//
//			for(Node node:nonvisitedList) {
//				//if(!isVisited(node) && node.getId()!=0) {
//				if(node.getId()!=0) {
//					notUsedNodes.add(node);
//					if(node.getProfit()>1) {
//						notVisitedVictims.add(node);
//					}
//				}
//			}
//			/*Promising edges*/
//			if(nonvisitedList.size()!=0 && nonvisitededgesList.size()!=0) {
//				for(Edge e:nonvisitededgesList) {
//
//					//if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
//					if(e.getEnd().getId()!=0 && e.getEnd().getId()!=0) {
//						//if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
////							if(importanceMainCriterion()) {
////								if(!isVisited(e.getOrigin()) && !isVisited(e.getEnd())) {promisingEdges.add(e);
////								}
////							}
//							//else{
//								promisingEdges.add(e);
//							//	}
//							//}
//					}
//				}}
//			/**/
//
//		}
//	}

	
	
	private void updatingNONvisitedElements() {
		if(sol.getRoutes().size()==0) {
			notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
			notVisitedVictims= new LinkedList<Node>();
			activeNodes= new LinkedList<Node>();
			activeEdges=new LinkedList<Edge>(); 
			promisingEdges=new LinkedList<Edge>();
			notUsedEdges=new LinkedList<Edge>(); 
		}
		/*active edges*/
		activeNodes= new LinkedList<Node>();
		activeEdges=new LinkedList<Edge>(); 
		promisingEdges=new LinkedList<Edge>();
		LinkedList<Edge> edgesList= new LinkedList<Edge>();
		for (Edge e:Optimization.inputs.getedgeList()) {
			if(importanceMainCriterion()) {
				if(e.getState()>0) {
					if(e.getOrigin().getId()!=e.getEnd().getId()) {
						if(e.getOrigin().getId()!=0 && e.getEnd().getId()!=0) {
							edgesList.add(new Edge(e));}}}
			}
			else {
				if(e.getOrigin().getId()!=e.getEnd().getId()) {
					if(e.getOrigin().getId()!=0 && e.getEnd().getId()!=0) {
						edgesList.add(new Edge(e));}}}
		}
		for(Edge e:edgesList) {
			activeEdges.add(new Edge (e));
		}

		/*active nodes*/
		LinkedList<Node> nodesList= new LinkedList<Node>();
		for(Node node:Optimization.inputs.getNodes()) {
			if(importanceMainCriterion()) {
				if(node.getConnection()) {nodesList.add(node);}
			}
			else {nodesList.add(node);}
		}
		for(Node node:nodesList) {
			activeNodes.add(node);
		}

		LinkedList<Edge> nonvisitededgesList= new LinkedList<Edge>();
		LinkedList<Node> nonvisitedList= new LinkedList<Node>();

		/*Edges*/
		//for (Edge e:Optimization.inputs.getedgeList()) {
		for (Edge e:activeEdges) {
			//if(isVisited(e)) {}
			if(!isVisited(e)) {nonvisitededgesList.add(new Edge(e));}
		}
		/*Nodes*/
		for (Node e:Optimization.inputs.getNodes()) {
			if(e.getId()!=0) 
			{if(!isVisited(e)) {nonvisitedList.add(e);}}
		}
		notUsedNodes= new LinkedList<Node>(); // list of reachable nodes but no visited nodes
		notVisitedVictims= new LinkedList<Node>();
		notUsedEdges=new LinkedList<Edge>();
		promisingEdges=new LinkedList<Edge>();
		if(nonvisitedList.size()!=0 && nonvisitededgesList.size()!=0) {
			for(Edge e:nonvisitededgesList) {
				if(!isVisited(e)) {
					notUsedEdges.add(e);
				}
			}

			for(Node node:nonvisitedList) {
				if(!isVisited(node) && node.getId()!=0) {
					notUsedNodes.add(node);
					if(node.getProfit()>1) {
						notVisitedVictims.add(node);
					}
				}
			}
			/*Promising edges*/
			if(nonvisitedList.size()!=0 && nonvisitededgesList.size()!=0) {
				for(Edge e:nonvisitededgesList) {

					//if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
					if(e.getEnd().getId()!=0 && e.getEnd().getId()!=0) {
						if(e.getState()>0) {
						if(!isVisited(e.getOrigin()) && !isVisited(e.getEnd())) {
						//if(!isVisited(e.getOrigin()) || !isVisited(e.getEnd())) {
							//							if(importanceMainCriterion()) {
							//								if(!isVisited(e.getOrigin()) && !isVisited(e.getEnd())) {promisingEdges.add(e);
							//								}
							//							}
							//else{
							promisingEdges.add(e);
							//	}
						}
						}
					}
				}}
			/**/

		}
	}



	/*Setters*/
	public void setBlocked_EdgesList(LinkedList<Edge> blocked_EdgesList) {	this.blocked_EdgesList = blocked_EdgesList;}
	public void setDesconected_nodesList(LinkedList<Node> desconected_nodesList) {	this.desconected_nodesList = desconected_nodesList;}
	public void setnotUsedNodesList(LinkedList<Node> nodesList) {	this.notUsedNodes = nodesList;}
	public void setSol(Solution sol) {this.sol = sol;}
	public void setInputs(Inputs inp) {this.inputs = inp;}
	public void setTest(Test t) {this.aTest = t;}
	public void setTimesPC(double tPC) {this.timesPC += tPC;}
	/*Getters*/
	public LinkedList<Node>  getDesconected_nodesList() {return desconected_nodesList;}
	public LinkedList<Edge> getBlocked_EdgesList() {return blocked_EdgesList;}
	public LinkedList<Node>  getnotUsedNodesList() {return notUsedNodes;}
	public Inputs  getInputs() {return inputs;}
	public Test  getTest() {return aTest;}
	public Solution getSol() {return sol;}
	public double getTimesPC() {return timesPC;}



}

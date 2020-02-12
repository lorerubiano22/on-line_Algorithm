package alg;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;



public class PrintingReveleadNetwork {

	public void printingInformationsofar(Test aTest,int totalDetectedDisruption,
			Map<String, Edge> visitedRoadConnections   , Map<String, Edge> originialEdgeRoadConnection,
			Map<String, Edge> revealedDisruptedEdges, Map<Integer, Node> checkedAccesibiliyVictims, Map<Integer, Node> connectedNodestoRevealedRoadNetwork, String string)  {
		// writeLinkedList(TV_file, Event.edgeRoadConnection,Event.DisruptedEdges,Event.DisruptedRoadNetwork,false);
		String name= "Instance_"+aTest.getInstanceName()+"seed_"+aTest.getseed()+"disruption_"+aTest.getpercentangeDisruption()+"_OPTCriterion_"+aTest.getOptcriterion()+"_Alpha_"+aTest.getpercentageDistance()+"_"+string+"_.txt";

		FileWriter fileWriter ;
		try {
			fileWriter = new FileWriter(
					"C:/Users/Lorena/Documents/wokspace_Java_BOKU/on-line_Algorithm/Outputs/"+name,
					true);

			PrintWriter bw = new PrintWriter(fileWriter);
			bw.println("\n\n"+"********"+"\n");
			bw.println("Working_Edges_"+totalDetectedDisruption+"\n");
			for(Edge e:originialEdgeRoadConnection.values()) {
				if(visitedRoadConnections.containsKey(e.getKey())) {
					if(!revealedDisruptedEdges.containsKey(e.getKey())) {
						if(e.getOrigin().getId()>e.getEnd().getId()) {
							//bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId()+"_Distance_"+e.getDistance()+"_road_Distance_"+e.getDistanceRoad()+ "_Connectivity_"+e.getConnectivity()+"_weight_"+e.getWeight());
							bw.println(+e.getOrigin().getId()+"  "+e.getEnd().getId());

						}
					}
				}
			}

			bw.println("Revelead_disrupted_Edges");
			for(Edge e:revealedDisruptedEdges.values()) {
				if(e.getOrigin().getId()>e.getEnd().getId()) {
					bw.println(+e.getOrigin().getId()+" "+e.getEnd().getId());
				}
			}

			bw.println("Checked_victims_accesibility");
			for(Node v:checkedAccesibiliyVictims.values()) {
				if (connectedNodestoRevealedRoadNetwork.containsKey(v.getId())) {
					bw.println(v.getId()+ "_checked_accesibility_"+"Reacheable"  );
				}
				else {
					bw.println(v.getId()+ "_checked_accesibility_"+"Unreacheable"  );
				}
			}

			bw.flush();
			bw.close();
		}

		catch (IOException e) {
			//why does the catch need its own curly?
		}
		//printingNetworkStructure(revealedDisruptedRoadConnections,aTest);
	}

	private void printingNetworkStructure(Map<String, Edge> revealedDisruptedRoadConnections, Test aTest)  {
		ArrayList<Edge> aux= new ArrayList<>();
		for(Edge e:revealedDisruptedRoadConnections.values()) {
			aux.add(e);
		}
		//DrawingNetwork net=	new DrawingNetwork(aux,aTest);
		//net.g.dispose();

		File x=new File("c.png");
		//ImageIO.write(net.rendImage, "png", x);

	}





}

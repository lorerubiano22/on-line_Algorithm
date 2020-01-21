package alg;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class Outputs {
	/* INSTANCE FIELDS & CONSTRUCTOR */

	private JumpingMovement Jumping_Strategy;
	private BackandForwardMovement Back_Strategy;
	private ArrayList<Outputs> list = null;
	private Inputs inputs;

	public void setList() {
		list.add(this);
	}

	/* SET METHODS */

	public Outputs(Inputs inp, Test t, BackandForwardMovement obSol) {
		inp = inputs;
		this.setBack(obSol);

	}

	public Outputs(Inputs inp, Test t, JumpingMovement obSol) {
		this.setJumping(obSol);
		inp = inputs;

	}

	public Outputs(Disaster Event, Inputs inp, Test aTest) {
		double alpha = 0;
		if (aTest.getOptcriterion() == 1001) {
			alpha = aTest.getOptcriterion();
		}
		String Disrup_file = new String(aTest.getInstanceName() + "_Edges_Disruptions" + "_Seed" + aTest.getseed()
				+ "_P(disruption)_" + aTest.getpercentangeDisruption() + "_Alpha_" + alpha + "_" + "Disruptions.txt");
		writeLinkedList2(Disrup_file, Event.edgeRoadConnection, Event.DisruptedEdges, Event.DisruptedRoadConnections,
				false);

	}

	public void setJumping(JumpingMovement obSol) {
		Jumping_Strategy = obSol;
		printJumpSol();
	}

	public void setBack(BackandForwardMovement obSol) {
		Back_Strategy = obSol;
		printBackSol();
	}

	public JumpingMovement getJumping() {
		return Jumping_Strategy;
	}

	public BackandForwardMovement getBack() {
		return Back_Strategy;
	}

	public void writeLinkedList2(String tV_file, LinkedList<Edge> edgeRoadConnection,
			HashMap<String, Edge> disruptedEdges, LinkedList<Edge> disruptedRoadNetwork, boolean b) {

		try {
			PrintWriter bw = new PrintWriter(tV_file);
			bw.println("Road_Connections");
			for (Edge e : edgeRoadConnection) {

				if (e.getOrigin().getId() > e.getEnd().getId()) {

					bw.println("(" + e.getOrigin().getId() + "," + e.getEnd().getId() + ")_" + "_Distance_"
							+ e.getDistance() + "_Road_Distance_" + e.getDistanceRoad() + "_Connectivity_"
							+ e.getConnectivity() + "_Weight_" + e.getWeight());
				}
			}

			bw.println("Disrupted_Edge");
			if (disruptedEdges != null) {
				for (Edge e : disruptedEdges.values()) {

					if (e.getOrigin().getId() > e.getEnd().getId()) {
						bw.println("(" + e.getOrigin().getId() + "," + e.getEnd().getId() + ")_" + "_Distance_"
								+ e.getDistance() + "_Road_Distance_" + e.getDistanceRoad() + "_Connectivity_"
								+ e.getConnectivity() + "_Weight_" + e.getWeight());

					}
				}
			}
			bw.println("Disrupted_Network");
			for (Edge e : disruptedRoadNetwork) {
				if (e.getOrigin().getId() > e.getEnd().getId()) {
					bw.println("(" + e.getOrigin().getId() + "," + e.getEnd().getId() + ")_" + "_Distance_"
							+ e.getDistance() + "_Road_Distance_" + e.getDistanceRoad() + "_Connectivity_"
							+ e.getConnectivity() + "_Weight_" + e.getWeight());

				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {

		}

	}

	public void printJumpSol() {
		/*
		 * Script with routes per instance
		 */
		try {
			double alpha = 0;
			if (this.Jumping_Strategy.getJump_Sol() != null) {
				if (this.Jumping_Strategy.getaTest().getOptcriterion() == 1001) {
					alpha = this.Jumping_Strategy.getaTest().getOptcriterion();
				}
				String file_name = new String(this.Jumping_Strategy.getaTest().getInstanceName() + "_SOLUTION_"
						+ "_p(disruption)_" + this.Jumping_Strategy.getaTest().getpercentangeDisruption() + "_seed_"
						+ this.Jumping_Strategy.getaTest().getseed() + "_Strategy_Jumping_OptCriterion_"
						+ this.Jumping_Strategy.getaTest().getOptcriterion() + "_Alpha_" + alpha + "_Output.txt");
				PrintWriter out = new PrintWriter(file_name);
				out.printf("*********************************\n");
				out.printf("*********************************\n");

				out.printf("*********************************\n");
				out.printf("      Jumping___________" + this.Jumping_Strategy.getaTest().getpercentangeDisruption()
						+ "  " + this.Jumping_Strategy.getaTest().getOptcriterion());
				out.println("\n");
				out.printf("*********************************\n");
				out.println("\n");
				out.printf("*********************************\n");
				out.printf("      Orientation Route    \n");
				out.println("\n");
				out.print(Jumping_Strategy.getJump_Sol().toString());
				out.println("\n");
				out.printf("*********************************\n");
				out.printf("      Victims     \n");
				out.println("\n");
				out.println("ID      State");
				for (Node v : Jumping_Strategy.getVictimList().values()) {
					if (!Jumping_Strategy.getConnectedNodestoRevealedRoadNetwork().containsKey(v.getId())) {
						out.print(v.getId() + "      Unreachable");
						out.println("\n");
					} else {
						out.print(v.getId() + "      reachable");
						out.println("\n");
					}
				}
				out.close();
			}

		} catch (IOException exception) {
			System.out.println("Error processing output file: " + exception);
		}
	}// end method

	public void printBackSol() {
		/*
		 * Script with routes per instance
		 */
		try {
			double alpha = 0;
			if (this.Back_Strategy.getBack_Sol() != null) {
				if (this.Back_Strategy.getaTest().getOptcriterion() == 1001) {
					alpha = this.Back_Strategy.getaTest().getOptcriterion();
				}
				String file_name = new String(this.Back_Strategy.getaTest().getInstanceName() + "_SOLUTION_"
						+ "_p(disruption)_" + this.Back_Strategy.getaTest().getpercentangeDisruption() + "_seed_"
						+ this.Back_Strategy.getaTest().getseed() + "_Strategy_BackandForward_OptCriterion_"
						+ this.Back_Strategy.getaTest().getOptcriterion() + "_Alpha_" + alpha + "_Output.txt");
				PrintWriter out = new PrintWriter(file_name);
				out.printf("*********************************\n");

				out.printf("*********************************\n");
				out.printf("*********************************\n");
				out.printf("      Back_Strategy___________" + this.Back_Strategy.getaTest().getpercentangeDisruption()
						+ "  " + this.Back_Strategy.getaTest().getOptcriterion());
				out.println("\n");
				out.printf("*********************************\n");
				out.printf("      Orientation Route    \n");
				out.println("\n");
				out.print(Back_Strategy.getBack_Sol().toString());
				out.println("\n");
				out.printf("*********************************\n");
				out.printf("      Victims     \n");
				out.println("\n");
				out.println("ID      State");
				for (Node v : Back_Strategy.getVictimList().values()) {
					if (!Back_Strategy.getConnectedNodestoRevealedRoadNetwork().containsKey(v.getId())) {
						out.print(v.getId() + "      Unreachable");
						out.println("\n");
					} else {
						out.print(v.getId() + "      reachable");
						out.println("\n");
					}
				}
				out.close();
			}

		} catch (IOException exception) {
			System.out.println("Error processing output file: " + exception);
		}
	}// end method

	public static void printSolST(ArrayList<Outputs> list) {
		try {
			FileWriter fileWriter = new FileWriter(
					"C:/Users/Lorena/Documents/wokspace_Java_BOKU/on-line_Algorithm/Outputs/ResumeSols_Jumping.txt",
					true);

			PrintWriter out = new PrintWriter(fileWriter);

			for (Outputs o : list) {

				out.println();
				out.printf("%s   %s ", o.Jumping_Strategy.getaTest().getInstanceName(), "Jumping");
				Locale.setDefault(Locale.US);
				double alpha = 0;
				if (o.Jumping_Strategy.getaTest().getOptcriterion() == 1001) {
					alpha = o.Jumping_Strategy.getaTest().getpercentageDistance();
				}

				out.printf(
						" %.0f         %.1f           %.1f           %.0f             %.3f           %.3f           %.3f           ",
						o.Jumping_Strategy.getaTest().getOptcriterion(),
						o.Jumping_Strategy.getaTest().getpercentangeDisruption(), alpha,
						(float) o.Jumping_Strategy.getaTest().getseed(),
						o.Jumping_Strategy.getJump_Sol().getTotalDistance(),
						o.Jumping_Strategy.getJump_Sol().getTotalTime(), o.Jumping_Strategy.getJump_Sol().getPCTime());

			}
			out.close();

		} catch (IOException exception) {
			System.out.println("Error processing output file: " + exception);
		}
	}// end method

	public static void printSolSTB(ArrayList<Outputs> list) {
		try {
			FileWriter fileWriter = new FileWriter(
					"C:/Users/Lorena/Documents/wokspace_Java_BOKU/on-line_Algorithm/Outputs/ResumeSols_BackandForward.txt",
					true);

			PrintWriter out = new PrintWriter(fileWriter);
			for (Outputs o : list) {
				out.println();
				out.printf("%s   %s ", o.Back_Strategy.getaTest().getInstanceName(), "Back_and_Forward");
				Locale.setDefault(Locale.US);
				double alpha = 0;
				if (o.Back_Strategy.getaTest().getOptcriterion() == 1001) {
					alpha = o.Back_Strategy.getaTest().getpercentageDistance();
				}
				out.printf(
						" %.0f         %.1f           %.1f           %.0f             %.3f           %.3f           %.3f           ",
						o.Back_Strategy.getaTest().getOptcriterion(),
						o.Back_Strategy.getaTest().getpercentangeDisruption(), alpha,
						(float) o.Back_Strategy.getaTest().getseed(), o.Back_Strategy.getBack_Sol().getTotalDistance(),
						o.Back_Strategy.getBack_Sol().getTotalTime(),
						(float) o.Back_Strategy.getBack_Sol().getPCTime());
			}
			out.close();

		} catch (IOException exception) {
			System.out.println("Error processing output file: " + exception);
		}
	}// end method

}
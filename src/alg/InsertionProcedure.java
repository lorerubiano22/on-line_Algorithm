package alg;

import java.util.ArrayList;

public class InsertionProcedure {
	Outputs outputsBack;
	Outputs outputsJump;
	Solution sol_exploration= new Solution();


	ArrayList<Outputs> outListBack = new ArrayList<Outputs>();
	ArrayList<Outputs> outListJump = new ArrayList<Outputs>();
	long start;
	double elapsed = 0;
	// ending point: operation time

	public InsertionProcedure(Test aTest, Disaster Event, UpdateRoadInformation reveledNetwork, Inputs inputs) {
		if (aTest.getMovementStrategy().equals("B")) {// Back and forward
			start = ElapsedTime.systemTime();
			BackandForwardMovement backforwardStrategy = new BackandForwardMovement(aTest, Event, reveledNetwork, inputs);
			elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
			backforwardStrategy.getBack_Sol().setPCTime(elapsed);
			outputsBack = new Outputs(inputs, aTest, backforwardStrategy);
			sol_exploration=backforwardStrategy.getBack_Sol();
			outputsBack.setBack(backforwardStrategy);
			outListBack.add(outputsBack);
			Outputs.printSolSTB(outListBack);

		}
		if (aTest.getMovementStrategy().equals("J")) {// Jumping movement
			start = ElapsedTime.systemTime();
			JumpingMovement jumpingStrategy = new JumpingMovement(aTest, Event, reveledNetwork, inputs);
			elapsed = ElapsedTime.calcElapsed(start, ElapsedTime.systemTime());
			jumpingStrategy.getJump_Sol().setPCTime(elapsed);
			outputsJump = new Outputs(inputs, aTest, jumpingStrategy);
			sol_exploration=jumpingStrategy.getJump_Sol();
			outputsJump.setJumping(jumpingStrategy);
			outListJump.add(outputsJump);
			Outputs.printSolST(outListJump);
		}

	}


	public Solution getSol_exploration() {
		return sol_exploration;
	}

}

package alg;

public class Test {
	/* INSTANCE FIELDS AND CONSTRUCTOR */
	private String instanceName;
	private String movement;
	private float percentageDistance; // period between events
	private float percentageDisruption; // p(x) disruption
	private long seed; // Seed value for the Random Number Generator (RNG)
	private double criterion;// 1100 connectivity // 1010 dist // 1001 weighted criterion
	private double drivingRange; // driving Range of the UAV Distance
	private float victimNodes; // driving Range of the UAV Distance
	private float staticScoreVictims; // static score- victim nodes
	private float staticScoreDMC; // static score- DMC node
	private float staticScoreRoadCrossing; // static score- DMC node


	public Test(String name, String m, float t, float disrup, double criterion, long s, double drivingRange,
			float vicitm,float ssdmc, float ssv, float ssr ) {
		instanceName = name;
		movement = m;
		percentageDistance = t; // weight for the distance criterion
		percentageDisruption = disrup;
		seed = s;
		victimNodes = vicitm;
		this.criterion = criterion;
		this.drivingRange = drivingRange;
		staticScoreVictims=ssv; // static score- victim nodes
		staticScoreDMC=ssdmc; // static score- DMC node
		staticScoreRoadCrossing=ssr; // static score- DMC node
	}

	/* GET METHODS */

	public String getMovementStrategy() {
		return movement;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public float getpercentageDistance() {
		return percentageDistance;
	} // the time is giving in minutes and here is becoming in hours

	public float getpercentangeDisruption() {
		return percentageDisruption;
	}

	public float getVictimNodesPercentage() {
		return victimNodes;
	}

	public long getseed() {
		return seed;
	}

	public double getdrivingRange() {
		return drivingRange;
	}

	public double getOptcriterion() {
		return criterion;
	}

	public float getSSDMC() {
		return staticScoreDMC;
	}

	public float getSSV() {
		return staticScoreVictims;
	}
	public float getRC() {
		return staticScoreRoadCrossing;
	}

}